<template>
  <a-modal
      v-model:open="visible"
      :width="750"
      cancel-text="取消"
      ok-text="确认"
      :title="form.basicInfoId ? '编辑':'添加'"
      @cancel="onClose"
      @ok="onSubmit"
  >
    <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
    >
      <a-row>
        <a-col :span="24">
                    <a-form-item class="smart-query-form-item" label="运单" name="waybillId">
                        <a-tag v-for="item in waybillList" :key="item.waybillId" class="smart-margin-bottom5" closable
                            @close="removeWaybill(item.waybillId)">
                            {{ item.waybillNumber }}
                        </a-tag>
                        <a-button size="small" type="primary" @click="showWaybillModal">选择运单</a-button>
                    </a-form-item>
                </a-col>
        <a-col :span="12">
          <a-form-item label="高速里程" name="highSpeedMileage">
            <a-input-number v-model:value="form.highSpeedMileage" :min="0" :precision="2"
                            placeholder="请输入高速里程" style="width: 200px"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="低速里程" name="lowSpeedMileage">
            <a-input-number v-model:value="form.lowSpeedMileage" :min="0" :precision="2"
                            placeholder="请输入低速里程" style="width: 200px"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="总公里数" name="gpsMileage">
            <a-input-number v-model:value="form.gpsMileage" :min="0" :precision="2"
                            placeholder="请输入总公里数（KM）" style="width: 200px"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="总用油量" name="oilConsumption">
            <a-input-number v-model:value="form.oilConsumption" :min="0" :precision="2"
                            placeholder="请输入用油量" style="width: 200px"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item label="出勤天数" name="attendanceDays">
            <a-input-number v-model:value="form.attendanceDays" :min="0" :precision="0"
                            placeholder="请输入出勤天数" style="width: 200px"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="提成工资" name="commissionSalary">
            <a-input-number v-model:value="form.commissionSalary" :min="0" :precision="2"
                            placeholder="请输入提成工资" style="width: 200px"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item label="基本工资" name="basicSalary">
            <a-input-number v-model:value="form.basicSalary" :min="0" :precision="0"
                            placeholder="请输入基本工资" style="width: 200px"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="补助" name="allowance">
            <a-input-number v-model:value="form.allowance" :min="0" :precision="2"
                            placeholder="请输入补助" style="width: 200px"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
  <WaybillModalSelect ref="waybillModalRef" carCostFlag @changeWaybill="changeWaybill" />
</template>
<script setup>
import { watch, ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';
import { useRoute } from 'vue-router';
import { carCostApi } from '/@/api/business/car-cost/car-cost-api';
import WaybillModalSelect from '/@/components/waybill-modal-select/index.vue';
let route = useRoute();
// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  waybillId: null,
  highSpeedMileage: null,
  lowSpeedMileage: null,
  gpsMileage: null,
  oilConsumption: null,
  attendanceDays: null,
  commissionSalary: null,
  basicSalary: null,
  allowance: null
};
let form = reactive({ ...formDefault });
const rules = {
  waybillId:[{ required: true, message: '请选择运单' }],
  highSpeedMileage: [{ required: true, message: '请输入计入工资里程（高速）' }],
  lowSpeedMileage: [{ required: true, message: '请输入计入工资里程（低速）' }],
  gpsMileage: [{ required: true, message: '请输入GPS公里数（KM）' }],
  oilConsumption: [{ required: true, message: '请输入用油量' }],
  attendanceDays: [{ required: true, message: '请输入出勤天数' }],
  commissionSalary: [{ required: true, message: '请输入提成工资' }],
  basicSalary: [{ required: true, message: '请输入基本工资' }],
  allowance: [{ required: true, message: '请输入补助' }],
};

watch((() => form.attendanceDays),
    () => {
      form.buzhu = form.attendanceDays * 50;
    }
);
// ----------------------- 抽屉的展示隐藏 start ------------------------
// 是否展示抽屉
const visible = ref(false);

function showModal (rowData) {
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    Object.assign(form, rowData);
    waybillList.value = [{
      waybillId:rowData.waybillId,
      waybillNumber:rowData.waybillNumber
    }]
  }
  visible.value = true;
}

function onClose () {
  Object.assign(form, formDefault);
  waybillList.value = []
  formRef.value.resetFields();
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
          await carCostApi.updateBasicInfo(params)
          message.success('操作成功');
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

const waybillModalRef = ref();
function showWaybillModal() {
    waybillModalRef.value.showModal({}, (waybillList.value || []).map(e => e.waybillId), waybillList.value);
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
