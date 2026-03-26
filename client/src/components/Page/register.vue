<template>
    <div class=" bg-gray-800">
        <HeaderMenu></HeaderMenu>

        <div class="flex">
            <!-- форма для заполнения комикса -->
            <div class="custom-margin bg-[#353333] text-white pl-7 pt-7 max-w-[520px] h-[1400px] p">
                <p class="mt-4 text-[22px] font-bold">Добро пожаловать в Comic-Fab</p>
                <ul>
                    <div class="text-field-header mt-8">
                        <span class="title text-white">Адрес электронной почты</span>
                    </div>
                    <textarea class="bg-white text-black w-[350px] h-[40px] mt-3 rounded-none resize-none pl-2 pt-2"
                        placeholder="Адрес электронной почты"></textarea>
                </ul>

                <ul>
                    <div class="text-field-header mt-6">
                        <span class="title text-white">Пароль</span>
                    </div>
                    <textarea class="bg-white text-black w-[350px] h-[40px] mt-3 rounded-none resize-none pl-2 pt-2"
                        placeholder="Пароль"></textarea>
                </ul>

                <a href="#" class="mt-4 text-blue-500 hover:text-blue-700 hover:underline">
                    Забыли пароль ?
                </a>

                <button
                    class="flex items-center justify-center mt-4 bg-yellow-300 text-black py-1 px-3 w-[350px] h-[50px] hover:bg-yellow-300 hover:scale-105 transition-all text-center"
                    :class="{ 'opacity-50 cursor-not-allowed': isPublishing }">
                    <p class="w-full font-bold">Войти</p>
                </button>
                <div class="mt-3">
                    <p class="mr-3">Еще не зарегистрировались в Comic-Fab ?</p>
                    <a href="#" class="mt-4 text-blue-500 hover:text-blue-700 hover:underline">
                        Регистарция
                    </a>
                </div>

                <p class="mt-12 text-left">Продолжая, вы принимаете Условия предоставление услуг Comic Fab и
                    подтверждаете, что ознакомились с нашей Политикой конфиденциальности. Уведомление.</p>
            </div>

            <!-- Смена картинок (справа) -->
            <img src="/imagePage/register/image4.svg" alt=""
                class="w-[1017px] h-[1400px] object-cover object-[center_10%]">
        </div>
        <!-- fuuters -->
        <div class="bg-[#353333] flex pt-2 pl-80 pb-2 gap-30 text-white">
            <div>
                <img src="/imagePage/register/Icon2.svg" alt="" class="w-[120px] h-auto">
            </div>
            <div>
                <div class="flex flex-row">
                    <p class="mr-7">Каталог</p>
                    <ul class="space-y-1">
                        <li>
                            <a href="#" class="text-gray-400 hover:text-gray-200 hover:underline transition">
                                Marvel
                            </a>
                        </li>
                        <li>
                            <a href="#" class="text-gray-400 hover:text-gray-200 hover:underline transition">
                                DC
                            </a>
                        </li>
                        <li>
                            <a href="#" class="text-gray-400 hover:text-gray-200 hover:underline transition">
                                Communities
                            </a>
                        </li>
                    </ul>
                </div>
                <div>
                    <p>В случаях нарушения авторских прав - обращайтесь на почту info@comicFab.me</p>
                    <p>По вопросам сотрудничества и PR - pr@comicFab.me</p>
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
import { reactive } from 'vue'
import HeaderMenu from './headerMenu.vue';


</script>



<style scoped>
/* Расстоние между кнопками */
.p {
    letter-spacing: 0.5px;
}

.custom-textarea {
    margin-top: 3px;
    display: block;
}

/* Стили для текстовых полей */
.text-field-header {}

.title {
    font-weight: bold;
    font-size: 16px;
}

.custom-margin {
    margin-left: 193.5px;
}
</style>