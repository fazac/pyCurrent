import {createRouter, createWebHashHistory} from 'vue-router'
import CodeDataView from '../views/CodeDataView.vue'
import LimitCodeView from '../views/LimitCodeView.vue'
import OtherCodeView from '../views/OtherCodeView.vue'
import OphcCodeView from '../views/OphcCodeView.vue'
import RocCodeView from '../views/RocCodeView.vue'
import RTAllCodeView from '../views/RTAllCodeView.vue'
import ContinuousCodeView from '../views/ContinuousCodeView.vue'
import CprtCodeView from '../views/CprtCodeView.vue'
import CcountSummaryView from '../views/CcountSummaryView.vue'

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
        },
        {
            path: '/ophc',
            name: 'ophc',
            component: OphcCodeView
        }, {
            path: '/roc',
            name: 'roc',
            component: RocCodeView
        }, {
            path: '/all',
            name: 'all',
            component: RTAllCodeView
        }, {
            path: '/continuous',
            name: 'continuous',
            component: ContinuousCodeView
        }, {
            path: '/cptr',
            name: 'cptr',
            component: CprtCodeView
        }, {
            path: '/ccs',
            name: 'ccs',
            component: CcountSummaryView
        }
    ]
})

export default router
