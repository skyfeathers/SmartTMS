<!--
 * @Author: zhuoda
 * @Date: 2021-12-03 23:22:28
 * @LastEditTime: 2022-07-27
 * @LastEditors: zhuoda
 * @Description:

-->
<template>
  <div class="login-container">
    <div class="login-website-name">
      {{ companyName }}
    </div>
    <div class="login-box">
      <div class="box-item login">
        <a-tooltip v-if="appDefaultConfig.dingdingLoginFlag" placement="top" title="点击切换登录方式" :open="true">
          <img class="login-qr" :src="appDefaultConfig.loginQr" @click="changeLoginType"/>
        </a-tooltip>

        <div class="login-title">欢迎登录{{websiteName}}</div>
        <a-form ref="formRef" class="login-form" :model="loginForm" :rules="rules" v-if="accountLoginFlag">
          <a-form-item name="loginName">
            <a-input v-model:value.trim="loginForm.loginName" placeholder="请输入用户名"/>
          </a-form-item>
          <a-form-item name="password">
            <a-input-password
                v-model:value="loginForm.password"
                autocomplete="on"
                :type="showPassword ? 'text' : 'password'"
                placeholder="请输入密码"
            />
          </a-form-item>
          <a-form-item name="captchaCode">
            <a-input class="captcha-input" v-model:value.trim="loginForm.captchaCode" placeholder="请输入验证码"/>
            <img class="captcha-img" :src="captchaBase64Image" @click="getCaptcha"/>
          </a-form-item>
          <a-form-item>
            <div class="btn" @click="handleFinish">登录</div>
          </a-form-item>
          <div class="code-info">
            <div class="code-info-item">
              <img src="/@/assets/images/login/code-h5.png" alt="" class="code-info-item-img" />
              <div class="code-info-item-text">移动端</div>
            </div>
            <div class="code-info-item">
              <img src="/@/assets/images/login/code-mp.png" alt="" class="code-info-item-img" />
              <div class="code-info-item-text">微信号</div>
            </div>
          </div>
        </a-form>
        <div v-else class="login-form dingding-login-qrcode">
          <div>
            <div class="sub-title">钉钉扫码登录</div>
            <div id="self_defined_element" class="self-defined-classname">
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="footer">
      <div>
        备案号：<span @click="toBeiAn">{{ appDefaultConfig.beiAnNo }}</span>版权所有© 2006{{
          copyYear
        }}.{{ appDefaultConfig.companyName }}版权所有
      </div>
    </div>
  </div>
