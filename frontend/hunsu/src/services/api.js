import axios from 'axios';
import store from '../store/store';

const rscApi = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL + ':8080/api',
});

const authApi = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL + ':8081/api',
});

const liveApi = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL + ':8082/api',
});

// // Add a request interceptor
rscApi.interceptors.request.use(
  function(config) {
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
export { rscApi, authApi, liveApi };
