import axios from 'axios';
const url = 'http://localhost:8080'
export default {
    getAccount(iban, token) {
        //create Authorization header from token
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        return axios
            .get(url + '/Accounts/' + iban)
            .then(response => response);
    }
}