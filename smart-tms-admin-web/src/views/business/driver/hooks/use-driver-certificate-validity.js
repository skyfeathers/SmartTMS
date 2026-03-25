import dayjs from "dayjs";

export function useDriverCertificateValidity() {
    /**
     * 有效期处理
     * @param dateStr
     * @param endlessFlag
     * @returns {string}
     */
    function dateHandle(dateStr, endlessFlag = false) {
        if(!dateStr){
            return '';
        }
        if (endlessFlag) {
            return '永久有效';
        }
        let expired = dateExpired(dateStr);
        return `${dateStr}${expired ? '（已到期）' : ''}`
    }

    /**
     * 时间是否过期
     * @param dateStr
     * @returns {boolean}
     */
    function dateExpired(dateStr) {
        if (!dateStr) {
            return false;
        }
        let date = dayjs(dateStr, 'YYYY-MM-DD');
        return dayjs().isAfter(date);
    }

    return {
        dateHandle,
        dateExpired
    }
}
