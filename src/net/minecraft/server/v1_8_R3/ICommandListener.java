package net.minecraft.server.v1_8_R3;

public interface ICommandListener {

    String getName();

    IChatBaseComponent getScoreboardDisplayName();

    void sendMessage(IChatBaseComponent ichatbasecomponent);

    boolean a(int i, String s);

    BlockPosition getChunkCoordinates();

    Vec3D d();

    World getWorld();

    Entity f();

    boolean getSendCommandFeedback();

    void a(CommandObjectiveExecutor.EnumCommandResult commandobjectiveexecutor_enumcommandresult, int i);
}
