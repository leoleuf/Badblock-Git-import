package fr.badblock.api.utils;


public class Transcoder {
    public static String encode(String uuid) {
        uuid = uuid.replace("-", "");
        return uuid;
    }

    public static String decode(String uuid) {
        uuid = uuid.toLowerCase();
        uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
        return uuid;
    }

}
