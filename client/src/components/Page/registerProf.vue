<template>
    <div class="bg-gray-800">
        <HeaderMenu></HeaderMenu>

        <div class="flex">
            <!-- форма для регистрации -->
            <div class="custom-margin bg-[#353333] text-white pl-7 pt-7 max-w-[520px] h-[1400px] p">
                <p class="mt-4 text-[22px] font-bold">Добро пожаловать в Comic-Fab</p>

                <!-- Email -->
                <ul>
                    <div class="text-field-header mt-8">
                        <span class="title text-white">Адрес электронной почты</span>
                    </div>
                    <input type="email" v-model="form.email"
                        class="bg-white text-black w-[350px] h-[40px] mt-3 rounded-none resize-none pl-2"
                        placeholder="Email">
                    <p v-if="errors.email" class="text-red-500 text-sm mt-1">{{ errors.email }}</p>
                </ul>

                <!-- Никнейм -->
                <ul>
                    <div class="text-field-header mt-8">
                        <span class="title text-white">Никнейм</span>
                    </div>
                    <input type="text" v-model="form.nickname"
                        class="bg-white text-black w-[350px] h-[40px] mt-3 rounded-none resize-none pl-2"
                        placeholder="Введите ваш ник">
                    <p v-if="errors.nickname" class="text-red-500 text-sm mt-1">{{ errors.nickname }}</p>
                </ul>

                <!-- Пароль -->
                <ul>
                    <div class="text-field-header mt-6">
                        <span class="title text-white">Пароль</span>
                    </div>
                    <div class="relative w-[350px]">
                        <input :type="showPassword ? 'text' : 'password'" v-model="form.password"
                            class="bg-white text-black w-full h-[40px] mt-3 rounded-none resize-none pl-2 pr-10"
                            placeholder="Пароль">
                        <img @click="showPassword = !showPassword" src="/imagePage/registerProf/eye.svg" alt="eye"
                            class="absolute right-6 top-8 transform -translate-y-1/2 w-4 h-4 cursor-pointer">
                    </div>
                    <p v-if="errors.password" class="text-red-500 text-sm mt-1">{{ errors.password }}</p>
                </ul>

                <!-- Повторить пароль -->
                <ul>
                    <div class="text-field-header mt-6">
                        <span class="title text-white">Повторить пароль</span>
                    </div>
                    <div class="relative w-[350px]">
                        <input :type="showConfirmPassword ? 'text' : 'password'" v-model="form.confirmPassword"
                            class="bg-white text-black w-full h-[40px] mt-3 rounded-none resize-none pl-2 pr-10"
                            placeholder="Повторите пароль">
                        <img @click="showConfirmPassword = !showConfirmPassword" src="/imagePage/registerProf/eye.svg"
                            class="absolute right-6 top-8 transform -translate-y-1/2 w-4 h-4 cursor-pointer">
                    </div>
                    <p v-if="errors.confirmPassword" class="text-red-500 text-sm mt-1">{{ errors.confirmPassword }}</p>
                </ul>



                <!-- Кнопка регистрации -->
                <button @click="handleRegister"
                    class="flex items-center justify-center mt-11 bg-yellow-300 text-black py-1 px-3 w-[350px] h-[50px] hover:bg-yellow-400 hover:scale-105 transition-all text-center"
                    :disabled="loading">
                    <p class="w-full font-bold">{{ loading ? 'Регистрация...' : 'Зарегистрироваться' }}</p>
                </button>



                <p class="mt-12 text-left text-sm text-gray-400">Продолжая, вы принимаете Условия предоставление услуг
                    Comic Fab и подтверждаете, что ознакомились с нашей Политикой конфиденциальности. Уведомление.</p>
            </div>

            <!-- Картинка справа -->
            <img src="/imagePage/register/image4.svg" alt=""
                class="w-[1017px] h-[1400px] object-cover object-[center_10%]">
        </div>

        <!-- Footer -->
        <div class="bg-[#353333] flex pt-2 pl-80 pb-2 gap-30 text-white">
            <div>
                <img src="/imagePage/register/Icon2.svg" alt="" class="w-[120px] h-auto">
            </div>
            <div>
                <div class="flex flex-row">
                    <p class="mr-7">Каталог</p>
                    <ul class="space-y-1">
                        <li><a href="#" class="text-gray-400 hover:text-gray-200 transition">Marvel</a></li>
                        <li><a href="#" class="text-gray-400 hover:text-gray-200 transition">DC</a></li>
                        <li><a href="#" class="text-gray-400 hover:text-gray-200 transition">Communities</a></li>
                    </ul>
                </div>
                <div>
                    <p>В случаях нарушения авторских прав - обращайтесь на почту info@comicFab.me</p>
                    <p>По вопросам сотрудничества и PR - pr@comicFab.me</p>
                </div>
            </div>
            <div>
                <p>Контакты</p>
                <p>fdsfsdf@gmail.com</p>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import HeaderMenu from './headerMenu.vue'

const router = useRouter()
const loading = ref(false)
const showPassword = ref(false)
const showConfirmPassword = ref(false)

const form = reactive({
    email: '',
    nickname: '',
    password: '',
    confirmPassword: ''
})

const errors = reactive({
    email: '',
    nickname: '',
    password: '',
    confirmPassword: ''
})

// Валидация формы
const validateForm = () => {
    let isValid = true

    // Проверка email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!form.email) {
        errors.email = 'Email обязателен'
        isValid = false
    } else if (!emailRegex.test(form.email)) {
        errors.email = 'Введите корректный email'
        isValid = false
    } else {
        errors.email = ''
    }

    // Проверка никнейма
    if (!form.nickname) {
        errors.nickname = 'Никнейм обязателен'
        isValid = false
    } else if (form.nickname.length < 3) {
        errors.nickname = 'Никнейм должен быть не менее 3 символов'
        isValid = false
    } else {
        errors.nickname = ''
    }

    // Проверка пароля
    if (!form.password) {
        errors.password = 'Пароль обязателен'
        isValid = false
    } else if (form.password.length < 6) {
        errors.password = 'Пароль должен быть не менее 6 символов'
        isValid = false
    } else {
        errors.password = ''
    }

    // Проверка подтверждения пароля
    if (form.password !== form.confirmPassword) {
        errors.confirmPassword = 'Пароли не совпадают'
        isValid = false
    } else {
        errors.confirmPassword = ''
    }

    return isValid
}

// Отправка формы
const handleRegister = async () => {
    if (!validateForm()) return

    loading.value = true

    try {
        const response = await axios.post('http://localhost:8080/api/auth/register', {
            email: form.email,
            password: form.password,
            nickname: form.nickname
        })

        if (response.data.token) {
            localStorage.setItem('token', response.data.token)
            localStorage.setItem('user', JSON.stringify({
                email: response.data.email,
                nickname: response.data.nickname
            }))
            alert('Регистрация успешна!')
            router.push('/')
        }
    } catch (error) {
        console.error('Ошибка регистрации:', error)
        alert(error.response?.data?.message || 'Ошибка при регистрации')
    } finally {
        loading.value = false
    }
}
</script>

<style scoped>
.title {
    font-weight: bold;
    font-size: 16px;
}

.custom-margin {
    margin-left: 193.5px;
}

input {
    outline: none;
}

input:focus {
    outline: 1px solid #eab308;
}
</style>