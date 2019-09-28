package fr.badblock.tech.logger;

public enum LogType {

    SUCCESS(LogChatColor.GREEN),
    INFO(LogChatColor.CYAN),
    ERROR(LogChatColor.RED),
    WARNING(LogChatColor.YELLOW),
    DEBUG(LogChatColor.PURPLE);

    private String chatColor;

    LogType(String chatColor) {
        this.chatColor = chatColor;
    }

    public String getChatColor() {
        return chatColor;
    }
}

