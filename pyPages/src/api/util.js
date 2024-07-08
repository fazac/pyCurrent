import {ElNotification} from 'element-plus'

export function amountFix(amount) {
    return parseFloat((amount / 100000000).toFixed(2));
}

export function tradeDateDecorate(date) {
    return date.substring(4, 10).replaceAll('-', '');
}

export function minArr(arr, props) {
    let rObj = {start: '', min: null, max: null};
    rObj.start = arr[29].tradeDate;
    let min = Infinity;
    let max = -Infinity;
    arr.reduce(function (pre, cur, index) {
        let tmpArr = props.map(prop => cur[prop])
        min = Math.min.apply(min, tmpArr);
        max = Math.max.apply(max, tmpArr);
    }, null);
    rObj.min = min;
    rObj.max = max;
    return rObj;
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
        || row.column.label === '详情') {
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

