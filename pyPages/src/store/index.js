import {createStore} from 'vuex';

const store = createStore({
    state: {
        // 在这里定义你的状态
        isDark: false,
    },
    mutations: {
        // 在这里定义改变状态的方法, 同步, .emit 调用
        updateIsDark(state, value) {
            state.isDark = value;
        }
    },
    actions: {
        // 在这里定义触发 mutations 的方法, 异步, .dispatch 调用
    },
    getters: {
        // 在这里定义从状态中派生出来的状态
    }
});

export default store;