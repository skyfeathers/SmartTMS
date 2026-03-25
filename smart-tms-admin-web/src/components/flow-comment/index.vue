<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 15:19:46
 * @LastEditors:
 * @LastEditTime: 2022-07-13 15:19:46
-->
<template>
  <a-modal title="评论"
    :width="720"
    :open="modalVisible"
    @cancel="onClose">
    <a-form ref="formRef"
      :model="form"
      :rules="rules"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 12 }">
      <a-form-item label="评论内容"
        name="content">
        <a-textarea style="width: 100%" placeholder="请输入评论" :rows="4" v-model:value="form.content" />
      </a-form-item>
    </a-form>

    <div :style="{
        position: 'absolute',
        right: 0,
        bottom: 0,
        width: '100%',
        borderTop: '1px solid #e9e9e9',
        padding: '10px 16px',
        background: '#fff',
        textAlign: 'right',
        zIndex: 1,
      }">
      <a-button style="margin-right: 8px"
        @click="onClose">取消</a-button>
      <a-button type="primary"
        @click="submitComment">提交</a-button>
    </div>
  </a-modal>
</template>
<script setup>
import { reactive, ref} from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { flowApi } from '/@/api/business/flow/flow-api';


const formRef = ref();
const formDefault = {
  instanceId: undefined,
  currentTaskId: undefined,
  content: '',
};
let form = reactive({ ...formDefault });
const rules = { comment: [{ required: true, message: '请输入评论' }] };


const modalVisible = ref(false);
// 显示
function showModal(instanceId,currentTaskId) {
  form.instanceId = instanceId;
  form.currentTaskId = currentTaskId;
  modalVisible.value = true;
}
// 关闭
function onClose() {
  Object.assign(form, formDefault)
  modalVisible.value = false;
}

const emit = defineEmits(['finish']);
async function submitComment() {
  formRef.value
    .validate()
    .then(async () => {
      if (!form.content) {
        return message.error('请输入评论');
      }
      useSpinStore().show();
      try {
        await flowApi.submitComment(form);
        message.info('提交成功');
        emit('finish');
        onClose();
      } catch (e) {
        console.log(e);
      } finally {
        useSpinStore().hide();
      }
    })
    .catch((error) => {
      console.log('error', error);
      message.error('参数验证错误，请仔细填写表单数据!');
    });
}

defineExpose({
  showModal,
});
</script>

<style lang="less" scoped>
.receive-time-tips {
  padding-top: 5px;
  line-height: 1.46;
  color: #f00;
}
</style>
