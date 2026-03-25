<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="addCategory()" type="primary" size="small">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
      :scroll="{ x: 1000 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="categoryId"
      :pagination="false"
      @expandedRowsChange="changeExand"
      :expanded-row-keys="expandedRowKeys"
      bordered
    >
      <template #disabledFlag="{record}">
        {{ record.disabledFlag ? '禁用' : '启用' }}
      </template>
      <template #action="{ record }">
        <a-button @click="addCategory(record.categoryId)" type="link"
          >增加子分类</a-button
        >
        <a-button @click="addCategory(undefined, record)" type="link">编辑</a-button>
        <a-button @click="confirmDeleteCategory(record.categoryId)" type="link"
          >删除</a-button
        >
      </template>
    </a-table>
    <CategoryOperateModal ref="operateModal" @reloadList="reloadList" />
  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted, computed } from "vue";
import { Modal, message } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import CategoryOperateModal from "./category-operate-modal.vue";
import { categoryApi } from "/@/api/business/category/category-api";
import { CATEGORY_TYPE_ENUM } from "/@/constants/business/category-const";

// ----------------------- 以下是计算属性 watch监听 ------------------------

// ----------------------- 定义的props ------------------------
const props = defineProps({
  // 分组类型
  categoryType: Number,
});

const columns = reactive([
  {
    title: '分类名称',
    dataIndex: "categoryName",
  },
  {
    title: '顺序',
    dataIndex: "sort",
  },
  {
    title: '状态',
    dataIndex: "disabledFlag",
    slots: { customRender: 'disabledFlag' },
  },
  {
    title: "操作",
    dataIndex: "action",
    slots: { customRender: "action" },
  },
]);
const operateModal = ref();
const tableLoading = ref(false);
const tableData = ref([]);

const expandedRowKeys = ref([]);
// ----------------------- 以下是方法 ------------------------
async function queryList() {
  try {
    tableLoading.value = true;
    let queryForm = {
      categoryType: props.categoryType,
    };
    let responseModel = await categoryApi.queryCategoryTree(queryForm);
    const list = responseModel.data;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}
function reloadList(parentId) {
  queryList();
  if (parentId) {
    expandedRowKeys.value.push(parentId);
  }
}

function changeExand(val) {
  expandedRowKeys.value = val;
}

function addCategory(parentId, rowData) {
  operateModal.value.showModal(props.categoryType, parentId, rowData);
}
function confirmDeleteCategory(categoryId) {
  Modal.confirm({
    title: "提示",
    content: "确定要删除当前分类吗?",
    okText: "确定",
    okType: "danger",
    async onOk() {
      deleteCategory(categoryId);
    },
    cancelText: "取消",
    onCancel() {},
  });
}
async function deleteCategory(categoryId) {
  try {
    useSpinStore().show();
    await categoryApi.deleteCategoryById(categoryId);
    message.success("删除成功");
    queryList();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}
onMounted(queryList);
// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  queryList,
});
</script>
