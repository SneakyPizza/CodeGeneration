<template>
<div>
    <div class="wrapper">
        <input type="search" v-model="search" placeholder="search accounts">
        <table class="results-table">
            <thead>
                <tr>
                    <th>id</th>
                    <th>name</th>
                    <th>payment IBAN</th>
                    <th>savings IBAN</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="result in filteredAccounts" :key="result.id">
                    <td>{{result.id}}</td>
                    <td>{{result.name}}</td>
                    <td>{{result.iban_payment}}</td>
                    <td>{{result.iban_savings}}</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
</template>

<script>

export default {

    name: "SearchResultList",

    data() {
        return {
            results: [
                // Hardcoded values, replace with data from api later.
                { id: 1, name: "Bob", iban_payment: "NL46RABO1234567891", iban_savings: "NL46RABO1234567892" },
                { id: 2, name: "Alice", iban_payment: "NL46RABO1234567893", iban_savings: "NL46RABO1234567894" },
                { id: 3, name: "Bill", iban_payment: "NL46RABO1234567895", iban_savings: "NL46RABO1234567896"}
            ],
            search: ""
        };
    },
    computed: {
        filteredAccounts: function(){
            return this.results.filter((result) => {
                //return true or false if account name contains the given text
                return result.name.match(this.search);
            });
        }
    }
};
</script>

<style>

</style>

