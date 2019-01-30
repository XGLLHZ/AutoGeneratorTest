<template>
    <div class="app-container calendar-list-container">
        <header :class="{'showStyle': status}" :style="{'width': clientWidth < 1366 ? (this.sidebar.opened ? '1146px' : '1306px') : 'auto'}">
            <div class='form-title'>
                {{this.$t('biz.btn.search')}}
                <div class="form-title-right">
                    <div @click="resetForm">_ {{this.$t('biz.btn.clear')}}</div>
                    <div v-if="!status" @click="status = !status">
                        <i class="el-icon-caret-bottom"></i>{{this.$t('biz.btn.expand')}}
                    </div>
                    <div v-else @click="status = !status">
                        <i class="el-icon-caret-top"></i>{{this.$t('biz.btn.contract')}}
                    </div>
                </div>
            </div>
            <!-- form的版面 -->
            <el-form
                    ref='queryForm'
                    label-position="left"
                    :inline="true"
                    :model="listQuery.data"
                    :class="['header-form-inline', {'header-form-inline-hide': !status}]">
                <el-row :gutter="0">
                    <el-col :span="7" v-for='(form, index) in status ? formData : formData.slice(0, 3)' :key='index'>
                        <!-- 日期 -->
                        <el-form-item v-if='form.type === "date"' :label="$t(form.label)">
                            <el-col :span="11">
                                <el-date-picker v-model="listQuery.data[form.props[0]]" v-bind='form.attrs' @change="changeStartTime" :picker-options='dateStartBefore' :type="form.type" :placeholder="$t('biz.placeholder.dateInput')">
                                </el-date-picker>
                            </el-col>
                            <el-col :span="2" align='center'>~</el-col>
                            <el-col :span="11">
                                <el-date-picker v-model="listQuery.data[form.props[1]]" v-bind='form.attrs' @change="changeEndTime" :picker-options='dateEndBefore' :type="form.type" :placeholder="$t('biz.placeholder.dateInput')">
                                </el-date-picker>
                            </el-col>
                        </el-form-item>
                        <!-- 非日期 -->
                        <el-form-item v-else :label="$t(form.label)" :prop='form.prop'>
                            <!-- 字典码表 -->
                            <el-select v-if='form.list' v-model="listQuery.data[form.prop]" v-bind='form.attrs' :placeholder="$t('biz.placeholder.choose')">
                                <el-option v-for="item in form.list" :key="item.value" :label="item.label" :value="item.value"></el-option>
                            </el-select>
                            <!-- 公用组件 -->
                            <component v-else :is='form.element' v-model='listQuery.data[form.prop]' v-bind='form.attrs' v-on='form.event'></component>
                        </el-form-item>
                    </el-col>
                </el-row>
                <!-- 右侧搜索按钮 -->
                <div class="search-btn" v-permission="['query']">
                    <el-button type="primary" @click="onSubmit" v-db-click size="medium">
                        {{$t('biz.lbl.search')}}
                    </el-button>
                </div>
            </el-form>
        </header>
        <!-- table必须包上v-if清除缓存 防止切换tab速度过慢 -->
        <template v-if='$route.name === "tmp"'>
            <!-- 下面的主题内容 各个弹出框 -->
            <main :class="{'activeTop': status}" :style="{'width': clientWidth < 1366 ? (this.sidebar.opened ? '1146px' : '1306px') : 'auto'}">
                <!-- 顶部按钮 -->
                <div class="keyBtn">
                    <el-row type='flex'>
                        <slot name='add'></slot>
                        <div v-for='(btn, index) in mainData.operation_top' :key='index'>
                            <el-button v-db-click size="mini" @click='baseEvent[btn.name].func()' v-permission="[btn.name]" :disabled='isDisabled(btn)' style='margin-right:10px;'>
                                <svg-icon :icon-class="baseEvent[btn.name].icon"></svg-icon>{{$t(baseEvent[btn.name].i18n)}}
                            </el-button>
                        </div>
                    </el-row>
                </div>
                <!-- 列表 -->
                <el-table
                        stripe border
                        highlight-current-row
                        v-loading="loading"
                        :element-loading-text="$t('route.load')"
                        element-loading-spinner="el-icon-loading"
                        class='table-content' ref="singleTable"
                        :data="tableData" :height='tableHeight'
                        @current-change="handleSelectRow"
                        :empty-text='emptyText'>
                    <el-table-column align='center' type="index" fixed="left" width="50" :label='$t("table.id")'></el-table-column>
                    <el-table-column v-for='(col, index) in mainData.table.cols' :key='index' v-bind='col' :label='$t(col.label)' :prop='col.prop'>
                        <template slot-scope='scope'>
                            <span v-if='!col.format'>{{ scope.row[col.prop] }}</span>
                            <span v-else>{{ dataFormat(col.format.func, scope.row[col.prop], col.format.dict) }}</span>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- 底部按钮 -->
                <div class='mainContainer'>
                    <div class="mainLeft">
                        <div v-for='(i, index) in mainData.operation_bottom.left' :title="$t(baseEvent[i.name].i18n)" :key='index' @click='baseEvent[i.name].func && baseEvent[i.name].func()'>
                            <svg-icon className='svg-fun-bottom' :icon-class="baseEvent[i.name].icon"></svg-icon>
                        </div>
                    </div>
                    <!-- 分页 -->
                    <el-pagination v-if='mainData.operation_bottom.pagination.show' background class="PageTions" :layout="mainData.operation_bottom.pagination.layout" :current-page="listQuery.current" :page-sizes="mainData.operation_bottom.pagination.pageSizes" :page-size="listQuery.size" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange">
                    </el-pagination>
                </div>
                <!-- 提交审批 -->
                <next-user-common-dialog-form v-if='nextUserDialogVisible' :isShow='true' :taskId='taskId' @closeHandler='closeDialogHandler'></next-user-common-dialog-form>
                <!-- 编辑页 -->
                <edit-tmp v-el-drag-dialog v-if='editVisible' :type='dialogType' :taskId='taskId' :id='dialogId' :originalId='originalId' :isChange='isChange' @closeDialog='closeDialog' @typeChange='typeChangeHandle'></edit-tmp>
            </main>
        </template>
    </div>
