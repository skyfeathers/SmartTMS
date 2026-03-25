<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-08 14:26:11
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-08 15:12:00
-->
<template>
  <a-card class="smart-margin-top10">
     <a-button @click="showModal()" type="primary" size="small" v-if="!actionFlag" class="smart-margin-bottom10">
          <template #icon>
            <PlusOutlined />
          </template>
          新增银行账户信息
      </a-button>
    <a-table :columns="columns" :dataSource="tableData" :pagination="false" bordered size="small">
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex == 'action' && actionFlag">
          <a @click="showModal()">新增银行账户信息</a>
        </template>
      </template>
      <template #paymentType="{ text }">
        <span>{{ $smartEnumPlugin.getDescByValue('PAYMENT_TYPE_ENUM', text) }}</span>
      </template>
      <template #publicAccountFlag="{record}">
        <span>{{ record.publicAccountFlag ? '是' : '否'}}</span>
      </template>
      <template #receiveImage="{text}">
        <FilePreview :fileList="text" />
      </template>
      <template #attachment="{text}">
        <FilePreview :fileList="text" />
      </template>
      <template #action="{ record, index }">
        <a style="margin-right:10px" @click="showModal(record)" type="link">编辑</a>
        <a @click="deleteBank(record,index)" type="link">删除</a>
      </template>
      <template #emptyText>
        <div>
          暂无数据
        </div>
      </template>
    </a-table>

    <a-modal title="银行账户" v-model:open="visible" :width="720" @cancel="onClose">
      <a-form
          ref="formRef"
          :model="bankForm"
          :rules="rules"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 12 }"
      >
        <a-form-item label="收付款方式" name="paymentType">
          <smart-enum-select
              width="100%"
              v-model:value="bankForm.paymentType"
              placeholder="请选择收付款方式"
              enum-name="PAYMENT_TYPE_ENUM"
          />
        </a-form-item>
        <a-form-item label="是否公户" name="publicAccountFlag">
          <BooleanFlagSelect v-model:value="bankForm.publicAccountFlag" true-text="是" false-text="否" placeholder="请选择是否公户" />
        </a-form-item>
        <a-form-item label="开户名" name="accountName">
          <a-input v-model:value="bankForm.accountName" placeholder="请输入开户名"/>
        </a-form-item>
        <template v-if="bankForm.publicAccountFlag == 1">
          <a-form-item label="银行账号" name="accountNumber">
            <a-input v-model:value="bankForm.accountNumber" placeholder="请输入银行账号"/>
          </a-form-item>
          <a-form-item label="银行名称" name="bankName">
            <a-input v-model:value="bankForm.bankName" placeholder="请输入银行名称"/>
          </a-form-item>
          <a-form-item label="支行名称" name="bankBranchName">
            <a-input v-model:value="bankForm.bankBranchName" placeholder="请输入支行名称"/>
          </a-form-item>
        </template>
        <a-form-item label="收款二维码" name="receiveImage" v-else>
          <Upload
              accept=".jpg,.jpeg,.png,.gif"
              listType="picture-card"
              :maxUploadSize="1"
              buttonText="点击上传二维码"
              :default-file-list="bankForm.receiveImage || []"
              @change="changeReceiveImage"
              :folder="FILE_FOLDER_TYPE_ENUM.SHIPPER.value"
          />
        </a-form-item>
        <a-form-item label="附件信息" name="remark">
          <Upload
              accept=".jpg,.jpeg,.png,.gif"
              listType="picture-card"
              :maxUploadSize="1"
              buttonText="点击上传附件"
              :default-file-list="bankForm.attachment || []"
              @change="changeAttachment"
              :folder="FILE_FOLDER_TYPE_ENUM.SHIPPER.value"
          />
        </a-form-item>
      </a-form>
      <div
          :style="{
          position: 'absolute',
          right: 0,
          bottom: 0,
          width: '100%',
          borderTop: '1px solid #e9e9e9',
          padding: '10px 16px',
          background: '#fff',
          textAlign: 'right',
          zIndex: 1,
        }"
      >
        <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">提交</a-button>
      </div>
    </a-modal>
  </a-card>
</template>
<script setup>
import { reactive, ref, computed, onMounted, watch } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';

