<template>
  <a-form class="smart-query-form" v-privilege="'businessCompany:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 350px" v-model:value="queryForm.keywords" placeholder="公司名称/公司编号/联系人/联系人电话/创建人"/>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange"/>
        </a-space>
      </a-form-item>

      <a-form-item label="类型状态" class="smart-query-form-item">
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
        <a-button @click="addOrUpdate()" type="primary" size="small" v-privilege="'businessCompany:add'">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建船公司
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :scroll="{ x: 1300 }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="companyId"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'disabledFlag'">
          <a-tag v-show="record.disabledFlag" color="error">禁用</a-tag>
          <a-tag v-show="!record.disabledFlag" color="success">启用</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addOrUpdate(record)" v-privilege="'businessCompany:edit'" type="link">编辑</a-button>
            <a-button @click="confirmDelete(record.companyId)" v-privilege="'businessCompany:delete'" type="link">删除
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
    <BusinessCompanyOperateModal ref="operateModal" @reloadList="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import BusinessCompanyOperateModal from './components/business-company-operate-modal.vue';
import {reactive, ref, onMounted} from 'vue';
import {message, Modal} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {businessCompanyApi} from '/@/api/business/business-material/business-company-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';

const columns = reactive([
  {
    title: 'ID',
    width: 50,
    dataIndex: 'companyId',
  },
  {
    title: '公司编号',
    dataIndex: 'companyCode',
  },
  {
    title: '公司名称',
    dataIndex: 'companyName',
  },
  {
    title: '联系人',
    width: 100,
    dataIndex: 'contact',
  },
  {
    title: '联系人电话',
    width: 120,
    dataIndex: 'contactPhone',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
  {
    title: '类型状态',
    width: 100,
    dataIndex: 'disabledFlag',
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
    let responseModel = await businessCompanyApi.pageQuery(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function confirmDelete(companyId) {
  Modal.confirm({
    title: "确定要删除吗？",
    content: "删除后，该信息将不可恢复",
    okText: "删除",
    okType: "danger",
    onOk() {
      del(companyId);
    },
    cancelText: "取消",
    onCancel() {
    },
  });
}

async function del(companyId) {
  try {
    useSpinStore().show();
    await businessCompanyApi.delete(companyId);
    message.success('删除成功');
    search();
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
