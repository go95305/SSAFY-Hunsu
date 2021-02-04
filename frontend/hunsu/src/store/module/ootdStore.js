import axios from 'axios';
const state = {
  ootdInfo: {},
  like: false,
};
const getters = {
  getOotdInfo(state) {
    return state.ootdInfo;
  },
  getLike(state) {
    return state.like;
  },
};
const mutations = {
  setOotdInfo(state, ootdInfo) {
    state.ootdInfo = ootdInfo;
  },
  toggleLike(state) {
    state.like = !state.like;
  },
};
const actions = {
  getOotdInfoInApi(context, ootdIdx) {
    return axios.get(`http://i4c102.p.ssafy.io:8080/api/ootd/detail/${ootdIdx}`).then((res) => {
      // console.log('Vuex get OOtd ', res);
      console.log(res.data);
      state.ootdInfo = res.data;
      context.commit('setOotdInfo', res.data);
    });
  },
  updateOotdInfo(context, ootdInfo) {
    return axios.put(`http://i4c102.p.ssafy.io:8080/api/ootd`, ootdInfo).then((res) => {
      console.log('in update', res);
      if (res.data === 'success') {
        console.log('update 성공');
        console.log(ootdInfo);
        context.commit('setOotdInfo', ootdInfo);
      } else {
        console.log('update 실패');
      }
    });
  },
  deleteOotdInfo(context, ootdIdx) {
    axios
      .delete(`http://i4c102.p.ssafy.io:8080/api/ootd/${ootdIdx}`)
      .then((res) => {
        console.log(res);
        if (res.data === 'success') {
          return true;
        } else {
          return false;
        }
      })
      .catch((err) => {
        console.log('delete error', err);
        return false;
      });
  },
  toggleLike({ commit, state }, nickname) {
    console.log(nickname);
    axios
      .put(`http://i4c102.p.ssafy.io:8080/api/ootd/like`, {
        nickname,
        ootdIdx: state.ootdInfo.ootdIdx,
      })
      .then((res) => {
        console.log(res);
        commit('toggleLike');
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
