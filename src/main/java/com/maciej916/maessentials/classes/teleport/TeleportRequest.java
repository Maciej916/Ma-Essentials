package com.maciej916.maessentials.classes.teleport;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;

import static com.maciej916.maessentials.libs.Methods.currentTimestamp;

public class TeleportRequest {

    private ServerPlayerEntity creator;
    private ServerPlayerEntity player;
    private ServerPlayerEntity target;
    private long timeout;
    private long delay;
    private long teleportTime;
    private boolean accepted = false;

    public TeleportRequest(ServerPlayerEntity creator, ServerPlayerEntity player, ServerPlayerEntity target, long delay) {
        this.creator = creator;
        this.player = player;
        this.target = target;
        this.delay = delay;
        this.timeout = currentTimestamp() + delay;
    }

    public ServerPlayerEntity getCreator() {
        return creator;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public ITextComponent getPlayerName() {
        return player.getDisplayName();
    }

    public ServerPlayerEntity getTarget() {
        return target;
    }

    public ITextComponent getTargetName() {
        return target.getDisplayName();
    }

    public Location getDestination() {
        return new Location(target);
    }

    public long getTimeout() {
        return timeout;
    }

    public long getDelay() {
        return delay;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted() {
        this.teleportTime = currentTimestamp() + ConfigValues.tpa_delay;
        this.accepted = true;
    }

    public long getTeleportTime() {
        return teleportTime;
    }
}
