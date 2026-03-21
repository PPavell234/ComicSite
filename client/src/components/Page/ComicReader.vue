<template>
    <div>
        <!-- Индикатор прогресса -->
        <div v-if="loading" class="bg-gray-800 min-h-screen flex flex-col items-center justify-center">
            <div class="text-white text-xl mb-4">Загрузка комикса...</div>
            <div class="w-64 h-2 bg-gray-700 rounded-full overflow-hidden">
                <div class="h-full bg-yellow-500 transition-all duration-300" :style="{ width: progress + '%' }"></div>
            </div>
            <div class="text-white text-sm mt-2">{{ progress }}%</div>
        </div>

        <div v-else-if="error" class="bg-gray-800 min-h-screen flex items-center justify-center">
            <div class="text-red-500 text-xl">Ошибка загрузки: {{ error }}</div>
        </div>

        <!-- Компонент для чтения комикса - передаем comicId -->
        <ComicP v-else-if="pdfPages.length > 0" :comicId="comicId" :chapterTitle="comicTitle" :pages="pdfPages"
            :currentPage="1" :totalPages="pdfPages.length" />

        <div v-else class="bg-gray-800 min-h-screen flex items-center justify-center">
            <div class="text-white text-xl">Нет страниц для отображения</div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import ComicP from './comicP.vue'

const route = useRoute()
const comicId = route.params.id
const comicTitle = ref('')
const pdfPages = ref([])
const loading = ref(true)
const error = ref(null)
const progress = ref(0)

console.log('comicId в ComicReader:', comicId)

const memoryCache = new Map()

const loadComic = async () => {
    loading.value = true
    error.value = null
    progress.value = 0

    try {
        console.log('Загрузка комикса с ID:', comicId)

        // Проверяем кэш
        if (memoryCache.has(comicId)) {
            console.log('Загружаем из кэша')
            const cached = memoryCache.get(comicId)
            comicTitle.value = cached.title
            pdfPages.value = cached.pages
            loading.value = false
            progress.value = 100
            return
        }

        progress.value = 30

        // Получаем комикс с готовыми страницами
        const response = await axios.get(`http://localhost:8080/api/comics/${comicId}`)
        const comic = response.data
        comicTitle.value = comic.title || 'Комикс'

        progress.value = 60

        console.log('PageImageIds:', comic.pageImageIds)

        // Просто берем готовые URL страниц
        if (comic.pageImageIds && comic.pageImageIds.length) {
            pdfPages.value = comic.pageImageIds.map(id =>
                `http://localhost:8080/api/comics/files/${id}`
            )
            console.log('Загружено страниц:', pdfPages.value.length)
            progress.value = 100

            // Сохраняем в кэш
            memoryCache.set(comicId, {
                title: comicTitle.value,
                pages: pdfPages.value
            })
        } else {
            throw new Error('У комикса нет страниц')
        }

    } catch (err) {
        console.error('Ошибка загрузки комикса:', err)
        error.value = err.message || 'Не удалось загрузить комикс'
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    loadComic()
})
</script>