<template>
  <a-form class="smart-query-form" v-privilege="'goods:edit'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value.trim="queryForm.keywords" placeholder="货物名称"/>
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
        <a-button @click="addOrUpdate()" v-privilege="'goods:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建货物
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="goodsId"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'goodsType'">
          {{ record.goodsTypeName}}
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addOrUpdate(record)" v-privilege="'goods:edit'" type="link">编辑</a-button>
            <a-button @click="confirmDelete(record.goodsId)" v-privilege="'goods:delete'" type="link">删除</a-button>
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
    <GoodsOperateModal ref="operateModal" @reloadList="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import GoodsOperateModal from './components/goods-operate-modal.vue';
import {reactive, ref, onMounted} from 'vue';
import {message, Modal} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {goodsApi} from '/@/api/business/business-material/goods-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';

const columns = reactive([
  {
    title: '货物名称',
    dataIndex: 'goodsName',
    width: 50
  },
  {
    title: '货物类型',
    dataIndex: 'goodsType',
    width:50,
  },
  {
    title: '创建人',
    width: 60,
    dataIndex: 'createUserName',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 60
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 50
  },
]);

const queryFormState = {
  keywords: '',
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true,
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const operateModal = ref();


function resetQuery() {
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

    let responseModel = await goodsApi.pageQuery(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function confirmDelete(goodsId) {
  Modal.confirm({
    title: "确定要删除吗？",
    content: "删除后，该信息将不可恢复",
    okText: "删除",
    okType: "danger",
    onOk() {
      del(goodsId);
    },
    cancelText: "取消",
    onCancel() {
    },
  });
}

async function del(goodsId) {
  try {
    useSpinStore().show();
    await goodsApi.delete(goodsId);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

function addOrUpdate(rowData) {
  operateModal.value.showModal(rowData);
}

onMounted(ajaxQuery);
</script>
