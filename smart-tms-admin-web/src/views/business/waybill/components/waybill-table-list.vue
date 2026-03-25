<!--
 * @Description: 运单列表
 * @version:
 * @Author: zhuoda
 * @Date: 2022-07-07 15:41:39
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-06
-->
<template>
  <a-form v-privilege="`${privilegePrefix}:query`" class="smart-query-form" :label-col="{  style: { width: '80px' } }" v-show="showQueryForm">
    <a-row class="smart-query-form-row" :gutter="24">
      <a-col :span="6">
        <a-form-item class="smart-query-form-item" label="运单号">
          <a-textarea v-model:value="queryForm.waybillNumbers" placeholder="运单号，多个以,进行拼接" style="width:100%"
                      :row="4"/>
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item class="smart-query-form-item" label="销售">
          <employee-select ref="salesRef" multiple v-model:value="queryForm.managerIdList"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value" placeholder="请选择销售员" width="100%" />
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item class="smart-query-form-item" label="调度员">
          <RoleEmployeeSelect v-model:value="queryForm.scheduleIdList"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SCHEDULE_ROLE_CODE.value" multiple placeholder="请选择调度员"
            width="100%" />
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item class="smart-query-form-item" label="客服">
          <RoleEmployeeSelect v-model:value="queryForm.customerIdList"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.CUSTOMER_SERVICE_ROLE_CODE.value" multiple placeholder="请选择客服"
            width="100%" />
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item class="smart-query-form-item" label="货主">
          <ShipperSelect v-model:value="queryForm.shipperId" placeholder="请选择货主" width="100%"/>
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item class="smart-query-form-item" label="车辆">
          <VehicleSelect v-model:value="queryForm.vehicleIdList" multiple width="100%" />
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item class="smart-query-form-item" label="司机">
          <DriverSelect v-model:value="queryForm.driverIdList" multiple width="100%" />
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item class="smart-query-form-item" label="关键字">
          <a-input v-model:value="queryForm.keywords" placeholder="运单号/创建人/订单号/箱号/铅封号" style="width: 100%" />
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item class="smart-query-form-item" label="车辆经营方式">
          <SmartEnumSelect width="100%" v-model:value="queryForm.vehicleBusinessMode" multiple placeholder="经营方式"
            enum-name="VEHICLE_BUSINESS_MODE_ENUM" />
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item class="smart-query-form-item" label="运单异常类型">
          <smart-enum-select v-model:value="queryForm.exceptionTypeList" enumName="WAYBILL_EXCEPTION_TYPE_ENUM"
            mode="multiple" placeholder="请选择异常类型" />
        </a-form-item>
      </a-col>
      <a-col :span="6">
        <a-form-item label="创建时间" class="smart-query-form-item">
            <a-range-picker v-model:value="createDateRange" :presets="defaultTimeRanges" @change="changeCreateTime"/>
        </a-form-item>
      </a-col>
    </a-row>
    <div class="search-btn-block">
          <a-form-item class="smart-query-form-item">
            <a-checkbox v-model:checked="queryForm.hideCancelFlag" @change="search">隐藏作废单据</a-checkbox>
          </a-form-item>
          <a-button type="primary" @click="search" class="smart-margin-left10">
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
    </div>

  </a-form>

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <slot name="actionList"></slot>
    </a-row>

    <!-- 金额统计 -->
    <span class="smart-statistics">
      合计：
      应收金额：<span class="amount">{{ formatMoneyStr(amountStatistics?.receiveAmount) }}</span>元，
      司机工资：<span class="amount">{{ formatMoneyStr(amountStatistics?.salaryAmount) }}</span>元，
      在途费用：<span class="amount">{{ formatMoneyStr(amountStatistics?.carCostAmount) }}</span>元，
      应付金额：<span class="amount">{{ formatMoneyStr(amountStatistics?.payableAmount) }}</span>元，
      利润总额：<span class="amount">{{ formatMoneyStr(amountStatistics?.profitAmount) }}</span>元。

      <div v-if="selectedRowKeyList.length !== 0">
        选中行：应收金额：<span class="amount">{{ formatMoneyStr(selectedStatisticInfo?.receiveAmount) }}</span>元，
        应付金额：<span class="amount">{{ formatMoneyStr(selectedStatisticInfo?.payableAmount) }}</span>元，
        利润总额：<span class="amount">{{ formatMoneyStr(selectedStatisticInfo?.profitAmount) }}</span>元。
      </div>
    </span>
    <slot name="tabList"></slot>
    <a-table :columns="columns" :dataSource="tableData" :loading="tableLoading" :pagination="false"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }" :scroll="{ x: tableWidth, y: 400 }"
      bordered rowKey="waybillId" size="small">
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex === 'taxAmount'">
          <SmartHeaderCell :column="column">
            <template #title>
              <span>
                税金
                <a-tooltip placement="top">
                  <template #title>
                    <span>税金 = 应收 * 税点</span>
                  </template>
                  <question-circle-outlined />
                </a-tooltip>
              </span>
            </template>
          </SmartHeaderCell>
        </template>
        <template v-else-if="column.dataIndex === 'profitAmount'">
          <SmartHeaderCell :column="column">
            <template #title>
              <span>
                利润
                <a-tooltip placement="top">
                  <template #title>
                    <span>利润 = 应收 - 应付 - (应收 * 税点)</span>
                  </template>
                  <question-circle-outlined />
                </a-tooltip>
              </span>
            </template>
          </SmartHeaderCell>
        </template>
        <template v-else-if="column.dataIndex === 'paidAmount'">
          <SmartHeaderCell :column="column">
            <template #title>
              <span>
                已付金额
                <a-tooltip placement="top">
                  <template #title>
                    <span>提交应付结算后财务已支付的金额</span>
                  </template>
                  <question-circle-outlined />
                </a-tooltip>
              </span>
            </template>
          </SmartHeaderCell>
        </template>
        <template v-else>
          <SmartHeaderCell v-model:value="queryForm[column.filterOptions?.key || column.dataIndex]" :column="column"
            @change="change" />
        </template>
      </template>
      <template #bodyCell="{ text, record, index, column }">

        <template v-if="column.dataIndex === 'waybillNumber'">
          <div class="waybill-code">
            <a v-if="$privilege(`${privilegePrefix}:detail`)" type="link" @click="waybillDetail(record.waybillId)">{{
    text }}</a>
            <span v-else>{{ text }}</span>
            <a style="margin:0 10px" v-if="!$lodash.isEmpty(record.contractFile)"
              @click="showContractAttachmentModal(record)">
              <file-outlined />
            </a>
            <SmartCopyIcon :value="text" />
          </div>
        </template>
        <template v-if="column.dataIndex === 'containerNumber'">
          <div>{{ text }}<SmartCopyIcon v-if="text" :value="text" /></div>
        </template>
        <template v-if="column.dataIndex === 'leadSealNumber'">
          <div>{{ text }}<SmartCopyIcon v-if="text" :value="text" /></div>
        </template>
        <template v-if="column.dataIndex === 'orderNo'">
          <a type="link" @click="orderDetail(record.orderId)">{{ text }}</a>
          <SmartCopyIcon :value="text" />
        </template>
        <template v-if="column.dataIndex === 'receiveOrderNumberList'">
          {{ getReceiveOrderNumber(record) }}
        </template>

        <template v-if="column.dataIndex === 'payOrderCount'">
          <div>
            <span v-if="record.submitPayFlag">已提交(<a type="link" @click="showPayOrderModal(record.waybillId)">{{ text
                }}</a>)</span>
            <span v-else>未提交</span>
          </div>
        </template>
        <template v-if="column.dataIndex === 'submitReceiveFlag'">
          <div>
            <span v-if="(record.receiveOrderNumberList || []).length == 0">未提交</span>
            <span v-else>已提交(<a @click="showReceiveOrderModal(record.waybillId)">{{
    (record.receiveOrderNumberList).length
  }}</a>)</span>
          </div>
        </template>
        <template v-if="column.dataIndex === 'confirmReceiveFlag'">
          <div>
            <span v-if="record.submitReceiveFlag">
              <a @click="showReceiveOrderModal(record.waybillId)">已确认</a>
            </span>
            <span v-else>
              <span v-if="(record.receiveOrderNumberList || []).length == 0">未确认</span>
              <span v-else><a @click="showReceiveOrderModal(record.waybillId)">部分确认</a></span>
            </span>
          </div>
        </template>
        <template v-if="column.dataIndex === 'shipperName'">
          <TextEllipsis :text="text" :classKey="column.dataIndex">
            <a type="link" @click="shipperDetail(record.shipperId)">{{ text }}</a>
          </TextEllipsis>
        </template>
        <template v-if="column.dataIndex === 'orderType'">
          {{ $smartEnumPlugin.getDescByValue('ORDER_TYPE_ENUM', text) }}
        </template>
        <template v-if="column.dataIndex === 'settleMode'">
          {{ $smartEnumPlugin.getDescByValue('SETTLE_MODE_ENUM', record.settleMode) }}
        </template>
        <template v-if="column.dataIndex === 'settleType'">
          {{ $smartEnumPlugin.getDescByValue('SETTLE_TYPE_ENUM', record.settleType) }}
        </template>
        <template v-if="column.dataIndex === 'transportMode'">
          {{ $smartEnumPlugin.getDescByValue('WAYBILL_TRANSPORT_MODE_ENUM', record.transportMode) }}
        </template>
        <template v-if="column.dataIndex === 'auditStatus'">
          <a-tag :color="$smartEnumPlugin.getFieldByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus, 'color')">{{
    $smartEnumPlugin.getDescByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus)
  }}</a-tag>
        </template>
        <template v-if="column.dataIndex === 'splitTransportFlag'">
          <span>{{ text ? '是' : '否' }}</span>
        </template>
        <template v-if="column.dataIndex === 'businessDate'">
          {{ (text || '').substring(0, 7) }}
        </template>
        <template v-if="column.dataIndex === 'receiptAttachment'">
          <a @click="showReceiptAttachmentModal(record)" v-if="record.receiptAttachment">查看附件</a>
        </template>
        <template v-if="column.dataIndex === 'truckOrderAttachment'">
          <file-preview :fileList="record.truckOrderAttachment" type="picture" />
        </template>
        <template v-if="column.dataIndex === 'attachment'">
          <file-preview :fileList="record.attachment" />
        </template>

        <template v-if="column.dataIndex === 'driverName'">
          <div v-if="record.splitTransportFlag" style="display: flex">
            <span>{{ text }}</span>
            <a @click="showSplitTransportModal(record.waybillId)">更多</a>
          </div>
          <span v-else>{{ text }}</span>
        </template>

        <template v-if="column.dataIndex === 'waybillStatus'">
          <a-tag :color="$smartEnumPlugin.getFieldByValue('WAYBILL_STATUS_ENUM', record.waybillStatus, 'color')">{{
    $smartEnumPlugin.getDescByValue('WAYBILL_STATUS_ENUM', record.waybillStatus)
  }}</a-tag>
        </template>
        <template v-if="column.dataIndex === 'exceptionList'">
          <div style="display: flex;flex-direction: column">
            <span v-for="item in text" :key="item.exceptionType">
              {{ $smartEnumPlugin.getDescByValue('WAYBILL_EXCEPTION_TYPE_ENUM', item.exceptionType) }}
            </span>
          </div>
        </template>
        <!-- 应付现金 -->
        <template v-if="column.dataIndex === 'payableCashAmount'">
          <div class="tag-show">
            <div class="label">{{ record.payableCashAmount }}</div>
            <a-tag size="small" :color="$smartEnumPlugin.getFieldByValue('PAY_ORDER_STATUS_ENUM', record.cashPayStatus , 'color')">
            {{ $smartEnumPlugin.getDescByValue('PAY_ORDER_STATUS_ENUM', record.cashPayStatus) }}</a-tag>
          </div>
        </template>
        <!-- 应付油卡 -->
        <template v-if="column.dataIndex === 'payableOilCardAmount'">
          <div class="tag-show">
            <div class="label">{{ record.payableOilCardAmount }}</div>
            <a-tag size="small" :color="$smartEnumPlugin.getFieldByValue('PAY_ORDER_STATUS_ENUM', record.oilCardPayStatus  , 'color')">
            {{ $smartEnumPlugin.getDescByValue('PAY_ORDER_STATUS_ENUM', record.oilCardPayStatus)}}
          </a-tag>
          </div>
        </template>
        <!-- 装货磅重 -->
        <template v-if="column.dataIndex === 'loadingPoundListQuantity'">
          <div>
            <div v-if="record.loadPoundListQuantity">
              <span>重量：{{ record.loadPoundListQuantity }}</span>
              <span 
                class="preview-link"
                v-if="record.loadPoundListAttachment" 
                @click="showPreview(record.loadPoundListAttachment)"
              >
                查看凭证
              </span>
            </div>
          </div>
        </template>
        <!-- 卸货磅重 -->
        <template v-if="column.dataIndex === 'unloadingPoundListQuantity'">
          <div>
            <div v-if="record.unloadPoundListQuantity">
              <span>重量：{{ record.unloadPoundListQuantity }}</span>
              <span 
                class="preview-link"
                v-if="record.unloadPoundListAttachment" 
                @click="showPreview(record.unloadPoundListAttachment)"
              >
                查看凭证
              </span>
            </div>
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
  </a-card>
  <WaybillPayOrderModal ref="waybillPayOrderModal" />
  <WaybillReceiveOrderModal ref="waybillReceiveOrderModal" />
  <WaybillSplitTransportModal ref="splitTransportModalRef" />
  <a-modal v-model:open="showReceiptAttachmentFlag" title="附件预览" :footer="null" width="500px">
    <a-card>
      <Upload :default-file-list="receiptAttachmentList" listType="picture-card" :showUploadBtn="false" />
    </a-card>
  </a-modal>
  <a-modal v-model:open="showContractAttachmentFlag" title="合同预览" :footer="null" width="500px">
    <a-card>
      <Upload :default-file-list="contractAttachmentList" listType="picture-card" :showUploadBtn="false" />
    </a-card>
  </a-modal>

  <a-image-preview-group :preview="{ visible: previewVisible, onVisibleChange: setVisible, current: 0 }">
    <a-image v-for="item in previewList" :key="item.fileKey" :style="{ display: 'none' }" :src="item.fileUrl" />
  </a-image-preview-group>
