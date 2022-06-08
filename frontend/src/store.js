
import {createStore} from "vuex";


export default createStore({
//store JWT
    state: {
        user: null,
        token: null,
        authentication: false,
    },
    mutations: {
        setUser(user) {
            this.state.user = user;
        },
        setToken( token) {
            this.state.token = token;
        },
        setAuthentication( authentication) {
            this.state.authentication = authentication;
        }
    },
    actions: {},
    getters: {
        getUser(state) {
            return state.user;
        },
        getToken(state) {
            return state.token;
        },
        getAuthentication(state) {
            return state.authentication;
        }
    },
})