import axios from 'axios';
const state = {
  accessToken: null,
  refreshToken: null,
  nickname: null,
  userInfo: {},
};
const getters = {
  // 모든 토큰은 jwt 의미함
  getAccessToken(state) {
    return state.accessToken;
  },
  getRefreshToken(state) {
    return state.refreshToken;
  },
  getAllToken(state) {
    return { accessToken: state.accessToken, refreshToken: state.refreshToken };
  },
  getNickname(state) {
    return state.nickname;
  },
  getUserInfo(state) {
    return state.userInfo
  }
};
const mutations = {
  //모든 토큰은 jwt 의미함
  setAccessToken(state, token) {
    state.accessToken = token;
  },
  setRefreshToken(state, token) {
    state.refreshToken = token;
  },
  setAllToken(state, { accessToken, refreshToken }) {
    state.accessToken = accessToken;
    state.refreshToken = refreshToken;
    console.log('setAllToken', accessToken, state.accessToken);
  },
  setNickname(state, nickname) {
    state.nickname = nickname;
  },
  setAllInfo(state, { accessToken, refreshToken, nickname }) {
    state.accessToken = accessToken;
    state.refreshToken = refreshToken;
    state.nickname = nickname;
  },
  setUserInfo(state, userInfo) {
    state.userInfo = userInfo
  }
};

const actions = {
  // 카카오 로그인 후 회원가입이 되어있는지 확인 후 유무에 따라 회원가입 절차 or 로그인 유도
  userCheck({ commit }, { accessToken, refreshToken }) {
    return axios
      .post('http://i4c102.p.ssafy.io:8081/api/v1/auth/usercheck', {
        accessToken,
        refreshToken,
      })
      .then((res) => {
        console.log(res.data.code);
        if (res.data.code === 1) {
          // 회원정보 없음
          commit('setAllInfo', {
            accessToken: res.data.jwtToken,
            refreshToken: res.data.jwtRefresh,
            nickname: res.data.nickname,
          });
        }
        return res.data.code;
        //return -1 or 1;
      })
      .catch((err) => {
        console.log('usercheck err : ', err);
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

    return axios
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
      });
  },
  getProfileInfoInApi(context, {myNickname, yourNickname}) {
    return axios
      .get(`http://i4c102.p.ssafy.io:8080/api/user/mypage/${myNickname}/${yourNickname}`)
      .then((res) => {
        console.log(res.data)
        context.commit('setUserInfo', res.data)

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
