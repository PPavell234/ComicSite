import { createRouter, createWebHistory } from 'vue-router'


import HelloWorld2 from "@/components/HelloWorld2.vue";
import HelloWorld from "@/components/HelloWorld.vue";

const routes = [
    {
        path: '/',
        name: 'HelloWorld',
        component: HelloWorld
    },
    {
        path: '/hello2',
        name: 'HelloWorld2',
        component: HelloWorld2
    },
    {
        path: '/hello3',
        name: 'HelloWorld3',
        component: HelloWorld2
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router