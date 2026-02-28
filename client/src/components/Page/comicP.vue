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
        <div class="mt-17 w-full flex items-center justify-between">
            <div><!-- пусто слева --></div>

            <div class="bg-[#353333] px-[500px] py-8 h-[1100px] space-x-4">
                <!-- Здесь будет контент страницы -->
                <slot name="content"></slot>
            </div>

            <div><!-- пусто справа --></div>
        </div>

        <!-- Нижний блок с пагинацией -->
        <div class="mt-12 flex items-center justify-center text-black">
            <p>{{ currentPage }}/{{ totalPages }}</p>
        </div>

        <div class="mt-60"></div>
    </div>
</template>

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
        }
    }
}
</script>