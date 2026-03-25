<template>
  <a-modal :open="visible" :width="500" cancel-text="取消" ok-text="确认" title="批量核销" @cancel="onClose"
           @ok="onSubmit">
    <a-page-header>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item> 订单应收金额： {{ amountInfo.totalAmount }}</a-descriptions-item>
            <a-descriptions-item> 已核销金额： {{ amountInfo.verificationAmount }}</a-descriptions-item>
            <a-descriptions-item> 未核销金额： {{ amountInfo.unpaidAmount }}</a-descriptions-item>
          </a-descriptions>
        </div>
      </div>
    </a-page-header>
    <a-form ref="formRef" :model="form" :rules="rules" class="smart-form" labelAlign="right">
      <!---基础信息-->
      <a-card size="small">
        <a-form-item label="收款账户" name="bankId">
          <EnterpriseBankSelect v-model:value="form.bankId"  placeholder="请选择收款账户" width="100%"/>
        </a-form-item>
        <a-form-item label="流水单号" name="sequenceCode">
          <a-input v-model:value="form.sequenceCode" placeholder="请输入流水单号"
                   style="width: 100%"/>
        </a-form-item>
        <a-form-item label="核销日期" name="verificationTime">
          <a-date-picker v-model:value="form.verificationTime" placeholder="请选择核销日期" style="width: 100%"
                         valueFormat="YYYY-MM-DD"/>
        </a-form-item>
        <a-form-item label="核销金额">
          <a-input v-model:value="amountInfo.unpaidAmount" disabled style="width: 100%" valueFormat="YYYY-MM-DD"/>
        </a-form-item>
        <a-form-item label="核销凭证" name="attachment">
          <Upload
              :default-file-list="form.attachment"
              :folder="FILE_FOLDER_TYPE_ENUM.SHIPPER.value"
              :maxUploadSize="3"
              accept=".jpg,.jpeg,.png,.gif"
              buttonText="点击上传核销凭证"
              listType="picture-card"
              @change="change"
          />
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-input v-model:value="form.remark" placeholder="请输入备注"/>
        </a-form-item>
      </a-card>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';

import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import EnterpriseBankSelect from '/@/components/enterprise-bank-select/index.vue';

import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { SmartLoading } from '/@/components/smart-loading';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';

const visible = ref(false);
const formDefault = {
  bankId: null,
  sequenceCode: null,
  receiveOrderIdList: null,
  verificationTime: null,
  verificationAmount: null,
  attachment: null,
  remark: null
};
const formRef = ref();
const form = reactive({ ...formDefault });

const rules = {
  bankId: [{ required: true, message: '请选择收款银行' }],
  verificationTime: [{ required: true, message: '请选择核销日期' }],
  attachment: [{ required: true, message: '请选择核销凭证' }],
  sequenceCode: [{ required: true, message: '请输入流水单号' }],
};

// ------------------ 货物列表
let amountInfo = ref({});

function showModal (receiveOrderIdList) {
  Object.assign(form, formDefault);
  form.receiveOrderIdList = receiveOrderIdList;
  getAmountInfo();
}

// ------- 文件上传 start
function change (fileList) {
  form.attachment = fileList;
  formRef.value.validateFields('attachment');
}

// 提交确认对账
async function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          let params = _.cloneDeep(form);
          await receiveOrderApi.batchVerificationReceiveOrder(params);
          message.success('核销成功');
          onClose();
          emit('refresh');
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

async function getAmountInfo () {
  try {
    let result = await receiveOrderApi.getBatchVerificationAmount(form.receiveOrderIdList);
    amountInfo.value = result.data;
    visible.value = true;
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

function onClose () {
  visible.value = false;
}

function changeEnterprise () {
  form.bankId = null;
}

const emit = defineEmits('refresh');
defineExpose({
  showModal
});
</script>
