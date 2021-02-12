import liveAxios from '@/services/live';

const state = {
  chatRooms: {},
  chatRoomDetail: {},
};

const getters = {
  getChatRooms(state) {
    return state.chatRooms;
  },
  getChatRoomDetail(state) {
    return state.chatRoomDetail;
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
};

const actions = {
  createRoom({ rootState }, { hashtagList, title }) {
    if ('' === this.room_name) {
      alert('방 제목을 입력해라');
      return;
    } else {
      console.log(rootState);
      return liveAxios
        .post('chat/room', {
          name: title, // title로 변경 필요
          publisher: rootState.user.nickname, // 항상 자신 (닉네임 가져오기)
          hashtagList,
          fixedComment: '', // 고정댓글 필요없었음
        })
        .then((res) => {
          console.log('in create', res);
          alert(res.data.name + ' 방 개설 성공');
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
    return liveAxios.get('chat/rooms/0').then((res) => {
      if (Object.prototype.toString.call(res.data) === '[object Array]') {
        console.log(res.data);
      }
      state.chatRooms = res.data;
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
