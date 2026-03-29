<!--
 * @Description: 创建订单
 * @Author: lidoudou
 * @Date: 2022-07-15
 * @LastEditTime: 2022-08-02
 * @LastEditors: zhuoda
-->
<template>
  <div class="smart-form-wrapper">
    <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules">
      <!---基础信息-->
      <a-card size="small" title="基础信息">
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }" class="base-info-top" :labelStyle="{ minWidth: '120px' }">
          <a-descriptions-item label="货主" class="required">
            <a-form-item name="shipperId">
              <div class="flex-center form-width">
                <ShipperSelect ref="shipperSelectRef" v-model:value="form.shipperId" @change="changeShipper" placeholder="请选择货主" width="140px"/>
                <a v-privilege="'shipper:add'" style="margin-left: 5px;" @click="showDrawer">新建</a>
              </div>
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="本单调度员" class="required">
            <a-form-item name="scheduleId">
              <OrderScheduleEmployeeSelect width="180px" v-model:value="form.scheduleId" :shipperId="form.shipperId"
                placeholder="请选择调度员" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="运输类型" class="required">
            <a-form-item name="businessTypeCode">
              <smart-enum-select v-model:value="form.businessTypeCode" enumName="TRANSPORTATION_TYPE_ENUM"
                placeholder="请选择运输类型" width="180px"/>
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="业务类型" class="required">
              <a-form-item name="containerBusinessTypeId">
                <BusinessTypeSelect v-model:value="form.containerBusinessTypeId" placeholder="请选择业务类型"
                  :default="form.orderId ? false : true" width="180px"/>
              </a-form-item>
          </a-descriptions-item>
          <template v-if="containerFlag">
            
            <a-descriptions-item label="柜型" class="required">
              <a-form-item name="cabinetId">
                <CabinetSelect width="180px" v-model:value="form.cabinetId" placeholder="请选择柜型"
                  :default="form.orderId ? false : true" />
              </a-form-item>
            </a-descriptions-item>
          </template>
        </a-descriptions>
        <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }" class="base-info-bottom" :labelStyle="{ minWidth: '120px' }">
          
          <a-descriptions-item label="装货时间" class="required">
            <a-form-item name="loadTime">
              <a-date-picker style="width: 180px" :show-time="{ format: 'HH:mm' }" format="YYYY-MM-DD HH:mm:00"
                valueFormat="YYYY-MM-DD HH:mm:00" v-model:value="form.loadTime" placeholder="请选择装货时间" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="卸货时间" class="required">
            <a-form-item name="unloadTime">
              <a-date-picker style="width: 180px" :show-time="{ format: 'HH:mm' }" format="YYYY-MM-DD HH:mm:00"
                valueFormat="YYYY-MM-DD HH:mm:00" v-model:value="form.unloadTime" placeholder="请选择卸货时间" />
            </a-form-item>
          </a-descriptions-item>
          
          <a-descriptions-item label="是否分段">
            <a-form-item name="splitTransportFlag">
            <a-switch v-model:checked="enabledSplitTransportFlag" @change="enabledSplitTransportFlagChange" checked-children="分段" un-checked-children="不分段"/>
          </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="所属区域">
            <a-form-item name="areaId">
              <smart-dict-select width="180px" keyCode="AREA" v-model:value="form.areaId" placeholder="请选择所属区域"
                disabled />
            </a-form-item>
          </a-descriptions-item>
         
          <a-descriptions-item label="税点(%)">
            <a-form-item name="taxPoint">
              <a-input-number v-model:value="form.taxPoint" :max="100" :min="0" :precision="2" placeholder="请输入税点"
                style="width: 180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="备注">
            <a-form-item name="remark">
              <a-textarea :auto-size="{ minRows: 1, maxRows: 5 }" v-model:value="form.remark" style="width: 180px"
                placeholder="请输入备注" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="客户联系人">
            <a-form-item name="shipperContact">
              <a-input v-model:value="form.shipperContact" placeholder="请输入客户联系人" style="width: 180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="客户订单号">
            <a-form-item name="shipperOrderNumber">
              <a-input v-model:value="form.shipperOrderNumber" placeholder="请输入客户订单号" style="width: 180px" />
            </a-form-item>
          </a-descriptions-item>
          <a-descriptions-item label="最迟提箱时间">
            <a-form-item name="latestPackingTime">
              <a-date-picker style="width: 180px" v-model:value="form.latestPackingTime" :show-time="{ format: 'YYYY-MM-DD HH:mm' }"
                format="YYYY-MM-DD HH:mm:00" placeholder="请选择最迟提箱时间" valueFormat="YYYY-MM-DD HH:mm:00" />
            </a-form-item>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
    </a-form>

    <!-- 路线信息 -->
    <PathManage ref="pathManageRef" :detail="form" :orderType="form.orderType" :transportRouteType="form.businessTypeCode" />

    <!-- 货物信息 -->
    <GoodsManage ref="goodsManageRef" :detail="form" />

    <!-- 运价信息 -->
    <FareManage ref="fareManageRef" :detail="form" />

    <a-card class="smart-margin-top5 end" size="small">
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="validateForm(false,false)">保存</a-button>
        <a-button type="primary" @click="validateForm(false,true)">保存并继续创建</a-button>
        <a-button type="primary" @click="validateForm(true,false)">保存并分配司机</a-button>
      </a-space>
    </a-card>
  </div>
  <ScheduleModal showNextBtn ref="scheduleModal" @refresh="scheduleHandler"/>
  <DrawerShipperAdd ref="drawerShipperRef" @reloadShipper="reloadShipper"/>
