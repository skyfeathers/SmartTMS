<template>
  <a-modal width="600px" :open="visible" title="新增盘点" ok-text="确定" cancel-text="取消" @ok="onSubmit" @cancel="onClose"
           :getContainer="getContainer">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }">
      <a-form-item label="盘点名称" name="checkName">
        <a-input v-model:value="form.checkName" placeholder="请输入盘点名称 " style="width:200px"/>
      </a-form-item>
      <a-form-item label="盘点位置" name="locationId">
        <LocationSelect v-model:value="form.locationId" placeholder="请选择盘点位置" style="width:200px"/>
      </a-form-item>
      <a-form-item label="盘点人员" name="employeeIdList">
        <employee-select
            ref="managerSelectRef"
            v-model:value="form.employeeIdList"
            placeholder="请选择盘点人员"
            width="200px"
            mode="multiple"
        />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea :rows="4" v-model:value="form.remark" type="textarea" placeholder="请输入备注" style="width:200px"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import LocationSelect from '/@/components/fixed-asset/locaton-select/index.vue';
import EmployeeSelect from '/@/components/employee-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';

import { getContainer } from '/@/utils/container-util';
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { consumablesCheckApi } from '/@/api/fixed-asset/asset/consumables-check-api';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

// ----------------------- 弹窗展示关闭 start ------------------------
// 是否展示
const visible = ref(false);

function showModal (rowData) {
  Object.assign(form, formDefault);
  visible.value = true;
}

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

// ----------------------- 提交 ------------------------
//  组件
const formRef = ref();

const formDefault = {
  checkName: '',
  locationId: null,
  employeeIdList: [],
  remark: '',
};
let form = reactive({ ...formDefault });
const rules = {
  checkName: [{ required: true, message: '请输入盘点名称' }],
  locationId: [{ required: true, message: '请选择盘点位置' }],
  employeeIdList: [{ required: true, message: '请选择盘点人员' }],
};

function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          await consumablesCheckApi.add(form);
          message.success('添加成功');
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

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
