<template>
  <a-form class="smart-query-form" v-privilege="'yard:edit'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="堆场名称/堆场编号/创建人"/>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange"/>
        </a-space>
      </a-form-item>

      <a-form-item label="类型状态" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.disabledFlag"  @change="quickQuery">
          <a-radio-button :value="null">全部</a-radio-button>
          <a-radio-button value="false">启用</a-radio-button>
          <a-radio-button value="true">禁用</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="quickQuery">
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
        <a-button @click="addOrUpdate()" v-privilege="'yard:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建堆场
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :scroll="{ x: 1900,y:500 }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="yardId"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'area'">
          {{ getArea(record) }}
        </template>
        <template v-else-if="column.dataIndex === 'disabledFlag'">
          <a-tag v-show="record.disabledFlag" color="error">禁用</a-tag>
          <a-tag v-show="!record.disabledFlag" color="success">启用</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addOrUpdate(record)" v-privilege="'yard:edit'" type="link">编辑</a-button>
            <a-button @click="confirmDelete(record.yardId)" v-privilege="'yard:delete'" type="link">删除</a-button>
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
    <YardOperateModal ref="operateModal" @reloadList="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import YardOperateModal from './components/yard-operate-modal.vue';
import {reactive, ref, onMounted} from 'vue';
import {message, Modal} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {yardApi} from '/@/api/business/business-material/yard-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';

const columns = reactive([
  {
    title: '堆场编号',
    dataIndex: 'yardCode',
    width: 80
  },
  {
    title: '堆场名称',
    dataIndex: 'yardName',
    width: 200
  },
  {
    title: '堆场地区',
    dataIndex: 'area',
    width:240,
  },
  {
    title: '堆场地址',
    dataIndex: 'address'
  },
  {
    title: '联系人',
    dataIndex: 'contact',
    width: 240
  },
  {
    title: '联系人电话',
    width: 120,
    dataIndex: 'contactPhone',
  },
  {
    title: '堆场状态',
    width: 80,
    dataIndex: 'disabledFlag',
  },
  {
    title: '创建人',
    width: 70,
    dataIndex: 'createUserName',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 90
  },
]);

const queryFormState = {
  keywords: '',
  disabledFlag: null,
  endTime: null,
  startTime: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const operateModal = ref();

// 日期选择
let searchDate = ref([]);

function dateChange(dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

// 地区
function getArea(record) {
  let area = "";
  if (record.provinceName) {
    area = area + record.provinceName;
  }
  if (record.cityName) {
    area = area + record.cityName;
  }
  if (record.districtName) {
    area = area + record.districtName;
  }
  return area;
}

function resetQuery() {
  searchDate.value = [];
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function quickQuery() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await yardApi.pageQuery(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function confirmDelete(yardId) {
  Modal.confirm({
    title: "确定要删除吗？",
    content: "删除后，该信息将不可恢复",
    okText: "删除",
    okType: "danger",
    onOk() {
      del(yardId);
    },
    cancelText: "取消",
    onCancel() {
    },
  });
}

async function del(yardId) {
  try {
    useSpinStore().show();
    await yardApi.delete(yardId);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
};

function addOrUpdate(rowData) {
  operateModal.value.showModal(rowData);
}

onMounted(ajaxQuery);
</script>
