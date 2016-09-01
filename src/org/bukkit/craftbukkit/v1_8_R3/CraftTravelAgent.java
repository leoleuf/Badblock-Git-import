package org.bukkit.craftbukkit.v1_8_R3;

import org.bukkit.Location;
import org.bukkit.TravelAgent;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PortalTravelAgent;
import net.minecraft.server.v1_8_R3.WorldServer;

public class CraftTravelAgent extends PortalTravelAgent implements TravelAgent {

    public static TravelAgent DEFAULT = null;

    private int searchRadius = 128;
    private int creationRadius = 16;
    private boolean canCreatePortal = true;

    public CraftTravelAgent(WorldServer worldserver) {
        super(worldserver);
        if (DEFAULT == null && worldserver.dimension == 0) {
            DEFAULT = this;
        }
    }

    @Override
    public Location findOrCreate(Location target) {
        WorldServer worldServer = ((CraftWorld) target.getWorld()).getHandle();
        boolean before = worldServer.chunkProviderServer.forceChunkLoad;
        worldServer.chunkProviderServer.forceChunkLoad = true;

        Location found = this.findPortal(target);
        if (found == null) {
            if (this.getCanCreatePortal() && this.createPortal(target)) {
                found = this.findPortal(target);
            } else {
                found = target; // fallback to original if unable to find or create
            }
        }

        worldServer.chunkProviderServer.forceChunkLoad = before;
        return found;
    }

    @Override
    public Location findPortal(Location location) {
        PortalTravelAgent pta = ((CraftWorld) location.getWorld()).getHandle().getTravelAgent();
        BlockPosition found = pta.findPortal(location.getX(), location.getY(), location.getZ(), this.getSearchRadius());
        return found != null ? new Location(location.getWorld(), found.getX(), found.getY(), found.getZ(), location.getYaw(), location.getPitch()) : null;
    }

    @Override
    public boolean createPortal(Location location) {
        PortalTravelAgent pta = ((CraftWorld) location.getWorld()).getHandle().getTravelAgent();
        return pta.createPortal(location.getX(), location.getY(), location.getZ(), this.getCreationRadius());
    }

    @Override
    public TravelAgent setSearchRadius(int radius) {
        this.searchRadius = radius;
        return this;
    }

    @Override
    public int getSearchRadius() {
        return this.searchRadius;
    }

    @Override
    public TravelAgent setCreationRadius(int radius) {
        this.creationRadius = radius < 2 ? 0 : radius;
        return this;
    }

    @Override
    public int getCreationRadius() {
        return this.creationRadius;
    }

    @Override
    public boolean getCanCreatePortal() {
        return this.canCreatePortal;
    }

    @Override
    public void setCanCreatePortal(boolean create) {
        this.canCreatePortal = create;
    }
}
