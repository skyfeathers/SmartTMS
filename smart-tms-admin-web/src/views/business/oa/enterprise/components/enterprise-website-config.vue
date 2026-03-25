<template>
  <a-card :bordered="false" :hoverable="true" size="small">
    <a-form ref="formRef" :model="form">
      <SmartWangeditor ref="contentRef" :modelValue="form.websiteDesc" :height="300" :exclude-menu="['color']"/>
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
import SmartWangeditor from '/@/components/smart-wangeditor/index.vue';

const props = defineProps({
  enterpriseId: {
    type: Number,
    default: null,
  },
  websiteDesc: {
    type: String,
    default: '',
  },
});

const formDefault = {
  enterpriseId: props.enterpriseId,
  websiteDesc: props.websiteDesc
};

const form = reactive({ ...formDefault });
const contentRef = ref();
const emit = defineEmits('reload');

async function saveConfig(){
  try {
    SmartLoading.show();
    form.websiteDesc = contentRef.value.getHtml();
    await enterpriseApi.updateWebsiteDesc(form);
    emit('reload');
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

onMounted(() => {

});


</script>
