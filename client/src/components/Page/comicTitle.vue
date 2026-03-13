<script setup>
defineProps({
    title: String,
    chapter: Number,
    bg: String,
    favorite: String
})
</script>

<template>

    <div class="text-white">

        <div class="relative w-[170px] h-[250px] bg-no-repeat bg-center bg-cover"
            :style="{ backgroundImage: `url(${bg})` }">

            <img :src="favorite" alt="" class="absolute top-2 left-2 w-6 h-6">

            <p class="absolute bottom-2 left-2 bg-black bg-opacity-70 px-2 py-1 rounded">
                Глава {{ chapter }}
            </p>

        </div>

        <p>{{ title }}</p>

    </div>

</template>