</template>
<script setup>
import { reactive, ref, onMounted, computed, nextTick, watch } from 'vue';
import { message } from 'ant-design-vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/system/user';
import _ from 'lodash';
import ScheduleModal from './components/schedule-modal.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import CabinetSelect from '/@/components/cabinet-select/index.vue';
import BusinessTypeSelect from '/@/components/business-type-select/index.vue';
import RoleEmployeeSelect from '/@/components/role-employee-select/index.vue';
import ShipperSelect from '/@/components/shipper-select/index.vue';
import GoodsManage from './components/order-goods-manage.vue';
import PathManage from './components/path-manage.vue';
import MailAddressManage from './components/mail-address-manage.vue';
import SmartEnumCheckbox from '/@/components/smart-enum-checkbox/index.vue';
import OrderEnterpriseSelect from '/@/components/order-enterprise-select/index.vue';
import OrderScheduleEmployeeSelect from '/@/components/order-schedule-employee-select/index.vue';
import DrawerShipperAdd from '/@/views/business/shipper/drawer-shipper-add.vue'
import { ENTERPRISE_TYPE_ENUM } from '/@/constants/business/enterprise-const';
import { TRANSPORTATION_TYPE_ENUM } from '/@/constants/business/transport-route-const';
import { SmartLoading } from '/@/components/smart-loading';
import { orderApi } from '/@/api/business/order/order-api';
import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';
import dayjs from "dayjs";
import { formWidth } from '/@/views/business/hooks/form-hooks';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import FareManage from './components/fare-manage.vue';

// ----- 加载form宽度
const helpDocFlag = computed(() => useAppConfigStore().$state.helpDocFlag);

let { initWidth } = formWidth();
let width = ref({});
width.value = initWidth();
watch(
  () => helpDocFlag.value,
  () => {
    width.value = initWidth();
  }
);

// 运输类型为集装箱类型
const containerFlag = computed(() => {
  return form.businessTypeCode == TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value;
});

// 订单类型是否为网络货运
let networkFlag = computed(() => {
  return form.orderType == ORDER_TYPE_ENUM.NETWORK_FREIGHT.value;
});

const enabledSplitTransportFlag= ref(false);
function enabledSplitTransportFlagChange(checked) {
  console.log(checked);
  form.splitTransportFlag = checked;
  if (checked) {
    form.orderType = ORDER_TYPE_ENUM.ORDINARY.value;
    form.nftEnterpriseId = null;
  }
}

// ---------------- 操作表单 ----------------
let route = useRoute();
let router = useRouter();

onMounted(() => {
  let orderId = route.query.orderId;
  if (orderId) {
    useUserStore().setTagName(route, '编辑订单');
    getDetail(orderId);
  } else {
    useUserStore().setTagName(route, '新建订单');
  }
});

// ----------------------- 详情查询 ----------------------------
async function getDetail(orderId) {
  SmartLoading.show();
  try {
    let response = await orderApi.getDetail(orderId);
    let detail = response.data;
    Object.assign(form, detail);
    nextTick(() => {
      pathManageRef.value.updatePathList(detail.pathList);
    });
  } catch (error) {
    console.log('error', error);
  } finally {
    SmartLoading.hide();
  }
}

// ---------------- 表单 ----------------
const formDefault = {
  orderId: null,
  businessTypeCode: TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value,
  pathList: [], // 路线列表
  area: null,
  cabinetId: null,
  containerBusinessTypeId: null,
  goodsList: [],
  remark: null,
  scheduleId: null,
  shipperId: null,
  transportRouteName: null,
  nftEnterpriseId: null,
  orderType: ORDER_TYPE_ENUM.ORDINARY.value,
  splitTransportFlag: false,
  mailAddress: null,
  taxPoint: 9,
  shipperContact: null,
  shipperOrderNumber: null,
  latestPackingTime: null,
  loadTime: null,
  unloadTime: null,
};
const form = reactive({ ...formDefault });

