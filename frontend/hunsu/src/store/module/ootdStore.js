import axios from 'axios';
const state = {
  ootdInfo: {},
  ootdList: [],
  like: false,
};
const getters = {
  getOotdInfo(state) {
    return state.ootdInfo;
  },
  getOotdList(state) {
    return state.ootdList;
  },
  getLike(state) {
    return state.like;
  },
};
const mutations = {
  setOotdInfo(state, ootdInfo) {
    state.ootdInfo = ootdInfo;
  },
  setOotdList(state, ootdList) {
    state.ootdList = ootdList;
  },
  updateOotdInfo(state, ootdInfo) {
    state.ootdInfo.content = ootdInfo.content;
    state.ootdInfo.hashtagList = ootdInfo.hashtagList;
  },
  toggleLike(state, flag) {
    state.ootdInfo.likeChk = flag;
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
  getOotdInfoInApi(context, info) {
    // ootd 디테일 가져오기
    console.log(info.ootdIdx, info.nickname);
    return axios
      .get(`http://i4c102.p.ssafy.io:8080/api/ootd/detail/${info.ootdIdx}/${info.nickname}`)
      .then((res) => {
        // console.log('Vuex get OOtd ', res);
        console.log('getOotdInfo', res);
        state.ootdInfo = res.data;
        context.commit('setOotdInfo', res.data);
      });
  },
  updateOotdInfo(context, ootdInfo) {
    // ootd 수정
    return axios.put(`http://i4c102.p.ssafy.io:8080/api/ootd`, ootdInfo).then((res) => {
      console.log('in update', res);
      if (res.data === 'success') {
        console.log('update 성공');
        console.log(ootdInfo);
        context.commit('updateOotdInfo', ootdInfo);
      } else {
        console.log('update 실패');
      }
    });
  },
  deleteOotdInfo(context, ootdIdx) {
    // ootd 삭제
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
    // 좋아요 토글
    console.log(nickname);
    axios
      .put(`http://i4c102.p.ssafy.io:8080/api/ootd/like`, {
        nickname,
        ootdIdx: state.ootdInfo.ootdIdx,
      })
      .then((res) => {
        // console.log(res);
        commit('toggleLike', res.data);
      });
  },
  createOotdInfo(context, params) {
    // ootd 작성
    axios
      .post('http://i4c102.p.ssafy.io:8080/api/ootd', params)
      .then((res) => {
        if (res.data === 'success') {
          return true;
          // 추후 자기가 쓴 페이지로 이동하는 것 수정 요망
        } else {
          return false;
        }
      })
      .catch(() => {
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
