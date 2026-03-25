<!--
 * @Author: LiHaiFan
 * @Date: 2021-08-17 21:03:34
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description:
-->
<template>
  <a-modal v-model:open="visible" title="调整部门" ok-text="提交" cancel-text="取消" @ok="handleOk" @cancel="closeModal"
           :getContainer="getContainer">
    <DepartmentTree ref="departmentTree" :height="400" :showMenu="false"/>
  </a-modal>
</template>
<script setup>
import {message} from 'ant-design-vue';
import _ from 'lodash';
import {ref} from 'vue';
import DepartmentTree from '../department-tree/index.vue';
import {employeeApi} from '/@/api/system/employee/employee-api';
import {useSpinStore} from '/@/store/modules/system/spin';
import {getContainer} from '/@/utils/container-util';
// ----------------------- 以下是字段定义 emits props ---------------------

const emit = defineEmits(['refresh']);

// ----------------------- 显示/隐藏 ------------------------

const departmentTree = ref();
const visible = ref(false);
const employeeIdList = ref([]);

//显示
async function showModal(selectEmployeeId) {
  employeeIdList.value = selectEmployeeId;
  visible.value = true;
}

//隐藏
function closeModal() {
  visible.value = false;
}

// ----------------------- form操作 ---------------------------------
async function handleOk() {
  useSpinStore().show();
  try {
    if (_.isEmpty(employeeIdList.value)) {
      message.warning('请选择要调整的员工');
      return;
    }
    if (_.isEmpty(departmentTree.value.selectedKeys)) {
      message.warning('请选择要调整的部门');
      return;
    }
    let departmentId = departmentTree.value.selectedKeys[0];
    let params = {
      employeeIdList: employeeIdList.value,
      departmentId: departmentId,
    };
    await employeeApi.batchUpdateDepartmentEmployee(params);
    message.success('操作成功');
    emit('refresh');
    closeModal();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({
  showModal,
});
</script>
<style scoped lang="less">
.footer {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 16px;
  background: #fff;
  text-align: right;
  z-index: 1;
}
</style>
