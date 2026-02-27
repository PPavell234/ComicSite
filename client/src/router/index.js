import { createRouter, createWebHistory } from "vue-router";

import headerMenu from "../views/headerMenu.vue";
import HelloWorld from "../components/HelloWorld.vue";
import ReadComic from "../views/readComic.vue";

const routes = [
  {
    path: "/",
    name: "readComic",
    component: ReadComic,
  },
  {
    path: "/headerMenu",
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
