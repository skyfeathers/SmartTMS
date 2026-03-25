<template>
  <a-modal :open="visible" title="提交应收核算" ok-text="提交" cancel-text="取消" @ok="onSubmit" width="600px"
           :getContainer="getContainer" @cancel="onClose">
    <a-card size="small">
      <a-form ref="formRef" :model="form" class="smart-form" labelAlign="right" :labelCol="{style: { width: '70px' }}">
        <a-form-item>
          <span class="warning">提交核算后，运单应付应收费用将不可更改，请谨慎操作！</span>
        </a-form-item>
        <a-form-item label="应收合计">
          {{ shipperDetail.invoiceAmount }}
        </a-form-item>
        <a-form-item label="货主名称">
          {{ shipperDetail.consignor }}
        </a-form-item>
        <a-form-item label="业务日期" name="businessDate">
            <a-date-picker v-model:value="form.businessDate" picker="month" placeholder="请选择业务日期"
                             style="width: 300px" valueFormat="YYYY-MM-01"/>
        </a-form-item>
        <a-form-item label="备注" name="remark">
            <a-textarea v-model:value="form.remark" :rows="2" placeholder="请输入应收核算备注" style="width: 300px"/>
        </a-form-item>

        <a-form-item label="附件" name="attachment">
          <Upload
              :default-file-list="form.attachment"
              :folder="FILE_FOLDER_TYPE_ENUM.RECEIVE_ORDER.value"
              :maxUploadSize="10"
              buttonText="点击上传附件"
              listType="picture-card"
              @change="changeAttachment"
          />
        </a-form-item>
      </a-form>
    </a-card>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';
import { orderApi } from '/@/api/business/order/order-api';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import Upload from '/@/components/upload/index.vue';
import { getContainer } from '/@/utils/container-util';
// ----------------------- 对外暴露 ------------------------
const emit = defineEmits(['refresh']);
defineExpose({
  showModal,
});

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal (waybillIdList, shipperId) {
  form.waybillIdList = waybillIdList;
  form.shipperId = shipperId;
  getShipperCheckInfo();
}

// ----------------------- 表单 ------------------------
const formDefault = {
  waybillIdList: [],
  remark: null,
  businessDate: null,
  attachment: null
};
let form = reactive({ ...formDefault });

// ----------------------- 提交数据 ------------------------
const costTableRef = ref();
async function onSubmit () {
  try {
    useSpinStore().show();

    let costFormList = costTableRef.value.getCostList();
    let params = Object.assign({}, form, { costFormList });

    await receiveOrderApi.submitReceiveOrder(params);
    message.success('提交成功');
    emit('refresh');
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

function changeAttachment (fileList) {
  form.attachment = fileList;
}

// ----------------------- 查询数据 ------------------------
let shipperDetail = reactive({});

async function getShipperCheckInfo () {
  useSpinStore().show();
  try {
    let responseModel = await orderApi.selectShipperCheckInfo(form.waybillIdList);
    shipperDetail = responseModel.data;
    
    Object.assign(form, { businessDate: shipperDetail.businessDate });
    visible.value = true;
  } catch (error) {
    console.log('error', error);
  } finally {
    useSpinStore().hide();
  }
}

// ----------------------- 应付表格 ------------------------
</script>
<style scoped lang="less">
:deep(.ant-descriptions-item-container){
  height: 30px;
}
.warning {
  font-weight: bold;
  color: #FF0000;
}
</style>
