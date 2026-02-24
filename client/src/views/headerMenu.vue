<template>
    <div class="bg-gray-800 min-h-screen text-white">
        <div class="flex flex-row ml-[193px] mr-[193px]">
            <!-- Heder-->
            <div class="Heder flex flex-row mt-8">
                <img src="/imagePage/headerMenu/Logo.png" alt="">
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
                <div class="flex flex-row ml-[138px] gap-30">
                    <ul>
                        <li class="flex items-center space-x-2">
                            <img src="/imagePage/headerMenu/Icon-2.png" alt="" class="w-3 h-3">
                            <p>Заказать комикс</p>
                        </li>
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

        <!-- Второй блок -->
        <div class="bg-white min-h-screen ml-[117px] mr-[229px] mt-38">
            <div class="flex flex-row ml-[73px] mr-[229px]">
                <div class="mt-10">
                    <div class="bg-gray-500 w-[259px] h-[72px]"></div>
                    <h3 class="text-black mt-30">Загрузить комикс</h3>

                    <!-- Текстовое поле с отображением количества символов -->
                    <div
                        class="flex items-center bg-yellow-500 text-white py-1 px-3 mt-12 w-[600px] h-[45px] rounded-full">
                        <textarea v-model="textInput" @input="updateCharacterCount"
                            class="w-full h-full bg-yellow-500 text-white p-2 rounded-full resize-none outline-none"
                            placeholder="Титульник(Название)*" maxlength="100"></textarea>

                        <!-- Количество символов -->
                        <span class="text-sm text-white ml-2">{{ charCount }}/100</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'

const textInput = ref('')
const charCount = ref(0)

function updateCharacterCount() {
    charCount.value = textInput.value.length
}
</script>