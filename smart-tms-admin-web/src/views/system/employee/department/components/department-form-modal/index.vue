<!--
 * @Author: LiHaiFan
 * @Date: 2021-08-16 08:52:38
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /xiaomifeng-crm-manage-web/src/views/system/employee/department/components/department-form-modal/index.vue
-->
<template>
  <a-modal v-model:open="visible" :title="formState.departmentId ? '编辑部门' : '添加部门'" @ok="handleOk" destroyOnClose>
    <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
      <a-form-item label="上级部门" name="parentId" v-if="formState.parentId != 0">
        <DepartmentTreeSelect ref="departmentTreeSelect" v-model:value="formState.parentId" :defaultValueFlag="false"
                              width="100%"/>
      </a-form-item>
      <a-form-item label="部门名称" name="name">
        <a-input v-model:value.trim="formState.name" placeholder="请输入部门名称"/>
      </a-form-item>
      <a-form-item label="部门负责人" name="managerId">
        <EmployeeSelect ref="employeeSelect" placeholder="请选择部门负责人" width="100%" v-model:value="formState.managerId"
                        :leaveFlag="false"/>
      </a-form-item>
      <a-form-item label="部门排序 （值越大越靠前！）" name="sort">
        <a-input-number style="width: 100%" v-model:value="formState.sort" :min="0" placeholder="请输入部门名称"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import message from 'ant-design-vue/lib/message';
import {reactive, ref} from 'vue';
import {departmentApi} from '/@/api/system/department/department-api';
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import EmployeeSelect from '/@/components/employee-select/index.vue';
import {useSpinStore} from '/@/store/modules/system/spin';

// ----------------------- 对外暴漏 ---------------------

defineExpose({
  showModal,
});

// ----------------------- modal 的显示与隐藏 ---------------------
const emits = defineEmits(['refresh']);

const visible = ref(false);

function showModal(data) {
  visible.value = true;
  updateFormData(data);
}

function closeModal() {
  visible.value = false;
  resetFormData();
}

// ----------------------- form 表单操作 ---------------------
const formRef = ref();
const departmentTreeSelect = ref();
const defaultDepartmentForm = {
  id: undefined,
  managerId: undefined,
  name: undefined,
  parentId: undefined,
  sort: 0,
};
const employeeSelect = ref();

let formState = reactive({
  ...defaultDepartmentForm,
});
// 表单校验规则
const rules = {
  parentId: [{required: true, message: '上级部门不能为空'}],
  name: [
    {required: true, message: '部门名称不能为空'},
    {max: 50, message: '部门名称不能大于20个字符', trigger: 'blur'},
  ],
  // managerId: [{required: true, message: '部门负责人不能为空'}],
};

// 更新表单数据
function updateFormData(data) {
  Object.assign(formState, defaultDepartmentForm);
  if (data) {
    Object.assign(formState, data);
  }
  visible.value = true;
}

// 重置表单数据
function resetFormData() {
  Object.assign(formState, defaultDepartmentForm);
}

async function handleOk() {
  try {
    await formRef.value.validate();
    if (formState.departmentId) {
      updateDepartment();
    } else {
      addDepartment();
    }
  } catch (error) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
}

// ----------------------- form 表单  ajax 操作 ---------------------
//添加部门ajax请求
async function addDepartment() {
  useSpinStore().show();
  try {
    await departmentApi.addDepartment(formState);
    emits('refresh');
    closeModal();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

//更新部门ajax请求
async function updateDepartment() {
  useSpinStore().show();
  try {
    if (formState.parentId == formState.departmentId) {
      message.warning('上级菜单不能为自己');
      return;
    }
    await departmentApi.updateDepartment(formState);
    emits('refresh');
    closeModal();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}
</script>
