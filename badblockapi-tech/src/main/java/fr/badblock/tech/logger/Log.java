package fr.badblock.tech.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyy/MM/dd HH:mm:ss");

    private static LogAction logAction;

    public static void setAction(LogAction action) {
        logAction = action;
    }

    public static String log(LogType logType, String message) {
        String date = simpleDateFormat.format(new Date());
        String result = date + " " + logType.getChatColor() + "[" + logType.name() + "] " + LogChatColor.RESET + message;
        System.out.println(result);
        if (logAction != null) {
            logAction.done(result);
        }
        return result;
    }

}
