<template>
  <div class="common-layout">
    <el-container>

              <el-aside width="250px"
                        style="text-align: center;background-color: rgb(216.8, 235.6, 255); font-size: 40px; font-family: '华文新魏';color: #303133;">

<!--      <el-aside width="250px"-->
<!--                style="text-align: center; background: linear-gradient(to bottom, rgb(197.7, 225.9, 255), rgb(216.8, 235.6, 255)); font-size: 40px; font-family: '华文新魏';color: #303133;">-->

        <!--          <span style="-webkit-text-stroke: 1px rgb(159.5, 206.5, 255);">聊天历史</span>-->
        <h4 style="margin-top: 15px">
          <img :src="logo" style="width: 50px; height: 50px; vertical-align: middle;"/>
          心语智疗
        </h4>
        <el-divider border-style="hidden"/>
        <div>
          <el-button size="large" color="rgb(198,226,255)" class="new" style="border-radius: 12px;" @click="newDialog">
            <el-icon class="el-icon--left" style="margin-right: 8px" size="17" color="#303133">
              <ChatLineSquare/>
            </el-icon>
            <p style="font-size: 15px; color: #303133">开启新对话</p>
          </el-button>
          <div>
            <el-scrollbar height="710px">

              <el-button size="large" color="rgb(235.9, 245.3, 255)" style="width: 200px;border-radius: 12px;"
                         v-for="item in chat" :key="item"
                         @mouseenter="item.isHover = true"
                         @mouseleave="item.isHover = false"
              >

                <p class="text-item">
                     <span v-if="!item.isEditing" @click="loadDialogs(item.id)">
                    {{ item.title }}

                    <el-dropdown size="small" placement="bottom-start" trigger="click">
                      <span>
                      <el-icon color="rgb(159.5, 206.5, 255)" style="margin-left: 7px" v-show="item.isHover">
                        <MoreFilled/>
                      </el-icon>
                        </span>
                      <template #dropdown>
                        <el-dropdown-menu>
                          <el-dropdown-item >
                            <el-button @click="startEdit(item)" text>
                              <el-icon>
                                <Edit />
                              </el-icon>
                              重命名
                            </el-button>
                          </el-dropdown-item>
                          <el-dropdown-item >
                            <el-button @click="clearDialogs(item.id)" text>
                              <el-icon>
                                <Delete/>
                              </el-icon>
                              删除
                            </el-button>
                          </el-dropdown-item>
                        </el-dropdown-menu>
                      </template>
                    </el-dropdown>
                     </span>

                  <el-input
                      v-else
                      v-model="item.editingTitle"
                      size="default"
                      style="width: 180px"
                      @keyup.enter="confirmEdit(item)"
                      @blur="confirmEdit(item)"
                      @keyup.esc="cancelEdit(item)"
                      autofocus
                  />
                </p>


              </el-button>

            </el-scrollbar>
          </div>
        </div>
      </el-aside>

      <el-container>

        <!--      <el-header-->
        <!--          style="text-align: center;background: radial-gradient(circle, rgb(159.5, 206.5, 255),#ffffff); font-size: 40px; font-family: '华文新魏';color: #ffffff;">-->
<!--        <el-header-->
<!--            style="text-align: center;background-color: #2c3e50; font-size: 40px; font-family: '华文新魏';color: #ffffff;">-->

<!--          <h4>-->
<!--            <img :src="logo" style="width: 50px; height: 50px; vertical-align: middle;"/>-->
<!--            心语智疗-->
<!--          </h4>-->
<!--        </el-header>-->

        <el-main style="background: linear-gradient(to bottom, rgb(216.8, 235.6, 255), #ffffff)">
<!--          <el-main style="background-color: rgb(235.9, 245.3, 255)">-->
          <el-row class="title-row">
            <el-col :span="2"></el-col>
            <el-col :span="19" class="title-col">对话</el-col>
            <el-col :span="1"></el-col>
            <el-col :span="1">
<!--              <el-tooltip-->
<!--                  class="box-item"-->
<!--                  effect="dark"-->
<!--                  content="管理"-->
<!--                  placement="left"-->
<!--              >-->
              <el-button link style="width: 50px;height: 50px; margin-left: 20px; font-size: 20px" :icon="Operation" @click="config = true"/>
<!--              </el-tooltip>-->
            </el-col>
            <el-drawer
                v-model="config"
                title="用户信息管理"
                :before-close="handleClose"
                direction="rtl"
                class="demo-drawer"
                size="20%"
            >
              <div class="demo-drawer__content">
                <el-form :model="currentUser">
                  <el-form-item label="用户名:" width="200">
                    <el-input v-model="currentUser.userName"/>
                  </el-form-item>
                </el-form>
                <div class="demo-drawer__footer">
                  <el-button @click="cancelForm">取消</el-button>
                  <el-button type="primary" :loading="loading" @click="onClick">提交</el-button>
                </div>
              </div>
            </el-drawer>

            <el-col :span="1">
              <el-avatar
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
              />
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" style="min-height: 751px">
              <el-scrollbar max-height="751px" ref="scrollRef">
                <div v-for="dialog in dialogs" id="dialog" :key="index" class="dialog-item">
                  <div class="grid-list">
                    <span>
                      {{ dialog.user }}
                      <v-md-preview :text="dialog.content"></v-md-preview>
                    </span>
                  </div>
                </div>
              </el-scrollbar>
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
<!--          <el-row>-->
<!--          </el-row>-->
        </el-main>
      </el-container>
    </el-container>
  </div>

