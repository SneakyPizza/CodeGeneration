import axios from 'axios';
const url = 'https://api-inholland-bank.herokuapp.com'
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