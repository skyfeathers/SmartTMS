<template>
  <a-modal
      :title="form.moduleId ? '编辑': '新建'"
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
        <a-col :span="24">
          <a-form-item label="出车款金额" name="amount">
            <a-input-number v-model:value="form.amount" :precision="2" placeholder="请输入申请的出车款金额"
            style="width: 200px"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="附件" name="attachment">
            <Upload
                :default-file-list="form.attachment"
                :folder="FILE_FOLDER_TYPE_ENUM.CAR_COST.value"
                :maxUploadSize="10"
                buttonText="点击上传附件"
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
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';
import { useRoute } from 'vue-router';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import WaybillModalSelect from '/@/components/waybill-modal-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import { carCostReceiveApi } from '/@/api/business/waybill/car-cost-receive-api';

let route = useRoute();
// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  moduleId:null,
  waybillId: null,
  categoryId: null,
  amount: null,
  attachment: null,
  remark: null,
};
let form = reactive({ ...formDefault });
const rules = {
  amount: [{ required: true, message: '请输入费用金额' }],
  waybillId: [{ required: true, message: '请选择运单' }],
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
    }
  if(!waybillSelect){
       showWaybillSelect.value = waybillSelect;
       form.waybillId = rowData.waybillId;
    }
  visible.value = true;
}

function onClose () {
  Object.assign(form, formDefault);
  formRef.value.resetFields();
  waybillList.value = [];
  visible.value = false;
}
// ----------------------- 抽屉的展示隐藏 end ------------------------

function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          let params = Object.assign(form);
          params.cashReceiveId = form.moduleId || undefined
          if (params.moduleId) {
            await carCostReceiveApi.updateCashReceive(params);
            message.success('编辑成功');
          } else {
            await carCostReceiveApi.addCashReceive(params);
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


const waybillList = ref([])
function changeWaybill(list) {
    waybillList.value = list;
    form.waybillId = list[0].waybillId
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
