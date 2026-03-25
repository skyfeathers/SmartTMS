<template>
  <a-card size="small">
    <a-page-header :title="oilCardDetail.oilCardNo">
      <template #extra>
        <a-button @click="updateOilCard(oilCardDetail)" v-privilege="'oil:edit'">编辑</a-button>
      </template>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="当前余额">{{ oilCardDetail.balance }}</a-descriptions-item>
            <a-descriptions-item v-if="OIL_CARD_TYPE_ENUM.MASTER.value == oilCardDetail.type " label="待充值余额合计">
              {{ oilCardDetail.preDistributionBalance }}
            </a-descriptions-item>
            <a-descriptions-item label="油卡品牌">{{
                oilCardDetail.brand && oilCardDetail.brand.map(item =>
                    item.valueName).join(',')
              }}
            </a-descriptions-item>
            <a-descriptions-item label="油卡类型">{{
                $smartEnumPlugin.getDescByValue('OIL_CARD_TYPE_ENUM',
                    oilCardDetail.type)
              }}
            </a-descriptions-item>
            <a-descriptions-item label="定点加油">{{ oilCardDetail.fixedPointFlag ? '是' : '否'}}
            </a-descriptions-item>
            <a-descriptions-item v-if="OIL_CARD_TYPE_ENUM.SLAVE.value == oilCardDetail.type " label="主卡卡号">
              <a href="javascript:void(0)" @click="gotoCardDetail(oilCardDetail.masterOilCardId)"> {{ oilCardDetail.masterOilCardNo }}</a>
            </a-descriptions-item>
            <a-descriptions-item label="领取人">{{ oilCardDetail.receiveUserName }}-{{ oilCardDetail.receiveUserPhone }}</a-descriptions-item>
            <a-descriptions-item label="取卡时间">-</a-descriptions-item>
            <a-descriptions-item label="持卡司机">{{ oilCardDetail.useDriverName }}-{{ oilCardDetail.useDriverPhone }}</a-descriptions-item>
            <a-descriptions-item label="当前持卡车">{{ oilCardDetail.useVehicleNumber }}</a-descriptions-item>
            <a-descriptions-item label="用途">{{
                oilCardDetail.purpose && oilCardDetail.purpose.map(item =>
                    item.valueName).join(',')
              }}
            </a-descriptions-item>
            <a-descriptions-item label="备注">{{ oilCardDetail.remark }}</a-descriptions-item>
            <a-descriptions-item label="创建人">{{ oilCardDetail.createUserName }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ oilCardDetail.createTime }}</a-descriptions-item>
          </a-descriptions>
        </div>
        <div class="extra">
          <div class="status">
            <div class="label">状态</div>
            <div class="value">{{ oilCardDetail.disabledFlag ? '禁用' : '启用' }}</div>
          </div>
        </div>
      </div>
    </a-page-header>
  </a-card>
  <a-card class="smart-margin-top10 content-card" size="small">
    <a-tabs>
      <a-tab-pane key="3" tab="交易记录">
        <a-form class="smart-query-form">
          <a-row class="smart-query-form-row">
            <a-form-item label="性质" class="smart-query-form-item">
              <SmartEnumSelect width="100px" v-model:value="dealQueryForm.type"
                               enum-name="OIL_CARD_BALANCE_RECORD_TYPE_ENUM"/>
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="关键字">
              <a-input v-model:value="dealQueryForm.keyWords" class="form-width" placeholder="流水单号/创建人"/>
            </a-form-item>
            <a-form-item class="smart-query-form-item" label="创建时间">
              <a-range-picker v-model:value="dealQueryForm.createTime" :ranges="defaultTimeRanges" class="form-width"
                              @change="changeCreateTime"/>
            </a-form-item>

            <a-form-item class="smart-query-form-item smart-margin-left10">
              <a-button type="primary" @click="searchOilRecord">
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
            </a-form-item>
          </a-row>
        </a-form>
        <a-table :columns="columns" :dataSource="dealTableData" :pagination="false" :scroll="{ x: 1300 }" bordered
                 size="small">

        </a-table>
        <div class="smart-query-table-page">
          <a-pagination v-model:current="dealQueryForm.pageNum" v-model:pageSize="dealQueryForm.pageSize"
                        :defaultPageSize="dealQueryForm.pageSize" :pageSizeOptions="PAGE_SIZE_OPTIONS"
                        :show-total="(dealTotal) => `共${dealTotal}条`" :total="dealTotal" show-less-items showQuickJumper
                        showSizeChanger @change="queryOilRecord" @showSizeChange="queryOilRecord"/>
        </div>
      </a-tab-pane>
      <a-tab-pane key="4" tab="操作记录">
        <DataTracerList
            :business-id="oilCardDetail.oilCardId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.OIL_CARD.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
  <!--新建编辑modal-->
  <OilOperateModal ref="operateModal" @reloadList="getDetail"/>
