import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import Axios from 'axios';

const getDefaultState = () => {
    return {
        token: '',
        user: Object,
    };
};
export default new Vuex.Store({
    strict: true,
    plugins: [createPersistedState()],
    state: getDefaultState(),
    getters: {
        getUser: state => {
            return state.user;
        },
        isLoggedIn: state => {
            return state.token;
        }

    },
    actions: {
        // eslint-disable-next-line no-unused-vars
        setToken: ({ commit, dispatch }, { token }) => {
            commit('SET_TOKEN', token);
            // set auth header
            Axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            // eslint-disable-next-line no-unused-vars
        },
        logout: ({ commit }) => {
            commit('RESET', '');
        }
        ,setUser: ({ commit }, { user }) => {
            commit('SET_USER', user);
            // set auth header
        }
    },
    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token;
        },
        SET_USER: (state, user) => {
            state.user = user;
        },
        RESET: state => {
            Object.assign(state, getDefaultState());
        }
    }

});