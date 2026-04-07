<template>
  <a-form class="smart-query-form" v-privilege="'pic:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value.trim="queryForm.keywords" placeholder="标题"/>
      </a-form-item>

      <a-form-item label="投放对象" class="smart-query-form-item">
        <SmartEnumSelect v-model:value="queryForm.type" enumName="PIC_TYPE_ENUM" placeholder="投放对象"
                         width="200px"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
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

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="add()" v-privilege="'pic:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :scroll="{ x: '100%' }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="enterpriseId"
        :pagination="false"
        :loading="tableLoading"
        bordered
    >
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'imgKey'">
        <file-preview v-if="!$lodash.isEmpty(text)" :fileList="text" type="picture"/>
      </template>
      <template v-if="column.dataIndex === 'type'">
        {{ $smartEnumPlugin.getDescByValue('PIC_TYPE_ENUM', text) }}
      </template>
      <template v-if="column.dataIndex === 'startTime'">
        {{ record.startTime }}-{{ record.endTime }}
      </template>
      <template v-if="column.dataIndex === 'enableFlag'">
        {{  text?'启用':'禁用' }}
      </template>
      <template v-if="column.dataIndex === 'action'">
        <a-button @click="update(record)" type="link" v-privilege="'pic:edit'">编辑</a-button>
        <a-button @click="changeEnableFlag(record)" type="link" v-privilege="'pic:enable'">{{record.enableFlag?'禁用':'启用'}}</a-button>
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
    <PicOperateModal ref="operateModal" @reloadList="ajaxQuery"/>

  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { picApi } from '/@/api/business/pic/pic-api.js';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRouter } from 'vue-router';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import PicOperateModal from './components/pic-operate-modal.vue';
import _ from 'lodash';
import FilePreview from '/@/components/file-preview/index.vue';

const columns = reactive([
  {
    title: '图片缩略图',
    dataIndex: 'imgKey',
    width: 150
  },
  {
    title: '图片标题',
    dataIndex: 'title',
    width: 100
  },
  {
    title: '投放方向',
    dataIndex: 'type',
    width: 120
  },
  {
    title: '展示时间',
    dataIndex: 'startTime',
    width: 300
  },
  {
    title: '顺序',
    dataIndex: 'seq',
    width: 100
  },
  {
    title: '是否启用',
    width: 100,
    dataIndex: 'enableFlag',
  },
  {
    title: '创建人',
    width: 80,
    dataIndex: 'createUserName',
    ellipsis: true,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 200
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 140,
    fixed: 'right',
  },
]);

const queryFormState = {
  keywords: '',
  type: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

// 日期选择
let searchDate = ref();

function dateChange (dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

function resetQuery () {
  searchDate.value = [];
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await picApi.query(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function confirmDelete (enterpriseId) {
  Modal.confirm({
    title: '确定要删除吗？',
    content: '删除后，该信息将不可恢复',
    okText: '删除',
    okType: 'danger',
    onOk () {
      del(enterpriseId);
    },
    cancelText: '取消',
    onCancel () {
    },
  });
}

async function del () {
  try {
    useSpinStore().show();
    await picApi.delete();
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

function changeEnableFlag(record){
  Modal.confirm({
    title: `提示`,
    content: `确定要${record.enableFlag?'禁用':'启用'}该图片吗？`,
    okText: '确定',
    okType: 'danger',
    async onOk() {
      try {
        await picApi.picEnable(record.picId);
        message.success('操作成功');
        ajaxQuery();
      } catch (e) {
        console.log(e);
      }
    },
    cancelText: '取消',
    onCancel() {},
  });
}

let router = useRouter();
const operateModal = ref();

function add () {
  operateModal.value.showModal();
}

function update (record) {
  operateModal.value.showModal(_.cloneDeep(record));
}


onMounted(ajaxQuery);
</script>
