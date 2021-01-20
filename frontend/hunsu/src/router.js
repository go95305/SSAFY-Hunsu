import Vue from "vue";
import VueRouter from "vue-router";
import Home from "@/views/Home";
import LiveChat from "@/views/LiveChat";
import Ootd from "@/views/Ootd";
import WhatWear from "@/views/WhatWear";

Vue.use(VueRouter);

const routes = [
  {
    path: '/home',
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
    component: WhatWear
  },

]




const router = new VueRouter({
  mode: "history",
  routes,
})

export default router