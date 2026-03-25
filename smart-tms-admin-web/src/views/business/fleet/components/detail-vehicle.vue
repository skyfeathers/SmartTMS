<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="关键字">
        <a-input
            v-model:value="queryForm.keyWords"
            class="form-width"
            placeholder="车牌号/实际所属人"
        />
      </a-form-item>
      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-space>
          <a-button type="primary" @click="search">
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

          <a-button type="primary" @click="addVehicle">
            添加车辆
          </a-button>
        </a-space>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-table
        :columns="columns"
        :dataSource="tableData"
        :pagination="false"
        :scroll="{ x: 1300 }"
        rowKey="fleetItemId"
        size="small"
        bordered
    >
      <template #vehicleNumber="{ record }">
        <a @click="vehicleDetail(record.vehicleId)">{{ record.vehicleNumber }}</a>
      </template>

      <template #vehicleType="{ record }">
        <span v-if="!_.isEmpty(record.vehicleType)">{{record.vehicleType[0].valueName}}</span>
      </template>

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
        <a-button type="link" @click="confirmDelete(record.fleetItemId,search)">删除</a-button>
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
  </a-card>
  <VehicleTableSelect ref="vehicleTableSelect" :multiple="true" @onChange="selectVehicle"/>
</template>
<script setup>
import {onMounted, reactive, ref} from "vue";
import {AUDIT_STATUS_ENUM, PAGE_SIZE, PAGE_SIZE_OPTIONS} from "/@/constants/common-const";
import {fleetApi} from "/@/api/business/fleet/fleet-api";
import {useRouter} from "vue-router";
import {useFleetItemDelete} from "/@/views/business/fleet/hooks/use-fleet-item-delete";
import VehicleTableSelect from '/@/components/vehicle-table-select/index.vue'
import {useSpinStore} from "/@/store/modules/system/spin";
import {FLEET_ITEM_TYPE_ENUM} from "/@/constants/business/fleet-const";
import {useVehicleExpired} from "/@/views/business/vehicle/hooks/use-vehicle-expired";
import _ from 'lodash';
let props = defineProps({
  fleetId: {
    type: [Number, String],
    require: true
  }
})
// --------------------- 列表查询 ------------------------
const columns = reactive([
  {
    title: "车牌号",
    dataIndex: "vehicleNumber",
    width: 180,
    fixed: "left",
    slots: {customRender: "vehicleNumber"},
  },
  {
    title: "车辆类型",
    width: 100,
    dataIndex: "vehicleType",
    slots: { customRender: 'vehicleType' }
  },
  {
    title: "载重（吨）",
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
    width: 100,
    slots: {customRender: "action"},
  },
]);

const queryFormState = {
  keyWords: "",
  fleetId: undefined,
  pageNum: 1,
  pageSize: PAGE_SIZE
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

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
    queryForm.fleetId = props.fleetId;
    let responseModel = await fleetApi.queryFleetVehicle(queryForm);
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

// ----------------------- 车队关联信息操作 -----------------------

let {confirmDelete} = useFleetItemDelete();

// ----------------- 跳转 --------------------
let router = useRouter();

function vehicleDetail(vehicleId) {
  router.push({path: '/vehicle/vehicle-detail', query: {vehicleId}})
}

// ----------------- 添加车辆 ------------------
let vehicleTableSelect = ref();

function addVehicle() {
  vehicleTableSelect.value.showModal();
}

async function selectVehicle(idList) {
  try {
    useSpinStore().show();
    let params = {
      fleetId: props.fleetId,
      itemType: FLEET_ITEM_TYPE_ENUM.VEHICLE.value,
      itemIdList: idList
    }
    await fleetApi.addFleetItem(params);
    await ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
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
