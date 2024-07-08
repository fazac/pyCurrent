import {isEmpty} from "@/api/util";

const lineTypeArr = ['dn', 'rt', 'hp'];

export function circleLineType(type) {
    if (isEmpty(type)) {
        return 'dn'
    } else {
        return lineTypeArr[(lineTypeArr.indexOf(type) + 1) % lineTypeArr.length]
    }
}