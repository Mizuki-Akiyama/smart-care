<template>
  <el-container class="tip">
    <el-main class="card">
      <el-card class="form">
        <el-result
            icon="error"
            title="Error Tip"
            sub-title="无效的用户，请重新登录"
        >
          <template #extra>
            <el-button type="primary" @click="jump">返回登录</el-button>
          </template>
        </el-result>
      </el-card>
    </el-main>

  </el-container>

</template>

<script setup>

import router from "@/router/index.js";

function jump(){
  router.push("/login")
}

</script>

<style scoped>
.tip {
  height: 98.2vh;
  background: linear-gradient(65deg, rgb(121.3, 187.1, 255), rgb(235.9, 245.3, 255));
}

.card{
  display: flex;
  justify-content: center;
  align-items: center;
}

.form {
  width: 450px;
  height: 350px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  text-align: center;
  padding-top: 40px;
}
</style>