import './assets/main.scss'
import { createPinia } from 'pinia'
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router/index.js'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import locale from 'element-plus/dist/locale/zh-cn.js'
const pinia = createPinia();

const app=createApp(App);
pinia.use(piniaPluginPersistedstate)
app.use(ElementPlus,{locale:locale});
app.use(router);
app.use(pinia);
app.mount('#app')

 