<template>
  <a-modal :open="visible" cancel-text="取消" ok-text="保存" title="修改" @cancel="onClose" @ok="onSubmit">
    <a-form ref="formRef" :label-col="{ span: 6 }" :model="form" :rules="rules">
      <a-form-item label="类型" name="orderType">
        <smart-enum-select v-model:value="form.orderType" enumName="ORDER_TYPE_ENUM" placeholder="请选择类型"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, nextTick, computed } from 'vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import { message } from 'ant-design-vue';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { SmartLoading } from '/@/components/smart-loading';
import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits("reloadList");
defineExpose({
  showModal,
});

// -------------表单字段------------------
const formDefault = {
  waybillId: null,
  orderType: null
};

const form = reactive({ ...formDefault });
const formRef = ref();

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal ({ waybillId, orderType}) {
  Object.assign(form, { waybillId, orderType});
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

// ----------------------- 表单 ------------------------
const rules = {
  orderType: [{ required: true, message: '请选择类型' }]
};

// ----------------------- 提交表单 ------------------------
function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        // 类型未发生变化，不调用接口
        if (form.orderType == ORDER_TYPE_ENUM.NETWORK_FREIGHT.value) {
          return;
        }
        SmartLoading.show();
        try {
          await waybillApi.updateOrderType(form.waybillId);
          message.success('更新成功');
          emit('reloadList');
          onClose();
        } catch (error) {
          console.log(error);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
}
</script>