</template>
<script setup>
import RoleEmployeeSelect from '/@/components/role-employee-select/index.vue';
import ShipperSelect from '/@/components/shipper-select/index.vue';
import DriverSelect from '/@/components/driver-select/index.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import WaybillPayOrderModal from './waybill-pay-order-modal.vue';
import WaybillReceiveOrderModal from './waybill-receive-order-modal.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import FilePreview from '/@/components/file-preview/index.vue';
import Upload from '/@/components/upload/index.vue';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import WaybillSplitTransportModal from './waybill-split-transport-modal.vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { reactive, ref, computed, nextTick } from 'vue';
import { useRoute, useRouter, viewDepthKey } from 'vue-router';
import { orderCancel } from '/@/views/business/order/hooks/order-cancel';
import { SmartLoading } from '/@/components/smart-loading';
import _ from "lodash"
import dayjs from 'dayjs';
import { formatMoney } from '/@/utils/money-util';
import SmartCopyIcon from '/@/components/smart-copy-icon/index.vue'
import SmartHeaderCell from '/@/components/smart-table-header-cell/index.vue'
import TextEllipsis from '/@/components/text-ellipsis/index.vue'
import {defaultTimeRanges} from "/@/lib/default-time-ranges";
import { Modal } from 'ant-design-vue';
const props = defineProps({
  // 表格列数组
  columns: {
    type: Array,
    default: new Array(),
  },
  privilegePrefix: {
    type: String,
    default: 'waybill'
  },
  tableWidth:{
    type:Number,
    default:7000
  },
  // 是否显示查询表单
  showQueryForm:{
    type:Boolean,
    default:true
  },
  // tab参数
  tabParams:{
    type:Object,
    default:()=>{}
  }
});

