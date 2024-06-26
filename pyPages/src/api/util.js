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