import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router/index.js'

// Tailwind CSS
import './assets/index.css'   // <-- путь исправлен

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.mount('#app')