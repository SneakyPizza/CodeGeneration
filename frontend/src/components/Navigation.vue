<template>
  <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
    <div class="container-fluid">
      <ul class="navbar-nav me-auto mr-auto  mb-2 mb-md-0">
        <li class="nav-item">
          <!-- Add a router link to the homepage (don't use the a tag!) -->
          <router-link to="/" class="nav-link" active-class="active"
            >Home</router-link
          >
        </li>
<!--        <li class="nav-item">   -->
<!--         <li v-if="!this.$store.getters.isAuthenticated" class="nav-item">-->
<!--          &lt;!&ndash; add a router link to the products page (don't use the a tag!) &ndash;&gt;-->
<!--          <router-link to="/login" class="nav-link" active-class="active"-->
<!--            >Login</router-link-->
<!--          >-->
<!--        </li>   -->
      <li>
           <router-link to="/AccountSearch" class="Dark_Blue nav-link" active-class="active">Accounts</router-link>
      </li>
      </ul>

    </div>
  </nav>
</template>

<script>
export default {
  name: "Navigation",
};
</script>

<style>
</style>