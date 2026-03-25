import dayjs from "dayjs";

export function useVehicleExpired(){
    function dateHandle(dateStr) {
        if (!dateStr) {
            return '';
        }
        // 已过期
        let expired = dateExpired(dateStr);
        if (expired) {
            return `${dateStr}(已过期)`
        }
        // 即将过期
        let expiringSoon = dateExpired(dateStr, 10);
        if (expiringSoon) {
            return `${dateStr}(即将过期)`
        }
        return dateStr;

    }

    function dateExpired(dateStr, advanceDay = 0) {
        if (!dateStr) {
            return false;
        }
        let date = dayjs(dateStr, 'YYYY-MM-DD');
        return dayjs().subtract(advanceDay, 'day').isAfter(date);
    }

    return {
        dateHandle,
        dateExpired
    }
}
