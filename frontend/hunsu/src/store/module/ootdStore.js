import axios from 'axios';
const state = {
  ootdInfo: {},
  ootdList: [],
};
const getters = {
  getOotdInfo(state) {
    return state.ootdInfo;
  },
  getOotdList(state) {
    return state.ootdList;
  },
};
const mutations = {
  setOotdInfo(state, ootdInfo) {
    state.ootdInfo = ootdInfo;
  },
  setOotdList(state, ootdList) {
    state.ootdList = ootdList;
  },
};

const actions = {
  // Ootd 리스트 정렬
  getOotdListInApi(context, sort) {
    axios
      .get(`http://i4c102.p.ssafy.io:8080/api/ootd/${sort}`)
      .then((res) => {
        context.commit('setOotdList', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  getSearchedListInApi(context, hashtag) {
    axios
      .get(`http://i4c102.p.ssafy.io:8080/api/ootd/hashtag/search/${hashtag}`)
      .then((res) => {
        context.commit('setOotdList', res.data);
        console.log(res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  getOotdInfoInApi(context, ootdIdx) {
    return axios.get(`http://i4c102.p.ssafy.io:8080/api/ootd/detail/${ootdIdx}`).then((res) => {
      console.log(res.data);
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
};

export default {
  state: {
    ...state,
  },
  getters,
  mutations,
  actions,
};
