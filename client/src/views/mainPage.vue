<template>
    <!-- Делаем бэкраунд чтобы небыло белого -->
    <div class="bg-gray-800">
        <HeaderMenu></HeaderMenu>

        <!-- Лист с комиксами - ЗАГРУЗКА ИЗ БД (верхний горизонтальный слайдер) -->
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
                :bg="getCoverUrl(comic)" :favorite="isFavorite(comic.id)" :comic-id="comic.id"
                @click="goToComic(comic.id)" @toggle-favorite="toggleFavorite" />
        </div>

        <NewsP></NewsP>


        <!-- ДВЕ КОЛОНКИ -->
        <div class="flex  max-w-[1450px] mx-auto mt-2">

            <!-- ============ ЛЕВАЯ КОЛОНКА ============ -->
            <div class="w-[425px] mt-5 flex-shrink-0">
                <!-- Форум -->
                <div class="flex items-center justify-between mb-6">
                    <ul>
                        <li class="flex items-center space-x-2">
                            <p class="text-white">Последние темы форума</p>
                            <img src="/comicP/IconBack.svg" alt="" class="w-3 h-3 rotate-180">
                        </li>
                    </ul>
                </div>

                <!-- Список тем -->
                <div class="bg-white pl-5 pt-7 pr-5 pb-5">
                    <div class="space-y-4">
                        <!-- Тема 1 -->
                        <div class="border-gray-600 pb-3">
                            <div class="flex items-center justify-between">
                                <a href="#" class="text-black">Название темы 1</a>
                                <div class="flex items-center gap-4">
                                    <div class="flex items-center gap-1">
                                        <img src="/imagePage/headerMenu/eas.svg" alt="" class="w-4 h-4"
                                            style="filter: brightness(0) saturate(100%) invert(50%) sepia(0%) saturate(0%) hue-rotate(0deg) brightness(100%) contrast(100%);">
                                        <span class="text-gray-400 text-sm">0</span>
                                    </div>
                                    <div class="flex items-center gap-1">
                                        <img src="/imagePage/headerMenu/message.svg" alt="" class="w-4 h-4">
                                        <span class="text-gray-400 text-sm">0</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Тема 2 -->
                        <div class="border-gray-600 pb-3">
                            <div class="flex items-center justify-between">
                                <a href="#" class="text-black">Название темы 2</a>
                                <div class="flex items-center gap-4">
                                    <div class="flex items-center gap-1">
                                        <img src="/imagePage/headerMenu/eas.svg" alt="" class="w-4 h-4"
                                            style="filter: brightness(0) saturate(100%) invert(50%) sepia(0%) saturate(0%) hue-rotate(0deg) brightness(100%) contrast(100%);">
                                        <span class="text-gray-400 text-sm">0</span>
                                    </div>
                                    <div class="flex items-center gap-1">
                                        <img src="/imagePage/headerMenu/message.svg" alt="" class="w-4 h-4">
                                        <span class="text-gray-400 text-sm">0</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Тема 3 -->
                        <div class="border-gray-600 pb-3">
                            <div class="flex items-center justify-between">
                                <a href="#" class="text-black">Название темы 3</a>
                                <div class="flex items-center gap-4">
                                    <div class="flex items-center gap-1">
                                        <img src="/imagePage/headerMenu/eas.svg" alt="" class="w-4 h-4"
                                            style="filter: brightness(0) saturate(100%) invert(50%) sepia(0%) saturate(0%) hue-rotate(0deg) brightness(100%) contrast(100%);">
                                        <span class="text-gray-400 text-sm">0</span>
                                    </div>
                                    <div class="flex items-center gap-1">
                                        <img src="/imagePage/headerMenu/message.svg" alt="" class="w-4 h-4">
                                        <span class="text-gray-400 text-sm">0</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- Пользователи активыне -->
                <div class="flex items-center justify-between mb-6 mt-4">
                    <ul>
                        <li class="flex items-center space-x-2">
                            <p class="text-white">Пользователи активные</p>
                            <img src="/comicP/IconBack.svg" alt="" class="w-3 h-3 rotate-180">
                        </li>
                    </ul>
                </div>

                <!-- Пользователи активные (один блок) -->
                <div class="bg-white p-4 mt-4">
                    <div class="grid grid-cols-2 gap-4">
                        <!-- Пользователь 1 -->
                        <div class="flex gap-3 items-center border-b border-gray-200 pb-3">
                            <img src="/imagePage/profileIcon/profile1.svg" alt="" class="w-10 h-10">
                            <div class="flex-1">
                                <div class="flex justify-between">
                                    <span class="text-black text-sm font-medium">Имя пользователя</span>
                                    <span class="text-gray-500 text-xs">#123</span>
                                </div>
                                <div class="flex justify-between mt-1">
                                    <span class="text-gray-500 text-xs">Уровень: 12</span>
                                    <span class="text-gray-500 text-xs">104/287 XP</span>
                                </div>
                                <div class="flex gap-1 mt-2">
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                </div>
                            </div>
                        </div>

                        <!-- Пользователь 2 -->
                        <div class="flex gap-3 items-center border-b border-gray-200 pb-3">
                            <img src="/imagePage/profileIcon/profile1.svg" alt="" class="w-10 h-10">
                            <div class="flex-1">
                                <div class="flex justify-between">
                                    <span class="text-black text-sm font-medium">Имя пользователя 2</span>
                                    <span class="text-gray-500 text-xs">#124</span>
                                </div>
                                <div class="flex justify-between mt-1">
                                    <span class="text-gray-500 text-xs">Уровень: 8</span>
                                    <span class="text-gray-500 text-xs">45/200 XP</span>
                                </div>
                                <div class="flex gap-1 mt-2">
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                </div>
                            </div>
                        </div>

                        <!-- Пользователь 3 -->
                        <div class="flex gap-3 items-center border-b border-gray-200 pb-3">
                            <img src="/imagePage/profileIcon/profile1.svg" alt="" class="w-10 h-10">
                            <div class="flex-1">
                                <div class="flex justify-between">
                                    <span class="text-black text-sm font-medium">Имя пользователя 3</span>
                                    <span class="text-gray-500 text-xs">#125</span>
                                </div>
                                <div class="flex justify-between mt-1">
                                    <span class="text-gray-500 text-xs">Уровень: 15</span>
                                    <span class="text-gray-500 text-xs">230/500 XP</span>
                                </div>
                                <div class="flex gap-1 mt-2">
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                </div>
                            </div>
                        </div>

                        <!-- Пользователь 4 (добавим для заполнения) -->
                        <div class="flex gap-3 items-center border-b border-gray-200 pb-3">
                            <img src="/imagePage/profileIcon/profile1.svg" alt="" class="w-10 h-10">
                            <div class="flex-1">
                                <div class="flex justify-between">
                                    <span class="text-black text-sm font-medium">Имя пользователя 4</span>
                                    <span class="text-gray-500 text-xs">#126</span>
                                </div>
                                <div class="flex justify-between mt-1">
                                    <span class="text-gray-500 text-xs">Уровень: 20</span>
                                    <span class="text-gray-500 text-xs">500/1000 XP</span>
                                </div>
                                <div class="flex gap-1 mt-2">
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                    <div class="bg-gray-200 rounded-full h-1.5 w-[30px]"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Топ недели -->
                <div class="bg-[#353333] rounded-lg p-4 ">
                    <h3 class="text-white font-bold mb-4 text-lg">Топ недели</h3>
                    <ul class="space-y-3">
                        <li class="flex items-center gap-2 text-white text-sm">
                            <span class="text-yellow-500 font-bold w-6">1.</span>
                            <span class="truncate">Название комикса</span>
                        </li>
                        <li class="flex items-center gap-2 text-white text-sm">
                            <span class="text-yellow-500 font-bold w-6">2.</span>
                            <span class="truncate">Название комикса</span>
                        </li>
                        <li class="flex items-center gap-2 text-white text-sm">
                            <span class="text-yellow-500 font-bold w-6">3.</span>
                            <span class="truncate">Название комикса</span>
                        </li>
                        <li class="flex items-center gap-2 text-white text-sm">
                            <span class="text-gray-400 w-6">4.</span>
                            <span class="truncate">Название комикса</span>
                        </li>
                        <li class="flex items-center gap-2 text-white text-sm">
                            <span class="text-gray-400 w-6">5.</span>
                            <span class="truncate">Название комикса</span>
                        </li>
                    </ul>
                </div>

                <!-- Категории -->
                <div class="bg-[#353333] rounded-lg p-4 ">
                    <h3 class="text-white font-bold mb-4">Категории</h3>
                    <div class="flex flex-wrap gap-2">
                        <a href="#"
                            class="bg-gray-600 text-white text-xs px-3 py-1 rounded-full hover:bg-yellow-500 transition">Манга</a>
                        <a href="#"
                            class="bg-gray-600 text-white text-xs px-3 py-1 rounded-full hover:bg-yellow-500 transition">Манхва</a>
                        <a href="#"
                            class="bg-gray-600 text-white text-xs px-3 py-1 rounded-full hover:bg-yellow-500 transition">Комиксы</a>
                        <a href="#"
                            class="bg-gray-600 text-white text-xs px-3 py-1 rounded-full hover:bg-yellow-500 transition">Новинки</a>
                        <a href="#"
                            class="bg-gray-600 text-white text-xs px-3 py-1 rounded-full hover:bg-yellow-500 transition">Популярное</a>
                    </div>
                </div>

                <!-- Баннер -->
                <div class=" rounded-lg p-4 mt-6 ">
                    <div>
                        <img src="/imagePage/mainPage/BannerDiscord.svg" alt="">
                    </div>
                </div>
            </div>

            <!-- ============ ПРАВАЯ КОЛОНКА ============ -->
            <div class="flex-1">
                <!-- ЛИСТ 1 - Последнее -->
                <div class="bg-[#353333] text-white pl-5 pt-7">
                    <ul>
                        <li class="flex items-center space-x-2">
                            <p>Последнее</p>
                            <img src="/comicP/IconBack.svg" alt="" class="w-3 h-3 rotate-180">
                        </li>
                    </ul>
                </div>

                <!-- Грид для первых 3 комиксов -->
                <div class="bg-[#353333] pt-8 pb-4 ">
                    <div v-if="loading" class="grid grid-cols-3 gap-12 justify-items-center">
                        <div v-for="n in 3" :key="n" class="animate-pulse">
                            <div class="w-[170px] h-[250px] bg-gray-600 rounded"></div>
                            <div class="w-[140px] h-4 bg-gray-600 mt-2 rounded"></div>
                        </div>
                    </div>
                    <div v-else class="grid grid-cols-3 gap-12 justify-items-center scale-89">
                        <comic-title v-for="comic in comics.slice(0, 3)" :key="comic.id" :title="comic.title"
                            :chapter="getLatestChapter(comic)" :bg="getCoverUrl(comic)" :favorite="isFavorite(comic.id)"
                            :comic-id="comic.id" @click="goToComic(comic.id)" @toggle-favorite="toggleFavorite" />
                    </div>
                </div>

                <!-- Полоска -->
                <div class="h-1 bg-[#2C2B2B]"></div>

                <!-- ЛИСТ 2 - Популярное -->
                <div class="bg-[#353333] text-white pl-5 pt-7">
                    <ul>
                        <li class="flex items-center space-x-2">
                            <p>Популярное</p>
                            <img src="/comicP/IconBack.svg" alt="" class="w-3 h-3 rotate-180">
                        </li>
                    </ul>
                </div>

                <!-- Грид для следующих 3 комиксов -->
                <div class="bg-[#353333] pt-8 pb-4">
                    <div v-if="loading" class="grid grid-cols-3 gap-12 justify-items-center">
                        <div v-for="n in 3" :key="n" class="animate-pulse">
                            <div class="w-[170px] h-[250px] bg-gray-600 rounded"></div>
                            <div class="w-[140px] h-4 bg-gray-600 mt-2 rounded"></div>
                        </div>
                    </div>
                    <div v-else class="grid grid-cols-3 gap-12 justify-items-center scale-89">
                        <comic-title v-for="comic in comics.slice(3, 6)" :key="comic.id" :title="comic.title"
                            :chapter="getLatestChapter(comic)" :bg="getCoverUrl(comic)" :favorite="isFavorite(comic.id)"
                            :comic-id="comic.id" @click="goToComic(comic.id)" @toggle-favorite="toggleFavorite" />
                    </div>
                </div>

                <!-- Полоска -->
                <div class="h-1 bg-[#2C2B2B]"></div>

                <!-- ЛИСТ 3 - Новинки -->
                <div class="bg-[#353333] text-white pl-5 pt-7">
                    <ul>
                        <li class="flex items-center space-x-2">
                            <p>Новинки</p>
                            <img src="/comicP/IconBack.svg" alt="" class="w-3 h-3 rotate-180">
                        </li>
                    </ul>
                </div>

                <!-- Грид для следующих 3 комиксов -->
                <div class="bg-[#353333] pt-8 pb-4">
                    <div v-if="loading" class="grid grid-cols-3 gap-12 justify-items-center">
                        <div v-for="n in 3" :key="n" class="animate-pulse">
                            <div class="w-[170px] h-[250px] bg-gray-600 rounded"></div>
                            <div class="w-[140px] h-4 bg-gray-600 mt-2 rounded"></div>
                        </div>
                    </div>
                    <div v-else class="grid grid-cols-3 gap-12 justify-items-center scale-89">
                        <comic-title v-for="comic in comics.slice(6, 9)" :key="comic.id" :title="comic.title"
                            :chapter="getLatestChapter(comic)" :bg="getCoverUrl(comic)" :favorite="isFavorite(comic.id)"
                            :comic-id="comic.id" @click="goToComic(comic.id)" @toggle-favorite="toggleFavorite" />
                    </div>
                </div>
            </div>
            <!-- 3 Колонка -->
            <div class=" flex-1">

                <!-- ЛИСТ 1 - Последнее -->
                <div class=" text-white pl-5 pt-7">
                    <ul>
                        <li class="flex items-center space-x-2">
                            <p>Последнее отзывы</p>
                            <img src="/comicP/IconBack.svg" alt="" class="w-3 h-3 rotate-180">
                        </li>
                    </ul>
                </div>

                <!-- Отзывы -->
                <div class=" pt-8 pb-4 scale-95">
                    <div class="w-[500px] h-12 overflow-hidden rounded">
                        <img src="/imagePage/comic-list/Comic.svg" alt=""
                            class="w-[100%] h-[100%] object-cover object-top">
                    </div>
                    <div>
                        <!---Первая колонка-->
                        <div>

                        </div>
                        <div class="bg-white pl-4 pt-3 w-[380px] ">
                            <div class="flex flex-row gap-5 ">
                                <div class="gap-2 flex flex-row">
                                    <img src="/imagePage/button-icon/edit.svg" alt="" class="w-4 h-4">
                                    <p>Резензия</p>
                                </div>
                                <div class="gap-2 flex flex-row">
                                    <img src="/imagePage/button-icon/notSmile.svg" alt="" class="w-4 h-4">
                                    <p>Нетральный</p>
                                </div>
                            </div>
                            <div class="mt-10 pl-5 pr-22 ">
                                <p>Отзыв</p>
                                <div class="mt-2"></div>
                                <p>Тут какой-то отзыв
                                </p>
                            </div>
                            <div class="flex flex-row mt-15 pb-5 pr-7 gap-6">
                                <div></div>
                                <div class="flex flex-row gap-2">
                                    <img src="/imagePage/headerMenu/eas.svg" alt="" class="w-4 h-4"
                                        style="filter: brightness(0) saturate(100%) invert(50%) sepia(0%) saturate(0%) hue-rotate(0deg) brightness(100%) contrast(100%);">
                                    <p>153</p>
                                </div>
                                <div class="flex flex-row gap-2">
                                    <img src="/imagePage/button-icon/hot.svg" alt="" class="w-4 h-4"
                                        style="filter: brightness(0) saturate(100%) invert(50%) sepia(0%) saturate(0%) hue-rotate(0deg) brightness(100%) contrast(100%);">
                                    <p>3</p>
                                </div>
                                <div class="flex flex-row gap-6 ml-auto">
                                    <p>5 часов назад</p>
                                </div>
                            </div>


                        </div>
                        <!-- Картинка поверх блока -->
                        <div class="ml-70 -mt-70 relative z-10 rotate-25">
                            <img src="/imagePage/comic-list/Comic.svg" alt="" class="w-60 h-60">
                        </div>

                    </div>


                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'  // <-- ДОБАВИЛИ computed
