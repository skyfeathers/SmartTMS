<!--
  * 修改密码
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:02:01 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal v-model:open="visible" title="修改密码" :destroyOnClose="true" :closable="false">
    <span v-if="forceEditPwdFlag" style="color: red;">您已长时间未修改密码，请先修改密码</span>
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="原密码" name="oldPassword">
        <a-input v-model:value.trim="form.oldPassword" type="password" placeholder="请输入原密码" />
      </a-form-item>
      <a-form-item label="新密码" name="newPassword" :help="tips">
        <a-input v-model:value.trim="form.newPassword" type="password" placeholder="请输入新密码" />
      </a-form-item>
      <a-form-item label="确认密码" name="confirmPwd" :help="tips">
        <a-input v-model:value.trim="form.confirmPwd" type="password" placeholder="请输入确认密码" />
      </a-form-item>
    </a-form>

    <template #footer>
      <a-button key="back" @click="cancelModal">取消</a-button>
      <a-button key="submit" type="primary" @click="updatePwd">保存</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { employeeApi } from "/@/api/system/employee/employee-api";
import { SmartLoading } from "/@/components/smart-loading";
import { smartSentry } from '/@/lib/smart-sentry';

const visible = ref(false);
const formRef = ref();
const tips = '密码长度8-20位且包含字母、数字、特殊符号三种'; //校验规则
const reg = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*?[~!@#$%^&*.\-_+<>?()|\\{}\[\]';:,/`])[a-zA-Z\d~!@#$%^&*.\-_+<>?()|\\{}\[\]';:,/`]{8,20}$/;

const rules = {
  oldPassword: [{ required: true, message: '请输入原密码' }],
  newPassword: [{ type: 'string', pattern: reg, message: '密码格式错误' }],
  confirmPwd: [{ type: 'string', pattern: reg, message: '请输入确认密码' }],
};

const formDefault = {
  oldPassword: '',
  newPassword: '',
};
let form = reactive({
  ...formDefault,
});
const emit = defineEmits(['forceEditPwdChange']);
async function updatePwd() {
  formRef.value
      .validate()
      .then(async () => {
        if (form.newPassword != form.confirmPwd) {
          message.error('新密码与确认密码不一致');
          return;
        }
        SmartLoading.show();
        try {
          await employeeApi.updateEmployeePassword(form);
          message.success('修改成功');
          if(forceEditPwdFlag.value){
            emit('forceEditPwdChange')
          }
          visible.value = false;
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
}
const forceEditPwdFlag=ref(false)
function showModal(flag) {
  forceEditPwdFlag.value=flag
  visible.value = true;
  form.oldPassword = '';
  form.newPassword = '';
  form.confirmPwd = '';
}

function cancelModal() {
  if(forceEditPwdFlag.value){
    message.error('您已长时间未修改密码，请先修改密码');
    return
  } else {
    visible.value = false;
  }
}

defineExpose({ showModal });
</script>
