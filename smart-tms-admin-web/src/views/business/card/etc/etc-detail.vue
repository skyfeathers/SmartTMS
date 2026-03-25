<template>
 <a-card size="small">
    <a-page-header :title="etcDetail.etcNo">
      <template #extra>
        <a-button @click="updateOilCard(etcDetail)">编辑</a-button>
      </template>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="所属公司">{{ etcDetail.enterpriseName }}</a-descriptions-item>
            <a-descriptions-item label="使用司机">{{ etcDetail.userName }}-{{ etcDetail.userPhone }}</a-descriptions-item>
            <a-descriptions-item label="绑定车牌">{{ etcDetail.useVehicleNumber }}</a-descriptions-item>
            <a-descriptions-item label="创建人">{{ etcDetail.createTime }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ etcDetail.createTime }}</a-descriptions-item>
            <a-descriptions-item label="备注">{{ etcDetail.createUserName }}</a-descriptions-item>
          </a-descriptions>
        </div>
        <div class="extra">
          <div class="status">
            <div class="label">状态</div>
            <div class="value">{{ etcDetail.disabledFlag ? '禁用' : '启用' }}</div>
          </div>
        </div>
      </div>
    </a-page-header>
  </a-card>
  <a-card class="smart-margin-top10 content-card" size="small">
    <a-tabs>
<!--      <a-tab-pane key="3" tab="交易记录">
        <a-form class="smart-query-form">
          <a-row class="smart-query-form-row">
            <a-form-item label="状态" class="smart-query-form-item">
              <a-radio-group @change="queryEtcRecord" v-model:value="dealQueryForm.incomeFlag">
                <a-radio-button :value="false">消费</a-radio-button>
                <a-radio-button :value="true">充值</a-radio-button>
              </a-radio-group>
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="关键字">
              <a-input v-model:value="dealQueryForm.keyWords" class="form-width" placeholder="流水单号/创建人" />
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="创建时间">
              <a-range-picker v-model:value="dealQueryForm.createTime" :ranges="defaultTimeRanges" class="form-width"
                @change="changeCreateTime" />
            </a-form-item>

            <a-form-item class="smart-query-form-item smart-margin-left10">
              <a-button type="primary" @click="queryEtcRecord">
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
        <a-table :columns="dealColumns" :dataSource="dealTableData" :pagination="false" size="small">
          <template #incomeFlag="{ record }">
            <span>{{ record.incomeFlag ? '充值' : '消费' }} </span>
          </template>
        </a-table>
        <div class="smart-query-table-page">
          <a-pagination v-model:current="dealQueryForm.pageNum" v-model:pageSize="dealQueryForm.pageSize"
            :defaultPageSize="dealQueryForm.pageSize" :pageSizeOptions="PAGE_SIZE_OPTIONS"
            :show-total="(dealTotal) => `共${dealTotal}条`" :total="dealTotal" show-less-items showQuickJumper
            showSizeChanger @change="queryEtcRecord" @showSizeChange="queryEtcRecord" />
        </div>
      </a-tab-pane>-->
      <a-tab-pane key="4" tab="操作记录">
        <DataTracerList
            :business-id="etcDetail.etcId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.ETC.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
    <!--新建编辑modal-->
  <EtcOperateModal ref="operateModal" @reloadList="getDetail" />
</template>
<script setup>
import { useSpinStore } from "/@/store/modules/system/spin";
import { etcApi } from "/@/api/business/card/etc-api";
import { onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { AUDIT_STATUS_ENUM, PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useUserStore } from "/@/store/modules/system/user";
//弹窗
import EtcOperateModal from './components/etc-operate-modal.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
let router = useRouter();
let route = useRoute();
let etcId = ref();

onMounted(() => {
  etcId.value = route.query.etcId;
  getDetail();
  queryEtcRecord();
})
//----------------------- 列表查询---------------------
const dealQueryFormState = {
  etcId: etcId,
  keyWords: '',
  createStartTime: undefined,
  createEndTime: undefined,
  createTime: [],
  incomeFlag: true,
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const dealTotal = ref(0);
const dealQueryForm = reactive({ ...dealQueryFormState });
const dealColumns = reactive([
  {
    title: '交易流水单号',
    width: 180,
    dataIndex: 'balanceRecordId',
  },
  {
    title: '交易时间',
    width: 180,
    dataIndex: 'createTime',
  },
  {
    title: '性质',
    width: 180,
    dataIndex: 'incomeFlag',
    slots: { customRender: 'incomeFlag' },
  },
  {
    title: '交易金额',
    width: 180,
    dataIndex: 'changeBalance',
  },
  {
    title: '备注',
    width: 180,
    dataIndex: 'remark',
  },
  {
    title: '创建人',
    width: 180,
    dataIndex: 'createUserName',
  },
])
const dealTableData = ref([])
// 交易记录
function changeCreateTime(dates, dateStrings) {
  dealQueryForm.createStartTime = dateStrings[0];
  dealQueryForm.createEndTime = dateStrings[1];
}
async function queryEtcRecord() {
  try {
    useSpinStore().show();
    let responseModel = await etcApi.queryEtcRecord(dealQueryForm);
    const list = responseModel.data.list;
    dealTotal.value = responseModel.data.total;
    dealTableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}
function resetQuery() {
  Object.assign(dealQueryForm, dealQueryFormState);
  queryEtcRecord();
}
// --------------------- 油卡详情 ---------------------
let etcDetail = reactive({});

async function getDetail() {
  useSpinStore().show();
  try {
    let responseModel = await etcApi.getEtcDetail(etcId.value);
    let detail = responseModel.data;
    Object.assign(etcDetail, detail);
    // etcDetail.vehicleList = (etcDetail.driverVehicleList || []).map(e=>e.vehicleNumber).join(',');
  } catch (error) {
    console.log("error", error);
  } finally {
    useSpinStore().hide();
  }
}

const operateModal = ref();
function updateOilCard(rowData) {
  operateModal.value.showModal(rowData);
}
// ----------------- 关闭页面 -----------------
function onClose() {
  useUserStore().closePage(route, router);
}


</script>
<style lang='less' scoped>
.content {
  display: flex;
}

.status {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  width: max-content;
  margin-right: 30px;

  .label {
    font-size: 15px;
    color: #8c8c8c;
  }

  .value {
    font-size: 25px;
    color: #1f1f1f;
  }
}

.ant-page-header {
  padding: 0;
}

.content-card {
  ::v-deep(.ant-card-body) {
    padding-top: 0px;
  }
}
</style>
