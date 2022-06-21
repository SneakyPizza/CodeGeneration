 import { createApp } from 'vue'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'

import UserOverview from './components/UserOverview.vue';
import Login from "./components/Login";
import axios from "axios";
import store from "./store";
import TransactionHistory from "./components/TransactionHistory";
import DoTransaction from "./components/DoTransaction";
import PostUser from "@/components/PostUser";
import Signup from "@/components/Signup";
 import UpdateUser from "@/components/UpdateUser";


axios.defaults.headers.common['Authorization'] = `Bearer ${store.state.token}`;

const routes = [
    { path: '/UserOverview', component: UserOverview, meta: {reqToken: true, adminOnly: false, }},
    { path: '/login', component: Login, meta: {reqToken: false, adminOnly: false, }},
    { path: '/History/:iban', name: 'History', component: TransactionHistory, meta: {reqToken: true, adminOnly: false, params: true}},
    { path: '/Transaction/:iban', name: 'Transaction', component: DoTransaction, meta: {reqToken: true, adminOnly: false, params: true}},
    { path: '/UpdateUser/:id', name: 'UpdateUser', component: UpdateUser, meta: {reqToken: true, adminOnly: true, params: true}},
    { path: '/PostUser', name: 'PostUser', component: PostUser, meta: {reqToken: true, adminOnly: true, params: true}},
    { path: '/signup', name: 'signup', component: Signup, meta: {reqToken: false, adminOnly: false, params: true}},
];


const router = createRouter({
    "history": createWebHistory(),
    routes
})
router.beforeEach((to, from, next) => {
    //else if to is /logout, logout and redirect to /login
     if (to.path === '/Logout') {
        store.commit('logout');
        next('/login');
    }
    if (to.path === '/signup') {
        next();
    }
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