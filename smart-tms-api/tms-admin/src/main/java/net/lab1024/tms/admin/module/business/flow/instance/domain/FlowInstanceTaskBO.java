package net.lab1024.tms.admin.module.business.flow.instance.domain;

import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskEntity;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/16 16:59
 */
@Data
public class FlowInstanceTaskBO {

   /**
    * 上一个任务节点
    */
   private FlowInstanceTaskBO preTask;

   /**
    * 当前任务节点
    */
   private FlowInstanceTaskEntity instanceTask;

   /**
    * 下一个任务节点
    */
   private FlowInstanceTaskBO nextTask;
}
