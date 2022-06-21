<template>
  <section>

    <div class="container-lg mt-5">
      <h1 class="blue heading mt-3 mt-lg-3">Inholland bank</h1>
      <div class="row">
        <div class="card main">
          <div class="card-body">
            <h1 class="card-title">Sign Up User As Admin</h1>
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
                    <label for="inputUserstatus" class="form-label">User Status</label>
                    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownUserstatus" data-bs-toggle="dropdown" aria-expanded="false">
                      User Status
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="dropdownUserstatus">
                      <li><a class="dropdown-item" id="inputUserstatusActive" href="#">Active</a></li>
                      <li><a class="dropdown-item" id="inputUserstatusDisabled" href="#">Disabled</a></li>
                    </ul>
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
                  <div class="mb-3">
                    <label for="inputRole" class="form-label">Role</label>
                    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownRole" data-bs-toggle="dropdown" aria-expanded="false">
                      Role
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="dropdownRole">
                      <li><a class="dropdown-item" id="inputRoleUser" href="#">User</a></li>
                      <li><a class="dropdown-item" id="inputRoleAdmin" href="#">Admin</a></li>
                    </ul>
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
      userstatus: "",
      dayLimit: "",
      transactionLimit: "",
      role: "",
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
          userstatus: this.userstatus,
          dayLimit: this.dayLimit,
          transactionLimit: this.transactionLimit,
          role: this.role,
        }
        console.log(user);
        const response = await LoginService.postUser(user);
        if (response.data() != null) {
          await this.$router.push('/UserOverview');
        }
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
.main {
  margin: 0 auto;
  max-width: 600px;
}

</style>