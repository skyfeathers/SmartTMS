import {Decimal} from "decimal.js";

export const nftCostCalculate = (amount, rate) => {
    let nftRate =  Decimal(rate).div(Decimal(100)).toNumber();
    let nftRateDiv =  Decimal(1).sub(Decimal(nftRate)).toNumber();
    return Decimal(amount).div(Decimal(nftRateDiv)).mul(Decimal(nftRate)).toNumber().toFixed(0);
};
