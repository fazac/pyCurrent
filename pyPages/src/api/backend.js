import axios from "@/api/http.js";

export function findDetails(type,) {
    return axios.get('/check', {params: {type: type}});
}

