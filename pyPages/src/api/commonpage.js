import {ref} from 'vue';

export const commonPageRef = ref(null)

export const handleRowClick = (row, column) => {
    commonPageRef.value.handleRowClick(row, column)
}

export const showDetails = (v) => {
    commonPageRef.value.showDetails(v)
}