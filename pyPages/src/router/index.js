import {createRouter, createWebHashHistory} from 'vue-router'
import CodeDataView from '../views/CodeDataView.vue'
import DailyOnBoard from '../views/OnBoardView.vue'
import LimitCodeView from '../views/LimitCodeView.vue'
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
            component: LimitCodeView
        },
        {
            path: '/other',
            name: 'other',
            component: OtherCodeView
        }
    ]
})

export default router
