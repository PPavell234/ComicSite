<template>
    <div>
        <input type="file" @change="uploadCbr" accept=".cbr" />
        <div v-if="pages.length" class="pages-container">
            <img v-for="(page, i) in pages" :key="i" :src="page" class="comic-page" />
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            pages: []
        };
    },
    methods: {
        async uploadCbr(event) {
            const file = event.target.files[0];
            if (!file) return;

            const formData = new FormData();
            formData.append("file", file);

            try {
                const res = await axios.post("/api/upload-cbr", formData, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
                this.pages = res.data.pages;
            } catch (err) {
                console.error(err);
                alert("Ошибка загрузки CBR");
            }
        }
    }
};
</script>

<style scoped>
.pages-container {
    display: flex;
    flex-direction: column;
    gap: 10px;
    max-height: 600px;
    overflow-y: auto;
}

.comic-page {
    width: 100%;
    border-radius: 8px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}
</style>