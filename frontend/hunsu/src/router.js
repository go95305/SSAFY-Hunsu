import Vue from "vue";
import VueRouter from "vue-router";
import Home from "@/views/Home";
import Live from "@/views/Live";
import Ootd from "@/views/Ootd";
import WhatWear from "@/views/WhatWear";
import MyPage from "@/views/MyPage"
import Login from "@/views/user/Login"
import OotdDetail from "@/views/OotdDetail"
import LiveDetail from "@/views/LiveDetail"

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/live',
    name: 'Live',
    component: Live,
    children: [
      {
        path: 'detail',
        component: LiveDetail
      }
    ]
  },
  {
    path: '/ootd',
    name: 'Ootd',
    component: Ootd,
    children: [
      {
        path: 'detail',
        component: OotdDetail
      }
    ]
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