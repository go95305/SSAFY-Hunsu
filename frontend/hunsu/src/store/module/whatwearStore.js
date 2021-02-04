import axios from 'axios';

const state = {
  whatwearInfo: {},
  whatwearReplyInfo: {},
  labels: [],
  datasets: [
    {
      data: [20, 20, 20, 20, 20],
      backgroundColor: ["Red", "Yellow", "Purple", "Black", "Pink",]
    }
  ],
}
const getters = {
  getWhatwearInfo(state) {
    return state.whatwearInfo
  },
  getWhatwearReplyInfo(state) {
    return state.whatwearReplyInfo
  }
}
const mutations = {
  setWhatwearInfo(state, whatwearInfo) {
    state.whatwearInfo = whatwearInfo
  },
  setWhatwearReplyInfo(state, whatwearReplyInfo) {
    state.whatwearReplyInfo = whatwearReplyInfo
  }
}
const actions = {
  getWhatwearInfoApi(context, wearIdx, nickname) {
    return axios.get(`http://i4c102.p.ssafy.io:8080/api/wear/detail/${wearIdx}/${nickname}`).then((res) => {
      console.log('Vuex get Whatwear ', res);
      state.wear_idx = res.data.wear_idx
      for (var i = 1; i <= res.data.voteList.length; i++ ) {
        state.labels.push(String(i))
      }
      context.commit('setWhatwearInfo', res.data)
    })
  },
  createWhatwearReplyInfo(context, whatwearReplyInfo) {
    axios.post('http://i4c102.p.ssafy.io:8080/api/wear/reply', whatwearReplyInfo)
      .then((res) => {
        // console.log('댓글성공', res)
        context.commit('setWhatwearReplyInfo', res)
      })
      .catch(err => {
        console.error(err)
      })
  }

}

export default {
  state: {
    ...state,
  },
  getters,
  mutations,
  actions,
}