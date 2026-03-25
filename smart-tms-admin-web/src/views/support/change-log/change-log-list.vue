<!--
  * 系统更新日志
  *
  * @Author:    卓大
  * @Date:      2022-09-26 14:53:50
  * @Copyright  1024创新实验室
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form v-privilege="'changeLog:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="更新类型">
        <SmartEnumSelect v-model:value="queryForm.type" enumName="CHANGE_LOG_TYPE_ENUM" placeholder="更新类型" width="200px" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="关键字">
        <a-input v-model:value="queryForm.keyword" placeholder="关键字" style="width: 200px" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="发布日期">
        <a-range-picker v-model:value="queryForm.publicDate" :ranges="defaultTimeRanges" style="width: 240px" @change="onChangePublicDate" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-date-picker v-model:value="queryForm.createTime" style="width: 150px" valueFormat="YYYY-MM-DD" />
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
        <a-button v-privilege="'changeLog:add'" size="small" type="primary" @click="showForm">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>
        <a-button v-privilege="'changeLog:batchDelete'" :disabled="selectedRowKeyList.length == 0" size="small" type="primary" danger  @click="confirmBatchDelete">
          <template #icon>
            <DeleteOutlined />
          </template>
          批量删除
        </a-button>
      </div>
      <div class="smart-table-setting-block">
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
        <template v-if="column.dataIndex === 'version'">
          <a-button type="link" @click="showModal(record)">{{text}}</a-button>
        </template>
        <template v-if="column.dataIndex === 'type'">
          <a-tag color="success">
            <template #icon>
              <check-circle-outlined />
            </template>
            {{ $smartEnumPlugin.getDescByValue('CHANGE_LOG_TYPE_ENUM', text) }}
          </a-tag>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button v-privilege="'changeLog:update'" type="link" @click="showForm(record)">编辑</a-button>
            <a-button v-privilege="'changeLog:delete'" danger type="link" @click="onDelete(record)">删除</a-button>
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

    <ChangeLogForm ref="formRef" @reloadList="queryData" />

    <ChangeLogModal ref="modalRef" />
  </a-card>
</template>
<script setup>
  import { reactive, ref, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/smart-loading';
  import { changeLogApi } from '/@/api/support/change-log/change-log-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import TableOperator from '/@/components/smart-table-operator/index.vue';
  import DictSelect from '/@/components/dict-select/index.vue';
  import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import ChangeLogModal from './change-log-modal.vue';
  import ChangeLogForm from './change-log-form.vue';
  // ---------------------------- 表格列 ----------------------------

  const columns = ref([
    {
      title: '版本',
      dataIndex: 'version',
      ellipsis: true,
    },
    {
      title: '更新类型',
      dataIndex: 'type',
      ellipsis: true,
    },
    {
      title: '发布人',
      dataIndex: 'publishAuthor',
      ellipsis: true,
    },
    {
      title: '发布日期',
      dataIndex: 'publicDate',
      ellipsis: true,
    },
    {
      title: '更新内容',
      dataIndex: 'content',
      ellipsis: true,
    },
    {
      title: '跳转链接',
      dataIndex: 'link',
      ellipsis: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      ellipsis: true,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
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
    type: undefined, //更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]
    keyword: undefined, //关键字
    publicDate: [], //发布日期
    publicDateBegin: undefined, //发布日期 开始
    publicDateEnd: undefined, //发布日期 结束
    createTime: undefined, //创建时间
    link: undefined, //跳转链接
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
      let queryResult = await changeLogApi.queryPage(queryForm);
      tableData.value = queryResult.data.list;
      total.value = queryResult.data.total;
    } catch (e) {
      console.log(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function onChangePublicDate(dates, dateStrings) {
    queryForm.publicDateBegin = dateStrings[0];
    queryForm.publicDateEnd = dateStrings[1];
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
      let deleteForm = {
        goodsIdList: selectedRowKeyList.value,
      };
      await changeLogApi.delete(data.changeLogId);
      message.success('删除成功');
      queryData();
    } catch (e) {
      console.log(e);
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
      await changeLogApi.batchDelete(selectedRowKeyList.value);
      message.success('删除成功');
      queryData();
    } catch (e) {
      console.log(e);
    } finally {
      SmartLoading.hide();
    }
  }
</script>
