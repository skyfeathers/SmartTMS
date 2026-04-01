<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-07 17:26:11
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-08 15:08:10
-->
<template>
  <a-card class="smart-margin-top10">
    <a-table :columns="columns" :dataSource="tableData" :pagination="false" :scroll="{ x: '100%' }" bordered size="small">
      <template #title>
      <a-button @click="showModal()" type="primary" size="small" v-if="!actionFlag">
          <template #icon>
            <PlusOutlined />
          </template>
          新增开票信息
      </a-button>
    </template>
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex == 'action' && actionFlag">
          <a @click="showModal()">新增开票信息</a>
        </template>
      </template>
      <template #attachment="{ text }">
        <file-preview :fileList="text"/>
      </template>
      <template #disableFlag="{record}">
        <span>{{ record.disableFlag ? '禁用' : '启用'}}</span>
      </template>
      <template #action="{ record, index }">
        <a style="margin-right:10px" @click="showModal(record)" type="link">编辑</a>
        <a @click="deleteInvoice(record, index)" type="link">删除</a>
      </template>
      <template #emptyText>
        <div>
          暂无数据
        </div>
      </template>
    </a-table>

    <InvoiceOperateModal ref="operateRef" @onSubmit="onSubmit"/>
  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted, watch } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import _ from 'lodash';

import DisabledFlagSelect from '/@/components/boolean-flag-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import FilePreview from '/@/components/file-preview/index.vue'
import InvoiceOperateModal from '/@/components/shipper-invoice-operate-modal/index.vue';
import { invoiceApi } from "/@/api/business/shipper/invoice-api";

const props = defineProps({
  // 是否显示操作按钮
  actionFlag: {
    type: Boolean,
    default: true
  },
  //  shipperId
  shipperId: {
    type: Number,
    default: 0
  },
  defaultInfo: {
    type: Object,
    default: () => ({})
  }
});

let tableData = ref([]);

// ------- 列表相关 start --------
const actionColumn = ref({
  title: '操作',
  dataIndex: 'action',
  slots: { customRender: 'action' },
  width:'130px',
  fixed: 'right',
});
const columns = ref([
  {
    title: '纳税人识别号',
    dataIndex: 'invoiceNo',
    width: 200,
  },
  {
    title: '开票抬头',
    dataIndex: 'invoiceName',
    width: 200,
    ellipsis: true,
  },
  {
    title: '开票银行',
    dataIndex: 'invoiceBankName',
    width: 160,
    ellipsis: true,
  },
  {
    title: '银行账号',
    dataIndex: 'invoiceBankAccount',
    width: 200,
  },
  {
    title: '开户行号',
    dataIndex: 'invoiceBankNo',
    width: 130,
  },
  {
    title: '开户行地址',
    dataIndex: 'invoiceBankAddress',
    width: 160,
    ellipsis: true,
  },
  {
    title: '开票电话',
    dataIndex: 'invoicePhone',
    width: 130,
  },
  {
    title: '开票地址',
    dataIndex: 'invoiceAddress',
    width: 200,
    ellipsis: true,
  },
  {
    title: '中征码',
    dataIndex: 'loanCardNo',
    width: 200,
  },
  {
    title: '禁用状态',
    dataIndex: 'disableFlag',
    slots: { customRender: 'disableFlag' },
    width: 100,
  },
  {
    title: '附件',
    dataIndex: 'attachment',
    slots: { customRender: 'attachment' },
    width: 160,
  },
]);
// ------- 列表相关 start --------

// ------- 新增、编辑发票信息 start --------
// 保存开票信息
async function onSubmit (params) {
  tableData.value[params.index] = Object.assign({}, params);

  if(props.actionFlag) {
    tableData.value[params.index] = Object.assign({}, params);
    console.log(tableData);
  }else {
    let api = params.invoiceId ? invoiceApi.updateInvoice : invoiceApi.addInvoice;
    await api({
      shipperId: props.shipperId,
      ...params,
    });
    message.success('操作成功');
    // 刷新列表
    queryInvoiceList();
  }
}

// 展示修改开票信息弹窗
let operateRef = ref();

function showModal (invoiceInfo) {
  let index = tableData.value.length;
  if(invoiceInfo){
    index = invoiceInfo.index;
  }
  console.log((invoiceInfo));
  // 货主详情，新增开票信息时，默认填充货主信息
  if(_.isEmpty(invoiceInfo) && !_.isEmpty(props.defaultInfo)) {
    invoiceInfo = Object.assign({}, {
      invoiceNo: props.defaultInfo.consignorId,
      invoiceName: props.defaultInfo.consignor,
    });
  }
  operateRef.value.showModal(index, invoiceInfo);
}

// ------- 新增、编辑开票信息 end --------

// 删除开票
function deleteInvoice (record, index) {
  Modal.confirm({
    title: '提示',
    content: '确认要删除该开票信息吗？',
    okText: '删除',
    okType: 'danger',
    onOk: async () => {
      // 区分actionFlag
      if (props.actionFlag) {
        tableData.value.splice(index, 1);
      } else {
        // 调用删除接口
        try {
          useSpinStore().show();
          await invoiceApi.deleteInvoice(record.invoiceId);
          message.success('删除成功');
          // 删除成功后刷新列表
          queryInvoiceList();
        } catch (error) {
          console.log(error);
          message.error('删除失败');
        } finally {
          useSpinStore().hide();
        }
      }
    },
  });
}

// 查询联系人列表
async function queryInvoiceList() {
  try {
    useSpinStore().show();
    let responseModel = await invoiceApi.queryInvoiceList(props.shipperId);
    tableData.value = responseModel.data;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

onMounted(() => {
  if (props.actionFlag) {
    columns.value = [
      actionColumn.value,
      ...columns.value,
    ];
  }else {
    columns.value = [
      ...columns.value,
      actionColumn.value,
    ];
    queryInvoiceList();
  }
});

defineExpose({
  tableData
});
</script>
