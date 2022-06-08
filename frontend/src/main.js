import { createApp } from 'vue'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'
import Home from './components/Home.vue';
import AccountSearch from './components/AccountSearch.vue';
import UserOverview from './components/UserOverview.vue';
import Login from "@/components/Login";


const routes = [
    { path: '/', component: Home },
    { path: '/AccountSearch', component: AccountSearch },
    { path: '/UserOverview', component: UserOverview },
    { path: '/login', component: Login },
];


const router = createRouter({
    "history": createWebHistory(),
    routes
})
//router.push('/login'); if not authenticated
router.beforeEach((to, from, next) => {
    //if jwt token is not present, redirect to login page
    if (localStorage.getItem('token') == null && localStorage.getItem('user') == null && to.path !== '/login') {
        next("/login");
    }
    else{
        //if token is expired, redirect to login page
        if(new Date(localStorage.getItem("expires")) < new Date() && to.path !== '/login') {
            //delete token and user and expires from local storage
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            localStorage.removeItem('expires');
            //redirect to login page
            next("/login");
        }
        else{
            next();
        }
    }
})


const app = createApp(App);
app.use(router);
app.mount('#app');