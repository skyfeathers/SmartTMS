<!--
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-12 17:34:23
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-18
-->
<template>
  <div class="clearfix">
    <a-upload
      :accept="props.accept"
      :before-upload="beforeUpload"
      :customRequest="customRequest"
      :file-list="fileList"
      :headers="{ 'x-access-token': useUserStore().getToken }"
      :list-type="listType"
      :disabled="!showUploadBtn || uploading"
      @change="handleChange"
      :multiple="multiple"
      @preview="handlePreview"
      @remove="handleRemove"
    >
      <div v-if="fileList.length < props.maxUploadSize && showUploadBtn">
        <template v-if="listType == 'picture-card'">
          <PlusOutlined />
          <div class="ant-upload-text">
            {{ buttonText }}
          </div>
        </template>
        <template v-else>
          <a-button>
            <upload-outlined />
            {{ buttonText }}
          </a-button>
        </template>
      </div>
    </a-upload>
    <p class="extra-msg" v-if="extraMsg">{{ extraMsg }}</p>
    <a-image-preview-group v-if="previewVisible" :preview="{ visible:previewVisible, onVisibleChange: setVisible }">
      <a-image :src="previewUrl" style="display: none"/>
    </a-image-preview-group>
  </div>
</template>
<script setup>
import { computed, ref, watch } from 'vue';
import { message } from 'ant-design-vue';
import { fileApi } from '/@/api/business/file/file-api';
import { useUserStore } from '/@/store/modules/system/user';
import { useSpinStore } from '/@/store/modules/system/spin';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { download } from '/@/lib/axios';
// ========================
const props = defineProps({
  value: String,
  buttonText: {
    type: String,
    default: '点击上传附件',
  },
  showUploadBtn: {
    type: Boolean,
    default: true,
  },
  defaultFileList: {
    type: Array,
    default: () => [],
  },
  multiple: {
    type: Boolean,
    default: false,
  },
  // 最多上传文件数量
  maxUploadSize: {
    type: Number,
    default: 10,
  },
  maxSize: {
    type: Number,
    default: 10,
  },
  // 上传的文件类型
  accept: {
    type: String,
    default: '',
  },
  // 文件上传类型
  folder: {
    type: Number,
    default: FILE_FOLDER_TYPE_ENUM.COMMON.value,
  },
  // 上传列表的内建样式，支持三种基本样式 text, picture 和 picture-card
  listType: {
    type: String,
    default: 'text',
  },
  // 额外提示
  extraMsg: {
    type: String,
    default: null,
  },
});
const imgFileType = ['jpg', 'jpeg', 'png', 'gif'];
const emit = defineEmits(['update:value', 'change']);

// 重新修改图片展示字段
const files = computed(() => {
  if (!props.defaultFileList) {
    return [];
  }
  if (!Array.isArray(props.defaultFileList)) {
    console.error('defaultFileList is not array!!!');
    return [];
  }
  let res = [];
  if (props.defaultFileList && props.defaultFileList.length > 0) {
    props.defaultFileList.forEach((element) => {
      element.url = element.fileUrl;
      element.name = element.fileName;
      res.push(element);
    });
    return res;
  }
  return res;
});
// ======================== 逻辑
const previewVisible = ref(false);
const fileList = ref([]);
const previewUrl = ref('');

watch(
  files,
  (value) => {
    fileList.value = value;
  },
  {
    immediate: true,
  }
);

const uploading = ref(false);
const customRequest = async (options) => {
  useSpinStore().show();
  uploading.value = true;
  try {
    console.log(options);
    const formData = new FormData();
    formData.append('file', options.file);
    let res = await fileApi.uploadFile(formData, props.folder);
    let file = res.data;
    file.url = file.fileUrl;
    fileList.value.push(file);
    emit('change', fileList.value);
  } catch (e) {
    console.log(e);
  } finally {
    uploading.value = false;
    useSpinStore().hide();
  }
};

function handleChange(info) {
  let fileStatus = info.file.status;
  let file = info.file;
  if (fileStatus == 'removed') {
    let index = fileList.value.findIndex((e) => e.fileId == file.fileId);
    if (index != -1) {
      fileList.value.splice(index, 1);
      emit('change', fileList.value);
    }
  }
}

function handleRemove(file) {
  console.log(fileList.value);
}

function beforeUpload(file) {
  const isLimitSize = file.size / 1024 / 1024 < props.maxSize;
  if (!isLimitSize) {
    return message.error(`上传的文件必须小于${props.maxSize}Mb`);
  }
  return isLimitSize;
}

function handleCancel() {
  previewVisible.value = false;
}

const handlePreview = async (file) => {
  if (imgFileType.some((e) => e === file.fileType)) {
    previewUrl.value = file.url || file.preview;
    previewVisible.value = true;
  } else {
    download(file.fileName, file.fileUrl);
  }
};

function setVisible(value) {
  previewVisible.value = value;
}

function clear() {
  fileList.value = [];
}

defineExpose({
  clear,
});
</script>
<style lang="less" scoped>
:deep(.ant-upload-picture-card-wrapper) {
  display: flex;
}
.extra-msg {
  padding: 4px 0;
  color: #bfbfbf;
}
</style>
