<template>
  <a-modal :width="800" :open="visible" title="申请发票" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">

    <a-page-header>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="受票方">{{ invoiceDetail.consignor }}</a-descriptions-item>
            <a-descriptions-item label="发票抬头">{{ invoiceDetail.invoiceName }}</a-descriptions-item>
            <a-descriptions-item label="纳税人识别号">{{ invoiceDetail.invoiceNo }}</a-descriptions-item>

            <a-descriptions-item label="开票地址">{{ invoiceDetail.invoiceAddress }}</a-descriptions-item>
            <a-descriptions-item label="开票银行">{{ invoiceDetail.invoiceBankName }}</a-descriptions-item>
            <a-descriptions-item label="开户行号">{{ invoiceDetail.invoiceBankNo }}</a-descriptions-item>

            <a-descriptions-item label="税点(%)">{{ invoiceDetail.taxPoint }}</a-descriptions-item>
            <a-descriptions-item label="开票总额">{{ invoiceAmount }}</a-descriptions-item>
          </a-descriptions>
        </div>
      </div>
    </a-page-header>

    <a-card size="small">
      <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="备注" name="remark">
              <a-textarea :rows="2" v-model:value="form.remark" type="textarea" placeholder="请输入备注" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <a-table
          size="small"
          :dataSource="orderList"
          :columns="columns"
          :pagination="false"
          bordered
      >
      </a-table>
    </a-card>
  </a-modal>
</template>
<script setup>
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import ShipperInvoiceSelect from '/@/components/shipper-invoice-select/index.vue';

import { ref, computed, reactive } from 'vue';
import { SmartLoading } from '/@/components/smart-loading';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';

const emit = defineEmits('refresh');
defineExpose({
  showModal
});
// ------------------ 弹窗操作 ------------------
const visible = ref(false);

function showModal (shipperId, showList, receiveOrderIdList,invoiceInfo) {
  form.shipperId = shipperId;
  orderList.value = showList;
  form.receiveOrderIdList = receiveOrderIdList;
  invoiceDetail.value = invoiceInfo || {};
  visible.value = true;
}
function onClose () {
  visible.value = false;
}

// ------------------ 查询详情 ------------------
let orderList = ref([]);
let invoiceDetail = ref({});
const columns = reactive([
  {
    title: '订单号',
    dataIndex: 'orderNo'
  },
  {
    title: '订单应收金额',
    dataIndex: 'totalAmount'
  },
  {
    title: '开票金额',
    dataIndex: 'invoiceAmount'
  }
])

function changeInvoice (invoiceId, invoiceInfo) {
  invoiceDetail.value = invoiceInfo;
}
const invoiceAmount = computed(() => {
  let invoiceAmountList = orderList.value.map(e=>e.invoiceAmount);
  return _.sum(invoiceAmountList);
});
// ------------------ 表单相关 ------------------
const formDefault = {
  receiveOrderIdList: [],
  invoiceRemark: null
};
const formRef = ref();
const form = reactive({ ...formDefault });

const rules = {
};

// 申请开票
async function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          let params = _.cloneDeep(form);
          await receiveOrderApi.applyInvoice(params);
          message.success('已申请开票')
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
</script>
