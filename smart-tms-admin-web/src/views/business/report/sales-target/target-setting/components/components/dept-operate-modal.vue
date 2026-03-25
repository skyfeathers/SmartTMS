<template>
  <a-modal title="校区/小组业绩目标" :width="720" :open="visible" @ok="onSubmit" @cancel="onClose" destroyOnClose>
    <a-form class="smart-query-form">
      <a-form-item label="校区/小组" class="smart-query-form-item">
        <DepartmentTreeSelect ref="departmentTreeSelect" :init="true" v-model:value="form.departmentId" />
      </a-form-item>
    </a-form>

    <a-card>
      <a-table size="small" :dataSource="monthList" :columns="columns" :pagination="false" rowKey="month">
        <template #target="{ index }">
          <a-input v-model:value="monthList[index].target" type="number" placeholder="请输入目标金额" />
        </template>
      </a-table>
    </a-card>
  </a-modal>
</template>
<script setup>
import { reactive, ref } from 'vue';
import { message } from 'ant-design-vue';
import _ from 'lodash';
import { useSpinStore } from '/@/store/modules/system/spin';
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import { departmentBusinessApi } from '/@/api/business/report/department-business-api';

// ------------ 定义Emit
const emit = defineEmits(['refresh']);

const columns = reactive([
  {
    title: '月份',
    dataIndex: 'month',
    fixed: 'left',
  },
  {
    title: '目标金额',
    dataIndex: 'target',
    slots: { customRender: 'target' },
  },
]);

const form = reactive({
  departmentId: undefined,
  monthList: [],
  year: undefined,
});

const monthList = ref([]);

// 是否展示弹窗
const visible = ref(false);
const departmentTreeSelect = ref();
function showModal(year, departmentId, sourceMonthList) {
  form.year = year;
  form.departmentId = departmentId;
  initMonth(sourceMonthList || []);
  visible.value = true;
}

async function onSubmit() {
  if (!form.departmentId) {
    return message.error('请选择校区');
  }
  monthList.value.forEach((e) => {
    e.target = Number(e.target);
  });
  form.monthList = monthList.value.filter((e) => !_.isNaN(Number(e.target)) && _.isNumber(Number(e.target)));
  if (_.isEmpty(form.monthList)) {
    return message.error('至少编辑一条业绩目标');
  }

  useSpinStore().show();
  try {
    await departmentBusinessApi.updateDeptSalesTarget(form);
    message.success('更新成功');
    onClose();
    emit('refresh');
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

function onClose() {
  visible.value = false;
}

function initMonth(sourceMonthList) {
  monthList.value = [];
  for (let month = 1; month <= 12; month++) {
    let find = sourceMonthList.find((e) => e.month == month);
    let target = undefined;
    if (find) {
      target = find.target;
    }
    monthList.value.push({
      month,
      target,
    });
  }
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
