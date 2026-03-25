<template>
  <a-form class="smart-query-form" v-privilege="'order:query'">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="货主">
        <ShipperSelect v-model:value="queryForm.shipperId" placeholder="请选择货主" width="200px"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="销售">
        <employee-select ref="salesRef" multiple v-model:value="queryForm.managerIdList"
          :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value" placeholder="请选择销售员" width="200px" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="调度员">
        <employee-select v-model:value="queryForm.scheduleId"
          :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SCHEDULE_ROLE_CODE.value" placeholder="请选择调度员"  width="200px"/>
      </a-form-item>
    </a-row>
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="关键字">
        <a-input v-model:value="queryForm.keywords" class="form-width" placeholder="订单号/创建人/货物名称" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="货物类型">
        <smart-dict-select width="200px" keyCode="cargoTypeClassificationCode"
          v-model:value="queryForm.cargoTypeClassificationCode" placeholder="请选择货物类型" />
      </a-form-item>
      <a-form-item label="创建时间" class="smart-query-form-item">
          <a-range-picker v-model:value="createDateRange" :presets="defaultTimeRanges" @change="changeCreateTime"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-checkbox v-model:checked="queryForm.hideCancelFlag" @change="search">隐藏作废单据</a-checkbox>
      </a-form-item>
      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
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
        <a-button size="small" type="primary" @click="addOrder()" v-privilege="'order:add'">
          <template #icon>
            <PlusOutlined />
          </template>
          新建订单
        </a-button>

        <a-button :disabled="selectedRowKeyList.length == 0" size="small" type="primary" danger
          @click="confirmCancel(selectedRowKeyList[0], ajaxQuery)" v-privilege="'order:cancel'">
          取消订单
        </a-button>

        <a-button @click="showScheduleModal" v-privilege="'order:scheduleDriver'" type="primary"
          :disabled="selectedRowKeyList.length == 0" size="small">
          调度分配司机
        </a-button>

        <a-button :disabled="selectedRowKeyList.length == 0" size="small" @click="reviseShipper()"
          v-privilege="'order:reviseShipper'">
          修改货主
        </a-button>

        <a-button @click="editOrder()" v-privilege="'order:edit'" size="small"
          :disabled="selectedRowKeyList.length == 0">
          编辑
        </a-button>

        <a-button @click="exportExcel()" v-privilege="'order:export'" size="small">导出</a-button>

        <a-button @click="importOrder" v-privilege="'order:import'" size="small">订单导入</a-button>

        <a-button v-privilege="'order:updateScheduleFlag'" :disabled="selectedRowKeyList.length == 0" size="small"
          type="primary" @click="showUpdateGoodsModal">
          修改货物
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.ORDER" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-tabs @tabClick="changeTab" v-model:activeKey="activeKey">
      <a-tab-pane :key="item.value" :tab="item.desc" v-for="item in tabList" />
    </a-tabs>

    <a-table :columns="columns" :dataSource="tableData" bordered :pagination="false"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }" :scroll="{ x: tableWidth, y: 500 }"
      rowKey="orderId" size="small" :loading="tableLoading">
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex === 'totalAmount'">
          <SmartHeaderCell :column="column">
            <template #title>
              <span>
                实际应收
                <a-tooltip placement="top">
                  <template #title>
                    <span>已分配运单的合计应收金额</span>
                  </template>
                  <question-circle-outlined />
                </a-tooltip>
              </span>
            </template>
          </SmartHeaderCell>

        </template>
        <template v-else-if="column.dataIndex === 'goodsTotalAmount'">
          <span>
            预计应收
            <a-tooltip placement="top">
              <template #title>
                <span>订单创建时所有货物的合计金额</span>
              </template>
              <question-circle-outlined />
            </a-tooltip>
          </span>
        </template>
        <template v-else>
          <SmartHeaderCell v-model:value="queryForm[column.filterOptions?.key || column.dataIndex]" :column="column" @change="change"/>
        </template>
      </template>
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'orderNo'">
          <a v-if="$privilege('order:detail')" @click="orderDetail(record.orderId)">{{ record.orderNo }}</a>
          <span v-else>{{ record.orderNo }}</span>
          <SmartCopyIcon :value="text" />
        </template>

        <template v-if="column.dataIndex === 'shortName'">
          <TextEllipsis :text="text" classKey="shortName"><a @click="shipperDetail(record.shipperId)">{{ text }}</a></TextEllipsis>
        </template>
        <template v-if="column.dataIndex === 'waybillCount'">
          <span v-if="record.waybillCount > 0">已分配(<a @click="toWaybillList(record.orderNo)">{{ text }}</a>)</span>
          <span v-else>0</span>
        </template>
        <template v-if="column.dataIndex === 'submitInvoiceFlag'">
          <span>{{ text ? '已提交' : '未提交' }}</span>
        </template>
        <template v-if="column.dataIndex === 'splitTransportFlag'">
          <span>{{ text ? '是' : '否' }}</span>
        </template>
        <template v-if="column.dataIndex === 'orderType'">
          {{ $smartEnumPlugin.getDescByValue('ORDER_TYPE_ENUM', text) }}
        </template>
        <template v-if="column.dataIndex === 'businessTypeCode'">
          {{ $smartEnumPlugin.getDescByValue('TRANSPORTATION_TYPE_ENUM', text) }}
        </template>
        <template v-if="column.dataIndex === 'status'">
          <a-tag :color="$smartEnumPlugin.getFieldByValue('ORDER_STATUS_ENUM', record.status, 'color')">{{
    $smartEnumPlugin.getDescByValue('ORDER_STATUS_ENUM', record.status)
  }}
          </a-tag>
        </template>
        <!-- 货物（名称 需求量/剩余量） -->
        <template v-if="column.dataIndex === 'goodsList'">
          <div v-for="item in record.goodsList" :key="item.goodsId" class="goods-item">
            <div class="goods-name">{{ item.goodsName }} </div>
            <span class="smart-margin-left5">{{ item.goodsQuantity  }}/</span>
            <span style="color: red;">{{ item.remainGoodsQuantity  }}</span>
          </div>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination v-model:current="queryForm.pageNum" v-model:pageSize="queryForm.pageSize"
        :defaultPageSize="queryForm.pageSize" :pageSizeOptions="PAGE_SIZE_OPTIONS" :show-total="(total) => `共${total}条`"
        :total="total" show-less-items showQuickJumper showSizeChanger @change="ajaxQuery"
        @showSizeChange="ajaxQuery" />
    </div>
    <ScheduleModal ref="scheduleModal" @refresh="ajaxQuery" />
    <UpdateGoodsModal ref="updateGoods" @refresh="ajaxQuery" />
    <!-- 修改货主 -->
    <ReviseShipperModal ref="reviseShipperRef" @refresh="ajaxQuery" />
  </a-card>
