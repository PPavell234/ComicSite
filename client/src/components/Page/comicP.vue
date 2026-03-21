<!-- components/Page/comicP.vue -->
<template>
    <div>
        <!-- Верхний хедер -->
        <div class="bg-[#353333] w-full h-[50px] flex items-center justify-start px-4 space-x-4">
            <!-- Левая часть: Иконка назад -->
            <div class="ml-5">
                <img src="/comicP/IconBack.svg" alt="back">
            </div>

            <!-- Средняя часть: Иконки и название главы -->
            <div class="flex items-center space-x-2 ml-50 gap-9 text-white">
                <img src="/comicP/IconT.svg" alt="icon1">
                <p>{{ chapterTitle }}</p>
                <img src="/comicP/IconT2.svg" alt="icon2">
            </div>

            <!-- Правая часть -->
            <div class="flex items-center space-x-2 mr-12" style="margin-left: auto;">
                <img src="/comicP/IconT3.svg" alt="icon3">
                <img src="/comicP/IconT5.svg" alt="icon4">
            </div>
        </div>

        <!-- Центральный контент -->
        <div class="w-full flex">
            <!-- Левая область (черный фон) -->
            <div class="bg-black w-120 flex-shrink-0"></div>

            <!-- Центральная область с контентом -->
            <div class="flex-1  bg-[#353333] h-[1100px] relative flex items-center justify-center">
                <!-- Отображение текущей страницы PDF с областями для перелистывания -->
                <div v-if="pages && pages.length > 0"
                    class="reader-container w-full h-full flex items-center justify-center">
                    <!-- Левая область для клика (предыдущая страница) -->
                    <div class="nav-area left-area" @click="prevPage" :class="{ 'disabled': currentPageIndex === 0 }">
                        <div class="nav-hint left-hint" v-if="currentPageIndex > 0">←</div>
                    </div>

                    <!-- Текущая страница -->
                    <img :src="pages[currentPageIndex]" class="pdf-page max-h-full max-w-full object-contain"
                        :alt="'Страница ' + (currentPageIndex + 1)" />

                    <!-- Правая область для клика (следующая страница) -->
                    <div class="nav-area right-area" @click="nextPage"
                        :class="{ 'disabled': currentPageIndex === pages.length - 1 }">
                        <div class="nav-hint right-hint" v-if="currentPageIndex < pages.length - 1">→</div>
                    </div>
                </div>

                <!-- Слот для контента (если нет страниц) -->
                <slot name="content" v-else></slot>
            </div>

            <!-- Правая область (черный фон) -->
            <div class="bg-black w-120 flex-shrink-0"></div>
        </div>
        <!-- Нижний блок с пагинацией - ПОКАЗЫВАЕТ ТЕКУЩУЮ СТРАНИЦУ И ВСЕГО СТРАНИЦ В ФАЙЛЕ -->
        <div class="text-white pt-12  pb-12 flex items-center justify-center  w-full flex gap-4 bg-[black]">
            <span class="text-lg ">
                {{ currentPageIndex + 1 }} / {{ pages.length }}
            </span>
        </div>


        <!-- Комментарии -->
        <comment></comment>


    </div>



</template>

<script setup>
import Comment from './comment.vue';

</script>

<script>

export default {
    name: 'ComicHeader',
    props: {
        chapterTitle: {
            type: String,
            default: 'Название Главы'
        },
        currentPage: {
            type: [Number, String],
            default: 1
        },
        totalPages: {
            type: [Number, String],
            default: 12
        },
        // Проп для страниц PDF
        pages: {
            type: Array,
            default: () => []
        }
    },
    data() {
        return {
            currentPageIndex: 0 // 0-индексация для массива
        }
    },
    watch: {
        // Сбрасываем на первую страницу при загрузке нового PDF
        pages: {
            handler(newPages) {
                this.currentPageIndex = 0;
            },
            immediate: true
        }
    },
    methods: {
        prevPage() {
            if (this.currentPageIndex > 0) {
                this.currentPageIndex--;
            }
        },
        nextPage() {
            if (this.currentPageIndex < this.pages.length - 1) {
                this.currentPageIndex++;
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
        window.addEventListener('keydown', this.handleKeyDown);
    },
    beforeDestroy() {
        window.removeEventListener('keydown', this.handleKeyDown);
    }
}
</script>

<style scoped>
.reader-container {
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.pdf-page {
    max-width: 100%;
    max-height: 100%;
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
    display: flex;
    align-items: center;
    justify-content: center;
}

.left-area {
    left: 0;
}

.right-area {
    right: 0;
}

.nav-hint {
    background: rgba(0, 0, 0, 0.5);
    color: white;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    opacity: 0;
    transition: opacity 0.3s;
}

.nav-area:hover .nav-hint {
    opacity: 1;
}

.nav-area.disabled {
    cursor: not-allowed;
    opacity: 0.3;
}


.pagination-btn {
    background: none;
    border: none;
    color: white;
    font-size: 1.25rem;
    cursor: pointer;
    padding: 0 0.5rem;
}

.pagination-btn:hover:not(:disabled) {
    color: #ffd700;
}

.opacity-50 {
    opacity: 0.5;
}

.cursor-not-allowed {
    cursor: not-allowed;
}

/* Адаптивность */
@media (max-width: 768px) {
    .nav-area {
        width: 15%;
    }
}
</style>