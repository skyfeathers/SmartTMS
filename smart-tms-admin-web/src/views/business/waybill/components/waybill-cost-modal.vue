<!--
 * @Description:运单结算
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-19
-->
<template>
  <a-drawer width="1000px" :open="visible" title="费用" ok-text="提交" cancel-text="取消" :getContainer="getContainer" :footer-style="{ textAlign: 'right' }" @close="onClose">
    <a-descriptions title="运单信息" :column="4" size="small">
      <a-descriptions-item label="运单号">{{ waybillDetail.waybillNumber }}</a-descriptions-item>
      <a-descriptions-item label="订单号">{{ waybillDetail.orderNo }}</a-descriptions-item>
      <a-descriptions-item label="货主">{{ waybillDetail.shipperName }}</a-descriptions-item>
      <a-descriptions-item label="创建人">{{ waybillDetail.createUserName }}</a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="" :column="4" size="small">
      <a-descriptions-item label="车队">{{ waybillDetail.fleetName }}</a-descriptions-item>
      <a-descriptions-item label="司机">{{ driverName }}</a-descriptions-item>
      <a-descriptions-item label="车辆">{{ waybillDetail.vehicleNumber }}</a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="" :column="4" size="small">

       <a-descriptions-item label="箱号">{{ waybillDetail.containerNumber }}</a-descriptions-item>
      <a-descriptions-item label="铅封号">{{ waybillDetail.leadSealNumber }}</a-descriptions-item>


      <a-descriptions-item label="创建时间">{{ waybillDetail.createTime }}</a-descriptions-item>

      <a-descriptions-item label="备注">{{ waybillDetail.remark }}</a-descriptions-item>


    </a-descriptions>
    <hr class="smart-hr" />
<!--    <a-alert v-if="networkFlag && waybillDetail.submitReceiveFlag" class="smart-margin-top10" message="运单已提交应收核算，应收费用无法修改，如需修改请先作废或驳回应收。" show-icon type="warning"/>-->
    <div class="tab-content">
      <div class="left-item">
        <div class="left-title">
          <h3>运单-应收明细</h3>
          <a-button @click="checkReceive = true" type="primary" size="small"> 校对应收 </a-button>
        </div>
        <span v-if="checkReceive" class="receive-check">应收费用已确认无误！</span>
        <a-table
          :columns="receiveColumns"
          :dataSource="receiveTableData"
          bordered
          :pagination="false"
          size="small"
          :class="payAmountChange && !checkReceive ? 'arrow-box' : ''"
          :scroll="{ x: 'max-content' }"
        >

          <template #costAmount="{ record }">
            <a-input-number v-model:value="record.costAmount" :min="0" :precision="2" style="width: 100%" />
          </template>
          <template #type> 应收 </template>
        </a-table>
      </div>

      <div class="right-item">
        <h3>运单-应付明细</h3>
        <a-table :columns="columns" :dataSource="tableData" bordered :pagination="false" size="small" :scroll="{ x: 'max-content'}">
          <template #bodyCell="{ text, record, index, column }">
            <template v-if="column.dataIndex === 'costAmount'">
              <a-input-number
                v-model:value="record.costAmount"
                @change="payCostAmountChange"
                :precision="2"
                :min="0"
                :placeholder="'请输入' + record.costItemName"
                :disabled = "disableFlag(record)"
                style="width: 100%"
              />
            </template>
          </template>
        </a-table>
      </div>
    </div>

    <template #footer>
      <div class="footer-btn">
        <a-button style="margin-right: 20px" @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">提交</a-button>
      </div>
    </template>

    <WaybillGoodsModal ref="waybillGoodsModalRef"/>
  </a-drawer>
</template>
<script setup>
import { ref, reactive, getCurrentInstance, computed} from 'vue';
import { SmartLoading } from '/@/components/smart-loading';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { SETTLE_TYPE_ENUM, WAYBILL_STATUS_ENUM } from '/@/constants/business/waybill-const';
import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';
import { YUN_JIAN_STATUS_ENUM } from "/@/constants/business/nft-const";
import { COST_ITEM_CATEGORY_ENUM } from '/@/constants/business/cost-const';
import { message } from 'ant-design-vue';
import WaybillGoodsModal from './waybill-goods-modal.vue';
import { getContainer } from '/@/utils/container-util';
import {Decimal} from "decimal.js";
import {nftCostCalculate} from "/@/utils/nft-util";

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});
const networkFlag = computed(() => {
  return waybillDetail.orderType == ORDER_TYPE_ENUM.NETWORK_FREIGHT.value;
});
const driverName = computed(() => {
  if (!waybillDetail.splitTransportFlag) {
    return waybillDetail.driverName;
  }
  return (waybillDetail.splitTransportList || []).map(e=>e.driverName).join(",");
});
// 应收是否校对
let checkReceive = ref(false);
let payAmountChange = ref(false);
// ----------------------- 提交表单 ------------------------
const formDefault = {
  waybillId: undefined,
  costItemList: [],
  receiveCostItemList: [],
};
let form = reactive({ ...formDefault });
// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal(waybillId) {
  visible.value = true;
  form.waybillId = waybillId;

  checkReceive.value = false;
  payAmountChange.value = false;
  getWaybillDetail(waybillId);
}

