import Vue from 'vue';
import Vuex from 'vuex';
import ootd from './module/ootdStore';
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: { ootd },
  plugins: [
    createPersistedState({
      paths: ['ootd'],
    }),
  ],
});
