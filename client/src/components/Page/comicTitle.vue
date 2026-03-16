<script setup>
import { ref } from 'vue'

const props = defineProps({
    title: String,
    chapter: Number,
    bg: String,
    favorite: String
})

const emit = defineEmits(['click', 'toggle-favorite'])
const imageError = ref(false)
const imageLoaded = ref(false)

// Обработчик ошибки загрузки изображения
const handleImageError = (e) => {
    console.error('Ошибка загрузки изображения:', props.bg)
    imageError.value = true
    e.target.style.display = 'none'
}

const handleImageLoad = () => {
    imageLoaded.value = true
    console.log('Изображение загружено успешно:', props.bg)
}
</script>

<template>
    <div class="text-white cursor-pointer" @click="emit('click')">
        <div class="relative w-[170px] h-[250px] group rounded overflow-hidden bg-gray-700">
            <!-- Пытаемся загрузить изображение -->
            <img v-if="bg && bg !== '/imagePage/comic-list/default-cover.jpg' && !imageError" :src="bg" :alt="title"
                class="w-full h-full object-cover" @error="handleImageError" @load="handleImageLoad">

            <!-- Заглушка если изображение не загрузилось -->
            <div v-else class="w-full h-full flex flex-col items-center justify-center bg-gray-600 p-2">
                <span class="text-white text-sm text-center">{{ title }}</span>
                <span class="text-gray-400 text-xs mt-1">Нет обложки</span>
            </div>

            <!-- Кнопка избранного -->
            <img :src="favorite" alt="favorite"
                class="absolute top-2 left-2 w-6 h-6 z-10 cursor-pointer hover:scale-110 transition"
                @click.stop="emit('toggle-favorite')">

            <!-- Номер главы/года -->
            <p class="absolute bottom-2 left-2 bg-black bg-opacity-70 px-2 py-1 rounded text-white text-sm">
                Глава {{ chapter }}
            </p>

            <!-- Оверлей при наведении -->
            <div
                class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-30 transition-all duration-300 rounded">
            </div>
        </div>

        <!-- Название комикса -->
        <p class="mt-2 text-sm font-medium truncate max-w-[170px]" :title="title">
            {{ title }}
        </p>
    </div>
</template>