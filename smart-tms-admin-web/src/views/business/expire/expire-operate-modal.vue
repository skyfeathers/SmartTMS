<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal :open="visible" title="提醒设置" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 12 }">
      <a-form-item v-for="item in typeList" :key="item.key" :label="item.desc" required>
        <a-input v-model:value="item.days" :placeholder="`请输入${item.desc}到期天数`">
          <template #suffix>
            天
          </template>
        </a-input>
      </a-form-item>

    </a-form>
  </a-modal>
</template>
<script setup>
import {nextTick, ref} from 'vue';
import {message} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {EXPIRED_CERTIFICATE_TYPE_ENUM} from '/@/constants/business/exipre-const';
import {expireApi} from '/@/api/business/expire/expire-api';
import _ from 'lodash';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

// 表单项
let typeList = ref([]);



// 是否展示
const visible = ref(false);

function showModal() {
  nextTick(()=>init());
  visible.value = true;
}

async function init() {
  useSpinStore().show();
  try {
    let responseModal = await expireApi.reminderTimeQuery();
    let data = responseModal.data;
    typeList.value = getTypeList(data.typeList);
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

function getTypeList(values){
  let list = [];
  Object.keys(EXPIRED_CERTIFICATE_TYPE_ENUM).map(key => {
    let type = EXPIRED_CERTIFICATE_TYPE_ENUM[key].value;
    let defaultValue = undefined;
    if (!_.isEmpty(values)){
      let find = values.find(item => type == item.type);
      if(find){
        defaultValue = find.days;
      }
    }
    list.push({
      key: key,
      type: EXPIRED_CERTIFICATE_TYPE_ENUM[key].value,
      desc: EXPIRED_CERTIFICATE_TYPE_ENUM[key].desc,
      days: defaultValue,
    })
  })
  return list;
}


function onClose() {
  typeList.value = [];
  visible.value = false;
}

async function onSubmit() {
  let find = typeList.value.find(item => item.days == null);
  if(find){
    message.error(`请设置${find.desc}到期天数`)
    return;
  }
  useSpinStore().show();
  try {
    let param = {typeList: typeList.value};
    await expireApi.reminderTimeUpdate(param);
    message.success('设置成功');
    emit('reloadList');
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
