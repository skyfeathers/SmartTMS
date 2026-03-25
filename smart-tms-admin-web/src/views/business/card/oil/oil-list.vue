<template>
  <a-form class="smart-query-form" v-privilege="'oil:query'">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="关键字">
        <a-input v-model:value="queryForm.keyWords" class="form-width" placeholder="油卡卡号/持卡司机/持卡车/创建人"/>
      </a-form-item>

      <a-form-item v-if="OIL_CARD_TYPE_ENUM.SLAVE.value == queryForm.type" class="smart-query-form-item" label="主卡">
        <OilCardSelect :type="OIL_CARD_TYPE_ENUM.MASTER.value" width="200px" v-model:value="queryForm.masterOilCardId"/>
      </a-form-item>

        <a-form-item class="smart-query-form-item" label="燃料类型">
          <SmartEnumSelect v-model:value="queryForm.fuelType " enumName="OIL_CARD_FUEL_TYPE_ENUM"
          placeholder="请选择燃料类型" width="200px"/>
        </a-form-item>

      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker v-model:value="queryForm.createTime" :presets="defaultTimeRanges" class="form-width"
                        @change="changeCreateTime"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="充值时间">
        <a-range-picker v-model:value="queryForm.rechargeTime" :presets="defaultTimeRanges" class="form-width"
                        @change="changeRechargeTime"/>
      </a-form-item>
      <a-form-item label="状态" class="smart-query-form-item">
        <a-radio-group @change="ajaxQuery" v-model:value="queryForm.disabledFlag">
          <a-radio-button :value="false">启用</a-radio-button>
          <a-radio-button :value="true">禁用</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined/>
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <ReloadOutlined/>
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="addOrUpdate(OIL_CARD_TYPE_ENUM.MASTER.value)" v-privilege="'oil:addMaster'" size="small"
                  type="primary">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建主卡
        </a-button>
        <a-button @click="addOrUpdate(OIL_CARD_TYPE_ENUM.SLAVE.value)" v-privilege="'oil:addBranch'" size="small" type="primary" >
          <template #icon>
            <PlusOutlined/>
          </template>
          新建副卡
        </a-button>
        <a-button :disabled="selectedRowKeyList.length == 0" v-privilege="'oil:delete'" @click="confirmBatchDelete()" size="small" type="primary" danger >
          <template #icon>
            <DeleteOutlined/>
          </template>
          删除
        </a-button>

        <a-button @click="showExportModal()" v-privilege="'oil:export'" size="small">导出</a-button>

        <a-button v-privilege="'oil:masterExport'" size="small" @click="exportMasterExcel()">主卡导出</a-button>

        <a-button v-privilege="'oil:slaveImport'" size="small" @click="showImportModal()">副卡导入</a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="tableId" :refresh="ajaxQuery" />
      </div>
    </a-row>
    <a-tabs v-model:activeKey="activeKey" @change="changeTab">
      <a-tab-pane :key="OIL_CARD_TYPE_ENUM.MASTER.value" tab="主卡"></a-tab-pane>
      <a-tab-pane :key="OIL_CARD_TYPE_ENUM.SLAVE.value" tab="副卡" force-render></a-tab-pane>
    </a-tabs>
    <span class="smart-statistics">
          合计充值： <span class="amount">{{ formatMoneyStr(totalRechargeBalance) }}</span>元
    </span>
    <a-table :columns="columns"
             :dataSource="tableData" :pagination="false" :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
             :scroll="{ x: 2000,y:400 }"
             bordered
             rowKey="oilCardId" size="small">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'oilCardNo'">
          <a v-if="$privilege('oil:detail')" @click="oilCardDetail(record.oilCardId)">{{ record.oilCardNo }} </a>
          <span v-else>{{ record.oilCardNo }} </span>
          <SmartCopyIcon :value="text" />
        </template>
        <template v-if="column.dataIndex === 'fuelType'">
          <span>{{ $smartEnumPlugin.getDescByValue('OIL_CARD_FUEL_TYPE_ENUM', record.fuelType) }}</span>
        </template>
        <template v-if="column.dataIndex === 'masterOilCardNo'">
          <a @click="oilCardDetail(record.masterOilCardId)">{{ record.masterOilCardNo }} </a>
        </template>
        <template v-if="column.dataIndex === 'disabledFlag'">
          <span>{{ record.disabledFlag ? '禁用' : '启用' }} </span>
        </template>
        <template v-if="column.dataIndex === 'brand'">
          <span>{{ record.brand && record.brand.map(item => item.valueName).join(',') }} </span>
        </template>
        <template v-if="column.dataIndex === 'type'">
          <span>{{ $smartEnumPlugin.getDescByValue('OIL_CARD_TYPE_ENUM', record.type) }} </span>
        </template>
        <template v-if="column.dataIndex === 'fixedPointFlag'">
          <span>{{ record.fixedPointFlag ? '是' : '否' }} </span>
        </template>
        <template v-if="column.dataIndex === 'purpose'">
          <span>{{ record.purpose && record.purpose.map(item => item.valueName).join(',') }} </span>
        </template>
        <template v-if="column.dataIndex === 'preRechargeAmount'">
          <a-button v-privilege="'oil:updatePreRechargeAmount'" type="link" @click="showPreRechargeAmountModal(record, true)">{{record.preRechargeAmount}}</a-button>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <template v-if="record.type == OIL_CARD_TYPE_ENUM.MASTER.value">
              <a-button v-privilege="'oil:topup'" type="link" @click="topup(record,0)">充值</a-button>
              <a-button @click="expend(record)" v-privilege="'oil:expend'" type="link">手动消耗</a-button>
              <a-button v-privilege="'oil:preRechargeAmount'" type="link" @click="showPreRechargeAmountModal(record, false)">计划充值</a-button>
            </template>
            <template v-else>
              <a-button @click="circumflexOil(record)" v-privilege="'oil:circumflex'" type="link">圈回</a-button>
              <a-button @click="cancelOil(record)" v-privilege="'oil:cancel'" type="link">挂失圈回</a-button>
              <a-button v-privilege="'oil:distributeSlaveCard'" type="link" @click="distributeSlaveCard(record)">分配副卡</a-button>
            </template>
            <a-button @click="addOrUpdate(record.type, record)" v-privilege="'oil:edit'" type="link">编辑</a-button>
          </div>
        </template>
      </template>

    </a-table>

    <div class="smart-query-table-page">
      <a-pagination v-model:current="queryForm.pageNum" v-model:pageSize="queryForm.pageSize"
                    :defaultPageSize="queryForm.pageSize" :pageSizeOptions="PAGE_SIZE_OPTIONS"
                    :show-total="(total) => `共${total}条`"
                    :total="total" show-less-items showQuickJumper showSizeChanger @change="ajaxQuery"
                    @showSizeChange="ajaxQuery"/>
    </div>
  </a-card>
  <!--新建编辑modal-->
  <OilOperateModal ref="operateModal" @reloadList="ajaxQuery"/>
  <!-- 充值 -->
  <OilTopupModal ref="topupModal" @reloadList="ajaxQuery"/>
  <!-- 手动消耗 -->
  <OilExpendModal ref="expendModal" @reloadList="ajaxQuery"/>
  <!-- 作废 -->
  <OilCancelModal ref="cancelModal" @reloadList="ajaxQuery"/>
  <!-- 圈回 -->
  <OilCircumflexModal ref="circumflexRef" @reloadList="ajaxQuery"/>
  <!-- 分配副卡 -->
  <SlaveCardDistributeModal ref="distributeSlaveCardRef" @reloadList="ajaxQuery"/>

  <!-- 交易流水导出 -->
  <BalanceExportModal ref="exportRef"/>

  <!-- 副卡导入 -->
  <SlaveImportModal ref="importModal" @reloadList="ajaxQuery" />
  <!-- 计划充值金额 -->
  <OilPreRechargeAmountModal ref="preRechargeAmountRef" @reloadList="ajaxQuery"/>
