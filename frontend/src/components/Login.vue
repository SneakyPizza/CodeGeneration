<template>
  <section>

    <div class="container-lg">
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
import axios from "axios";
import {useStore} from "vuex";

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
    data() {
      return {
        store: useStore(),
      }
    },
    // login through a store action
    login() {
      //log in with axios
      // with a aplication json header
      axios
        .post("http://localhost:8080/login", {
          username: this.username,
          password: this.password,
        })
        .then((response) => {
          // login successful
          //   this.store.mutations.setToken(response.data.JWTtoken);
          //   this.store.mutations.setUser(response.data.id);
            // redirect to the home page
          console.log(response.data);
            this.$router.push("/");

        })
        .catch((error) => {
          // login was not successful
          // set the error message
          this.errorMessage = error.response.data.message;
        });

    },
  },
};
</script>

<style>
/*center .main */
.main {
  margin: 0 auto;
  max-width: 600px;
}

</style>
