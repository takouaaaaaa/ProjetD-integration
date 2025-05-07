// src/main.js

import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // Assuming you have a router
// If you installed Font Awesome via npm
import '@fortawesome/fontawesome-free/css/all.min.css';

// Correct Bootstrap imports after installation
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

const app = createApp(App);
app.use(router); // Assuming you have a router
app.mount('#app');