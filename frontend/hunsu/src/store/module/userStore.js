import { rscApi, authApi } from '@/services/api';
const state = {
  accessToken: null,
  refreshToken: null,
  nickname: null,
  userInfo: {},
  uid: null,
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
  getRefreshToken(state) {
    if (!state.refreshToken) {
      return localStorage.getItem('hunsu-refresh-token');
    }
    return state.refreshToken;
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
  },
  getUid(state) {
    return state.uid;
  },
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
  setAllInfo(state, { accessToken, refreshToken, nickname, uid }) {
    state.accessToken = accessToken;
    state.refreshToken = refreshToken;
    state.nickname = nickname;
    state.uid = uid;
    // localStorage.setItem('hunsu-access-token', accessToken);
    // localStorage.setItem('hunsu-refresh-token', refreshToken);
  },
  setUserInfo(state, userInfo) {
    state.userInfo = userInfo;
  },
  setAllInfoClear(state) {
    state.accessToken = null;
    state.refreshToken = null;
    state.nickname = null;
    state.userInfo = {};
    state.myProfileImage = null;
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
  },
};

const actions = {
  // 카카오 로그인 후 회원가입이 되어있는지 확인 후 유무에 따라 회원가입 절차 or 로그인 유도
  async userCheck({ commit }, { accessToken, refreshToken }) {
    const res = await authApi.post('/v1/auth/usercheck', {
      accessToken,
      refreshToken,
    });
    if (res.data.code === 1) {
      commit('setAllInfo', {
        accessToken: res.data.jwtToken,
        refreshToken: res.data.jwtRefresh,
        nickname: res.data.nickname,
        uid: res.data.uid,
      });
    }
    console.log('in usercheck ', res);
    return res.data.code;
  },
  signUpInApi({ commit }, params) {
    // 회원가입 api
    return authApi
      .post('/v1/auth/signup?accessToken=' + params.accessToken, {
        height: params.height,
        size: params.size,
        nickname: params.nickname,
      })
      .then((res) => {
        if (res.data.code === 1) {
          commit('setAllInfo', {
            accessToken: res.data.jwtToken,
            refreshToken: res.data.jwtRefresh,
            nickname: res.data.nickname,
            uid: res.data.uid,
          });
        } else {
          console.log('signup error');
        }
      })
      .catch((err) => {
        console.log('err in signUpInApi ', err);
      });
  },
  async kakaoLogin({ state, commit }) {
    // 로그인 API
    // console.log('rootState ', state);
    // console.log('in kakaoLogin 3 a', state.accessToken); //jwtAccessToken
    // console.log('in kakaoLogin 3', state.refreshToken); //jwtAccessToken

    const res = await authApi.post('/v1/auth/tokenlogin', {
      jwtToken: state.accessToken,
      jwtRefresh: state.refreshToken,
    });
    // console.log('in kakaologin result ', res);
    commit('setAllInfo', {
      accessToken: res.data.jwtToken,
      refreshToken: res.data.jwtRefresh,
      nickname: res.data.nickname,
      uid: res.data.uid,
    });
    // console.log('in tokenLogin', res);
  },
  getProfileInfoInApi(context, targetNickname) {
    // 상대 프로필 정보 가져오기
    console.log(targetNickname);
    return rscApi
      .get(`/user/mypage/${targetNickname}`)
      .then((res) => {
        // console.log('스토어', res.data);
        context.commit('setUserInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  getMyProfileInfoInApi(context) {
    // 내 프로필 정보 가져오기
    return rscApi
      .get(`/user/mypage/profile`)
      .then((res) => {
        context.commit('setMyProfileInfo', res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  },
  updateMyProfileInfoInApi(context, { newNickname, height, size }) {
    // 프로필 업데이트
    console.log(newNickname, height, size);
    return rscApi
      .put(`/user/mypage/modify`, {
        nickname: newNickname,
        height: height,
        size: size,
      })
      .then((res) => {
        console.log(res.data);
        context.commit('setMyProfileInfo', res.data);
        console.log('프로필수정성공');
      })
      .catch((err) => {
        console.error(err);
      });
  },

  logout({ state, commit }) {
    return authApi.post(`/v1/auth/logout?jwtToken=` + state.accessToken).then((res) => {
      if (res.data.code === 1) {
        // console.log('logout success');
        commit('setAllInfoClear');
      } else {
        console.log('logout fail');
        console.log(
          'access',
          state.accessToken,
          'refresh',
          state.refreshToken,
          'nick',
          state.nickname
        );
      }
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
