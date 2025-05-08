// src/main.js

import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; 
import '@fortawesome/fontawesome-free/css/all.min.css';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

const app = createApp(App);
app.use(router); 
app.mount('#app');