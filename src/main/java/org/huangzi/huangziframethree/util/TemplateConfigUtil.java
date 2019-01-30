package net.qmgf.frame.biz.generator.utils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author: guohao
 * @date: 2019/1/29/14:24
 * @description: 模板路径配置项
 */
@Data
@Accessors(chain = true)
public class TemplateConfigUtil {

    @Getter(AccessLevel.NONE)
    private String entity = ConstValUtil.TEMPLATE_ENTITY_JAVA;

    private String entityKt = ConstValUtil.TEMPLATE_ENTITY_KT;

    private String service = ConstValUtil.TEMPLATE_SERVICE;

    private String serviceImpl = ConstValUtil.TEMPLATE_SERVICE_IMPL;

    private String mapper = ConstValUtil.TEMPLATE_MAPPER;

    private String xml = ConstValUtil.TEMPLATE_XML;

    private String controller = ConstValUtil.TEMPLATE_CONTROLLER;

    private String index = ConstValUtil.TEMPLATE_INDEX;

    private String edit = ConstValUtil.TEMPLATE_EDIT;

    public String getEntity(boolean kotlin) {
        return kotlin ? entityKt : entity;
    }

}
