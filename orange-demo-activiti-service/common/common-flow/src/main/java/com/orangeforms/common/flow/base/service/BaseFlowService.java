package com.orangeforms.common.flow.base.service;

import com.alibaba.fastjson.JSONObject;
import com.orangeforms.common.core.base.service.BaseService;
import com.orangeforms.common.flow.constant.FlowApprovalType;
import com.orangeforms.common.flow.constant.FlowTaskStatus;
import com.orangeforms.common.flow.model.FlowTaskComment;
import com.orangeforms.common.flow.service.FlowApiService;
import com.orangeforms.common.flow.service.FlowWorkOrderService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public abstract class BaseFlowService<M, K extends Serializable> extends BaseService<M, K> {

    @Autowired
    private FlowApiService flowApiService;
    @Autowired
    private FlowWorkOrderService flowWorkOrderService;

    public void startWithBusinessKey(String processDefinitionId, K dataId) {
        ProcessInstance instance = flowApiService.start(processDefinitionId, dataId);
        flowWorkOrderService.saveNew(instance, dataId, null, super.tableName);
    }

    public void startAndTakeFirst(
            String processDefinitionId, K dataId, FlowTaskComment comment, JSONObject variables) {
        ProcessInstance instance = flowApiService.startAndTakeFirst(
                processDefinitionId, dataId, comment, variables);
        flowWorkOrderService.saveNew(instance, dataId, null, super.tableName);
    }

    public void takeFirstTask(
            String processInstanceId, String taskId, K dataId, FlowTaskComment comment, JSONObject variables) {
        Task task = flowApiService.getProcessInstanceActiveTask(processInstanceId, taskId);
        flowApiService.setBusinessKeyForProcessInstance(processInstanceId, dataId);
        flowApiService.completeTask(task, comment, variables);
        ProcessInstance instance = flowApiService.getProcessInstance(processInstanceId);
        flowWorkOrderService.saveNew(instance, dataId, null, super.tableName);
    }

    public void takeTask(Task task, K dataId, FlowTaskComment comment, JSONObject variables) {
        int flowStatus = FlowTaskStatus.APPROVING;
        if (comment.getApprovalType().equals(FlowApprovalType.REFUSE)) {
            flowStatus = FlowTaskStatus.REFUSED;
        }
        flowWorkOrderService.updateFlowStatusByProcessInstanceId(task.getProcessInstanceId(), flowStatus);
        flowApiService.completeTask(task, comment, variables);
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param processInstanceId ????????????Id???
     * @param businessKey       ????????????Id???????????????????????????????????????????????????????????????????????????????????????
     */
    public void doSyncBusinessData(String processInstanceId, String businessKey) {
        throw new UnsupportedOperationException();
    }
}
