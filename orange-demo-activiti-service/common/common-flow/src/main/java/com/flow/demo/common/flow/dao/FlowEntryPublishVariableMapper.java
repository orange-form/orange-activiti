package com.flow.demo.common.flow.dao;

import com.flow.demo.common.core.base.dao.BaseDaoMapper;
import com.flow.demo.common.flow.model.FlowEntryPublishVariable;

import java.util.List;

/**
 * 数据操作访问接口。
 *
 * @author Jerry
 * @date 2021-06-06
 */
public interface FlowEntryPublishVariableMapper extends BaseDaoMapper<FlowEntryPublishVariable> {

    /**
     * 批量插入流程发布的变量列表。
     *
     * @param entryPublishVariableList 流程发布的变量列表。
     */
    void insertList(List<FlowEntryPublishVariable> entryPublishVariableList);
}
