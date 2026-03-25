<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-05-30
 * @LastEditTime: 2022-08-25
 * @LastEditors: zhuoda
-->
<template>
  <div style="border: 1px solid #ccc">
    <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :defaultConfig="toolbarConfig"/>
    <Editor
      style="overflow-y: hidden"
      :style="{ height: `${height}px` }"
      v-model="editorHtml"
      :defaultConfig="editorConfig"
      @onCreated="handleCreated"
      @onChange="handleChange"
    />
  </div>
</template>
<script setup>
import { shallowRef, onBeforeUnmount, watch, ref } from 'vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { fileApi } from '/@/api/business/file/file-api';
import '@wangeditor/editor/dist/css/style.css';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
// ----------------------- 以下是公用变量 emits props ----------------
const editorHtml = ref();
let props = defineProps({
  modelValue: String,
  height: {
    type: Number,
    default: 500,
  },
  excludeMenu: {
    type: Array,
    default: [],
  },
});
watch(
    () => props.modelValue,
    (nVal) => {
      console.log(nVal);
      editorHtml.value = nVal;
    },
    {
      immediate: true,
      deep: true,
    }
);

//菜单
const editorConfig = { MENU_CONF: {} };
//上传
let customUpload = {
  async customUpload(file, insertFn) {
    try {
      const formData = new FormData();
      formData.append('file', file);
      let res = await fileApi.uploadFile(formData, FILE_FOLDER_TYPE_ENUM.COMMON.value);
      let data = res.data;
      insertFn(data.fileUrl);
    } catch (error) {}
  },
};
editorConfig.MENU_CONF['uploadImage'] = customUpload;
editorConfig.MENU_CONF['uploadVideo'] = customUpload;


const toolbarConfig = { excludeKeys: {} };
toolbarConfig.excludeKeys = props.excludeMenu;


// 获取编辑器实例html
const emit = defineEmits(['update:modelValue']);
const editorRef = shallowRef();
const handleCreated = (editor) => {
  editorRef.value = editor;
};
const handleChange = (editor) => {
  emit('update:modelValue', editorHtml.value);
};

function getHtml() {
  const htmlContent = editorRef.value.getHtml();
  return htmlContent === '<p><br></p>' ? '' : htmlContent;
}
function getText() {
  return editorRef.value.getText();
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
});

defineExpose({
  editorRef,
  getHtml,
  getText,
});

// ----------------------- 以下是业务内容 ----------------------------
</script>
<style scoped>
.w-e-full-screen-container {
  z-index: 9999 !important;
}
</style>
