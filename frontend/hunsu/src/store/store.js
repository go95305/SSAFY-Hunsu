import Vue from 'vue';
import Vuex from 'vuex';
import ootd from './module/ootdStore';
import whatwear from './module/whatwearStore';
import user from './module/userStore';
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: { ootd, whatwear, user },
  plugins: [
    createPersistedState({
      paths: ['ootd', 'whatwear'],
    }),
  ],
});
