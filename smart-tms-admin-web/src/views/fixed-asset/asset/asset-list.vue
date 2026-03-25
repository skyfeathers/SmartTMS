<!--
  * 固定资产
  *
  * @Author:    lidoudou
  * @Date:      2023-03-15 14:15:14
  * @Copyright  1024创新实验室
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form" v-privilege="'asset:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 150px" v-model:value="queryForm.keywords" placeholder="资产编号/资产名称"/>
      </a-form-item>
      <a-form-item label="状态" class="smart-query-form-item">
        <SmartEnumSelect enumName="ASSET_STATUS_ENUM" v-model:value="queryForm.status" placeholder="请选择状态"/>
      </a-form-item>
      <a-form-item label="所属分类" class="smart-query-form-item">
        <CategoryTreeSelect v-model:value="queryForm.categoryId" placeholder="请选择所属分类"
                            :categoryType="CATEGORY_TYPE_ENUM.FIXED_ASSET.value"/>
      </a-form-item>
      <a-form-item label="存放位置" class="smart-query-form-item">
        <LocationSelect v-model:value="queryForm.locationId" placeholder="请选择存放位置"/>
      </a-form-item>
      <a-form-item label="所属部门" class="smart-query-form-item">
        <DepartmentTreeSelect ref="departmentTreeSelect" width="100%" :init="false" placeholder="请选择使用部门"
                              v-model:value="queryForm.departmentId"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-button type="primary" @click="queryData">
          <template #icon>
            <ReloadOutlined/>
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery" class="smart-margin-left10">
          <template #icon>
            <SearchOutlined/>
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>
  <!---------- 查询表单form end ----------->

  <a-card size="small" :bordered="false" :hoverable="true">
    <!---------- 表格操作行 begin ----------->
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="operateAsset" v-privilege="'asset:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.ASSET_LIST" :refresh="queryData"/>
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table
        :scroll="{x:2400}"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="assetId"
        bordered
        :loading="tableLoading"
        :pagination="false"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'assetName' || column.dataIndex === 'assetNo'">
          <a @click="toDetail(record.assetId)">{{record[column.dataIndex]}}</a>
        </template>
        <template v-if="column.dataIndex === 'sourceId'">
          {{record.sourceId[0].valueName}}
        </template>
        <template v-if="column.dataIndex === 'status'">
          {{ $smartEnumPlugin.getDescByValue('ASSET_STATUS_ENUM', record.status) }}
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="operateAsset(record)" v-privilege="'asset:edit'" type="link">编辑</a-button>
            <a-button @click="onDelete(record)" v-privilege="'asset:delete'" danger type="link">删除</a-button>
          </div>
        </template>
      </template>
    </a-table>
    <!---------- 表格 end ----------->

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
          @change="queryData"
          @showSizeChange="queryData"
          :show-total="(total) => `共${total}条`"
      />
    </div>


  </a-card>
</template>
<script setup>
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import LocationSelect from '/@/components/fixed-asset/locaton-select/index.vue';
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import CategoryTreeSelect from '/@/components/fixed-asset/category-tree-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { assetApi } from '/@/api/fixed-asset/asset/asset-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category-const';
import { useRouter } from 'vue-router';
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '资产编号',
    dataIndex: 'assetNo',
  },
  {
    title: '所属分类',
    dataIndex: 'categoryName',
  },
  {
    title: '资产名称',
    dataIndex: 'assetName',
  },
  {
    title: '资产来源',
    dataIndex: 'sourceId',
    width: 90
  },
  {
    title: '计量单位',
    dataIndex: 'unit',
  },
  {
    title: '品牌',
    dataIndex: 'brand',
  },
  {
    title: '规格型号',
    dataIndex: 'model',
  },
  {
    title: '设备序列号',
    dataIndex: 'serialId',
  },
  {
    title: '存放位置',
    dataIndex: 'locationName',
  },
  {
    title: '供应商',
    dataIndex: 'supplierName',
  },
  {
    title: '预计使用期限(月)',
    dataIndex: 'monthCount',
    width: 130
  },
  {
    title: '使用人',
    dataIndex: 'userName',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 80
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
    width: 90,
  },
]);

// ---------------------------- 查询数据表单和方法 ----------------------------

const queryFormState = {
  keywords: '',
  status: null,
  locationId: null,
  departmentId: null,
  categoryId: null,
  pageNum: 1,
  pageSize: 10,
};
// 查询表单form
const queryForm = reactive({ ...queryFormState });
// 表格加载loading
const tableLoading = ref(false);
// 表格数据
const tableData = ref([]);
// 总数
const total = ref(0);

// 重置查询条件
function resetQuery () {
  let pageSize = queryForm.pageSize;
  Object.assign(queryForm, queryFormState);
  queryForm.pageSize = pageSize;
  queryData();
}

// 查询数据
async function queryData () {
  tableLoading.value = true;
  try {
    let queryResult = await assetApi.queryPage(queryForm);
    tableData.value = queryResult.data.list;
    total.value = queryResult.data.total;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

onMounted(queryData);

let router = useRouter();
// ---------------------------- 跳转 ----------------------------
function operateAsset ({ assetId }) {
  router.push({
    path: '/fixed-asset/asset-operate',
    query:{
      assetId
    }
  });
}

function toDetail (assetId) {
  router.push({
    path: '/fixed-asset/asset-detail',
    query: {
      assetId
    }
  });
}

// ---------------------------- 单个删除 ----------------------------
//确认删除
function onDelete (data) {
  Modal.confirm({
    title: '提示',
    content: '确定要删除吗?',
    okText: '删除',
    okType: 'danger',
    onOk () {
      requestDelete(data);
    },
    cancelText: '取消',
    onCancel () {},
  });
}

//请求删除
async function requestDelete (data) {
  SmartLoading.show();
  try {
    await assetApi.delete(data.assetId);
    message.success('删除成功');
    queryData();
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}



</script>
