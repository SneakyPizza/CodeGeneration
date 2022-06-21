<template>
<div>
    <div class="wrapper">
        <input type="search" v-model="search" placeholder="search accounts">
        <table class="results-table">
            <thead>
                <tr>
                    <th>id</th>
                    <th>First name</th>
                    <th>Last name</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="result in filteredUsers" :key="result.id">
                    <td>{{result.id}}</td>
                    <td>{{result.firstName}}</td>
                    <td>{{result.lastName}}</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
</template>

<script>

import UserService from "@/Services/UserService";

export default {

    name: "AdminManageUsers",
    data() {
        return {
            Users: [
            ],
            search: ""
        };
    },
    computed: {
        filteredUsers: function(){
            return this.results.filter((Users) => {
                //return true or false if account name contains the given text
                return Users.name.match(this.search);
            });
        }
    },
  async created() {
    console.log("Home created");
    try {
      //get token from store
      const token = this.$store.state.token;
      //while loop to get all users
      var i = 0;
      var j = 0;
      var bool = false;
      while(!bool) {
        //get all users
        const resp = await UserService.getAllUsers(token, (50*i), (50*j));
        if(resp.data.length === 0) {
          bool = true;
        }
        else {
          this.Users = this.Users.concat(resp.data);
          if(i >0){
            j++;
          }
          i++;
        }
      }

    }
    catch (error) {
      console.log(error + " in Home created");
    }
  }
};
</script>

<style>

</style>

