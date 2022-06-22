import axios from 'axios';
const url = 'https://api-inholland-bank.herokuapp.com/'
export default {
    getUser(id, token) {
        //create Authorization header from token
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        return axios
            .get(url + 'Users/' + id)
            .then(response => response);
    },
    updateUser(id, data, token) {
        //create Authorization header from token
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        return axios
            .put(url + 'Users/' + id, data)
            .then(response => response);
    },
    getAllUsers(token, limit, offset) {
        //create Authorization header from token
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        return axios
            .get(url + 'Users?limit=' + limit + '&offset=' + offset)
            .then(response => response);
    }
}