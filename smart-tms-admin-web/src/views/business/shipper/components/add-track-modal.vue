<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-07 11:32:59
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-07-22
-->
<template>
  <a-modal title="添加跟进记录" v-model:open="visible" :width="720" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item label="跟进方式" name="trackWay">
        <smart-enum-select v-model:value="form.trackWay" placeholder="请选择跟进方式" enum-name="TRACK_WAY_ENUM" style="width: 100%" />
      </a-form-item>
      <a-form-item label="联系人" name="intervieweeName">
        <a-input v-model:value="form.intervieweeName" placeholder="请输入联系人" />
      </a-form-item>
      <a-form-item label="跟进日期" name="trackTime">
        <a-date-picker v-model:value="form.trackTime" valueFormat="YYYY-MM-DD HH:mm:ss" placeholder="请选择跟进日期" show-time style="width: 100%" />
      </a-form-item>
      <a-form-item label="跟踪内容" name="trackContent">
        <a-input v-model:value="form.trackContent" placeholder="请输入跟踪内容" />
      </a-form-item>
      <a-form-item label="下次跟进日期" name="nextTrackTime">
        <a-date-picker
          v-model:value="form.nextTrackTime"
          valueFormat="YYYY-MM-DD HH:mm:ss"
          placeholder="请选择下次跟进日期"
          show-time
          style="width: 100%"
        />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-input v-model:value="form.remark" placeholder="请输入备注" />
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
</template>
<script setup>
import { reactive, ref } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { shipperTrackApi } from '/@/api/business/shipper/shipper-track-api';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
// ----------------------- 以下是字段定义 emits props ------------------------

const props = defineProps({
  shipperIdList: {
    type: Array,
    default: () => {
      return [];
    },
  },
});

// emit
const emit = defineEmits('reloadList');

// 是否展示弹窗
const visible = ref(false);

const form = reactive({
  shipperId: null,
  trackWay: null,
  intervieweeName: null,
  trackTime: null,
  trackContent: null,
  nextTrackTime: null,
  remark: null,
});
const rules = {
  trackWay: [{ required: true, message: '请选择跟进方式' }],
  intervieweeName: [{ required: true, message: '请输入联系人' }],
  trackTime: [{ required: true, message: '请输入跟进日期' }],
};

// ----------------------- 以下是方法 ------------------------
function showModal(shipperId) {
  form.shipperId = shipperId;
  visible.value = true;
}

function onClose() {
  visible.value = false;
}

const formRef = ref();

async function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        let param = {};
        await shipperTrackApi.addTrack(form);
        message.success('修改成功');
        onClose();
        emit('reloadList');
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

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
