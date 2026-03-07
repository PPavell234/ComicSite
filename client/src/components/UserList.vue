<template>
    <div>
        <h2>Users</h2>

        <ul>
            <li v-for="user in users" :key="user.id">
                {{ user.name }} - {{ user.email }}
            </li>
        </ul>

    </div>
</template>

<script>
import { getUsers } from "../services/userService"

export default {
    data() {
        return {
            users: []
        }
    },
    mounted() {
        getUsers().then(res => {
            this.users = res.data
        })
    }
}
</script>