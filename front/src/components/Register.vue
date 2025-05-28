<template>
  <el-container class="register">
    <el-header>
      <h4 style="margin-top: 15px;font-size: 40px; font-family: '华文新魏'">
        <img :src="logo" style="width: 50px; height: 50px; vertical-align: middle;"/>
        心语智疗
      </h4>
    </el-header>
    <el-main class="card">
      <el-card class="form">
        <el-form label-width="auto" style="max-width: 500px">
          <h1 style="font-size: 36px; margin-bottom: 10px">注册一个账号</h1>
          <br>
          <el-form-item label="账号:">
            <el-input v-model="user.userId" type="text" autocomplete="off"
                      placeholder="账号必须为纯数字且大于10位"
                      style="width: 250px"/>
          </el-form-item>

          <el-form-item label="密码:">
            <el-input v-model="user.password" type="password" autocomplete="off" placeholder="请输入8位以上的密码"/>
          </el-form-item>

          <el-form-item label="确认密码:">
            <el-input v-model="confirmPassword" type="password" autocomplete="off" placeholder="确认密码"/>
          </el-form-item>

          <el-form-item label="用户名:">
            <el-input v-model="user.userName" type="text" autocomplete="off" placeholder="请输入您的用户名"/>
          </el-form-item>

          <el-form-item style="gap: 70px;">
            <el-button type="primary" plain @click="submit">注册</el-button>
            <el-button type="info" plain @click="back">返回</el-button>
          </el-form-item>

        </el-form>
      </el-card>
    </el-main>

  </el-container>
</template>

<script setup>
import {reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'
import ServerAPI from '../scripts/ServerAPI';
import router from "@/router/index.js";
import logo from "@/components/icons/NoBackgroundLogo.png";

const user = reactive({
  userId: '',
  password: '',
  userName: '',
})
const confirmPassword = ref('')

const submit = () => {

  if (user.userId === '') {
    ElMessage.error('账号不能为空')
    return
  }

  if (!Number.isInteger(Number.parseInt(user.userId))) {
    ElMessage.error('账号必须为数字')
    return
  }

  if (user.userId.length < 10) {
    ElMessage.error('账号必须大于10位')
    return
  }

  if (!(user.password !== '')) {
    ElMessage.error('密码不能为空')
    return
  }

  if (user.password.length < 8) {
    ElMessage.error('密码必须大于8位')
    return
  }

  if (user.password !== confirmPassword.value) {
    ElMessage.error('两次密码不一致')
    return
  }

  if (user.userName === '') {
    ElMessage.error('用户名不能为空')
    return
  }

  const authUser = JSON.stringify(user)

  ServerAPI.register(authUser, () => {
    ElMessage.success('注册成功,两秒后自动返回登录')
    setTimeout(back,2000)
  })


}
const back = () => {
  router.push("/login")
}
</script>

<style scoped>

.register {
  height: 98.2vh;
  background: linear-gradient(65deg, rgb(121.3, 187.1, 255), rgb(235.9, 245.3, 255));
}

.card {
  display: flex;
  justify-content: center;
  align-items: center;
}

.form {
  width: 600px;
  height: 500px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  text-align: center;
  padding-top: 40px;
}


</style>