import { useRouter } from 'vue-router'
import axios from 'axios'
import HeaderMenu from '../components/Page/headerMenu.vue'
import comicTitle from '../components/Page/comicTitle.vue'
import NewsP from '../components/Page/newsP.vue'

const router = useRouter()
const comics = ref([])
const loading = ref(true)
const favorites = ref(new Set())
let refreshInterval = null

// Вычисляемое свойство для группировки комиксов по 3
const groupedComics = computed(() => {
    const result = []
    for (let i = 0; i < comics.value.length; i += 3) {
        result.push(comics.value.slice(i, i + 3))
    }
    return result
})

// Загрузка комиксов из MongoDB
const fetchComics = async () => {
    try {
        console.log('Загрузка комиксов...')
        const response = await axios.get('http://localhost:8080/api/comics/all')
        comics.value = response.data
        console.log('Загружено комиксов:', comics.value.length)
    } catch (error) {
        console.error('Ошибка загрузки комиксов:', error)
    } finally {
        loading.value = false
    }
}

// Получение URL обложки
const getCoverUrl = (comic) => {
    if (comic.coverImageId) {
        return `http://localhost:8080/api/comics/files/${comic.coverImageId}`
    }
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
    router.push(`/comic/${comicId}/read`)
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
}

// Слушаем событие публикации нового комикса
const handleNewComic = (event) => {
    console.log('Новый комикс опубликован:', event.detail)
    fetchComics()
}

// При монтировании компонента
onMounted(() => {
    console.log('Component mounted')
    fetchComics()
    loadFavorites()
    window.addEventListener('comic-published', handleNewComic)
    refreshInterval = setInterval(fetchComics, 30000)
})

// При размонтировании компонента
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