<template>
  <a-form class="smart-query-form" v-privilege="'contract:query'">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="关键字">
        <a-input
            v-model:value="queryForm.keywords"
            class="form-width"
            placeholder="编号/名称/关联订单"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="状态">
        <a-radio-group v-model:value="queryForm.status" @change="search">
          <a-radio-button :value="CONTRACT_STATUS_ENUM.WAIT_SIGN.value">{{CONTRACT_STATUS_ENUM.WAIT_SIGN.desc}}</a-radio-button>
          <a-radio-button :value="CONTRACT_STATUS_ENUM.SIGNED.value">{{CONTRACT_STATUS_ENUM.SIGNED.desc}}</a-radio-button>
          <a-radio-button :value="CONTRACT_STATUS_ENUM.CANCEL.value">{{CONTRACT_STATUS_ENUM.CANCEL.desc}}</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="合同类型">
        <ContractTypeSelect v-model:value="queryForm.contractType" placeholder="请选择合同类型"></ContractTypeSelect>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker v-model:value="queryForm.createTime" :presets="defaultTimeRanges" class="form-width"
                        @change="changeCreateTime"/>
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

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block" v-if="showOperate">
        <a-button @click="createContract" v-privilege="'contract:create'" size="small" type="primary">
          创建合同
        </a-button>
        <a-button @click="confirmCancel()" v-privilege="'contract:cancel'" size="small" type="primary" danger>
          作废
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.CONTRACT" :refresh="ajaxQuery" />
      </div>
    </a-row>
    <a-table
        :columns="columns"
        :dataSource="tableData"
        rowKey="contractId"
        :pagination="false"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
        :scroll="{ x: 2300,y:500 }"
        size="small"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'signerType'">
          {{ $smartEnumPlugin.getDescByValue('CONTRACT_SIGNER_TYPE_ENUM', text) }}
        </template>

        <template v-if="column.dataIndex === 'signType'">
          {{ $smartEnumPlugin.getDescByValue('CONTRACT_SIGN_TYPE_ENUM', text) }}
        </template>

        <template v-if="column.dataIndex === 'orderId'">
          <a @click="orderDetail(record.orderId)">{{ record.orderNo }}</a>
        </template>

        <template v-if="column.dataIndex === 'waybillId'">
          <a @click="waybillDetail(record.waybillId)">{{ record.waybillNumber }}</a>
        </template>

        <template v-if="column.dataIndex === 'status'">
          {{ $smartEnumPlugin.getDescByValue('CONTRACT_STATUS_ENUM', text) }}
        </template>

        <template v-if="column.dataIndex === 'contractFile'">
          <div v-if="!_.isEmpty(text)">
              <a @click="showAttachmentFlagModal(text)">查看附件</a>
          </div>
          <span v-else> 暂无附件</span>
      </template>

        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button v-if="CONTRACT_SIGN_TYPE_ENUM.OFFLINE.value == record.signType" v-privilege="'contract:edit'" type="link" @click="updateContract(record)">编辑</a-button>
            <a-button v-if="record.onlineContractId && record.status !== CONTRACT_STATUS_ENUM.CANCEL.value" type="link" @click="getExecuteUrl(record.contractId,false)">预览
            </a-button>
            <a-button v-if="record.onlineContractId && record.status !== CONTRACT_STATUS_ENUM.CANCEL.value" type="link" @click="getExecuteUrl(record.contractId,true)">签署
            </a-button>
            <a-button v-if="record.status !== CONTRACT_STATUS_ENUM.CANCEL.value" type="link" @click="downloadContract(record.contractId)">下载</a-button>
            <a-button v-if="record.status !== CONTRACT_STATUS_ENUM.CANCEL.value" type="link" @click="cancelContractModal(record.contractId)">作废
            </a-button>
            <a-button v-if="record.onlineContractId" type="link" @click="showRecordModal(record.contractId)">操作记录
            </a-button>
            <a-button v-if="CONTRACT_SIGN_TYPE_ENUM.OFFLINE.value == record.signType" type="link" @click="showDataTracerRecordModal(record.contractId)">操作记录
            </a-button>
          </div>
        </template>
      </template>
    </a-table>

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
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
      />
    </div>
  </a-card>
  <ContractCreateModal ref="contractCreateModal" :signerBelongId="signerBelongId" :signerType="signerType"
                       @refresh="ajaxQuery"/>
  <ContractRecordsModal ref="contractRecordsModal"/>
  <DataTracerModal ref="dataTracerModalRef"/>
  <a-modal v-model:open="showAttachmentFlag" title="合同预览" :footer="null" width="500px">
    <a-card>
      <Upload :default-file-list="attachmentList" listType="picture-card" :showUploadBtn="false" />
    </a-card>
  </a-modal>
