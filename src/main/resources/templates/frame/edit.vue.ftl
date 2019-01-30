<template>
    <div class='dialog-wrapper'>
        <div class='dialog-container' style='width:1166px;'>
            <!-- 头部 -->
            <title-contain :titleName="titleName" @TitleFun="cancel"></title-contain>

            <!-- 头部信息 -->
            <header>
                <div class='form-title'>{{$t('biz.title.mainInfo')}}</div>
                <el-form :inline="true" :model="listQuery" class="header-form-inline" label-position="left" :rules='rules' ref="refForm">
                    <el-row :gutter="0">
                        <el-col :span="8" v-for='(form, index) in formData' :key='index'>
                            <!-- 非日期 -->
                            <el-form-item :label="$t(form.label)" :prop='form.prop'>
                                <!-- 字典码表 -->
                                <el-select v-if='form.list' v-model="listQuery[form.prop]" v-bind='form.attrs' :placeholder="$t('biz.placeholder.choose')">
                                    <el-option v-for="item in form.list" :key="item.value" :label="item.label" :value="item.value"></el-option>
                                </el-select>
                                <!-- 公用组件 -->
                                <component v-else :is='form.element' v-model='listQuery[form.prop]' v-bind='form.attrs' v-on='form.event'></component>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </header>

            <!-- 底部按钮 -->
            <div class="dialog-footer">
        <span v-for='(button, index) in footerButtons' v-if='button.isShow()' :key='index'>
          <!-- 左侧按钮 -->
          <div @click='triggerEvent(button.event)' v-if='button.align === "left"' style='float:left;margin-left:16px;' :title="button.name">
            <svg-icon className='svg-fun-bottom' :icon-class="button.iconName"></svg-icon>
          </div>
            <!-- 中间按钮 -->
          <el-button v-else v-db-click v-bind='button.attrs' @click='triggerEvent(button.event)' :style="{'margin-left': index > 0 ? '10px' : '0'}">{{ button.name }}</el-button>
        </span>
            </div>
        </div>

        <div class="mask"></div>
    </div>
</template>

<script>
    import request from '@/utils/frame/base/request'
    import { confirmTip } from '@/utils/frame/base/notifyParams'
    import qmEdit from './qmEdit'

    export default {
        data() {
            return {
                listQuery: {},
                rules: {},
                apiList: {
                    getData: '/api/spot/frameProtocol/list'
                },

                formData: [
                    //     {
                    //     label: 'spot.frameProtocol.edit.protocolCode',
                    //     prop: 'protocolCode',
                    //     element: 'input-validate',
                    //     validate: [{
                    //         required: true,
                    //         trigger: 'blur',
                    //         message: this.$t('validateMsg.notBlank')
                    //     }]
                    // }, {
                    //     label: 'spot.frameProtocol.edit.protocolNo',
                    //     prop: 'protocolNo',
                    //     element: 'input-validate',
                    //     validate: [{
                    //         required: true,
                    //         trigger: 'blur',
                    //         message: this.$t('validateMsg.notBlank')
                    //     }]
                    // }, {
                    //     label: 'spot.frameProtocol.edit.signDate',
                    //     prop: 'signDate',
                    //     element: 'el-date-picker',
                    //     attrs: {
                    //         format: 'yyyy-MM-dd',
                    //         'value-format': 'yyyyMMdd'
                    //     },
                    //     validate: [{
                    //         required: true,
                    //         trigger: 'change',
                    //         message: this.$t('validateMsg.requireSelect')
                    //     }]
                    // }, {
                    //     label: 'spot.frameProtocol.edit.tradeType',
                    //     prop: 'tradeType',
                    //     default: '1',
                    //     list: this.$t('datadict.tradeType'),
                    //     attrs: {
                    //         clearable: true,
                    //         filterable: true,
                    //         'picker-options': {
                    //             disabledDate(time) {
                    //                 return time.getTime() > new Date().getTime()
                    //             }
                    //         }
                    //     },
                    //     validate: [{
                    //         required: true,
                    //         trigger: 'change',
                    //         message: this.$t('validateMsg.requireSelect')
                    //     }]
                    // }
                ],

                footerButtons: [{
                    align: 'left',
                    name: this.$t('biz.btn.originView'),
                    iconName: '原始信息',
                    event: this.originalProtocol,
                    isShow() {
                        return true
                    }
                }, {
                    name: this.$t('biz.btn.close'),
                    event: 'close',
                    isShow() {
                        return this.type==='view' || this.type==='originView'
                    }
                }, {
                    name: this.$t('biz.btn.cancel'),
                    event: 'cancel',
                    isShow() {
                        return this.type!=='view' && this.type!=='originView'
                    }
                }, {
                    name: this.$t('biz.btn.saveDraft'),
                    event: this.saveEdit,
                    isShow() {
                        return this.type!=='view' && this.type!=='originView'
                    }
                }, {
                    name: this.$t('biz.btn.saveAndSubmit'),
                    event: this.saveSubmitEdit,
                    attrs: {
                        type: 'primary'
                    },
                    isShow() {
                        return this.type!=='view' && this.type!=='originView'
                    }
                }]
            }
        },
        props: {
            type: {
                type: String,
                default: ''
            },
            id: {
                type: String,
                default: ''
            },
            isChange: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            titleName() {
                return this.$t(`biz.btn.${this.type}`)
            }
        },
        components: {
            qmEdit
        },
        beforeMount() {
            // 初始化数据
            this.formData.forEach(v => {
                const val = v.default || ''
                this.$set(this.listQuery, v.prop, val)
        })
            // 校验规则
            this.formData.forEach(v => {
                this.rules[v.prop] = v.validate
        })
            if (this.type === 'view') {
                this.rules = {}
            }
        },
        mounted() {
            if (this.id) this.getEditData()
        },
        methods: {
            closeDialog() {
                this.$emit('closeDialog')
            },

            // 保存
            saveEdit() {
                // element验证参数
                this.$refs.qmEdit.$refs['refForm'].validate(valid => {
                    if (valid) {
                        confirmTip(this, 'confirmSave')
                                .then(() => {
                            this.$emit('closeDialog', true)
                    })
                    .catch(() => { })
                    } else {
                        return false
                    }
                })
            },

            // 保存并提交
            saveSubmitEdit() {
                // element验证参数
                this.$refs.qmEdit.$refs['refForm'].validate(valid => {
                    if (valid) {
                        confirmTip(this, 'confirmSaveAndSubmit')
                                .then(() => {
                            this.$emit('closeDialog', true)
                    })
                    .catch(() => { })
                    } else {
                        return false
                    }
                })
            },
            originalProtocol() {
                this.$emit('typeChange', 'originView')
            },
            // 获取初始化数据
            getEditData() {
                request({
                    url: this.api.getData,
                    method: 'POST',
                    data: this.id
                })
                        .then(response => {

                })
            .catch(() => { })
            },

            // 按钮事件
            triggerEvent(event) {
                if (typeof event === 'function') {
                    event()
                } else if (typeof event === 'string') {
                    this[event]()
                }
            },

            // 取消通用事件
            cancel() {
                confirmTip(this, 'confirmCancel')
                        .then(() => {
                    this.$emit('closeDialog')
            })
            .catch(() => { })
            },

            // 关闭通用事件
            close() {
                this.$emit('closeDialog')
            }
        }
    }
</script>
