import { rscApi } from '@/services/api';

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
      console.log('정렬완료', sort, pageNum)
    } else {
      console.log(ootdList);
    }
  },
  getSearchedListInApi(context, hashtag) {
    rscApi
      .get(`ootd/hashtag/search/${hashtag}`)

      .then((res) => {
        context.commit('setOotdList', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  async getOotdInfoInApi(context, info) {
    // ootd 디테일 가져오기
    await rscApi.get(`ootd/detail/${info.ootdIdx}`).then((res) => {
      state.ootdInfo = res.data;
      context.commit('setOotdInfo', res.data);
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
      .put(`ootd/del?ootdIdx=${ootdIdx}`)
      .then((res) => {
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
        .then((res) => {
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
        console.log('스토어에서 확인하기', res.data)
      })
      .catch((err) => {
        console.error(err);
      });
  },
  likeOotdReplyInfo(context, replyIdx) {
    rscApi
      .put(`ootd/reply/like?replyIdx=${replyIdx}`)
      .then((res) => {
        context.commit('setOotdReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  deleteOotdReplyInfo(context, replyIdx) {
    rscApi
      .put(`ootd/reply/del?replyIdx=${replyIdx}`)
      .then((res) => {
        context.commit('setOotdReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  updateOotdReplyInfo(context, replyInfo) {
    rscApi
      .put('ootd/reply/modi', replyInfo)
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
