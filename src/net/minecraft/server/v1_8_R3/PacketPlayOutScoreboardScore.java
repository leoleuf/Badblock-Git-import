package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutScoreboardScore implements Packet<PacketListenerPlayOut> {

    private String a = "";
    private String b = "";
    private int c;
    private PacketPlayOutScoreboardScore.EnumScoreboardAction d;

    public PacketPlayOutScoreboardScore() {}

    public PacketPlayOutScoreboardScore(ScoreboardScore scoreboardscore) {
        this.a = scoreboardscore.getPlayerName();
        this.b = scoreboardscore.getObjective().getName();
        this.c = scoreboardscore.getScore();
        this.d = PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE;
    }

    public PacketPlayOutScoreboardScore(String s) {
        this.a = s;
        this.b = "";
        this.c = 0;
        this.d = PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE;
    }

    public PacketPlayOutScoreboardScore(String s, ScoreboardObjective scoreboardobjective) {
        this.a = s;
        this.b = scoreboardobjective.getName();
        this.c = 0;
        this.d = PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE;
    }

    @Override
	public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.c(40);
        this.d = packetdataserializer.a(PacketPlayOutScoreboardScore.EnumScoreboardAction.class);
        this.b = packetdataserializer.c(16);
        if (this.d != PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE) {
            this.c = packetdataserializer.e();
        }

    }

    @Override
	public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
        packetdataserializer.a(this.d);
        packetdataserializer.a(this.b);
        if (this.d != PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE) {
            packetdataserializer.b(this.c);
        }

    }

    @Override
	public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }


    public static enum EnumScoreboardAction {

        CHANGE, REMOVE;

        private EnumScoreboardAction() {}
    }
}