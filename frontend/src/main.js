import { createApp } from 'vue'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'

import Home from './components/Home.vue';
import AccountSearch from './components/AccountSearch.vue'; 

const routes = [
    { path: '/', component: Home },
    { path: '/AccountSearch', component: AccountSearch },

];

const router = createRouter({
    "history": createWebHistory(),
    routes
})

const app = createApp(App);
app.use(router);
app.mount('#app');