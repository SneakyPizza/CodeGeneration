import axios from 'axios';
const url = 'http://localhost:8080'
export default {
    getUser(id, token) {
        //create Authorization header from token
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        return axios
            .get(url + '/Users/' + id)
            .then(response => response);
    }
}