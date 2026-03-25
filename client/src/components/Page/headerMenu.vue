<template>
    <div class="flex flex-row ml-[193px] mr-[193px] bg-gray-800 text-white">
        <!-- Heder-->
        <div class="Heder flex flex-row mt-8">
            <div class="px-10 pb-4">
                <img src="/imagePage/headerMenu/Logo.png" alt="">
            </div>
            <div class="flex flex-row ml-[350px] gap-20">
                <ul>
                    <li class="flex items-center space-x-2">
                        <img src="/imagePage/headerMenu/Icon.png" alt="" class="w-3 h-3">
                        <p>Каталог</p>
                    </li>
                </ul>
                <ul>
                    <li class="flex items-center space-x-2">
                        <img src="/imagePage/headerMenu/Icon-1.png" alt="" class="w-3 h-3">
                        <p>Поиск</p>
                    </li>
                </ul>
                <ul>
                    <li class="flex items-center space-x-2">
                        <img src="/imagePage/headerMenu/Icon-2.png" alt="" class="w-3 h-3">
                        <p>Форум</p>
                    </li>
                </ul>
            </div>
            <div class="flex flex-row ml-[100px] gap-9">
                <ul>
                    <li class="flex items-center space-x-2">
                        <img src="/imagePage/headerMenu/Icon-2.png" alt="" class="w-3 h-3">
                        <p>Заказать комикс</p>
                    </li>
                </ul>

                <ul>
                    <button @click="toggleTheme"
                        class="flex items-center text-white bg-[#353333] py-1 px-2 rounded-full hover:scale-105 transition-all h-8 pr-12">
                        <img :src="isDarkMode ? '/imagePage/headerMenu/IconNight.svg' : '/imagePage/headerMenu/IconSun.svg'"
                            alt="theme" class="w-7 h-7 mr-2 transition-all duration-300"
                            :class="{ 'translate-x-6': isDarkMode }">
                    </button>
                </ul>

                <ul>
                    <li class="flex items-center space-x-2">
                        <button
                            class="flex items-center bg-yellow-500 text-white py-1 px-3 rounded-full hover:bg-yellow-300 hover:scale-105 transition-all">
                            <img src="/imagePage/headerMenu/Icon-3.png" alt="" class="w-4 h-4 mr-2">
                            <p>Выход / Регистрация</p>
                        </button>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// Состояние темы
const isDarkMode = ref(false)

// Функция переключения темы
const toggleTheme = () => {
    isDarkMode.value = !isDarkMode.value

    // Сохраняем в localStorage
    localStorage.setItem('theme', isDarkMode.value ? 'dark' : 'light')

    // Применяем тему к body
    if (isDarkMode.value) {
        document.documentElement.classList.add('dark')
    } else {
        document.documentElement.classList.remove('dark')
    }
}

// Загрузка сохраненной темы
onMounted(() => {
    const savedTheme = localStorage.getItem('theme')
    if (savedTheme === 'dark') {
        isDarkMode.value = true
        document.documentElement.classList.add('dark')
    }
})
</script>

<style scoped>
/* Анимация для иконки */
.translate-x-6 {
    transform: translateX(1.5rem);
}
</style>