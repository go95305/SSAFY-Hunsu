import { rscApi } from '@/services/api';

const state = {
  whatwearListInfo: {},
  whatwearListCount: 0,
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
  getWhatwearListInfo(state) {
    return state.whatwearListInfo;
  },
  getWhatwearListCount(state) {
    return state.whatwearListCount;
  },
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
  },
};
const mutations = {
  setWhatwearListInfo(state, whatwearListInfo) {
    state.whatwearListInfo = whatwearListInfo;
  },
  setWhatwearListCount(state, WhatwearListCount) {
    state.whatwearListCount = WhatwearListCount;
  },
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
    state.time = time;
  },
};
const actions = {
  getWhatwearListInfoApi(context, pageNum) {
    return rscApi
      .get(`wear/${pageNum}`)
      .then((res) => {
        context.commit('setWhatwearListInfo', res.data.wearMainDTOList);
        context.commit('setWhatwearListCount', res.data.count);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  getWhatwearInfoApi(context, { wearIdx, voteCheck }) {
    return rscApi
      .get(`wear/detail/${wearIdx}`)
      .then((res) => {
        let replyCount = 0;
        res.data.replyList.map((v) => {
          if (v.flag) {
            replyCount++;
          }
        });

        // 투표활성화가 되어있으면 그때 실행
        if (voteCheck) {
          const endTime = (res) => {
            // 현재시간값
            let nowTime = new Date().getTime();

            // 마감시간값
            let endTime = res.end_time;

            let endYear = endTime.slice(0, 4);
            let endMonth = endTime.slice(5, 7);
            let endDay = endTime.slice(8, 10);
            let endHours = endTime.slice(11, 13);
            let endMinute = endTime.slice(14, 16);
            let endSecond = endTime.slice(17, 19);

            // Date에서 월값은 0부터 시작함
            let endTimeValue = new Date(
              endYear,
              Number(endMonth) - 1,
              Number(endDay),
              Number(endHours),
              Number(endMinute),
              Number(endSecond)
            ).getTime();

            // 현재시간이 마감시간보다 커지면 마감
            if (nowTime > endTimeValue) {
              state.endTimeCheck = true;
            }

            return state.endTimeCheck;
          };
          context.commit('setWhatwearVoteTime', endTime(res.data));
        }
        context.commit('setReplyCount', replyCount);
        context.commit('setWhatwearInfo', res.data);
        context.commit('setWhatwearReplyInfo', res.data.replyList);
        context.commit('setWhatwearVoteInfo', res.data.voteList);
        state.endTimeCheck = false; // 초기화해줘야하나?
      })
      .catch((err) => {
        console.error(err);
      });
  },
  createWhatwearReplyInfo(context, whatwearReplyInfo) {
    return rscApi
      .post('wear/reply', whatwearReplyInfo)
      .then((res) => {
        let replyCount = 0;
        res.data.map((v) => {
          if (v.flag) {
            replyCount++;
          }
        });
        context.commit('setWhatwearReplyInfo', res.data);
        context.commit('setReplyCount', replyCount);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  likeWhatwearReplyInfo(context, replyIdx) {
    return rscApi
      .put(`wear/reply/like?reply_idx=${replyIdx}`)
      .then((res) => {
        context.commit('setWhatwearReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  deleteWhatwearReplyInfo(context, replyIdx) {
    return rscApi
      .put(`wear/reply/del?idx=${replyIdx}`)
      .then((res) => {
        let replyCount = 0;
        res.data.map((v) => {
          if (v.flag) {
            replyCount++;
          }
        });
        context.commit('setReplyCount', replyCount);
        context.commit('setWhatwearReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  updateWhatwearReplyInfo(context, replyInfo) {
    rscApi
      .put('wear/reply/modi', replyInfo)
      .then((res) => {
        context.commit('setWhatwearReplyInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  voteWhatwearInfo(context, voteIdx) {
    rscApi
      .put(`wear/reply/vote?vote_item_idx=${voteIdx}`)
      .then((res) => {
        context.commit('setWhatwearVoteInfo', res.data);
        let voteTotal = 0;
        for (let q = 0; q < res.data.length; q++) {
          voteTotal += res.data[q].count;
        }
        context.commit('setVoteTotal', voteTotal);
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
