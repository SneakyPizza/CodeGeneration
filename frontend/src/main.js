import { createApp } from 'vue'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'

import Home from './components/Home.vue';
import AccountSearch from './components/AccountSearch.vue';
import UserOverview from './components/UserOverview.vue';

const routes = [
    { path: '/', component: Home },
    { path: '/AccountSearch', component: AccountSearch },
    { path: '/UserOverview', component: UserOverview },
];

const router = createRouter({
    "history": createWebHistory(),
    routes
})

const app = createApp(App);
app.use(router);
app.mount('#app');