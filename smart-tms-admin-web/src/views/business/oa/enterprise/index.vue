<template>
  <a-form class="smart-query-form" v-privilege="'enterprise:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="企业名称/联系人/联系电话"/>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange"/>
        </a-space>
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
        <a-button @click="add()" v-privilege="'enterprise:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建企业
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
        <template v-if="column.dataIndex === 'platformFlag'">
          <span>{{ record.platformFlag ? '平台': '租户' }}</span>
        </template>
        <template v-if="column.dataIndex === 'unifiedSocialCreditCode'">
          <span>{{ record.unifiedSocialCreditCode }}</span>
          <SmartCopyIcon :value="text" v-if="record.unifiedSocialCreditCode" />
        </template>
        <template v-if="column.dataIndex === 'networkFreightTransportCode'">
          <span>{{ record.networkFreightTransportCode }}</span>
          <SmartCopyIcon :value="text" v-if="record.networkFreightTransportCode" />
        </template>
        <template v-if="column.dataIndex === 'contactPhone'">
          <span>{{ record.contactPhone }}</span>
          <SmartCopyIcon :value="text" v-if="record.contactPhone" />
        </template>
        <template  v-if="column.dataIndex === 'disabledFlag'">
          {{ text ? '禁用' : '启用' }}
        </template>
        <template  v-if="column.dataIndex === 'enterpriseName'">
          <a @click="detail(record.enterpriseId)">{{ record.enterpriseName}}</a>
        </template>
        <template  v-if="column.dataIndex === 'type'">
          {{$smartEnumPlugin.getDescByValue('ENTERPRISE_TYPE_ENUM', text)}}
        </template>
        <template  v-if="column.dataIndex === 'action'">
          <a-button size="small" type="link" @click="flowConfig(record.enterpriseId)" v-privilege="'enterprise:flowConfig'">流程配置</a-button>
          <a-button size="small" type="link" @click="initAccount(record.enterpriseId)" v-privilege="'enterprise:initAccount'">账号初始化</a-button>
          <a-button v-privilege="'enterprise:edit'" size="small" type="link" @click="update(record.enterpriseId)">编辑</a-button>
          <a-button v-privilege="'enterprise:delete'" size="small" type="link" danger @click="confirmDelete(record.enterpriseId)">删除</a-button>
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
    <EnterpriseOperate ref="operateRef" @refresh="ajaxQuery"/>
    <EnterpriseFlowConfig ref="flowConfigRef"/>
    <InitAccountDialog ref="initAccountDialogRef"/>
  </a-card>
</template>
<script setup>
import {reactive, ref, onMounted, nextTick} from 'vue';
import {message, Modal} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {enterpriseApi} from '/@/api/business/oa/enterprise-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';
import {useRouter} from "vue-router";

import EnterpriseOperate from './components/enterprise-operate-modal.vue';
import EnterpriseFlowConfig from './components/enterprise-flow-config.vue';
import InitAccountDialog from './components/enterprise-init-account-dialog.vue';
const columns = reactive([
  {
    title: '企业名称',
    dataIndex: 'enterpriseName',
    width: 240
  },
  {
    title: '企业简称',
    dataIndex: 'enterpriseShortName',
    width: 100
  },
  {
    title: '账号类型',
    dataIndex: 'platformFlag',
    width: 100
  },
  {
    title: '统一社会信用代码',
    dataIndex: 'unifiedSocialCreditCode',
    width: 200
  },
  {
    title:'道路运输经营许可证',
    dataIndex:'networkFreightTransportCode',
    width: 180
  },
  {
    title: '企业类型',
    dataIndex: 'type',
    width: 100
  },
  {
    title: '联系人',
    width: 100,
    dataIndex: 'contact',
  },
  {
    title: '联系人电话',
    width: 150,
    dataIndex: 'contactPhone',
  },
  {
    title: '邮箱',
    width: 180,
    dataIndex: 'email',
  },
  {
    title: '状态',
    width: 50,
    dataIndex: 'disabledFlag',
  },
  {
    title: '创建人',
    width: 60,
    dataIndex: 'createUserName',
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
    width: 260
  },
]);

const queryFormState = {
  keywords: '',
  endTime: null,
  startTime: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

// 日期选择
let searchDate = ref();

function dateChange(dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

function resetQuery() {
  searchDate.value = [];
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
    let responseModel = await enterpriseApi.pageQuery(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function confirmDelete(enterpriseId) {
  Modal.confirm({
    title: "确定要删除吗？",
    content: "删除后，该信息将不可恢复",
    okText: "删除",
    okType: "danger",
    onOk() {
      del(enterpriseId);
    },
    cancelText: "取消",
    onCancel() {
    },
  });
}

async function del (enterpriseId) {
  try {
    useSpinStore().show();
    await enterpriseApi.delete(enterpriseId);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
};

let router = useRouter();
const operateRef = ref();
function add() {
  operateRef.value.showModal();
}

function update(enterpriseId) {
  operateRef.value.showModal(enterpriseId, true);
}

const flowConfigRef = ref();
function flowConfig(enterpriseId) {
  flowConfigRef.value.showDrawer(enterpriseId);
}

async function initAccount(enterpriseId) {
  try {
    tableLoading.value = true;
    await enterpriseApi.initEnterpriseAccount(enterpriseId);
    message.success('账号初始化完成');
    this.showAccount(enterpriseId);
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

//-------------回显账号密码信息----------
let initAccountDialogRef = ref();
async function showAccount(enterpriseId) {
  try {
    let result = await enterpriseApi.detail(enterpriseId);
    let data = result.data;
    initAccountDialogRef.value.showModal(data.contactPhone, data.contactPhone);
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }

}

function detail(enterpriseId) {
  router.push({path: '/business/oa/enterprise/enterprise-detail', query: {enterpriseId: enterpriseId}});
}

onMounted(ajaxQuery);
</script>
