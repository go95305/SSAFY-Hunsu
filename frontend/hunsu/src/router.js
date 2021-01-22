import Vue from "vue";
import VueRouter from "vue-router";
import Home from "@/views/Home";
import LiveChat from "@/views/LiveChat";
import Ootd from "@/views/Ootd";
import WhatWear from "@/views/WhatWear";
import MyPage from "@/views/MyPage"
import Login from "@/views/user/Login"
import WhatWearDetail from '@/views/WhatWearDetail'

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/livechat',
    name: 'LiveChat',
    component: LiveChat
  },
  {
    path: '/ootd',
    name: 'Ootd',
    component: Ootd
  },
  {
    path: '/whatwear',
    name: 'WhatWear',
    component: WhatWear,
  },
  {
    path: '/mypage',
    name: 'MyPage',
    component: MyPage
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/whatwear/detail',
    name: "WhatWearDetail",
    component: WhatWearDetail,
  }

]




const router = new VueRouter({
  mode: "history",
  routes,
})

export default router