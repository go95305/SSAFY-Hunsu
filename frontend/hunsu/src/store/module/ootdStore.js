import resource from '@/services/resource';
// import axios from 'axios';
const state = {
  ootdInfo: {},
  ootdReplyInfo: {},
  ootdList: [],
  like: false,
};
const getters = {
  getOotdInfo(state) {
    return state.ootdInfo;
  },
  getOotdReplyInfo(state) {
    return state.ootdReplyInfo;
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
  setOotdReplyInfo(state, ootdReplyInfo) {
    state.ootdReplyInfo = ootdReplyInfo;
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
  getOotdListInApi({ commit }, { sort, pageNum }) {
    // console.log(rootState);
    console.log(sort, pageNum);
    return (
      resource({
        url: `ootd/${sort}/${pageNum}`,
        method: 'get',
      })
        // .get(`http://i4c102.p.ssafy.io:8080/api/ootd/${sort}/${pageNum}`)
        .then((res) => {
          console.log(res);
          res.data.forEach((info) => {
            info.imageUrls = [];
          });
          commit('setOotdList', res.data);
          return res.data;
        })
        .catch((err) => {
          console.error(err);
        })
    );
  },
  getSearchedListInApi(context, hashtag) {
    resource({
      url: `ootd/hashtag/search/${hashtag}`,
      method: 'get',
    })
      // (`http://i4c102.p.ssafy.io:8080/api/ootd/hashtag/search/${hashtag}`)

      .then((res) => {
        context.commit('setOotdList', res.data);
        // console.log(res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  getOotdInfoInApi(context, info) {
    // ootd 디테일 가져오기
    // console.log(info.ootdIdx, info.nickname);
    return (
      resource({
        url: `ootd/detail/${info.ootdIdx}/${info.nickname}`,
        method: 'get',
      })
        // .get(`http://i4c102.p.ssafy.io:8080/api/ootd/detail/${info.ootdIdx}/${info.nickname}`)
        .then((res) => {
          // console.log('Vuex get OOtd ', res);
          console.log('getOotdInfo', res);
          state.ootdInfo = res.data;
          context.commit('setOotdInfo', res.data);
          console.log(res.data.ootdReplyDTOList);
          context.commit('setOotdReplyInfo', res.data.ootdReplyDTOList);
        })
    );
  },
  updateOotdInfo(context, ootdInfo) {
    // ootd 수정
    // return axios.put(`http://i4c102.p.ssafy.io:8080/api/ootd`, ootdInfo).then((res) => {
    //   console.log('in update', res);
    return resource({
      url: 'ootd',
      method: 'put',
      data: ootdInfo,
    }).then((res) => {
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
    // axios
    //   .delete(`http://i4c102.p.ssafy.io:8080/api/ootd/${ootdIdx}`)
    resource({
      url: `ootd/${ootdIdx}`,
      method: 'delete',
    })
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
    // console.log(nickname);
    resource({
      url: 'ootd/like',
      method: 'put',
      data: {
        nickname,
        ootdIdx: state.ootdInfo.ootdIdx,
      },
    })
      // .put(`http://i4c102.p.ssafy.io:8080/api/ootd/like`, {
      //   nickname,
      //   ootdIdx: state.ootdInfo.ootdIdx,
      // })
      .then((res) => {
        // console.log(res);
        commit('toggleLike', res.data);
      });
  },
  createOotdInfo(context, params) {
    // ootd 작성

    return (
      resource({
        url: 'ootd',
        method: 'post',
        data: params,
      })
        // .post('http://i4c102.p.ssafy.io:8080/api/ootd', params)
        .then((res) => {
          console.log(res.status);
          if (res.status === 200) {
            return res.data;
          } else {
            return false;
          }
          // 추후 자기가 쓴 페이지로 이동하는 것 수정 요망
        })
        .catch(() => {
          return false;
        })
    );
  },

  // 댓글
  createOotdReplyInfo(context, OotdReplyInfo) {
    // axios
    //   .post('http://i4c102.p.ssafy.io:8080/api/ootd/reply', OotdReplyInfo)
    resource({
      url: 'ootd/reply',
      method: 'post',
      data: OotdReplyInfo,
    })
      .then((res) => {
        context.commit('setOotdReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  likeOotdReplyInfo(context, { replyIdx, nickname }) {
    // axios
    //   .put(`http://i4c102.p.ssafy.io:8080/api/ootd/reply/like/${replyIdx}/${nickname}`)
    resource({
      url: `ootd/reply/like/${replyIdx}/${nickname}`,
      method: 'put',
    })
      .then((res) => {
        console.log(res);
        context.commit('setOotdReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  deleteOotdReplyInfo(context, replyIdx) {
    resource({
      url: `ootd/reply/${replyIdx}`,
      method: 'delete',
    })
      // .delete(`http://i4c102.p.ssafy.io:8080/api/ootd/reply/${replyIdx}`)
      .then((res) => {
        context.commit('setOotdReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  updateOotdReplyInfo(context, replyInfo) {
    // axios
    //   .put('http://i4c102.p.ssafy.io:8080/api/ootd/reply', replyInfo)
    resource({
      url: 'ootd/reply',
      method: 'put',
      data: replyInfo,
    })
      .then((res) => {
        console.log('수정완료', res);
        context.commit('setOotdReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
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
