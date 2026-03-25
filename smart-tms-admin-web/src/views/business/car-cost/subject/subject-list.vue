<template>
  <a-form v-privilege="'carCostCategory:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="科目名称">
        <a-input v-model:value="queryForm.keywords" placeholder="科目名称" style="width: 180px"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="科目类型">
        <SmartEnumSelect v-model:value="queryForm.costType" enum-name="CAR_COST_CATEGORY_TYPE_ENUM" placeholder="请选择科目类型"
                         width="100%"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="支付方式类型">
        <SmartEnumSelect v-model:value="queryForm.payMode" enum-name="CAR_COST_PAY_MODE_ENUM" placeholder="请选择支付方式"
                         width="200px"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item">
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

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button v-privilege="'carCostCategory:add'" size="small" type="primary" @click="add()">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建科目
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table :columns="columns" :dataSource="tableData" :pagination="false" bordered rowKey="categoryId" size="small">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'costType'">
          <span>{{ $smartEnumPlugin.getDescByValue('CAR_COST_CATEGORY_TYPE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'payMode'">
          <span>{{ $smartEnumPlugin.getDescByValue('CAR_COST_PAY_MODE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'incomeFlag'">
          <span>{{text?'收入':'支出'}}</span>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button v-privilege="'carCostCategory:edit'" type="link" @click="update(record)">修改</a-button>
            <a-button v-privilege="'carCostCategory:delete'" danger type="link" @click="confirmDelete(record.categoryId)">删除
            </a-button>
          </div>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :defaultPageSize="queryForm.pageSize"
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :show-total="(total) => `共${total}条`"
          :total="total"
          show-less-items
          showQuickJumper
          showSizeChanger
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
      />
    </div>
    <!--新建编辑modal-->
    <CarCostCategoryForm ref="formRef" @reloadList="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import CarCostCategoryForm from './components/category-form.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import { SmartLoading } from '/@/components/smart-loading';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { carCostCategoryApi } from '/@/api/business/waybill/car-cost-category-api';
import useDragTable from '/@/hook/use-drag-table/index';

const { columnsData:columns } = useDragTable([
  {
    title: '科目名称',
    dataIndex: 'categoryName',
  },
  {
    title: '科目类型',
    dataIndex: 'costType',
  },
  {
    title: '支付方式',
    dataIndex: 'payMode',
  },
  {
    title: '收入支出标识',
    dataIndex: 'incomeFlag',
  },
  {
    title: '排序',
    dataIndex: 'sort',
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 90,
  },
]);

const queryFormState = {
  keywords: null,
  costType: null,
  payMode: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
};

const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

onMounted(ajaxQuery);

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await carCostCategoryApi.query(queryForm);
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

function add () {
  formRef.value.showModal();
}

function update (form) {
  formRef.value.showModal(form);
}

function confirmDelete (categoryId) {
  Modal.confirm({
    title: '提示',
    content: '确认要删除吗？',
    okText: '删除',
    okType: 'danger',
    onOk: () => {
      deleteCarCostCategory(categoryId);
    },
  });
}

async function deleteCarCostCategory (categoryId) {
  try {
    SmartLoading.show();
    await carCostCategoryApi.delete(categoryId);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}
</script>
