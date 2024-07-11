import {ElNotification} from 'element-plus'
import {reactive, provide} from 'vue'
import moment from 'moment'

// export const baseUrl = moment().hour() > 16 ? 'http://localhost:19093' : 'http://10.243.161.168:19093';
export const baseUrl = 'http://localhost:19093';

export function amountFix(amount) {
    return parseFloat((amount / 100000000).toFixed(2));
}

export function tradeDateDecorate(date) {
    if (date.indexOf('-') > 0) {
        return date.substring(6, 10).replaceAll('-', '');
    } else {
        return date.substring(5, 10).replaceAll('-', '');
    }
}

export function minArr(arr, excludeKeys) {
    let rObj = {min: null, max: null};

    let min = Infinity;
    let max = -Infinity;
    arr.reduce(function (pre, cur, index) {
        const values = Object.keys(cur)
            .filter(key => !excludeKeys.includes(key))
            .map(key => cur[key]);
        if (min !== Infinity) {
            values.push(min);
        }
        if (max !== -Infinity) {
            values.push(max);
        }
        min = Math.min.apply(null, values);
        max = Math.max.apply(null, values);
    }, null);
    rObj.min = Math.floor(min);
    rObj.max = Math.ceil(max);
    return rObj;
}

function createObject(prop, sortable) {
    return {
        prop: prop,
        fullProp: 'extraNode.' + prop,
        sortable: sortable
    };
}

export function extraTdKey(td) {
    if (!isEmpty(td) && !isEmpty(td.value) && !isEmpty(td.value[0].extraNode)) {
        return Object.entries(td.value[0].extraNode).map(entry => createObject(entry[0], typeof entry[1] === 'number')
        )
    }
    return null;
}

export function nfc(title, message, type) {
    ElNotification({
        title: title,
        message: message,
        type: isEmpty(type) ? 'success' : type,
    })
}

export function nullArr(length) {
    return Array(length).fill(null);
}

export function tableData() {
    const tableData = reactive({});
    provide('myTableData', tableData);
    return tableData;
}

export function rowStyleClass(row) {
    if (!isEmpty(row) && !isEmpty(row.row) && !isEmpty(row.row.mark)) {
        if (row.row.mark.indexOf('H') > 0) {
            return 'row-hold-mark';
        } else if (row.row.mark.charAt(0) === 'R') {
            return 'row-high-light';
        }
    }
}

export function cellStyle(row) {
    if (row.column.label === 'mark'
        || row.column.label === 'code'
        || row.column.label === 'tsCode'
        || row.column.label === '详情'
        || row.column.label === 'date'
        || row.column.label === 'startDate'
        || row.column.label === 'endDate'
    ) {
        return {'text-align': 'center'};
    } else {
        return {'text-align': 'right'};
    }
}

export function headerCellStyle() {
    return {'text-align': 'center'};
}


export function isEmpty(value) {
    return value === undefined || value === null || value === '';
}


// 判断对象是否为空
function isObjectEmpty(obj) {
    return Object.keys(obj).length === 0 && JSON.stringify(obj) === '{}';
}

// 判断字符串是否为空
function isStringEmpty(str) {
    return !str || str.length === 0;
}

// 判断数组是否为空
function isArrayEmpty(arr) {
    return !arr || arr.length === 0;
}

