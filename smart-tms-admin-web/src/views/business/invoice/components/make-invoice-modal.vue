<template>
  <a-modal :width="500" :open="visible" title="开票" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           :getContainer="getContainer" @cancel="onClose">
    <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules">
      <!---基础信息-->
      <a-card size="small">
        <a-form-item label="开票时间" name="invoiceTime">
          <a-date-picker v-model:value="form.invoiceTime" valueFormat="YYYY-MM-DD" style="width: 100%"
                         placeholder="请输入开票时间"/>
        </a-form-item>
        <a-form-item label="费用发票号" name="invoiceNumberList">
          <a-input v-model:value="form.invoiceNumberList[index]" v-for="(item,index) in form.invoiceNumberList"
                   style="width: 100%"
                   placeholder="请输入费用发票号"/>
          <a-button @click="addInvoiceNumber" type="link">新增发票号</a-button>
        </a-form-item>
        <a-form-item label="快递单号" name="courierNumber">
          <a-input v-model:value="form.courierNumber" style="width: 100%"
                   placeholder="请输入快递单号"/>
        </a-form-item>
        <a-form-item label="上传附件" name="invoiceAttachment">
          <Upload
              listType="picture-card"
              :maxUploadSize="15"
              buttonText="点击上传附件"
              @change="change"
              :default-file-list="form.invoiceAttachment"
              :folder="FILE_FOLDER_TYPE_ENUM.WAYBILL_VOUCHER.value"
          />
        </a-form-item>
        <a-form-item label="上传对账单" name="invoiceAttachment">
          <Upload
              :default-file-list="form.billAttachment"
              :folder="FILE_FOLDER_TYPE_ENUM.WAYBILL_VOUCHER.value"
              :maxUploadSize="15"
              buttonText="点击上传对账单"
              listType="picture-card"
              @change="uploadBillAttachment"
          />
        </a-form-item>
      </a-card>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';

import Upload from '/@/components/upload/index.vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { SmartLoading } from '/@/components/smart-loading';
import { message } from 'ant-design-vue';
import _ from 'lodash';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';
import { getContainer } from '/@/utils/container-util';

const visible = ref(false);
const formDefault = {
  receiveOrderInvoiceId: null,
  receiveOrderInvoiceIdList: [],
  invoiceTime: null,
  invoiceNumberList: [],
  invoiceAttachment: null,
  billAttachment: null,
  courierNumber: null
};
const formRef = ref();
const form = reactive({ ...formDefault });

const rules = {
  invoiceTime: [{ required: true, message: '请选择开票时间' }],
};

// ------------------ 货物列表

function showModal (receiveOrderInvoiceId, receiveOrderInvoiceIdList) {
  form.receiveOrderInvoiceId = receiveOrderInvoiceId;
  form.receiveOrderInvoiceIdList = receiveOrderInvoiceIdList;
  form.invoiceNumberList = [''];
  visible.value = true;
}

// ------- 文件上传 start
function change (fileList) {
  form.invoiceAttachment = fileList;
}

// 上传对账单
function uploadBillAttachment (fileList) {
  form.billAttachment = fileList;
}

// 提交确认对账
async function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          let params = _.cloneDeep(form);
          let invoiceNumberList = params.invoiceNumberList.filter(e => e);
          if (_.isEmpty(invoiceNumberList)) {
            message.error('费用发票号不能为空');
            return;
          }
          if (_.isEmpty(form.receiveOrderInvoiceIdList)) {
            await receiveOrderApi.applyInvoice(params);
          } else {
            await receiveOrderApi.batchInvoice(params);
          }
          message.success('已开票');
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
  Object.assign(form, formDefault);
  visible.value = false;
}

function addInvoiceNumber () {
  form.invoiceNumberList.push('');
}

const emit = defineEmits('refresh');
defineExpose({
  showModal
});
</script>
