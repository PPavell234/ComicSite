import { createRouter, createWebHistory } from "vue-router";

import headerMenu from "../views/headerMenuf.vue";
import HelloWorld from "../components/HelloWorld.vue";
import readComic from "../views/readComic.vue";
import comicP from "../components/Page/comicP.vue";
import UserList from "../components/UserList.vue";
import MainPage from "../views/mainPage.vue";
import HeaderMenuf from "../views/headerMenuf.vue";
import HeaderMenu from "../components/Page/headerMenu.vue";

const routes = [
  //Комонент
  {
    path: "/headerMenu",
    name: "headerMenu",
    component: HeaderMenu,
  },
  {
    path: "/",
    name: "ComicP",
    component: comicP,
  },
  {
    path: "/ru",
    name: "MainPage",
    component: MainPage,
  },
  {
    path: "/readComic",
    name: "readComic",
    component: readComic,
  },
  {
    path: "/userList",
    name: "userList",
    component: UserList,
  },
  {
    path: "/headerMenuf",
    name: "headerMenuf",
    component: HeaderMenuf,
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
