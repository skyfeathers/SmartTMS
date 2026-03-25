<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 11:59:20
 * @LastEditTime: 2022-08-21
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-tree/role-tree-checkbox.vue
-->
<template>
  <div style="height: 542px; overflow: auto">
    <a-checkbox-group v-model:value="checkedData">
      <div class="checked-box">
        <ul>
          <!--li 菜单模块 start-->
          <RoleTreeMenu :tree="props.tree" :index="0" />
          <!--li 菜单模块 end-->
        </ul>
      </div>
    </a-checkbox-group>
  </div>
</template>
<script setup>
import { ref, watch } from 'vue';
import { useRoleStore } from '/@/store/modules/system/role';
import RoleTreeMenu from './role-tree-menu.vue';

let props = defineProps({
  tree: {
    type: Array,
    default: [],
  },
});
defineEmits('update:value');

let roleStore = useRoleStore();
let checkedData = ref();
watch(
  () => roleStore.checkedData,
  (e) => (checkedData.value = e),
  {
    deep: true,
  }
);
</script>
<style scoped lang="less">
@import './index.less';
</style>
