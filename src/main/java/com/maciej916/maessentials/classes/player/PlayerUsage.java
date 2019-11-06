package com.maciej916.maessentials.classes.player;

import java.util.HashMap;
import java.util.Map;

import static com.maciej916.maessentials.libs.Methods.currentTimestamp;

public class PlayerUsage {

    private Map<String, Long> command = new HashMap<>();
    private Map<String, Long> kit = new HashMap<>();
    private Map<String, Long> other = new HashMap<>();

    public PlayerUsage() {
    }

    public void setKitUssage(String name) {
        this.kit.put(name, currentTimestamp());
    }

    public void setKitUssage(String name, long time) {
        this.kit.put(name, time);
    }

    public void setCommandUsage(String command) {
        this.command.put(command, currentTimestamp());
    }

    public void setCommandUsage(String command, long time) {
        this.command.put(command, time);
    }

    public void setOtherUssage(String other) {
        this.other.put(other, currentTimestamp());
    }

    public long getCommandCooldown(String command, long cooldown) {
        return getCooldown(this.command, command, cooldown);
    }

    public long getKitCooldown(String kit, long cooldown) {
        return getCooldown(this.kit, kit, cooldown);
    }

    public long getOtherUssage(String other, long cooldown) {
        return getCooldown(this.other, other, cooldown);
    }

    private long getCooldown(Map<String, Long> map, String key, long cooldown) {
        if (map.containsKey(key)) {
            long currentTime = currentTimestamp();
            long usageTime = map.get(key);
            if (cooldown == 0 || usageTime + cooldown < currentTime) {
                return 0;
            } else {
                long timeleft = usageTime + cooldown - currentTime;
                return timeleft;
            }
        } else {
            return 0;
        }
    }
}
