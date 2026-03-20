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

        <ComicP v-else-if="pdfPages.length > 0" :chapterTitle="comicTitle" :pages="pdfPages" :currentPage="1"
            :totalPages="pdfPages.length" />

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

const memoryCache = new Map()

const loadComic = async () => {
    loading.value = true
    error.value = null
    progress.value = 0

    try {
        if (memoryCache.has(comicId)) {
            const cached = memoryCache.get(comicId)
            comicTitle.value = cached.title
            pdfPages.value = cached.pages
            loading.value = false
            progress.value = 100
            return
        }

        const response = await axios.get(`http://localhost:8080/api/comics/${comicId}`)
        const comic = response.data
        comicTitle.value = comic.title || 'Комикс'

        if (!comic.pdfFileId) {
            throw new Error('У комикса нет PDF файла')
        }

        progress.value = 20

        const pdfResponse = await axios.get(`http://localhost:8080/api/comics/files/${comic.pdfFileId}`, {
            responseType: 'blob'
        })

        progress.value = 50

        const formData = new FormData()
        formData.append('file', pdfResponse.data, 'comic.pdf')

        const convertResponse = await axios.post('http://localhost:8080/api/upload-pdf', formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
            onUploadProgress: (progressEvent) => {
                const percent = 50 + (progressEvent.loaded / progressEvent.total) * 50
                progress.value = Math.round(percent)
            }
        })

        if (convertResponse.data.pages && convertResponse.data.pages.length) {
            pdfPages.value = convertResponse.data.pages
            memoryCache.set(comicId, { title: comicTitle.value, pages: pdfPages.value })
            progress.value = 100
        } else {
            throw new Error('Не удалось конвертировать PDF в изображения')
        }

    } catch (err) {
        console.error('Ошибка:', err)
        error.value = err.message || 'Не удалось загрузить комикс'
    } finally {
        loading.value = false
    }
}

onMounted(loadComic)
</script>