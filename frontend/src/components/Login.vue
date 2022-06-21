<template>
  <section>

    <div class="container-lg mt-5">
      <h1 class="blue heading mt-3 mt-lg-3">Inholland bank</h1>
      <div class="row">
        <div class="card main">
          <div class="card-body">
            <h1 class="card-title">Login</h1>
            <div class="row">
              <div class="col">
                <div v-if="errorMessage" class="alert alert-danger" role="alert">
                  {{ errorMessage }}
                </div>
                <form>
                  <div class="mb-3">
                    <label for="inputUsername" class="form-label">Username</label>
                    <input
                        id="inputUsername"
                        type="text"
                        class="form-control"
                        v-model="username"
                    />
                  </div>
                  <div class="mb-3">
                    <label for="inputPassword" class="form-label">Password</label>
                    <input
                        type="password"
                        class="form-control"
                        id="inputPassword"
                        v-model="password"
                    />
                  </div>
                  <button type="button" @click="login()" class="btn btn-primary">
                    Login
                  </button>
                </form>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import LoginService from "@/Services/LoginService";
import UserService from "@/Services/UserService";

export default {
  name: "Login",
  data() {
    return {
      username: "",
      password: "",
      errorMessage: null,
    };
  },
  methods: {
    // login through a store action
    async login() {
      try{
        const data = {
          username: this.username,
          password: this.password
        }
        console.log(data);
        const response = await LoginService.login(data)
        const token = response.data.JWTtoken;
        this.$store.dispatch('setToken', { token});
        const userId = response.data.Id;
        //get user
        const resp = await UserService.getUser(userId, token);
        const user = resp.data;
        this.$store.dispatch('setUser', { user });

        await this.$router.push('/UserOverview')
      } catch(error) {
        // login was not successful
        // set the error message
        this.errorMessage = error.response.data.message;
      }
    },
  },
};
</script>

<style>

.heading{
  font-weight: bold;
  font-size: 60px;
  text-align: center;
}
.main {
  margin: 0 auto;
  max-width: 600px;
}

</style>
