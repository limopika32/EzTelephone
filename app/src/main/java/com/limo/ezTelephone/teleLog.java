package com.limo.ezTelephone;

import static android.provider.CallLog.Calls.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class teleLog {
    private int code;
    private String name;
    private String number;
    private Date date;

    teleLog(int code, String name,String number, Date date){
        this.code = code;
        this.name = name;
        this.number = number;
        this.date = date;
    }

    public int getCode(){
        return code;
    }

    public String getType(){
        switch (code){
            case INCOMING_TYPE:
                return  "着信";
            case OUTGOING_TYPE:
                return  "発信";
            case MISSED_TYPE:
                return  "不在";
            case VOICEMAIL_TYPE:
                return  "伝言";
            case REJECTED_TYPE:
                // Call log type for calls rejected by direct user action.
                return  "拒否";
            case BLOCKED_TYPE:
                // 着信拒否
                return  "着信拒否";
            case ANSWERED_EXTERNALLY_TYPE:
                // Call log type for a call which was answered on another device.
                return  "転送";

            default:
                return "(" + code + ")";
        }
    }

    public String getName(){
        if (name != null){
            return name;
        }else if ((number != null) | !number.equals("")){
            return number;
        }else {
            return "非通知または通知不可";
        }
    }

    public String getNumber(){
        return number;
    }

    public Date getDate(){
        return date;
    }

    public String getDatef(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
        return df.format(date);
    }
}
