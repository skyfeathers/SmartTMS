<template>
  <a-form class="smart-query-form" v-privilege="'system:dict:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.searchWord" placeholder="关键字" />
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
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
        <a-button @click="addOrUpdateKey" v-privilege="'system:dict:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>

        <a-button @click="confirmBatchDelete" v-privilege="'system:dict:batch:delete'" type="primary" danger  size="small" :disabled="selectedRowKeyList.length == 0">
          <template #icon>
            <DeleteOutlined />
          </template>
          批量删除
        </a-button>

        <a-button @click="cacheRefresh" v-privilege="'system:dict:cache:refresh'" type="primary" size="small">
          <template #icon>
            <cloud-sync-outlined />
          </template>
          缓存刷新
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :columns="columns"
        :dataSource="tableData"
        :pagination="false"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
        :scroll="{ x: 1300 }"
        bordered
        rowKey="dictKeyId"
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'keyCode'">
          <a @click="showValueList(record.dictKeyId)">{{ record.keyCode }}</a>
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <a-button @click="addOrUpdateKey(record)" v-privilege="'system:dict:edit'" type="link">编辑</a-button>
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

    <DictKeyOperateModal ref="operateModal" @reloadList="ajaxQuery" />
    <!-- 值列表 -->
    <DictValueModal ref="dictValueModal" />
  </a-card>
</template>
<script setup>
  import DictKeyOperateModal from './components/dict-key-operate-modal.vue';
  import DictValueModal from './components/dict-value-modal.vue';
  import { reactive, ref, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { dictApi } from '/@/api/support/dict/dict-api';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';

  const columns = reactive([
    {
      title: 'ID',
      width: 90,
      dataIndex: 'dictKeyId',
    },
    {
      title: '编码',
      dataIndex: 'keyCode',
    },
    {
      title: '名称',
      dataIndex: 'keyName',
    },
    {
      title: '备注',
      dataIndex: 'remark',
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
    },
  ]);

  const queryFormState = {
    searchWord: '',
    pageNum: 1,
    pageSize: PAGE_SIZE,
  };
  const queryForm = reactive({ ...queryFormState });
  const tableLoading = ref(false);
  const selectedRowKeyList = ref([]);
  const tableData = ref([]);
  const total = ref(0);
  const operateModal = ref();
  const dictValueModal = ref();

  // 显示操作记录弹窗
  function showValueList(dictKeyId) {
    dictValueModal.value.showModal(dictKeyId);
  }

  function onSelectChange(selectedRowKeys) {
    selectedRowKeyList.value = selectedRowKeys;
  }

  function resetQuery() {
    Object.assign(queryForm, queryFormState);
    ajaxQuery();
  }

  function search () {
    queryForm.pageNum = 1;
    ajaxQuery();
  }

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await dictApi.keyQuery(queryForm);
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      console.log(e);
    } finally {
      tableLoading.value = false;
    }
  }

  async function cacheRefresh() {
    try {
      useSpinStore().show();
      await dictApi.cacheRefresh();
      message.success('缓存刷新成功');
      ajaxQuery();
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  }
  function confirmBatchDelete() {
    Modal.confirm({
      title: '提示',
      content: '确定要删除选中Key吗?',
      okText: '删除',
      okType: 'danger',
      onOk() {
        batchDelete();
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  const batchDelete = async () => {
    try {
      useSpinStore().show();
      await dictApi.keyDelete(selectedRowKeyList.value);
      message.success('删除成功');
      ajaxQuery();
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  };

  function addOrUpdateKey(rowData) {
    operateModal.value.showModal(rowData);
  }

  onMounted(ajaxQuery);
</script>
