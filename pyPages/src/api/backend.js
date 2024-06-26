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


