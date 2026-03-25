<template>
  <a-modal
    :open="visible"
    ok-text="提交财务开票"
    cancel-text="取消"
    @ok="onSubmit"
    :getContainer="getContainer"
    destroyOnClose
    width="800px"
    wrap-class-name="full-modal"
    @cancel="onClose"
  >
    <a-form class="smart-form smart-margin-top20" labelAlign="right" ref="invoiceFormRef" :rules="rules" :model="form" :labelCol="{style: { width: '90px' }}">
        <a-card title="开票信息">
          <a-form-item label="业务日期" name="businessDate">
              <a-date-picker v-model:value="form.businessDate" picker="month" placeholder="请选择业务日期"
                              style="width: 100%" valueFormat="YYYY-MM-01"/>
          </a-form-item>
            <a-form-item label="税点(%)" name="taxPoint">
              <a-input-number v-model:value="form.taxPoint" :min="0" :precision="2" :max="100" placeholder="请输入税点" style="width: 100%" />
            </a-form-item>
            <a-form-item label="开票金额" :name="['receiveOrderInvoiceForm', 'invoiceAmount']" :rules="[{ required: true, message: '请输入开票金额' }]">
              <a-input-number v-model:value="form.receiveOrderInvoiceForm.invoiceAmount" placeholder="请输入开票金额" :min="0" :precision="2" :max="2000000" style="width: 100%" :disabled="true"/>
            </a-form-item>
            <a-form-item label="发票抬头" :name="['receiveOrderInvoiceForm', 'invoiceName']" :rules="[{ required: true, message: '请输入发票抬头' }]">
              <InvoiceHeadSelect placeholder="请选发票抬头" style="width:100%" :shipperId="form.shipperId" @change="headChange"/>
            </a-form-item>
            <a-form-item label="纳税人识别号" :name="['receiveOrderInvoiceForm', 'invoiceNo']" :rules="[{ required: true, message: '请输入纳税人识别号' }]">
              <a-input v-model:value="form.receiveOrderInvoiceForm.invoiceNo" placeholder="请输入纳税人识别号" style="width: 100%" />
            </a-form-item>
            <a-form-item label="发票种类" :name="['receiveOrderInvoiceForm', 'invoiceKindType']" :rules="[{ required: true, message: '请选择发票种类' }]">
              <smart-enum-select v-model:value="form.receiveOrderInvoiceForm.invoiceKindType" placeholder="请选择发票种类"
                           enum-name="INVOICE_KIND_TYPE_ENUM" style="width:100%"/>
            </a-form-item>
            <a-form-item label="发票类型" :name="['receiveOrderInvoiceForm', 'invoiceType']" :rules="[{ required: true, message: '请选择类型' }]">
              <smart-enum-select v-model:value="form.receiveOrderInvoiceForm.invoiceType" placeholder="请选择类型"
                           enum-name="INVOICE_TYPE_ENUM" style="width:100%"/>
            </a-form-item>
            <a-form-item label="备注" name="remark">
              <a-textarea v-model:value="form.remark" :rows="2" placeholder="请输入备注" style="width: 100%" />
            </a-form-item>
        </a-card>
        <a-card title="发票邮寄信息">
              <a-form-item label="收货人" :name="['receiveOrderMailForm', 'receivePerson']" :rules="[{ required: true, message: '请输入收货人' }]">
                <a-input v-model:value="form.receiveOrderMailForm.receivePerson" placeholder="请输入收货人" style="width: 100%" />
              </a-form-item>
              <a-form-item label="收货人电话" :name="['receiveOrderMailForm', 'receivePersonPhone']" :rules="[{ required: true, message: '请输入收货人电话' }]">
                <a-input v-model:value="form.receiveOrderMailForm.receivePersonPhone" placeholder="请输入收货人电话" style="width: 100%" />
              </a-form-item>
              <a-form-item label="邮箱" :name="['receiveOrderMailForm', 'email']" :rules="[{ required: true, message: '请输入邮箱' }, { type: 'email', message: '请输入正确的邮箱格式' }]">
                <a-input v-model:value="form.receiveOrderMailForm.email" placeholder="请输入邮箱" style="width: 100%" />
              </a-form-item>
        </a-card>
      </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { orderApi } from '/@/api/business/order/order-api';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';
