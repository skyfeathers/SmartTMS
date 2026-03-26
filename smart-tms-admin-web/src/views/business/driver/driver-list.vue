<template>
  <a-form class="smart-query-form" v-privilege="'driver:query'">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="关键字">
        <a-input v-model:value="queryForm.keyWords" class="form-width" placeholder="姓名/电话/身份证号/创建人" />
      </a-form-item>

      <!-- <a-form-item class="smart-query-form-item" label="审核状态">
        <smart-enum-select v-model:value="queryForm.auditStatus" enumName="AUDIT_STATUS_ENUM" />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="身份证到期时间">
        <a-range-picker v-model:value="queryForm.idCardTime" :ranges="defaultTimeRanges" class="form-width"
          @change="changeIdCardTime" />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker v-model:value="queryForm.createTime" :ranges="defaultTimeRanges" class="form-width"
          @change="changeCreateTime" />
      </a-form-item> -->

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery" class="smart-margin-left10">
          <template #icon>
            <ReloadOutlined />
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button size="small" type="primary" @click="addDriver()" v-privilege="'driver:add'">
          <template #icon>
            <PlusOutlined />
          </template>
          新建司机
        </a-button>

        <a-button :disabled="selectedRowKeyList.length == 0" size="small" @click="batchAuditDriver"
          v-privilege="'driver:batchAudit'">
          <template #icon>
            <AuditOutlined />
          </template>
          批量审核
        </a-button>

        <a-button :disabled="selectedRowKeyList.length == 0" size="small" type="primary" danger
          @click="confirmBatchDisable" v-privilege="'driver:batchUpdateDisable'">
          批量禁用
        </a-button>

        <a-button :disabled="selectedRowKeyList.length == 0" size="small" type="primary" @click="confirmBatchEnable"
          v-privilege="'driver:batchUpdateEnable'">
          批量启用
        </a-button>

        <a-button :disabled="selectedRowKeyList.length == 0" size="small" @click="showUpdateManagerModal()"
          v-privilege="'driver:updateManager'">
          <template #icon>
            <AuditOutlined />
          </template>
          分配负责人
        </a-button>

        <a-button
            :disabled="selectedRowKeyList.length == 0"
            size="small"
            @click="showUpdateBusinessModeModal">
          <template #icon>
            <AuditOutlined/>
          </template>
          修改经营方式
        </a-button>

        <a-button @click="confirmDelete(selectedRowKeyList, ajaxQuery)" :disabled="selectedRowKeyList.length == 0"
          size="small" v-privilege="'driver:delete'" type="primary" danger>删除
        </a-button>

        <a-button v-privilege="'driver:quickCreate'" size="small" type="primary" @click="showQuickCreateModal()">
          快速新建
        </a-button>

        <a-button @click="exportExcel()" v-privilege="'driver:export'" size="small">导出</a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.DRIVER" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-tabs @tabClick="changeTab" v-model:activeKey="activeKey">
      <a-tab-pane key="" tab="全部">
      </a-tab-pane>
      <a-tab-pane key="1" tab="内管">
      </a-tab-pane>
      <a-tab-pane key="2" tab="挂靠">
      </a-tab-pane>
      <a-tab-pane key="3" tab="外派">
      </a-tab-pane>
    </a-tabs>

    <a-table :columns="columns" :dataSource="tableData" :pagination="false"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }" :scroll="{ x: tableWidth, y: 500 }"
      :loading="tableLoading" rowKey="driverId" size="small" bordered>
      <template #headerCell="{ column }">
          <SmartHeaderCell v-model:value="queryForm[column.filterOptions?.key || column.dataIndex]" :column="column"
            @change="change" />
      </template>
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'driverName'">
          <a v-if="$privilege('driver:detail')" @click="driverDetail(record.driverId)" type="link">{{ record.driverName }} - {{ record.telephone }}</a>
        <span v-else>{{ record.driverName }} - {{ record.telephone }}</span>
        </template>
        <template v-if="column.dataIndex === 'businessMode'">
          <span>{{ $smartEnumPlugin.getDescByValue('DRIVER_BUSINESS_MODE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'idCardEffectiveDate'">
          <span :class="{ 'expired': dateExpired(record.idCardEffectiveEndDate) }">{{
      dateHandle(record.idCardEffectiveEndDate, record.idCardEndlessFlag) }}</span>
        </template>
        <template v-if="column.dataIndex === 'auditStatus'">
          <a-tag v-show="text === AUDIT_STATUS_ENUM.AUDIT_PASS.value" color="success">{{ AUDIT_STATUS_ENUM.AUDIT_PASS.desc
            }}</a-tag>
          <a-tag v-show="text === AUDIT_STATUS_ENUM.WAIT_AUDIT.value" color="warning">{{ AUDIT_STATUS_ENUM.WAIT_AUDIT.desc
            }}</a-tag>
          <a-tag v-show="text === AUDIT_STATUS_ENUM.REJECT.value" color="error">{{ AUDIT_STATUS_ENUM.REJECT.desc
            }}</a-tag>
        </template>
        <template v-if="column.dataIndex === 'status'">
          {{ $smartEnumPlugin.getDescByValue('DRIVER_STATUS_ENUM', text) }}
        </template>
        <template v-if="column.dataIndex === 'createUserType'">
          {{ $smartEnumPlugin.getDescByValue('USER_TYPE_ENUM', text) }}
        </template>
        <template v-if="column.dataIndex === 'driverVehicleVOList'">
          <template v-if="$privilege('vehicle:detail')">
            <a-button v-if="!$lodash.isEmpty(record.driverVehicleVOList) && record.driverVehicleVOList.length > 0" type="link"
                      @click="vehicleDetail(record.driverVehicleVOList[0].vehicleId)">{{ record.driverVehicleVOList[0].vehicleNumber }} &nbsp;
            </a-button>
            <a-button v-if="!$lodash.isEmpty(record.driverVehicleVOList) && record.driverVehicleVOList.length > 1" type="link"
                      @click="moreVehicle(record.driverVehicleVOList, false)">更多</a-button>
          </template>
          <template v-else>
            <span v-if="!$lodash.isEmpty(record.driverVehicleVOList) && record.driverVehicleVOList.length > 0">{{ record.driverVehicleVOList[0].vehicleNumber }}</span>
            <a-button v-if="!$lodash.isEmpty(record.driverVehicleVOList) && record.driverVehicleVOList.length > 1" type="link"
                      @click="moreVehicle(record.driverVehicleVOList, true)">更多</a-button>
          </template>
          <template v-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button @click="addDriver(record.driverId)" v-privilege="'driver:edit'" type="link">编辑</a-button>
            </div>
          </template>
        </template>
      </template>

    </a-table>

    <div class="smart-query-table-page">
      <a-pagination v-model:current="queryForm.pageNum" v-model:pageSize="queryForm.pageSize"
        :defaultPageSize="queryForm.pageSize" :pageSizeOptions="PAGE_SIZE_OPTIONS" :show-total="(total) => `共${total}条`"
        :total="total" show-less-items showQuickJumper showSizeChanger @change="ajaxQuery"
        @showSizeChange="ajaxQuery" />
    </div>
    <AuditModal ref="auditModal" @refresh="handleFinish" />
    <UpdateManagerModal ref="updateManagerRef" @refresh="handleFinish" />
    <UpdateBusinessModeModal ref="updateBusinessModeRef" @refresh="handleFinish"/>
    <!-- 快速创建司机、车辆、挂车 -->
    <VehicleQuickCreate ref="quickCreateRef" @reloadList="handleFinish" />
    <DriverVehicleModal ref="driverVehicleModal"/>
  </a-card>
</template>
<script setup>
import SmartEnumSelect from "/@/components/smart-enum-select/index.vue";
import UpdateManagerModal from "/@/components/update-manager-modal/index.vue";
import UpdateBusinessModeModal from "/@/components/update-business-mode/index.vue";
import AuditModal from '/@/components/audit-modal/index.vue'
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import VehicleQuickCreate from '/@/components/vehicle-quick-create/index.vue';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { onMounted, reactive, ref } from "vue";
import { message, Modal } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import { AUDIT_STATUS_ENUM, PAGE_SIZE, PAGE_SIZE_OPTIONS } from "/@/constants/common-const";
import DriverVehicleModal from '/@/views/business/driver/components/driver-vehicle-list-model.vue';
import { driverApi } from "/@/api/business/driver/driver-api";
import { DRIVER_STATUS_ENUM } from "/@/constants/business/driver-const";
import { useRouter } from 'vue-router';
import { useDriverDelete } from '/@/views/business/driver/hooks/use-driver-delete';
import { useDriverCertificateValidity } from '/@/views/business/driver/hooks/use-driver-certificate-validity';
import _ from 'lodash';
import { SmartLoading } from '/@/components/smart-loading';
import useDragTable from '/@/hook/use-drag-table/index';
import SmartHeaderCell from '/@/components/smart-table-header-cell/index.vue'
// --------------------- 列表查询 ------------------------
const { columnsData:columns,tableWidth } = useDragTable([
  {
    title: "姓名-电话",
    dataIndex: "driverName",
    width: 120,
    fixed: "left",
    filterOptions:{
        type:'input',
    }
  },
  {
    title: '经营方式',
    dataIndex: 'businessMode',
    width: 150,
    filterOptions:{
      type:'enum-select',
      enumName:'VEHICLE_BUSINESS_MODE_ENUM'
    }
  },
  {
    title: "关联车辆",
    dataIndex: "driverVehicleVOList",
    width: 120,
  },

  {
    title: "身份证号",
    width: 120,
    dataIndex: "drivingLicense",
    filterOptions:{
        type:'input',
    }
  },
  {
    title: "身份证有效期",
    dataIndex: "idCardEffectiveDate",
    width: 140,
    filterOptions:{
      type:'date-range',
      ranges:true,
      key:'idCardTime'
    }
  },
  {
    title: "审核状态",
    dataIndex: "auditStatus",
    width: 120,
    filterOptions:{
        type:'enum-select',
        enumName:'AUDIT_STATUS_ENUM'
    }
  },
  {
    title: "状态",
    dataIndex: "status",
    width: 120,
    filterOptions:{
        type:'enum-select',
        enumName:'FLAG_STATUS_TEXT_ENUM'
    }
  },
  {
    title: "创建人类型",
    width: 120,
    dataIndex: "createUserType",
    filterOptions:{
        type:'enum-select',
        enumName:'USER_TYPE_ENUM'
    }
  },
  {
    title: "创建人",
    width: 120,
    dataIndex: "createUserName",
    filterOptions:{
        type:'input',
    }
  },
  {
    title: "创建时间",
    width: 140,
    dataIndex: "createTime",
    filterOptions:{
      type:'date-range',
      ranges:true
    }
  },
  {
    title: "操作",
    dataIndex: 'action',
    fixed: 'right',
    width: 50,
    filterOptions:{
      type:'submit',
      btnType:'link'
    }
  },
], TABLE_ID_CONST.DRIVER);

const queryFormState = {
  keyWords: "",
  shorthandCode:undefined,
  status:undefined,
  createUserName:undefined,
  createUserType:undefined,
  enterpriseId:undefined,
  driverName:undefined,
  drivingLicense:undefined,
  auditStatus: undefined,
  birthdayStartTime: undefined,
  birthdayEndTime: undefined,
  birthdayTime: [],
  idCardStartTime: undefined,
  idCardEndTime: undefined,
  idCardTime: [],
  createStartTime: undefined,
  createEndTime: undefined,
  createTime: [],
  businessMode: undefined,
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const selectedRowKeyList = ref([]);
const tableData = ref([]);
const total = ref(0);
const activeKey = ref('');

function changeTab (value) {
  queryForm.businessMode = value;
  ajaxQuery();
}

function change(obj){
  if(obj.key == 'createTime'){
    queryForm.createStartTime = obj.value[0];
    queryForm.createEndTime = obj.value[1];
    search()
    return
  }
  if(obj.key == 'idCardTime'){
    queryForm.idCardStartTime = obj.value[0];
    queryForm.idCardEndTime = obj.value[1];
    search()
    return
  }
  if(obj.search){
    search()
  }
}

function changeIdCardTime(dates, dateStrings) {
  queryForm.idCardStartTime = dateStrings[0];
  queryForm.idCardEndTime = dateStrings[1];
}

function changeCreateTime(dates, dateStrings) {
  queryForm.createStartTime = dateStrings[0];
  queryForm.createEndTime = dateStrings[1];
}

// 有效期
let { dateHandle, dateExpired } = useDriverCertificateValidity();

function onSelectChange(selectedRowKeys) {
  selectedRowKeyList.value = selectedRowKeys;
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  selectedRowKeyList.value = [];
  ajaxQuery();
}

function search() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let data = { ...queryForm };
    let responseModel = await driverApi.queryDriver(data);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

const driverVehicleModal = ref();
function moreVehicle(driverVehicleVOList, disableLink) {
  driverVehicleModal.value.showModal(driverVehicleVOList, disableLink);
}

// ----------------------- 司机操作 -----------------------

function confirmBatchDisable() {
  Modal.confirm({
    title: "确定要禁用该司机吗？",
    content: "禁用后，该司机将不能登陆不能接单",
    okText: "禁用",
    okType: "danger",
    onOk() {
      batchUpdateDisableFlag(DRIVER_STATUS_ENUM.DISABLED.value);
    },
    cancelText: "取消",
    onCancel() {
    },
  });
}

function confirmBatchEnable() {
  Modal.confirm({
    title: "提示",
    content: "确定要启用该司机吗？",
    okText: "启用",
    onOk() {
      batchUpdateDisableFlag(DRIVER_STATUS_ENUM.ACTIVE.value);
    },
    cancelText: "取消",
    onCancel() {
    },
  });
}

async function batchUpdateDisableFlag(status) {
  try {
    useSpinStore().show();
    let param = {
      driverIdList: selectedRowKeyList.value,
      status
    };
    await driverApi.batchUpdateDisableFlag(param);
    message.success(status == DRIVER_STATUS_ENUM.ACTIVE.value ? '启用成功' : '禁用成功');
    selectedRowKeyList.value = [];
    await ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

let { confirmDelete } = useDriverDelete();

// ----------------- 审核 --------------------
let auditModal = ref();

function batchAuditDriver() {
  for (const item of tableData.value) {
    if (_.includes(selectedRowKeyList.value, item.driverId)) {
      if (item.auditStatus !== AUDIT_STATUS_ENUM.WAIT_AUDIT.value) {
        message.warning('请选择待审核的司机！请移除【' + item.driverName + '】');
        return;
      }
    }
  }

  let params = {
    driverIdList: selectedRowKeyList.value
  };
  auditModal.value.showModal(params, driverApi.batchAuditDriver);
}

function handleFinish() {
  selectedRowKeyList.value = [];
  ajaxQuery();
}

const updateManagerRef = ref();

function showUpdateManagerModal() {
  let driverIdList = selectedRowKeyList.value;
  let params = { driverIdList };
  updateManagerRef.value.showModal(params, driverApi.batchUpdateManager);
}


// 修改经营方式
const updateBusinessModeRef = ref();
function showUpdateBusinessModeModal () {
  if (selectedRowKeyList.value.length > 1) {
    message.error('请选择一条单据');
    return;
  }
  let driverId =  selectedRowKeyList.value[0];
  let params = { driverId };
  updateBusinessModeRef.value.showModal(params, driverApi.businessModeUpdate);
}


// ----------------- 显示快速创建弹窗 --------------------
const quickCreateRef = ref();

function showQuickCreateModal() {
  quickCreateRef.value.showModal();
}


// ----------------- 跳转 --------------------
let router = useRouter();
function addDriver(driverId) {
  router.push({ path: '/driver/operate', query: { driverId } })
}
function driverDetail(driverId) {
  router.push({ path: '/driver/detail', query: { driverId } })
}
function vehicleDetail(vehicleId) {
  router.push({ path: '/vehicle/vehicle-detail', query: { vehicleId } })
}

// ------------ 导出 Excel -----------
function exportExcel() {
  SmartLoading.show();
  let params = _.cloneDeep(queryForm);
  driverApi.downloadExcel('司机列表.xlsx', params);
  SmartLoading.hide();
}

onMounted(ajaxQuery);
</script>
<style lang="less" scoped>
.form-width {
  width: 240px;
}

.expired {
  color: red;
}
</style>
