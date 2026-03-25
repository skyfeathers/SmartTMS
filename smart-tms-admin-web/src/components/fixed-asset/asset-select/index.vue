<!--
  * 固定资产 - 弹窗选择
  *
  * @Author:    lidoudou
  * @Date:      2023-03-18 10:45:14
  * @Copyright  1024创新实验室
-->
<template>
  <a-modal :open="visible" title="选择资产" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose" width="800">
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 150px" v-model:value="queryForm.keywords" placeholder="资产编号/资产名称"/>
        </a-form-item>
        <a-form-item label="所属分类" class="smart-query-form-item">
          <CategoryTreeSelect v-model:value="queryForm.categoryId" placeholder="请选择所属分类"
                              :categoryType="CATEGORY_TYPE_ENUM.FIXED_ASSET.value"/>
        </a-form-item>
<!--        <a-form-item label="存放位置" class="smart-query-form-item">
          <LocationSelect v-model:value="queryForm.locationId" placeholder="请选择存放位置"/>
        </a-form-item>-->
        <a-form-item label="所属部门" class="smart-query-form-item">
          <DepartmentTreeSelect ref="departmentTreeSelect" width="200px" :init="false" placeholder="请选择使用部门"
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
      <!---------- 表格 begin ----------->
      <a-table
          size="small"
          :dataSource="tableData"
          :columns="columns"
          rowKey="assetId"
          bordered
          :loading="tableLoading"
          :pagination="false"
          :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange}"
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'assetName' || column.dataIndex === 'assetNo'">
            <a @click="toDetail(record.assetId)">{{ record[column.dataIndex] }}</a>
          </template>
          <template v-if="column.dataIndex === 'sourceId'">
            {{ record.sourceId[0].valueName }}
          </template>
          <template v-if="column.dataIndex === 'status'">
            {{ $smartEnumPlugin.getDescByValue('ASSET_STATUS_ENUM', record.status) }}
          </template>
          <template v-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button @click="operateAsset(record)" type="link">编辑</a-button>
              <a-button @click="onDelete(record)" danger type="link">删除</a-button>
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
  </a-modal>
</template>
<script setup>
import TableOperator from '/@/components/smart-table-operator/index.vue';
import LocationSelect from '/@/components/fixed-asset/locaton-select/index.vue';
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import CategoryTreeSelect from '/@/components/fixed-asset/category-tree-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';

import { reactive, ref, onMounted, inject ,isRef,isReactive} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { assetApi } from '/@/api/fixed-asset/asset/asset-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category-const';
import { useRouter } from 'vue-router';
import _ from 'lodash';

// emit
const emit = defineEmits(['confirmSelect']);
// ---------------------------- 查询数据表单和方法 ----------------------------
const queryFormState = {
  keywords: '',
  locationId: null,
  departmentId: null,
  categoryId: null,
  pageNum: 1,
  pageSize: 10,
  statusList: []
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

let customQueryForm = inject('setCustomQueryForm');

// 查询数据
async function queryData () {
  tableLoading.value = true;
  try {
    let params = _.cloneDeep(queryForm);
    if (customQueryForm) {
      if (isRef(customQueryForm)) {
        params = Object.assign(params, customQueryForm.value);
      } else {
        params = Object.assign(params, customQueryForm);
      }
    }
    let queryResult = await assetApi.queryPage(params);
    tableData.value = queryResult.data.list;
    total.value = queryResult.data.total;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// ---------------------------- table以及确认选择操作 ----------------------------
const selectedRowKeyList = ref([]);
const selectedRowList = ref([]);

function onSelectChange (selectedRowKeys, selectedRows) {
  // 过滤选中的数据
  let currentAssetIdList = tableData.value.map(e => e.assetId);

  // 组合数据
  let otherPageSelectedList = selectedRowList.value.filter(e => !currentAssetIdList.includes(e.assetId));
  selectedRowList.value = otherPageSelectedList.concat(selectedRows);
  selectedRowKeyList.value = selectedRowList.value.map(e => e.assetId);
}

// ---------------------------- 弹窗操作 ----------------------------
const visible = ref(false);

function showModal (assetList) {
  visible.value = true;
  Object.assign(queryForm, queryFormState);
  selectedRowKeyList.value = assetList.map(e => e.assetId);
  selectedRowList.value = assetList;
  queryData();
}

// 提交
function onSubmit () {
  if (_.isEmpty(selectedRowList.value)) {
    message.error('请选择资产');
    return;
  }
  emit('confirmSelect', selectedRowList.value);
  onClose();
}

// 取消弹窗选择
function onClose () {
  visible.value = false;
}

// ---------------------------- 表格列 ----------------------------
const columns = ref([
  {
    title: '资产名称',
    dataIndex: 'assetName',
  },
  {
    title: '资产编号',
    dataIndex: 'assetNo',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 60
  },
  {
    title: '所属分类',
    dataIndex: 'categoryName',
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
    title: '存放位置',
    dataIndex: 'locationName',
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 80
  },
]);

defineExpose({
  showModal
});
</script>