import { getContainer } from '/@/utils/container-util';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import InvoiceHeadSelect from '/@/components/invoice-header-select/index.vue';
import _ from "lodash";
// ----------------------- 对外暴露 ------------------------
const emit = defineEmits(['refresh']);
defineExpose({
  showModal,
});
const formDefault = {
  waybillIdList: [],
  remark: null,
  taxPoint: 9,
  businessDate: null,
  receiveOrderInvoiceForm: {
    invoiceAddress: null,
    invoiceAmount: 0,
    invoiceBankName:null,
    invoiceBankNo:null,
    invoiceKindType:null,
    invoiceName:null,
    invoiceNo: null,
    invoicePhone: null,
    invoiceType: null
  },
  receiveOrderMailForm: {
    receiveAddress: null,
    receiveCityCode: null,
    receiveCityName: null,
    receiveDistrictCode: null,
    receiveDistrictName: null,
    receivePerson: null,
    receivePersonPhone: null,
    receiveProvinceCode: null,
    receiveProvinceName:null,
    email: null,
  }
};
// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
let form = reactive(_.cloneDeep(formDefault));

// ----------------------- 表单 ------------------------
const rules = {
  taxPoint: [{ required: true, message: '请输入税点' }],
  invoiceName: [{ required: true, message: '请输入发票抬头' }],
  invoiceNo: [{ required: true, message: '请输入发票号' }],
  invoiceType: [{ required: true, message: '请输入发票类型' }],
  receivePerson: [{ required: true, message: '请输入收货人' }],
  receivePersonPhone: [{ required: true, message: '请输入收货人电话' }],
  businessDate: [{ required: true, message: '请选择业务日期', trigger: ['change'] }],
};


function onClose() {
  Object.assign(form, _.cloneDeep(formDefault));
  visible.value = false;
}

async function showModal(waybillIdList, shipperId) {
  form.waybillIdList = waybillIdList;
  form.shipperId = shipperId;
  await nextTick();
  getShipperCheckInfo();
}

// ----------------------- 提交数据 ------------------------
// 开票信息的form
let invoiceFormRef = ref();

async function onSubmit() {
  await invoiceFormRef.value.validate();
  try {
    useSpinStore().show();
    let params = Object.assign(form);
    await receiveOrderApi.submitReceiveOrderAndInvoice(params);
    message.success('提交成功');
    emit('refresh');
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

function headChange(invoiceHeadId, invoiceHead) {
  if (invoiceHead) {
    form.receiveOrderInvoiceForm.invoiceName = invoiceHead.invoiceName;
    form.receiveOrderInvoiceForm.invoiceNo = invoiceHead.invoiceNo;
  }
}

// ----------------------- 查询数据 ------------------------
let shipperDetail = reactive({});

async function getShipperCheckInfo() {
  useSpinStore().show();
  try {
    let responseModel = await orderApi.selectShipperCheckInfo(form.waybillIdList);
    let detail = responseModel.data;
    Object.assign(shipperDetail, detail);
    form.receiveOrderInvoiceForm.invoiceAmount=detail.invoiceAmount;
    Object.assign(form.receiveOrderMailForm, detail.mailAddressDTO);
    form.businessDate = shipperDetail.businessDate;
    visible.value = true;
  } catch (error) {
    console.log('error', error);
  } finally {
    useSpinStore().hide();
  }
}
</script>
<style scoped lang="less">
.full-modal {
  .ant-modal {
    max-width: 100%;
    top: 0;
    padding-bottom: 0;
    margin: 0;
  }

  .ant-modal-content {
    display: flex;
    flex-direction: column;
    height: calc(100vh);
  }

  .ant-modal-body {
    flex: 1;
  }
}

:deep(.ant-card-body) {
  padding: 5px;
}

:deep(.ant-alert) {
  padding: 4px 10px;
}

:deep(.ant-card-head) {
  min-height: 0px;
}

:deep(.ant-card-head-title) {
  padding: 4px 0;
}
:deep(.ant-descriptions-item-container) {
  height: 30px;
}
</style>
