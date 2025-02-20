<template>
  <div class="common-layout">
    <el-container>
      <el-header
          style="text-align: center;background-color: #0e1422; font-size: 40px; font-family: '华文新魏';color: #f1ede4;">
        <h4>心语智疗</h4>
      </el-header>
      <el-container>
        <el-main style="background-color: #252831">
          <el-row class="title-row">
            <el-col :span="24" class="title-col">对话</el-col>
          </el-row>
          <el-row>

            <el-col :span="24" style="background-color: #f2f2f2;min-height: 600px">
              <div v-for="dialog in dialogs"  id="dialog">
                  <span>{{dialog.user}}{{dialog.content}}</span>
              </div>

            </el-col>
          </el-row>
          <el-row style="padding-top: 10px">
            <el-col :span="20">
              <el-input v-model="input" style="width: 100%" placeholder="请输入要发送的内容，试着发送 ‘给我讲个笑话’"/>
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
              <el-button type="warning" @click="clearDialogs">清空历史</el-button>
            </el-col>
          </el-row>
        </el-main>
      </el-container>
    </el-container>
  </div>

</template>


<script setup>
import {onMounted, ref} from 'vue'
import ServerAPI from '../scripts/ServerAPI'

const dialogs = ref([])
const input = ref('')
const send = () => {
  let content = '';
  if (input.value === '') {
    input.value ='给我讲个笑话'
  }
  dialogs.value.push({user: '你:',content: input.value})
  dialogs.value.push({user: '系统：',content: content})
  ServerAPI.chat(input.value, data => {
    content += data;
    dialogs.value[dialogs.value.length - 1].content = content
  })
  input.value = ''
}

const saveDialogs = () => {
  ServerAPI.save(JSON.stringify(dialogs.value), data => {
    console.log(data)
    alert('保存成功！')
  })
}

const loadDialogs = () => {
  ServerAPI.load(data => {
    if (data !== '[]'){
    dialogs.value = JSON.parse(data)
    alert('加载成功！')
    }else {
      alert('没有历史记录！')
    }
  })
}

const clearDialogs = () => {
  if (confirm('此操作会丢失所有聊天记录，确定要清空历史记录吗？')){
    ServerAPI.clear(() => {
      dialogs.value = []
    })
  }
  alert('删除成功！')
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
  background-color: #252831; /* 增加背景色 */
  border-radius: 8px; /* 增加圆角 */
  box-shadow: 0 2px 4px rgb(0, 0, 0); /* 增加阴影 */
  transition: box-shadow 0.3s ease, background-color 0.3s ease;
}

.title-col:hover {
  background-color: #252831; /* 鼠标悬浮时改变背景色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 鼠标悬浮时增加阴影 */
}
</style>