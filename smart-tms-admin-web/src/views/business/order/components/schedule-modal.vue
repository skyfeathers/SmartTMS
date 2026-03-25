<template>
  <a-modal :width="1000" :open="visible" title="分配司机"
            :getContainer="getContainer" :destroyOnClose="true" @cancel="onClose">
    <a-descriptions :column="4" size="small">
      <a-descriptions-item label="货主名称">{{ orderDetail.consignor }}</a-descriptions-item>
      <a-descriptions-item label="所属区域">{{ orderDetail.areaDesc }}</a-descriptions-item>
      <a-descriptions-item label="创建人">{{ orderDetail.createUserName }}</a-descriptions-item>

      <a-descriptions-item label="运输类型">
        {{ $smartEnumPlugin.getDescByValue('TRANSPORTATION_TYPE_ENUM', orderDetail.businessTypeCode) }}
      </a-descriptions-item>
      <a-descriptions-item label="业务类型">{{ orderDetail.containerBusinessTypeName }}</a-descriptions-item>
      <a-descriptions-item label="订单类型">{{ $smartEnumPlugin.getDescByValue('ORDER_TYPE_ENUM', orderDetail.orderType) }}</a-descriptions-item>
      <a-descriptions-item label="柜型">{{ orderDetail.cabinetName }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ orderDetail.createTime }}</a-descriptions-item>
      <a-descriptions-item label="备注">{{ orderDetail.remark }}</a-descriptions-item>
    </a-descriptions>
    <hr class="smart-hr"/>
    <a-descriptions :column="2" size="small">
      <a-descriptions-item label="运输路线名称">{{ orderDetail.transportRouteName }}</a-descriptions-item>
      <a-descriptions-item label=""/>
      <a-descriptions-item label="提箱地点">{{ orderDetail.containerLocation }}</a-descriptions-item>
      <a-descriptions-item label="装货地点">{{ orderDetail.placingLocation }}</a-descriptions-item>
      <a-descriptions-item label="还箱地点">{{ orderDetail.returnContainerLocation }}</a-descriptions-item>
      <a-descriptions-item label="卸货地点">{{ orderDetail.unloadingLocation }}</a-descriptions-item>
    </a-descriptions>

    <hr class="smart-hr"/>
    <a-tabs v-model:activeKey="activeKey"  @change="changeTabs">
      <a-tab-pane key="schedule" tab="分配司机">
        <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules"
                :labelCol="{ span: 8 }">
          <!---基础信息-->
          <a-row>
            <a-col :span="8">
              <a-form-item label="业务时间" name="businessDate">
                <a-date-picker :allowClear="false" :value="businessDate" picker="month" placeholder="请选择业务时间" style="width: 200px" @change="dateChange"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="结算方式" name="settleMode">
                <smart-enum-select v-model:value="form.settleMode" enumName="SELECT_SETTLE_MODE_ENUM" placeholder="请选择结算方式"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="运输方式" name="transportMode">
                <SmartEnumSelect v-model:value="form.transportMode" enumName="WAYBILL_TRANSPORT_MODE_ENUM"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="8">
              <a-form-item label="车队" name="fleetId">
                <FleetSelect v-model:value="form.fleetId" placeholder="请选择车队"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="司机" name="driverId">
                <div class="flex-center">
                  <ScheduleDriverSelect ref="scheduleDriverSelectRef" width="160px" @change="changeDriver" :key="ScheduleDriverSelectKey" :vehicleId="form.vehicleId" placeholder="请选择司机"/>
                  <a @click="showQuickCreateModal(QUICK_CREATE_TYPE_ENUM.DRIVER.value)" style="margin-left: 5px;">新建</a>
                </div>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="车辆" name="vehicleId">
                <div class="flex-center">
                  <ScheduleVehicleSelect ref="scheduleVehicleSelectRef" width="160px" @change="changeVehicle" :key="ScheduleVehicleSelectKey" :driverId="form.driverId" placeholder="请选择车辆"/>
                  <a @click="showQuickCreateModal(QUICK_CREATE_TYPE_ENUM.VEHICLE.value)" style="margin-left: 5px;">新建</a>
                </div>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="8">
              <a-form-item label="装货时间" name="loadTime">
                <a-date-picker v-model:value="form.loadTime" :show-time="{ format: 'HH:mm' }"
                               format="YYYY-MM-DD HH:mm:00"
                               placeholder="请选择装货时间" valueFormat="YYYY-MM-DD HH:mm:00"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="卸货时间" name="unloadTime">
                <a-date-picker v-model:value="form.unloadTime" :show-time="{ format: 'HH:mm' }"
                               format="YYYY-MM-DD HH:mm:00"
                               placeholder="请选择卸货时间" valueFormat="YYYY-MM-DD HH:mm:00"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="备注" name="remark">
                <a-input v-model:value="form.remark" style="width: 200px" placeholder="请输入备注"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="8">
              <a-form-item label="运费" name="cashAmount">
                <a-input-number v-model:value="form.cashAmount" :precision="4" :min="0" placeholder="请输入运费"
                                style="width: 200px"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="油卡" name="oilCardAmount">
                <a-input-number v-model:value="form.oilCardAmount" :min="0" :precision="4" placeholder="请输入油卡"
                                style="width: 200px"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="收款" name="receiveAmount">
                <a-input-number v-model:value="form.receiveAmount" :precision="4" :min="0" placeholder="请输入收款"
                                style="width: 200px"/>
              </a-form-item>
            </a-col>
          </a-row>


        </a-form>
        <a-alert v-if="beyondNumberFlag" message="存在超出订单实际毛重或数量的货物信息，请谨慎操作。" type="error" show-icon class="smart-margin-bottom5"/>
        <a-table :columns="columns" :dataSource="goodsList" bordered :pagination="false" rowKey="orderItemId"
                 size="small">
          <template #bodyCell="{ text, record, index, column }">
            <template v-if="column.dataIndex === 'goodsTotalQuantity'">
              {{ record.goodsTotalQuantity }}/<span style="color: red">{{ record.remainGoodsQuantity }}</span>
            </template>
            <template v-if="column.dataIndex === 'goodsQuantity'">
              <a-input-number :min="0" v-model:value="record.goodsQuantity"/>
            </template>

            <template v-if="column.dataIndex === 'goodsUnit'">
              {{ $smartEnumPlugin.getDescByValue('GOODS_UNIT_TYPE_ENUM', record.goodsUnit) }}
            </template>
            <template v-if="column.dataIndex === 'remark'">
              <a-input v-model:value="record.remark"/>
            </template>
          </template>
        </a-table>
      </a-tab-pane>

    </a-tabs>
    <template #footer>
      <a-button @click="onClose">取消</a-button>
      <a-button type="primary" @click="onSubmit('back')">提交</a-button>
      <template v-if="showNextBtn">
        <a-button type="primary" @click="onSubmit('next')">保存并继续分配</a-button>
        <a-button type="primary" @click="onSubmit('create')">保存并继续创建</a-button>
      </template>
    </template>
  </a-modal>
  <VehicleQuickCreate ref="quickCreateRef" @reloadList="reload" />
