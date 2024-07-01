import {ElNotification} from 'element-plus'


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

export function txtCenter() {
    return {'text-align': 'center'};
}


export function rowStyleClass(row) {
    if (!isEmpty(row)) {
        if (!isEmpty(row.row) && !isEmpty(row.row.mark) && row.row.mark.indexOf('H') > 0) {
            return 'row-hold-mark';
        } else if (row.row.mark.charAt(0) === 'R') {
            return 'row-high-light';
        }
    }
}

export function cellStyle(row) {
    console.log(row)
    if (row.column.label === 'mark' || row.column.label === 'code' || row.column.label === '详情') {
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

