import Vue from 'vue';
import App from './App.vue';
import vuetify from './plugins/vuetify';
import router from './router';
import 'vuetify/dist/vuetify.min.css';
import store from './store';
import axios from 'axios';
import VueAxios from 'vue-axios';
import VueCookies from 'vue-cookies';

import 'chart.js';
import 'hchs-vue-charts';

Vue.use(window.VueCharts);

Vue.use(VueAxios, axios);
Vue.use(VueCookies);
Vue.config.productionTip = false;
// window.Kakao.init('ea2498225f7ae036eec6397d152e3497');
// window.Auth.login({
//   success: (auth) => {

//   },
//   fail: (err)=> {
//     console.error(err)
//   }
// })
new Vue({
  vuetify,
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
