<template>
    <div class="w-full flex">
        <!-- Левая область -->
        <div class="bg-black w-90 flex-shrink-0"></div>

        <!-- Центральная область с контентом -->
        <div class="flex-1 bg-black">
            <!-- Поле для написания комментария -->
            <div class="bg-[#353333] p-3 mt-12 mr-12 ml-12 text-white">
                <textarea v-model="newCommentText" class="w-full bg-transparent text-white outline-none resize-none"
                    placeholder="Написать комментарий..." rows="3" @keydown.ctrl.enter="submitComment"></textarea>
                <div class="flex justify-end mt-2">
                    <button @click="submitComment"
                        class="bg-yellow-500 text-black px-4 py-1 rounded hover:bg-yellow-400 transition">
                        Отправить
                    </button>
                </div>
            </div>

            <!-- Список комментариев -->
            <div v-if="comments.length > 0">
                <div v-for="comment in comments" :key="comment.id" class="mt-6">
                    <!-- Формат комментария -->
                    <div class="flex justify-between items-start text-white">
                        <!-- Левая часть -->
                        <div class="flex-1 ml-12">
                            <div class="flex items-center gap-2">
                                <img src="/comicP/ProfileIcon.svg" alt="" class="w-8 h-8">
                                <p class="font-semibold">{{ comment.userName || 'Аноним' }}</p>
                                <p class="ml-4 text-gray-500 text-sm">{{ formatDate(comment.createdAt) }}</p>
                            </div>
                            <div class="mt-2">
                                <p class="text-gray-300">{{ comment.content }}</p>
                            </div>
                            <div class="flex items-center gap-6 mt-2 text-red-500">
                                <p class="cursor-pointer hover:text-yellow-500 transition"
                                    @click="showReplyForm(comment.id)">Ответить</p>
                                <p class="cursor-pointer hover:text-yellow-500 transition">Жалоба</p>
                            </div>

                            <!-- Ответы -->
                            <div v-if="comment.replies && comment.replies.length > 0"
                                class="ml-8 mt-4 border-l-2 border-gray-600 pl-4">
                                <div v-for="reply in comment.replies" :key="reply.id" class="mt-3">
                                    <div class="flex items-center gap-2">
                                        <img src="/comicP/ProfileIcon.svg" alt="" class="w-6 h-6">
                                        <p class="font-semibold text-sm">{{ reply.userName || 'Аноним' }}</p>
                                        <p class="ml-4 text-gray-500 text-xs">{{ formatDate(reply.createdAt) }}</p>
                                    </div>
                                    <p class="text-gray-400 text-sm mt-1">{{ reply.content }}</p>
                                </div>
                            </div>

                            <!-- Форма ответа -->
                            <div v-if="replyFormVisible === comment.id" class="mt-3 ml-8">
                                <textarea v-model="replyText"
                                    class="w-full bg-gray-700 text-white p-2 rounded outline-none resize-none"
                                    placeholder="Написать ответ..." rows="2"></textarea>
                                <div class="flex gap-2 mt-2">
                                    <button @click="submitReply(comment.id)"
                                        class="bg-yellow-500 text-black px-3 py-1 rounded text-sm hover:bg-yellow-400">
                                        Ответить
                                    </button>
                                    <button @click="replyFormVisible = null"
                                        class="bg-gray-600 text-white px-3 py-1 rounded text-sm hover:bg-gray-500">
                                        Отмена
                                    </button>
                                </div>
                            </div>
                        </div>

                        <!-- Правая часть: лайки -->
                        <div class="flex gap-4 mr-12">
                            <div class="flex flex-col items-center gap-1 cursor-pointer hover:scale-110 transition"
                                @click="addReaction(comment.id, false)">
                                <img src="/comicP/dislike.svg" alt="" class="w-6 h-6">
                                <p class="text-sm">{{ comment.dislikes || 0 }}</p>
                            </div>
                            <div class="flex flex-col items-center gap-1 cursor-pointer hover:scale-110 transition"
                                @click="addReaction(comment.id, true)">
                                <img src="/comicP/like.svg" alt="" class="w-6 h-6">
                                <p class="text-sm">{{ comment.likes || 0 }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Сообщение если нет комментариев -->
            <div v-else class="text-center text-gray-500 mt-12">
                Пока нет комментариев. Будьте первым!
            </div>

            <!-- Кнопка "Еще комментарии" -->
            <div v-if="hasMoreComments" class="mt-8 pb-12">
                <div @click="loadMoreComments"
                    class="bg-[#353333] p-3 mr-12 ml-12 text-white text-center cursor-pointer hover:bg-gray-600 transition">
                    Еще комментарии
                </div>
            </div>
        </div>

        <!-- Правая область -->
        <div class="bg-black w-90 flex-shrink-0"></div>
    </div>
</template>

<script>
import axios from 'axios'

export default {
    name: 'CommentsSection',
    props: {
        comicId: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            comments: [],
            currentPage: 0,
            hasMoreComments: false,
            loading: false,
            newCommentText: '',
            replyText: '',
            replyFormVisible: null
        }
    },
    mounted() {
        this.loadComments()
    },
    methods: {
        formatDate(dateString) {
            if (!dateString) return 'только что'
            const date = new Date(dateString)
            const now = new Date()
            const diff = Math.floor((now - date) / 1000 / 60)

            if (diff < 1) return 'только что'
            if (diff < 60) return `${diff} минут назад`
            if (diff < 1440) return `${Math.floor(diff / 60)} часов назад`
            return `${Math.floor(diff / 1440)} дней назад`
        },

        async loadComments() {
            this.loading = true
            try {
                const response = await axios.get(`http://localhost:8080/api/comments/comic/${this.comicId}?page=${this.currentPage}`)
                this.comments = [...this.comments, ...response.data.comments]
                this.hasMoreComments = response.data.hasMore
            } catch (error) {
                console.error('Ошибка загрузки комментариев:', error)
            } finally {
                this.loading = false
            }
        },

        async loadMoreComments() {
            this.currentPage++
            await this.loadComments()
        },

        async submitComment() {
            if (!this.newCommentText.trim()) return

            try {
                const newComment = {
                    comicId: this.comicId,
                    userId: 'user123', // Замените на реального пользователя
                    userName: 'Читатель', // Замените на реальное имя
                    content: this.newCommentText,
                    likes: 0,
                    dislikes: 0,
                    replies: []
                }

                const response = await axios.post('http://localhost:8080/api/comments/create', newComment)
                this.comments.unshift(response.data)
                this.newCommentText = ''
            } catch (error) {
                console.error('Ошибка отправки комментария:', error)
            }
        },

        showReplyForm(commentId) {
            this.replyFormVisible = commentId
            this.replyText = ''
        },

        async submitReply(commentId) {
            if (!this.replyText.trim()) return

            try {
                const reply = {
                    userId: 'user123',
                    userName: 'Читатель',
                    content: this.replyText
                }

                await axios.post(`http://localhost:8080/api/comments/${commentId}/reply`, reply)

                // Обновляем комментарий в списке
                await this.loadComments()
                this.replyFormVisible = null
                this.replyText = ''
            } catch (error) {
                console.error('Ошибка отправки ответа:', error)
            }
        },

        async addReaction(commentId, isLike) {
            try {
                await axios.post(`http://localhost:8080/api/comments/${commentId}/like?like=${isLike}`)
                await this.loadComments()
            } catch (error) {
                console.error('Ошибка:', error)
            }
        }
    }
}
</script>

<style scoped>
/* Стили при необходимости */
</style>