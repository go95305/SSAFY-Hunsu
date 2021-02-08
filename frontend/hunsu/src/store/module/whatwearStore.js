import axios from 'axios';

const state = {
  whatwearInfo: {},
  whatwearReplyInfo: {},
  // 차트오류아직 해결안됨
  WhatwearVoteInfo: {},
  voteTotal: 0,
};
const getters = {
  getWhatwearInfo(state) {
    return state.whatwearInfo;
  },
  getWhatwearReplyInfo(state) {
    return state.whatwearReplyInfo;
  },
  getWhatwearVoteInfo(state) {
    return state.WhatwearVoteInfo
  },
  getVoteTotal(state) {
    return state.voteTotal
  }
};
const mutations = {
  setWhatwearInfo(state, whatwearInfo) {
    state.whatwearInfo = whatwearInfo;
  },
  setWhatwearReplyInfo(state, whatwearReplyInfo) {
    state.whatwearReplyInfo = whatwearReplyInfo;
  },
  setWhatwearVoteInfo(state, WhatwearVoteInfo) {
    state.WhatwearVoteInfo = WhatwearVoteInfo
  },
  setVoteTotal(state, voteTotal) {
    state.voteTotal = voteTotal
  }
};
const actions = {
  getWhatwearInfoApi(context, { wearIdx, nickname }) {
    return axios
      .get(`http://i4c102.p.ssafy.io:8080/api/wear/detail/${wearIdx}/${nickname}`)
      .then((res) => {
        // console.log('Vuex get Whatwear ', res.data);
        
        context.commit('setWhatwearInfo', res.data);
        context.commit('setWhatwearReplyInfo', res.data.replyList);
        context.commit('setWhatwearVoteInfo', res.data.voteList);
      })
      .catch((err) => {
        console.error(err)
      })
  },
  createWhatwearReplyInfo(context, whatwearReplyInfo) {
    console.log('create 댓구ㄹ', whatwearReplyInfo); 
    axios
      .post('http://i4c102.p.ssafy.io:8080/api/wear/reply', whatwearReplyInfo)
      .then((res) => {
        console.log('댓글성공', res.data);

        context.commit('setWhatwearReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  likeWhatwearReplyInfo(context, { replyIdx, nickname }) {
    axios
      .put(`http://i4c102.p.ssafy.io:8080/api/wear/reply/like/${replyIdx}/${nickname}`)
      .then((res) => {
        console.log('좋아요성공', res.data)
        context.commit('setWhatwearReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  deleteWhatwearReplyInfo(context, replyIdx) {
    axios
      .put(`http://i4c102.p.ssafy.io:8080/api/wear/reply/${replyIdx}`)
      .then((res) => {
        console.log('삭제완료', res);
        context.commit('setWhatwearReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  updateWhatwearReplyInfo(context, replyInfo) {
    axios
      .put('http://i4c102.p.ssafy.io:8080/api/wear/reply', replyInfo)
      .then((res) => {
        console.log('수정완료', res);
        context.commit('setWhatwearReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  voteWhatwearInfo(context, { voteIdx, nickname }) {
    axios
    .put(`http://i4c102.p.ssafy.io:8080/api/wear/reply/vote/${voteIdx}/${nickname}`)
    .then((res) => {
      console.log('투표완료', res.data)
      context.commit('setWhatwearVoteInfo', res.data)
      var voteTotal = 0
      for (var q = 0; q < res.data.length; q++) {
        voteTotal += res.data[q].count
      }
      // console.log('합', voteTotal)
      context.commit('setVoteTotal', voteTotal)
    })
    .catch((err) => {
      console.error(err)
    })
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
