import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '@/views/Home';
import Live from '@/views/live/Live';
import Ootd from '@/views/ootd/Ootd';
import WhatWear from '@/views/whatwear/WhatWear';
import WhatWearDetail from '@/views/whatwear/WhatWearDetail';
import MyPage from '@/views/user/MyPage';
import Login from '@/views/user/Login';
import OotdDetail from '@/views/ootd/OotdDetail';
import LiveDetail from '@/views/live/LiveDetail';
// import Auth from "@/views/user/Auth"

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/live',
    name: 'Live',
    component: Live,
  },
  {
    path: '/live/detail',
    name: 'LiveDetail',
    component: LiveDetail,
  },
  {
    path: '/ootd',
    name: 'Ootd',
    component: Ootd,
  },
  {
    path: '/ootd/detail',
    name: 'OotdDetail',
    component: OotdDetail,
    // props: true,
  },
  {
    path: '/whatwear',
    name: 'WhatWear',
    component: WhatWear,
  },
  {
    path: '/mypage',
    name: 'MyPage',
    component: MyPage,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/whatwear/detail',
    name: 'WhatWearDetail',
    component: WhatWearDetail,
    props: true,
  },
  // {
  //   path : '/auth',
  //   name : "Auth",
  //   component: Auth,
  // },
];

const router = new VueRouter({
  mode: 'history',
  routes,
});

export default router;
