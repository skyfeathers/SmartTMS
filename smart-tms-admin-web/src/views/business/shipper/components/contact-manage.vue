<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-07 17:26:11
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-07 18:06:14
-->
<template>
  <a-card class="smart-margin-top10" size="small">
      <a-button @click="showModal()" type="primary" size="small" v-if="!actionFlag" class="smart-margin-bottom10">
          <template #icon>
            <PlusOutlined />
          </template>
          新建联系人
      </a-button>
      <div style="color:red" v-else class="smart-margin-bottom10">联系人信息(至少一条)</div>
    <a-table :columns="columns" :dataSource="tableData" :pagination="false" :scroll="{ x: 800 }" bordered size="small">
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex == 'action' && actionFlag">
          <a @click="showModal()">新增联系人</a>
        </template>
      </template>
      <template #action="{ record, index }">
        <a style="margin-right:10px" @click="showModal(record)" type="link">编辑</a>
        <a @click="deleteContact(record, index)" type="link">删除</a>
      </template>
      <template #emptyText>
        <div>
          暂无数据
        </div>
      </template>
    </a-table>

    <a-modal title="联系人" v-model:open="visible" :width="720" @cancel="onClose">
      <a-form
          ref="formRef"
          :model="contactForm"
          :rules="rules"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 12 }"
      >
        <a-form-item label="联系人姓名" name="contactName">
          <a-input
              v-model:value="contactForm.contactName"
              placeholder="请输入联系人姓名"
          />
        </a-form-item>
        <a-form-item label="联系电话" name="contactPhone">
          <a-input
              v-model:value="contactForm.contactPhone"
              placeholder="请输入联系电话"
          />
        </a-form-item>
        <a-form-item label="职位" name="position">
          <a-input v-model:value="contactForm.position" placeholder="请输入职位"/>
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-input v-model:value="contactForm.remark" placeholder="请输入备注"/>
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
import { contactApi } from "/@/api/business/shipper/contact-api";

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

let columns = ref([
  {
    title: '联系人姓名',
    dataIndex: 'contactName',
  },
  {
    title: '联系人电话',
    dataIndex: 'contactPhone',
  },
  {
    title: '联系人职位',
    dataIndex: 'position',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
]);
// ------- 列表相关 start --------

// ------- 新增、编辑联系人 start --------

const defaultForm = {
  contactName: '',
  contactPhone: '',
  position: '',
  remark: '',
  index: null,
  contactId: null,
};
let contactForm = reactive({ ...defaultForm });

const rules = {
  contactName: [{ required: true, message: '请输入联系人姓名' }],
  contactPhone: [
    { required: true, message: '请输入联系人电话' },
    { pattern: regular.phone, message: '请输入正确的联系人电话', trigger: 'blur' }
  ]
};
const formRef = ref();

// 保存联系人
function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if(props.actionFlag) {
            tableData.value[contactForm.index] = Object.assign({}, contactForm);
            console.log(tableData);
          }else {
            let api = contactForm.contactId ? contactApi.updateContact : contactApi.addContact;
            await api({
              shipperId: props.shipperId,
              ...contactForm,
            });
            message.success('操作成功');
            // 刷新列表
            queryContactList();
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

// 展示修改联系人弹窗
function showModal (contactInfo, index) {
  // 默认设置index
  if (!contactInfo) {
    contactForm.index = tableData.value.length;
  } else {
    Object.assign(contactForm, contactInfo);
  }
  
  visible.value = true;
}

function onClose () {
  Object.assign(contactForm, defaultForm);
  visible.value = false;
}

// ------- 新增、编辑联系人 end --------
// 删除联系人
function deleteContact (record, index) {
  Modal.confirm({
    title: '提示',
    content: '确认要删除该联系人吗？',
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
          await contactApi.deleteContact(record.contactId);
          message.success('删除成功');
          // 删除成功后刷新列表
          queryContactList();
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
async function queryContactList() {
  try {
    useSpinStore().show();
    let responseModel = await contactApi.queryContactList(props.shipperId);
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
    queryContactList();
  }
});

defineExpose({
  tableData
});
</script>
