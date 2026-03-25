<template>
  <a-modal :open="visible" :title="form.id ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit" :width="900" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="标题" name="title">
            <a-input v-model:value="form.title" placeholder="请输入标题" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="投放类型" name="type">
            <SmartEnumSelect v-model:value="form.type" enumName="PIC_TYPE_ENUM" placeholder="投放类型" width="100%"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="有效时间" name="startTime">
            <a-range-picker v-model:value="date" @change="dateChange" valueFormat="YYYY-MM-DD HH:mm:ss" :showTime="true" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="排序" name="seq">
            <a-input-number v-model:value="form.seq" placeholder="请输入顺序" style="width: 100%" />
            <p class="seq-text">排序越大越靠前</p>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="图片" name="imgKey">
            <Upload
              accept="jpg, jpeg, png"
              listType="picture-card"
              :maxUploadSize="1"
              buttonText="点击上传图片"
              :default-file-list="form.imgKey"
              @change="imgKeyChange"
            />
          </a-form-item>
          <div class="smart-margin-left10" v-if="proposal">
            提示：<span class="color-warning">{{ proposal }}</span>
          </div>
        </a-col>
        <a-col :span="12">
          <a-form-item label="备注" name="remark">
            <a-textarea :rows="4" v-model:value="form.remark" type="textarea" placeholder="请输入备注" />
          </a-form-item>

        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, computed, inject } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { picApi } from '/@/api/business/pic/pic-api.js';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import Upload from '/@/components/upload/index.vue';

// ----------------------- 以下是字段定义 emits props ------------------------
let smartEnumPlugin = inject('smartEnumPlugin');

// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  id: null,
  title: null,
  type: null,
  seq: 0,
  remark: null,
  imgKey: null,
  startTime: '',
  endTime: '',
};
let form = reactive({ ...formDefault });
const rules = {
  title: [{ required: true, message: '请输入标题' }],
  type: [{ required: true, message: '请输入投放类型' }],
  imgKey: [{ required: true, message: '请输入图片' }],
  startTime: [{ required: true, message: '请输入有效时间' }],
};


const proposal = computed(() => {
  return '首页轮播图建议尺寸：702px * 278px';
});


// 是否展示
const visible = ref(false);


function imgKeyChange(fileList) {
  form.imgKey = fileList;
  formRef.value.validateFields('imgKey');
}
let date = ref();
function dateChange(dates, dateStrings) {
  form.startTime = dateStrings[0];
  form.endTime = dateStrings[1];
}

function showModal(rowData) {
  Object.assign(form, formDefault);
  if (rowData) {
    Object.assign(form, rowData);
    date.value = [form.startTime, form.endTime];
    form.id = rowData.picId;
  }
  visible.value = true;
}

function onClose() {
  Object.assign(form, formDefault);
  formRef.value.clearValidate();
  date.value = [];
  proposal.value = '';
  visible.value = false;
}

function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        if (form.id) {
          await picApi.update(form);
        } else {
          await picApi.add(form);
        }
        message.success(`${form.id ? '修改' : '添加'}成功`);
        emit('reloadList');
        onClose();
      } catch (error) {
        console.log(error);
      } finally {
        useSpinStore().hide();
      }
    })
    .catch((error) => {
      console.log('error', error);
      message.error('参数验证错误，请仔细填写表单数据!');
    });
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>
<style lang="less" scoped>
.color-warning {
  color: red;
}
.seq-text {
  color: #666;
}

.exp-img {
  width: 40px;
  height: 40px;
}

.exp-img:hover {
  width: 100px;
  height: 100px;
}
</style>
