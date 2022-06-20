<template>
  <navigation />
  <section>
    <!--    Transaction history list-->
    <div class="container-lg">
      <h1 class="blue mt-3 mt-lg-3">Transaction history</h1>
      <div class="row mt-4 ">
        <div class="list">
            <div class="card mt-3 transaction" v-for="transaction in transactions" v-bind:key="transaction">
              <div class="row">
                <div class="col-3 ">
                  <p class="left  m-2">{{format_date(transaction.timestamp, 'DD-MM-YYYY')}} <br>
                  {{format_date(transaction.timestamp, 'HH:mm')}}</p>
                </div>
                <div class="col-3">
                  <p class=""><b>From:  </b>{{transaction.fromIBAN}}</p>
                  <p class=""><b>To:  </b>{{transaction.toIBAN}}</p>
                </div>
                <div class="col-3"></div>
                <div class="col-3 right">
                  <div v-if="this.user.Accounts[0] === transaction.toIBAN || this.user.Accounts[1] === transaction.toIBAN">
                    <h1 class="green mt-2">&euro; + {{transaction.amount }}</h1>
                  </div>
                  <div v-else >
                    <h1 class="red mt-2">&euro; - {{transaction.amount }}</h1>
                  </div>
                </div>
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
import moment from 'moment'

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
      transactions: [],
    };
  },
  async created() {
    console.log("I'm created");
    try {
      //get token from store
      const token = this.$store.state.token;
      const resp = await TransactionService.getHistory(this.$route.params.iban, token);
      this.transactions = resp.data;
    }
    catch (error) {
      console.log(error);
    }
  },
  methods: {
    format_date(value, format) {
      if (value) {
        return moment(String(value)).format(format);
      }
    },
  },
}
</script>

<style>
.list{
/*  overflow scroll*/
  max-height: 700px;
  overflow-y: scroll;
}

.red{
  color: red;
}
.green{
  color: green;
}
.blue {
  color: #004391;
}
.right {
  float: right;
}
.left {
  float: left;}

</style>