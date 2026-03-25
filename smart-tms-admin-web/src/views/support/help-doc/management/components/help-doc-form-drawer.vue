<!--
 * @Description: 帮助文档表单
 * @version: 
 * @Author: zhuoda
 * @Date: 2022-08-16 21:08:43
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-26
-->
<template>
  <a-drawer
    :title="formData.helpDocId ? '编辑' : '新建'"
    :open="visibleFlag"
    :width="1000"
    :footerStyle="{ textAlign: 'right' }"
    @close="onClose"
    :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="formData" :rules="formRules" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
      <a-form-item label="标题" name="title">
        <a-input v-model:value="formData.title" placeholder="请输入标题" />
      </a-form-item>
      <a-form-item label="目录" name="helpDocCatalogId">
        <HelpDocCatalogTreeSelect v-model:value="formData.helpDocCatalogId" style="width: 100%" />
      </a-form-item>
      <a-form-item label="作者" name="author">
        <a-input v-model:value="formData.author" placeholder="请输入作者" />
      </a-form-item>
      <a-form-item label="排序" name="sort">
        <a-input-number v-model:value="formData.sort" placeholder="值越小越靠前" />（值越小越靠前）
      </a-form-item>
      <a-form-item label="关联菜单">
        <MenuTreeSelect v-model:value="formData.relationIdList" ref="menuTreeSelect" />
      </a-form-item>
      <a-form-item label="公告内容" name="contentHtml">
        <SmartWangeditor ref="contentRef" :modelValue="formData.contentHtml" :height="300" />
      </a-form-item>
      <a-form-item label="附件">
        <Upload
          :defaultFileList="defaultFileList"
          :maxUploadSize="10"
          :folder="FILE_FOLDER_TYPE_ENUM.HELP_DOC.value"
          buttonText="上传附件"
          listType="text"
          extraMsg="最多上传10个附件"
          @change="changeAttachment"
        />
      </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>

<script setup>
import { reactive, ref, nextTick } from 'vue';
import { message, Modal } from 'ant-design-vue';
import lodash from 'lodash';
import { useSpinStore } from '/@/store/modules/system/spin';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { helpDocApi } from '/@/api/support/help-doc/help-doc-api';
import SmartWangeditor from '/@/components/smart-wangeditor/index.vue';
import Upload from '/@/components/upload/index.vue';
import HelpDocCatalogTreeSelect from './help-doc-catalog-tree-select.vue';
import MenuTreeSelect from '/@/components/menu-tree-select/index.vue';
import _ from 'lodash';

const emits = defineEmits(['reloadList']);

// ------------------ 显示，关闭 ------------------
// 显示
const visibleFlag = ref(false);
function showModal(helpDocId) {
  Object.assign(formData, defaultFormData);
  defaultFileList.value = [];
  if (helpDocId) {
    getDetail(helpDocId);
  }

  visibleFlag.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

// 关闭
function onClose() {
  visibleFlag.value = false;
}

// ------------------ 表单 ------------------

const formRef = ref();
const contentRef = ref();
const noticeFormVisibleModal = ref();

const defaultFormData = {
  helpDocId: undefined,
  helpDocCatalogId: undefined,
  title: undefined, // 标题
  author: undefined, // 作者
  sort: 0, // 排序
  attachment: [], // 附件
  relationIdList: [], //关联id集合
  contentHtml: '', // html内容
  contentText: '', // 纯文本内容
};

const formData = reactive({ ...defaultFormData });

const formRules = {
  title: [{ required: true, message: '请输入' }],
  helpDocCatalogId: [{ required: true, message: '请选择目录' }],
  author: [{ required: true, message: '请输入作者' }],
  sort: [{ required: true, message: '请输入排序' }],
  contentHtml: [{ required: true, message: '请输入内容' }],
};

// 查询详情
async function getDetail(helpDocId) {
  try {
    useSpinStore().show();
    const result = await helpDocApi.getDetail(helpDocId);
    const attachment = result.data.attachment;
    if (!lodash.isEmpty(attachment)) {
      defaultFileList.value = attachment;
    } else {
      defaultFileList.value = [];
    }
    Object.assign(formData, result.data);
    formData.relationIdList = result.data.relationList ? result.data.relationList.map((e) => e.relationId) : [];
  } catch (err) {
    console.log(err);
  } finally {
    useSpinStore().hide();
  }
}

// 点击确定，验证表单
async function onSubmit() {
  try {
    formData.contentHtml = contentRef.value.getHtml();
    formData.contentText = contentRef.value.getText();
    await formRef.value.validateFields();
    save();
  } catch (err) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
}

// 新建、编辑API
const menuTreeSelect = ref();

async function save() {
  try {
    useSpinStore().show();
    let param = _.cloneDeep(formData);
    let relationList = menuTreeSelect.value.getMenuListByIdList(formData.relationIdList);
    param.relationList = relationList.map((e) => Object.assign({}, { relationId: e.menuId, relationName: e.menuName }));

    if (param.helpDocId) {
      await helpDocApi.update(param);
    } else {
      await helpDocApi.add(param);
    }
    message.success('保存成功');
    emits('reloadList');
    onClose();
  } catch (err) {
    console.log(err);
  } finally {
    useSpinStore().hide();
  }
}

// ----------------------- 上传附件 ----------------------------
// 已上传的附件列表
const defaultFileList = ref([]);
function changeAttachment(fileList) {
  defaultFileList.value = fileList;
  formData.attachment = lodash.isEmpty(fileList) ? [] : fileList;
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>

<style lang="less" scoped>
.visible-list {
  display: flex;
  flex-wrap: wrap;
  .visible-item {
    padding-top: 8px;
  }
}
</style>
