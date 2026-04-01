<template>
  <a-modal
    width="800px"
    :open="visible"
    :title="form.oilCardId ? '编辑' : '添加'"
    ok-text="确认"
    cancel-text="取消"
    @cancel="onClose"
  >
    <a-form ref="formRef" :model="form" :rules="rules">
      <a-row :gutter="16">
        

        <a-col :span="12" v-if="form.type === OIL_CARD_TYPE_ENUM.SLAVE.value">
          <a-form-item label="主卡">
            <OilCardSelect :type="OIL_CARD_TYPE_ENUM.MASTER.value" width="100%" v-model:value="form.masterOilCardId" @change="handleMasterOilCardChange" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="油卡卡号" name="oilCardNo">
            <a-input v-model:value="form.oilCardNo" placeholder="请输入油卡卡号" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item class="smart-query-form-item" label="燃料类型" name="fuelType">
          <SmartEnumSelect v-model:value="form.fuelType " enumName="OIL_CARD_FUEL_TYPE_ENUM"
                             placeholder="请选择燃料类型" width="100%"/>
        </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="油卡品牌" name="brand">
            <DictSelect width="100%" keyCode="OIL-CARD-BRAND" v-model:value="form.brand" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="领取人" name="receiveUserId">
            <employee-select
                placeholder="请选择领取人"
                :disabledFlag="false"
                v-model:value="form.receiveUserId"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="持卡司机" name="useDriverId">
            <DriverSelect width="100%" v-model:value="form.useDriverId" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="当前持卡车" name="useVehicleId">
            <VehicleSelect width="100%" placeholder="请选择车辆" v-model:value="form.useVehicleId" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="期初余额" name="beginBalance">
            <a-input
                :disabled="!!form.oilCardId"
                style="width: 100% !important"
                v-model:value="form.beginBalance"
                :min="0"
                :max="999999.99"
                :precision="2"
                suffix="新建后期初余额不可编辑"
                placeholder="请输入期初余额"
            />
          </a-form-item>
        </a-col>

        <a-col :span="12" v-if="!form.oilCardId">
          <a-form-item label="油卡密码" name="password">
            <a-input v-model:value="form.password" type="password" placeholder="请输入油卡密码" autocomplete />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="用途" name="purpose">
            <DictSelect width="100%" keyCode="OIL-CARD-PURPOSE" v-model:value="form.purpose" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="状态" name="disabledFlag">
            <a-select v-model:value="form.disabledFlag" placeholder="请选择状态">
              <a-select-option :value="1">启用</a-select-option>
              <a-select-option :value="2">禁用</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="定点加油" name="fixedPointFlag">
            <a-select v-model:value="form.fixedPointFlag" placeholder="请选择是否定点加油">
              <a-select-option :value="1">是</a-select-option>
              <a-select-option :value="2">否</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="备注" name="remark">
            <a-textarea :rows="4" v-model:value="form.remark" type="textarea" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
    <template #footer>
      <a-button @click="onClose">取消</a-button>
      <a-button @click="validateForm" type="primary">确定</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import DictSelect from '/@/components/smart-dict-select/index.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import DriverSelect from '/@/components/driver-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import EmployeeSelect from '/@/components/employee-select/index.vue';
import OilCardSelect from '/@/components/oil-card-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import { OIL_CARD_TYPE_ENUM } from '/@/constants/business/card-const';
import { ref, reactive } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { oilApi } from '/@/api/business/card/oil-api';
import _ from 'lodash';


// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  beginBalance: null,
  brand: null,
  fixedPointFlag: 2,
  disabledFlag: 1,
  oilCardNo: '',
  oilCardId: '',
  masterOilCardId: null, //主卡id
  password: '',
  purpose: null,
  remark: '',
  type: null,
  useVehicleId: null,
  useDriverId: null,
  receiveUserId: null,  // 领取人
  fuelType :null  //燃料类型
};
let form = reactive({ ...formDefault });
const rules = {
  oilCardNo: [{ required: true, message: '请输入油卡卡号' }],
  brand: [{ required: true, message: '请选择油卡品牌' }],
  beginBalance: [{ required: true, message: '请选输入期初余额' }],
  password: [{ required: true, message: '请输入油卡密码' }],
  fuelType: [{ required: true, message: '请选择燃料类型' }],
};

const enabledChecked = ref(true);
// 是否展示
const visible = ref(false);

// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------

function showModal (cardType, rowData) {
  Object.assign(form, formDefault);
  if (rowData) {
    if(!_.isEmpty(rowData.purpose)){
      rowData.purpose = rowData.purpose[0].valueCode;
    }
    if(!_.isEmpty(rowData.brand)){
      rowData.brand = rowData.brand[0].valueCode;
    }
    if(!rowData.fuelType){
      rowData.fuelType=null
    }
    rowData.disabledFlag = rowData.disabledFlag ? 2 : 1;
    rowData.fixedPointFlag = rowData.fixedPointFlag ? 1 : 2;
    Object.assign(form, rowData);
  }
  form.type = cardType;
  visible.value = true;
}

function onClose() {
  Object.assign(form, formDefault);
  enabledChecked.value = true;
  visible.value = false;
}

function validateForm () {
  formRef.value.validate()
      .then(async () => {
        useSpinStore().show();
        console.log(form)
        if (form.type === OIL_CARD_TYPE_ENUM.MASTER.value && form.disabledFlag == 2) {
          Modal.confirm({
            title: '您确定要禁用该主卡吗？',
            content: '主卡关联的副卡也会被禁用',
            okText: '确定',
            okType: 'danger',
            onOk () {
              onSubmit();
            },
            cancelText: '取消',
            onCancel () {
            },
          });
          return;
        } else {
          onSubmit();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
}

async function onSubmit () {
  useSpinStore().show();
  try {
    let params = Object.assign({}, form);
    params.disabledFlag = params.disabledFlag == 1 ? false : true;
    params.fixedPointFlag = params.fixedPointFlag == 1 ? true : false;
    if (params.oilCardId) {
      await oilApi.updateOil(params);
    } else {
      await oilApi.saveOil(params);
    }
    message.success(`${params.oilCardId ? '修改' : '添加'}成功`);
    emit('reloadList');
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

// 主卡选择改变时，更新副卡列表
function handleMasterOilCardChange (value, item) {
  console.log(item);
  form.brand = item.brand[0].valueCode
  form.fuelType = item.fuelType;
}






// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