</template>
<script setup>

import OilCardSelect from '/@/components/oil-card-select/index.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import OilOperateModal from './components/oil-operate-modal.vue';
import OilTopupModal from './components/oil-topup-modal.vue';
import OilExpendModal from './components/oil-expend-modal.vue';
import OilCancelModal from './components/oil-cancel-modal.vue';
import OilCircumflexModal from './components/oil-circumflex-modal.vue';
import BalanceExportModal from './components/balance-export-modal.vue';
import SlaveCardDistributeModal from './components/slave-card-distribute-modal.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import SlaveImportModal from './components/slave-import-modal.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import OilPreRechargeAmountModal from './components/oil-pre-recharge-amount-modal.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { computed, onMounted, reactive, ref} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { OIL_CARD_TYPE_ENUM } from '/@/constants/business/card-const';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { oilApi } from '/@/api/business/card/oil-api';
import { useRouter } from 'vue-router';
import { formatMoney } from '/@/utils/money-util';
import _ from "lodash"
//弹窗
import { SmartLoading } from '/@/components/smart-loading';
import useDragTable from '/@/hook/use-drag-table/index';
// --------------------- 列表查询 ------------------------
const basicColumns = [
  {
    title: '油卡卡号',
    dataIndex: 'oilCardNo',
    width: 200,
    fixed: 'left',
  },
  {
    title: '主卡卡号',
    dataIndex: 'masterOilCardNo',
    width: 180,
    fixed: 'left',
  },
  {
    title: '油卡品牌',
    width: 75,
    dataIndex: 'brand',
  },
  {
    title: '油卡类型',
    width: 75,
    dataIndex: 'type',
  },
  {
    title: '是否定点',
    width: 75,
    dataIndex: 'fixedPointFlag',
  },
  {
    title: '燃料类型',
    width: 75,
    dataIndex: 'fuelType',
  },
  {
    title: '取卡时间',
    width: 160,
    dataIndex: 'drivingLicense',
  },
  {
    title: '领取人',
    width: 90,
    dataIndex: 'receiveUserName',
  },
  {
    title: '持卡司机',
    width: 90,
    dataIndex: 'useDriverName',
  },
  {
    title: '当前持卡车',
    width: 90,
    dataIndex: 'useVehicleNumber',
  },
  {
    title: '期初余额',
    width: 75,
    dataIndex: 'beginBalance',
  },
  {
    title: '当前余额',
    width: 75,
    dataIndex: 'balance',
  },
  {
    title: '待分配金额',
    width: 100,
    dataIndex: 'preDistributionBalance',
  },
  {
    title: '充值金额',
    dataIndex: 'rechargeBalance',
    width: 100
  },
  {
    title: '计划充值金额',
    dataIndex: 'preRechargeAmount',
    width: 120
  },
  {
    title: '分配金额',
    dataIndex: 'distributeBalance'
  },
  {
    title: '用途',
    width: 100,
    dataIndex: 'purpose',
  },
  {
    title: '油卡状态',
    width: 75,
    dataIndex: 'disabledFlag',
  },
  {
    title: '创建人',
    width: 70,
    dataIndex: 'createUserName',
  },
  {
    title: '创建时间',
    width: 160,
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    width: 210,
    dataIndex: 'action',
    fixed: 'right',
  },
];