</template>


<script setup>
import {nextTick, onMounted, ref, watch} from 'vue'
import ServerAPI from '../scripts/ServerAPI'
import {
  ChatLineSquare,
  Delete,
  Promotion,
  MoreFilled,
  Edit, Operation
} from "@element-plus/icons-vue";
import index from "vuex";
import {ElMessage, ElMessageBox} from "element-plus";
import logo from '@/components/icons/NoBackgroundLogo.png'
import {useRoute} from "vue-router";


const currentUser = ref({})
let copyUser = {}
const route = useRoute()
const dialogs = ref([])
const chat = ref([])
const input = ref('')
let currentId = ref('')
let isProcessing = false
const config = ref(false)

const scrollRef = ref()

const autoScroll = () => {
  nextTick(() => {
    const scroll = scrollRef.value;
    scroll.wrapRef.scrollTop = scroll.wrapRef.scrollHeight;
    const wrap = scroll.$el.querySelector('.el-scrollbar__wrap');
    scroll.setScrollTop(wrap.scrollHeight);
  });
};

watch(dialogs, () => {
  autoScroll();
}, {deep: true});

const isEmpty = () => {
  if (!currentId.value) {
    currentId.value = crypto.randomUUID()
    chat.value.push({
      id: currentId.value,
      title: input.value.slice(0, 8),
      isHover: false
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
  dialogs.value.push({user: copyUser.userName + '：', content: input.value})
  dialogs.value.push({user: '系统：', content: content})
  ServerAPI.chat(currentId.value, input.value,(data) => {

    content += data;
    dialogs.value[dialogs.value.length - 1].content = content

  }, () => {
    isProcessing = false
    saveDialogs()
    autoScroll()
  })

  input.value = ''

}

const startEdit = (item) => {
  item.isEditing = true
  item.editingTitle = item.title // 初始化编辑文本
}

const confirmEdit = (item) => {
  if (item.editingTitle.trim()) {
    item.title = item.editingTitle.slice(0,8) // 临时更新标题
    item.isEditing = false

    ServerAPI.setTitle(item.id, item.editingTitle, () => {

    })
  }
  item.isEditing = false
}

const cancelEdit = (item) => {
  item.isEditing = false
  item.editingTitle = ''
}

function saveDialogs() {
  ServerAPI.save(currentId.value, dialogs.value, () => {
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

  ElMessageBox.confirm(
      '这会彻底删除聊天记录，确定要继续吗?',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        icon: 'warning',
      }
  )
      .then(() => {
        if (!isProcessing) {

          ServerAPI.clear(chatId, () => {
            dialogs.value = []
          })
          chat.value = chat.value.filter(item => item.id !== chatId)
          if (currentId.value === chatId) {
            currentId.value = null
            newDialog()
          }

          ElMessage({
            type: 'success',
            message: '删除成功',
          })
        }

      })

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

      for (let i = 0; i < data.length; i++) {
        const title = data[i].title
            ? data[i].title
            : JSON.parse(data[i].content)[0]?.content.slice(0, 8)

        chat.value.push({
          id: data[i].id,
          title: title,
          isHover: false
        })

      }

    }

  })
}


let timer
const loading = ref(false)

const onClick = () => {
  loading.value = true

  setTimeout(() => {

      ServerAPI.updateUser(currentUser.value, () => {
        console.log(currentUser.value)
        copyUser = currentUser.value
      })
      ElMessage.success('修改成功！')

    loading.value = false
    config.value = false
  }, 400)
}


const handleClose = () => {
  ElMessageBox.confirm(
      '确定要关闭窗口吗？', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      })
      .then(() => {
        cancelForm()
      })
}

const cancelForm = () => {
  config.value = false
  clearTimeout(timer)

}

onMounted(() => {
  ServerAPI.getUserById(route.params.userId, (user) => {
    currentUser.value = user
    copyUser = user
  })
  loadAll()
  newDialog()
})
</script>

<style>

.text-item {
  text-align: left;
  font-size: 13.5px;
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
  color: rgb(115.2, 117.6, 122.4);
  margin: 10px 0;
  font-family: '华文新魏';
  background-color: rgb(197.7, 225.9, 255); /* 增加背景色 */
  border-radius: 8px; /* 增加圆角 */
  box-shadow: 0 2px 4px rgb(0, 0, 0); /* 增加阴影 */
  transition: box-shadow 0.3s ease, background-color 0.3s ease;
}

.title-col:hover {
  background-color: rgb(216.8, 235.6, 255); /* 鼠标悬浮时改变背景色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 鼠标悬浮时增加阴影 */
}

/* 每个对话项的样式 */
.dialog-item {
  border-bottom: 1px solid #d9d9d9; /* 可选：添加分隔线 */
}

/* 偶数行设置浅灰色背景 */
.dialog-item:nth-child(even) .grid-list {
  background-color: #F2F6FC;
}

/* 奇数行保持白色背景 */
.dialog-item:nth-child(odd) .grid-list {
  background-color: rgb(235.9, 245.3, 255);
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