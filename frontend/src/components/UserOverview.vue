<template>
  <section>
    <div class="container-lg">
      <h1 class="blue mt-3 mt-lg-3">Overview</h1>
      <div class="row mt-4">
        <div class="card">
          <div class="card-body">
            <div class="">
              <div class="round">
                <img class="payment_icon" src="../assets/icons/box-arrow-in-down.svg" alt="img">
              </div>
              <p class="left">Transaction</p>
            </div>
          </div>
        </div>
      </div>
      <div class="row mt-4">
        <div class="card shadow ">
          <div class="card-body">
            <h6 class="card-title">Current</h6>
            <h2><img class="small_img margin-right" src="../assets/icons/dollar.png" alt="">Payments </h2><p class=" price">&euro; 1000,01</p>
            <p class="left50px">User info plus IBAN <br>
            Limit &euro; moneys</p>
          </div>
          <div class="card-footer">
            <h4 class="p-2 blue">Show more <img class="Small_pointer" src="../assets/icons/caret-right.svg" alt=""></h4>
          </div>
        </div>
      </div>
      <div class="row mt-4">
        <div class="card shadow ">
          <div class="card-body">
            <h6 class="card-title">Savings</h6>
            <h2><img class="small_img margin-right" src="../assets/icons/saving-money.svg" alt="">Savings account</h2><p class=" price">&euro; 1000,01</p>
            <p class="left50px">User info plus IBAN <br>
              Limit &euro; moneys</p>
          </div>
          <div class="card-footer">
            <h4 class="p-2 blue">Show more <img class="Small_pointer" src="../assets/icons/caret-right.svg" alt=""></h4>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: "Home",
};
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
</style>