const queryFormState = {
  keyWords: '',
  disabledFlag: false,
  masterOilCardId: undefined,
  type: OIL_CARD_TYPE_ENUM.MASTER.value,
  startTime: undefined,
  endTime: undefined,
  createTime: [],
  rechargeTime: [],
  pageNum: 1,
  pageSize: PAGE_SIZE,
  fuelType:null
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const selectedRowKeyList = ref([]);
const selectedRowList = ref([]);
const tableData = ref([]);
const operateModal = ref();
const total = ref(0);


const activeKey = ref(OIL_CARD_TYPE_ENUM.MASTER.value);

const tableId = computed(() => {
  if (activeKey.value == OIL_CARD_TYPE_ENUM.MASTER.value) {
    return TABLE_ID_CONST.OIL_CARD_MASTER;
  }
  if (activeKey.value == OIL_CARD_TYPE_ENUM.SLAVE.value) {
    return TABLE_ID_CONST.OIL_CARD_SLAVE;
  }
});

// 定义主卡不显示的列
let masterNoShowList = ['masterOilCardNo', 'distributeBalance'];
// 定义副卡不显示的列
let slaveNoShowList = ['preDistributionBalance', 'rechargeBalance'];


let { columnsData:masterColumns } = useDragTable([...basicColumns.filter(e => !masterNoShowList.includes(e.dataIndex))],TABLE_ID_CONST.OIL_CARD_MASTER)

let { columnsData:slaveColumns } = useDragTable(basicColumns.filter(e => !slaveNoShowList.includes(e.dataIndex)),TABLE_ID_CONST.OIL_CARD_SLAVE)

let columns = masterColumns;

function changeTab (value) {
  console.log( activeKey.value, value)
  queryForm.type = value;
  if (queryForm.type == OIL_CARD_TYPE_ENUM.MASTER.value) {
    queryForm.masterOilCardId = null;
    columns = masterColumns;
  } else {
    columns = slaveColumns;
  }
  ajaxQuery();
}

function changeCreateTime (dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

function changeRechargeTime (dates, dateStrings) {
  queryForm.balanceRecordStartTime = dateStrings[0];
  queryForm.balanceRecordEndTime = dateStrings[1];
}

function onSelectChange (selectedRowKeys, selectedRows) {
  selectedRowKeyList.value = selectedRowKeys;
  selectedRowList.value = selectedRows;
}

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  // activeKey.value = OIL_CARD_TYPE_ENUM.MASTER.value;
  changeTab(OIL_CARD_TYPE_ENUM.MASTER.value);
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}
let totalRechargeBalance = ref(0);

async function ajaxQuery () {
  selectedRowKeyList.value = [];
  selectedRowList.value = [];
  try {
    useSpinStore().show();
    tableLoading.value = true;
    let responseModel = await oilApi.queryOil(queryForm);
    let summary = await oilApi.cardSummary(queryForm);
    totalRechargeBalance.value = summary.data;
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
    useSpinStore().hide();
  }
}

// ----------------- 油卡操作 --------------------
function addOrUpdate (cardType, rowData) {
  operateModal.value.showModal(cardType, _.cloneDeep(rowData));
}

// 触发批量删除
function confirmBatchDelete () {
  Modal.confirm({
    title: '提示',
    content: '确定要删除选中值吗?',
    okText: '删除',
    okType: 'danger',
    onOk () {
      batchDelete();
    },
    cancelText: '取消',
    onCancel () { },
  });
}

// 批量删除
const batchDelete = async () => {
  try {
    useSpinStore().show();
    await oilApi.batchDeleteOil(selectedRowKeyList.value);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
};

// 触发删除
function confirmDelete () {
  Modal.confirm({
    title: '提示',
    content: '确定要删除选中值吗?',
    okText: '删除',
    okType: 'danger',
    onOk () {
      batchDelete();
    },
    cancelText: '取消',
    onCancel () { },
  });
}


// 充值
const topupModal = ref();

function topup (rowData, changeAmount = 0, rechargeFlag = false) {
  topupModal.value.showModal(_.cloneDeep(rowData), changeAmount, rechargeFlag);
}

// 消耗
const expendModal = ref();

function expend (rowData) {
  expendModal.value.showModal(_.cloneDeep(rowData));
}

// 挂失圈回
const cancelModal = ref();

function cancelOil (rowData) {
  cancelModal.value.showModal(_.cloneDeep(rowData));
}

// 圈回
const circumflexRef = ref();

function circumflexOil (rowData) {
  circumflexRef.value.showModal(_.cloneDeep(rowData));
}

// 编辑计划充值金额
const preRechargeAmountRef = ref();

function showPreRechargeAmountModal ({ oilCardId, oilCardNo}, updateFlag = false){
  preRechargeAmountRef.value.showModal(_.cloneDeep({ oilCardId, oilCardNo }), updateFlag)
}

const exportRef = ref();

function showExportModal () {
  exportRef.value.showModal();
}

// 分配副卡
const distributeSlaveCardRef = ref();

function distributeSlaveCard (rowData) {
  distributeSlaveCardRef.value.showModal(_.cloneDeep(rowData));
}

function exportMasterExcel () {
  SmartLoading.show();
  let param = Object.assign({}, queryForm);
  oilApi.downloadMasterExcel(param);
  SmartLoading.hide();
}

// ----------------- 副卡导入 --------------------
const importModal = ref();

function showImportModal () {
  importModal.value.showModal();
}

function formatMoneyStr(value) {
  return formatMoney(value);
}



// ----------------- 跳转 --------------------
let router = useRouter();

function oilCardDetail (oilCardId) {
  router.push({ path: '/card/oil-detail', query: { oilCardId } });
}
onMounted(() => {
  changeTab(OIL_CARD_TYPE_ENUM.MASTER.value);
});

</script>
<style lang="less" scoped>
.form-width {
  width: 240px;
}

.expired {
  color: red;
}
</style>
