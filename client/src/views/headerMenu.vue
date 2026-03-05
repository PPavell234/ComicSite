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
        <div class="bg-[#464343] min-h-screen ml-[200px] mr-[300px] mt-38 pb-12 ">
            <div class="flex flex-row ml-[60px] mr-[300px] gap-55">

                <!-- ЛЕВАЯ КОЛОНКА (БЕЗ ИЗМЕНЕНИЙ) -->
                <div class="mt-10">

                    <div class="bg-gray-500 w-[259px] h-[72px]"></div>
                    <h3 class="text-white mt-30">Загрузить комикс</h3>

                    <!-- Титульник -->
                    <div
                        class="flex items-center bg-[#D2D2D2] text-white py-1 px-3 mt-12 w-[600px] h-[45px] rounded-full">
                        <textarea v-model="textInput1" @input="updateCharacterCount1"
                            class="w-full h-full bg-[#D2D2D2] text-black p-2 rounded-full resize-none outline-none"
                            placeholder="Титульник(Название)*" maxlength="100"></textarea>

                        <span class="text-sm text-black ml-2">
                            {{ charCount1 }}/100
                        </span>
                    </div>

                    <!-- Описание -->
                    <div class="text-field-container mt-10">
                        <div class="text-field-header">
                            <img src="/imagePage/headerMenu/info.svg" alt="Иконка" class="icon">
                            <span class="title title text-black">Описание</span>
                        </div>

                        <textarea class="text-area bg-white text-black" placeholder="Про что-то бла бла бла"></textarea>
                    </div>

                    <!-- Теги -->
                    <div class="text-field-container mt-10">
                        <div class="text-field-header">
                            <img src="/imagePage/headerMenu/info.svg" alt="Иконка" class="icon">
                            <span class="title text-black">Теги</span>
                        </div>

                        <textarea class="text-area text-black" placeholder="Введите теги, разделяя их пробелом"
                            v-model="textInput" @keyup="addTagOnSpace"></textarea>

                        <div class="tags-container mt-4">
                            <div v-for="(tag, index) in tags" :key="index" class="tag">
                                <span>{{ tag }}</span>
                                <button @click="removeTag(index)" class="close-btn">X</button>
                            </div>
                        </div>
                    </div>

                    <!-- Переводчик -->
                    <div
                        class="flex items-center bg-[#D2D2D2] text-white py-1 px-3 mt-12 w-[600px] h-[45px] rounded-full">
                        <textarea v-model="textInput2" @input="updateCharacterCount2"
                            class="w-full h-full bg-[#D2D2D2] text-black p-2 rounded-full resize-none outline-none"
                            placeholder="Переводчик(Это перевод)//Или Свой комикс имя свое*" maxlength="100"></textarea>

                        <span class="text-sm text-black ml-2">
                            {{ charCount2 }}/100
                        </span>
                    </div>

                    <!-- Художник -->
                    <div
                        class="flex items-center bg-[#D2D2D2] text-white py-1 px-3 mt-12 w-[600px] h-[45px] rounded-full">
                        <textarea v-model="textInput3" @input="updateCharacterCount3"
                            class="w-full h-full bg-[#D2D2D2] text-black p-2 rounded-full resize-none outline-none"
                            placeholder="Художник(имя)*" maxlength="100"></textarea>

                        <span class="text-sm text-black ml-2">
                            {{ charCount3 }}/100
                        </span>
                    </div>

                    <!-- Кнопка -->
                    <div class="mt-12 flex justify-center">
                        <button
                            class="flex items-center bg-[#D2D2D2] text-white py-1 px-3 rounded-full hover:bg-yellow-300 hover:scale-105 transition-all">
                            <img src="/imagePage/button-icon/upload-2-line.svg" alt="" class="w-4 h-4 mr-2">
                            <p>Опубликовать</p>
                        </button>
                    </div>

                </div>

                <!-- ПРАВАЯ КОЛОНКА -->
                <div class="mt-80">
                    <div class="flex justify-left gap-3 relative -top-6 ">
                        <img src="/imagePage/button-icon/upload-2-line.svg" alt="">
                        <p>обложка</p>
                    </div>

                    <!-- Обложка -->
                    <div class="bg-gray-600 w-[440px] h-[660px] rounded-xl flex items-center justify-center">
                        <p class="text-white">Обложка</p>
                    </div>

                    <!-- Название комикса -->
                    <div class="mt-3">
                        <p class="text-white text-xl font-semibold break-words">
                            {{ textInput1 || 'Название комикса' }}
                        </p>
                    </div>

                    <!-- Ваш красивый div для загрузки PDF -->
                    <div class="bg-[#D2D2D2] w-full h-[120px] mt-3 rounded-xl flex flex-col items-center justify-center cursor-pointer"
                        :class="{ 'opacity-50': loading }" @click="triggerFileUpload" @dragover.prevent
                        @drop.prevent="handleDrop">
                        <!-- Скрытый input -->
                        <input type="file" @change="uploadPdf" accept=".pdf" :disabled="loading" class="hidden"
                            id="pdf-upload-input" />

                        <!-- Блок с иконкой и текстом -->
                        <div class="flex flex-center items-center gap-3">
                            <img src="/imagePage/button-icon/upload-2-line.svg" alt="">
                            <p class="mt-2 text-sm text-gray-700">
                                {{ loading ? 'Загрузка...' : 'Загрузить PDF файлы' }}
                            </p>
                        </div>

                        <!-- Второй текст -->
                        <p class="mt-2 text-sm text-gray-700">
                            {{ loading ? 'Пожалуйста, подождите...' : 'Drag and drop PDF here or click to upload' }}
                        </p>
                    </div>

                </div>
            </div>
        </div>

        <!-- кк -->
        <div class="bg-gray-800 h-[50px] "></div>

        <div class="bg-[#464343] h-[130px] ml-[200px] mr-[300px] flex justify-center items-center pt-12 gap-2">
            <h1>Пред просмотр Страниц</h1>
            <img src="/imagePage/headerMenu/eas.svg" alt="">
        </div>

        <div class="bg-gray-800 h-[50px] "></div>

        <!-- Компонент ComicP получает загруженные страницы -->
        <ComicP :pages="pdfPages" :loading="loading"></ComicP>

    </div>
