import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';

import ootd from './module/ootdStore';
import whatwear from './module/whatwearStore';
import user from './module/userStore';
import image from './module/imageStore';
import live from './module/liveStore';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: { ootd, whatwear, user, image, live },
  plugins: [
    createPersistedState({
      paths: ['ootd', 'whatwear', 'user', 'live', 'image'],
    }),
  ],
});
