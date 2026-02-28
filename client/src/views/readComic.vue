<template>
    <div class="app">
        <h1>Загрузка PDF</h1>

        <!-- Индикатор загрузки -->
        <div v-if="loading" class="loading">
            Загрузка и обработка PDF... Подождите
        </div>

        <!-- Выбор файла PDF -->
        <input type="file" @change="uploadPdf" accept=".pdf" :disabled="loading" />

        <!-- Информация о файле -->
        <div v-if="fileInfo" class="file-info">
            <p>Файл: {{ fileInfo.filename }}</p>
            <p>Страниц: {{ fileInfo.totalPages }}</p>
        </div>

        <!-- Отображение страниц PDF -->
        <div v-if="pages.length" class="pages-container">
            <div v-for="(page, index) in pages" :key="index" class="page-wrapper">
                <p class="page-number">Страница {{ index + 1 }}</p>
                <img :src="page" class="pdf-page" alt="PDF страница" />
            </div>
        </div>

        <!-- Сообщение, если нет страниц -->
        <div v-else-if="!loading" class="empty-message">
            Выберите PDF-файл для просмотра страниц.
        </div>

        <!-- Сообщение об ошибке -->
        <div v-if="error" class="error-message">
            {{ error }}
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            pages: [],
            fileInfo: null,
            loading: false,
            error: null
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
        }
    }
};
</script>

<style scoped>
.app {
    max-width: 900px;
    margin: auto;
    font-family: sans-serif;
    padding: 20px;
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
    padding: 15px;
    background-color: #f5f5f5;
    border-radius: 4px;
}

.pages-container {
    display: flex;
    flex-direction: column;
    gap: 20px;
    margin-top: 20px;
    max-height: 80vh;
    overflow-y: auto;
    padding: 10px;
}

.page-wrapper {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 10px;
    background-color: #fafafa;
}

.page-number {
    margin: 0 0 10px 0;
    color: #666;
    font-size: 14px;
}

.pdf-page {
    width: 100%;
    border-radius: 4px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.empty-message {
    margin-top: 20px;
    padding: 40px;
    text-align: center;
    color: #666;
    background-color: #f5f5f5;
    border-radius: 4px;
}

.error-message {
    margin-top: 20px;
    padding: 15px;
    background-color: #ffebee;
    color: #c62828;
    border-radius: 4px;
    border: 1px solid #ef9a9a;
}
</style>