import FilePreview from '/@/components/file-preview/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import BooleanFlagSelect from '/@/components/boolean-flag-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { PAYMENT_TYPE_ENUM } from '/@/constants/business/shipper-const';
import _ from 'lodash';
import { paymentWayApi } from '/@/api/business/shipper/payment-way-api.js';

const props = defineProps({
  // 是否显示操作按钮
  actionFlag: {
    type: Boolean,
    default: true
  },
  // shipperId
  shipperId: {
    type: Number,
    default: 0
  }
});

let tableData = ref([]);


// ------- 列表相关 start --------
const actionColumn = ref({
  title: '操作',
  dataIndex: 'action',
  slots: { customRender: 'action' },
  width:'130px'
});

const columns = ref([
  {
    title: '收付款方式',
    dataIndex: 'paymentType',
    slots: { customRender: 'paymentType' },
  },
  {
    title: '是否公户',
    dataIndex: 'publicAccountFlag',
    slots: { customRender: 'publicAccountFlag' },
  },
  {
    title: '开户名',
    dataIndex: 'accountName',
  },
  {
    title: '银行账号',
    dataIndex: 'accountNumber',
    slots: { customRender: 'accountNumber' },
  },
  {
    title: '银行名称',
    dataIndex: 'bankName',
    slots: { customRender: 'bankName' },
  },
  {
    title: '支行名称',
    dataIndex: 'bankBranchName',
    slots: { customRender: 'bankBranchName' },
  },
  {
    title: '收款二维码',
    dataIndex: 'receiveImage',
    slots: { customRender: 'receiveImage' },
  },
  {
    title: '附件信息',
    dataIndex: 'attachment',
    slots: { customRender: 'attachment' },
  },
]);
// ------- 列表相关 start --------

// ------- 新增、编辑银行信息 start --------

const defaultForm = {
  accountName: '',
  accountNumber: '',
  attachment: [],
  bankBranchName: '',
  bankName: '',
  defaultFlag: false,
  paymentType: 0,
  publicAccountFlag: 1,
  receiveImage: [],
  index: null,
  paymentWayId: null,
};
let bankForm = reactive({ ...defaultForm });

const rules = {
  paymentType: [{ required: true, message: '请选择收付款方式', type: 'number' }],
  publicAccountFlag: [{ required: true, message: '请选择是否公户' }],
};
const formRef = ref();

// 保存银行信息
function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {

          if(props.actionFlag) {
            tableData.value[bankForm.index] = Object.assign({}, bankForm, { publicAccountFlag: bankForm.publicAccountFlag == 1 ? true : false });
            console.log(tableData);
          }else {
            let api = bankForm.paymentWayId ? paymentWayApi.updatePaymentWay : paymentWayApi.addPaymentWay;
            await api({
              shipperId: props.shipperId,
              ...bankForm,
              publicAccountFlag: bankForm.publicAccountFlag == 1 ? true : false,
            });
            message.success('操作成功');
            // 刷新列表
            queryPaymentWayList();
          }
          onClose();
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
}

// 是否展示弹窗
const visible = ref(false);

// 展示修改银行信息弹窗
function showModal (bankInfo, index) {
  // 默认设置index
  if (!bankInfo) {
    console.log(tableData.value);
    bankForm.index = tableData.value.length;
  } else {
    Object.assign(bankForm, bankInfo, { publicAccountFlag: bankInfo.publicAccountFlag == 1 ? true : false });
  }
  visible.value = true;
}

function onClose () {
  Object.assign(bankForm, defaultForm);
  visible.value = false;
}

function changeAttachment (fileList) {
  bankForm.attachment = fileList;
}

function changeReceiveImage (fileList) {
  bankForm.receiveImage = fileList;
}

// ------- 新增、编辑银行信息 end --------
// 删除银行信息
function deleteBank (record,index) {
  Modal.confirm({
    title: '提示',
    content: '确认要删除该收付款信息吗？',
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
          await paymentWayApi.deletePaymentWay(record.paymentWayId);
          message.success('删除成功');
          // 删除成功后刷新列表
          queryPaymentWayList();
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

// 查询列表
async function queryPaymentWayList() {
  try {
    useSpinStore().show();
    let responseModel = await paymentWayApi.queryPaymentWayList(props.shipperId);
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
    queryPaymentWayList();
  }
});

defineExpose({
  tableData,
});
</script>
