<template>
  <div>
    <a-modal :getContainer="getContainer" :open="visible" cancel-text="取消" ok-text="确定" title="修改货物" width="800px"
             @cancel="onClose" @ok="onSubmit">
      <a-table :columns="columns" :dataSource="form.goodsList" :pagination="false" bordered size="small" >
        <template #bodyCell="{ text, record, index, column }">
          
          <template v-if="column.dataIndex === 'goodsQuantity'">
            <a-input-number v-model:value="record.goodsQuantity" :min="0" :precision="2" placeholder="请输入总数量"
                            style="width: 100%"/>
          </template>
          <template v-if="column.dataIndex === 'goodsUnit'">
            {{$smartEnumPlugin.getDescByValue('GOODS_UNIT_TYPE_ENUM', record.goodsUnit)}}
          </template>
        </template>
      </a-table>
    </a-modal>
  </div>
</template>
<script setup>
import { reactive, ref } from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { getContainer } from '/@/utils/container-util';

import { orderApi } from '/@/api/business/order/order-api';

const emits = defineEmits(['refresh'])
// ----------------------- 显示/隐藏 ------------------------

const visible = ref(false);

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal (orderId) {
  form.orderId = orderId;
  getOrderDetail();
  visible.value = true;
}

// ----------------------- 表单 ------------------------
const formDefault = {
  orderId: null,
};
let form = reactive({ ...formDefault });

async function onSubmit () {
  try {
    useSpinStore().show();
    await orderApi.updateGoods(form);
    onClose();
    emits('refresh');
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}
// ----------------------- 获取详情 ------------------------
async function getOrderDetail () {
  if (!form.orderId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await orderApi.getDetail(form.orderId);
    const { orderId, goodsList } = data;
    Object.assign(form, { orderId, goodsList });
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}
// ----------------------- 列表展示 ------------------------

const columns = reactive([
  {
    title: '货物类型',
    dataIndex: 'goodsTypeName',
    fixed: 'left',
  },
  {
    title: '货物名称',
    dataIndex: 'goodsName',
  },
  {
    title: '货物单位',
    width: 80,
    dataIndex: 'goodsUnit',
  },
  {
    title: '货物量',
    dataIndex: 'goodsQuantity',
  }
]);

defineExpose({
  showModal
})
</script>

<style lang="css" scoped>
</style>
