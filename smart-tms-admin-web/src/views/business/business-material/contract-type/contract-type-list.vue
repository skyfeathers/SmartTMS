<!--
 * @Description: 费用项目管理列表
 * @version:
 * @Author: zhuoda
 * @Date: 2022-07-07 15:41:39
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-23
-->
<template>
  <a-form class="smart-query-form" v-privilege="'contractTypeId:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="类型名称" class="smart-query-form-item">
        <a-input style="width: 180px" v-model:value="queryForm.keywords" placeholder="类型名称" />
      </a-form-item>

      <a-form-item class="smart-query-form-item">
        <a-button type="primary" @click="search">
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

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="add()" type="primary" size="small">
          <template #icon>
            <PlusOutlined />
          </template>
          新建合同类型
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table :columns="columns" :scroll="{ y:500}" :dataSource="tableData" :pagination="false" bordered rowKey="contractTypeId" size="small">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="update(record)" v-privilege="'contractType:edit'" type="link">修改</a-button>
            <a-button @click="confirmDelete(record.contractTypeId)" v-privilege="'contractType:delete'" danger type="link">删除</a-button>
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
    <ContractTypeForm ref="formRef" @reloadList="ajaxQuery" />
  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { contractTypeApi } from '/@/api/business/business-material/contract-type-api';
import ContractTypeForm from './components/contract-type-form.vue';
import { SmartLoading } from '/@/components/smart-loading';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';

const columns = reactive([
  {
    title: '费用名称',
    dataIndex: 'name',
  },
  {
    title: '排序',
    dataIndex: 'seq',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 90,
  },
]);

const queryFormState = {
  keywords: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true,
};

const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

onMounted(ajaxQuery);
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await contractTypeApi.query(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// ----------- 新建/更新/删除-----------
const formRef = ref();
function add() {
  formRef.value.showModal();
}

function update(form) {
  formRef.value.showModal(form);
}

function confirmDelete(contractTypeId) {
  Modal.confirm({
    title: '提示',
    content: '确认要删除吗？',
    okText: '删除',
    okType: 'danger',
    onOk: () => {
      deleteContractType(contractTypeId);
    },
  });
}

async function deleteContractType(contractTypeId) {
  try {
    SmartLoading.show();
    await contractTypeApi.delete(contractTypeId);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}
</script>
