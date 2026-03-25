<template>
  <a-card :bordered="false" :hoverable="true" size="small">
  <a-tabs>
    <a-tab-pane key="app" tab="APP包管理">
      <!---------- 查询表单form begin ----------->
      <a-form v-privilege="'mobileApp:query'" class="smart-query-form">
        <a-row class="smart-query-form-row">
          <a-form-item class="smart-query-form-item" label="关键字">
            <a-input v-model:value="queryForm.keywords" placeholder="关键字" style="width: 200px" />
          </a-form-item>
          <a-form-item class="smart-query-form-item">
            <a-button type="primary" @click="queryData">
              <template #icon>
                <ReloadOutlined />
              </template>
              查询
            </a-button>
            <a-button class="smart-margin-left10" @click="resetQuery">
              <template #icon>
                <SearchOutlined />
              </template>
              重置
            </a-button>
          </a-form-item>
        </a-row>
      </a-form>
      <!---------- 查询表单form end ----------->

      <a-card :bordered="false" :hoverable="true" size="small">
        <!---------- 表格操作行 begin ----------->
        <a-row class="smart-table-btn-block">
          <div class="smart-table-operate-block">
            <a-button v-privilege="'mobileApp:add'" size="small" type="primary" @click="showForm">
              <template #icon>
                <PlusOutlined />
              </template>
              新建
            </a-button>
            <a-button v-privilege="'mobileApp:batchDelete'" :disabled="selectedRowKeyList.length == 0" size="small" type="primary" danger  @click="confirmBatchDelete">
              <template #icon>
                <DeleteOutlined />
              </template>
              批量删除
            </a-button>
          </div>
          <div class="smart-table-setting-block">
<!--            <SmartTableOperator v-model="columns" :tableId="null" :refresh="queryData" />-->
          </div>
        </a-row>
        <!---------- 表格操作行 end ----------->

        <!---------- 表格 begin ----------->
        <a-table
          :columns="columns"
          :dataSource="tableData"
          :pagination="false"
          :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
          bordered
          rowKey="changeLogId"
          size="small"
        >
          <template #bodyCell="{ text, record, column }">
            <template v-if="column.dataIndex === 'platformType'">
              {{ $smartEnumPlugin.getDescByValue('MOBILE_APP_PLATFORM_TYPE_ENUM', text) }}
            </template>
            <template v-if="column.dataIndex === 'forceUpdateFlag'">
              <a-tag :color="text ? 'error' : 'processing'">{{ text ? '是' : '否' }}</a-tag>
            </template>
            <template v-if="column.dataIndex === 'newestFlag'">
              <a-tag :color="text ? 'error' : 'processing'">{{ text ? '最新' : '过时' }}</a-tag>
            </template>
            <template v-if="column.dataIndex === 'appFile'">
              <a-button type="link" @click="showModal(record)">查看</a-button>
            </template>
            <template v-if="column.dataIndex === 'action'">
              <div class="smart-table-operate">
                <a-button v-privilege="'mobileApp:update'" type="link" @click="showForm(record)">编辑</a-button>
                <a-button v-privilege="'mobileApp:delete'" danger type="link" @click="onDelete(record)">删除</a-button>
              </div>
            </template>
          </template>
        </a-table>
        <!---------- 表格 end ----------->

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
            @change="queryData"
            @showSizeChange="queryData"
          />
        </div>

        <MobileAppForm ref="formRef" @reloadList="queryData" />

        <MobileAppModal ref="modalRef" />
      </a-card>
    </a-tab-pane>
    <a-tab-pane key="privacy_agreement" tab="隐私协议">
      <MobileContentForm config-key="privacy_agreement" />
    </a-tab-pane>
    <a-tab-pane key="user_agreement" tab="隐私协议">
      <MobileContentForm config-key="user_agreement" />
    </a-tab-pane>
  </a-tabs>
  </a-card>

</template>
<script setup>
  import { reactive, ref, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/smart-loading';
  import { mobileAppApi } from '/@/api/support/mobile-app/mobile-app-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
  import MobileAppForm from './mobile-app-form.vue';
  import MobileContentForm from './mobile-content-form.vue';
  import MobileAppModal from './mobile-app-modal.vue';
  // ---------------------------- 表格列 ----------------------------

  const columns = ref([
    {
      title: '平台',
      dataIndex: 'platformType',
      ellipsis: true,
    },
    {
      title: '版本号',
      dataIndex: 'versionNo',
      ellipsis: true,
    },
    {
      title: '更新描述',
      dataIndex: 'updateDesc',
      ellipsis: true,
    },
    {
      title: '强制更新',
      dataIndex: 'forceUpdateFlag',
      ellipsis: true,
    },
    {
      title: '最新版',
      dataIndex: 'newestFlag',
      ellipsis: true,
    },
    {
      title: '包文件',
      dataIndex: 'appFile',
      ellipsis: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      ellipsis: true,
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
    keywords: undefined, //关键字
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
  function resetQuery() {
    let pageSize = queryForm.pageSize;
    Object.assign(queryForm, queryFormState);
    queryForm.pageSize = pageSize;
    queryData();
  }

  // 查询数据
  async function queryData() {
    tableLoading.value = true;
    try {
      let queryResult = await mobileAppApi.queryPage(queryForm);
      tableData.value = queryResult.data.list;
      total.value = queryResult.data.total;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  onMounted(queryData);

  // ---------------------------- 查看 ----------------------------
  const modalRef = ref();

  function showModal(data) {
    modalRef.value.show(data);
  }

  // ---------------------------- 添加/修改 ----------------------------
  const formRef = ref();

  function showForm(data) {
    formRef.value.show(data);
  }

  // ---------------------------- 单个删除 ----------------------------
  //确认删除
  function onDelete(data) {
    Modal.confirm({
      title: '提示',
      content: '确定要删除选吗?',
      okText: '删除',
      okType: 'danger',
      onOk() {
        requestDelete(data);
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  //请求删除
  async function requestDelete(data) {
    SmartLoading.show();
    try {
      await mobileAppApi.delete(data.id);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  // ---------------------------- 批量删除 ----------------------------

  // 选择表格行
  const selectedRowKeyList = ref([]);

  function onSelectChange(selectedRowKeys) {
    selectedRowKeyList.value = selectedRowKeys;
  }

  // 批量删除
  function confirmBatchDelete() {
    Modal.confirm({
      title: '提示',
      content: '确定要批量删除这些数据吗?',
      okText: '删除',
      okType: 'danger',
      onOk() {
        requestBatchDelete();
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  //请求批量删除
  async function requestBatchDelete() {
    try {
      SmartLoading.show();
      await mobileAppApi.batchDelete(selectedRowKeyList.value);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }
</script>
