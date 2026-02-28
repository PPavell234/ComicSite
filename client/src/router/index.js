import { createRouter, createWebHistory } from "vue-router";

import headerMenu from "../views/headerMenu.vue";
import HelloWorld from "../components/HelloWorld.vue";
import readComic from "../views/readComic.vue";
import comicP from "../components/Page/comicP.vue";

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
