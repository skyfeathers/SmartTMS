<template>
  <a-modal width="600px" :open="visible" title="导入" ok-text="确认" cancel-text="取消"
           @cancel="onClose" :footer="null">
    <!--    -->
      <div style="height:100px" class="clearfix">
        <a-upload
            accept=".xlsx,.xls"
            listType="picture-card"
            :before-upload="beforeUpload"
            :customRequest="customRequest"
            :file-list="fileList"
            :headers="{ 'x-access-token': useUserStore().getToken }"
            list-type="text"
            @change="handleChange"
            @remove="handleRemove"
        >
          <div v-if="fileList.length == 0">
            <a-button>
              <upload-outlined/>
              上传导入文件
            </a-button>
            <a-button type="link" @click="download">模板下载</a-button>
          </div>
        </a-upload>
      </div>

  </a-modal>
</template>
<script setup>
import { useUserStore } from '/@/store/modules/system/user';
import { useSpinStore } from '/@/store/modules/system/spin';
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { etcApi } from '/@/api/business/card/etc-api';
import { orderApi } from '/@/api/business/order/order-api';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

const fileList = ref([]);

function handleChange (info) {
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

function beforeUpload (file) {
  const isLimitSize = file.size / 1024 / 1024 < 10;
  if (!isLimitSize) {
    return message.error(`上传的文件必须小于10Mb`);
  }
  return isLimitSize;
}

function handleRemove () {

}

// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------
// 是否展示
const visible = ref(false);

function showModal () {
  visible.value = true;
}

function onClose () {
  visible.value = false;
  fileList.value = [];
}

async function customRequest (options) {
  useSpinStore().show();
  try {
    const formData = new FormData();
    formData.append('uploadFile', options.file);
    let res = await etcApi.importEtc(formData);
    let file = options.file;
    fileList.value.push(file);
    emit('reloadList');
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}
function download () {
  const fileName = '/ETC导入模板.xlsx';
  let downloadUrl = '';
  console.log(process.env.NODE_ENV)
  if (process.env.NODE_ENV == 'production') {
    downloadUrl = '/admin';
  }
  if (process.env.NODE_ENV == 'sit') {
    downloadUrl = '/manage';
  }
  window.open(`${downloadUrl}${fileName}`);
}
// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
