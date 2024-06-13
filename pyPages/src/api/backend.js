import axios from "@/api/http.js";

export function findDetails(code) {
    return axios.get('/rt/findByCode', {params: {code: code}});
}

