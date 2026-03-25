<template>
  <a-card>
    <input type="file" id="uploadFile"/>
    <a-button @click="submit">提交</a-button>
    <div>
      {{uploadResult}}
    </div>
  </a-card>
</template>
<script setup>
import { ref } from 'vue';
import { getTokenFromCookie } from '/@/utils/cookie-util';

let uploadResult = ref('');
function submit () {
  let url = 'https://tms.zbyyd.com/admin-api/support/file/upload?folder=7';
  let xhr = new XMLHttpRequest();
  const token = getTokenFromCookie();
  xhr.open('POST', url);
  xhr.responseType = 'application/json';
  xhr.setRequestHeader('x-access-token', token);
  xhr.onreadystatechange = function () {
    console.log(xhr.responseText);
    if (xhr.readyState === 4 && xhr.status === 200) {
      uploadResult.value = xhr.responseText;
    }
  };

  let fd = new FormData();
  let fileList = document.getElementById('uploadFile').files;
  fd.append('file', fileList[0]);

  xhr.send(fd);

}

</script>

<style lang="css" scoped>
</style>
