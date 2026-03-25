<template>
  <a-card :bordered="false" :hoverable="true" size="small">

    <a-form ref="formRef" :model="form">
      <div v-for="(item, index) in domainNameList" style="display: flex">
        <a-form-item :label="`域名${index + 1}`">
          <a-input v-model:value="domainNameList[index]" placeholder="请输入域名"  style="width: 400px"/>
        </a-form-item>
        <a-button @click="addDomain()" type="link">添加域名</a-button>
        <a-button @click="removeDomain(index)" type="link">删除域名</a-button>
      </div>
    </a-form>
    <a-button class="smart-margin-left10" size="small"
              type="primary" @click="saveConfig()">保存
    </a-button>
  </a-card>
</template>
<script setup>
import {reactive, onMounted, computed, ref} from 'vue';
import { enterpriseApi } from '/@/api/business/oa/enterprise-api';
import { SmartLoading } from '/@/components/smart-loading';

const props = defineProps({
  enterpriseId: {
    type: Number,
    default: null,
  },
  domainName: {
    type: String,
    default: '',
  },
});

let domainNameList = ref(['']);

const formDefault = {
  enterpriseId: props.enterpriseId,
  domainName: ''
};

const form = reactive({ ...formDefault });

const emit = defineEmits('reload');
async function saveConfig(){
  try {
    SmartLoading.show();
    form.domainName = domainNameList.value.join(",");
    await enterpriseApi.updateDomainName(form);
    emit('reload');
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

function addDomain(){
  domainNameList.value.push('');
}
function removeDomain(index){
  domainNameList.value.splice(index, 1);
}

onMounted(() => {
  domainNameList.value = props.domainName.split(",");
});


</script>
