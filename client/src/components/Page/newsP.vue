<template>
    <!-- News page -->
    <div class="bg-[#353333] h-[110px] max-w-[1450px] mx-auto relative group">
        <!-- Кнопка слева (появляется при наведении на group) -->
        <button @click="prevNews"
            class="absolute left-4 top-1/2 -translate-y-1/2 z-10 hover:scale-110 transition p-2 opacity-0 group-hover:opacity-100">
            <img src="/imagePage/NewsPage/ButtonL.svg" alt="Previous" class="w-6 h-6">
        </button>

        <!-- Изображение новости -->
        <img :src="currentNewsImage" alt="" class="w-full h-full object-cover">

        <!-- Кнопка справа (появляется при наведении на group) -->
        <button @click="nextNews"
            class="absolute right-4 top-1/2 -translate-y-1/2 z-10 hover:scale-110 transition p-2 opacity-0 group-hover:opacity-100">
            <img src="/imagePage/NewsPage/ButtonL.svg" alt="Next" class="w-6 h-6 rotate-180">
        </button>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const newsImages = ref([
    '/imagePage/NewsPage/NewsPage.png',
    '/imagePage/NewsPage/NewsPage2.png',
    '/imagePage/NewsPage/NewsPage3.png'
])

const currentIndex = ref(0)
const currentNewsImage = ref(newsImages.value[0])
let intervalId = null

// Автоматическое переключение
const startAutoPlay = () => {
    intervalId = setInterval(() => {
        nextNews()
    }, 120000) // 120000 миллисекунд = 2 минуты
}

// Остановка автоплея
const stopAutoPlay = () => {
    if (intervalId) {
        clearInterval(intervalId)
        intervalId = null
    }
}

// Переключение на следующую новость
const nextNews = () => {
    currentIndex.value = (currentIndex.value + 1) % newsImages.value.length
    currentNewsImage.value = newsImages.value[currentIndex.value]
}

// Переключение на предыдущую новость
const prevNews = () => {
    currentIndex.value = (currentIndex.value - 1 + newsImages.value.length) % newsImages.value.length
    currentNewsImage.value = newsImages.value[currentIndex.value]
}

// При монтировании компонента запускаем автоплей
onMounted(() => {
    startAutoPlay()
})

// При размонтировании компонента очищаем интервал
onUnmounted(() => {
    stopAutoPlay()
})
</script>