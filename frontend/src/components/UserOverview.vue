<template>
  <navigation />
  <section>
    <div class="container-lg">
      <h1 class="blue mt-3 mt-lg-3">Overview</h1>
<!--      v for account in account-->
      <div v-for="account in accounts" v-bind:key="account">
            <div class="row mt-4">
              <div class="card shadow mt-3">
                <div class="card-body">
                  <h6 class="card-title">{{account.accountType}}</h6>
                  <div v-if="account.accountType === 'savings'">
                    <h2><img class="small_img margin-right" src="../assets/icons/piggy-bank-fill.svg" alt="">Savings account</h2>
                  </div>
                  <div v-if="account.accountType === 'current'">
                    <h2><img class="small_img margin-right" src="../assets/icons/dollar.png" alt="">Payments account</h2>
                  </div>

                  <p class="left50px">{{account.IBAN}}<br>
                    Balance &euro; {{account.balance}}<br>
                    Limit &euro; {{user.transactionLimit}}</p>
                </div>
                <div class="">
                  <div class="right m-1">
                    <router-link :to="{name: 'Transaction', params: {iban: account.IBAN}}" class=" nav-link" active-class="active"><h4>Perform a payment</h4></router-link>
                  </div>
                </div>
                <div class="card-footer">
                  <router-link :to="{name: 'History', params: {iban: account.IBAN}}" class="blue bigText nav-link" active-class="active">View history<img class="Small_pointer" src="../assets/icons/caret-right.svg" alt=""></router-link>
                </div>
              </div>
            </div>
      </div>
    </div>
  </section>
</template>

<script>
import Navigation from './Navigation.vue';
import AccountService from "@/Services/AccountService";

export default {
  name: "Home",
  components: {
    Navigation,
  },
  computed:{
    user(){
      return this.$store.state.user;
    },
  },
  data() {
    return {
      total: 0,
      accounts: [],
    };
  },
  async created() {
    console.log("Home created");
    try {
      //get token from store
      const token = this.$store.state.token;
      //for account in this.user.accounts
      for (let account in this.user.Accounts) {
        const resp = await AccountService.getAccount(this.user.Accounts[account], token);
        this.accounts.push(resp.data);
      }

    }
    catch (error) {
      console.log(error + " in Home created");
    }
  }
}
</script>

<style>
.blue {
  color: #004391;
}
.small_img {
  width: 40px;
  height: 40px;
}
.margin-right {
  margin-right: 10px;
}
.left50px {
  margin-left: 50px;
}
.Small_pointer {
  filter: invert(16%) sepia(61%) saturate(6800%) hue-rotate(197deg) brightness(86%) contrast(106%);
  float: right;
  width: 30px;
  height: 30px;
}
.card-body {
  padding-left: 25px !important;
}
.card{
  border-radius: 10px !important;
  padding: 0 !important;
}
.price {
  font-size: 20px;
  font-weight: bold;
  float: right;
}
.card-footer:hover {
  cursor: pointer;
  background-color: #b6d0ea;
}
.round {

  background: #ffc0c0;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
}
.payment_icon {
  margin : auto;
  width: 40px;
  height: 40px;
}
.right {
  float: right;
}
.left {
  float: left;}

.bigText {
  font-size: 25px;
  font-weight: bold;
}
.nav-link{
  flex-direction: row !important;
}
</style>