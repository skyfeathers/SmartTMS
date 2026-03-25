<template>
  <a-form class="smart-query-form" v-privilege="'etc:query'">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="关键字">
        <a-input v-model:value="queryForm.keyWords" class="form-width" placeholder="ETC卡号/创建人" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker v-model:value="queryForm.createTime" :presets="defaultTimeRanges" class="form-width"
          @change="changeCreateTime" />
      </a-form-item>
      <a-form-item label="状态" class="smart-query-form-item">
        <a-radio-group @change="ajaxQuery"  v-model:value="queryForm.disabledFlag">
          <a-radio-button :value="false">启用</a-radio-button>
          <a-radio-button :value="true">禁用</a-radio-button>
        </a-radio-group>
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

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button size="small" type="primary" @click="addOrUpdate()" v-privilege="'etc:add'">
          <template #icon>
            <PlusOutlined />
          </template>
          新建ETC
        </a-button>
        <a-button :disabled="selectedRowKeyList.length == 0" @click="confirmBatchDelete()" v-privilege="'etc:batchDelete'" size="small" type="primary" danger >
          <template #icon>
            <DeleteOutlined />
          </template>
          批量删除
        </a-button>
        <a-button @click="showImportModal" v-privilege="'etc:import'" size="small">
          导入
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.ETC" :refresh="ajaxQuery" />
      </div>
    </a-row>
    <a-table :columns="columns" :dataSource="tableData" :pagination="false" bordered
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }" :scroll="{ x: 1100,y:500 }"
      rowKey="etcId" size="small">
      <template  #bodyCell="{ column, record, text }">
        <template v-if="column.dataIndex === 'etcNo'">
          <a @click="oilCardDetail(record.etcId)">{{ record.etcNo }} </a><SmartCopyIcon :value="text" />
        </template>
        <template v-else-if="column.dataIndex === 'disabledFlag'">
          <span>{{ record.disabledFlag ? '禁用' : '启用' }} </span>
        </template>
        <template v-else-if="column.dataIndex === 'type'">
          <span>{{ $smartEnumPlugin.getDescByValue('OIL_CARD_TYPE_ENUM', record.type) }} </span>
        </template>
         <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button type="link" @click="addOrUpdate(record)" v-privilege="'etc:edit'">编辑</a-button>
          </div>
        </template>
      </template>
      
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination v-model:current="queryForm.pageNum" v-model:pageSize="queryForm.pageSize"
        :defaultPageSize="queryForm.pageSize" :pageSizeOptions="PAGE_SIZE_OPTIONS" :show-total="(total) => `共${total}条`"
        :total="total" show-less-items showQuickJumper showSizeChanger @change="ajaxQuery"
        @showSizeChange="ajaxQuery" />
    </div>
  </a-card>
  <!--新建编辑modal-->
  <EtcOperateModal ref="operateModal" @reloadList="ajaxQuery" />
  <!-- 导入 -->
  <EtcImportModal ref="importModal" @reloadList="ajaxQuery" />
</template>
<script setup>
import EtcOperateModal from './components/etc-operate-modal.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import EtcImportModal from './components/etc-import-modal.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { onMounted, reactive, ref } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { OIL_CARD_TYPE_ENUM } from '/@/constants/business/card-const';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { etcApi } from '/@/api/business/card/etc-api';
import { useRouter } from 'vue-router';

const columns = ref([
  {
    title: 'ETC卡号',
    dataIndex: 'etcNo',
    width: 180,
    fixed: 'left',
  },
  {
    title: '使用司机',
    width: 180,
    dataIndex: 'userName',
  },
  {
    title: '绑定车牌',
    width: 180,
    dataIndex: 'useVehicleNumber',
  },

  {
    title: 'ETC状态',
    width: 180,
    dataIndex: 'disabledFlag',
  },
  {
    title: '创建人',
    width: 180,
    dataIndex: 'createUserName',
  },
  {
    title: '创建时间',
    width: 180,
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    width: 50,
    dataIndex: 'action',
    fixed: 'right',
  },
]);

const queryFormState = {
  keyWords: '',
  disabledFlag: false,
  type: OIL_CARD_TYPE_ENUM.MASTER.value,
  createStartTime: undefined,
  createEndTime: undefined,
  createTime: [],
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const selectedRowKeyList = ref([]);
const tableData = ref([]);
const operateModal = ref();
const total = ref(0);

function changeCreateTime(dates, dateStrings) {
  queryForm.createStartTime = dateStrings[0];
  queryForm.createEndTime = dateStrings[1];
}

// 有效期
// let { idCardEffectiveDate,idCardEffectiveDateExpired }  = driverCertificateValiditySetup();

function onSelectChange(selectedRowKeys) {
  selectedRowKeyList.value = selectedRowKeys;
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}
function addOrUpdate(rowData) {
  operateModal.value.showModal(rowData);
}
// 触发批量删除
function confirmBatchDelete() {
  Modal.confirm({
    title: '提示',
    content: `确定要删除${selectedRowKeyList.value.length}个ETC吗?`,
    okText: '删除',
    okType: 'danger',
    onOk() {
      batchDelete();
    },
    cancelText: '取消',
    onCancel() { },
  });
}
// 批量删除
const batchDelete = async () => {
  try {
    useSpinStore().show();
    await etcApi.batchDeleteEtc(selectedRowKeyList.value);
    message.success('删除成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
};

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery() {
  try {
    useSpinStore().show();
    tableLoading.value = true;
    let responseModel = await etcApi.queryEtc(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
    useSpinStore().hide();
  }
}

const importModal = ref();

function showImportModal () {
  importModal.value.showModal();
}
// ----------------- 跳转 --------------------
let router = useRouter();
function addDriver(driverId) {
  router.push({ path: '/driver/operate', query: { driverId } });
}
function oilCardDetail(etcId) {
  router.push({ path: '/card/etc-detail', query: { etcId } });
}

onMounted(ajaxQuery);
</script>
<style lang="less" scoped>
.form-width {
  width: 240px;
}

.expired {
  color: red;
}
</style>
