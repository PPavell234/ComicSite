<template>
    <!-- Делаем бэкраунд чтобы небыло белого -->

    <div class="bg-gray-800">
        <HeaderMenu></HeaderMenu>

        <!-- Лист с комиксами Исп в начале-->
        <div class="bg-[#353333] h-[320px] flex items-center justify-start max-w-[1450px] mx-auto pl-23 gap-12">

            <comic-title v-for="comic in comics" :key="comic.id" :title="comic.title" :chapter="comic.chapter"
                :bg="comic.bg" :favorite="comic.favorite" />

        </div>

        <!-- Пустое простартсво-->
        <div class="h-[25px] flex items-center justify-start mx-auto pl-23 gap-12"></div>


        <!-- News page -->
        <div class="bg-[#353333] h-[110px] max-w-[1450px] mx-auto relative">
            <img src="/imagePage/NewsPage/NewsPage.png" alt="" class="w-full h-full object-cover">
        </div>

        <div>
        </div>



    </div>




</template>

<script setup>
// Компоненты
import HeaderMenu from '../components/Page/headerMenu.vue';
import headerMenu from '../components/Page/headerMenu.vue'
import comicTitle from '../components/Page/comicTitle.vue';

//--------------------------------------------------

const comics = [
    {
        id: 1,
        title: "Король в черном",
        chapter: 4,
        bg: "/imagePage/comic-list/Comic.svg",
        favorite: "/imagePage/comic-list/Favorite.svg"
    },
    {
        id: 2,
        title: "Spider-Man",
        chapter: 12,
        bg: "/imagePage/comic-list/Comic2.svg",
        favorite: "/imagePage/comic-list/Favorite.svg"
    },
    {
        id: 3,
        title: "Batman",
        chapter: 1,
        bg: "/imagePage/comic-list/Comic3.svg",
        favorite: "/imagePage/comic-list/Favorite.svg"
    },
    {
        id: 4,
        title: "Batman",
        chapter: 1,
        bg: "/imagePage/comic-list/Comic3.svg",
        favorite: "/imagePage/comic-list/Favorite.svg"
    }
]

</script>