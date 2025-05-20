import {createRouter, createWebHistory} from 'vue-router'
import Home from '../components/Index.vue'
import Login from "@/components/Login.vue";
import Register from "@/components/Register.vue";
import Error from "@/components/Error.vue";


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/login',
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
        },
        {
            path: '/home/:userId',
            name: 'home',
            component: Home,
        },
        {
            path: '/register',
            name: 'register',
            component: Register,
        },
        {
            path: "/error",
            name: "Error",
            component: Error,
        }
    ]
})

export default router
