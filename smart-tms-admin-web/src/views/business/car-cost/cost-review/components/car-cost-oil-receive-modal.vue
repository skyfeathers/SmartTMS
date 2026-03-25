<template>
  <a-modal
      :title="form.moduleId ? '编辑' :'新建'"
      v-model:open="visible"
      cancel-text="取消"
      ok-text="确认"
      @cancel="onClose"
      @ok="onSubmit"
  >
    <a-form ref="formRef"
            :label-col="{ span: 5 }"
            :model="form"  :rules="rules" :wrapper-col="{ span: 12 }">
      <a-row>
        <a-col :span="24" v-if="showWaybillSelect">
            <a-form-item class="smart-query-form-item" label="运单" name="waybillId">
                <a-tag v-for="item in waybillList" :key="item.waybillId" class="smart-margin-bottom5" closable
                    @close="removeWaybill(item.waybillId)">
                    {{ item.waybillNumber }}
                </a-tag>
                <a-button size="small" type="primary" @click="showWaybillModal">选择运单</a-button>
            </a-form-item>
        </a-col>
        <a-col :span="24" v-if="vehicleId">
          <a-form-item label="充值油卡" name="oilCardId">
            <VehicleOilCardSelect v-model:value="form.oilCardId"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="充值金额" name="amount">
            <a-input-number v-model:value="form.amount" :min="0" :precision="2" placeholder="请输入充值金额"
            style="width: 200px"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="附件" name="attachment">
            <Upload
                :default-file-list="form.attachment"
                :folder="FILE_FOLDER_TYPE_ENUM.CAR_COST.value"
                :maxUploadSize="10"
                buttonText="点击上传充值附件"
                listType="picture-card"
                @change="uploadAttachment"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="备注" name="remark">
            <a-textarea v-model:value="form.remark" :min="0" :precision="2"
                            placeholder="请输入备注" style="width: 200px"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
  <WaybillModalSelect ref="waybillModalRef" carCostFlag @changeWaybill="changeWaybill" />
</template>
<script setup>
import { provide, ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';
import { useRoute } from 'vue-router';
import { CAR_COST_CATEGORY_TYPE_ENUM } from '/@/constants/business/car-cost-const';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import WaybillModalSelect from '/@/components/waybill-modal-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import VehicleOilCardSelect from '/@/components/vehicle-oil-card-select/index.vue';
import { carCostReceiveApi } from '/@/api/business/waybill/car-cost-receive-api';

let route = useRoute();
// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  moduleId: null,
  waybillId: null,
  oilCardId: null,
  amount: null,
  attachment: null,
  remark: null,
  oilReceiveId: null
};
let form = reactive({ ...formDefault });
const rules = {
  oilCardId: [{ required: true, message: '请选择油卡' }],
  amount: [{ required: true, message: '请输入费用金额' }],
};

const showWaybillSelect = ref(true);

// ----------------------- 抽屉的展示隐藏 start ------------------------
// 是否展示抽屉
const visible = ref(false);

function showModal (rowData, waybillSelect = true) {
  showWaybillSelect.value = waybillSelect;
  Object.assign(form, formDefault);
  Object.assign(form, rowData);
  if(rowData && rowData.tabulationId){
        waybillList.value = [{
            waybillId:rowData.waybillId,
            waybillNumber:rowData.waybillNumber
        }]
        vehicleId.value = rowData.vehicleId || undefined
    }
  if(!waybillSelect){
       showWaybillSelect.value = waybillSelect;
       form.waybillId = rowData.waybillId;
    }
  visible.value = true;
}

function onClose () {
  Object.assign(form, formDefault);
  waybillList.value = [];
  formRef.value.resetFields();
  visible.value = false;
}
// ----------------------- 抽屉的展示隐藏 end ------------------------

let waybillId = Number(route.query.waybillId);
function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          let params = Object.assign(form);
          params.oilCardReceiveId = form.moduleId || undefined
          if (params.oilCardReceiveId) {
            await carCostReceiveApi.updateOilReceive(params);
            message.success('编辑成功');
          } else {
            await carCostReceiveApi.addOilReceive(params);
            message.success('添加成功');
          }
          emit('reloadList');
          onClose();
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
}

// 上传附件
function uploadAttachment (fileList) {
  form.attachment = fileList;
}

const waybillModalRef = ref();
function showWaybillModal() {
    waybillModalRef.value.showModal({}, (waybillList.value || []).map(e => e.waybillId), waybillList.value);
}


const vehicleId = ref(null);
provide('vehicleId', vehicleId)
const waybillList = ref([])
function changeWaybill(list) {
    waybillList.value = list;
    form.waybillId = list[0].waybillId
    vehicleId.value = list[0].vehicleId
    formRef.value.validate('waybillId')
}

function removeWaybill (waybillId) {
  let findIndex = waybillList.value.findIndex(e => e.waybillId == waybillId);
  if (findIndex == -1) {
    return;
  }
  waybillList.value.splice(findIndex, 1);
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
