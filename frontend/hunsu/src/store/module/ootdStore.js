import axios from 'axios';
const state = {
  ootdInfo: {},
};
const getters = {
  getOotdInfo(state) {
    return state.ootdInfo;
  },
};
const mutations = {
  setOotdInfo(state, ootdInfo) {
    state.ootdInfo = ootdInfo;
  },
};
const actions = {
  getOotdInfoInApi(context, ootdIdx) {
    return axios.get(`http://i4c102.p.ssafy.io:8080/api/ootd/detail/${ootdIdx}`).then((res) => {
      // console.log('Vuex get OOtd ', res);
      context.commit('setOotdInfo', res.data);
    });
  },
  updateOotdInfo(context, ootdInfo) {
    return axios.put(`http://i4c102.p.ssafy.io:8080/api/ootd`, ootdInfo).then((res) => {
      console.log('in update', res);
      if (res.msg === 'success') {
        console.log('update 성공');
      } else {
        console.log('update 실패');
      }
    });
  },
};

export default {
  state: {
    ...state,
  },
  getters,
  mutations,
  actions,
};
