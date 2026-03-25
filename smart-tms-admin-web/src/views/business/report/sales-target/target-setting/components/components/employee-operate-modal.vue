<template>
  <a-modal title="业绩目标"
    :width="720"
    :open="visible"
    @ok="onSubmit"
    @cancel="onClose"
    destroyOnClose>
    <a-form class="smart-query-form">

      <a-form-item label="销售人员"
        class="smart-query-form-item">
        <employee-select
            ref="managerSelectRef"
            v-model:value="form.employeeId"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value"
            placeholder="请选择业务负责人"
            width="200px"
        />
      </a-form-item>
    </a-form>

    <a-card>
      <a-table size="small"
        :dataSource="monthList"
        :columns="columns"
        :pagination="false"
        rowKey="month"
        bordered>
        <template #target="{ index }">
          <a-input v-model:value="monthList[index].target"
            type="number"
            :min="0"
            placeholder="请输入目标金额" />
        </template>
      </a-table>
    </a-card>
  </a-modal>
</template>
<script setup>
import { reactive, ref, watch } from 'vue';
import { message } from 'ant-design-vue';
import _ from 'lodash';
import { useSpinStore } from '/@/store/modules/system/spin';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import { employeeBusinessApi } from '/@/api/business/report/employee-business-api';
import { employeeApi } from '/@/api/system/employee/employee-api';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';

// ------------ 定义Emit
const emit = defineEmits(['refresh']);

const enterpriseId = ref();
// ---- watch 监听
watch(enterpriseId, async (e) => {
});

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
  employeeId: undefined,
  monthList: [],
  year: undefined,
});

const monthList = ref([]);
// 是否展示弹窗
const visible = ref(false);
function showModal(year, selectedEnterpriseId, selectedEmployeeId, sourceMonthList) {
  form.year = year;
  form.employeeId = selectedEmployeeId;
  // form.enterpriseId = selectedEmployeeId;
  // enterpriseId.value = selectedEnterpriseId;
  initMonth(sourceMonthList || []);
  visible.value = true;
}

async function onSubmit() {
  if (!form.employeeId) {
    return message.error('请选择销售人员');
  }
  monthList.value.forEach((e) => {
    e.target = Number(e.target) || 0;
  });
  form.monthList = monthList.value.filter((e) => !_.isNaN(e.target) && _.isNumber(e.target));
  if (_.isEmpty(form.monthList)) {
    return message.error('至少编辑一条业绩目标');
  }

  useSpinStore().show();
  try {
    await employeeBusinessApi.updateEmployeeSalesTarget(form);
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
    let target = 0;
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
