<template>
    <div class="app">
        <!-- Компонент ComicHeader -->
        <ComicHeader :chapterTitle="chapterTitle" :currentPage="currentPage + 1" :totalPages="totalPages">
            <!-- Контент вставляется в слот компонента -->
            <template #content>
                <!-- Загрузка PDF и отображение страниц -->
                <div class="pdf-container">
                    <!-- Индикатор загрузки -->
                    <div v-if="loading" class="loading">
                        Загрузка и обработка PDF... Подождите
                    </div>

                    <!-- Выбор файла PDF -->
                    <div class="file-input-container" v-if="!pages.length">
                        <input type="file" @change="uploadPdf" accept=".pdf" :disabled="loading" id="pdf-upload" />
                        <label for="pdf-upload" class="file-input-label">
                            Выберите PDF файл
                        </label>
                    </div>

                    <!-- Информация о файле -->
                    <div v-if="fileInfo" class="file-info">
                        <p>Файл: {{ fileInfo.filename }}</p>
                    </div>

                    <!-- Отображение страницы PDF с областями для перелистывания -->
                    <div v-if="pages.length" class="reader-container">
                        <!-- Левая область для клика (предыдущая страница) -->
                        <div class="nav-area left-area" @click="prevPage" :class="{ 'disabled': currentPage === 0 }">
                        </div>

                        <!-- Текущая страница -->
                        <img :src="pages[currentPage]" class="pdf-page" alt="PDF страница" />

                        <!-- Правая область для клика (следующая страница) -->
                        <div class="nav-area right-area" @click="nextPage"
                            :class="{ 'disabled': currentPage === pages.length - 1 }"></div>
                    </div>

                    <!-- Сообщение об ошибке -->
                    <div v-if="error" class="error-message">
                        {{ error }}
                    </div>
                </div>
            </template>
        </ComicHeader>
    </div>
</template>

<script>
import axios from "axios";
import ComicHeader from "../components/Page/comicP.vue";

export default {
    name: 'PdfViewer',
    components: {
        ComicHeader
    },
    data() {
        return {
            pages: [],
            fileInfo: null,
            loading: false,
            error: null,
            currentPage: 0,
            chapterTitle: 'Название Главы',
            totalPages: 12
        };
    },
    methods: {
        async uploadPdf(event) {
            const file = event.target.files[0];
            if (!file) return;

            // Сброс состояния
            this.pages = [];
            this.fileInfo = null;
            this.error = null;
            this.loading = true;
            this.currentPage = 0;

            const formData = new FormData();
            formData.append("file", file);

            try {
                const res = await axios.post("http://localhost:8080/api/upload-pdf", formData, {
                    headers: {
                        "Content-Type": "multipart/form-data"
                    },
                    timeout: 300000 // 5 минут на обработку больших файлов
                });

                console.log("Ответ от сервера:", res.data);

                if (res.data.error) {
                    this.error = res.data.error;
                } else if (res.data.pages && res.data.pages.length) {
                    this.pages = res.data.pages;
                    this.fileInfo = {
                        filename: res.data.filename,
                        totalPages: res.data.totalPages
                    };
                    this.totalPages = res.data.totalPages;
                } else {
                    this.error = "В PDF нет страниц или неверный формат ответа";
                }
            } catch (err) {
                console.error("Ошибка загрузки PDF:", err);

                if (err.code === 'ECONNABORTED') {
                    this.error = "Превышено время ожидания. Файл слишком большой.";
                } else if (err.response) {
                    this.error = err.response.data?.error || err.response.data || err.message;
                } else {
                    this.error = err.message;
                }
            } finally {
                this.loading = false;
                // Очищаем input, чтобы можно было загрузить тот же файл снова
                event.target.value = '';
            }
        },

        prevPage() {
            if (this.currentPage > 0) {
                this.currentPage--;
            }
        },

        nextPage() {
            if (this.currentPage < this.pages.length - 1) {
                this.currentPage++;
            }
        },

        // Обработка клавиш клавиатуры
        handleKeyDown(e) {
            if (e.key === 'ArrowLeft') {
                this.prevPage();
            } else if (e.key === 'ArrowRight') {
                this.nextPage();
            }
        }
    },
    mounted() {
        // Добавляем обработчик клавиатуры
        window.addEventListener('keydown', this.handleKeyDown);
    },
    beforeDestroy() {
        // Удаляем обработчик при уничтожении компонента
        window.removeEventListener('keydown', this.handleKeyDown);
    }
};
</script>

<style scoped>
.app {
    font-family: sans-serif;
}

.pdf-container {
    width: 100%;
    min-height: 1050px;
    /* Чуть меньше h-[1100px] из компонента */
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.file-input-container {
    margin: 20px 0;
}

.file-input-label {
    display: inline-block;
    padding: 12px 24px;
    background-color: #4CAF50;
    color: white;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.file-input-label:hover {
    background-color: #45a049;
}

input[type="file"] {
    display: none;
}

.loading {
    padding: 20px;
    background-color: #e3f2fd;
    color: #1976d2;
    border-radius: 4px;
    margin: 10px 0;
    text-align: center;
}

.file-info {
    margin: 15px 0;
    padding: 10px;
    color: white;
    font-size: 14px;
}

.reader-container {
    position: relative;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.pdf-page {
    max-width: 100%;
    max-height: 1000px;
    object-fit: contain;
    border-radius: 4px;
}

.nav-area {
    position: absolute;
    top: 0;
    bottom: 0;
    width: 20%;
    cursor: pointer;
    z-index: 10;
}

.left-area {
    left: 0;
}

.right-area {
    right: 0;
}

.nav-area.disabled {
    cursor: not-allowed;
    opacity: 0.3;
}

.error-message {
    margin-top: 20px;
    padding: 15px;
    background-color: #ffebee;
    color: #c62828;
    border-radius: 4px;
    border: 1px solid #ef9a9a;
}

/* Адаптивность */
@media (max-width: 768px) {
    .nav-area {
        width: 15%;
    }

    .pdf-page {
        max-height: 600px;
    }
}
</style>