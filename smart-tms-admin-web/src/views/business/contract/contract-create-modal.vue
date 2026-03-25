<template>
  <a-modal v-model:open="visible" :width="600" :title="form.contractId ? '编辑合同' : '创建合同'" @close="onClose" :maskClosable="false"
           :getContainer="getContainer">
    <a-form ref="formRef" :label-col="{ span: 5 }" :model="form" :rules="rules">
      <a-form-item label="运单号" v-if="form.waybillNumber">
        <span>{{ form.waybillNumber }}</span>
      </a-form-item>
      <a-form-item label="合同形式" name="signType">
        <smart-enum-select :disabled="disableSignType" v-model:value="form.signType" enumName="CONTRACT_SIGN_TYPE_ENUM" width="100%"/>
      </a-form-item>
      <a-form-item label="合同编号" name="contractCode" v-if="offlineFlag">
        <a-input v-model:value.trim="form.contractCode" class="form-width" placeholder="请输入合同编号"/>
      </a-form-item>
      <a-form-item label="合同名称" name="contractName">
        <a-input v-model:value.trim="form.contractName" class="form-width" placeholder="请输入合同名称"/>
      </a-form-item>
      <a-form-item v-if="!form.waybillId" label="合同负责人" name="responsibleUserId">
        <employee-select v-model:value="form.responsibleUserId" :leaveFlag="false" placeholder="请选择合同负责人" width="100%"/>
      </a-form-item>
      <a-form-item label="合同模板" name="contractType">
        <ContractTypeSelect v-model:value="form.contractType" placeholder="请选择合同类型"></ContractTypeSelect>
      </a-form-item>
      <a-form-item label="合同有效期" name="expirationDate">
        <a-date-picker v-model:value="form.expirationDate" class="form-width" :allowClear="false" format="YYYY-MM-DD"
                       valueFormat="YYYY-MM-DD"/>
      </a-form-item>
      <a-form-item v-if="form.signType === CONTRACT_SIGN_TYPE_ENUM.OFFLINE.value" label="上传合同" name="contractFile">
        <Upload
            :default-file-list="form.contractFile"
            :folder="FILE_FOLDER_TYPE_ENUM.CONTRACT.value"
            :maxUploadSize="10"
            buttonText="点击上传文件"
            @change="contractFileChange"
            :multiple="true"
        />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="3" class="form-width" placeholder="请输入备注"/>
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button @click="onClose">取消</a-button>
      <a-button type="primary" @click="onSubmit">确定</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import EmployeeSelect from '/@/components/employee-select/index.vue';
import ContractTypeSelect from "/@/components/contract-type-select/index.vue";
import { reactive, ref, computed } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { contractApi } from '/@/api/business/contract/contract-api';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { CONTRACT_SIGN_TYPE_ENUM } from '/@/constants/business/contract-const';
import Upload from '/@/components/upload/index.vue';
import { getContainer } from '/@/utils/container-util';

defineExpose({
  showModal,
  showUpdateModal
})

const offlineFlag = computed(() => {
  return form.signType == CONTRACT_SIGN_TYPE_ENUM.OFFLINE.value;
});

const props = defineProps({
  signerType: Number,// 签署人类型 CONTRACT_SIGNER_TYPE_ENUM
  signerBelongId: Number// 签署人归属ID（货主/车队/司机）
})
const emit = defineEmits("refresh");
// form组件与参数
const formRef = ref();
const formDefault = {
  contractId: null,
  contractName: undefined, // 合同名称
  contractType: undefined, // 合同类型
  expirationDate: undefined,// 到期日期
  responsibleUserId: undefined,// 合同负责人
  signType: undefined, // 合同签署类型 CONTRACT_SIGN_TYPE_ENUM
  contractFile: undefined, // 合同文件
  signerType: undefined,// 签署人类型 CONTRACT_SIGNER_TYPE_ENUM
  signerBelongId: undefined,// 签署人归属ID（货主/车队/司机）
  waybillId: undefined, // 运单ID
  waybillNumber: undefined, // 运单号
  remark: undefined // 备注
};
let form = reactive({...formDefault});
const rules = {
  contractCode: [{required: true, message: "合同编号不能为空"}],
  contractName: [{required: true, message: "合同名称不能为空"}],
  contractType: [{required: true, message: "合同类型不能为空"}],
  expirationDate: [{required: true, message: "到期日期不能为空"}],
  responsibleUserId: [{required: true, message: "合同负责人不能为空"}],
  signType: [{required: true, message: "合同形式不能为空"}],
  contractFile: [{required: true, message: "请上传合同附件"}],
};
// 是否展示与方法
const visible = ref(false);

function showModal(waybillId, waybillNumber) {
  Object.assign(form, formDefault);
  form.waybillId = waybillId;
  form.waybillNumber = waybillNumber;
  visible.value = true;
}

const disableSignType = ref(false);
function showUpdateModal(data) {
  Object.assign(form, formDefault);
  disableSignType.value = false;
  if (data) {
    Object.assign(form, data);
    disableSignType.value = true;
  }
  visible.value = true;
}

function contractFileChange(fileList) {
  form.contractFile = fileList;
  formRef.value.validateFields('contractFile');
}

// 操作
function onSubmit() {
  formRef.value
      .validate()
      .then(async () => {
        try {
          useSpinStore().show();
          form.signerType = props.signerType;
          form.signerBelongId = props.signerBelongId;
          if (form.contractId) {
            await contractApi.updateContract(form);
          } else {
            await contractApi.createContract(form);
          }
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
  Object.assign(form, formDefault);
  visible.value = false;
}

</script>
<style lang='less' scoped>
.form-width {
  width: 100%;
}
</style>
