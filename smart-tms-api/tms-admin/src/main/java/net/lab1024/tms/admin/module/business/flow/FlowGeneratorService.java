package net.lab1024.tms.admin.module.business.flow;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.flow.config.FlowGatewayConfigBO;
import net.lab1024.tms.admin.module.business.flow.config.FlowHandlerConfigBO;
import net.lab1024.tms.admin.module.business.flow.constant.FlowHandlerTypeEnum;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskGatewayTypeEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 17:47
 */
public class FlowGeneratorService {


    public static void main(String[] args) {
        FlowHandlerConfigBO flowHandlerConfigBO = new FlowHandlerConfigBO();
        flowHandlerConfigBO.setHandlerType(FlowHandlerTypeEnum.ASSIGN_USER.getValue());
        flowHandlerConfigBO.setConfigIdList(Lists.newArrayList(2L));
        //System.out.printf(JSON.toJSONString(flowHandlerConfigBO));


        FlowGatewayConfigBO jsGatewayConfigBO = new FlowGatewayConfigBO();
        jsGatewayConfigBO.setGatewayType(FlowTaskGatewayTypeEnum.JS.getValue());
        jsGatewayConfigBO.setScript("businessData.amount>100");

        //System.out.printf(JSON.toJSONString(jsGatewayConfigBO));



        FlowGatewayConfigBO spelGatewayConfigBO = new FlowGatewayConfigBO();
        spelGatewayConfigBO.setGatewayType(FlowTaskGatewayTypeEnum.SPEL.getValue());
        spelGatewayConfigBO.setScript("#{businessData.amount<=100}");

        System.out.printf(JSON.toJSONString(spelGatewayConfigBO));
    }
}
