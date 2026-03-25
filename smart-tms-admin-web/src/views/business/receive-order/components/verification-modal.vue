<template>
  <a-modal :width="500" :open="visible" title="确认核销" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-page-header>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item> 订单应收金额： {{ receiveInfo.totalAmount }}</a-descriptions-item>
            <a-descriptions-item> 已核销金额： {{ receiveInfo.verificationAmount }}</a-descriptions-item>
            <a-descriptions-item> 未核销金额： {{ receiveInfo.unpaidAmount }}</a-descriptions-item>
          </a-descriptions>
        </div>
      </div>
    </a-page-header>
    <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules">
      <!---基础信息-->
      <a-card size="small">

        <a-form-item label="收款账户" name="bankId">
          <EnterpriseBankSelect width="100%" placeholder="请选择收款账户" v-model:value="form.bankId"/>
        </a-form-item>
        <a-form-item label="流水单号" name="sequenceCode">
          <a-input v-model:value="form.sequenceCode" style="width: 100%"
                  placeholder="请输入流水单号"/>
        </a-form-item>
        <a-form-item label="核销日期" name="verificationTime">
          <a-date-picker v-model:value="form.verificationTime" valueFormat="YYYY-MM-DD" style="width: 100%"
                         placeholder="请选择核销日期"/>
        </a-form-item>
        <a-form-item label="核销金额" name="verificationAmount">
          <a-input-number v-model:value="form.verificationAmount" :precision="4" style="width: 100%"
                          placeholder="请输入核销金额"/>
        </a-form-item>
        <a-form-item label="核销凭证" name="attachment">
          <Upload
              accept=".jpg,.jpeg,.png,.gif"
              listType="picture-card"
              :maxUploadSize="3"
              buttonText="点击上传核销凭证"
              @change="change"
              :default-file-list="form.attachment"
              :folder="FILE_FOLDER_TYPE_ENUM.SHIPPER.value"
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
  receiveOrderId: null,
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
  verificationAmount: [{ required: true, message: '请输入核销金额' }],
  // sequenceCode: [{ required: true, message: '请输入流水单号' }],
};

// ------------------ 货物列表
let receiveInfo = ref({});

function showModal (detail, receiveOrderId) {
  Object.assign(form, formDefault);
  receiveInfo.value = detail;
  form.receiveOrderId = receiveOrderId;
  visible.value = true;
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
          await receiveOrderApi.verificationReceiveOrder(params);
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

function onClose () {
  visible.value = false;
}

const emit = defineEmits('refresh');
defineExpose({
  showModal
});
</script>
