import { computed } from 'vue';
import { useAppConfigStore } from '/@/store/modules/system/app-config';

const helpDocFlag = computed(() => useAppConfigStore().$state.helpDocFlag);

function initWidth () {
  let xs = 24;
  let sm = 24;
  let md = 12;
  let lg = 12;
  let xl = 8;
  let xxl = 6;
  if (helpDocFlag.value) {
    xs = 24;
    sm = 24;
    md = 24;
    lg = 12;
    xl = 12;
    xxl = 8;
  }
  return {
    xs, sm, md, lg, xl, xxl
  };
}

export function formWidth () {
  return {
    initWidth
  };
}
