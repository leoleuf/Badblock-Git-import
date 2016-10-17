package net.minecraft.server.v1_8_R3;

import java.util.Vector;

import javax.swing.JList;

public class PlayerListBox extends JList implements IUpdatePlayerListBox {

    private MinecraftServer a;
    private int b;

    public PlayerListBox(MinecraftServer minecraftserver) {
        this.a = minecraftserver;
        minecraftserver.a(this);
    }

    @Override
	public void c() {
        if (this.b++ % 20 == 0) {
            Vector vector = new Vector();

            for (int i = 0; i < this.a.getPlayerList().v().size(); ++i) {
                vector.add(this.a.getPlayerList().v().get(i).getName());
            }

            this.setListData(vector);
        }

    }
    
    @Override
    public boolean mustUpdatePlayerListBox(){
    	return true;
    }
}
