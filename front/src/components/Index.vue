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
            <el-divider border-style="hidden"/>
            <div>
              <el-scrollbar height="710px">

                <el-button size="large" color="#0e1422" style="width: 230px;" round
                           v-for="item in chat" :key="item"
                           @mouseenter="item.isHover = true"
                           @mouseleave="item.isHover = false"
                           >

                  <p class="text-item">
                     <span v-if="!item.isEditing" @click="loadDialogs(item.id)">
                    {{ item.title }}


                    <!--                    <el-popconfirm-->
                    <!--                        :icon="InfoFilled"-->
                    <!--                        width="220"-->
                    <!--                        icon-color="#626AEF"-->
                    <!--                        title="这会彻底删除聊天记录，确定要继续吗"-->
                    <!--                        @confirm="clearDialogs(item.id)"-->
                    <!--                        placement="right-start"-->
                    <!--                    >-->
                    <!--                      <template #reference>-->
                    <!--                        <el-button color="#0e1422" style="margin-left: 7px" size="small" v-show="item.isHover">-->
                    <!--                          <el-icon size="10px">-->
                    <!--                            <MoreFilled/>-->
                    <!--                          </el-icon>-->
                    <!--                        </el-button>-->
                    <!--                      </template>-->
                    <!--                    </el-popconfirm>-->

                    <el-dropdown size="small" placement="bottom-start" trigger="click">
                      <span>
                      <el-icon color="#0e1422" style="margin-left: 7px" v-show="item.isHover">
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


        <el-main style="background-color: #252831">
          <el-row class="title-row">
            <el-col :span="24" class="title-col">对话</el-col>
          </el-row>
          <el-row>
            <el-col :span="24" style="background-color: #f2f2f2;min-height: 685px">
              <el-scrollbar max-height="685px" ref="scrollRef">
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
          <el-row>
          </el-row>
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
  InfoFilled,
  MoreFilled,
  More,
  Operation,
  Tools, Edit
} from "@element-plus/icons-vue";
import index from "vuex";
import {ElMessage, ElMessageBox} from "element-plus";

const dialogs = ref([])
const chat = ref([])
const input = ref('')
let currentId = ref('')
let isProcessing = false

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
  dialogs.value.push({user: '你：', content: input.value})
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
    item.title = item.editingTitle // 临时更新标题
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

      currentId.value = data[0].id

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

onMounted(() => {
  loadAll()
})
</script>

<style>

.text-item {
  text-align: left;
  font-size: 13px;
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