let formRef = ref();
const rules = {
  shipperId: [{ required: true, message: '请选择客户' }],
  nftEnterpriseId: [{ required: true, message: '请选择货运平台' }],
  scheduleId: [{ required: true, message: '请选择本单调度员' }],
  orderType: [{ required: true, message: '请选择订单类型' }],
  businessTypeCode: [{ required: true, message: '请选择运输类型' }],
  cabinetId: [{ required: true, message: '请选择柜型' }],
  containerBusinessTypeId: [{ required: true, message: '请选择业务类型' }],
  loadTime: [{ required: true, message: '装货时间不能为空', }],
  unloadTime: [{ required: true, message: '卸货时间不能为空', }]
};

function changeOrderType() {
  nextTick(() => {
    formRef.value.validateFields('loadTime');
  });
}

const goodsManageRef = ref();
const pathManageRef = ref();
const fareManageRef = ref();

async function validateForm(showSchedule,resetForm) {
  let validate = false;
  try {
    await formRef.value.validate();
    await fareManageRef.value.formRef.validate();
    validate = true;
  } catch (e) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
  if (validate) {
    submitForm(showSchedule,resetForm);
  }
}

function scheduleHandler(type){
  switch(type){
    case 'back':
      onClose();
      break;
    case 'create':
      reset();
      break;
  }
}

async function reset(){
  form.orderId = undefined
  formRef.value.resetFields()
  pathManageRef.value.resetForm();
  nextTick(()=>{
    goodsManageRef.value.resetTable();
  })
  fareManageRef.value.resetForm();
  useUserStore().setTagName(route, '新建订单');
}

const scheduleModal = ref();

function showScheduleModal() {
  const { orderId, orderType, nftEnterpriseId } = form;
  scheduleModal.value.showModal(orderId, orderType, nftEnterpriseId);
}

async function submitForm(showSchedule,resetForm) {
  console.log(goodsManageRef.value);
  let goodsList = _.cloneDeep(goodsManageRef.value.tableData);
  let distance = pathManageRef.value.form.distance;
  let pathList = pathManageRef.value.form.pathList;

  pathList = pathList.filter((e) => e.provinceName);
  if (_.isEmpty(pathList)) {
    message.error('请选择运输路线');
    return;
  }
  if (_.isEmpty(goodsList)) {
    message.error('请输入货物信息');
    return;
  }
  goodsList.forEach((item) => {
    item.goodsCount = item.goodsCount || 0;
    item.goodsWeight = item.goodsWeight || 0;
    item.goodsUnitPrice = item.goodsUnitPrice || 0;
  });
  SmartLoading.show();
  try {
    let params = _.cloneDeep(form);
    Object.assign(params, {
      goodsList,
      pathList,
      distance
    });
    let fareManage = fareManageRef.value.getFareManage();
    Object.assign(params, { ...fareManage });
    params.taxPoint = params.taxPoint || 0
    if (params.orderId) {
      await orderApi.updateOrder(params);
    } else {
      const res = await orderApi.createOrder(params);
      form.orderId = res.data
    }
    message.success('操作成功');
    if(showSchedule){
        showScheduleModal()
        useUserStore().setTagName(route, '编辑订单');
    }else if(resetForm){
      reset();
    }else{
      onClose();
    }
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

// 关闭
function onClose() {
  useUserStore().closePage(route, router,'/order/list');
}

function changeShipper(shipperId, shipper) {
  if (!shipper) {
    form.areaId = null;
    return;
  }
  form.areaId = shipper.area;
  form.taxPoint = shipper.taxPoint || 0;
}


const drawerShipperRef = ref()
function showDrawer(){
  drawerShipperRef.value.showDrawer()
}

const shipperSelectRef = ref()
function reloadShipper(){
  shipperSelectRef.value.queryData()
}
</script>
<style lang="less" scoped>
::v-deep .ant-form-item-required{
  color: #ff4d4f !important;
}

.end{
  position: sticky;
  bottom: 0;
}

::v-deep .ant-form-item {
  margin-bottom: 0 !important;
}

::v-deep .ant-descriptions-row>th {
  width: 100px;
}

::v-deep .ant-descriptions-row>td {
  width: 200px;
}

::v-deep .ant-descriptions-item-content {
  padding: 2px 16px !important;
}

::v-deep .ant-upload-select {
  margin-bottom: 0px !important;
}

::v-deep th.required {
  color: red;
}

.form-width {
  width: 180px;
}

.base-info-top {
  ::v-deep .ant-descriptions-view {
    border-bottom-left-radius: 0;
    border-bottom-right-radius: 0;
    border-bottom: 0 !important;
  }
}
.base-info-bottom {
  ::v-deep .ant-descriptions-view {
    border-top-left-radius: 0;
    border-top-right-radius: 0;
  }
}
</style>
