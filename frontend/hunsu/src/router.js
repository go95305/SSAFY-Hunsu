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
// import Description from '@/views/Description';
import SignUp from '@/components/Mypage/SignupInfo';
import AboutUs from '@/views/AboutUs';
// import SignUp from '@/views/user/SignUp';
// import Auth from "@/views/user/Auth"

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(() => {
    return window.location.reload();
  });
};

Vue.use(VueRouter);

const routes = [
  // {
  //   path: '/',
  //   name: 'Description',
  //   component: Description,
  // },

  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/signup',
    name: 'SignUp',
    component: SignUp,
    props: true,
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
    props: true,
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

  {
    path: '/aboutus',
    name: 'AboutUs',
    component: AboutUs,
  },
];

const router = new VueRouter({
  mode: 'history',
  routes,
});

export default router;
