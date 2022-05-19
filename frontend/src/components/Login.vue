<template>
<!-- login form -->
<div class="login-form">
  <div class="login-form__header">
    <h1 class="login-form__title">
      <img src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/142996/logo.svg" alt="Logo" />
    </h1>
    <p class="login-form__subtitle">
      Lorem ipsum dolor sit amet, consectetur adipisicing elit.
    </p>
  </div>
  <form class="login-form__form" @submit.prevent="login">
    <div class="login-form__input-wrapper">
      <label class="login-form__label" for="email">Email</label>
      <input class="login-form__input" type="email" id="email" v-model="email" />
    </div>
    <div class="login-form__input-wrapper">
      <label class="login-form__label" for="password">Password</label>
      <input class="login-form__input" type="password" id="password" v-model="password" />
    </div>
    <button class="login-form__button" type="submit">Login</button>
  </form>
</div>
</template>

<script>
export default {
  name: "login",
};
</script>

<style scoped>

</style>