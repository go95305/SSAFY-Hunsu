import { rscApi } from '@/services/api';

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
  async getOotdListInApi({ commit }, { sort, pageNum }) {
    const ootdList = await rscApi.get(`/ootd/${sort}/${pageNum}`);
    if (ootdList) {
      ootdList.data.forEach((info) => {
        info.imageUrls = [];
      });
      commit('setOotdList', ootdList.data);
    } else {
      console.log(ootdList);
    }

    // .get(`http://i4c102.p.ssafy.io:8080/api/ootd/${sort}/${pageNum}`)
    // .then((res) => {
    //   console.log(res);
    //   res.data.forEach((info) => {
    //     info.imageUrls = [];
    //   });
    //   commit('setOotdList', res.data);
    //   return res.data;
    // })
    // .catch((err) => {
    //   console.error(err);
    // })
  },
  getSearchedListInApi(context, hashtag) {
    rscApi
      .get(`ootd/hashtag/search/${hashtag}`)
      // (`http://i4c102.p.ssafy.io:8080/api/ootd/hashtag/search/${hashtag}`)

      .then((res) => {
        context.commit('setOotdList', res.data);
        // console.log(res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  async getOotdInfoInApi(context, info) {
    // ootd 디테일 가져오기
    // console.log(info.ootdIdx, info.nickname);
    await rscApi.get(`ootd/detail/${info.ootdIdx}`).then((res) => {
      // console.log('getOotdInfo', res);
      state.ootdInfo = res.data;
      context.commit('setOotdInfo', res.data);
      // console.log(res.data.ootdReplyDTOList);
      context.commit('setOotdReplyInfo', res.data.ootdReplyDTOList);
    });
  },
  async updateOotdInfo(context, ootdInfo) {
    // ootd 수정
    await rscApi.put('ootd/modi', ootdInfo).then((res) => {
      if (res.data === 'success') {
        context.commit('updateOotdInfo', ootdInfo);
      } else {
        console.log('update 실패');
      }
    });
  },
  async deleteOotdInfo(context, ootdIdx) {
    // ootd 삭제
    return await rscApi
      .delete(`ootd/${ootdIdx}`)
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
    rscApi
      .put('ootd/like', {
        nickname,
        ootdIdx: state.ootdInfo.ootdIdx,
      })
      .then((res) => {
        commit('toggleLike', res.data);
      });
  },
  createOotdInfo(context, params) {
    // ootd 작성

    return (
      rscApi
        .post('ootd', params)
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
    rscApi
      .post('ootd/reply', OotdReplyInfo)
      .then((res) => {
        context.commit('setOotdReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  likeOotdReplyInfo(context, { replyIdx, nickname }) {
    rscApi
      .put(`ootd/reply/like/${replyIdx}/${nickname}`)
      .then((res) => {
        context.commit('setOotdReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  deleteOotdReplyInfo(context, replyIdx) {
    rscApi
      .delete(`ootd/reply/${replyIdx}`)
      .then((res) => {
        context.commit('setOotdReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  updateOotdReplyInfo(context, replyInfo) {
    rscApi
      .put('ootd/reply', replyInfo)
      .then((res) => {
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