</template>

<script>
    import Vue from 'vue'
    import { mapGetters } from 'vuex'
    import { notifyInfo, notifySuccess, confirmDelete, confirmClose } from '@/utils/frame/base/notifyParams'
    import { dateFormate } from '@/utils/frame/base/index'
    import request from '@/utils/frame/base/request'
    import elementFun from '@/utils/frame/base/elementFun'
    import exportExcel from '@/utils/frame/base/downloadExcel'
    import nextUserCommonDialogForm from '@/views/frame/bpm/components/nextUserDialog'
    import editTmp from './edit'
    import qmForm from '@/views/example/template/qmForm.vue'
    import qmTable from '@/views/example/template/qmTable.vue'

    export default {
        name: 'tmp',
        data() {
            return {
                apiList: {
                    getList: '',
                    doDelete: '',
                    doClose: '',
                    doSubmit: ''
                    //getList: '/api/spot/frameProtocol/list'
                },
                status: false,
                dateStartBefore: {},
                dateEndBefore: {},
                loading: false,
                total: 0,
                emptyText: ' ',
                nextUserDialogVisible: false,
                editVisible: false,
                dialogType: '',
                taskId: '',
                dialogId: '',
                originalId: '',
                isChange: false,
                currentRow: null,
                tableData: [],
                totalData: [],
                baseEvent: {
                    add: {
                        func: this.doAdd,
                        icon: '新增',
                        i18n: 'biz.btn.add'
                    },
                    view: {
                        func: this.doView,
                        icon: '查看',
                        i18n: 'biz.btn.view'
                    },
                    edit: {
                        func: this.doUpdate,
                        icon: '修改',
                        i18n: 'biz.btn.update'
                    },
                    remove: {
                        func: this.doDelete,
                        icon: '删除',
                        i18n: 'biz.btn.delete'
                    },
                    close: {
                        func: this.doClose,
                        icon: '关闭',
                        i18n: 'biz.btn.close'
                    },
                    submit: {
                        func: this.doSubmit,
                        icon: '提交',
                        i18n: 'biz.btn.submitApproval'
                    },
                    change: {
                        func: this.doChange,
                        icon: '变更',
                        i18n: 'biz.btn.change'
                    },
                    refresh: {
                        func: this.getList,
                        icon: '线性-刷新',
                        i18n: 'biz.btn.refresh'
                    },
                    export: {
                        func: this.handleDownload,
                        icon: '线性-导出',
                        i18n: 'biz.btn.export'
                    },
                    print: {
                        icon: '线性-打印',
                        i18n: 'biz.btn.print'
                    }
                },

                listQuery: {
                    current: 1,
                    size: 20,
                    isPage: true,
                    sorString: '',
                    funcModule: this.$t('route.frameProtocol'),
                    funcOperation: this.$t('biz.btn.search'),
                    data: { }
                },

                formData: [
                    // {
                    //     label: 'spot.frameProtocol.protocolCode',
                    //     prop: 'protocolCode',
                    //     element: 'input-validate'
                    // }, {
                    //     label: 'spot.frameProtocol.titleId',
                    //     prop: 'titleId',
                    //     element: 'tree-organ',
                    //     component: require('@/views/frame/base/fun/components/TreeOrgan'),
                    //     attrs: {
                    //         clickParent: true,
                    //         multiple: false
                    //     }
                    // }, {
                    //     label: 'spot.frameProtocol.customerId',
                    //     prop: 'customerId',
                    //     element: 'base-select',
                    //     attrs: {
                    //         queryData: {
                    //             customerStat: '3',
                    //             type: 'customer'
                    //         }
                    //     },
                    //     event: {
                    //         change: this.customerChange
                    //     }
                    // }, {
                    //     label: 'spot.frameProtocol.customerTitleId',
                    //     prop: 'customerTitleId',
                    //     element: 'base-select',
                    //     attrs: {
                    //         queryData: {
                    //             type: 'customerTitle'
                    //         }
                    //     }
                    // }, {
                    //     label: 'spot.frameProtocol.productId',
                    //     prop: 'productId',
                    //     element: 'base-select',
                    //     attrs: {
                    //         queryData: {
                    //             type: 'product'
                    //         }
                    //     }
                    // }, {
                    //     label: 'spot.frameProtocol.protocolStat',
                    //     prop: 'protocolStat',
                    //     list: this.$t('datadict.protocolStat'),
                    //     attrs: {
                    //         clearable: true,
                    //         filterable: true
                    //     }
                    // }, {
                    //     label: 'spot.frameProtocol.protocolNo',
                    //     prop: 'protocolNo',
                    //     element: 'input-validate'
                    // }, {
                    //     label: 'spot.frameProtocol.businessName',
                    //     prop: 'businessName',
                    //     element: 'input-validate'
                    // }, {
                    //     label: 'spot.frameProtocol.serviceManagerId',
                    //     prop: 'serviceManagerId',
                    //     element: 'tree-staff',
                    //     component: require('@/views/frame/base/fun/components/TreeStaff'),
                    //     attrs: {
                    //         jurisdiction: true,
                    //         valueConsistsOf: 'LEAF_PRIORITY'
                    //     }
                    // }, {
                    //     label: 'spot.frameProtocol.documentNo',
                    //     prop: 'documentNo',
                    //     element: 'input-validate'
                    // }, {
                    //     type: 'date',
                    //     label: 'spot.frameProtocol.signDate',
                    //     props: ['signDateFrom', 'signDateTo'],
                    //     attrs: {
                    //         format: 'yyyy-MM-dd',
                    //         'value-format': 'yyyyMMdd'
                    //     }
                    // }
                ],

                mainData: {
                    operation_top: [{
                        name: 'add'
                    }, {
                        $refs: this.$refs,
                        name: 'remove',
                        validate() {
                            return this.$refs.qmTable && this.$refs.qmTable.currentRow ? this.$refs.qmTable.currentRow.protocolStat === '1' : true
                        }
                    }, {
                        name: 'close',
                        isShow: {
                            label: 'protocolStat',
                            value: ['3']
                        }
                    }, {
                        name: 'view'
                    }, {
                        name: 'edit',
                        isShow: {
                            label: 'protocolStat',
                            value: ['1']
                        }
                    }, {
                        name: 'submit',
                        isShow: {
                            label: 'protocolStat',
                            value: ['1']
                        }
                    }, {
                        name: 'change',
                        isShow: {
                            label: 'protocolStat',
                            value: ['3']
                        }
                    }],

                    table: {
                        cols: [
                            // {
                            //     prop: 'protocolStat',
                            //     'min-width': '100',
                            //     align: 'center',
                            //     label: 'spot.frameProtocol.protocolStat',
                            //     format: {
                            //         func: 'dataDictFormat',
                            //         dict: this.$t('datadict.protocolStat')
                            //     }
                            // }, {
                            //     prop: 'titleName',
                            //     'min-width': '120',
                            //     label: 'spot.frameProtocol.titleName'
                            // }, {
                            //     prop: 'protocolCode',
                            //     'min-width': '180',
                            //     label: 'spot.frameProtocol.protocolCode'
                            // }, {
                            //     prop: 'customerName',
                            //     'min-width': '120',
                            //     label: 'spot.frameProtocol.customerName'
                            // }, {
                            //     prop: 'customerTitleName',
                            //     'min-width': '160',
                            //     label: 'spot.frameProtocol.customerTitleName'
                            // }, {
                            //     prop: 'protocolNo',
                            //     'min-width': '180',
                            //     label: 'spot.frameProtocol.protocolNo'
                            // }, {
                            //     prop: 'protocolStartDate',
                            //     'min-width': '120',
                            //     align: 'center',
                            //     label: 'spot.frameProtocol.protocolStartDate',
                            //     format: {
                            //         func: 'dateFormat'
                            //     }
                            // }, {
                            //     prop: 'protocolEndDate',
                            //     'min-width': '120',
                            //     align: 'center',
                            //     label: 'spot.frameProtocol.protocolEndDate',
                            //     format: {
                            //         func: 'dateFormat'
                            //     }
                            // }, {
                            //     prop: 'tradeType',
                            //     'min-width': '100',
                            //     label: 'spot.frameProtocol.tradeType',
                            //     format: {
                            //         func: 'dataDictFormat',
                            //         dict: this.$t('datadict.tradeType')
                            //     }
                            // }, {
                            //     prop: 'quantity',
                            //     'min-width': '140',
                            //     align: 'right',
                            //     label: 'spot.frameProtocol.quantity',
                            //     format: {
                            //         func: 'numFilter',
                            //         dict: 3
                            //     }
                            // }, {
                            //     prop: 'currencyName',
                            //     'min-width': '90',
                            //     label: 'spot.frameProtocol.currencyName'
                            // }, {
                            //     prop: 'settlementBasis',
                            //     'min-width': '100',
                            //     label: 'spot.frameProtocol.settlementBasis',
                            //     format: {
                            //         func: 'dataDictFormat',
                            //         dict: this.$t('datadict.settlementBasis')
                            //     }
                            // }, {
                            //     prop: 'settlementMethod',
                            //     'min-width': '100',
                            //     label: 'spot.frameProtocol.settlementMethod',
                            //     format: {
                            //         func: 'dataDictFormat',
                            //         dict: this.$t('datadict.settlementMethod')
                            //     }
                            // }, {
                            //     prop: 'invoiceAgreement',
                            //     'min-width': '100',
                            //     label: 'spot.frameProtocol.invoiceAgreement',
                            //     format: {
                            //         func: 'dataDictFormat',
                            //         dict: this.$t('datadict.invoiceAgreement')
                            //     }
                            // }, {
                            //     prop: 'serviceManagerName',
                            //     'min-width': '120',
                            //     label: 'spot.frameProtocol.serviceManagerName'
                            // }, {
                            //     prop: 'signDate',
                            //     'min-width': '100',
                            //     align: 'center',
                            //     label: 'spot.frameProtocol.signDate',
                            //     format: {
                            //         func: 'dateFormat'
                            //     }
                            // }, {
                            //     prop: 'businessName',
                            //     'min-width': '100',
                            //     label: 'spot.frameProtocol.businessName'
                            // }, {
                            //     prop: 'makerDate',
                            //     'min-width': '100',
                            //     align: 'center',
                            //     label: 'spot.frameProtocol.makerDate',
                            //     format: {
                            //         func: 'dateFormat'
                            //     }
                            // }, {
                            //     prop: 'makerUserName',
                            //     'min-width': '140',
                            //     label: 'spot.frameProtocol.makerUserName'
                            // }, {
                            //     prop: 'changeDate',
                            //     'min-width': '100',
                            //     align: 'center',
                            //     label: 'spot.frameProtocol.changeDate',
                            //     format: {
                            //         func: 'dateFormat'
                            //     }
                            // }, {
                            //     prop: 'changeUserName',
                            //     'min-width': '100',
                            //     label: 'spot.frameProtocol.changeUserName'
                            // }, {
                            //     prop: 'closeDate',
                            //     'min-width': '100',
                            //     align: 'center',
                            //     label: 'spot.frameProtocol.closeDate',
                            //     format: {
                            //         func: 'dateFormat'
                            //     }
                            // }, {
                            //     prop: 'closeUserName',
                            //     'min-width': '100',
                            //     label: 'spot.frameProtocol.closeUserName'
                            // }
                        ]
                    },

                    operation_bottom: {
                        left: [{
                            name: 'refresh'
                        }, {
                            name: 'export',
                            fileName: this.$t('spot.frameProtocol.title')
                        }, {
                            name: 'print'
                        }],
                        pagination: {
                            show: true,
                            layout: 'total, sizes, prev, pager, next, jumper',
                            pageSizes: [20, 40, 60, 80, 100]
                        }
                    }
                }
            }
        },
        components: {
            nextUserCommonDialogForm,
            editTmp
        },
        computed: {
                ...mapGetters(['sidebar', 'clientWidth', 'clientHeight']),
        tableHeight() {
            return this.clientWidth > 1355 ? this.clientHeight - 290 : this.clientHeight - 310 // 防止底部滚动条遮挡
        },
        beforeMount() {
            registerComponent(this.formData)
        },
        status() {
            return this.$parent.$refs.qmForm.status
        }
    },
    mounted() {
        // 列表懒加载
        // $('.el-table__body-wrapper').css('overflow-y', 'auto').append(`<div class="blank-box" style="height:${560 * (this.listQuery.size / 20 - 1)}px;"></div>`)
        // $('.el-table__body-wrapper').scroll(() => {
        //   if (this.tableData.length >= this.listQuery.size) return
        //   const scrollT = $('.el-table__body-wrapper').scrollTop()
        //   if (scrollT > 0 && scrollT < 560) {
        //     $('.el-table__body-wrapper>div').css('height', `${560 * (this.listQuery.size / 20 - 2)}px`)
        //     this.tableData = this.totalData.slice(0, 40)
        //   } else if (scrollT > 560 && scrollT < 1120) {
        //     $('.el-table__body-wrapper>div').css('height', `${560 * (this.listQuery.size / 20 - 3)}px`)
        //     this.tableData = this.totalData.slice(0, 60)
        //   } else if (scrollT > 1120 && scrollT < 1680) {
        //     $('.el-table__body-wrapper>div').css('height', `${560 * (this.listQuery.size / 20 - 4)}px`)
        //     this.tableData = this.totalData.slice(0, 80)
        //   } else if (scrollT > 1680) {
        //     $('.el-table__body-wrapper>div').css('height', `${560 * (this.listQuery.size / 20 - 5)}px`)
        //     this.tableData = this.totalData.slice(0, 100)
        //   }
        // })

        this.getList()
    },
    methods: {
        // 客户名称改变
        customerChange() {
            this.listQuery.data.customerTitleId = ''
        },
        // 重置查询表单
        resetForm() {
            if (this.$refs.queryForm) {
                for (const i in this.listQuery.data) {
                    this.listQuery.data[i] = ''
                }
                this.$parent.$refs.qmTable.getList()
            }
        },
        // 查询
        onSubmit() {
            this.$parent.$refs.qmTable.getList()
        },
        // 开始时间变化
        changeStartTime(date) {
            this.dateEndBefore = {
                disabledDate(time) {
                    return dateFormate(time, 'YYYYMMDD') * 1 < date * 1
                }
            }
        },
        // 开始时间变化
        changeEndTime(date) {
            this.dateStartBefore = {
                disabledDate(time) {
                    return date ? dateFormate(time, 'YYYYMMDD') * 1 > date * 1 : false
                }
            }
        },
    ...elementFun,
                getList() {
            this.loading = true
            request({
                url: this.api.getList,
                method: 'POST',
                data: this.listQuery
            })
                    .then(response => {
                this.loading = false
            // $('.el-table__body-wrapper').scrollTop(0)
            // $('.el-table__body-wrapper .blank-box').css('height', `${560 * (this.listQuery.size / 20 - 1)}px`)
            // this.totalData = response.data
            this.tableData = response.data
            this.total = response.total
            if (this.total === 0) this.emptyText = this.$t('table.emptyText')
        })
        .catch(() => {
                this.loading = false
            this.emptyText = this.$t('table.emptyText')
        })
        },
        // 返回指定过滤条件结果
        dataFormat(func, value, list) {
            return Vue.filter(func)(value, list)
        },
        // 关闭审批弹窗回调
        closeDialogHandler(isSubmit, value) {
            this.nextUserDialogVisible = false
            // 提交
            if (isSubmit) {
                const params = {
                    data: {
                        bpmVariableParamList: value,
                        taskId: this.taskId,
                        id: this.id
                    }
                }
                request({
                    url: this.api.doSubmit,
                    method: 'POST',
                    data: params
                })
                        .then(response => {
                    this.$notify(
                        notifySuccess({ msg: this.$t('biz.msg.submitSuccess') })
                )
                this.getList()
            })
            .catch(() => { })
            }
        },
        // 按钮是否可用
        isDisabled(btn) {
            return (btn.isShow && this.currentRow) ? !btn.isShow.value.includes(this.currentRow[btn.isShow.label]) : (btn.validate ? !btn.validate() : false)
        },
        // 新增
        doAdd() {
            this.editVisible = true
            this.dialogType = 'add'
        },
        // 删除
        doDelete() {
            if (!this.currentRow) {
                this.$notify(
                        notifyInfo({
                            msg: this.$t('biz.msg.noRowSelected')
                        })
                )
                return
            }
            confirmDelete(this)
                    .then(() => {
                this.loading = true
            request({
                url: this.api.doDelete,
                method: 'POST',
                data: [this.currentRow.id]
            })
                    .then(response => {
                this.$notify(notifySuccess({ msg: this.$t('biz.msg.deleteSuccess') }))
            this.loading = false
            this.getList()
        })
        .catch(() => {
                this.loading = false
        })
        })
        .catch(() => { })
        },
        // 关闭
        doClose() {
            if (!this.currentRow) {
                this.$notify(
                        notifyInfo({
                            msg: this.$t('biz.msg.noRowSelected')
                        })
                )
                return
            }
            confirmClose(this)
                    .then(() => {
                this.loading = true
            request({
                url: this.api.doClose,
                method: 'POST',
                data: [this.currentRow.id]
            })
                    .then(response => {
                this.$notify(notifySuccess({ msg: this.$t('biz.msg.closeSuccess') }))
            this.loading = false
            this.getList()
        })
        .catch(() => {
                this.loading = false
        })
        })
        .catch(() => { })
        },
        // 查看
        doView() {
            if (!this.currentRow) {
                this.$notify(
                        notifyInfo({
                            msg: this.$t('biz.msg.noRowSelected')
                        })
                )
                return
            }
            this.editVisible = true
            this.dialogType = 'view'
            this.taskId = this.currentRow.taskId
            this.dialogId = this.currentRow.id
        },
        // 更新
        doUpdate() {
            if (!this.currentRow) {
                this.$notify(
                        notifyInfo({
                            msg: this.$t('biz.msg.noRowSelected')
                        })
                )
                return
            }
            this.editVisible = true
            this.dialogType = 'edit'
            this.taskId = this.currentRow.taskId
            this.dialogId = this.currentRow.id
        },
        // 提交
        doSubmit() {
            if (!this.currentRow) {
                this.$notify(
                        notifyInfo({
                            msg: this.$t('biz.msg.noRowSelected')
                        })
                )
                return
            }
            this.id = this.currentRow.id
            this.taskId = this.currentRow.taskId
            this.nextUserDialogVisible = true
        },
        // 变更
        doChange() {
            if (!this.currentRow) {
                this.$notify(
                        notifyInfo({
                            msg: this.$t('biz.msg.noRowSelected')
                        })
                )
                return
            }
            this.isChange = true
            this.dialogType = 'change'
            this.originalId = this.currentRow.id
        },
        // 编辑关闭回调
        closeDialog(params) {
            this.editVisible = false
            if (params) {
                this.getList()
            }
        },
        // 编辑类型修改
        typeChangeHandle(newType) {
            this.type = newType
        },
        // 导出excel
        exportfunc() {
            this.loading = true
            const params = Object.assign({}, this.listQuery, {
                isPage: false
            })
            return request({
                url: this.api.getList,
                method: 'POST',
                data: params
            })
        },
        callBackfunc() {
            this.listQuery.isPage = true
            this.loading = false
        },
        handleDownload() {
            exportExcel({
                fileName: this.mainData.operation_bottom.left[1].fileName,
                header: this.mainData.table.cols.map(col => {
                    if (col.label) return this.$t(col.label)
        }),
            filterVal: this.mainData.table.cols.map(col => {
                if (col.format && col.format.dict instanceof Array) {
                return {
                    val: col.prop,
                    dicCode: col.format.dict
                }
            } else {
                return col.prop
            }
        }),
            exportfunc: this.exportfunc,
                    callBackfunc: this.callBackfunc
        })
        }
    }
}
</script>