</template>
<script setup>
import {ref, computed, reactive, nextTick} from 'vue';
import _ from 'lodash';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import ScheduleVehicleSelect from '/@/components/schedule-vehicle-select/index.vue';
import ScheduleDriverSelect from '/@/components/schedule-driver-select/index.vue';
import FleetSelect from '/@/components/fleet-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import { SETTLE_TYPE_ENUM, WAYBILL_TRANSPORT_MODE_ENUM, SETTLE_MODE_ENUM, SELECT_SETTLE_MODE_ENUM } from '/@/constants/business/waybill-const';
import {SmartLoading} from '/@/components/smart-loading';
import {orderApi} from '/@/api/business/order/order-api';
import {message} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {waybillApi} from '/@/api/business/waybill/waybill-api';
import {getContainer} from '/@/utils/container-util';
import {TRANSPORTATION_TYPE_ENUM} from "/@/constants/business/transport-route-const";
import { GOODS_UNIT_TYPE_ENUM, ORDER_TYPE_ENUM } from '/@/constants/business/order-const';
import {Decimal} from "decimal.js";
import dayjs from 'dayjs';
import { v4 as uuid } from 'uuid';
import VehicleQuickCreate from '/@/components/vehicle-quick-create/index.vue';
import { QUICK_CREATE_TYPE_ENUM } from '/@/constants/business/vehicle-const';
defineProps({
  showNextBtn:{
    type:Boolean,
    default:false
  }
})


