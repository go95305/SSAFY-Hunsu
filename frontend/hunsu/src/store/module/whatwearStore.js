import axios from 'axios';

const state = {
  whatwearInfo: {},
  wear_idx: 0,
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
  }
}
const mutations = {
  setWhatwearInfo(state, whatwearInfo) {
    state.whatwearInfo = whatwearInfo
  }
}
const actions = {
  getWhatwearInfoApi(context, wearIdx, nickname) {
    return axios.get(`http://i4c102.p.ssafy.io:8080/api/wear/detail/${wearIdx}/${nickname}`).then((res) => {
      console.log('Vuex get OOtd ', res);
      state.wear_idx = res.data.wear_idx
      for (var i = 1; i <= res.data.voteList.length; i++ ) {
        state.labels.push(String(i))
      }
      context.commit('setWhatwearInfo', res.data)
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