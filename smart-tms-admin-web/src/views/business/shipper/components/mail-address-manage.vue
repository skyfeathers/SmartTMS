<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-08 16:52:11
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-08 17:16:34
-->
<template>
  <a-card class="smart-margin-top10">
    <a-button @click="showModal()" type="primary" size="small" v-if="!actionFlag" class="smart-margin-bottom10">
          <template #icon>
            <PlusOutlined />
          </template>
          新增邮寄信息
      </a-button>
    <a-table :columns="columns" :dataSource="tableData" :pagination="false" :scroll="{x:1000}" bordered size="small">
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex == 'action' && actionFlag">
          <a @click="showModal()">新增邮寄信息</a>
        </template>
      </template>
      <template #defaultFlag="{ record }">
        <span>{{ record.defaultFlag ? '是' : '否'}}</span>
      </template>
      <template #area="{ record }">
        <span>{{ record.receiveProvinceName }}{{ record.receiveCityName }}{{ record.receiveDistrictName }}</span>
      </template>
      <template #action="{ record, index }">
        <a style="margin-right:10px" @click="showModal(record)" type="link">编辑</a>
        <a @click="handleDelete(record,index)" type="link">删除</a>
      </template>
      <template #emptyText>
        <div>
          暂无数据
        </div>
      </template>
    </a-table>

    <a-modal title="邮寄信息" v-model:open="visible" :width="720" @cancel="onClose">
      <a-form
          ref="formRef"
          :model="mailAddressForm"
          :rules="rules"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 12 }"
      >
        <a-form-item label="收件人姓名" name="receivePerson">
          <a-input
              v-model:value="mailAddressForm.receivePerson"
              placeholder="请输入收件人姓名"
          />
        </a-form-item>
        <a-form-item label="收件人手机号" name="receivePersonPhone">
          <a-input
              v-model:value="mailAddressForm.receivePersonPhone"
              placeholder="请输入收件人手机号"
          />
        </a-form-item>
        <a-form-item label="是否默认地址" name="defaultFlag">
          <BooleanFlagSelect v-model:value="mailAddressForm.defaultFlag" true-text="是" false-text="否" placeholder="请选择是否默认" />
        </a-form-item>
        <a-form-item label="所在地区" name="remark">
          <smart-area-cascader v-model:value="area" @change="changeArea" type="province_city_district"
                               placeholder="请选择所在地区"
                               style="width:100%"/>
        </a-form-item>
        <a-form-item label="详细地址" name="receiveAddress">
          <a-input v-model:value="mailAddressForm.receiveAddress" placeholder="请输入详细地址"/>
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
import { reactive, ref, onMounted, watch } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';
import { regular } from '/@/constants/regular-const';

import SmartAreaCascader from '/@/components/smart-area-cascader/index.vue';
import BooleanFlagSelect from '/@/components/boolean-flag-select/index.vue';
import { mailAddressApi } from '/@/api/business/shipper/mail-address-api';

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

// ------- 列表相关 start --------
let tableData = ref([]);

const actionColumn = ref({
  title: '操作',
  dataIndex: 'action',
  slots: { customRender: 'action' },
  width:'130px'
});

const columns = ref([
  {
    title: '收件人姓名',
    dataIndex: 'receivePerson',
  },
  {
    title: '收件人手机号',
    dataIndex: 'receivePersonPhone',
  },
  {
    title: '是否默认地址',
    dataIndex: 'defaultFlag',
    slots: { customRender: 'defaultFlag' },
  },
  {
    title: '所在地区',
    dataIndex: 'area',
    slots: { customRender: 'area' },
  },
  {
    title: '详细地址',
    dataIndex: 'receiveAddress',
  },
]);
// ------- 列表相关 start --------

// ------- 新增、编辑 start --------

const defaultForm = {
  receivePerson: '',
  receivePersonPhone: '',
  defaultFlag: null,
  receiveAddress: '',
  index: null,
  mailAddressId: null,
};
let mailAddressForm = reactive({ ...defaultForm });
let area = ref([]);

const rules = {
  receivePerson: [{ required: true, message: '请输入收件人姓名' }],
  receivePersonPhone: [
    { required: true, message: '请输入收件人手机号' },
    { pattern: regular.phone, message: '收件人手机号格式不正确', trigger: 'blur' }
  ],
  defaultFlag: [{ required: true, message: '请选择是否默认地址' }],
};
const formRef = ref();

// 保存
function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if(props.actionFlag) {
            tableData.value[mailAddressForm.index] = Object.assign({}, mailAddressForm, { defaultFlag: mailAddressForm.defaultFlag == 1 ? true : false });
            console.log(tableData);
          }else {
            let api = mailAddressForm.mailAddressId ? mailAddressApi.updateMailAddress : mailAddressApi.addMailAddress;
            await api({
              shipperId: props.shipperId,
              ...mailAddressForm,
              defaultFlag: mailAddressForm.defaultFlag == 1 ? true : false,
            });
            message.success('操作成功');
            // 刷新列表
            queryMailAddressList();
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

function changeArea (value, selectedOptions) {
  if (!_.isEmpty(selectedOptions)) {
    // 更新所选择地区
    mailAddressForm.receiveProvinceCode = area.value[0].value;
    mailAddressForm.receiveProvinceName = area.value[0].label;

    mailAddressForm.receiveCityCode = area.value[1].value;
    mailAddressForm.receiveCityName = area.value[1].label;
    if (area.value[2]) {
      mailAddressForm.receiveDistrictCode = area.value[2].value;
      mailAddressForm.receiveDistrictName = area.value[2].label;
    }
  }
}

// 是否展示弹窗
const visible = ref(false);

// 展示修改联系人弹窗
function showModal (mailAddressInfo, index) {
  // 默认设置index
  if (!mailAddressInfo) {
    Object.assign(mailAddressForm, defaultForm);
    mailAddressForm.index = tableData.value.length;
    area.value = [];
  } else {
    if (mailAddressInfo.receiveProvinceCode) {
      area.value = [{ value: mailAddressInfo.receiveProvinceCode }, { value: mailAddressInfo.receiveCityCode }, { value: mailAddressInfo.receiveDistrictCode }];
    }
    Object.assign(mailAddressForm, mailAddressInfo);
  }
  visible.value = true;
}

function onClose () {
  Object.assign(mailAddressForm, defaultForm);
  visible.value = false;
}

// ------- 新增、编辑 end --------
// 删除
function handleDelete (record, index) {
  Modal.confirm({
    title: '提示',
    content: '确认要删除该邮寄地址吗？',
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
          await mailAddressApi.deleteMailAddress(record.mailAddressId);
          message.success('删除成功');
          // 删除成功后刷新列表
          queryMailAddressList();
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
async function queryMailAddressList() {
  try {
    useSpinStore().show();
    let responseModel = await mailAddressApi.queryMailAddressList(props.shipperId);
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
    queryMailAddressList();
  }
});

defineExpose({
  tableData
});
</script>
