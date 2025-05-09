<template>
  <div class="common-layout">
    <el-container>
      <el-header
          style="text-align: center;background-color: #0e1422; font-size: 40px; font-family: '华文新魏';color: #f1ede4;">
        <h4>心语智疗</h4>
      </el-header>


      <el-container>
        <el-aside width="250px"
                  style="text-align: center;background-color: #0e1422; font-size: 30px; font-family: '华文新魏';color: #f1ede4;">
          聊天历史
          <div>
            <el-button size="large" color="#626aef" class="new" round @click="newDialog">
              <el-icon class="el-icon--left" style="margin-right: 8px" size="17" color="#ffffff">
                <ChatLineSquare/>
              </el-icon>
              <p style="font-size: 15px">开启新对话</p>
            </el-button>

            <el-scrollbar height="710px">
              <div>
                <el-button :class="{new:isOn}" size="large" color="#0e1422" style="width: 230px;" round
                           v-for="item in chat" :key="item" @click="loadDialogs(item.id)">
                  <p class="text-item">
                    {{ item.title }}

                    <el-popconfirm
                        :icon="InfoFilled"
                        width="220"
                        icon-color="#626AEF"
                        title="这会彻底删除聊天记录，确定要继续吗"
                        @confirm="clearDialogs(item.id)"
                        placement="right-start"
                    >
                      <template #reference>
                        <el-button color="#0e1422" style="margin-left: 7px">
                          <el-icon>
                            <Delete/>
                          </el-icon>
                        </el-button>
                      </template>
                    </el-popconfirm>
                  </p>
                </el-button>
              </div>
            </el-scrollbar>
          </div>
        </el-aside>


        <el-main style="background-color: #252831">
          <el-row class="title-row">
            <el-col :span="24" class="title-col">对话</el-col>
          </el-row>
          <el-row>
            <el-col :span="24" style="background-color: #f2f2f2;min-height: 685px">
              <div v-for="dialog in dialogs" id="dialog" :key="index" class="dialog-item">
                <span class="grid-list">{{ dialog.user }}{{ dialog.content }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row style="padding-top: 10px">
            <el-col :span="20">
              <el-input v-model="input" size="large" placeholder="请输入要发送的内容，试着发送 ‘给我讲个笑话’"
                        @keyup.enter="send" clearable/>
            </el-col>
            <el-col :span="1"></el-col>
            <el-col :span="3">
              <el-button @click="send" size="large" style="font-size: 20px; border-radius: 2px; /* 增加圆角 */
  box-shadow: 0 2px 4px rgb(0, 0, 0); /* 增加阴影 */
  transition: box-shadow 0.3s ease, background-color 0.3s ease;">
                <el-icon style="">
                  <Promotion/>
                </el-icon>
              </el-button>
            </el-col>
          </el-row>
          <el-row style="padding-top: 10px">
            <el-col :span="24"></el-col>
          </el-row>
          <el-row>
          </el-row>
        </el-main>
      </el-container>
    </el-container>
  </div>

</template>


<script setup>
import {onMounted, ref} from 'vue'
import ServerAPI from '../scripts/ServerAPI'
import {ChatLineSquare, Delete, Promotion, InfoFilled} from "@element-plus/icons-vue";
import index from "vuex";


const dialogs = ref([])
const chat = ref([])
const input = ref('')
const isOn = ref(false)
let currentId = ref('')
let isProcessing = false

const isEmpty = () => {
  if (!currentId.value) {
    currentId.value = crypto.randomUUID()
    chat.value.push({
      id: currentId.value,
      title: input.value.slice(0, 8)
    })
  }
}

const send = () => {
  if (isProcessing) {
    return
  }

  let content = '';
  if (input.value === '') {
    input.value = '给我讲个笑话'
  }
  isProcessing = true
  isEmpty()
  dialogs.value.push({user: '你:', content: input.value})
  dialogs.value.push({user: '系统：', content: content})
  ServerAPI.chat(input.value, (data) => {
    content += data;
    dialogs.value[dialogs.value.length - 1].content = content
  }, () => {
    isProcessing = false
    saveDialogs()
  })

  input.value = ''


}

function saveDialogs() {
  ServerAPI.save(currentId.value, dialogs.value, data => {
  })
}

const loadDialogs = (chatId) => {
  if (!isProcessing) {
    ServerAPI.load(chatId, data => {
      currentId.value = chatId
      if (data !== '[]') {
        dialogs.value = JSON.parse(data)
      } else {
        dialogs.value = []
      }
    })
  }

}

const clearDialogs = (chatId) => {

  if (!isProcessing) {

    ServerAPI.clear(chatId, () => {
      dialogs.value = []
    })
    chat.value = chat.value.filter(item => item.id !== chatId)
    if (currentId.value === chatId) {
      currentId.value = null
      newDialog()
    }
  }
}


const newDialog = () => {
  if (!isProcessing) {

    dialogs.value = []
    input.value = ''
    currentId.value = null
  }

}

const loadAll = () => {
  ServerAPI.loadAll(data => {
    if (data.length > 0) {
      console.log(data)

      currentId.value = data[0].id

      for (let i = 0; i < data.length; i++) {
        chat.value.push({id: data[i].id, title: JSON.parse(data[i].content)[0].content})
      }

    }

  })
}

onMounted(() => {
  loadAll()
})
</script>

<style>

.text-item {
  text-align: center;
  font-size: 12px;
}

.new {
  font-size: 24px;
  font-family: 微软雅黑;
  width: 200px;
}


.common-layout {
  min-height: 100vh;

}

.title-row {
  text-align: center;
}

.title-col {
  font-size: 30px;
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

/* 每个对话项的样式 */
.dialog-item {
  border-bottom: 1px solid #d9d9d9; /* 可选：添加分隔线 */
}

/* 偶数行设置浅灰色背景 */
.dialog-item:nth-child(even) .grid-list {
  background-color: #eeeeed;
}

/* 奇数行保持白色背景 */
.dialog-item:nth-child(odd) .grid-list {
  background-color: #f2f2f2;
}

/* span 样式 */
.grid-list {
  display: block; /* 确保 span 占据整行 */
  padding: 10px;
  border-radius: 4px; /* 可选：圆角效果 */
}

.grid-list:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
</style>