</template>
<script setup>
import ContractCreateModal from './contract-create-modal.vue'
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import FilePreview from '/@/components/file-preview/index.vue';
import ContractRecordsModal from './contract-records-modal.vue';
import DataTracerModal from '/@/components/data-tracer/record-modal.vue';
import ContractTypeSelect from "/@/components/contract-type-select/index.vue";
import { CONTRACT_SIGN_TYPE_ENUM } from '/@/constants/business/contract-const';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import {computed, onMounted, reactive, ref} from "vue";
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from "/@/constants/common-const";
import {useRouter} from "vue-router";
import Upload from '/@/components/upload/index.vue';
import {contractApi} from "/@/api/business/contract/contract-api";
import {message, Modal} from "ant-design-vue";
import {useSpinStore} from "/@/store/modules/system/spin";
import {CONTRACT_SIGNER_TYPE_ENUM, CONTRACT_STATUS_ENUM} from "/@/constants/business/contract-const";
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { isImageByType } from '/@/utils/image-util';
import _ from 'lodash';
import {DATA_TRACER_BUSINESS_TYPE_ENUM} from "/@/constants/support/data-tracer-const";

let showAttachmentFlag = ref(false);
let attachmentList = ref([])

function showAttachmentFlagModal(contractFile) {
  showAttachmentFlag.value = true;
  attachmentList.value = contractFile || [];
}

const props = defineProps({
  signerType: Number, // CONTRACT_SIGNER_TYPE_ENUM
  signerBelongId: Number, // 对应ID
  waybillId: Number, // 运单ID
  orderId: Number, // 订单ID
})
// ----------------------- 以下是暴露的方法内容 -----------------------
defineExpose({
  resetQuery
});
// --------------------- 列表查询 ------------------------
const columns = ref([
  {
    title: "合同编号",
    dataIndex: "contractCode",
    width: 150,
    fixed: "left",
  },
  {
    title: "合同名称",
    width: 180,
    dataIndex: "contractName",
  },
  {
    title: "签署人类型",
    width: 90,
    dataIndex: "signerType",
  },
  {
    title: "合同形式",
    dataIndex: "signType",
    width: 100,
  },
  {
    title: "合同类型",
    dataIndex: "contractTypeName",
    width: 180,
  },
  {
    title: "合同负责人",
    dataIndex: "responsibleUserName",
    width: 100,
  },
  {
    title: "关联订单",
    width: 150,
    dataIndex: "orderId",
  },
  {
    title: "关联运单",
    dataIndex: "waybillId",
    width: 150,
  },
  {
    title: "合同附件",
    dataIndex: "contractFile",
    width: 150
  },
  {
    title: "到期时间",
    width: 100,
    dataIndex: "expirationDate",
  },
  {
    title: "状态",
    dataIndex: "status",
    width: 70,
  },
  {
    title: "创建人",
    width: 80,
    dataIndex: "createUserName",
  },
  {
    title: "创建时间",
    width: 160,
    dataIndex: "createTime",
  },
  {
    title: "操作",
    width: 220,
    dataIndex: "action",
    fixed: 'right',
  },
]);
const showOperate = computed(() => {
  // 组件应用在各自业务中 不展示操作列
  if (props.orderId || props.waybillId) {
      return false;
  }
  return true;
})


