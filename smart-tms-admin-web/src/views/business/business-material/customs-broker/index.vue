<template>
  <a-form class="smart-query-form" v-privilege="'customsBroker:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="报关行名称/简称/编号/联系人/创建人"/>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange"/>
        </a-space>
      </a-form-item>

      <a-form-item label="状态" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.disabledFlag" @change="quickQuery">
          <a-radio-button :value="null">全部</a-radio-button>
          <a-radio-button :value="false">启用</a-radio-button>
          <a-radio-button :value="true">禁用</a-radio-button>
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
        <a-button @click="addOrUpdate()" v-privilege="'customsBroker:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建报关行
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :scroll="{ x: '100%' }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="customsBrokerId"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'address'">
          {{ getAddress(record) }}
        </template>
        <template v-else-if="column.dataIndex === 'disabledFlag'">
          <a-tag v-show="record.disabledFlag" color="error">禁用</a-tag>
          <a-tag v-show="!record.disabledFlag" color="success">启用</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addOrUpdate(record)" v-privilege="'customsBroker:add'" type="link">编辑</a-button>
            <a-button @click="confirmDelete(record.customsBrokerId)" v-privilege="'customsBroker:delete'" type="link">
              删除
            </a-button>
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
    <CustomsBrokerOperateModal ref="operateModal" @reloadList="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import CustomsBrokerOperateModal from './components/customs-broker-operate-modal.vue';
import {reactive, ref, onMounted} from 'vue';
import {message, Modal} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {customsBrokerApi} from '/@/api/business/business-material/customs-broker-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';

const columns = reactive([
  {
    title: '报关行编号',
    dataIndex: 'customsBrokerCode',
    width: 90,
    ellipsis: true,
  },
  {
    title: '报关行名称',
    dataIndex: 'customsBrokerName',
    width: 120,
    ellipsis: true,
  },
  {
    title: '报关行简称',
    dataIndex: 'customsBrokerShortName',
    width: 120,
    ellipsis: true,
  },
  {
    title: '地址',
    dataIndex: 'address',
    width: 160,
    ellipsis: true,
  },
  {
    title: '联系人',
    dataIndex: 'contact',
    width: 100,
    ellipsis: true,
  },
  {
    title: '联系人电话',
    dataIndex: 'contactPhone',
    width: 120,
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 130,
    ellipsis: true,
  },
  {
    title: '状态',
    dataIndex: 'disabledFlag',
    width: 60,
  },
  {
    title: '创建人',
    width: 90,
    dataIndex: 'createUserName',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 170
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 100
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
let searchDate = ref();

function dateChange(dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

// 地址
function getAddress(record) {
  let address = "";
  if (record.provinceName) {
    address = address + record.provinceName;
  }
  if (record.cityName) {
    address = address + record.cityName;
  }
  if (record.districtName) {
    address = address + record.districtName;
  }
  if (record.address) {
    address = address + record.address;
  }
  return address;
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
    let responseModel = await customsBrokerApi.pageQuery(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function confirmDelete(customsBrokerId) {
  Modal.confirm({
    title: "确定要删除吗？",
    content: "删除后，该信息将不可恢复",
    okText: "删除",
    okType: "danger",
    onOk() {
      del(customsBrokerId);
    },
    cancelText: "取消",
    onCancel() {
    },
  });
}

async function del(customsBrokerId) {
  try {
    useSpinStore().show();
    await customsBrokerApi.delete(customsBrokerId);
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
