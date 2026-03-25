<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="保养时间" class="smart-query-form-item">
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
        <a-button @click="addOrUpdate()" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新增保养
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :scroll="{ x: 1300 }"
        size="small"
        bordered
        :dataSource="tableData"
        :columns="columns"
        rowKey="maintenanceId"
        :pagination="false"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'vehicleNumber'">
          <a v-if="$privilege('vehicle:detail')" @click="handleDetail(record)">{{ record.vehicleNumber }}</a>
          <span v-else>{{ record.vehicleNumber }}</span>
        </template>
        <template v-if="column.dataIndex === 'contentVOList'">
          <div v-for="(item,index) in record.contentVOList" :key="index">
            <div v-if="index<2" style="line-height: 20px;">
              <span>{{ item.maintenanceContent }}</span>,
              <span>￥{{ item.maintenanceAmount?.toFixed(1) }}</span>
            </div>
          </div>
          <div v-if="record.contentVOList?.length>2">
            <a-button :size="size" type="link" @click="showContentVOListModal(record.contentVOList)">更多></a-button>
          </div>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <a-button @click="addOrUpdate(record)" type="link">编辑</a-button>
          <a-button @click="confirmDelete(record.maintenanceId)" type="link">删除</a-button>
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
    <VehicleMaintenanceOperateModal ref="operateModal" @reloadList="ajaxQuery"/>

    <!-- 保养内容弹出框 -->
    <a-modal :open="openContentVOListModal" title="维修内容" @cancel="contentVOListModalCancel" @ok="contentVOListModalOk">
      <a-table
          :columns="columnsContentVOList"
          :data-source="dataContentVOList"
          :pagination="false"
          bordered
      >
        <template #bodyCell="{ column, text }">
          <template v-if="column.dataIndex === 'repairAmount'">
            <span>￥{{ text?.toFixed(2) }}</span>
          </template>
        </template>
      </a-table>
    </a-modal>
  </a-card>
</template>
<script setup>
import VehicleMaintenanceOperateModal from './components/vehicle-maintenance-operate-modal.vue';
import {reactive, ref, onMounted} from 'vue';
import {message, Modal} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {vehicleMaintenanceApi} from '/@/api/business/vehicle-maintenance/vehicle-maintenance-api';
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
  },
  {
    title: '保养时间',
    dataIndex: 'maintenanceDate',
  },
  {
    title: '下次保养时间',
    dataIndex: 'nextMaintenanceDate',
  },
  {
    title: '保养内容',
    dataIndex: 'contentVOList',
  },
  {
    title: '保养里程',
    dataIndex: 'mileage',
  },
  {
    title: '下次保养里程',
    dataIndex: 'nextMaintenanceMileage',
  },
  {
    title: '保养人',
    dataIndex: 'maintenancePerson',
  },
  {
    title: '保养地点',
    width: 100,
    dataIndex: 'maintenancePlant',
  },
  {
    title: '保养备注',
    dataIndex: 'remark',
  },
  {
    title: '创建人',
    width: 100,
    dataIndex: 'createUserName',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
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
    let responseModel = await vehicleMaintenanceApi.queryPage(queryForm);
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

function confirmDelete(maintenanceId) {
  Modal.confirm({
    title: '提示',
    content: '确认要删除该保养信息吗？',
    okText: '删除',
    okType: 'danger',
    onOk: () => {
      del(maintenanceId);
    },
  });
}

// 删除
async function del(maintenanceId) {
  try {
    useSpinStore().show();
    await vehicleMaintenanceApi.delete(maintenanceId);
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

function addOrUpdate (rowData) {
  operateModal.value.showModal(rowData, props.moduleId);
}



// 保养明细详情
const columnsContentVOList = [
  {
    title: '维修内容',
    dataIndex: 'repairContent',
    align:'center'
  },
  {
    title: '费用',
    dataIndex: 'repairAmount',
    align:'center'
  },
]
const dataContentVOList = ref([]);
const openContentVOListModal = ref(false);

function showContentVOListModal (contentVOList) {
  openContentVOListModal.value = true;
  dataContentVOList.value = contentVOList;
}

function contentVOListModalOk () {
  openContentVOListModal.value = false;
}

function contentVOListModalCancel () {
  openContentVOListModal.value = false;
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
