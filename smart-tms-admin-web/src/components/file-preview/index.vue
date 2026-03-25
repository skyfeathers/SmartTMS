<template>
  <div>
    <template v-if="type == 'text'">
      <div v-for="(item, index) in fileList" class="file-name">
        <a :key="index" @click="preview(item, index)">
          <component v-if="mode != 'name'" :is="icon" />
          <span v-else>{{ item.fileName }}</span>
          <span v-if="index != fileList.length - 1" v-html="separator"></span>
        </a>
      </div>
    </template>
    <div :style="{
      height:type == 'text' ? 0 : 'auto'
    }">
      <a-image-preview-group :preview="{ visible, onVisibleChange: setVisible, current: previewCurrent }">
        <a-image v-for="(item, index) in fileList" :key="index" :src="item.fileUrl"
          :style="{ display: type == 'text' ? 'none' : '' }" />
      </a-image-preview-group>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, watch } from 'vue';
import { download } from '/@/lib/axios';

let props = defineProps({
  fileList: {
    type: Array,
    default: () => {
      return [];
    }
  },
  // 类型 text,picture
  type: {
    type: String,
    default: 'text'
  },
  mode: {
    type: String,
    default: 'name'
  },
  icon: {
    type: String,
    default: 'file-two-tone'
  },
  // 分隔符 可设置html标签 例如：<br/>
  separator: {
    type: String,
    default: '，'
  }
});
const imgFileType = ['jpg', 'jpeg', 'png', 'gif'];

// 文件预览
function preview(file, index) {
  if (imgFileType.some(e => e === file.fileType)) {
    previewCurrent.value = index;
    visible.value = true;
  } else {
    // download(file.fileName, file.fileUrl);
    window.open(file.fileUrl)
  }
}

// 预览受控
const visible = ref(false);
const previewCurrent = ref(0);

function setVisible(value) {
  visible.value = value;
}

</script>
<style lang='less' scoped>
.file-name {
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  word-break: break-all;
}

:deep(.ant-image img) {
  width: 80px;
  height: 80px;
}
</style>
