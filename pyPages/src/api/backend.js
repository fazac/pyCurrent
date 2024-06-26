import axios from "@/api/http.js";

export function findDataByCode(code) {
    return axios.get('/rt/findDataByCode', {params: {code: code}});
}

export function findDataLineByCode(code) {
    return axios.get('/rt/findDataLineByCode', {params: {code: code}});
}

export function findConstants() {
    return axios.get('/constants/findAll');
}

export function updateConstant(constant) {
    return axios.post('/constants/updateOne', constant, {
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

export function findCurcc() {
    return axios.get('/curcount/findLastAll');
}

export function findOtherConcernList() {
    return axios.get('/rt/findOtherList');
}

export function searchSome(type, code, name, searchDate, hand, pch, count) {
    return axios.get('/rt/searchSome', {
        params: {
            code: code,
            name: name,
            searchDate: searchDate,
            hand: hand,
            pch: pch,
            count: count,
            type: type,
        }
    });
}

