<template>
  <Navigation />
  <section>
    <div class="container-lg">
      <h1 class="blue mt-3 mt-lg-3">All users</h1>
      <div class="row mt-4 ">
        <div v-if="errorMessage" class="alert alert-danger" role="alert">
          {{ errorMessage }}
        </div>
        <div class="input-group mb-3">
          <span class="input-group-text" id="basic-addon1">@</span>
          <input type="search" class="form-control" placeholder="name" v-model="search" aria-label="name" aria-describedby="basic-addon1">
        </div>
        <div class="list">
          <div class="card mt-3 " v-for="user in filteredUsers" v-bind:key="user">
            <div class="card p-3">
<!--              row with first and last Name-->
              <div class="row m-3">
                <div class="col-6">
                  <h4>First name: {{ user.firstName }} <br>Last name: {{ user.lastName }} <br/> Email: {{user.email}}</h4>
                </div>
                <div class="col-6">
                  <h4>Status: {{user.userstatus}}</h4>
                  <h4>Accounts:</h4> <h4 v-for="(Accounts,index) in user.Accounts" v-bind:key="Accounts">{{index + 1}}: {{Accounts}}</h4>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>

import UserService from "@/Services/UserService";
import Navigation from "@/components/Navigation.vue";

export default {
    components: {
      Navigation,
    },
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
            return this.Users.filter((Users) => {
                //return true or false if account name contains the given text
                return Users.firstName.match(this.search);
            });
        }
    },
  async created() {
    console.log("Home created");
    try {
      //get token from store
      const token = this.$store.state.token;
      //while loop to get all users
      var i = 1;
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
          if(i >1){
            j++;
          }
          i++;
          //if response cintains 50 users, get next 50 users
          if(resp.data.length !== 50) {
            bool = true;
          }
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
.list{
/*  overflow scroll*/
  max-height: 700px;
  overflow-y: scroll;
}
</style>

