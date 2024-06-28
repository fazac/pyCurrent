import {createRouter, createWebHashHistory} from 'vue-router'
import CodeDataView from '../views/CodeDataView.vue'
import DailyOnBoard from '../views/OnBoardView.vue'

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {
            path: '/',
            name: 'codeDataView',
            component: CodeDataView
        },
        {
            path: '/board',
            name: 'board',
            component: DailyOnBoard
        }
    ]
})

export default router