// ----------- 计算属性 start -----------

const selectedStatisticInfo = computed(() => {
  // 计算应收金额
  let receiveAmountList = selectedDataList.value.map(e => e.receiveAmount);
  let receiveTotalAmount = receiveAmountList.reduce((a, b) => {
    return a + b;
  });
  receiveTotalAmount = Number(receiveTotalAmount.toFixed(2));

  // 计算应付金额
  let payableAmountList = selectedDataList.value.map(e => e.payableAmount);
  let payableTotalAmount = payableAmountList.reduce((a, b) => {
    return a + b;
  });
  payableTotalAmount = Number(payableTotalAmount.toFixed(2));

  // 计算利润总额
  let profitAmountList = selectedDataList.value.map(e => e.profitAmount);
  let profitTotalAmount = profitAmountList.reduce((a, b) => {
    return a + b;
  });
  profitTotalAmount = Number(profitTotalAmount.toFixed(2));
  return {
    receiveAmount: receiveTotalAmount,
    payableAmount: payableTotalAmount,
    profitAmount: profitTotalAmount
  };
})

const emit = defineEmits(['changeSelect', 'resetQuery']);
// ----------- 列表查询 start -----------

let route = useRoute();

const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  waybillNumbers: null,
  //关键字
  keywords: null,
  //结算类型
  settleType: null,
  //车队
  fleetId: null,
  //司机
  driverIdList: [],
  //车辆
  vehicleIdList: [],
  //车辆类型
  vehicleBusinessMode: [],
  //销售
  managerIdList: [],
  //公司id
  enterpriseIdList: [],
  //状态
  waybillStatusList: [],
  //运单类型
  orderType: null,
  //运单-开始时间
  startTime: null,
  //运单-结束时间
  endTime: null,
  // 或者ID
  shipperId: null,
  // 结算方式
  settleMode: null,
  // 是否提交过收款
  submitReceiveFlag: null,
  // 创建人角色
  createUserRoleId: null,
  // 运输方式
  transportMode: null,
  // 异常类型
  exceptionTypeList: [],
  businessStartDate: null,
  businessEndDate: null,
  // 调度
  scheduleIdList: [],
  // 客服
  customerIdList: [],
  splitTransportFlag: null,
  waybillNumber: undefined,
  orderNo: undefined,
  shipperOrderNumber: undefined,
  containerNumber: undefined,
  leadSealNumber: undefined,
  cabinetId: undefined,
  businessDate: [],

  deliverGoodsTime: [],
  auditStatus: undefined,
  containerLocation: undefined,
  placingLocation: undefined,
  unloadingLocation: undefined,
  returnContainerLocation: undefined,
  createTime: [],
  latestPackingTime: [],
  receiveGoodsTime:[],

  loadTime: [],
  startLoadTime: null,
  endLoadTime: null,

  unloadTime: [],
  startUnloadTime: null,
  endUnloadTime: null,

  // 是否隐藏作废单据
  hideCancelFlag: true,
};

