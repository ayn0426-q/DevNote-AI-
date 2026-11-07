```vue
<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import { ref } from 'vue'
import avatar from '@/assets/default.png'
const uploadRef = ref()
import useUserInfoStore  from '@/stores/userinfo'
const userInfostore=useUserInfoStore();
import { userAvatarUpdetaService } from '@/api/user'
import { ElResult } from 'element-plus'


// 当前显示的头像地址
const imgUrl = ref(avatar)
const avatarUrl=ref('')

// 选择图片后执行
const onSelectFile = (uploadFile) => {
    // 创建本地预览地址
    imgUrl.value = URL.createObjectURL(uploadFile.raw)
    selectedFile.value=uploadFile.raw
}
import {ElMessage} from 'element-plus'
const selectedFile=ref(null)

const updateAwater=async()=>{
if(avatarUrl.value===''){
   ElMessage.error("请输入头像地址")
}else{
   const res= await userAvatarUpdetaService(avatarUrl.value)
   if(res.code===0){
    imgUrl.value=avatarUrl.value
    ElMessage.success("头像修改成功")
   }else{
    ElMessage.error("头像修改失败")
   }
}
}

</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>

        <el-row>
            <el-col :span="12">
                <el-upload
                    ref="uploadRef"
                    class="avatar-uploader"
                    :show-file-list="false"
                    :auto-upload="false"
                    :on-change="onSelectFile"
                >
                    <img :src="imgUrl" class="avatar" />
                </el-upload>

                <br />

                <el-button
                    type="primary"
                    :icon="Plus"
                    size="large"
                    @click="uploadRef.$el.querySelector('input').click()"
                >
                    选择图片
                </el-button>
                <el-input v-model="avatarUrl" placeholder="选择头像上传路径" />
                <el-button type="success" :icon="Upload" size="large"@click= "updateAwater">
                    上传头像
                
                </el-button>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
    :deep(.avatar) {
        width: 278px;
        height: 278px;
        display: block;
        object-fit: cover;
    }

    :deep(.el-upload) {
        border: 1px dashed var(--el-border-color);
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: var(--el-transition-duration-fast);
    }

    :deep(.el-upload:hover) {
        border-color: var(--el-color-primary);
    }
}
</style>
```
