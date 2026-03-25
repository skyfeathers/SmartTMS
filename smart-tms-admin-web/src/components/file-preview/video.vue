<template>
    <a-modal class="preview-main" :width="modelWidth + 'px'" :footer="null" :title="data.title || '预览'"
        v-model:visible="showModel" destroy-on-close>
        <div class="video-box">
            <video ref="videoCtx" style="width: 760px;" autoplay controls :src="fileUrl"></video>
        </div>
    </a-modal>
</template>


<script setup>
import { ref } from 'vue';
import _ from 'lodash';

let modelWidth = ref(500);
// 是否显示model
let showModel = ref(false);
let data = ref({});
let type = ref();
let videoCtx = ref();

let fileUrl = ref();
defineExpose({ openModal });

function openModal(e) {
    type.value = e.fileType;
    data.value = e;
    modelWidth.value = 800;
    fileUrl.value = e.fileUrl;
    showModel.value = true;
}
</script>
<style lang='less' scoped>
.preview-main {
    .video-box {
        width: 100%;
        // height: 600px;
        display: flex;
        align-content: center;
        justify-content: center;
    }
}

.img-box {
    text-align: center;
    margin: 0 auto;
}

.img-style {
    width: 760px;
}

.music-box {
    display: flex;
    padding: 0 20px;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: 200px;

    .poster {
        width: 120px;
        height: 120px;
        border: 2px rgb(30, 211, 172) solid;
        border-radius: 50%;
        overflow: hidden;

        img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            animation: turn 7s linear infinite;
        }
    }
}

@keyframes turn {
    0% {
        -webkit-transform: rotate(0deg);
    }

    25% {
        -webkit-transform: rotate(90deg);
    }

    50% {
        -webkit-transform: rotate(180deg);
    }

    75% {
        -webkit-transform: rotate(270deg);
    }

    100% {
        -webkit-transform: rotate(360deg);
    }
}

.iframe-box {
    width: 100%;
    height: 600px;
}
</style>