//-------------查询信息------------------
const orderDetail = reactive({});

async function getDetail(orderId) {
  try {
    useSpinStore().show();
    const {data} = await orderApi.getDetail(orderId);
    Object.assign(orderDetail, data);
    data.goodsList.forEach(e => {
      e.goodsTotalQuantity = e.goodsQuantity;
      e.goodsQuantity = 0;
    })
    goodsList.value = data.goodsList
    form.loadTime = data.loadTime;
    form.unloadTime = data.unloadTime;
    form.cashAmount = data.singleTripFreightAmount;
    form.receiveAmount = data.singleTripReceiveAmount;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}
const ScheduleVehicleSelectKey = ref(uuid())
const ScheduleDriverSelectKey = ref(uuid())
function reset(){
  ScheduleVehicleSelectKey.value = uuid()
  ScheduleDriverSelectKey.value = uuid()
  businessDate.value = null;
  getDetail(form.orderId);
  formRef.value.resetFields();
}

// -------------表单字段------------------

const formDefault = {
  goodsFormList: [
    {
      orderItemId: null,
      goodsQuantity: null,
      remark: null,
    },
  ],
  orderId: null,
  cashAmount: 0,
  receiveAmount: 0,
  remark: null,
  settleMode: SELECT_SETTLE_MODE_ENUM.ARRIVE_PAY.value,
  vehicleId: null,
  driverId: null,
  fleetId: null,
  oilCardAmount: 0,
  // 车牌号
  vehicleNumber: null,
  // 司机姓名
  driverName: null,
  // 司机身份证号
  drivingLicense: null,
  // 司机手机号
  telephone: null,
  businessDate: null,
  transportMode: WAYBILL_TRANSPORT_MODE_ENUM.LONG_DISTANCE.value,
  loadTime: null,
  unloadTime: null,
};
const businessDate = ref()
const form = reactive({...formDefault});
const formRef = ref();
const goodsList = ref([]);


//计算是否存在超出数量的货物信息
const beyondNumberFlag = computed(() => {
  if(_.isEmpty(goodsList)){
    return false;
  }
  let filterArray = goodsList.value.filter((e) => (e.remainGoodsQuantity - e.goodsQuantity) < 0);
  if(_.isEmpty(filterArray)){
    return false;
  }
  return true;
})

const validateDriver = async (rule, value) => {
  if (!form.driverId && !form.driverName) {
    return Promise.reject('请选择司机');
  }
  return Promise.resolve();
};

const validateVehicle = async (rule, value) => {
  if (!form.vehicleId && !form.vehicleNumber) {
    return Promise.reject('请选择车辆');
  }
  return Promise.resolve();
};

const rules = {
  settleMode: [{ required: true, message: '请选择结算方式' }],
  driverId: [{ required: true, message: '请选择司机' }],
  // driverId: [{ validator: validateDriver }],
  vehicleId: [{ required: true, message: '请选择车辆' }],
  // vehicleId: [{ validator: validateVehicle }],
  cashAmount: [{ required: true, message: '请输入本单运费' }],
  receiveAmount: [{ required: true, message: '请输入本单收款' }],
  oilCardAmount: [{ required: true, message: '请输入油卡费用' }],
  businessDate: [{ required: true, message: '请选择业务时间' }],
  transportMode: [{ required: true, message: '请选择运输方式' }],
  loadTime: [{ required: true, message: '请选择装货时间' }],
  unloadTime: [{ required: true, message: '请选择卸货时间' }],
};

//-------------提交表单------------------
const emit = defineEmits(['refresh']);

async function onSubmit(type) {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        let zeroGoods = goodsList.value.filter(e=> !e.goodsQuantity).map(item=>{
          return item.goodsName;
        });
        if(!_.isEmpty(zeroGoods)){
          let zeroGoodsName = zeroGoods.join(",");
          message.error(`【${zeroGoodsName}】本单货物量不能为0`);
          SmartLoading.hide();
          return;
        }
        try {
          let goodsFormList = goodsList.value.map((e) => {
            return {
              orderGoodsId: e.orderGoodsId,
              goodsQuantity: e.goodsQuantity,
              remark: e.remark
            };
          });
          let params = _.cloneDeep(form);
          params.goodsFormList = goodsFormList;
          await waybillApi.schedule(params);
          message.success('分配成功!');
          if(type == 'next'){
            reset();
          }else{
            emit('refresh',type);
            onClose();
          }
        } catch (error) {
          console.log(error);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
}

// ------------------ 货物列表 ------------------
const columns = reactive([
  {
    title: '货物名称',
    width: 100,
    dataIndex: 'goodsName',
  },
  {
    title: '货物单位',
    dataIndex: 'goodsUnit',
    width: 90,
  },

  {
    title: '总量/剩余',
    dataIndex: 'goodsTotalQuantity',
    width: 100,
  },

  {
    title: '本单货物量',
    dataIndex: 'goodsQuantity',
    width: 90,
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 150,
  },
]);

//-------------显示/隐藏------------------
const visible = ref(false);
// 默认运输类型-集装箱运输
let activeKey = ref('schedule');

function showModal (orderId) {
  Object.assign(form, formDefault, { orderId });
  form.orderId = orderId;
  visible.value = true;
  getDetail(orderId);
  nextTick(() => {
    formRef.value.clearValidate();
  });
}


function onClose() {
  businessDate.value = null;
  visible.value = false;
  activeKey.value = 'schedule';
}

// 计算货物总价
function getGoodsTotalAmount(goods){
  if(goods.goodsUnitPrice == 0 ){
    return 0;
  }
  if(goods.goodsUnit == GOODS_UNIT_TYPE_ENUM.UNIT_TOM.value) {
    return Decimal(goods.goodsWeight).mul(Decimal(goods.goodsUnitPrice)).toNumber();
  }
  return Decimal(goods.goodsCount).mul(Decimal(goods.goodsUnitPrice)).toNumber();
}

function changeDriver (driver) {
  if (driver.driverId) {
    Object.assign(form, { driverId: driver.driverId, driverName: '', telephone: '', drivingLicense: '' });
  } else {
    let { driverName, telephone, drivingLicense } = driver;
    Object.assign(form, { driverName, telephone, drivingLicense, driverId: null });
  }
}

function changeVehicle (vehicle) {
  if (vehicle.vehicleId) {
    Object.assign(form, { vehicleId: vehicle.vehicleId, vehicleNumber: '' });
  } else {
    let vehicleNumber = vehicle.vehicleNumber;
    Object.assign(form, { vehicleNumber, vehicleId: null});
  }
}

const quickCreateRef = ref()
function showQuickCreateModal (typeValue) {
  quickCreateRef.value.showModal(typeValue);
}

function dateChange (date, dateString) {
  businessDate.value = date;
  form.businessDate = dayjs(date).format('YYYY-MM-01');
}

const scheduleVehicleSelectRef = ref()
const scheduleDriverSelectRef = ref()

function reload(){
  scheduleVehicleSelectRef.value.queryData()
  scheduleDriverSelectRef.value.queryData()
}

defineExpose({
  showModal,
});
</script>
<style lang="less" scoped></style>
