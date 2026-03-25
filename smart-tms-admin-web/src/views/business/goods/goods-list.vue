<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="商品名称" class="smart-query-form-item">
        <a-input
          style="width: 300px"
          v-model:value="queryForm.searchWord"
          placeholder="商品名称"
        />
      </a-form-item>

      <a-form-item label="商品分类" class="smart-query-form-item">
        <category-tree
          v-model:value="queryForm.categoryId"
          placeholder="请选择商品分类"
          :categoryType="CATEGORY_TYPE_ENUM.GOODS.value"
        />
      </a-form-item>

      <a-form-item label="快速筛选" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.shelvesFlag" @change="ajaxQuery">
          <a-radio-button :value="undefined">全部</a-radio-button>
          <a-radio-button :value="true">上架</a-radio-button>
          <a-radio-button :value="false">下架</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="ajaxQuery">
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
        <a-button @click="addGoods" type="primary" size="small">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>

        <!-- <a-button type="primary" size="small">
          <template #icon>
            <ExportOutlined />
          </template>
          批量导出
        </a-button> -->

        <a-button
          @click="confirmBatchDelete"
          type="primary" danger 
          size="small"
          :disabled="selectedRowKeyList.length == 0"
        >
          <template #icon>
            <DeleteOutlined />
          </template>
          批量删除
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
      :scroll="{ x: 1300 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="goodsId"
      :pagination="false"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
      bordered
    >
      <template #goodsType="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue("GOODS_TYPE_ENUM", text) }}</span>
      </template>

      <template #shelvesFlag="{ text }">
        <span>{{ text ? "上架" : "下架" }}</span>
      </template>
      <template #action="{ record }">
        <a-button @click="addGoods(record)" type="link">编辑</a-button>
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

    <GoodsOperateModal ref="operateModal" @reloadList="ajaxQuery" />
  </a-card>
</template>
<script setup>
import SmartEnumSelect from "/@/components/smart-enum-select/index.vue";
import GoodsOperateModal from "./components/goods-operate-modal.vue";
import { reactive, ref, onMounted } from "vue";
import { message, Modal } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import { goodsApi } from "/@/api/business/goods/goods-api";
import { PAGE_SIZE_OPTIONS } from "/@/constants/common-const";
import CategoryTree from "/@/components/category-tree-select/index.vue";
import { CATEGORY_TYPE_ENUM } from "/@/constants/business/category-const";

const columns = reactive([
  {
    title: "商品分类",
    dataIndex: "categoryName",
  },
  {
    title: "商品类型",
    dataIndex: "goodsType",
    slots: { customRender: "goodsType" },
  },
  {
    title: "商品名称",
    dataIndex: "goodsName",
  },
  {
    title: "商品价格",
    dataIndex: "price",
  },
  {
    title: "商品状态",
    dataIndex: "shelvesFlag",
    slots: { customRender: "shelvesFlag" },
  },
  {
    title: "备注",
    dataIndex: "remark",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "操作",
    dataIndex: "action",
    fixed: "right",
    slots: { customRender: "action" },
  },
]);

const queryFormState = {
  searchWord: "",
  categoryId: undefined,
  shelvesFlag: undefined,
  goodsType: undefined,
  pageNum: 1,
  pageSize: 10,
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref < Boolean > false;
const selectedRowKeyList = ref([]);
const tableData = ref([]);
const total = ref(0);
const operateModal = ref();

function onSelectChange(selectedRowKeys) {
  selectedRowKeyList.value = selectedRowKeys;
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await goodsApi.queryGoodsList(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function confirmBatchDelete() {
  Modal.confirm({
    title: "提示",
    content: "确定要删除选中商品吗?",
    okText: "删除",
    okType: "danger",
    onOk() {
      batchDelete();
    },
    cancelText: "取消",
    onCancel() {},
  });
}

const batchDelete = async () => {
  try {
    useSpinStore().show();
    let deleteForm = {
      goodsIdList: selectedRowKeyList.value,
    };
    await goodsApi.deleteGoods(deleteForm);
    message.success("删除成功");
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
};

function addGoods(goodsData) {
  operateModal.value.showDrawer(goodsData);
}

onMounted(ajaxQuery);
</script>
