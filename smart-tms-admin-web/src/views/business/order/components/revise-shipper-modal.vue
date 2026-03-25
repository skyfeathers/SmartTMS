<template>
  <a-modal :open="visible" cancel-text="取消" ok-text="保存" title="修改货主" @cancel="onClose" @ok="onSubmit">
    <a-form>
      <a-form-item class="query-item" label="货主">
        <ShipperSelect v-model:value="shipperId" placeholder="请选择货主"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, defineExpose, defineEmits } from 'vue';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import ShipperSelect from '/@/components/shipper-select/index.vue';
import { orderApi } from '/@/api/business/order/order-api';

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose () {
  visible.value = false;
}

const orderId = ref(null);
const shipperId = ref(null);

function showModal (id) {
  visible.value = true;
  orderId.value = id;
  shipperId.value = null;
}

// ----------------------- 提交表单 ------------------------
const emit = defineEmits('refresh');

async function onSubmit () {
  SmartLoading.show();
  try {
    await orderApi.updateShipper(orderId.value, shipperId.value);
    message.success('更新成功');
    emit('refresh');
    shipperId.value = null;

    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

defineExpose({
  showModal,
});
</script>
<style lang="less" scoped>
.query-item {
  width: 240px;
  margin-left: 20px;
  margin-top: 20px;
}
</style>
