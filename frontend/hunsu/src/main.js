import Vue from 'vue';
import App from './App.vue';
import vuetify from './plugins/vuetify';
import router from './router';
import 'vuetify/dist/vuetify.min.css';
import store from './store/store';
import axios from 'axios';
import VueAxios from 'vue-axios';
import VueCookies from 'vue-cookies';
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import 'chart.js';
import 'hchs-vue-charts';
// 아이콘 리스트
import { faHeart, faAngleRight } from '@fortawesome/free-solid-svg-icons';

library.add(faHeart, faAngleRight);
Vue.component('font-awesome-icon', FontAwesomeIcon);

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
