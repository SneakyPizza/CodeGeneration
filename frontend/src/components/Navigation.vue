<template>
  <nav class="navbar navbar-expand-md mb-4">
    <div class="container-fluid">
      <ul class="navbar-nav me-auto mr-auto  mb-2 mb-md-0">

      <li>
           <router-link to="/AccountSearch" class="Dark_Blue nav-link" active-class="active">Accounts</router-link>
      </li>
<!--        logout-->
      <li>
           <router-link to="/Logout" class="Dark_Blue nav-link" active-class="active">Logout</router-link>
      </li>
<!--        v-if user from store has role admin-->
      <li v-if="user.roles[0] === 'admin' || user.roles[1] === 'admin'">
           <router-link to="/PostUser" class="Dark_Blue nav-link" active-class="active">Admin</router-link>
      </li>
      </ul>
    </div>
  </nav>
</template>

<script>

export default {
  name: "Navigation",
  data() {

  },
  computed:{
    user(){
      return this.$store.state.user;
    },
  },
}
</script>

<style>
.Dark_Blue {
  color: #002c62;
  font-weight: bold;
  font-size: 20px;
}
</style>