</template>
<script setup>
import {message} from 'ant-design-vue';
import {computed, nextTick, onMounted, onUnmounted, reactive, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {loginApi} from '/@/api/system/login/login-api';
import {SmartLoading} from '/@/components/smart-loading';
import {LOGIN_DEVICE_ENUM} from '/@/constants/system/login-device-const';
import {useUserStore} from '/@/store/modules/system/user';
import {saveTokenToCookie} from '/@/utils/cookie-util';
import { localClear, localRead, localSave } from '/@/utils/local-util';
import { v4 as uuid } from 'uuid';
import localStorageKeyConst from '/@/constants/local-storage-key-const';

import {buildRoutes} from '/@/router/index';
import {useAppConfigStore} from '/@/store/modules/system/app-config';

let appDefaultConfig = useAppConfigStore();
const websiteName = computed(() => useAppConfigStore().websiteName);
const companyName = computed(() => useAppConfigStore().companyName);
const copyYear = computed(() => {

  return `-${new Date().getFullYear()}`;
});
//--------------------- 登录表单 ---------------------------------

const loginForm = reactive({
  loginName: '',
  password: '',
  captchaCode: '',
  captchaUuid: '',
  loginDevice: LOGIN_DEVICE_ENUM.PC.value,
  uniqueCode: ''
});



const rules = {
  loginName: [{required: true, message: '用户名不能为空'}],
  password: [{required: true, message: '密码不能为空'}],
  captchaCode: [{required: true, message: '验证码不能为空'}],
};

const showPassword = ref(false);
const router = useRouter();
let route = useRoute();
const formRef = ref();
const rememberPwd = ref(false);

let showMode = route.query.showMode;
if (showMode === 'preview') {
  loginForm.loginName = '13700000001';
  loginForm.password = '789321';
}


onMounted(() => {
  document.onkeyup = (e) => {
    if (e.keyCode == 13) {
      handleFinish();
    }
  };
});

onUnmounted(() => {
  document.onkeyup = null;
});

const loginInfo = ref();
async function handleFinish() {
  formRef.value.validate().then(async () => {
    try {
      SmartLoading.show();
      loginForm.uniqueCode = getUniqueCode();
      const res = await loginApi.login(loginForm);
      stopRefrestCaptchaInterval();
      saveTokenToCookie(res.data.token ? res.data.token : '');
      loginInfo.value = res
      getDualFactorLoginFlag()
    } catch (e) {
      if (e.data && e.data.code === 30001) {
        loginForm.captchaCode = '';
        getCaptcha();
      }
      console.log(e);
    } finally {
      SmartLoading.hide();
    }
  });
}

function getUniqueCode() {
  let uniqueCode = localRead(localStorageKeyConst.UNIQUE_CODE);
  if (uniqueCode === null) {
    uniqueCode = uuid();
    localSave(localStorageKeyConst.UNIQUE_CODE, uniqueCode);
  }

  return uniqueCode;
}

async function getDualFactorLoginFlag() {
  try {
    //更新用户信息到pinia
    useUserStore().setUserLoginInfo(loginInfo.value.data);
    //构建系统的路由
    buildRoutes();
    message.success('登录成功');
    if(loginInfo.value.data.forceEditPwdFlag){
      router.push({
        path: '/home',
        state: {
          forceEditPwdFlag: loginInfo.value.data.forceEditPwdFlag
        }
      });
    }else{
      router.push({
        path: '/home',
      });
    }
  } catch (e) {

  }
}

function onShowPassword() {
  showPassword.value = !showPassword.value;
}

//--------------------- 验证码 ---------------------------------

const captchaBase64Image = ref('');

async function getCaptcha() {
  try {
    SmartLoading.show();
    let captchaResult = await loginApi.getCaptcha();
    captchaBase64Image.value = captchaResult.data.captchaBase64Image;
    loginForm.captchaUuid = captchaResult.data.captchaUuid;
    beginRefrestCaptchaInterval(captchaResult.data.expireSeconds);
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

let refrestCaptchaInterval = null;

function beginRefrestCaptchaInterval(expireSeconds) {
  if (refrestCaptchaInterval === null) {
    refrestCaptchaInterval = setInterval(getCaptcha, (expireSeconds - 5) * 1000);
  }
}

function stopRefrestCaptchaInterval() {
  if (refrestCaptchaInterval != null) {
    clearInterval(refrestCaptchaInterval);
    refrestCaptchaInterval = null;
  }
}

// 跳转备案
function toBeiAn() {
  window.open('https://beian.miit.gov.cn')
}

// 切换登录方式
const accountLoginFlag = ref(true);

function changeLoginType() {
  accountLoginFlag.value = !accountLoginFlag.value;
  if (!accountLoginFlag.value) {
    nextTick(() => {
      // STEP3：在需要的时候，调用 window.DTFrameLogin 方法构造登录二维码，并处理登录成功或失败的回调。
      window.DTFrameLogin(
          {
            id: 'self_defined_element',
            width: 300,
            height: 300,
          },
          {
            redirect_uri: encodeURIComponent(appDefaultConfig.dingdingRedirectUri),
            client_id: appDefaultConfig.dingdingAppKey,
            scope: 'openid',
            response_type: 'code',
            state: 'xxxxxxxxx',
            prompt: 'consent',
          },
          (loginResult) => {
            debugger;
            console.log(loginResult)
            const {redirectUrl, authCode, state} = loginResult;
            // 这里可以直接进行重定向
            // window.location.href = redirectUrl;
            // 也可以在不跳转页面的情况下，使用code进行授权
            dingDingLogin(authCode);
            console.log(authCode);
          },
          (errorMsg) => {
            console.log(errorMsg)
            // 这里一般需要展示登录失败的具体原因
            message.error(`登录失败，${errorMsg}`);
          },
      );
    })
  }
}

async function dingDingLogin(authCode) {
  try {
    SmartLoading.show();
    const res = await loginApi.dingDingLogin(authCode);
    saveTokenToCookie(res.data.token ? res.data.token : '');
    message.success('登录成功');
    //更新用户信息到pinia
    useUserStore().setUserLoginInfo(res.data);
    //构建系统的路由
    buildRoutes();
    router.push('/home');
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

onMounted(() => {
  getCaptcha();
});
</script>
<style lang="less" scoped>
@import './login.less';
/* STEP2：指定这个包裹容器元素的CSS样式，尤其注意宽高的设置 */
.self-defined-classname {
  width: 300px;
  height: 300px;
}
</style>
