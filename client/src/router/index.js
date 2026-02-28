import { createRouter, createWebHistory } from "vue-router";

import headerMenu from "../views/headerMenu.vue";
import HelloWorld from "../components/HelloWorld.vue";
import comicP from "../views/ComicP.vue";
import readComic from "../views/readComic.vue";

const routes = [
  {
    path: "/",
    name: "ComicP",
    component: comicP,
  },
  {
    path: "/readComic",
    name: "readComic",
    component: readComic,
  },
  {
    path: "/headerMenu",
    name: "headerMenu",
    component: headerMenu,
  },
  {
    path: "/helloWorld",
    name: "HelloWorld",
    component: HelloWorld,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
