<!--
 * @Author: zhuoda
 * @Date: 2021-08-11 14:11:28
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description: 菜单新增编辑抽屉
 * @FilePath: /smart-admin/src/views/system/menu/components/menu-operate-modal.vue
-->
<template>
  <a-drawer
    :body-style="{ paddingBottom: '80px' }"
    :maskClosable="true"
    :title="form.menuId ? '编辑' : '添加'"
    :open="visible"
    :width="550"
    @close="onClose"
  >
    <a-form ref="formRef" :labelCol="{ span: labelColSpan }" :labelWrap="true" :model="form" :rules="rules">
      <a-form-item label="菜单类型" name="menuType">
        <a-radio-group v-model:value="form.menuType" button-style="solid">
          <a-radio-button v-for="item in MENU_TYPE_ENUM" :key="item.value" :value="item.value">
            {{ item.desc }}
          </a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item v-if="form.menuType != MENU_TYPE_ENUM.POINTS.value" :label="form.menuType === MENU_TYPE_ENUM.MENU.value ? '上级菜单' : '上级目录'">
        <MenuTreeSelect ref="parentMenuTreeSelect" v-model:value="form.parentId" />
      </a-form-item>
      <!--      目录 菜单 start   -->
      <template v-if="form.menuType == MENU_TYPE_ENUM.CATALOG.value || form.menuType == MENU_TYPE_ENUM.MENU.value">
        <a-form-item label="菜单名称" name="menuName">
          <a-input v-model:value="form.menuName" placeholder="请输入菜单名称" />
        </a-form-item>
        <a-form-item label="菜单图标" name="icon">
          <IconSelect @updateIcon="selectIcon">
            <template #iconSelect>
              <a-input v-model:value="form.icon" placeholder="请输入菜单图标" style="width: 200px" />
              <component :is="$antIcons[form.icon]" class="smart-margin-left15" style="font-size: 20px" />
            </template>
          </IconSelect>
        </a-form-item>
        <a-form-item v-if="form.menuType == MENU_TYPE_ENUM.MENU.value" label="路由地址" name="path">
          <a-input v-model:value="form.path" placeholder="请输入路由地址" />
        </a-form-item>
        <a-form-item label="排序" name="sort">
          <a-input-number v-model:value="form.sort" :max="9999" :min="0" placeholder="请输入排序" style="width: 100%" />
          <h6 style="color: #ababab">值越小越靠前</h6>
        </a-form-item>
        <template v-if="form.menuType == MENU_TYPE_ENUM.MENU.value">
          <a-form-item v-if="form.frameFlag" label="外链地址" name="frameUrl">
            <a-input v-model:value="form.frameUrl" placeholder="请输入外链地址" />
          </a-form-item>
          <a-form-item v-else label="组件地址" name="component">
            <a-input v-model:value="form.component" placeholder="请输入组件地址 默认带有开头/@/views" />
          </a-form-item>
        </template>

        <a-form-item v-if="form.menuType == MENU_TYPE_ENUM.MENU.value" label="是否缓存" name="cacheFlag">
          <a-switch v-model:checked="form.cacheFlag" checked-children="是" un-checked-children="否" />
        </a-form-item>
        <a-form-item v-if="form.menuType == MENU_TYPE_ENUM.MENU.value" label="是否外链" name="frameFlag">
          <a-switch v-model:checked="form.frameFlag" checked-children="是" un-checked-children="否" />
        </a-form-item>
        <a-form-item label="显示状态" name="frameFlag">
          <a-switch v-model:checked="form.visibleFlag" checked-children="显示" un-checked-children="隐藏" />
        </a-form-item>
        <a-form-item label="菜单状态" name="frameFlag">
          <a-switch v-model:checked="form.disabledFlag" checked-children="启用" un-checked-children="禁用" />
        </a-form-item>
        <a-form-item label="平台标识" name="platformFlag">
          <a-switch v-model:checked="form.platformFlag" checked-children="平台" un-checked-children="租户" />
        </a-form-item>
      </template>
      <!--      目录 菜单 end   -->
      <!--      按钮 start   -->
      <template v-if="form.menuType == MENU_TYPE_ENUM.POINTS.value">
        <a-form-item label="功能点名称" name="menuName">
          <a-input v-model:value="form.menuName" placeholder="请输入功能点名称" />
        </a-form-item>
        <a-form-item label="功能点关联菜单">
          <MenuTreeSelect ref="contextMenuTreeSelect" v-model:value="form.contextMenuId" />
        </a-form-item>
        <a-form-item label="前端权限字符" name="permsIdentifier">
          <a-input v-model:value="form.permsIdentifier" placeholder="请输入前端权限字符" />
        </a-form-item>
        <a-form-item label="功能点状态" name="frameFlag">
          <a-switch v-model:checked="form.disabledFlag" checked-children="启用" un-checked-children="禁用" />
        </a-form-item>
        <a-form-item label="接口权限(saAuth模式)" name="apiPermsList">
          <a-select v-model:value="form.apiPermsList" mode="multiple" placeholder="请选择接口权限" style="width: 100%">
            <a-select-option v-for="item in allUrlData" :key="item.name">{{ item.url }} </a-select-option>
          </a-select>
        </a-form-item>
      </template>
      <!--      按钮 end   -->
    </a-form>
    <div class="footer">
      <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
      <a-button style="margin-right: 8px" type="primary" @click="onSubmit(false)">提交 </a-button>
      <a-button v-if="!form.menuId" type="primary" @click="onSubmit(true)">提交并添加下一个 </a-button>
    </div>
  </a-drawer>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import { computed, nextTick, reactive, ref, watch } from 'vue';
  import MenuTreeSelect from './menu-tree-select.vue';
  import { menuApi } from '/@/api/system/menu/menu-api';
  import IconSelect from '/@/components/icon-select/index.vue';
  import { MENU_DEFAULT_PARENT_ID, MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
  import { useSpinStore } from '/@/store/modules/system/spin';

  // ----------------------- 以下是字段定义 emits props ------------------------
  // emit
  const emit = defineEmits(['reloadList']);

  // ----------------------- 展开、隐藏编辑窗口 ------------------------

  // 是否展示抽屉
  const visible = ref(false);

  const labelColSpan = computed(() => {
    if (form.menuType == MENU_TYPE_ENUM.POINTS.value) {
      return 6;
    }
    return 4;
  });

  watch(visible, (e) => {
    if (e) {
      getAuthUrl();
    }
  });

  const contextMenuTreeSelect = ref();
  const parentMenuTreeSelect = ref();

  //展开编辑窗口
  async function showDrawer(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
      if (form.parentId === MENU_DEFAULT_PARENT_ID) {
        form.parentId = null;
      }
    }
    visible.value = true;
    refreshParentAndContext();
  }

  function refreshParentAndContext() {
    nextTick(() => {
      if (contextMenuTreeSelect.value) {
        contextMenuTreeSelect.value.queryMenuTree();
      }
      if (parentMenuTreeSelect.value) {
        parentMenuTreeSelect.value.queryMenuTree();
      }
    });
  }

  // 隐藏窗口
  function onClose() {
    Object.assign(form, formDefault);
    formRef.value.resetFields();
    visible.value = false;
  }

  // ----------------------- 预加载数据 ------------------------

  let allUrlData = ref([]);

  // url数据
  async function getAuthUrl() {
    let res = await menuApi.getAuthUrl();
    allUrlData.value = res.data;
  }

  // ----------------------- form表单相关操作 ------------------------

  const formRef = ref();
  const formDefault = {
    menuId: undefined,
    menuName: undefined,
    menuType: MENU_TYPE_ENUM.CATALOG.value,
    icon: undefined,
    parentId: undefined,
    path: undefined,
    permsIdentifier: undefined,
    apiPermsList: undefined,
    sort: undefined,
    visibleFlag: true,
    cacheFlag: false,
    component: undefined,
    contextMenuId: undefined,
    disabledFlag: false,
    frameFlag: false,
    frameUrl: undefined,
    platformFlag: false,
  };
  let form = reactive({ ...formDefault });

  function continueResetForm() {
    refreshParentAndContext();
    const menuType = form.menuType;
    const parentId = form.parentId;
    const permsIdentifier = form.permsIdentifier;
    Object.assign(form, formDefault);
    formRef.value.resetFields();
    form.menuType = menuType;
    form.parentId = parentId;
    // 移除最后一个：后面的内容
    if (permsIdentifier && permsIdentifier.lastIndexOf(':')) {
      form.permsIdentifier = permsIdentifier.substring(0, permsIdentifier.lastIndexOf(':') + 1);
    }
  }

  const rules = {
    menuType: [{ required: true, message: '菜单类型不能为空' }],
    menuName: [
      { required: true, message: '菜单名称不能为空' },
      { max: 20, message: '菜单名称不能大于20个字符', trigger: 'blur' },
    ],
    frameUrl: [
      { required: true, message: '外链地址不能为空' },
      { max: 500, message: '外链地址不能大于500个字符', trigger: 'blur' },
    ],
    path: [
      { required: true, message: '路由地址不能为空' },
      { max: 100, message: '路由地址不能大于100个字符', trigger: 'blur' },
    ],
    permsIdentifier: [{ required: true, message: '前端权限字符不能为空' }],
  };

  function validateForm(formRef) {
    return new Promise((resolve) => {
      formRef
        .validate()
        .then(() => {
          resolve(true);
        })
        .catch(() => {
          resolve(false);
        });
    });
  }

  const onSubmit = async (continueFlag) => {
    let validateFormRes = await validateForm(formRef.value);
    if (!validateFormRes) {
      message.error('参数验证错误，请仔细填写表单数据!');
      return;
    }
    useSpinStore().show();
    try {
      let params = _.cloneDeep(form);
      // 若无父级ID 默认设置为0
      if (!params.parentId) {
        params.parentId = 0;
      }
      if (form.menuType === MENU_TYPE_ENUM.POINTS.value) {
        params.parentId = params.contextMenuId;
      }
      if (params.menuId) {
        await menuApi.updateMenu(params);
      } else {
        await menuApi.addMenu(params);
      }
      message.success(`${params.menuId ? '修改' : '添加'}成功`);
      if (continueFlag) {
        continueResetForm();
      } else {
        onClose();
      }
      emit('reloadList');
    } catch (error) {
      console.log(error);
    } finally {
      useSpinStore().hide();
    }
  };

  function selectIcon(icon) {
    form.icon = icon;
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showDrawer,
  });
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
