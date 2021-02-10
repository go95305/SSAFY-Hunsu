import resource from '@/services/resource';

const state = {
  whatwearInfo: {},
  whatwearReplyInfo: {},
  WhatwearVoteInfo: {},
  voteTotal: 0,
  replyCount: 0,
};
const getters = {
  getWhatwearInfo(state) {
    return state.whatwearInfo;
  },
  getWhatwearReplyInfo(state) {
    return state.whatwearReplyInfo;
  },
  getWhatwearVoteInfo(state) {
    return state.WhatwearVoteInfo;
  },
  getVoteTotal(state) {
    return state.voteTotal;
  },
  getReplyCount(state) {
    return state.replyCount;
  },
};
const mutations = {
  setWhatwearInfo(state, whatwearInfo) {
    state.whatwearInfo = whatwearInfo;
  },
  setWhatwearReplyInfo(state, whatwearReplyInfo) {
    state.whatwearReplyInfo = whatwearReplyInfo;
  },
  setWhatwearVoteInfo(state, WhatwearVoteInfo) {
    state.WhatwearVoteInfo = WhatwearVoteInfo;
  },
  setVoteTotal(state, voteTotal) {
    state.voteTotal = voteTotal;
  },
  setReplyCount(state, replyCount) {
    state.replyCount = replyCount;
  },
  setWhatwearInfoImages(state, images) {
    state.whatwearInfo.imageUrls = images;
  },
};
const actions = {
  getWhatwearInfoApi(context, { wearIdx, nickname }) {
    return (
      resource({
        url: `wear/detail/${wearIdx}/${nickname}`,
        method: 'get',
      })
      .then((res) => {
        console.log('wwstore', res.data)

        let replyCount = 0
        res.data.replyList.map((v) => {
          if (v.flag) {
            replyCount++
          }
        })
        console.log('댓글합', replyCount)
        context.commit('setReplyCount', replyCount)
        context.commit('setWhatwearInfo', res.data)
        context.commit('setWhatwearReplyInfo', res.data.replyList)
        context.commit('setWhatwearVoteInfo', res.data.voteList)
      })
      .catch((err) => {
        console.error(err)
      })
    )
  },
  createWhatwearReplyInfo(context, whatwearReplyInfo) {
    resource({
      url: 'wear/reply',
      method: 'post',
      data: whatwearReplyInfo,
    })
      .then((res) => {
        console.log(res)
        let replyCount = 0
        res.data.map((v) => {
          if (v.flag) {
            replyCount++
          }
        })
        console.log('댓글합', replyCount)
        context.commit('setWhatwearReplyInfo', res.data)
        context.commit('setReplyCount', replyCount)
      })
      .catch((err) => {
        console.error(err)
      })
  },
  likeWhatwearReplyInfo(context, { replyIdx, nickname }) {
    resource({
      url: `wear/reply/like/${replyIdx}/${nickname}`,
      method: 'put',
    })
      .then((res) => {
        context.commit('setWhatwearReplyInfo', res.data)
      })
      .catch((err) => {
        console.error(err)
      })
  },
  deleteWhatwearReplyInfo(context, replyIdx) {
    resource({
      url: `wear/reply/${replyIdx}`,
      method: 'put',
    })
      .then((res) => {
        let replyCount = 0
        res.data.map((v) => {
          if (v.flag) {
            replyCount++
          }
        })
        context.commit('setReplyCount', replyCount)
        context.commit('setWhatwearReplyInfo', res.data)
      })
      .catch((err) => {
        console.error(err)
      })
  },
  updateWhatwearReplyInfo(context, replyInfo) {
    resource({
      url: 'wear/reply',
      method: 'put',
      data: replyInfo
    })
      .then((res) => {
        context.commit('setWhatwearReplyInfo', res.data)
      })
      .catch((err) => {
        console.error(err)
      })
  },
  voteWhatwearInfo(context, { voteIdx, nickname }) {
    resource({
      url: `wear/reply/vote/${voteIdx}/${nickname}`,
      method: 'put',
    })
      .then((res) => {
        context.commit('setWhatwearVoteInfo', res.data)
        let voteTotal = 0;
        for (let q = 0; q < res.data.length; q++) {
          voteTotal += res.data[q].count
        }
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