</template>

<script setup>
import { ref } from 'vue'
import ComicP from '../components/Page/comicP.vue'
import axios from 'axios' // Добавляем axios для реальных запросов

//Количетсво символов для 
const textInput1 = ref('')
const charCount1 = ref(0)
const textInput2 = ref('')
const charCount2 = ref(0)
const textInput3 = ref('')
const charCount3 = ref(0)

function updateCharacterCount1() {
    charCount1.value = textInput1.value.length
}
function updateCharacterCount2() {
    charCount2.value = textInput2.value.length
}
function updateCharacterCount3() {
    charCount3.value = textInput3.value.length
}

//Создание тегов для поля с тегами
const textInput = ref('')
const tags = ref([])

function addTagOnSpace(event) {
    if (event.key === ' ' && textInput.value.trim()) {
        const newTag = textInput.value.trim()
        if (!tags.value.includes(newTag)) {
            tags.value.push(newTag)
        }
        textInput.value = ''
    }
}

function removeTag(index) {
    tags.value.splice(index, 1)
}

// ЗАГРУЗКА PDF - исправленная версия
const loading = ref(false)
const pdfPages = ref([]) // Переименовано с pages на pdfPages для ясности
const fileInfo = ref(null)
const error = ref(null)

// Функция для открытия диалога выбора файла
const triggerFileUpload = () => {
    if (!loading.value) {
        document.getElementById('pdf-upload-input').click()
    }
}

// Функция загрузки PDF (с реальным запросом к серверу)
const uploadPdf = async (event) => {
    const file = event.target.files[0]
    if (!file) return

    if (file.type !== 'application/pdf') {
        alert('Пожалуйста, выберите PDF файл')
        return
    }

    // Сброс состояния
    pdfPages.value = []
    fileInfo.value = null
    error.value = null
    loading.value = true

    const formData = new FormData()
    formData.append("file", file)

    try {
        // Реальный запрос к вашему серверу
        const res = await axios.post("http://localhost:8080/api/upload-pdf", formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            },
            timeout: 300000 // 5 минут
        })

        console.log("Ответ от сервера:", res.data)

        if (res.data.error) {
            error.value = res.data.error
            alert('Ошибка: ' + res.data.error)
        } else if (res.data.pages && res.data.pages.length) {
            // Сохраняем страницы
            pdfPages.value = res.data.pages
            fileInfo.value = {
                filename: res.data.filename,
                totalPages: res.data.totalPages
            }

            alert(`PDF успешно загружен! ${res.data.totalPages} страниц`)
        } else {
            error.value = "В PDF нет страниц или неверный формат ответа"
            alert(error.value)
        }
    } catch (err) {
        console.error("Ошибка загрузки PDF:", err)

        if (err.code === 'ECONNABORTED') {
            error.value = "Превышено время ожидания. Файл слишком большой."
        } else if (err.response) {
            error.value = err.response.data?.error || err.response.data || err.message
        } else {
            error.value = err.message
        }
        alert('Ошибка: ' + error.value)
    } finally {
        loading.value = false
        // Очищаем input
        event.target.value = ''
    }
}

// Обработка перетаскивания файла
const handleDrop = (event) => {
    const file = event.dataTransfer.files[0]
    if (file && file.type === 'application/pdf') {
        const fakeEvent = {
            target: {
                files: [file]
            }
        }
        uploadPdf(fakeEvent)
    } else {
        alert('Пожалуйста, выберите PDF файл')
    }
}
</script>

<style scoped>
/* Это для ввода 2 (описнаие) */
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

/* Это для Тегов стиль */
.tags-container {
    display: flex;
    flex-wrap: wrap;
    margin-top: 10px;
}

.tag {
    background-color: #6e6e6e;
    border-radius: 12px;
    padding: 5px 10px;
    margin: 5px;
    display: flex;
    align-items: center;
}

.close-btn {
    background: none;
    border: none;
    color: rgb(8, 8, 8);
    cursor: pointer;
    margin-left: 8px;
}

.opacity-50 {
    opacity: 0.5;
    cursor: not-allowed;
}

.hidden {
    display: none;
}
</style>