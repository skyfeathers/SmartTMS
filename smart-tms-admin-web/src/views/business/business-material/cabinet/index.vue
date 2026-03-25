<template>
  <a-form class="smart-query-form" v-privilege="'cabinet:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="柜型名称/创建人"/>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange"/>
        </a-space>
      </a-form-item>

      <a-form-item label="柜型状态" class="smart-query-form-item">
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
        <a-button @click="addOrUpdate()" v-privilege="'cabinet:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建柜型
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :scroll="{ x: 1300,y:500 }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="cabinetId"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'defaultFlag'">
          {{ record.defaultFlag ? '是' : '否' }}
        </template>
        <template v-else-if="column.dataIndex === 'disabledFlag'">
          <a-tag v-show="record.disabledFlag" color="error">禁用</a-tag>
          <a-tag v-show="!record.disabledFlag" color="success">启用</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addOrUpdate(record)" v-privilege="'cabinet:edit'" type="link">编辑</a-button>
            <a-button @click="confirmDelete(record.cabinetId)" v-privilege="'cabinet:delete'" type="link">删除</a-button>
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
    <CabinetOperateModal ref="operateModal" @reloadList="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import CabinetOperateModal from './components/cabinet-operate-modal.vue';
import {reactive, ref, onMounted} from 'vue';
import {message, Modal} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {cabinetApi} from '/@/api/business/business-material/cabinet-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';

const columns = reactive([
  {
    title: 'ID',
    width: 50,
    dataIndex: 'cabinetId',
  },
  {
    title: '柜型名称',
    dataIndex: 'cabinetName',
  },
  {
    title: '柜型皮重（千克）',
    dataIndex: 'tare',
  },
  {
    title: '默认箱型',
    width: 100,
    dataIndex: 'defaultFlag',
  },
  {
    title: '柜型状态',
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
    width: 160
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
    let responseModel = await cabinetApi.pageQuery(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// ------------------------- 列表操作 -------------------------
function confirmDelete(cabinetId) {
  Modal.confirm({
    title: "确定要删除吗？",
    content: "删除后，该信息将不可恢复",
    okText: "删除",
    okType: "danger",
    onOk() {
      del(cabinetId);
    },
    cancelText: "取消",
    onCancel() {
    },
  });
}

async function del(cabinetId) {
  try {
    useSpinStore().show();
    await cabinetApi.delete(cabinetId);
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