const queryFormState = {
  keywords: "",
  status: CONTRACT_STATUS_ENUM.SIGNED.value,
  createStartTime: undefined,
  createEndTime: undefined,
  createTime: [],
  signerType: undefined,
  signerBelongId: undefined,
  waybillId: undefined,
  orderId: undefined,
  contractType: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const selectedRowKeyList = ref([]);
const tableData = ref([]);
const total = ref(0);

function changeCreateTime(dates, dateStrings) {
  queryForm.createStartTime = dateStrings[0];
  queryForm.createEndTime = dateStrings[1];
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
    selectedRowKeyList.value = [];
    tableLoading.value = true;
    queryForm.signerType = props.signerType;
    queryForm.signerBelongId = props.signerBelongId;
    queryForm.waybillId = props.waybillId;
    queryForm.orderId = props.orderId;
    let responseModel = await contractApi.queryContract(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// ----------------------- 合同操作 -----------------------
function cancelContractModal(contractId){
  Modal.confirm({
    title: "确定要作废吗？",
    content: "作废后，该信息将不可恢复",
    okText: "作废",
    okType: "danger",
    onOk() {
      // 查询是否为电子签约合同
      let find = tableData.value.find(e=>e.contractId === contractId);
      if(!find){
        message.error("未找到合同信息");
        return;
      }
      if(find.onlineContractId){
        revokeContract(contractId);
      } else {
        cancelContract(contractId);
      }
    },
    cancelText: "取消",
    onCancel() {
    },
  });
}

function confirmCancel() {
  if (selectedRowKeyList.value.length !== 1) {
    message.error('请选择一个合同');
    return;
  }
  let contractId = selectedRowKeyList.value[0];
  cancelContractModal(contractId);
}

async function cancelContract(contractId) {
  try {
    useSpinStore().show();
    let params = {
      contractIdList: [contractId],
      status: CONTRACT_STATUS_ENUM.CANCEL.value
    }
    await contractApi.batchUpdateStatus(params);
    message.success('作废成功');
    await ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// 获取预览/签署地址
async function getExecuteUrl(contractId, signedFlag) {
  useSpinStore().show();
  try {
    let res = await contractApi.getExecuteUrl(contractId, signedFlag);
    let keyword = signedFlag ? '签署' : '预览';
    Modal.confirm({
      title: `合同${keyword}地址为短连接，30天内有效`,
      content: res.data.shortUrl,
      okText: '打开',
      onOk() {
        window.open(res.data.shortUrl);
      },
      cancelText: "取消",
      onCancel() {
      },
    });
  } catch (error) {
    console.error(error);
  } finally {
    useSpinStore().hide();
  }
}

// 获取下载地址
async function downloadContract(contractId) {
  useSpinStore().show();
  try {
    let res = await contractApi.downloadContract(contractId);
    Modal.confirm({
      title: '确认下载合同？',
      okText: '确定',
      onOk() {
        window.open(res.data);
      },
      cancelText: "取消",
      onCancel() {
      },
    });
  } catch (error) {
    console.error(error);
  } finally {
    useSpinStore().hide();
  }
}

// 合同撤销
async function revokeContract(contractId) {
  useSpinStore().show();
  try {
    await contractApi.revokeContract(contractId);
    message.success('作废成功');
    await ajaxQuery();
  } catch (error) {
    console.error(error);
  } finally {
    useSpinStore().hide();
  }
}

// 查询线上合同操作记录
let contractRecordsModal = ref();
function showRecordModal(contractId) {
  contractRecordsModal.value.showModal(contractId);
}

let dataTracerModalRef = ref();
function showDataTracerRecordModal(contractId) {
  dataTracerModalRef.value.showModal(contractId, DATA_TRACER_BUSINESS_TYPE_ENUM.CONTRACT.value);
}

function download (url) {
  window.open(url);
}

// ----------------- 新建 --------------------
let contractCreateModal = ref();

function createContract() {
  contractCreateModal.value.showModal();
}

function updateContract(record) {
  contractCreateModal.value.showUpdateModal(record);
}

// ----------------- 跳转 --------------------
let router = useRouter();

function contractDetail(contractId) {

}

function orderDetail(orderId) {
  router.push({path: '/order/detail', query: {orderId}})
}

function waybillDetail(waybillId) {
  router.push({path: '/waybill/waybill-detail', query: {waybillId}})
}

onMounted(ajaxQuery);
</script>
<style lang="less" scoped>
.form-width {
  width: 240px;
}

</style>
