<template>
  <a-form class="smart-query-form" v-privilege="'vehicle-review:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="审车时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange"/>
        </a-space>
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

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="addOrUpdate()" type="primary" size="small" v-privilege="'vehicle-review:add'">
          <template #icon>
            <PlusOutlined/>
          </template>
          新增费用
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :scroll="{ x: '100%' }"
        size="small"
        bordered
        :dataSource="tableData"
        :columns="columns"
        rowKey="reviewId"
        :pagination="false"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'vehicleNumber'">
          <a v-if="$privilege('vehicle:detail')" @click="handleDetail(record)">{{ record.vehicleNumber }}</a>
          <span v-else>{{ record.vehicleNumber }}</span>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addOrUpdate(record)" type="link" v-privilege="'vehicle-review:edit'">编辑</a-button>
            <a-button @click="confirmDelete(record.reviewId)" type="link" danger v-privilege="'vehicle-review:delete'">删除</a-button>
          </div>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
          :show-total="(total) => `共${total}条`"
      />
    </div>
    <!--新建编辑modal-->
    <VehicleReviewOperateModal ref="operateModal" @reloadList="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import VehicleReviewOperateModal from './components/vehicle-review-operate-modal.vue';
import {reactive, ref, onMounted} from 'vue';
import {message, Modal} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {vehicleReviewApi} from '/@/api/business/vehicle-review/vehicle-review-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps({
  moduleId: {
    type: Number,
    require: true,
  }
});


// table 相关
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const columns = reactive([
  {
    title: '车辆号',
    dataIndex: 'vehicleNumber',
    width: 140,
    ellipsis: true,
  },
  {
    title: '审车时间',
    dataIndex: 'reviewDate',
    width: 130,
  },
  {
    title: '审车费用',
    dataIndex: 'reviewAmount',
    width: 130,
  },
  {
    title: '审车人',
    dataIndex: 'reviewPerson',
    width: 100,
    ellipsis: true,
  },
  {
    title: '审车地点',
    dataIndex: 'reviewPlant',
    width: 100,
    ellipsis: true,
  },
  {
    title: '审车备注',
    dataIndex: 'remark',
    width: 200,
    ellipsis: true,
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 120,
    ellipsis: true,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 170,
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 100,
  },
]);
// 日期选择
let searchDate = ref();

function dateChange(dates, dateStrings) {
  queryForm.startDate = dateStrings[0];
  queryForm.endDate = dateStrings[1];
}

// 查询相关
const queryFormState = {
  vehicleId: props.moduleId,
  endDate: null,
  startDate: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true
};
const queryForm = reactive({...queryFormState});

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await vehicleReviewApi.queryPage(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

function resetQuery() {
  searchDate.value = [];
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function confirmDelete(reviewId) {
  Modal.confirm({
    title: '提示',
    content: '确认要删除该审车信息吗？',
    okText: '删除',
    okType: 'danger',
    onOk: () => {
      del(reviewId);
    },
  });
}

// 删除
async function del(reviewId) {
  try {
    useSpinStore().show();
    await vehicleReviewApi.delete(reviewId);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// 新建编辑
const operateModal = ref();
function addOrUpdate(rowData) {
  operateModal.value.showModal(rowData, props.moduleId);
}

function handleDetail(record) {
  router.push({
    path: '/vehicle/vehicle-detail',
    query: {
      vehicleId: record.vehicleId,
    }
  })
}

onMounted(ajaxQuery);
</script>
