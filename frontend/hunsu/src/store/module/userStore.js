import axios from 'axios';
const state = {
  accessToken: null,
  refreshToken: null,
  nickname: null,
  userInfo: {},
  myProfileImage: '',
  targetProfileImage: '',
  myProfileInfo: {}, // 내 프로필(키, 사이즈, 닉네임 등)
};
const getters = {
  // 모든 토큰은 jwt 의미함
  getAccessToken(state) {
    if (!state.accessToken) {
      return localStorage.getItem('hunsu-access-token');
    }
    return state.accessToken;
  },
  getRefreshToken() {
    // if (!state.refreshToken) {
    return localStorage.getItem('hunsu-refresh-token');
    // }
    // return state.refreshToken;
  },
  getAllToken(state) {
    return { accessToken: state.accessToken, refreshToken: state.refreshToken };
  },
  getNickname(state) {
    return state.nickname;
  },
  getUserInfo(state) {
    return state.userInfo;
  },
  getMyProfileImage(state) {
    return state.myProfileImage;
  },
  getTargetProfileImage(state) {
    return state.targetProfileImage;
  },
  getMyProfileInfo(state) {
    return state.myProfileInfo;
  }
};
const mutations = {
  //모든 토큰은 jwt 의미함
  setAccessToken(state, token) {
    state.accessToken = token;
    localStorage.setItem('hunsu-access-token', token);
  },
  setRefreshToken(state, token) {
    state.refreshToken = token;
    localStorage.setItem('hunsu-refresh-token', token);
  },
  setAllToken(state, { accessToken, refreshToken }) {
    state.accessToken = accessToken;
    state.refreshToken = refreshToken;
    localStorage.setItem('hunsu-access-token', accessToken);
    localStorage.setItem('hunsu-refresh-token', refreshToken);
    console.log('setAllToken', accessToken, state.accessToken);
  },
  setNickname(state, nickname) {
    state.nickname = nickname;
  },
  setAllInfo(state, { accessToken, refreshToken, nickname }) {
    state.accessToken = accessToken;
    state.refreshToken = refreshToken;
    state.nickname = nickname;
    localStorage.setItem('hunsu-access-token', accessToken);
    localStorage.setItem('hunsu-refresh-token', refreshToken);
  },
  setUserInfo(state, userInfo) {
    state.userInfo = userInfo;
  },
  setAllInfoClear(state) {
    state.accessToken = null;
    state.refreshToken = null;
    state.nickname = null;
    state.userInfo = {};
  },
  setMyProfileImage(state, payload) {
    state.myProfileImage = payload;
    console.log('in setprofile', payload);
  },
  setTargetProfileImage(state, payload) {
    state.targetProfileImage = payload;
    console.log('int targetprofile', payload);
  },
  setMyProfileInfo(state, myProfileInfo) {
    state.myProfileInfo = myProfileInfo;
  }
};

const actions = {
  // 카카오 로그인 후 회원가입이 되어있는지 확인 후 유무에 따라 회원가입 절차 or 로그인 유도
  userCheck({ commit }, { accessToken, refreshToken }) {
    return new Promise((resolve) => {
      axios
        .post('http://i4c102.p.ssafy.io:8081/api/v1/auth/usercheck', {
          accessToken,
          refreshToken,
        })
        .then((res) => {
          console.log('in usercheck', res.data);
          if (res.data.code === 1) {
            // 회원정보 있음
            commit('setAllInfo', {
              accessToken: res.data.jwtToken,
              refreshToken: res.data.jwtRefresh,
              nickname: res.data.nickname,
            });
          }
          resolve(res.data.code);
          //return -1 or 1;
        })
        .catch((err) => {
          console.log('usercheck err : ', err);
        });
    });
  },
  signUpInApi({ commit }, params) {
    // 회원가입 api
    return axios
      .post('http://i4c102.p.ssafy.io:8081/api/v1/auth/signup?accessToken=' + params.accessToken, {
        height: params.height,
        size: params.size,
        nickname: params.nickname,
      })
      .then((res) => {
        if (res.data.code === 1) {
          console.log('in singupinapi 1', res.data);
          commit('setAllInfo', {
            accessToken: res.data.jwtToken,
            refreshToken: res.data.jwtRefresh,
            nickname: res.data.nickname,
          });
        } else {
          console.log('signup error');
        }
      })
      .catch((err) => {
        console.log('err in signUpInApi ', err);
      });
  },
  kakaoLogin({ state, commit }) {
    // 로그인 API
    console.log('rootState ', state);
    console.log('in kakaoLogin 3', state.accessToken); //jwtAccessToken
    console.log('in kakaoLogin 3', state.refreshToken); //jwtAccessToken

    new Promise((resolve) => {
      axios
        .post('http://i4c102.p.ssafy.io:8081/api/v1/auth/tokenlogin', {
          jwtToken: state.accessToken,
          jwtRefresh: state.refreshToken,
        })
        .then((res) => {
          console.log(res);
          commit('setAllInfo', {
            accessToken: res.data.jwtToken,
            refreshToken: res.data.refreshToken,
            nickname: res.data.nickname,
          });
          resolve();
        });
    });
  },
  getProfileInfoInApi(context, { myNickname, yourNickname }) {
    return axios
      .get(`http://i4c102.p.ssafy.io:8080/api/user/mypage/${myNickname}/${yourNickname}`)
      .then((res) => {
        console.log(res.data);
        context.commit('setUserInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  getMyProfileInfoInApi(context, myNickname) {
    return axios
    .get(`http://i4c102.p.ssafy.io:8080/api/user/mypage/profile/${myNickname}`)
    .then((res) => {
      context.commit('setMyProfileInfo', res.data)
    })
    .catch((err) => {
      console.error(err)
    })
  },
  updateMyProfileInfoInApi(context, {getNickname, newNickname, height, size}) {
    console.log(getNickname, newNickname, height, size)
    return axios
    .put(`http://i4c102.p.ssafy.io:8080/api/user/mypage/modify/${getNickname}`, {
      nickname: newNickname,
      height: height,
      size: size,
    })
    .then((res) => {
      context.commit('setMyProfileInfo', res.data)
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
