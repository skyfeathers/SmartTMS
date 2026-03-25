<template>
  <a-modal :open="visible" cancel-text="取消" ok-text="确认" title="导入"
           width="600px" @cancel="onClose" @ok="onSubmit">
    <a-form ref="formRef">
      <a-form-item label="副卡所属公司">
        <EnterpriseSelect v-model:value="enterpriseId"  placeholder="请选择所属公司" width="100%" :defaultFlag="true"/>
      </a-form-item>

      <a-form-item label="燃料类型">
          <SmartEnumSelect v-model:value="fuelType" enumName="OIL_CARD_FUEL_TYPE_ENUM"
          placeholder="请选择燃料类型" width="100%"/>
      </a-form-item>

      <a-form-item label="导入文件">
        <div class="clearfix" style="height:100px">
          <a-upload
              :before-upload="beforeUpload"
              :customRequest="customRequest"
              :file-list="fileList"
              accept=".xlsx,.xls"
              list-type="text"
              @change="handleChange"
              @remove="handleRemove"
          >
            <div v-if="fileList.length == 0">
              <a-button>
                <upload-outlined/>
                上传导入文件
              </a-button>
            </div>
          </a-upload>
        </div>
        <a-button type="link" @click="download">模板下载</a-button>
      </a-form-item>
    </a-form>


  </a-modal>
</template>
<script setup>
import { useUserStore } from '/@/store/modules/system/user';
import { useSpinStore } from '/@/store/modules/system/spin';
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { oilApi } from '/@/api/business/card/oil-api';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';

import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
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
const enterpriseId = ref(null);
const fuelType = ref();
let uploadFile = null;

function showModal () {
  visible.value = true;
}

function onClose () {
  visible.value = false;
  fileList.value = [];
}

function customRequest (options) {
  uploadFile = options.file;
  fileList.value.push(options.file);
}

async function onSubmit(){
  if(!uploadFile){
    message.error('请选择导入文件')
    return;
  }
  useSpinStore().show();
  try {
    const formData = new FormData();
    formData.append('uploadFile', uploadFile);
    formData.append('enterpriseId', enterpriseId.value);
    formData.append('fuelType', fuelType.value);
    await oilApi.importSlaveCard(formData);

    emit('reloadList');
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

function download () {
  const fileName = '/油卡副卡导入模板.xlsx';
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
