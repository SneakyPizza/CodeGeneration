<template>
  <navigation />


  <router-view />
  <!-- Add a router view -->
</template>

<script>
import Navigation from './components/Navigation.vue';
import {useStore} from "vuex";

export default {
  name: "App",
  components: {
    Navigation,
  },
  data() {
    return {
      store: useStore(),
    }
  },

};
</script>

<style>
</style>
