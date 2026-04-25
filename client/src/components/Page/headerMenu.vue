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

                <!-- Условное отображение кнопок -->
                <ul v-if="!isLoggedIn">
                    <li class="flex items-center space-x-2">
                        <button @click="goToRegister"
                            class="flex items-center bg-yellow-500 text-white py-1 px-3 rounded-full hover:bg-yellow-300 hover:scale-105 transition-all">
                            <img src="/imagePage/headerMenu/Icon-3.png" alt="" class="w-4 h-4 mr-2">
                            <p>Регистрация / Вход</p>
                        </button>
                    </li>
                </ul>

                <!-- Если пользователь авторизован - показываем профиль -->
                <ul v-if="isLoggedIn">
                    <li class="flex items-center space-x-2">
                        <div class="flex items-center space-x-2 gap-2 px-3 py-1 transform scale-88">
                            <!-- scale-75 = 75% от исходного размера -->
                            <div class="relative inline-block">
                                <img src="/imagePage/headerMenu/notifications.svg" alt="" class="w-8 h-8">
                                <div
                                    class="absolute -top-1 -right-1 bg-red-500 text-white text-[14px] font-bold rounded-full w-5 h-5 flex items-center justify-center">
                                    1
                                </div>
                            </div>
                            <div class="relative inline-block">
                                <img src="/imagePage/profileIcon/profile1.svg" alt="" class="w-8 h-8">
                                <div
                                    class="absolute -bottom-1 -right-1 bg-green-500 text-white text-[8px] font-bold rounded-full w-3 h-3 flex items-center justify-center">
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Состояние темы
const isDarkMode = ref(false)

// Состояние авторизации
const isLoggedIn = ref(false)
const userNickname = ref('')
const userAvatar = ref('')

// Функция переключения темы
const toggleTheme = () => {
    isDarkMode.value = !isDarkMode.value
    localStorage.setItem('theme', isDarkMode.value ? 'dark' : 'light')
    if (isDarkMode.value) {
        document.documentElement.classList.add('dark')
    } else {
        document.documentElement.classList.remove('dark')
    }
}

// Проверка авторизации
const checkAuth = () => {
    const token = localStorage.getItem('token')
    const user = localStorage.getItem('user')

    if (token && user) {
        isLoggedIn.value = true
        try {
            const userData = JSON.parse(user)
            userNickname.value = userData.nickname || userData.email?.split('@')[0] || 'User'
            userAvatar.value = userData.avatar || ''
        } catch (e) {
            userNickname.value = 'User'
        }
    } else {
        isLoggedIn.value = false
        userNickname.value = ''
        userAvatar.value = ''
    }
}

// Переход на регистрацию
const goToRegister = () => {
    router.push('/register')
}

// Выход из системы
const handleLogout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    isLoggedIn.value = false
    router.push('/')
}

// Загрузка сохраненной темы и проверка авторизации
onMounted(() => {
    const savedTheme = localStorage.getItem('theme')
    if (savedTheme === 'dark') {
        isDarkMode.value = true
        document.documentElement.classList.add('dark')
    }
    checkAuth()
})
</script>

<style scoped>
.translate-x-6 {
    transform: translateX(1.5rem);
}
</style>