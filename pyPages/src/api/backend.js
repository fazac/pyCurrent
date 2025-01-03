import axios from "@/api/http.js";

export function findDataByCode(code) {
    return axios.get('/rt/findDataByCode', {params: {code: code}});
}

export function findDataLineByCode(code) {
    return axios.get('/rt/findDataLineByCode', {params: {code: code}});
}

export function findLast() {
    return axios.get('/rt/findLast');
}

export function findCptr(symbol) {
    return axios.get('/rt/findCptr', {params: {symbol: symbol}});
}


export function findConstants() {
    return axios.get('/constants/findAll');
}

export function findLastContinuous() {
    return axios.get('/continuous/findLastContinuous');
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

export function findSummaryList() {
    return axios.get('/curcount/findSummaryList');
}

export function findOtherConcernList() {
    return axios.get('/rt/findOtherList');
}

export function searchSome(type, code, name, searchDate, hand, pch, count, r2LowLimit, r2HighLimit, r1LowLimit, r1HighLimit) {
    return axios.get('/rt/searchSome', {
        params: {
            code: code,
            name: name,
            searchDate: searchDate,
            hand: hand,
            pch: pch,
            count: count,
            r2LowLimit: r2LowLimit,
            r2HighLimit: r2HighLimit,
            r1LowLimit: r1LowLimit,
            r1HighLimit: r1HighLimit,
            type: type,
        }
    });
}

export function findLHPByCode(code, type) {
    return axios.get('/lhp/findLHPByCode', {params: {code: code, type: type}});
}

export function findRocByTypeDate(code, type, sdate, edate) {
    return axios.get('/rt/findRocByTypeDate', {params: {code: code, type: type, startDate: sdate, endDate: edate}});
}

