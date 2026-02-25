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
        <div class="bg-[#464343] min-h-screen ml-[117px] mr-[229px] mt-38">
            <div class="flex flex-row ml-[73px] mr-[229px]">
                <div class="mt-10">
                    <div class="bg-gray-500 w-[259px] h-[72px]"></div>
                    <h3 class="text-white mt-30">Загрузить комикс</h3>

                    <!-- Текстовое поле с отображением количества символов для первого поля -->
                    <div
                        class="flex items-center bg-[#D2D2D2] text-white py-1 px-3 mt-12 w-[600px] h-[45px] rounded-full">
                        <textarea v-model="textInput1" @input="updateCharacterCount1"
                            class="w-full h-full bg-[#D2D2D2] text-white p-2 rounded-full resize-none outline-none"
                            placeholder="Титульник(Название)*" maxlength="100"></textarea>

                        <!-- Количество символов -->
                        <span class="text-sm text-white ml-2">{{ charCount1 }}/100</span>
                    </div>

                    <!-- Текстовое поле 2 с отображением количества символов для первого поля -->
                    <div class="text-field-container mt-10">
                        <div class="text-field-header">
                            <!-- Иконка -->
                            <img src="/imagePage/headerMenu/info.svg" alt="Иконка" class="icon">
                            <!-- Название -->
                            <span class="title title text-black">Описание</span>
                        </div>

                        <!-- Текстовое поле -->
                        <textarea class="text-area bg-white text-black" placeholder="Про что-то бла бла бла"></textarea>
                    </div>

                    <!-- Текстовое поле 2 с отображением количества символов для первого поля -->
                    <div class="text-field-container mt-10">
                        <div class="text-field-header">
                            <!-- Иконка -->
                            <img src="/imagePage/headerMenu/info.svg" alt="Иконка" class="icon">
                            <!-- Название -->
                            <span class="title title text-black">Теги</span>
                        </div>

                        <!-- Текстовое поле -->
                        <textarea class="text-area bg-white text-black" placeholder="Про что-то бла бла бла"></textarea>
                    </div>

                </div>
            </div>
        </div>
    </div>

</template>

<script setup>



import { ref } from 'vue'

//Количетсво символов для 

const textInput1 = ref('')  // Для первого текстового поля
const charCount1 = ref(0)   // Счётчик символов для первого текстового поля

const textInput2 = ref('')  // Для второго текстового поля
const charCount2 = ref(0)   // Счётчик символов для второго текстового поля

function updateCharacterCount1() {
    charCount1.value = textInput1.value.length
}

function updateCharacterCount2() {

    charCount2.value = textInput2.value.length
}

//Отображение

</script>

<style scoped>
.text-area::placeholder {
    color: #16181b;

    opacity: 1;
}

.text-field-container {
    width: 600px;
    padding: 10px;
    background-color: #ffffff;
    border-radius: 8px;
    border: 1px solid #ffffff;
}

.text-field-header {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
}

.icon {
    width: 20px;
    height: 20px;
    margin-right: 8px;
}

.title {
    font-weight: bold;
    font-size: 16px;
    color: #020202;
}

.text-area {
    width: 100%;
    height: 100px;
    padding: 10px;
    font-size: 14px;
    border: 1px solid #777777;
    border-radius: 4px;
    resize: none;
}
</style>