</template>
<script setup>
import DataTracerList from '/@/components/data-tracer/index.vue';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { useSpinStore } from '/@/store/modules/system/spin';
import { oilApi } from '/@/api/business/card/oil-api';
import { onMounted, reactive, ref, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useUserStore } from '/@/store/modules/system/user';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { OIL_CARD_TYPE_ENUM } from '/@/constants/business/card-const';
import _ from 'lodash';
//弹窗
import OilOperateModal from './components/oil-operate-modal.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';

const columns = computed(() => {
  if (oilCardDetail.type == OIL_CARD_TYPE_ENUM.MASTER.value) {
    return dealColumns;
  }
  return dealColumns.filter(e => e.dataIndex != 'relatedOilCardNo');
});

let route = useRoute();
let oilCardId = ref();

watch(
    () => route.query,
    (val) => {
      if (val.oilCardId) {
        oilCardId.value = val.oilCardId;
        getDetail();
        queryOilRecord();
      }
    }
)

onMounted(() => {
  oilCardId.value = route.query.oilCardId;
  getDetail();
  queryOilRecord();
});
//----------------------- 列表查询---------------------
const dealQueryFormState = {
  oilCardId: oilCardId,
  type: null,
  keyWords: '',
  startTime: '',
  endTime: '',
  createTime: [],
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const dealTotal = ref(0);
const dealQueryForm = reactive({ ...dealQueryFormState });
const dealColumns = reactive([
  {
    title: '交易流水单号',
    minWidth: 110,
    dataIndex: 'balanceRecordNo',
  },
  {
    title: '交易时间',
    width: 170,
    dataIndex: 'transactionTime',
  },
  {
    title: '性质',
    width: 80,
    dataIndex: 'recordTypeDesc'
  },
  {
    title: '影响副卡',
    minWidth: 80,
    dataIndex: 'relatedOilCardNo',
  },
  {
    title: '交易金额',
    width: 180,
    dataIndex: 'changeBalance',
  },
  {
    title: '变更后余额',
    width: 180,
    dataIndex: 'afterBalance',
  },
  {
    title: '备注',
    minWidth: 70,
    dataIndex: 'remark',
  },
  {
    title: '创建人',
    width: 70,
    dataIndex: 'createUserName',
  },
  {
    title: '创建时间',
    width: 170,
    dataIndex: 'createTime',
  },
]);
const dealTableData = ref([]);

// 交易记录
async function queryOilRecord () {
  try {
    useSpinStore().show();
    let responseModel = await oilApi.queryOilRecord(dealQueryForm);
    const list = responseModel.data.list;
    dealTotal.value = responseModel.data.total;
    dealTableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

function searchOilRecord () {
  dealQueryForm.pageNum = 1;
  queryOilRecord();
}

function resetQuery () {
  Object.assign(dealQueryForm, dealQueryFormState);
  queryOilRecord();
}

function changeCreateTime (dates, dateStrings) {
  dealQueryForm.startTime = dateStrings[0];
  dealQueryForm.endTime = dateStrings[1];
}

// --------------------- 油卡详情 ---------------------
let oilCardDetail = reactive({});

async function getDetail () {
  useSpinStore().show();
  try {
    let responseModel = await oilApi.getOilDetail(oilCardId.value);
    let detail = responseModel.data;
    Object.assign(oilCardDetail, detail);
  } catch (error) {
    console.log('error', error);
  } finally {
    useSpinStore().hide();
  }
}

const operateModal = ref();
function updateOilCard () {
  operateModal.value.showModal(oilCardDetail.type, _.cloneDeep(oilCardDetail));
}

let router = useRouter();
function gotoCardDetail(oilCardId){
  useUserStore().closePage(route, router);
  router.push({
    path: '/card/oil-detail',
    query: { oilCardId }
  });
}

// ----------------- 关闭页面 -----------------
function onClose () {
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