function disableFlag (record) {
  if (waybillDetail.orderType == ORDER_TYPE_ENUM.NETWORK_FREIGHT.value &&
      COST_ITEM_CATEGORY_ENUM.NFT_COST.value == record.costItemCategory) {
    return true;
  }
  return false;
}

// ----------------------- 应付表格 ------------------------
const tableData = ref([]);
const columns = reactive([
  {
    title: '费用名称',
    width: 90,
    dataIndex: 'costItemName',
  },
  {
    title: '应付金额',
    dataIndex: 'costAmount',
    width: 120
  },
]);

//应收列表
const receiveTableData = ref([]);
const receiveColumns = reactive([
  {
    title: '费用名称',
    width: 95,
    dataIndex: 'costItemName',
  },
  {
    title: '金额',
    dataIndex: 'costAmount',
    width: 90,
    slots: { customRender: 'costAmount' },
  },
]);

function payCostAmountChange() {
  payAmountChange.value = true;
  checkReceive.value = false;
  // 已提交应付后的金额变动， 不需要修改网络货运费用
  if (networkFlag && !waybillDetail.submitPayFlag) {
    let payDriverTotalAmount = tableData.value.filter(e=>e.costItemCategory != COST_ITEM_CATEGORY_ENUM.NFT_COST.value && e.costItemCategory != COST_ITEM_CATEGORY_ENUM.OIL_CARD.value)
        .map(e => e.costAmount).reduce((a, b) => Decimal(a).add(Decimal(b)).toNumber());
    let nftCost = nftCostCalculate(payDriverTotalAmount, waybillDetail.nftRate);
    let nftCostItem = tableData.value.find(e => e.costItemCategory == COST_ITEM_CATEGORY_ENUM.NFT_COST.value);
    if (nftCostItem) {
      nftCostItem.costAmount = nftCost;
    }
  }


}

// ----------------------- 查询付款单数据 ------------------------
let waybillDetail = reactive({});
const internalInstance = getCurrentInstance();
const smartEnumPlugin = internalInstance.appContext.config.globalProperties.$smartEnumPlugin;
async function getWaybillDetail(waybillId) {
  SmartLoading.show();
  try {
    //运单详情
    let responseModel = await waybillApi.getDetail(waybillId);
    let detail = responseModel.data;
    Object.assign(waybillDetail, detail);
    //运单应付费用
    let costList = waybillDetail.costList;
    costList.forEach((e) => {
      if (waybillDetail.orderType == ORDER_TYPE_ENUM.NETWORK_FREIGHT.value && e.costItemCategory == COST_ITEM_CATEGORY_ENUM.NFT_COST.value){
        e.settleObjectName = waybillDetail.nftEnterpriseName;
        e.settleType = '货运平台';
      }else {
        if (SETTLE_TYPE_ENUM.DRIVER.value == waybillDetail.settleType) {
          e.settleObjectName = waybillDetail.driverName;
          if (waybillDetail.splitTransportFlag) {
            e.settleObjectName = (waybillDetail.splitTransportList || []).map(e=>e.driverName).join(",");
          }
        } else {
          e.settleObjectName = waybillDetail.fleetName;
        }
        e.settleType = smartEnumPlugin.getDescByValue('SETTLE_TYPE_ENUM', waybillDetail.settleType);
      }
    });
    tableData.value = costList;
    //运单应收费用
    receiveTableData.value = waybillDetail.receiveCostList;
  } catch (error) {
    console.log('error', error);
  } finally {
    SmartLoading.hide();
  }
}

async function onSubmit() {
  if (!checkReceive.value) {
    message.error('请先校对应收费用项是否正确');
    return;
  }
  try {
    SmartLoading.show();
    form.costItemList = tableData.value;
    form.receiveCostItemList = receiveTableData.value;

    await waybillApi.costSubmit(form);
    message.success('结算成功');
    emit('reloadList');
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

// 收货
let waybillGoodsModalRef = ref();
function showWaybillGoods() {
  waybillGoodsModalRef.value.showModal(waybillDetail.goodsList);
}

</script>
<style scoped lang="less">
.tab-content {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 20px;
  >div {
    flex: 1;
    width: 0;
  }
}

.left-item {
  border: solid 1px rgba(0, 0, 0, 0.2);
  padding: 6px;
  margin-top: 10px;
  box-sizing: border-box;
}
.left-title {
  display: flex;
  justify-content: space-between;
}
.receive-check {
  color: red;
  font-weight: bold;
}
.right-item {
  margin-left: 5px;
  border: solid 1px rgba(0, 0, 0, 0.2);
  padding: 6px;
  margin-top: 10px;
  box-sizing: border-box;
}
.arrow-box {
  animation: glow 800ms ease-out infinite alternate;
}
@keyframes glow {
  0% {
    border-color: #ff4d4f;
    box-shadow: 0 0 5px rgba(255, 0, 0, 0.2), inset 0 0 5px rgba(255, 0, 0, 0.1), 0 1px 0 #ff0000;
  }
  100% {
    border-color: #ff0000;
    box-shadow: 0 0 20px rgba(255, 0, 0, 0.6), inset 0 0 10px rgba(255, 0, 0, 0.4), 0 1px 0 #ff0000;
  }
}


.footer-btn {
  margin: 10px 30px 10px 0;
}
</style>
