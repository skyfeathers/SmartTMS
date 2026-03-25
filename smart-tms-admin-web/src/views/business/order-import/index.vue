<template>
  <a-card>
    <a-row>
      <a-col :span="12">
        <FileUpload
            :maxUploadSize="1"
            @change="importAfter"
            :folder="FILE_FOLDER_TYPE_ENUM.SHIPPER.value"
        />
      </a-col>
    </a-row>
    <a-button type="link" @click="download">模板下载</a-button>
  </a-card>

  <a-card v-if="!_.isEmpty(normalList)">
    <template #title>
      正常数据
    </template>
    <template #extra>
      <a-button @click="confirmImport" type="link">确认导入</a-button>
    </template>
    <OrderImportTable :tableData="normalList"/>
  </a-card>

  <a-card v-if="!_.isEmpty(errorList)">
    <template #title>
      异常数据
    </template>
    <template #extra>
      <a-button @click="exportErrorList" type="link">导出并修改</a-button>
    </template>
    <OrderImportTable :tableData="errorList" :errorFlag="true"/>
  </a-card>

</template>
<script setup>
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import FileUpload from './components/order-file-upload.vue';
import OrderImportTable from './components/order-import-table.vue';
import { ref } from 'vue';
import _ from 'lodash';
import { defaultColumns } from './components/import-columns';
import { message } from 'ant-design-vue';
import { customExport } from '/@/utils/custom-export-util';
import { SmartLoading } from '/@/components/smart-loading';
import { orderApi } from '/@/api/business/order/order-api';
import { useUserStore } from '/@/store/modules/system/user';
import { useRoute, useRouter } from 'vue-router';

let errorList = ref([]);
let normalList = ref([]);
let importList = ref([]);

function importAfter (dataList) {
  dataList.forEach(item=>{
    item.errorMsgList = (item.errorMsg || '').split(',');
  })
  normalList.value = dataList.filter(e => !e.errorMsg);
  errorList.value = dataList.filter(e => e.errorMsg);
  importList.value = dataList;
}

async function confirmImport () {
  if (_.isEmpty(normalList.value)) {
    message.error('暂无需要导入的数据');
    return;
  }
  try {
    SmartLoading.show();
    await orderApi.importOrder(normalList.value);
    message.success('导入成功');
    normalList.value = [];
    onClose();
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

let route = useRoute();
let router = useRouter();

// 关闭
function onClose () {
  useUserStore().closePage(route, router);
}

function exportErrorList () {
  if (_.isEmpty(errorList.value)) {
    message.error('没有可供导出的异常数据');
    return;
  }
  customExport(errorList.value, defaultColumns, '异常数据列表.xlsx');
}

function download () {
  const fileName = '/订运单导入模板.xlsx';
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
</script>

<style lang="less" scoped>
</style>
