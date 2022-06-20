import axios from 'axios';
const url = 'https://api-inholland-bank.herokuapp.com'
export default {
    getHistory(iban, token) {
        //create Authorization header from token
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        const date1 = new Date();
        date1.setMonth(date1.getMonth() -3);

        const date2 = new Date();
        date2.setDate(date2.getDate() +1);

        return axios
            .get(url + '/Transactions/' + iban + "?dateONE=" + date1 + "dateTWO=" + date2)
            .then(response => response);
    }
}