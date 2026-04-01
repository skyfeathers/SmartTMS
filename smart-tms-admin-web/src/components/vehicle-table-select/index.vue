<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal :open="visible" :width="900" title="车辆选择" @cancel="onClose">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item class="smart-query-form-item" label="关键字">
          <a-input v-model:value="queryForm.vehicleKeyWords" placeholder="车牌号/速记码" style="width: 300px"/>
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

      <template #ownerType="{ record }">
        {{ $smartEnumPlugin.getDescByValue('VEHICLE_OWNER_TYPE_ENUM', record.ownerType) }}
      </template>

      <template #expireDate="{ record }">
        <span :class="{ 'expired' : dateExpired(record.expireDate) }">{{ dateHandle(record.expireDate) }}</span>
      </template>

      <template #relyEnterpriseRoadTransportBusinessLicenseExpireDate="{ record }">
        <span :class="{ 'expired' : dateExpired(record.relyEnterpriseRoadTransportBusinessLicenseExpireDate) }">{{
            dateHandle(record.relyEnterpriseRoadTransportBusinessLicenseExpireDate)
          }}</span>
      </template>

      <template #compulsoryTrafficExpireTime="{ record }">
        <span :class="{ 'expired' : dateExpired(record.compulsoryTrafficExpireTime) }">{{
            dateHandle(record.compulsoryTrafficExpireTime)
          }}</span>
      </template>

      <template #commercialExpireTime="{ record }">
        <span :class="{ 'expired' : dateExpired(record.commercialExpireTime) }">{{
            dateHandle(record.commercialExpireTime)
          }}</span>
      </template>

      <template #vehicleAuditExpireDate="{ record }">
        <span :class="{ 'expired' : dateExpired(record.vehicleAuditExpireDate) }">{{
            dateHandle(record.vehicleAuditExpireDate)
          }}</span>
      </template>

      <template #auditStatus="{ text }">
        {{ $smartEnumPlugin.getDescByValue('AUDIT_STATUS_ENUM', text) }}
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
import {vehicleApi} from '/@/api/business/vehicle/vehicle-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';
import _ from 'lodash';
import {useVehicleExpired} from "/@/views/business/vehicle/hooks/use-vehicle-expired";
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
    dataIndex: 'vehicleId',
    fixed: "left",
  },
  {
    title: "车牌号",
    dataIndex: "vehicleNumber",
    width: 180,
    fixed: "left",
  },
  {
    title: "车辆类型",
    width: 100,
    dataIndex: "vehicleTypeName",
  },
  {
    title: "载重（kg）",
    dataIndex: "vehicleTonnage",
    width: 100,
  },
  {
    title: "实际所属人",
    width: 100,
    dataIndex: "owner",
  },
  {
    title: "所属人性质",
    dataIndex: "ownerType",
    width: 100,
    slots: {customRender: "ownerType"},
  },
  {
    title: "行驶证到期日期",
    dataIndex: "expireDate",
    width: 180,
    slots: {customRender: "expireDate"},
  },
  {
    title: "挂靠到期时间",
    dataIndex: "relyEnterpriseRoadTransportBusinessLicenseExpireDate",
    width: 180,
    slots: {customRender: "relyEnterpriseRoadTransportBusinessLicenseExpireDate"},
  },
  {
    title: "交强险到期时间",
    dataIndex: "compulsoryTrafficExpireTime",
    width: 180,
    slots: {customRender: "compulsoryTrafficExpireTime"},
  },
  {
    title: "商业险到期时间",
    dataIndex: "commercialExpireTime",
    width: 180,
    slots: {customRender: "commercialExpireTime"},
  },
  {
    title: "车辆审验到期时间",
    dataIndex: "vehicleAuditExpireDate",
    width: 180,
    slots: {customRender: "vehicleAuditExpireDate"},
  },
  {
    title: "审核状态",
    dataIndex: "auditStatus",
    width: 100,
    slots: {customRender: "auditStatus"},
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
  vehicleKeyWords: '',
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
// 表格选择
let rowKey = ref('vehicleId');
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
    let responseModel = await vehicleApi.query(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// ----------------------- 时间处理 -----------------------
let {dateHandle,dateExpired} = useVehicleExpired();

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
