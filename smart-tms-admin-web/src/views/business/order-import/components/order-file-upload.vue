<template>
  <div class="clearfix">
    <a-upload
        accept=".xlsx,.xls"
        :before-upload="beforeUpload"
        :customRequest="customRequest"
        :file-list="fileList"
        :headers="{ 'x-access-token': useUserStore().getToken }"
        list-type="text"
        @change="handleChange"
        @remove="handleRemove"
    >
      <div v-if="fileList.length < props.maxUploadSize && showUploadBtn">
        <a-button>
          <upload-outlined/>
          {{ buttonText }}
        </a-button>
      </div>
    </a-upload>
    <a-alert v-if="_.isEmpty(importList) && !_.isEmpty(fileList)" type="error" show-icon class="smart-margin-top10 smart-margin-bottom10">
      <template #icon><smile-outlined /></template>
      <template #message>未监测到导入，请重新上传</template>
    </a-alert>
  </div>
</template>
<script setup>
import { computed, ref, watch } from 'vue';
import { message } from 'ant-design-vue';
import { fileApi } from '/@/api/business/file/file-api';
import { useUserStore } from '/@/store/modules/system/user';
import { useSpinStore } from '/@/store/modules/system/spin';
import { download } from '/@/lib/axios';
import { orderApi } from '/@/api/business/order/order-api';
import _ from "lodash";
// ========================
const props = defineProps({
  value: String,
  buttonText: {
    type: String,
    default: '点击上传',
  },
  showUploadBtn: {
    type: Boolean,
    default: true,
  },
  // 最多上传文件数量
  maxUploadSize: {
    type: Number,
    default: 1,
  },
  maxSize: {
    type: Number,
    default: 10,
  },
  // 上传的文件类型
  accept: {
    type: String,
    default: 'xlsx',
  },
  // 额外提示
  extraMsg: {
    type: String,
    default: null,
  },
});
const emit = defineEmits(['update:value', 'change']);

// ======================== 逻辑
const fileList = ref([]);
const previewUrl = ref('');

const uploading = ref(false);
let importList = ref([]);
const customRequest = async (options) => {
  useSpinStore().show();
  uploading.value = true;
  try {
    const formData = new FormData();
    formData.append('uploadFile', options.file);
    let res = await orderApi.importDoc(formData);
    let file = options.file;
    fileList.value.push(file);
    importList.value = res.data;
    emit('change', res.data);
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

</script>
<style lang="less" scoped>
.extra-msg {
  padding: 4px 0;
  color: #bfbfbf;
}
</style>