</template>
<script setup>
import ShipperSelect from '/@/components/shipper-select/index.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import ScheduleModal from './components/schedule-modal.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import UpdateGoodsModal from './components/update-goods-modal.vue';
import ReviseShipperModal from './components/revise-shipper-modal.vue';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { onMounted, reactive, ref, inject } from 'vue';
import { message } from 'ant-design-vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { orderApi } from '/@/api/business/order/order-api';
import { useRouter } from 'vue-router';
import { orderCancel } from './hooks/order-cancel';
import { columns, tableWidth } from './order-columns';
import _ from 'lodash';
import { FLAG_NUMBER_ENUM } from '/@/constants/common-const';
import { SmartLoading } from '/@/components/smart-loading';
import SmartCopyIcon from '/@/components/smart-copy-icon/index.vue'
import SmartHeaderCell from '/@/components/smart-table-header-cell/index.vue'
import TextEllipsis from '/@/components/text-ellipsis/index.vue'
import {defaultTimeRanges} from "/@/lib/default-time-ranges";
import { ORDER_STATUS_ENUM } from '/@/constants/business/order-const';

const smartEnumPlugin = inject('smartEnumPlugin');

// --------------------- 列表查询 ------------------------

const queryFormState = {
  keywords: '',
  pageNum: 1,
  pageSize: PAGE_SIZE,
  consignor:undefined,
  areaId: null,
  orderType: null,
  businessTypeCode: null,
  cabinetId: null,
  cargoTypeClassificationCode: null,
  containerBusinessTypeId: null,
  endTime: null,
  enterpriseId: null,
  scheduleId: null,
  startTime: null,
  scheduleFlag: null,
  shipperId: null,
  //隐藏作废单据
  hideCancelFlag: true,
  // 客户联系人
  shipperContact: null,
  managerIdList: [],
  splitTransportFlag: null,
  latestPackingTimeStart:null,
  latestPackingTimeEnd:null,
  latestPackingTime:[],
  createTime:[],
  orderNo:undefined,
  containerLocation:undefined,
  placingLocation:undefined,
  unloadingLocation:undefined,
  returnContainerLocation:undefined,
  status: null,
  createUserName:undefined,

  loadTime:[],
  startLoadTime:null,
  endLoadTime:null,
  unloadTime:[],
  startUnloadTime:null,
  endUnloadTime:null,

};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const selectedRowKeyList = ref([]);
const selectedRowList = ref([]);
const tableData = ref([]);
const total = ref(0);
const createDateRange = ref([]);

