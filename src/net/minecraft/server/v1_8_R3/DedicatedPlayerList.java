package net.minecraft.server.v1_8_R3;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.GameProfile;

public class DedicatedPlayerList extends PlayerList {

    private static final Logger f = LogManager.getLogger();

    public DedicatedPlayerList(DedicatedServer dedicatedserver) {
        super(dedicatedserver);
        this.a(dedicatedserver.a("view-distance", 10));
        this.maxPlayers = dedicatedserver.a("max-players", 20);
        this.setHasWhitelist(dedicatedserver.a("white-list", false));
        if (!dedicatedserver.T()) {
            this.getProfileBans().a(true);
            this.getIPBans().a(true);
        }

        this.z();
        this.x();
        this.y();
        this.w();
        this.A();
        this.C();
        this.B();
        if (!this.getWhitelist().c().exists()) {
            this.D();
        }

    }

    @Override
	public void setHasWhitelist(boolean flag) {
        super.setHasWhitelist(flag);
        this.getServer().a("white-list", Boolean.valueOf(flag));
        this.getServer().a();
    }

    @Override
	public void addOp(GameProfile gameprofile) {
        super.addOp(gameprofile);
        this.B();
    }

    @Override
	public void removeOp(GameProfile gameprofile) {
        super.removeOp(gameprofile);
        this.B();
    }

    @Override
	public void removeWhitelist(GameProfile gameprofile) {
        super.removeWhitelist(gameprofile);
        this.D();
    }

    @Override
	public void addWhitelist(GameProfile gameprofile) {
        super.addWhitelist(gameprofile);
        this.D();
    }

    @Override
	public void reloadWhitelist() {
        this.C();
    }

    private void w() {
        try {
            this.getIPBans().save();
        } catch (IOException ioexception) {
            DedicatedPlayerList.f.warn("Failed to save ip banlist: ", ioexception);
        }

    }

    private void x() {
        try {
            this.getProfileBans().save();
        } catch (IOException ioexception) {
            DedicatedPlayerList.f.warn("Failed to save user banlist: ", ioexception);
        }

    }

    private void y() {
        try {
            this.getIPBans().load();
        } catch (IOException ioexception) {
            DedicatedPlayerList.f.warn("Failed to load ip banlist: ", ioexception);
        }

    }

    private void z() {
        try {
            this.getProfileBans().load();
        } catch (IOException ioexception) {
            DedicatedPlayerList.f.warn("Failed to load user banlist: ", ioexception);
        }

    }

    private void A() {
        try {
            this.getOPs().load();
        } catch (Exception exception) {
            DedicatedPlayerList.f.warn("Failed to load operators list: ", exception);
        }

    }

    private void B() {
        try {
            this.getOPs().save();
        } catch (Exception exception) {
            DedicatedPlayerList.f.warn("Failed to save operators list: ", exception);
        }

    }

    private void C() {
        try {
            this.getWhitelist().load();
        } catch (Exception exception) {
            DedicatedPlayerList.f.warn("Failed to load white-list: ", exception);
        }

    }

    private void D() {
        try {
            this.getWhitelist().save();
        } catch (Exception exception) {
            DedicatedPlayerList.f.warn("Failed to save white-list: ", exception);
        }

    }

    @Override
	public boolean isWhitelisted(GameProfile gameprofile) {
        return !this.getHasWhitelist() || this.isOp(gameprofile) || this.getWhitelist().isWhitelisted(gameprofile);
    }

    @Override
	public DedicatedServer getServer() {
        return (DedicatedServer) super.getServer();
    }

    @Override
	public boolean f(GameProfile gameprofile) {
        return this.getOPs().b(gameprofile);
    }
}
