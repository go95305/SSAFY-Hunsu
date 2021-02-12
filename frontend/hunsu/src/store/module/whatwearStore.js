import resource from '@/services/resource';

const state = {
  whatwearInfo: {},
  whatwearReplyInfo: {},
  WhatwearVoteInfo: {},
  voteTotal: 0,
  replyCount: 0,
  dayCheck: false,
  timeCheck: false,
  endTimeCheck: false,
  time: false,
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
  getVoteTime(state) {
    return state.time;
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
  setWhatwearVoteTime(state, time) {
    state.time = time
  }
};
const actions = {
  getWhatwearInfoApi(context, { wearIdx, nickname }) {
    const endTime = (res) => {
      // 현재시간값
      let nowTime = new Date().getTime()
      console.log('현재', nowTime)

      // 마감시간값
      let endTime = res.end_time

      let endYear = endTime.slice(0, 4)
      let endMonth = endTime.slice(5,7)
      let endDay = endTime.slice(8,10)
      let endHours = endTime.slice(11, 13)
      let endMinute = endTime.slice(14, 16)
      let endSecond = endTime.slice(17,19)

      // console.log(endYear, endMonth, endDay, endHours, endMinute, endSecond)
      // Date에서 월값은 0부터 시작함
      let endTimeValue = new Date(endYear, Number(endMonth)-1, Number(endDay), Number(endHours), Number(endMinute), Number(endSecond)).getTime()
      console.log('마감', endTimeValue)

      // 현재시간이 마감시간보다 커지면 마감
      if (nowTime > endTimeValue) {
        state.endTimeCheck = true
      }

      return state.endTimeCheck
    }
    return (
      resource({
        url: `wear/detail/${wearIdx}/${nickname}`,
        method: 'get',
      })
      .then((res) => {
        console.log('wwstore', res.data)
        console.log('함수값', endTime(res.data))
        
        let replyCount = 0
        res.data.replyList.map((v) => {
          if (v.flag) {
            replyCount++
          }
        })
        // console.log('댓글합', replyCount)
        context.commit('setWhatwearVoteTime', endTime(res.data))
        context.commit('setReplyCount', replyCount)
        context.commit('setWhatwearInfo', res.data)
        context.commit('setWhatwearReplyInfo', res.data.replyList)
        context.commit('setWhatwearVoteInfo', res.data.voteList)
        state.endTimeCheck = false // 초기화해줘야하나?
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
        // console.log('댓글합', replyCount)
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