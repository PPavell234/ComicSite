import { createRouter, createWebHistory } from "vue-router";

import headerMenu from "../views/headerMenu.vue";
import HelloWorld from "../components/HelloWorld.vue";

const routes = [
  {
    path: "/",
    name: "headerMenu",
    component: headerMenu,
  },
  {
    path: "/HelloWorld",
    name: "HelloWorld",
    component: HelloWorld,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
