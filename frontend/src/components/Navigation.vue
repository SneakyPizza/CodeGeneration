<template>
  <nav class="navbar navbar-expand-md mb-4">
    <div class="container-fluid">
      <ul class="navbar-nav me-auto mr-auto  mb-2 mb-md-0">

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
  data() {

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