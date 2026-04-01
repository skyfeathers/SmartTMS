<!--
 * @Description: 车辆列表
 * @version:
 * @Author: zhuoda
 * @Date: 2022-07-07 15:41:39
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-21
-->
<template>
  <a-form class="smart-query-form" v-privilege="'vehicle:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 200px" v-model:value="queryForm.vehicleKeyWords" placeholder="车牌号" />
      </a-form-item>
      <a-form-item label="所属人性质" class="smart-query-form-item">
        <SmartEnumSelect width="200px" v-model:value="queryForm.ownerType" placeholder="所属人性质" enum-name="VEHICLE_OWNER_TYPE_ENUM" />
      </a-form-item>
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

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="addVehicle" type="primary" size="small" v-privilege="'vehicle:add'">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建车辆
        </a-button>

        <a-button @click="batchAudit" v-privilege="'vehicle:batchAudit'" type="primary" size="small"
                  :disabled="selectedRowKeyList.length == 0"> 审核
        </a-button>
        <a-button @click="confirmEnabled(true)" v-privilege="'vehicle:updateEnable'" type="primary" size="small"
                  :disabled="selectedRowKeyList.length == 0"> 启用
        </a-button>
        <a-button @click="confirmEnabled(false)" v-privilege="'vehicle:updateDisable'" type="primary" danger  size="small"
                  :disabled="selectedRowKeyList.length == 0"> 禁用
        </a-button>
        <a-button @click="confirmDelete" type="primary" danger  v-privilege="'vehicle:delete'" size="small"
                  :disabled="selectedRowKeyList.length == 0"> 删除
        </a-button>

        <a-button @click="showUpdateManagerModal()" :disabled="selectedRowKeyList.length == 0"
                  v-privilege="'vehicle:updateManager'" size="small">
          <template #icon>
            <AuditOutlined/>
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

        <a-button v-privilege="'vehicle:quickCreate'" size="small" type="primary" @click="showQuickCreateModal()">
          快速新建
        </a-button>

        <a-button @click="exportExcel()" v-privilege="'vehicle:export'" size="small">导出</a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.VEHICLE" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-tabs @tabClick="changeTab" v-model:activeKey="activeKey">
      <a-tab-pane key="" tab="全部" />
        <a-tab-pane :key="item.value" :tab="item.desc" v-for="(item) in $smartEnumPlugin.getValueDescList('DRIVER_BUSINESS_MODE_ENUM')" />
    </a-tabs>

    <a-table
      :scroll="{ x: '100%' }"
      size="small"
      bordered
      :dataSource="tableData"
      :columns="columns"
      rowKey="vehicleId"
      :pagination="false"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
      :loading="tableLoading"
    >
      <template #headerCell="{ column }">
          <SmartHeaderCell v-model:value="queryForm[column.filterOptions?.key || column.dataIndex]" :column="column"
            @change="change" />
      </template>
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'disabledFlag'">
          <a-tag v-show="!text" color="success">启用</a-tag>
          <a-tag v-show="text" color="error">禁用</a-tag>
        </template>
        <template v-if="column.dataIndex === 'vehicleNumber'">
          <a v-if="$privilege('vehicle:detail')" @click="vehicleDetail(record.vehicleId)" type="link">{{ text }}</a>
          <span v-else>{{text}}</span>
        </template>
        <template v-if="column.dataIndex === 'vehicleType'">
          <span>{{ getVehicleType(text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'businessMode'">
          <span>{{ $smartEnumPlugin.getDescByValue('VEHICLE_BUSINESS_MODE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'ownerType'">
          <span>{{ $smartEnumPlugin.getDescByValue('VEHICLE_OWNER_TYPE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'bracketNo'">
          <a v-if="$privilege('bracket:detail')" type="link" @click="bracketDetail(record.bracketId)">{{ text }} </a>
          <span v-else>{{text}}</span>
        </template>
        <template v-if="column.dataIndex === 'driverList'">
          <template v-if="$privilege('driver:detail')">
            <a-button v-if="!$lodash.isEmpty(record.driverVehicleVOList) && record.driverVehicleVOList.length > 0" type="link"
                      @click="driverDetail(record.driverVehicleVOList[0].driverId)">{{ record.driverVehicleVOList[0].driverName }} &nbsp;
            </a-button>
            <a-button v-if="!$lodash.isEmpty(record.driverVehicleVOList) && record.driverVehicleVOList.length > 1" type="link"
                      @click="moreDriver(record.driverVehicleVOList, false)">更多</a-button>
          </template>
          <template v-else>
            <span v-if="!$lodash.isEmpty(record.driverVehicleVOList) && record.driverVehicleVOList.length > 0">{{ record.driverVehicleVOList[0].driverName }}</span>
            <a-button v-if="!$lodash.isEmpty(record.driverVehicleVOList) && record.driverVehicleVOList.length > 1" type="link"
                      @click="moreDriver(record.driverVehicleVOList, true)">更多</a-button>
          </template>
        </template>
        <!-- 保养提示 -->
        <template v-if="column.dataIndex === 'maintenance'">
            {{getMaintenanceMsg(record)}}
        </template>
        <template v-if="column.dataIndex === 'auditStatus'">
          <a-tag v-show="text === AUDIT_STATUS_ENUM.AUDIT_PASS.value" color="success">{{ AUDIT_STATUS_ENUM.AUDIT_PASS.desc }}</a-tag>
          <a-tag v-show="text === AUDIT_STATUS_ENUM.WAIT_AUDIT.value" color="warning">{{ AUDIT_STATUS_ENUM.WAIT_AUDIT.desc }}</a-tag>
          <a-tag v-show="text === AUDIT_STATUS_ENUM.REJECT.value" color="error">{{ AUDIT_STATUS_ENUM.REJECT.desc }}</a-tag>
        </template>
        <template v-if="column.dataIndex === 'operate'">
          <div class="smart-table-operate">
            <a-button @click="updateVehicle(record.vehicleId)" v-privilege="'vehicle:edit'" type="link">编辑</a-button>
          </div>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
        showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="ajaxQuery"
        @showSizeChange="ajaxQuery"
        :show-total="(total) => `共${total}条`"
      />
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
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import UpdateManagerModal from '/@/components/update-manager-modal/index.vue';
import UpdateBusinessModeModal from "/@/components/update-business-mode/index.vue";
import AuditModal from '/@/components/audit-modal/index.vue';
import VehicleQuickCreate from '/@/components/vehicle-quick-create/index.vue';
import { reactive, ref, onMounted, computed } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { vehicleApi } from '/@/api/business/vehicle/vehicle-api';
import { useRouter } from 'vue-router';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { columns,tableWidth } from './components/vehicle-list-table-column';
import { AUDIT_STATUS_ENUM } from '/@/constants/common-const';
import { SmartLoading } from '/@/components/smart-loading';
import { deleteVehicle } from './components/vehicle-operate';
import _ from 'lodash';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import DriverVehicleModal from '/@/views/business/driver/components/driver-vehicle-list-model.vue';
import SmartHeaderCell from '/@/components/smart-table-header-cell/index.vue'
import { QUICK_CREATE_TYPE_ENUM } from '/@/constants/business/vehicle-const';

function change(obj){
  if(obj.key == 'createTime'){
    queryForm.createStartTime = obj.value[0];
    queryForm.createEndTime = obj.value[1];
    search()
    return
  }
  if(obj.key == 'relyEnterpriseExpireDate'){
    queryForm.relyEnterpriseStartTime = obj.value[0];
    queryForm.relyEnterpriseEndTime = obj.value[1];
    search()
    return
  }
  if(obj.key == 'vehicleAuditExpireDate'){
    queryForm.vehicleAuditExpireStartTime = obj.value[0];
    queryForm.vehicleAuditExpireEndTime = obj.value[1];
    search()
    return
  }
  if(obj.key == 'roadTransportCertificateExpireDate'){
    queryForm.roadTransportCertificateExpireDateStartTime = obj.value[0];
    queryForm.roadTransportCertificateExpireDateEndTime = obj.value[1];
    search()
    return
  }

  if(obj.key == 'businessMode'){
    activeKey.value = obj.value;
  }

  if(obj.search){
    search()
  }
}

//展开、收起
const showMoreQueryConditionFlag = ref(false);

const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  //车牌号
  vehicleKeyWords: null,
  bracketNo:undefined,
  //车辆类型
  vehicleType: null,
  //经营方式
  businessMode: null,
  //挂靠企业名称
  // relyEnterpriseName: null,
  //所属人性质
  ownerType: null,
  //审核状态
  auditStatus: null,
  //年审时间-开始时间
  vehicleAuditExpireStartTime: null,
  //年审时间-结束时间
  vehicleAuditExpireEndTime: null,
  //创建时间-开始时间
  createStartTime: null,
  //创建时间-结束时间
  createEndTime: null,
  //挂靠到期-开始时间
  relyEnterpriseStartTime: null,
  //挂靠到期-结束时间
  relyEnterpriseEndTime: null,
  //道路运输证到期时间-开始时间
  roadTransportCertificateExpireDateStartTime: null,
  //道路运输证到期时间-结束时间
  roadTransportCertificateExpireDateEndTime: null,
  // 禁用状态
  disabledFlag: null,
  // 所属公司
  enterpriseIdList: [],
  roadTransportCertificateNumber:undefined,
  vehicleNumber: undefined,
  shorthand: undefined,
  roadTransportCertificateExpireDate:[],
  vehicleAuditExpireDate:[],
  relyEnterpriseExpireDate:[],
  createTime:[]
};

const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const activeKey = ref('');

function changeTab (value) {
  queryForm.businessMode = value || null;
  ajaxQuery();
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let data = { ...queryForm };
    if(queryForm.disabledFlag === false){
      data.disabledFlag = true
    }else if(queryForm.disabledFlag === true){
      data.disabledFlag = false
    }
    let responseModel = await vehicleApi.query(data);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
    clearSelected();
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// 获取车辆类型
function getVehicleType(text) {
  if (_.isEmpty(text)) {
    return '';
  }
  return text[0].valueName;
}
// ----------- 新建 、修改、详情-----------

let router = useRouter();
function addVehicle(vehicleId) {
  router.push({ path: '/vehicle/vehicle-form' });
}
function updateVehicle(vehicleId) {
  router.push({ path: '/vehicle/vehicle-form', query: { vehicleId } });
}
function vehicleDetail(vehicleId) {
  router.push({ path: '/vehicle/vehicle-detail', query: { vehicleId } });
}

function driverDetail (driverId) {
  router.push({
    path: '/driver/detail',
    query: {
      driverId
    }
  });
}

function bracketDetail (bracketId) {
  router.push({
    path: '/bracket/detail',
    query: {
      bracketId
    }
  });
}

const driverVehicleModal = ref();
function moreDriver(driverVehicleVOList, disableLink) {
    driverVehicleModal.value.showModal(driverVehicleVOList, disableLink);
}

// ----------- table 批量操作 start -----------
const selectedRowKeyList = ref([]);
const hasSelected = computed(() => selectedRowKeyList.value.length > 0);

function onSelectChange(keyArray) {
  selectedRowKeyList.value = keyArray;
}

function clearSelected() {
  selectedRowKeyList.value = [];
}

// 启用 / 禁用
function confirmEnabled(enabled) {
  if (!hasSelected.value) {
    message.warning('请先选择车辆');
    return;
  }
  let text = enabled ? '启用' : '禁用';
  Modal.confirm({
    title: '提示',
    content: `确认要${text}${selectedRowKeyList.value.length}个车辆吗？`,
    okText: text,
    okType: enabled ? 'primary' : 'danger',
    onOk: () => {
      (async () => {
        try {
          SmartLoading.show();
          if (!enabled) {
            await vehicleApi.batchEnabled(selectedRowKeyList.value);
          } else {
            await vehicleApi.batchDisabled(selectedRowKeyList.value);
          }
          message.success('操作成功');
          ajaxQuery();
        } catch (e) {
          console.log(e);
        } finally {
          SmartLoading.hide();
        }
      })();
    },
  });
}

function confirmDelete() {
  if (!hasSelected.value) {
    message.warning('请先选择车辆');
    return;
  }
  deleteVehicle(selectedRowKeyList.value, () => {
    clearSelected();
    ajaxQuery();
  });
}

// ----------------- 审核 --------------------
let auditModal = ref();

function batchAudit() {
  if (!hasSelected.value) {
    message.warning('请选择待审核的车辆！');
    return;
  }

  for (const item of tableData.value) {
    if (_.includes(selectedRowKeyList.value, item.vehicleId)) {
      if (item.auditStatus !== AUDIT_STATUS_ENUM.WAIT_AUDIT.value) {
        message.warning('请选择待审核的车辆！请移除车【' + item.vehicleNumber + '】');
        return;
      }
    }
  }

  let params = {
    vehicleIdList: selectedRowKeyList.value,
  };
  auditModal.value.showModal(params, vehicleApi.batchAudit);
}

function handleFinish() {
  clearSelected();
  ajaxQuery();
}

// ----------------- 修改负责人 --------------------

const updateManagerRef = ref();

function showUpdateManagerModal() {
  let vehicleIdList = selectedRowKeyList.value;
  let params = { vehicleIdList };
  updateManagerRef.value.showModal(params, vehicleApi.batchUpdateManager);
}

// 修改经营方式
const updateBusinessModeRef = ref();
function showUpdateBusinessModeModal () {
  if (selectedRowKeyList.value.length > 1) {
    message.error('请选择一条单据');
    return;
  }
  let vehicleId =  selectedRowKeyList.value[0];
  let params = { vehicleId };
  updateBusinessModeRef.value.showModal(params, vehicleApi.businessModeUpdate);
}


// ----------------- 显示快速创建弹窗 --------------------
const quickCreateRef = ref();

function showQuickCreateModal () {
  quickCreateRef.value.showModal(QUICK_CREATE_TYPE_ENUM.VEHICLE.value);
}

// ------------ 导出 Excel -----------
function exportExcel () {
  SmartLoading.show();
  let params = _.cloneDeep(queryForm);
  vehicleApi.downloadExcel('车辆列表.xlsx', params);
  SmartLoading.hide();
}
// 展示保养提示
function getMaintenanceMsg (item) {
  if(item.nextMaintenanceDate){
    return `下次保养时间为：${item.nextMaintenanceDate}`;
  }
  if(item.nextMaintenanceMileage){
    return `下次保养里程为：${item.nextMaintenanceMileage}`;
  }
}

onMounted(ajaxQuery);
</script>
<style scoped lang="less"></style>
