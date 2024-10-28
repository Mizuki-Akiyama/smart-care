<template>
    <div class="common-layout">
      <el-container>
        <el-header style="text-align: center;background-color: #0e1422; font-size: 40px; font-family: '华文新魏';color: #f1ede4;">Ollama Test</el-header>
        <el-container>
          <el-main style="background-color: #252831">
            <el-row class="title-row">
              <el-col :span="24" class="title-col">Chat</el-col>
            </el-row>
            <el-row>
              <el-col :span="24" style="background-color: #f2f2f2;min-height: 600px">
                <div v-for="dialog in dialogs"><span >{{dialog.user}}: {{dialog.content}}</span></div>
              </el-col>
            </el-row>
            <el-row style="padding-top: 10px">
              <el-col :span="20">
                <el-input v-model="input" style="width: 100%" placeholder="请输入" />
              </el-col>
              <el-col :span="4" style="text-align: center">
                <el-button type="primary" @click="send">发送</el-button>
              </el-col>
            </el-row>
            <el-row style="padding-top: 10px">
              <el-col :span="24"></el-col>
            </el-row>
            <el-row>
              <el-col :span="8" style="text-align: center">
                <el-button type="success" @click="saveDialogs">保存对话</el-button>
              </el-col>
              <el-col :span="8" style="text-align: center">
                <el-button type="info" @click="loadDialogs">加载对话</el-button>
              </el-col>
              <el-col :span="8" style="text-align: center">
                <el-button type="warning" @click="clearDialogs">清空对话</el-button>
              </el-col>
            </el-row>
          </el-main>
        </el-container>
      </el-container>
    </div>

</template>


<script setup>
import {ref, onMounted} from 'vue'
import ServerAPI from '../scripts/ServerAPI'

const dialogs = ref([])
const input = ref('')
const send = () => {
  dialogs.value.push({user: '你', content: input.value})
  ServerAPI.chat(input.value, data => {
    dialogs.value.push({user: '系统', content: data})
  })
  input.value = ''
}

const saveDialogs = () => {
  ServerAPI.save(JSON.stringify(dialogs.value), data => {
    console.log(data)
  })
}

const loadDialogs = () => {
  ServerAPI.load(data => {
    dialogs.value = JSON.parse(data)
  })
}

const clearDialogs = () => {
  dialogs.value = []
}

onMounted(() => {

})
</script>

<style>

.common-layout {
  height: 100vh;
}

.title-row {
  text-align: center;
}

.title-col {
  font-size: 37px;
  color: #f1ede4;
  margin: 10px 0;
  font-family: '华文新魏';
  background-color:#252831; /* 增加背景色 */
  border-radius: 8px; /* 增加圆角 */
  box-shadow: 0 2px 4px rgb(0, 0, 0); /* 增加阴影 */
  transition: box-shadow 0.3s ease, background-color 0.3s ease;
}

.title-col:hover {
  background-color: #252831; /* 鼠标悬浮时改变背景色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 鼠标悬浮时增加阴影 */
}
</style>