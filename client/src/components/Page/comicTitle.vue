<template>
    <div class="text-white cursor-pointer" @click="handleClick">
        <div class="relative w-[170px] h-[250px] rounded overflow-hidden bg-gray-700">
            <!-- Изображение комикса -->
            <img v-if="bg && bg !== '/imagePage/comic-list/default-cover.jpg'" :src="bg" :alt="title"
                class="w-full h-full object-cover" @error="onImageError" @load="onImageLoad">

            <!-- Заглушка если нет изображения -->
            <div v-else class="w-full h-full flex items-center justify-center bg-gray-600">
                <span class="text-white text-sm text-center px-2">{{ title }}</span>
            </div>

            <!-- Кнопка избранного с изменяемым цветом картинки -->
            <button @click.stop="handleFavoriteClick"
                class="absolute top-2 left-2 z-10 w-6 h-6 hover:scale-110 transition">
                <!-- Картинка Favorite.svg с изменяемым цветом через CSS фильтр -->
                <img :src="'/imagePage/comic-list/Favorite.svg'" alt="favorite"
                    class="w-full h-full transition-all duration-300" :class="{ 'favorite-active': isFavorite }">
            </button>

            <!-- Номер главы -->
            <div class="absolute bottom-2 left-2 bg-black bg-opacity-70 px-2 py-1 rounded z-10">
                <span class="text-white text-sm">Глава {{ chapter }}</span>
            </div>
        </div>

        <!-- Название комикса -->
        <p class="mt-2 text-sm font-medium truncate max-w-[170px]" :title="title">
            {{ title }}
        </p>
    </div>
</template>

<script setup>
import { ref } from 'vue'

// Определяем входные параметры
const props = defineProps({
    title: String,
    chapter: Number,
    bg: String,
    favorite: Boolean,  // true - в избранном, false - нет
    comicId: String
})

// Определяем события
const emit = defineEmits(['click', 'toggle-favorite'])

// Локальное состояние избранного (синхронизируется с пропсом)
const isFavorite = ref(props.favorite)

// Обработчик клика на весь компонент
const handleClick = () => {
    console.log('Клик по комиксу:', props.title)
    emit('click')
}

// Обработчик клика на избранное
const handleFavoriteClick = () => {
    console.log('Клик по избранному для:', props.title)
    // Меняем состояние
    isFavorite.value = !isFavorite.value
    // Отправляем событие родителю
    emit('toggle-favorite', props.comicId)
}

// Обработчик ошибки загрузки изображения
const onImageError = (e) => {
    console.log('Ошибка загрузки изображения:', props.bg)
    e.target.style.display = 'none'
    const parent = e.target.parentNode
    const fallback = document.createElement('div')
    fallback.className = 'w-full h-full flex items-center justify-center bg-gray-600'
    fallback.innerHTML = `<span class="text-white text-sm text-center px-2">${props.title}</span>`
    parent.appendChild(fallback)
}

// Обработчик успешной загрузки
const onImageLoad = () => {
    console.log('Изображение загружено:', props.bg)
}
</script>

<style scoped>
.favorite-active {
    filter: invert(85%) sepia(50%) saturate(1000%) hue-rotate(330deg) brightness(100%) contrast(100%);
}


/* Эффект при наведении */
.favorite-active:hover {
    filter: brightness(0) saturate(100%) invert(89%) sepia(50%) saturate(2000%) hue-rotate(340deg) brightness(110%) contrast(105%);
}
</style>