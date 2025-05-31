<template>
  <el-container class="login">
    <el-header>
      <h4 style="margin-top: 15px;font-size: 40px; font-family: 'Aa可爱被甜甜圈住了'; color: #FFFFFF">
        <img :src="logo" style="width: 50px; height: 50px; vertical-align: middle;"/>
        心语智疗
      </h4>
    </el-header>
    <el-main class="card">
      <el-card class="form">
        <h1 style="font-size: 36px; margin-bottom: 10px;font-family: 'Aa可爱被甜甜圈住了'" >欢迎登录</h1>
        <p><el-text class="mx-1" size="small">请输入您的账号密码</el-text></p>

        <form @submit.prevent="submit">
          <el-input v-model="account" type="text" autocomplete="off" placeholder="账号" style="margin: 10px 0"/>
          <el-input v-model="password" type="password" autocomplete="off" placeholder="密码" style="margin: 10px 0"/>
          <span class="button-group">
      <el-button type="primary" native-type="submit" plain>登录</el-button>
      <a href="/register"><el-button type="info" plain>注册</el-button></a>
    </span>
        </form>
      </el-card>
    </el-main>

  </el-container>
</template>

<script setup>
import {ref } from 'vue'
import ServerAPI from '../scripts/ServerAPI';
import {ElMessage} from 'element-plus'
import router from "@/router/index.js";
import logo from "@/components/icons/NoBackgroundLogo.png";

const account = ref('')
const password = ref('')

const submit = () => {

  if(account.value === '' ){
    ElMessage.error('账号不能为空')
  }else if (password.value === ''){
 ElMessage.error('密码不能为空')
  }else {

    ServerAPI.login(account.value, password.value, (user) => {

      if (JSON.parse(user).token != null) {
        router.push({
          name: 'home',
          params: {userId: account.value}
        })
      } else {
        ElMessage.error('账号或密码错误')
      }
    })
  }
}

</script>

<style scoped>

.login {
  height: 98.2vh;
  background-image: url("@/components/icons/BackgroundImg.jpg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.card{
  display: flex;
  justify-content: center;
  align-items: center;

}

.form {
  width: 550px;
  height: 450px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  text-align: center;
  padding-top: 40px;
  background-color: #F2F6FC;
}
.button-group {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-top: 50px;
  width: 100%;
  align-self: center;
}


</style>