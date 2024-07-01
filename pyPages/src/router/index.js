import {createRouter, createWebHashHistory} from 'vue-router'
import CodeDataView from '../views/CodeDataView.vue'
import DailyOnBoard from '../views/OnBoardView.vue'
import OtherCodeView from '../views/OtherCodeView.vue'

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
        },
        {
            path: '/other',
            name: 'other',
            component: OtherCodeView
        }
    ]
})

export default router
