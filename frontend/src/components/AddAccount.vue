<template>
  <navigation />
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
                    <div class="mb-3">
                      <label for="inputAccountType" class="form-label">Account Type</label>
                      <select class="form-select" aria-label="Default select example" id="inputAccountType" v-model="accountType">
                        <option selected>Select Account Type</option>
                        <option value="savings">savings</option>
                        <option value="current">current</option>
                      </select>
                    </div>
                    <div class="mb-3">
                      <label for="inputActive" class="form-label">Active</label>
                      <select class="form-select" aria-label="Default select example" id="inputActive" v-model="active">
                        <option selected>Select Active</option>
                        <option value="active">active</option>
                        <option value="disabled">disabled</option>
                      </select>
                    </div>
                    <div class="mb-3">
                      <label for="inputAbsoluteLimit" class="form-label">Absolute Limit</label>
                      <input
                          type="number"
                          class="form-control"
                          id="inputAbsoluteLimit"
                          v-model="absoluteLimit"
                      />
                    </div>
                  </div>
                  <button type="button" @click="addAccount()" class="btn btn-primary">
                    Add Account
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
import AccountService from "@/Services/AccountService";
import Navigation from "@/components/Navigation";

export default {
  name: "AddAccount",
  components: {
    Navigation,
  },
  data() {
    return {
      accountType: "",
      active: "",
      absoluteLimit: "",
      errorMessage: null,
    };
  },
  methods: {
    async addAccount() {
      try {
        const account = {
          accountType: this.accountType,
          active: this.active,
          absoluteLimit: this.absoluteLimit,
          userid: this.$route.params.id,
        }
        console.log(account);
        const response = await AccountService.addAccount(account);
        if (response.data != null) {
          await this.$router.push("/UserOverview");
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
.main_signUp {
  margin: 0 auto;
  max-width: 1200px;
}

</style>