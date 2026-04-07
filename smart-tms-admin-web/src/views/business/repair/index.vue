<template>
  <a-form class="smart-query-form" v-privilege="'vehicle-repair:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="创建时间" class="smart-query-form-item">
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
        <a-button @click="addOrUpdate()" type="primary" size="small" v-privilege="'vehicle-repair:add'">
          <template #icon>
            <PlusOutlined/>
          </template>
          新增维修信息
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
        rowKey="repairId"
        :pagination="false"
    >
      <template #bodyCell="{ text, record, index, column }">
         <template v-if="column.dataIndex === 'vehicleNumber'">
          <a v-if="$privilege('vehicle:detail')" @click="handleDetail(record)">{{ record.vehicleNumber }}</a>
          <span v-else>{{ record.vehicleNumber }}</span>
        </template>
        <template v-if="column.dataIndex === 'contentVOList'">
            <div class="repair-content">
              <div>
                <div  v-for="(item,index) in record.contentVOList" :key="index">
                  <div v-if="index<2" style="line-height: 20px;">
                    <span>{{ item.repairContent }}</span>,
                    <span>￥{{ item.repairAmount?.toFixed(1) }}</span>
                  </div>
                </div>
              </div>
              <div v-if="record.contentVOList?.length>2">
                <a-button type="link" :size="size" @click="showContentVOListModal(record.contentVOList)">更多></a-button>
              </div>
            </div>
        </template>
        <template v-else-if="column.dataIndex === 'attachment'">
          <file-preview :fileList="record.attachment" :width="50"/>
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addOrUpdate(record)" type="link" v-privilege="'vehicle-repair:edit'">编辑</a-button>
            <a-button @click="confirmDelete(record.repairId)" type="link" danger v-privilege="'vehicle-repair:delete'">删除</a-button>
          </div>
          
        </template>
      </template>
    </a-table>

    <!-- 维修内容弹出框 -->
    <a-modal :open="openContentVOListModal" title="维修内容" @ok="contentVOListModalOk" @cancel="contentVOListModalCancel">
      <a-table
      :columns="columnsContentVOList"
      :data-source="dataContentVOList"
      bordered
      :pagination="false"
      >
        <template #bodyCell="{ column, text }">
          <template v-if="column.dataIndex === 'repairAmount'">
            <span>￥{{ text?.toFixed(2) }}</span>
          </template>
        </template>
      </a-table>
    </a-modal>

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
    <RepairOperateModal ref="operateModal" @reloadList="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import RepairOperateModal from './components/repair-operate-modal.vue';
import FilePreview from '/@/components/file-preview/index.vue';

import {reactive, ref, onMounted} from 'vue';
import {message, Modal} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {repairApi} from '/@/api/business/repair/repair-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';
import lodash from "lodash";
import { useRouter } from 'vue-router';
import { INSURANCE_MODULE_TYPE_ENUM } from '/@/constants/business/insurance-const';

const router = useRouter();

const props = defineProps({
  moduleId: {
    type: Number,
    require: true,
  },
  moduleType: {
    type: Number,
    require: true,
  }
});

// 维修弹出框相关
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
const dataContentVOList=ref([])
const openContentVOListModal = ref(false);

function showContentVOListModal(contentVOList){
  openContentVOListModal.value=true
  dataContentVOList.value=contentVOList
}
function contentVOListModalOk(){
  openContentVOListModal.value = false;
}
function contentVOListModalCancel(){
  openContentVOListModal.value = false;
}


// table 相关
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const columns = reactive([
  {
    title: 'ID',
    width: 50,
    dataIndex: 'repairId',
  },
  {
    title: '维修对象',
    dataIndex: 'vehicleNumber',
    width: 120,
  },
  {
    title: '维修厂家',
    dataIndex: 'repairPlantName',
    width: 120,
    ellipsis: true,
  },
  {
    title: '维修人',
    dataIndex: 'repairPerson',
    width: 120,
  },
  {
    title: '维修时间',
    width: 150,
    dataIndex: 'repairDate',
  },
  {
    title: '维修内容',
    dataIndex: 'contentVOList',
    width: 200,
  },
  {
    title: '维修配件附件',
    width: 120,
    dataIndex: 'attachment',
  },
  {
    title: '创建人',
    width: 100,
    dataIndex: 'createUserName',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
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
  queryForm.createStartTime = dateStrings[0];
  queryForm.createEndTime = dateStrings[1];
}

// 查询相关
const queryFormState = {
  moduleId: props.moduleId,
  moduleType: props.moduleType,
  createEndTime: null,
  createStartTime: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true
};
const queryForm = reactive({...queryFormState});

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await repairApi.queryPage(queryForm);
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

function confirmDelete(repairId) {
  Modal.confirm({
    title: '提示',
    content: '确认要删除该维修信息吗？',
    okText: '删除',
    okType: 'danger',
    onOk: () => {
      del(repairId);
    },
  });
}

// 删除
async function del(repairId) {
  try {
    useSpinStore().show();
    await repairApi.delete(repairId);
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
  operateModal.value.showModal(lodash.clone(rowData), props.moduleId, props.moduleType);
}

// 详情
function handleDetail(record) {
  let path = '/vehicle/vehicle-detail';
  let query = {
    vehicleId: record.moduleId,
  }
  if(record.moduleType == INSURANCE_MODULE_TYPE_ENUM.BRACKET.value) {
    path = '/bracket/detail';
    query = {
      bracketId: record.moduleId,
    }
  }
  router.push({
    path,
    query,
  })
}



onMounted(ajaxQuery);
</script>

<style lang="less" scoped>
.repair-content{
  display: flex; 
  justify-content: space-between; 
  align-items: center;
  padding: 8px 0;
}
</style>
