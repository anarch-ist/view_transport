"use strict"

var testObjSingle = {
    data: {
        docPeriodId: 5,
        owned: true,
        periodBegin: 0,
        periodEnd: 180,
        state: "CLOSED"
    },
    periods: [
        {periodBegin: 0, periodEnd: 180}
    ]
};

var testObj = {
    data: {
        docPeriodId: 5,
        owned: true,
        periodBegin: 0,
        periodEnd: 180,
        state: "CLOSED"
    },
    periods: [
        {periodBegin: 60, periodEnd: 90},
        {periodBegin: 120, periodEnd: 150}
    ]
};

function recreated(obj){
    if(obj.periods.length === 1 && obj.data.periodBegin === obj.periods[0].periodBegin && obj.data.periodEnd === obj.periods[0].periodEnd){
        return [{
            action: "DELETE",
            docPeriodId: obj.data.docPeriodId
        }];
    }else if(obj.periods.length === 1){
        if(obj.data.periodBegin === obj.periods[0].periodBegin){
            return [{
                periodBegin: obj.periods[0].periodEnd,
                periodEnd: obj.data.periodEnd,
                action: "UPDATE",
                docPeriodId: obj.data.docPeriodId
            }];
        }else if(obj.data.periodEnd === obj.periods[0].periodEnd){
            return [{
                periodBegin: obj.data.periodBegin,
                periodEnd: obj.periods[0].periodBegin,
                action: "UPDATE",
                docPeriodId: obj.data.docPeriodId
            }];
        }else{
            return [{
                periodBegin: obj.data.periodBegin,
                periodEnd: obj.periods[0].periodBegin,
                action: "UPDATE",
                docPeriodId: obj.data.docPeriodId},

                {periodBegin: obj.periods[0].periodEnd,
                periodEnd: obj.data.periodEnd,
                action: "CREATE"}
            ];
        }
    }else{
        var result = [];
        if(obj.data.periodBegin !== obj.periods[0].periodBegin){
            result.push({
                periodBegin: obj.data.periodBegin,
                periodEnd: obj.periods[0].periodBegin,
                action: "UPDATE",
                docPeriodId: obj.data.docPeriodId
            });
        }
        for(var i = 0; i < obj.periods.length - 1; i++) {
            if (i === 0 && result.length === 0) {
                result.push({
                    periodBegin: obj.periods[i].periodEnd,
                    periodEnd: obj.periods[i + 1].periodBegin,
                    action: "UPDATE",
                    docPeriodId: obj.data.docPeriodId
                });
            }else {
                result.push({
                    periodBegin: obj.periods[i].periodEnd,
                    periodEnd: obj.periods[i + 1].periodBegin,
                    action: "CREATE"
                });
            }
        }
        if(obj.data.periodEnd !== obj.periods[obj.periods.length - 1].periodEnd){
            result.push({
                periodBegin: obj.periods[obj.periods.length - 1].periodEnd,
                periodEnd: obj.data.periodEnd,
                action: "CREATE"
            });
        }
        return result;
    }
}

window.console.log(recreated(testObj));

