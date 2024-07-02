import {ref} from 'vue';

export const commonPageRef = ref(null)

export const showLineChart = (v) => {
    commonPageRef.value.showLineChart(v)
}

export const showDetails = (v) => {
    commonPageRef.value.showDetails(v)
}