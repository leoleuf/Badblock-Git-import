package fr.badblock.ladder.api.entities;

public abstract interface CommandSender
{
  public abstract String getName();
  
  public abstract boolean hasPermission(String paramString);
  
  public abstract void sendMessage(String paramString);
  
  public abstract void sendMessages(String... paramVarArgs);
  
  public abstract void forceCommand(String... paramVarArgs);
}
