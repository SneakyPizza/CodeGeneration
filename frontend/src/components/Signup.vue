<template>
  <section>
    <div class="container-lg mt-5">
      <h1 class="blue heading mt-3 mt-lg-3">Inholland bank</h1>
      <div class="row">
        <div class="card main_signUp">
          <div class="card-body">
            <h1 class="card-title">Sign Up</h1>
            <div class="row">
              <div class="col">
                <div v-if="errorMessage" class="alert alert-danger" role="alert">
                  {{ errorMessage }}
                </div>
                <form>
                  <div class="row">
                    <div class="col-6">
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
                            type="text"
                            class="form-control"
                            id="inputPassword"
                            v-model="password"
                        />
                      </div>
                      <div class="mb-3">
                        <label for="inputEmail" class="form-label">Email</label>
                        <input
                            type="email"
                            class="form-control"
                            id="inputEmail"
                            v-model="email"
                        />
                      </div>
                      <div class="mb-3">
                        <label for="inputFirstName" class="form-label">First Name</label>
                        <input
                            type="text"
                            class="form-control"
                            id="inputFirstName"
                            v-model="firstName"
                        />
                      </div>
                      <div class="mb-3">
                        <label for="inputLastName" class="form-label">Last Name</label>
                        <input
                            type="text"
                            class="form-control"
                            id="inputLastName"
                            v-model="lastName"
                        />
                      </div>
                    </div>
                    <div class="col-6">

                      <div class="mb-3">
                        <label for="inputStreet" class="form-label">Street</label>
                        <input
                            type="text"
                            class="form-control"
                            id="inputStreet"
                            v-model="street"
                        />
                      </div>
                      <div class="mb-3">
                        <label for="inputCity" class="form-label">City</label>
                        <input
                            type="text"
                            class="form-control"
                            id="inputCity"
                            v-model="city"
                        />
                      </div>
                      <div class="mb-3">
                        <label for="inputZipcode" class="form-label">Zipcode</label>
                        <input
                            type="text"
                            class="form-control"
                            id="inputZipcode"
                            v-model="zipcode"
                        />
                      </div>
                      <div class="mb-3">
                        <label for="inputDayLimit" class="form-label">Day Limit</label>
                        <input
                            type="number"
                            class="form-control"
                            id="inputDayLimit"
                            v-model="dayLimit"
                        />
                      </div>
                      <div class="mb-3">
                        <label for="inputTransaction" class="form-label">Transaction Limit</label>
                        <input
                            type="number"
                            class="form-control"
                            id="inputTransaction"
                            v-model="transactionLimit"
                        />
                      </div>
                    </div>
                  </div>

                  <button type="button" @click="postUser()" class="btn btn-primary">
                    Create user
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

export default {
  name: "PostUser",
  data() {
    return {
      username: "",
      password: "",
      email: "",
      firstName: "",
      lastName: "",
      street: "",
      city: "",
      zipcode: "",
      dayLimit: "",
      transactionLimit: "",
      errorMessage: null,
    };
  },
  methods: {
    async postUser() {
      try {
        const user = {
          username: this.username,
          password: this.password,
          email: this.email,
          firstName: this.firstName,
          lastName: this.lastName,
          street: this.street,
          city: this.city,
          zipcode: this.zipcode,
          dayLimit: this.dayLimit,
          transactionLimit: this.transactionLimit,
        }
        console.log(user);
        const response = await LoginService.signUp(user);
        if (response.data != null) {
          await this.$router.push('/login')
        }
        console.log(response.data);
      }
      catch (error) {
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
.main_signUp {
  margin: 0 auto;
  max-width: 1200px;
}

</style>