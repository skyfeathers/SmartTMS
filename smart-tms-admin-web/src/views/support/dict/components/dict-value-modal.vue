<template>
  <a-drawer :width="800" :open="visible" :body-style="{ paddingBottom: '80px' }" title="字典值" @close="onClose">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 300px" v-model:value="queryForm.searchWord" placeholder="关键字" />
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

    <a-card size="small" :bordered="false">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button @click="addOrUpdateValue" type="primary" size="small">
            <template #icon>
              <PlusOutlined />
            </template>
            新建
          </a-button>

          <a-button @click="confirmBatchDelete" type="primary" danger  size="small" :disabled="selectedRowKeyList.length == 0">
            <template #icon>
              <DeleteOutlined />
            </template>
            批量删除
          </a-button>
        </div>
        <div class="smart-table-setting-block"></div>
      </a-row>

      <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="dictValueId"
        :pagination="false"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
        bordered
      >
        <template #bodyCell="{ text, record, index, column }">
          <template v-if="column.dataIndex === 'action'">
            <a-button @click="addOrUpdateValue(record)" type="link">编辑</a-button>
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
    </a-card>
    <DictValueOperateModal ref="operateModal" @reloadList="ajaxQuery" />
  </a-drawer>
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import DictValueOperateModal from './dict-value-operate-modal.vue';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { dictApi } from '/@/api/support/dict/dict-api';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { Modal } from 'ant-design-vue';
  import { message } from 'ant-design-vue';

  // 是否展示抽屉
  const visible = ref(false);
  const dictKeyId = ref(undefined);
  // ----------------------- 以下是生命周期 ------------------------

  // ----------------------- 以下是方法 ------------------------
  function showModal(keyId) {
    dictKeyId.value = keyId;
    visible.value = true;
    ajaxQuery();
  }

  function onClose() {
    visible.value = false;
    dictKeyId.value = undefined;
  }

  const columns = reactive([
    {
      title: 'ID',
      width: 80,
      dataIndex: 'dictValueId',
    },
    {
      title: '编码',
      dataIndex: 'valueCode',
    },
    {
      title: '名称',
      dataIndex: 'valueName',
    },
    {
      title: '排序',
      width: 80,
      dataIndex: 'sort',
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
    dictKeyId: undefined,
    searchWord: '',
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive({ ...queryFormState });
  const selectedRowKeyList = ref([]);
  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

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
      queryForm.dictKeyId = dictKeyId.value;
      let responseModel = await dictApi.valueQuery(queryForm);
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
      title: '提示',
      content: '确定要删除选中值吗?',
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
      await dictApi.valueDelete(selectedRowKeyList.value);
      message.success('删除成功');
      ajaxQuery();
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  };

  const operateModal = ref();
  function addOrUpdateValue(rowData) {
    operateModal.value.showModal(rowData, dictKeyId.value);
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showModal,
  });
</script>
