<template>
    <div>
        <!-- Комментарии -->
        <div class="flex flex-row bg-black text-white gap-4 justify-end pr-80">
            <span class="flex items-center gap-2 bg-[#353333] p-2 rounded">
                <div class="mr-4"></div>
                <img src="/comicP/IconEm.svg" alt="">
                <p>Правила</p>
                <div class="mr-4"></div>
            </span>
        </div>
        <div class="w-full flex">
            <!-- Левая область (черный фон) -->
            <div class="bg-black w-90 flex-shrink-0"></div>



            <!-- Центральная область с контентом -->
            <div class="flex-1 bg-black h-[1100px] ">
                <div class="bg-[#353333] p-3 mt-12 mr-12 ml-12 text-white ">Написать комментарий</div>

                <!-- Формат комментария -->
                <div class="flex justify-between items-start text-white mt-12">
                    <!-- Левая часть: всё кроме лайков -->
                    <div class="flex-1 ml-12">
                        <div class="flex items-center gap-2">
                            <img src="/comicP/ProfileIcon.svg" alt="">
                            <p>Имя</p>
                            <p class="ml-4 text-[#353333]">2 месяца назад</p>
                        </div>
                        <div class="mt-2">
                            <p>бюджет 144р</p>
                        </div>
                        <div class="flex items-center gap-6 mt-2 text-red-500">
                            <p>Ответить</p>
                            <p>Жалоба</p>
                        </div>
                    </div>

                    <div class="flex gap-12 mr-12">
                        <!-- Элемент слева (текст снизу) -->
                        <div class="flex flex-col items-center gap-1">
                            <img src="/comicP/dislike.svg" alt="">
                            <p>0</p>
                        </div>

                        <!-- Элемент справа (лайк снизу) -->
                        <div class="flex flex-col items-center gap-1">
                            <img src="/comicP/like.svg" alt="">
                            <p>0</p>
                        </div>
                    </div>
                </div>

            </div>

            <!-- Правая область (черный фон) -->
            <div class="bg-black w-90 flex-shrink-0"></div>
        </div>


    </div>

</template>