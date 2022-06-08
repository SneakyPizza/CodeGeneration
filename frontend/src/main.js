import { createApp } from 'vue'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'
import Home from './components/Home.vue';
import AccountSearch from './components/AccountSearch.vue';
import UserOverview from './components/UserOverview.vue';
import Login from "@/components/Login";
import Vuex, {useStore} from 'vuex'
import store from "@/store";

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
    if (!store.getters.getAuthentication) {
        if (to.path === '/login') {
            next();
        } else {
            next('/login');
        }
    } else {
        next();
    }
})

const app = createApp(App);
app.use(router);
app.use(Vuex);
app.use(useStore());
app.mount('#app');