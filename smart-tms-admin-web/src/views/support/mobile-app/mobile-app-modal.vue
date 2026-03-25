<template>
  <a-modal :open="visibleFlag" title="移动APP" width="700px" @close="onClose" >

    <div>
      <pre>版本号：{{ versionNo }}</pre>
      <pre>更新描述：{{ updateDesc }}</pre>
      <div>链接：<a :href="link" target="_blank">{{ link }}</a></div>
    </div>

    <template #footer>
      <a-space>
        <a-button type="primary" @click="onClose">关闭</a-button>
      </a-space>
    </template>

  </a-modal>
</template>
<script setup>
  import { ref } from 'vue';

  const visibleFlag = ref(false);
  const versionNo = ref('');
  const updateDesc = ref('');
  const link = ref('');

  function show(data) {
    versionNo.value = data.versionNo;
    updateDesc.value = data.updateDesc;
    link.value = data.appFile[0].fileUrl;
    visibleFlag.value = true;
  }

  function onClose() {
    visibleFlag.value = false;
  }

  defineExpose({
    show,
  });
</script>
