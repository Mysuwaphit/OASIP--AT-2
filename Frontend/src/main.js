import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import 'bootstrap/dist/css/bootstrap.min.css'
// import Emitter from 'tiny-emitter';
// import store from './store';

const app = createApp(App)
// app.config.globalProperties.$msalInstance = {};
// app.config.globalProperties.$emitter = new Emitter();
app.use(router).mount('#app')
