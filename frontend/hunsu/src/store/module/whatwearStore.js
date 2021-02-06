import axios from 'axios';

const state = {
  whatwearInfo: {},
  whatwearReplyInfo: {},
  // 차트오류아직 해결안됨
  labels: [],
  datasets: [
    {
      data: [20, 20, 20, 20, 20],
      backgroundColor: ['Red', 'Yellow', 'Purple', 'Black', 'Pink'],
    },
  ],
};
const getters = {
  getWhatwearInfo(state) {
    return state.whatwearInfo;
  },
  getWhatwearReplyInfo(state) {
    return state.whatwearReplyInfo;
  },
  getWhatwearChartlabels(state) {
    return state.labels
  }
};
const mutations = {
  setWhatwearInfo(state, whatwearInfo) {
    state.whatwearInfo = whatwearInfo;
  },
  setWhatwearReplyInfo(state, whatwearReplyInfo) {
    state.whatwearReplyInfo = whatwearReplyInfo;
  },
  setWhatwearChartInfo(state, labels) {
    state.labels = labels
  }
};
const actions = {
  getWhatwearInfoApi(context, wearIdx, nickname) {
    return axios
      .get(`http://i4c102.p.ssafy.io:8080/api/wear/detail/${wearIdx}/${nickname}`)
      .then((res) => {
        console.log('Vuex get Whatwear ', res.data);
        state.wear_idx = res.data.wear_idx;
        const labels = []
        for (var i = 1; i <= res.data.voteList.length; i++) {
          labels.push(String(i));
        }
        context.commit('setWhatwearChartInfo', labels)

        context.commit('setWhatwearInfo', res.data);
        context.commit('setWhatwearReplyInfo', res.data.replyList);
      });
  },
  createWhatwearReplyInfo(context, whatwearReplyInfo) {
    axios
      .post('http://i4c102.p.ssafy.io:8080/api/wear/reply', whatwearReplyInfo)
      .then((res) => {
        // console.log('댓글성공', res.data);
        context.commit('setWhatwearReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  likeWhatwearReplyInfo(context, replyIdx, nickname) {
    axios
      .put(`http://i4c102.p.ssafy.io:8080/api/wear/reply/like/${replyIdx}/${nickname}`)
      .then((res) => {
        // console.log('좋아요성공', res.data)
        context.commit('setWhatwearReplyInfo', res.data)
      })
      .catch((err) => {
        console.error(err)
      })
  },
  deleteWhatwearReplyInfo(context, replyIdx) {
    axios
    .put(`http://i4c102.p.ssafy.io:8080/api/wear/reply/${replyIdx}`)
    .then((res) => {
      console.log('삭제완료', res)
      context.commit('setWhatwearReplyInfo', res.data)
    })
    .catch((err) => {
      console.error(err)
    })
  },
  updateWhatwearReplyInfo(context, replyInfo) {
    axios
    .put('http://i4c102.p.ssafy.io:8080/api/wear/reply', replyInfo)
    .then((res) => {
      console.log('수정완료', res)
      context.commit('setWhatwearReplyInfo', res.data)
    })
    .catch((err) => {
      console.error(err)
    })
  },
  voteWhatwearInfo(context, { voteIdx, nickname }) {
    axios
    .put(`http://i4c102.p.ssafy.io:8080/api/wear/reply/vote/${voteIdx}/${nickname}`)
    .then((res) => {
      console.log('투표완료', res)
      console.log(nickname)
    })
    .catch((err) => {
      console.error(err)
    })
  }
};

export default {
  state: {
    ...state,
  },
  getters,
  mutations,
  actions,
};
