<template>
  <a-modal :open="visible" :width="900" title="司机选择" @cancel="onClose">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item class="smart-query-form-item" label="关键字">
          <a-input v-model:value="queryForm.keyWords" placeholder="姓名/电话/身份证号/创建人" style="width: 300px"/>
        </a-form-item>
        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button type="primary" @click="ajaxQuery">
            <template #icon>
              <ReloadOutlined/>
            </template>
            查询
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <SearchOutlined/>
            </template>
            重置
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>

    <a-table
        :columns="showColumns"
        :dataSource="tableData"
        :loading="tableLoading"
        :pagination="false"
        :row-selection="multiple ? { selectedRowKeys: selectedRowKeyList, onChange: onSelectChange } : null"
        :rowKey="rowKey"
        :scroll="{ x: 1300 }"
        size="small"
        bordered
    >

      <template #driverName="{ record }">
        {{ record.driverName }} - {{ record.telephone }}
      </template>

      <template #idCardEffectiveDate="{ record }">
        <span :class="{ 'expired' : dateExpired(record.idCardEffectiveEndDate) }">{{
            dateHandle(record.idCardEffectiveEndDate, record.idCardEndlessFlag)
          }}</span>
      </template>

      <template #vehicleClass="{ record }">
        {{ $smartEnumPlugin.getDescByValue('VEHICLE_CLASS_ENUM', record.vehicleClass) }}
      </template>

      <template #validPeriod="{ record }">
        <span :class="{ 'expired' : dateExpired(record.validPeriodTo) }">{{
            dateHandle(record.validPeriodTo, record.drivingLicenseEndlessFlag)
          }}</span>
      </template>

      <template #qualificationCertificateDate="{ record }">
        <span :class="{ 'expired' : dateExpired(record.qualificationCertificateEndDate) }">{{
            dateHandle(record.qualificationCertificateEndDate)
          }}</span>
      </template>

      <template #action="{ record }">
        <a @click="selectSingleItem(record.vehicleId,record)">选择</a>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :defaultPageSize="queryForm.pageSize"
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :show-total="(total) => `共${total}条`"
          :total="total"
          show-less-items
          showQuickJumper
          showSizeChanger
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
      />
    </div>

    <template #footer>
      <a-space v-if="multiple">
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">确定</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
import {computed, reactive, ref} from 'vue';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';
import _ from 'lodash';
import {driverApi} from "/@/api/business/driver/driver-api";
import {useDriverCertificateValidity} from "/@/views/business/driver/hooks/use-driver-certificate-validity";
// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['onChange', 'onSelect']);
// props
const props = defineProps({
  multiple: {
    type: Boolean,
    default: false,
  }
})
// 表格
let columns = reactive([
  {
    title: 'ID',
    width: 50,
    dataIndex: 'driverId',
    fixed: "left"
  },
  {
    title: "姓名-电话",
    dataIndex: "driverName",
    width: 200,
    fixed: "left",
    slots: {customRender: "driverName"},
  },
  {
    title: "身份证号",
    width: 180,
    dataIndex: "drivingLicense",
  },
  {
    title: "身份证有效期",
    dataIndex: "idCardEffectiveDate",
    width: 180,
    slots: {customRender: "idCardEffectiveDate"},
  },
  {
    title: "准驾车型",
    dataIndex: "vehicleClass",
    width: 100,
    slots: {customRender: "vehicleClass"},
  },
  {
    title: "驾驶证有效期",
    dataIndex: "validPeriod",
    width: 100,
    slots: {customRender: "validPeriod"},
  },
  {
    title: "从业资格证号",
    dataIndex: "qualificationCertificate",
    width: 180,
  },
  {
    title: "从业资格证有效期",
    dataIndex: "qualificationCertificateDate",
    width: 180,
    slots: {customRender: "qualificationCertificateDate"},
  },
  {
    title: "创建人",
    width: 180,
    dataIndex: "createUserName",
  },
  {
    title: "创建时间",
    width: 180,
    dataIndex: "createTime",
  },
  {
    title: "操作",
    dataIndex: "action",
    fixed: "right",
    width: 200,
    slots: {customRender: "action"},
  },
]);

const showColumns = computed(() => {
  if (props.multiple) {
    let actionIndex = columns.findIndex(e => e.dataIndex === 'action');
    if (actionIndex !== -1) {
      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      columns.splice(actionIndex, 1);
    }
  }
  return columns;
})

const queryFormState = {
  keyWords: '',
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
// 表格选择
let rowKey = ref('driverId');
let selectedRowKeyList = ref([]);
let selectedRowsData = ref([]);
const onSelectChange = (selectedRowKeys, selectedRows) => {
  // 找到当页未被勾选项
  let unchecked = _.xorBy(tableData.value, selectedRows, rowKey.value);
  // 合并当前与选中项
  selectedRowKeyList.value = _.unionBy(selectedRowKeyList.value, selectedRowKeys);
  selectedRowsData.value = _.unionBy(selectedRowsData.value, selectedRows, rowKey.value);
  // 移除未被勾选项
  selectedRowKeyList.value = _.differenceBy(selectedRowKeyList.value, unchecked.map((e) => e[rowKey.value]));
  selectedRowsData.value = _.differenceBy(selectedRowsData.value, unchecked, rowKey.value);
};
// 是否展示
const visible = ref(false);
// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------
function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await driverApi.queryDriver(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// 有效期
let {dateHandle, dateExpired} = useDriverCertificateValidity();

function showModal() {
  visible.value = true;
  ajaxQuery();
}

function onClose() {
  Object.assign(queryForm, queryFormState);
  selectedRowKeyList.value = [];
  selectedRowsData.value = [];
  tableLoading.value = false;
  visible.value = false;
}

function onSubmit() {
  let idList = _.cloneDeep(selectedRowKeyList.value);
  let itemList = _.cloneDeep(selectedRowsData.value);
  emit('onChange', idList, itemList);
  onClose();
}

function selectSingleItem(id, item) {
  let result = _.cloneDeep(item);
  emit('onSelect', id, result);
  onClose();
}


// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
<style lang="less" scoped>
.expired {
  color: red;
}
</style>