const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const createDateRange = ref([]);

function resetQuery() {
  clearSelected();
  Object.assign(queryForm, queryFormState);
  createDateRange.value = [];
  emit('resetQuery');
  ajaxQuery();
}

function change(obj){
  if(obj.key == 'createTime'){
    console.log(obj);
    queryForm.startTime = obj.value[0];
    queryForm.endTime = obj.value[1];
    createDateRange.value = [dayjs(obj.value[0]),dayjs(obj.value[1])];
    search()
    return
  }
  if(obj.key == 'deliverGoodsTime'){
    queryForm.deliverGoodsStartTime = obj.value[0];
    queryForm.deliverGoodsEndTime = obj.value[1];
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
  if(obj.key == 'businessDate'){
    queryForm.businessStartDate = obj.value[0] ? obj.value[0] + '-01' : '';
    queryForm.businessEndDate = obj.value[1] ? obj.value[1] + '-01' : '';
    search()
    return
  }
  if(obj.key == 'latestPackingTime'){
    queryForm.latestPackingTimeStart  = obj.value[0];
    queryForm.latestPackingTimeEnd = obj.value[1];
  }
  if(obj.key == 'receiveGoodsTime'){
    queryForm.receiveGoodsTimeStart  = obj.value[0];
    queryForm.receiveGoodsTimeEnd = obj.value[1];
  }
  if(obj.search){
    search()
  }
}

function changeCreateTime (dates, dateStrings) {
    queryForm.startTime = dateStrings[0];
    queryForm.endTime = dateStrings[1];
}

function search(params = {}) {
  clearSelected();
  queryForm.pageNum = 1;
  Object.assign(queryForm, params);
  ajaxQuery();
}


async function ajaxQuery() {
  try {
    await nextTick();
    tableLoading.value = true;
    let params = {
      ...queryForm,
      ...props.tabParams,
    };
    let responseModel = await waybillApi.query(params);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
    queryAmountStatistics();
    // 由于目前查询后不再清空所选择的运单，导致选中运单不会变化，需手动更新
    updateSelectedList();
    // clearSelected();
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

let amountStatistics = ref({
  payableTotalAmount: 0,
  paidTotalAmount: 0
});

// 统计应付、已付合计
async function queryAmountStatistics() {
  try {
    await nextTick();
    let params = Object.assign({}, queryForm, props.tabParams);
    let responseModel = await waybillApi.queryAmountStatistic(params);
    amountStatistics.value = responseModel.data;
  } catch (e) {
    console.log(e);
  } finally {
  }
}

function formatMoneyStr(value) {
  return formatMoney(value);
}
// ----------- table 批量操作 start -----------
const selectedRowKeyList = ref([]);
const selectedDataList = ref([]);

function onSelectChange(keyArray, dataArray) {
  selectedRowKeyList.value = keyArray;
  selectedDataList.value = dataArray;
  emit('changeSelect', keyArray, dataArray);
}

function clearSelected() {
  selectedRowKeyList.value = [];
  selectedDataList.value = [];
  emit('changeSelect', [], []);
}

function updateSelectedList() {
  if (_.isEmpty(selectedDataList.value)) {
    return;
  }
  for (let i = 0; i < selectedDataList.value.length; i++) {
    let item = selectedDataList.value[i];
    let waybillId = item.waybillId;
    let find = tableData.value.find(e => e.waybillId === waybillId);
    console.log('find', find);
    if (find) {
      selectedDataList.value[i] = _.cloneDeep(find);
    }
  }
}

// ----------- 详情页-----------
let router = useRouter();
function waybillDetail(waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
}
function orderDetail(orderId) {
  router.push({ path: '/order/detail', query: { orderId } });
}
function shipperDetail(shipperId) {
  router.push({ path: '/shipper/detail', query: { shipperId } });
}
// 付款单信息
const waybillPayOrderModal = ref();

function showPayOrderModal(waybillId) {
  waybillPayOrderModal.value.showModal(waybillId);
}

// 查询收款单信息
const waybillReceiveOrderModal = ref();

function showReceiveOrderModal(waybillId) {
  waybillReceiveOrderModal.value.showModal(waybillId);
}

function getReceiveOrderNumber(record) {
  if (_.isEmpty(record.receiveOrderNumberList)) {
    return '';
  }
  let receiveOrderNumberArray = record.receiveOrderNumberList;
  let size = receiveOrderNumberArray.length;
  if (size > 1) {
    return receiveOrderNumberArray[0] + '等' + size + '条';
  }
  return receiveOrderNumberArray[0];
}

// ----------------- 提交结算 、并开票 --------------------
let { confirmCancel } = orderCancel();

async function exportExcel() {
  if(!queryForm.endTime || !queryForm.startTime){
    Modal.confirm({
      title: '提示',
      content: '请选择创建时间进行导出，最多3个月',
      okText: '确认',
    });
    return
  }
  SmartLoading.show();
  let params = _.cloneDeep(queryForm);
  params.waybillStatusList = params.waybillStatusList.join(',');
  params.driverIdList = params.driverIdList.join(',');
  params.vehicleIdList = params.vehicleIdList.join(',');
  params.enterpriseIdList = params.enterpriseIdList.join(',');
  params.scheduleIdList = params.scheduleIdList.join(',');
  params.customerIdList = params.customerIdList.join(',');
  await waybillApi.downloadExcel('运单列表.xlsx', params);
  SmartLoading.hide();
}

// ----------------- 显示附件弹窗 --------------------
let showReceiptAttachmentFlag = ref(false);
let receiptAttachmentList = ref([])

function showReceiptAttachmentModal(record) {
  showReceiptAttachmentFlag.value = true;
  receiptAttachmentList.value = record.receiptAttachment || [];
}


let showContractAttachmentFlag = ref(false);
let contractAttachmentList = ref([])

function showContractAttachmentModal(record) {
  showContractAttachmentFlag.value = true;
  contractAttachmentList.value = record.contractFile || [];
}

let splitTransportModalRef = ref();
function showSplitTransportModal(waybillId) {
  splitTransportModalRef.value.showModal(waybillId);
}

// ----------------- 磅单凭证预览 --------------------  
let previewList = ref([]);
let previewVisible = ref(false);

const setVisible = (value) => {
  previewVisible.value = value;
}

function showPreview(attachment) {
  if (attachment.length === 0) {
    return;
  }
  previewList.value = attachment;
  console.log('previewList', previewList.value);
  previewVisible.value = true;
}

defineExpose({
  tableData,
  ajaxQuery,
  queryFormState,
  exportExcel,
  search,
})

</script>
<style lang="less" scoped>
.waybill-code {
  display: flex;

}

.waybill-code :deep(.ant-image) {
  display: none !important;
}

.waybill-code :deep(.file-name) {
  float: left;
}

.smart-query-form-item {
  margin-bottom: 5px !important;
}

.search-btn-block {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  margin-right: 50px;
}


.tag-show {
  display: flex;
  flex-direction: row;
  align-items: center;
  .label {
    margin-right: 10px;
  }
}

.preview-link {
  color: #1890FF;
  cursor: pointer;
  margin-left: 10px;
}

</style>
