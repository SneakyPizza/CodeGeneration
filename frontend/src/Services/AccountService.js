import axios from 'axios';
const url = 'https://api-inholland-bank.herokuapp.com/'
export default {
    getAccount(iban, token) {
        //create Authorization header from token
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        return axios
            .get(url + 'Accounts/' + iban)
            .then(response => response);
    },
    addAccount(data, token) {
        //create Authorization header from token
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        return axios
            .post(url + 'Accounts', data)
            .then(response => response);
    }
}