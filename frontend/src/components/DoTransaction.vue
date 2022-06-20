<template>
  <navigation />
  <section>
    <!--    Transaction history list-->
    <div class="container-lg">
      <h1 class="blue middle ">Transaction from {{this.$route.params.iban}}</h1>
      <div class="card main1">
        <div class="card-body">
          <div class="row">
            <div class="col">
              <div v-if="errorMessage" class="alert alert-danger" role="alert">
                {{ errorMessage }}
              </div>
              <form>
                <div class="row mb-3">
                  <div class="col-6">
                    <label for="inputFromIban" class="form-label">From iban</label>
                    <input id="inputFromIban" type="text" :value="this.$route.params.iban" class="form-control" disabled readonly/>
                  </div>
                  <div class="col-6">
                      <label for="inputToIban" class="form-label">To Iban</label>
                      <input id="inputToIban" type="text" class="form-control" v-model="toIBAN" required/>
                  </div>
                </div>
                <div class="row mb-3">
                  <div class="col-6">
                    <label for="inputAmount" class="form-label">Amount</label>
                    <input id="inputAmount" type="number" min="1" step="1" class="form-control" v-model="amount" required/>
                  </div>
<!--                  pincode input hidden-->
                  <div class="col-6">
                    <label for="inputPincode" class="form-label">Pincode</label>
                    <input id="inputPincode" type="password" class="form-control" v-model="pincode" required/>
                  </div>
                </div>
                <button type="button" @click="doTransaction()" class="btn btn-primary">
                  Confirm
                </button>
              </form>
            </div>

          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import Navigation from './Navigation.vue';
import TransactionService from "@/Services/TransactionService";
import AccountService from "@/Services/AccountService";

export default {
  name: "transaction",
  components: {
    Navigation,
  },
  data() {
    return {
      errorMessage: null,
      account: null,
      amount: null,
      fromIBAN: "",
      fromUserId: "",
      pincode: "",
      toIBAN: ""
    };
  },
  computed:{
    user(){
      return this.$store.state.user;
    },
  },
  async created() {
    console.log("I'm created");
    try {
      //get token from store
      const token = this.$store.state.token;
      //get account from $route.params.iban
      const resp = await AccountService.getAccount(this.$route.params.iban, token);
      this.account = resp.data;
      console.log(this.account);
    } catch (error) {
      console.log(error);
    }
  },
  methods: {
    async doTransaction() {
      try {
        console.log("doTransaction");
        const transaction = {
          fromIBAN: this.$route.params.iban,
          fromUserId: this.user.Id,
          pincode: this.pincode,
          toIBAN: this.toIBAN,
          amount: this.amount,
        };
        console.log(transaction);
        //get token from store
        const token = this.$store.state.token;
        console.log(token);
        //get account from $route.params.iban
        const resp = await TransactionService.doTransaction(transaction, token);
        console.log(resp.data);
        //go to history page
        await this.$router.push({ name: 'History', params: { iban: this.$route.params.iban } });

      } catch (error) {
        this.errorMessage = error.response.data.message;
        console.log(error);
      }
    },
  },
}
</script>

<style>

.blue {
  color: #004391;
}
.right {
  float: right;
}
.left {
  float: left;}

.middle{
  text-align: center;
}

.main1{
  margin-top: 40px;
}
</style>