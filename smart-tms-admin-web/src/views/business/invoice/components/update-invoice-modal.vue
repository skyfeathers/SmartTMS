<template>
  <a-modal :getContainer="getContainer" :open="visible" :width="500" cancel-text="取消" ok-text="确认" title="修改开票信息"
           @cancel="onClose" @ok="onSubmit">
    <a-form ref="formRef" :model="form" :rules="rules" class="smart-form" labelAlign="right">
      <!---基础信息-->
      <a-card size="small">
        <a-form-item label="开票时间" name="invoiceTime">
          <a-date-picker v-model:value="form.invoiceTime"  format="YYYY-MM-DD" placeholder="请输入开票时间" style="width: 100%"
                         valueFormat="YYYY-MM-DD"/>
        </a-form-item>
        <a-form-item label="费用发票号" name="invoiceNumberList">
          <a-input v-for="(item,index) in form.invoiceNumberList" v-model:value="form.invoiceNumberList[index]"
                   placeholder="请输入费用发票号"
                   style="width: 100%"/>
          <a-button type="link" @click="addInvoiceNumber">新增发票号</a-button>
        </a-form-item>
        <a-form-item label="快递单号" name="courierNumber">
          <a-input v-model:value="form.courierNumber" placeholder="请输入快递单号"
                   style="width: 100%"/>
        </a-form-item>
        <a-form-item label="上传附件" name="invoiceAttachment">
          <Upload
              :default-file-list="form.invoiceAttachment"
              :folder="FILE_FOLDER_TYPE_ENUM.WAYBILL_VOUCHER.value"
              :maxUploadSize="10"
              accept=".jpg,.jpeg,.png,.gif"
              buttonText="点击上传附件"
              listType="picture-card"
              @change="change"
          />
        </a-form-item>
        <a-form-item label="上传对账单" name="billAttachment">
          <Upload
              :default-file-list="form.billAttachment"
              :folder="FILE_FOLDER_TYPE_ENUM.WAYBILL_VOUCHER.value"
              :maxUploadSize="10"
              accept=".jpg,.jpeg,.png,.gif"
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
import dayjs from 'dayjs';

const visible = ref(false);
const formDefault = {
  receiveOrderInvoiceId: null,
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

function showModal (row) {
  console.log(row);
  let {
    invoiceTime,
    invoiceNumber,
    courierNumber,
    invoiceAttachment,
    billAttachment,
    receiveOrderInvoiceId
  } = _.cloneDeep(row);
  let invoiceNumberList = (invoiceNumber || '').split(',');
  Object.assign(form, {
    invoiceTime: dayjs(invoiceTime).format('YYYY-MM-DD'),
    invoiceNumberList,
    courierNumber,
    invoiceAttachment,
    billAttachment,
    receiveOrderInvoiceId });
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
          await receiveOrderApi.updateInvoice(params);
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
