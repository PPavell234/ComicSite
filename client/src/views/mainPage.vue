<template>
    <!-- Делаем бэкраунд чтобы небыло белого -->
    <div class="bg-gray-800">
        <HeaderMenu></HeaderMenu>

        <!-- Лист с комиксами - ЗАГРУЗКА ИЗ БД -->
        <div
            class="bg-[#353333] h-[320px] flex items-center justify-start max-w-[1450px] mx-auto pl-23 gap-12 overflow-x-auto">
            <!-- Показываем скелетон во время загрузки -->
            <div v-if="loading" v-for="n in 4" :key="n" class="animate-pulse">
                <div class="w-[170px] h-[250px] bg-gray-600 rounded"></div>
                <div class="w-[140px] h-4 bg-gray-600 mt-2 rounded"></div>
            </div>

            <!-- Сообщение если нет комиксов -->
            <div v-else-if="comics.length === 0" class="text-white text-center w-full py-10">
                Пока нет опубликованных комиксов
            </div>

            <!-- Реальные комиксы из БД -->
            <comic-title v-for="comic in comics" :key="comic.id" :title="comic.title" :chapter="getLatestChapter(comic)"
                :bg="getCoverUrl(comic)"
                :favorite="isFavorite(comic.id) ? '/imagePage/comic-list/Favorite.svg' : '/imagePage/comic-list/NotFavorite.svg'"
                @click="goToComic(comic.id)" @toggle-favorite="toggleFavorite(comic.id)" />
        </div>

        <!-- Пустое простартсво-->
        <div class="h-[25px] flex items-center justify-start mx-auto pl-23 gap-12"></div>

        <!-- News page -->
        <div class="bg-[#353333] h-[110px] max-w-[1450px] mx-auto relative">
            <img src="/imagePage/NewsPage/NewsPage.png" alt="" class="w-full h-full object-cover">
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import HeaderMenu from '../components/Page/headerMenu.vue'
import comicTitle from '../components/Page/comicTitle.vue'

const router = useRouter()
const comics = ref([])
const loading = ref(true)
const favorites = ref(new Set())
let refreshInterval = null

// Загрузка комиксов из MongoDB
const fetchComics = async () => {
    try {
        console.log('Загрузка комиксов...')
        const response = await axios.get('http://localhost:8080/api/comics/all')
        comics.value = response.data
        console.log('Загружено комиксов:', comics.value.length)
        console.log('Первый комикс:', comics.value[0]) // Отладка
    } catch (error) {
        console.error('Ошибка загрузки комиксов:', error)
    } finally {
        loading.value = false
    }
}

// Получение URL обложки
const getCoverUrl = (comic) => {
    console.log('getCoverUrl для комикса:', comic.title, 'coverImageId:', comic.coverImageId)
    if (comic.coverImageId) {
        const url = `http://localhost:8080/api/comics/files/${comic.coverImageId}`
        console.log('URL обложки:', url)
        return url
    }
    console.log('Нет coverImageId, используем заглушку')
    return '/imagePage/comic-list/default-cover.jpg'
}

// Получение номера главы
const getLatestChapter = (comic) => {
    if (comic.chapterNumber) {
        return comic.chapterNumber
    }
    return comic.year || 1
}

// Проверка избранного
const isFavorite = (comicId) => {
    return favorites.value.has(comicId)
}

// Переход на страницу комикса
const goToComic = (comicId) => {
    console.log('Переход к комиксу с ID:', comicId)
    router.push(`/comic/${comicId}`)
}

// Загрузка избранного
const loadFavorites = () => {
    const saved = localStorage.getItem('favoriteComics')
    if (saved) {
        favorites.value = new Set(JSON.parse(saved))
        console.log('Загружено избранное:', [...favorites.value])
    }
}

// Добавление/удаление из избранного
const toggleFavorite = (comicId) => {
    console.log('Toggle favorite для комикса:', comicId)
    if (favorites.value.has(comicId)) {
        favorites.value.delete(comicId)
    } else {
        favorites.value.add(comicId)
    }
    localStorage.setItem('favoriteComics', JSON.stringify([...favorites.value]))
    console.log('Избранное обновлено:', [...favorites.value])
}

// Слушаем событие публикации нового комикса
const handleNewComic = (event) => {
    console.log('Новый комикс опубликован:', event.detail)
    fetchComics()
}

onMounted(() => {
    console.log('Component mounted')
    fetchComics()
    loadFavorites()
    window.addEventListener('comic-published', handleNewComic)
    refreshInterval = setInterval(fetchComics, 30000)
})

onUnmounted(() => {
    window.removeEventListener('comic-published', handleNewComic)
    if (refreshInterval) {
        clearInterval(refreshInterval)
    }
})
</script>

<style scoped>
.overflow-x-auto {
    scrollbar-width: thin;
    scrollbar-color: #888 #333;
    padding-bottom: 10px;
}

.overflow-x-auto::-webkit-scrollbar {
    height: 8px;
}

.overflow-x-auto::-webkit-scrollbar-track {
    background: #333;
    border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-thumb:hover {
    background: #555;
}

@keyframes pulse {

    0%,
    100% {
        opacity: 1;
    }

    50% {
        opacity: 0.5;
    }
}

.animate-pulse {
    animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}
</style>