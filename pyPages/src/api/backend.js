import axios from "@/api/http.js";

export function findDataByCode(code) {
    return axios.get('/rt/findDataByCode', {params: {code: code}});
}


export function findRtDataStream(code) {
    return axios.get('/rt/findRtDataStream', {params: {code: code}});
}

