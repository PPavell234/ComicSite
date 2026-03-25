<template>
    <div class="w-full flex">
        <div class="bg-black w-90 flex-shrink-0"></div>
        <div class="flex-1 bg-black">
            <!-- Поле для написания комментария -->
            <div class="bg-[#353333] p-3 mt-12 mr-12 ml-12 text-white">
                <textarea v-model="newCommentText" class="w-full bg-transparent text-white outline-none resize-none"
                    placeholder="Написать комментарий..." rows="3"></textarea>
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
                    <div class="flex justify-between items-start text-white">
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

            <div v-else-if="!loading" class="text-center text-gray-500 mt-12">
                Пока нет комментариев на этой странице. Будьте первым!
            </div>

            <div v-if="loading" class="text-center text-white mt-12">
                Загрузка комментариев...
            </div>

            <div v-if="hasMoreComments && !loading" class="mt-8 pb-12">
                <div @click="loadMoreComments"
                    class="bg-[#353333] p-3 mr-12 ml-12 text-white text-center cursor-pointer hover:bg-gray-600 transition">
                    Еще комментарии
                </div>
            </div>
        </div>
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
        },
        pageNumber: {
            type: Number,
            default: 1
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
            replyFormVisible: null,
            userId: null
        }
    },
    mounted() {
        console.log('=== COMMENTS MOUNTED ===')
        console.log('comicId:', this.comicId)
        console.log('pageNumber:', this.pageNumber)

        this.userId = localStorage.getItem('commentUserId')
        if (!this.userId) {
            this.userId = 'user_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
            localStorage.setItem('commentUserId', this.userId)
        }
        console.log('userId:', this.userId)

        this.loadComments()
    },
    watch: {
        comicId(newVal, oldVal) {
            console.log('comicId changed:', oldVal, '->', newVal)
            this.comments = []
            this.currentPage = 0
            this.loadComments()
        },
        pageNumber(newVal, oldVal) {
            console.log('pageNumber changed:', oldVal, '->', newVal)
            this.comments = []
            this.currentPage = 0
            this.loadComments()
        }
    },
    methods: {
        formatDate(dateString) {
            if (!dateString) return 'только что'
            try {
                const date = new Date(dateString)
                const now = new Date()
                const diff = Math.floor((now - date) / 1000 / 60)
                if (diff < 1) return 'только что'
                if (diff < 60) return `${diff} мин назад`
                if (diff < 1440) return `${Math.floor(diff / 60)} ч назад`
                return `${Math.floor(diff / 1440)} дн назад`
            } catch {
                return 'недавно'
            }
        },

        async loadComments() {
            if (this.loading) return
            this.loading = true

            try {
                const url = `http://localhost:8080/api/comments/comic/${this.comicId}?page=${this.currentPage}&pageNumber=${this.pageNumber}&userId=${this.userId}`
                console.log('Загрузка комментариев URL:', url)

                const response = await axios.get(url)

                console.log('📥 Ответ сервера:', response.data)
                console.log('📊 Получено комментариев:', response.data.comments?.length)

                this.comments = [...this.comments, ...(response.data.comments || [])]
                this.hasMoreComments = response.data.hasMore || false

            } catch (error) {
                console.error('Ошибка загрузки комментариев:', error)
                console.error('Статус:', error.response?.status)
                console.error('Данные ошибки:', error.response?.data)
            } finally {
                this.loading = false
            }
        },

        async loadMoreComments() {
            this.currentPage++
            await this.loadComments()
        },

        async submitComment() {
            if (!this.newCommentText.trim()) {
                alert('Напишите комментарий')
                return
            }

            try {
                console.log('📤 Отправка комментария:')
                console.log('  comicId:', this.comicId)
                console.log('  pageNumber:', this.pageNumber)
                console.log('  content:', this.newCommentText)

                const newComment = {
                    comicId: this.comicId,
                    pageNumber: this.pageNumber,
                    userId: this.userId,
                    userName: 'Читатель_' + this.userId.slice(-6),
                    content: this.newCommentText,
                    likes: 0,
                    dislikes: 0,
                    replies: []
                }

                const response = await axios.post('http://localhost:8080/api/comments/create', newComment)
                console.log('Комментарий сохранен:', response.data)

                this.comments.unshift(response.data)
                this.newCommentText = ''

            } catch (error) {
                console.error('Ошибка отправки комментария:', error)
                console.error('Статус:', error.response?.status)
                console.error('Данные ошибки:', error.response?.data)
                alert('Ошибка: ' + (error.response?.data?.error || error.message))
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
                    userId: this.userId,
                    userName: 'Читатель_' + this.userId.slice(-6),
                    content: this.replyText
                }

                await axios.post(`http://localhost:8080/api/comments/${commentId}/reply`, reply)

                const comment = this.comments.find(c => c.id === commentId)
                if (comment) {
                    if (!comment.replies) comment.replies = []
                    comment.replies.push({
                        ...reply,
                        id: Date.now(),
                        createdAt: new Date().toISOString()
                    })
                }

                this.replyFormVisible = null
                this.replyText = ''
            } catch (error) {
                console.error('Ошибка отправки ответа:', error)
            }
        },

        async addReaction(commentId, isLike) {
            try {
                const response = await axios.post(`http://localhost:8080/api/comments/${commentId}/reaction?like=${isLike}&userId=${this.userId}`)

                const comment = this.comments.find(c => c.id === commentId)
                if (comment) {
                    comment.likes = response.data.likes
                    comment.dislikes = response.data.dislikes
                    comment.userLiked = response.data.userLiked
                    comment.userDisliked = response.data.userDisliked
                }
            } catch (error) {
                console.error('Ошибка:', error)
            }
        }
    }
}
</script>