import { liveApi } from '@/services/api';

const state = {
  chatRooms: {},
  chatRoomDetail: {},
  stompClient: '',
};

const getters = {
  getChatRooms(state) {
    return state.chatRooms;
  },
  getChatRoomDetail(state) {
    return state.chatRoomDetail;
  },
  getStompClient(state) {
    return state.stompClient;
  },
};

const mutations = {
  setChatRooms(state, payload) {
    state.chatRooms = payload;
  },
  setChatRoomDetail(state, payload) {
    state.chatRoomDetail = payload;
  },
  setChatImageFiles(state, payload) {
    state.chatRoomDetail.images = payload;
  },
  setStompClient(state, payload) {
    state.stompClient = payload;
  },
};

const actions = {
  createRoom({ rootState }, { hashtagList, title }) {
    if ('' === this.room_name) {
      alert('방 제목을 입력해라');
      return;
    } else {
      return liveApi
        .post('/chat/room', {
          title, // title로 변경 필요
          nickname: rootState.user.nickname, // 항상 자신 (닉네임 가져오기)
          hashtagList,
          fixedComment: '', // 고정댓글 필요없었음
          jwtToken: rootState.user.accessToken, // jwtToken
        })
        .then((res) => {
          console.log('in create', res);
          alert(res.data.title + ' 방 개설 성공');
          return res;
          //   this.room_name = '';
          //   this.hashtagList = [];
          //   this.fixedComment = '';
          //   this.findAllRoom();
        })
        .catch((res) => {
          alert('채팅방개설 실패');
          console.log(res);
          return -1;
        });
    }
  },
  findAllRoom({ state }) {
    return liveApi.get('/chat/rooms/0').then((res) => {
      if (Object.prototype.toString.call(res.data) === '[object Array]') {
        console.log(res.data);
      }
      state.chatRooms = res.data;
    });
  },
  endMyRoom({ state }) {
    return liveApi.post('/chat/room/remove/' + state.chatRoomDetail.roomId).then(() => {});
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
