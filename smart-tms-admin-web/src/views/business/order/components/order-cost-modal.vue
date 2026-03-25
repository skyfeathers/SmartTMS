<!--
 * @Description: 订单编辑费用明细弹窗
 * @version:
 * @Author: lidoudou
 * @Date: 2022-08-01
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-08-01
-->
<template>
  <a-modal :open="visible" cancel-text="取消" ok-text="确定" title="费用明细" width="1000px" @ok="onSubmit"
           @cancel="onClose" :getContainer="getContainer">
      <OrderCostTable ref="orderCostRef" :orderId="form.orderId" :consignor="form.consignor" :readonlyFlag="readonlyFlag"/>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { orderApi } from '/@/api/business/order/order-api';
import _ from 'lodash';
import { getContainer } from '/@/utils/container-util';
import OrderCostTable from './order-cost-table.vue';

// ----------------------- 对外暴露 ------------------------
const emits = defineEmits(['refresh'])

defineExpose({
  showModal,
});

const props = defineProps({
  readonlyFlag: {
    type: Boolean,
    default: false
  }
})
// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal (orderId, consignor) {
  form.orderId = orderId;
  form.consignor = consignor;
  visible.value = true;
}

// ----------------------- 表单 ------------------------
const formDefault = {
  orderId: null,
};
let form = reactive({ ...formDefault });
// ----------------------- 提交数据 ------------------------
const orderCostRef = ref();
async function onSubmit () {
  if(props.readonlyFlag){
    onClose();
    return
  }
  try {
    SmartLoading.show();
    let params = Object.assign(form);
    params.costItemList = orderCostRef.value.tableData;
    await orderApi.updateCost(form);
    message.success('提交成功');
    emits('refresh');
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

// ----------------------- 查询数据 ------------------------

const columns = reactive([
  {
    title: '序号',
    dataIndex: 'index',
    slots: { customRender: 'index' }
  },
  {
    title: '费用名称',
    dataIndex: 'costItemName'
  },
  {
    title: '金额',
    dataIndex: 'costAmount',
    slots: { customRender: 'costAmount' }
  },
  {
    title: '费用类型',
    dataIndex: 'type',
    slots: { customRender: 'type' },
  },
  {
    title: '支付单位',
    dataIndex: 'payObject',
    slots: { customRender: 'payObject' }
  },
  {
    title: '支付对象',
    dataIndex: 'consignor',
    slots: { customRender: 'consignor' }
  }
]);
</script>
<style scoped lang="less">
</style>