function onSelectChange(selectedRowKeys, selectedRows) {
  selectedRowKeyList.value = selectedRowKeys;
  selectedRowList.value = selectedRows;
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  queryForm.status = activeKey.value;
  createDateRange.value = [];
  ajaxQuery();
}

function change(obj){
  if(obj.key == 'createTime'){
    queryForm.startTime = obj.value[0];
    queryForm.endTime = obj.value[1];
    search()
    return
  }
  if(obj.key == 'latestPackingTime'){
    queryForm.latestPackingTimeStart = obj.value[0];
    queryForm.latestPackingTimeEnd = obj.value[1];
    search()
    return
  }
  if(obj.key == 'loadTime'){
    queryForm.startLoadTime = obj.value[0];
    queryForm.endLoadTime = obj.value[1];
    search()
    return
  }
  if(obj.key == 'unloadTime'){
    queryForm.startUnloadTime = obj.value[0];
    queryForm.endUnloadTime = obj.value[1];
    search()
    return
  }
  if(obj.search){
    search()
  }
}

function changeCreateTime (dates, dateStrings) {
    queryForm.startTime = dateStrings[0];
    queryForm.endTime = dateStrings[1];
}

function search() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery() {
  selectedRowKeyList.value = [];
  selectedRowList.value = [];
  try {
    tableLoading.value = true;
    let responseModel = await orderApi.queryOrder(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}


// ----------------- 列表操作 --------------------
let { confirmCancel } = orderCancel();

//显示调度弹窗
const scheduleModal = ref();

function showScheduleModal() {
  if (!_.isEmpty(selectedRowKeyList.value) && selectedRowKeyList.value.length > 1) {
    message.error('只能选择一条订单数据');
    return;
  }
  const { orderId } = selectedRowList.value[0];
  scheduleModal.value.showModal(orderId);
}


// ----------------- 更新分配状态 --------------------
const updateGoods = ref();

function showUpdateGoodsModal() {
  if (selectedRowKeyList.value.length > 1) {
    message.error('只能选择一条订单进行修改');
    return;
  }
  updateGoods.value.showModal(selectedRowKeyList.value[0]);
}
// 修改货主
const reviseShipperRef = ref()
function reviseShipper() {
  reviseShipperRef.value.showModal(selectedRowKeyList.value[0]);
}
// ----------------- 跳转 --------------------
let router = useRouter();

function addOrder() {
  router.push({ path: '/order/operate' });
}

function editOrder() {
  let orderId = selectedRowKeyList.value[0];
  router.push({ path: '/order/operate', query: { orderId } });
}

function orderDetail(orderId) {
  router.push({ path: '/order/detail', query: { orderId } });
}

function shipperDetail(shipperId) {
  router.push({ path: '/shipper/detail', query: { shipperId } });
}


function toWaybillList(orderNo) {
  router.push({
    path: '/waybill/waybill-list',
    query: {
      orderNo
    }
  });
}

// 跳转至订单导入页面
function importOrder() {
  router.push({
    path: '/order/import'
  });
}

// ------------ 导出 Excel -----------
function exportExcel() {
  SmartLoading.show();
  let params = _.cloneDeep(queryForm);
  orderApi.downloadExcel('订单列表.xlsx', params);
  SmartLoading.hide();
}

// ------------ Tab -----------
let tabList = ref([
  {
    desc: '全部',
    value: null
  },
  ...smartEnumPlugin.getValueDescList('ORDER_STATUS_ENUM')
]);

const activeKey = ref(null);

function changeTab(value) {
  queryForm.status = value;
  ajaxQuery();
}

onMounted(ajaxQuery);
</script>
<style lang="less" scoped>
.form-width {
  width: 240px;
}
.goods-item {
  display: flex;
  flex-direction: row;
  align-items: center;
}
.goods-name {
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-break: normal;
}

</style>
