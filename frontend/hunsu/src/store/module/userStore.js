import axios from 'axios';
const state = {
  accessToken: '',
  refreshToken: '',
  nickname: '',
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
};
const mutations = {
  //모든 토큰은 jwt 의미함
  setAccessToken(state, token) {
    state.accessToken = token;
  },
  setRefreshToken(state, token) {
    state.refreshToken = token;
  },
  setAllToken(state, accessToken, refreshToken) {
    state.accessToken = accessToken;
    state.refreshTOken = refreshToken;
  },
};

const actions = {
  // 카카오 로그인 후 회원가입이 되어있는지 확인 후 유무에 따라 회원가입 절차 or 로그인 유도
  userCheck(context, accessToken, refreshToken) {
    console.log('in usercheckapi 1', accessToken, refreshToken);
    return axios
      .post('http://localhost:8081/v1/auth/usercheck', {
        accessToken,
        refreshToken,
      })
      .then((res) => {
        console.log(res.data.code);
        return res.data.code;
        //return -1 or 1;
      })
      .catch((err) => {
        console.log('usercheck err : ', err);
      });
  },
  signUpInApi(context, params) {
    return (
      axios
        // .post("http://i4c102.p.ssafy.io:8081/api/v1/auth/signup", {
        .post('http://localhost:8081/v1/auth/signup', params)
        .then((res) => {
          console.log('in singupinapi 1', res.data);
          context.commit('setAllToken', res.data.accessToken, res.data.refreshToken);
          // this.kakaoLogin(res.accessToken);
        })
        .catch((err) => {
          console.log('err in signUpInApi ', err);
        })
    );
  },
  kakaoLogin(context) {
    console.log('in kakaoLogin 3', context.state.accessToken); //jwtAccessToken
    // return axios.get('http://localhost:8081/v1/auth/check?jwtToken=' + accessToken).then((res) => {
    //   console.log(res);
    // });
    return axios
      .post('http://localhost:8081/v1/auth/login?jwtToken=' + context.state.accessToken)
      .then((res) => {
        console.log('in kakaoLogin 4', res);
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
