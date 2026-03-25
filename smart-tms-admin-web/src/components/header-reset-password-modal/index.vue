<template>
  <a-modal
      :open="visible"
      title="修改密码"
      ok-text="确认"
      cancel-text="取消"
      @ok="updatePwd"
      @cancel="cancelModal"
  >
    <a-form
        ref="formRef"
        :model="form"
        :rules="rules"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 12 }"
    >
      <a-form-item label="原密码" name="oldPassword">
        <a-input
            v-model:value.trim="form.oldPassword"
            type="password"
            placeholder="请输入原密码"
        />
      </a-form-item>
      <!--      <a-form-item label="新密码" name="newPassword" :help="tips">-->
      <a-form-item label="新密码" name="newPassword">
        <a-input v-model:value.trim="form.newPassword" type="password" placeholder="请输入新密码"/>
      </a-form-item>
      <a-form-item label="确认密码" name="confirmPwd">
        <a-input
            v-model:value.trim="form.confirmPwd"
            type="password"
            placeholder="请输入确认密码"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { employeeApi } from '/@/api/system/employee/employee-api';
import { useSpinStore } from '/@/store/modules/system/spin';
import { regular } from '/@/constants/regular-const';

const visible = ref(false);
const formRef = ref();
const tips = '字母（不限大小写）+数字组合，6-15位（含6、15）'; //校验规则
const reg = /(^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$)/; //校验正则

const rules = {
  oldPassword: [{ required: true, message: '请输入原密码' }],
  newPassword: [
      { required: true, message: '请输入新密码'},
      {message: '新密码长度为8-15位，必须包含大小写字母以及数字', pattern: regular.strict_pwd }
  ],
  confirmPwd: [
      { required: true, message: '请输入确认密码',},
    {message: '确认密码长度为8-15位，必须包含大小写字母以及数字', pattern: regular.strict_pwd }
  ],
  // newPassword: [{ required: true, message: '请输入新密码' }],
  // confirmPwd: [{ required: true, message: '请输入确认密码' }],
};

const formDefault = {
  oldPassword: '',
  newPassword: '',
};
let form = reactive({
  ...formDefault,
});

async function updatePwd () {
  formRef.value
      .validate()
      .then(async () => {
        if (form.newPassword != form.confirmPwd) {
          message.error('新密码与确认密码不一致');
          return;
        }
        useSpinStore().show();
        try {
          await employeeApi.updateEmployeePassword(form);
          message.success('修改成功');
          visible.value = false;
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

function showModal () {
  visible.value = true;
}

function cancelModal () {
  visible.value = false;
}

// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({ showModal });
</script>
