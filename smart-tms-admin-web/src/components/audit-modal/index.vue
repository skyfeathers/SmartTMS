<template>
  <a-modal v-model:open="visible" :width="700" title="审核" @close="onClose">
    <a-form ref="formRef" :model="form" :rules="rules">
      <a-form-item label="审核结果" name="auditStatus">
        <a-radio-group v-model:value="form.auditStatus" name="radioGroup">
          <a-radio :value="AUDIT_STATUS_ENUM.AUDIT_PASS.value">审核通过</a-radio>
          <a-radio :value="AUDIT_STATUS_ENUM.REJECT.value">审核驳回</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="审核备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="4" placeholder="请输入备注"/>
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button @click="onClose">取消</a-button>
      <a-button type="primary" @click="onSubmit">确定</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import {ref} from "vue";
import {message} from "ant-design-vue";
import {AUDIT_STATUS_ENUM} from "/@/constants/common-const";
import {useSpinStore} from "/@/store/modules/system/spin";

defineExpose({
  showModal
})

const emit = defineEmits("refresh");
// form组件与参数
const formRef = ref();
const formDefault = {
  auditStatus: undefined,
  remark: "",
};
let form = ref({...formDefault});
const rules = {
  auditStatus: [{required: true, message: "审核结果不能为空"}],
};
// 是否展示与方法
const visible = ref(false);
let auditHandle = null;

function showModal(auditForm, auditFunction) {
  form.value = auditForm;
  auditHandle = auditFunction
  visible.value = true;
}

// 操作
function onSubmit() {
  formRef.value
      .validate()
      .then(async () => {
        if (!auditHandle) {
          message.error("未设置审批方法");
          return;
        }
        if (AUDIT_STATUS_ENUM.REJECT.value === form.value.auditStatus && !form.value.remark) {
          message.error("请输入审核备注");
          return;
        }
        try {
          useSpinStore().show();
          await auditHandle(form.value);
          message.success('操作成功');
          onClose();
          emit("refresh");
        } catch (e) {
          console.log(e);
        } finally {
          useSpinStore().hide();
        }
      })
      .catch((error) => {
        console.log("error", error);
        message.error("参数验证错误，请仔细填写表单数据!");
      });
}

function onClose() {
  form.value = formDefault;
  visible.value = false;
}

</script>
<style lang='less' scoped></style>
