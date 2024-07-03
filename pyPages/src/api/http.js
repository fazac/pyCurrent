import axios from "axios";
import {showFullScreenLoading, tryHideFullScreenLoading} from "@/api/axiosLoading";

const BASE_API = "http://139.84.194.82:7001";
// const BASE_API = "http://localhost:19099";

const service = axios.create({
    baseURL: BASE_API, //所有的请求地址前缀部分(没有后端请求不用写)
    timeout: 30000, // 请求超时时间
    showLoading: true, // 默认开启loading
    withCredentials: true, //允许跨域携带凭证(cookie)
    credentials: true, //后端允许跨域携带cookie
})


// http request 请求 拦截器
service.interceptors.request.use(
    config => {

        // let url = config.url;
        // if (url.indexOf("https://tp.ymdatas.com") === 0) {
        //     //标记是否为第三方接口
        //     config.tp = true;
        // } else {
        //     if (store.state.token) {  // 判断是否存在token,如果存在的话,则每个http header都加上token
        //         config.headers.Authorization = `Bearer ${store.state.token}`;
        //     }
        // }
        config.showLoading && showFullScreenLoading()
        return config;
    },
    err => {
        // GlobalLoading.hide();
        return Promise.reject(err);
    });

// http response 响应 拦截器
service.interceptors.response.use(
    response => {
        // if (response.config.tp) {
        //第三方接口, 非我方标准接口格式
        response.config.showLoading && tryHideFullScreenLoading()
        //对数据返回做什么
        return Promise.resolve(response.data);
        // }
        // if (response.data.result === 1) {
        //     return Promise.resolve(response.data.data);
        // } else {
        //     // console.warn("调取接口返回业务错误: ", response.config.url, "\n", response.data);
        //     return Promise.reject(response.data);
        // }
    },
    error => {
        if (error.response) {
            // switch (error.response.status) {
            //     case 401:
            //         // 返回 401 清除token信息并跳转到登录页面
            //         if (router.currentRoute.name !== "Login") {
            //             Dialog.confirm({
            //                 title: "",
            //                 message: error.response.data.msg + ", 需要您重新进行登录",
            //                 confirmButtonText: "去登录"
            //             }).then(() => {
            //                 store.dispatch('logOut');
            //                 router.replace({path: '/login', query: {redirect: router.currentRoute.fullPath}});
            //             }).catch(err => {
            //                 store.dispatch('logOut');
            //             })
            //         }
            // }
            var _rejectData = error.response.data;
            // if (typeof _rejectData === "string") {
            //     _rejectData = {msg: "连接失败: " + error.response.status + " " + error.response.data};
            // }
            return Promise.reject(_rejectData);   // 返回接口返回的错误信息
        } else {
            error.msg = "连接失败";
            return Promise.reject(error);
        }
    });

export default service;