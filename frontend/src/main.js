import { createApp } from 'vue'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'

import UserOverview from './components/UserOverview.vue';
import Login from "./components/Login";
import axios from "axios";
import store from "./store";
import TransactionHistory from "./components/TransactionHistory";


axios.defaults.baseURL = 'http://localhost:8080'

axios.defaults.headers.common['Authorization'] = `Bearer ${store.state.token}`;

const routes = [
    { path: '/UserOverview', component: UserOverview, meta: {reqToken: true, adminOnly: false, }},
    { path: '/login', component: Login, meta: {reqToken: false, adminOnly: false, }},
    { path: '/History/:iban', name: 'History', component: TransactionHistory, meta: {reqToken: true, adminOnly: false, params: true}},
];


const router = createRouter({
    "history": createWebHistory(),
    routes
})
router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.reqToken)) {
        if (!store.state.token) {
            next('/login');
        } else {
            next();
        }
    } else if (to.matched.some(record => record.meta.adminOnly)) {
        if (store.state.user.admin) {
            next();
        } else {
            next('/UserOverview');
        }
    }
    else if (!to.matched.some(record => record)){
        if (!store.state.token) {
            next('/login');
        } else {
            next('/UserOverview');
        }
    }
    else{
        next();
    }

});


const app = createApp(App);
app.use(router);
app.use(store)
app.mount('#app');