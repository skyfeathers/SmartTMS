<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="关键字">
        <a-input
            v-model:value="queryForm.keyWords"
            class="form-width"
            placeholder="姓名/电话/创建人"
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
          <a-button type="primary" @click="addDriver">
            添加司机
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
      <template #driverName="{ record }">
        <a @click="driverDetail(record.driverId)">{{ record.driverName }} - {{ record.telephone }}</a>
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
  <DriverTableSelect ref="driverTableSelect" :multiple="true" @onChange="selectDriver"/>
</template>
<script setup>
import {onMounted, reactive, ref} from "vue";
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from "/@/constants/common-const";
import {fleetApi} from "/@/api/business/fleet/fleet-api";
import {useRouter} from "vue-router";
import {useFleetItemDelete} from "/@/views/business/fleet/hooks/use-fleet-item-delete";
import {useDriverCertificateValidity} from "/@/views/business/driver/hooks/use-driver-certificate-validity";
import DriverTableSelect from '/@/components/driver-table-select/index.vue'
import {useSpinStore} from "/@/store/modules/system/spin";
import {FLEET_ITEM_TYPE_ENUM} from "/@/constants/business/fleet-const";

let props = defineProps({
  fleetId: {
    type: [Number, String],
    require: true
  }
})
// --------------------- 列表查询 ------------------------
const columns = reactive([
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
    let responseModel = await fleetApi.queryFleetDriver(queryForm);
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

// ----------------------- 车队关联信息操作 -----------------------

let {confirmDelete} = useFleetItemDelete();

// ----------------- 跳转 --------------------
let router = useRouter();

function driverDetail(driverId) {
  router.push({path: '/driver/detail', query: {driverId}})
}

// ----------------- 添加司机 ------------------
let driverTableSelect = ref();

function addDriver() {
  driverTableSelect.value.showModal();
}

async function selectDriver(idList) {
  try {
    useSpinStore().show();
    let params = {
      fleetId: props.fleetId,
      itemType: FLEET_ITEM_TYPE_ENUM.DRIVER.value,
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
