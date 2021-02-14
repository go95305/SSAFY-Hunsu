import axios from 'axios';
import store from '../store/store';

const rscApi = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL + ':8080/api',
});

const authApi = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL + ':8081/api',
});
// axios.defaults.headers = {

// } // 헤더 추가

// // Add a request interceptor
rscApi.interceptors.request.use(
  function(config) {
    // 현재 디비상 액세스토큰과 리프레시 토큰이 반대로 되어있어 임시로 리프레시 토큰으로 테스트
    console.log('in axios', store.getters.getAccessToken);
    if (store.state.accessToken !== null) {
      config['headers'] = {
        'X-AUTH-ACCESS': store.getters.getAccessToken,
      };
    }
    // Do something before request is sent
    return config;
  },
  function(error) {
    // Do something with request error
    return Promise.reject(error);
  }
);
export { rscApi, authApi };

// // Add a response interceptor
// axios.interceptors.response.use(
//   function(response) {
//     // Any status code that lie within the range of 2xx cause this function to trigger
//     // Do something with response data
//     return response;
//   },
//   function(error) {
//     // Any status codes that falls outside the range of 2xx cause this function to trigger
//     // Do something with response error
//     return Promise.reject(error.response);
//   }
// );
