import axios from 'axios';
const url = 'http://localhost:8080'
export default {
    login(data) {
        return axios
            .post(url + '/login', data)
            .then(response => response);
    },
    signUp(data) {
        return axios
            .post(url + '/signup', data)
            .then(response => response);
    }
}