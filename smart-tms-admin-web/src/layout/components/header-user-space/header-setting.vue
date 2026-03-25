<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 20:27:11
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description:
-->

<template>
  <a-drawer :title="$t('setting.title')" placement="right" :open="visible" @close="close">
    <a-form layout="horizontal" :label-col="{ span: 8 }">
      <!-- <a-form-item label="语言/Language">
        <a-select v-model:value="formState.language" @change="changeLanguage" style="width: 120px">
          <a-select-option v-for="item in i18nList" :value="item.value">{{ item.text }}</a-select-option>
        </a-select>
      </a-form-item> -->
      <a-form-item :label="$t('setting.compact')">
        <a-radio-group v-model:value="formState.compactFlag" button-style="solid" @change="changeCompactFlag">
          <a-radio-button :value="false">默认</a-radio-button>
          <a-radio-button :value="true">紧凑</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="菜单布局">
        <a-radio-group @change="changeLayout" button-style="solid" v-model:value="formState.layout">
          <a-radio-button v-for="item in $smartEnumPlugin.getValueDescList('LAYOUT_ENUM')" :key="item.value" :value="item.value">
            {{ item.desc }}
          </a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="菜单宽度" v-if="formState.layout === LAYOUT_ENUM.SIDE.value">
        <a-input-number @change="changeSideMenuWidth" v-model:value="formState.sideMenuWidth" :min="1" />
        像素（px）
      </a-form-item>
      <a-form-item label="菜单主题">
        <a-radio-group v-model:value="formState.sideMenuTheme" button-style="solid" @change="changeMenuTheme">
          <a-radio-button value="dark">Dark</a-radio-button>
          <a-radio-button value="light">Light</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="标签页位置">
        <a-radio-group v-model:value="formState.pageTagLocation" button-style="solid" @change="changePageTagLocation">
          <a-radio-button value="top">顶部</a-radio-button>
          <a-radio-button value="center">中部</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="面包屑">
        <a-switch @change="changeBreadCrumbFlag" :disabled="formState.pageTagLocation == 'top'" v-model:checked="formState.breadCrumbFlag" checked-children="显示" un-checked-children="隐藏" />
      </a-form-item>
      <a-form-item label="标签页">
        <a-switch @change="changePageTagFlag" v-model:checked="formState.pageTagFlag" checked-children="显示" un-checked-children="隐藏" />
      </a-form-item>
      <a-form-item label="页脚">
        <a-switch @change="changeFooterFlag" v-model:checked="formState.footerFlag" checked-children="显示" un-checked-children="隐藏" />
      </a-form-item>
      <a-form-item label="网站名称">
        <a-input v-model:value="formState.websiteName" @change="changeField('websiteName',formState.websiteName)"/>
      </a-form-item>
    </a-form>
    <div class="footer">
      <a-button style="margin-right: 8px" type="primary" @click="copy">复制配置信息</a-button>
      <a-button type="block" danger @click="reset">恢复默认配置 </a-button>
    </div>
  </a-drawer>
</template>
<script setup>
  import { ref, reactive, watch } from 'vue';
  import { i18nList } from '/@/i18n/index';
  import { useI18n } from 'vue-i18n';
  import localStorageKeyConst from '/@/constants/local-storage-key-const';
  import { LAYOUT_ENUM } from '/@/constants/layout-const';
  import { localRead, localSave } from '/@/utils/local-util';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { message } from 'ant-design-vue';

  let appDefaultConfig = useAppConfigStore();
  // ----------------- modal 显示与隐藏 -----------------

  const visible = ref(false);
  defineExpose({
    show,
  });

  function close() {
    visible.value = false;
  }

  function show() {
    visible.value = true;
  }

  // ----------------- 配置信息操作 -----------------
  function copy() {
    let content = JSON.stringify(formState, null, 2);
    // 创建元素用于复制
    const aux = document.createElement('input');
    // 设置元素内容
    aux.setAttribute('value', content);
    // 将元素插入页面进行调用
    document.body.appendChild(aux);
    // 复制内容
    aux.select();
    // 将内容复制到剪贴板
    document.execCommand('copy');
    // 删除创建元素
    document.body.removeChild(aux);

    message.success('复制成功');
  }

  function reset() {
    for (const k in appDefaultConfig) {
      console.log(formState[k])
      formState[k] = appDefaultConfig[k];
    }
    console.log(appDefaultConfig)
    appConfigStore.reset();
  }

  // ----------------- 表单数据实时保存到localstorage -----------------

  const appConfigStore = useAppConfigStore();
  useAppConfigStore().$subscribe((mutation, state) => {
    localSave(localStorageKeyConst.APP_CONFIG, JSON.stringify(state));
  });

  // ----------------- 表单 -----------------

  let formValue = {
    // i18n 语言选择
    language: appConfigStore.language,
    // 布局: side 或者 side-expand
    layout: appConfigStore.layout,
    // 页面紧凑
    compactFlag: appConfigStore.compactFlag,
    // 侧边菜单宽度
    sideMenuWidth: appConfigStore.sideMenuWidth,
    // 菜单主题
    sideMenuTheme: appConfigStore.sideMenuTheme,
    // 标签页
    pageTagFlag: appConfigStore.pageTagFlag,
    // 面包屑
    breadCrumbFlag: appConfigStore.breadCrumbFlag,
    // 页脚
    footerFlag: appConfigStore.footerFlag,
    // 网站名称
    websiteName: appConfigStore.websiteName,
    //标签页位置
    pageTagLocation: appConfigStore.pageTagLocation,
  };

  let formState = reactive({ ...formValue });

  watch(()=>formState.pageTagLocation,()=>{
    if(formState.pageTagLocation == 'top'){
      appConfigStore.$patch({
        changeBreadCrumbFlag: false,
      });
      formState.breadCrumbFlag = false
    }else{
      appConfigStore.$patch({
        changeBreadCrumbFlag: true,
      });
      formState.breadCrumbFlag = true
    }
  },{
    immediate:true
  })

  const { locale } = useI18n();
  function changeLanguage(languageValue) {
    locale.value = languageValue;
    appConfigStore.$patch({
      language: languageValue,
    });
  }

  function changeCompactFlag(e) {
    appConfigStore.$patch({
      compactFlag: e.target.value,
    });
  }

  function changeLayout(e) {
    appConfigStore.$patch({
      layout: e.target.value,
    });
  }

  function changeSideMenuWidth(value) {
    appConfigStore.$patch({
      sideMenuWidth: value,
    });
  }
  function changeMenuTheme(e) {
    appConfigStore.$patch({
      sideMenuTheme: e.target.value,
    });
  }

  function changePageTagLocation(e) {
    appConfigStore.$patch({
      pageTagLocation: e.target.value,
    });
  }

  function changeBreadCrumbFlag(e) {
    appConfigStore.$patch({
      changeBreadCrumbFlag: e,
    });
  }

  function changePageTagFlag(e) {
    appConfigStore.$patch({
      pageTagFlag: e,
    });
  }

  function changeFooterFlag(e) {
    appConfigStore.$patch({
      footerFlag: e,
    });
  }

  function changeField (key, value) {
    let param = {};
    param[key] = value;
    appConfigStore.$patch(param);
  }
</script>
<style lang="less" scoped>
  .footer {
    position: absolute;
    right: 0;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e9e9e9;
    padding: 10px 16px;
    background: #fff;
    text-align: left;
    z-index: 1;
  }
</style>
