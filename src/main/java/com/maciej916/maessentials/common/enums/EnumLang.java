package com.maciej916.maessentials.common.enums;


import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.common.interfaces.text.ILangEntry;

public enum EnumLang implements ILangEntry {
    GENERIC("", "spawn"),
    SPAWN("", "spawn"),

    TELEPORT_IN("teleport", "teleporting"),
    TELEPORT_TO("teleport", "teleport_to"),
    TELEPORT_DONE("teleport", "teleported"),
    TELEPORT_ACTIVE("teleport", "active"),
    TELEPORT_MOVED("teleport", "moved"),
    TELEPORT_COOLDOWN("teleport", "cooldown"),
    TELEPORT_INVALID("teleport", "invalid"),
    TELEPORT_FAILED("teleport", "failed"),

    TPACCEPT_REQUEST("tpaccept", "request"),
    TPACCEPT_REQUEST_WAIT("tpaccept", "request.wait"),
    TPACCEPT_TARGET("tpaccept", "target"),
    TPACCEPT_TARGET_WAIT("tpaccept", "target.wait"),

    TPDENY_REQUEST("tpdeny", "request"),
    TPDENY_TARGET("tpdeny", "target"),

    TPA_EXPIRED_TARGET("tpa", "expired.target"),
    TPA_EXPIRED_REQUEST("tpa", "expired.request"),
    TPA_MOVED_TARGET("tpa", "expired.target"),
    TPA_MOVED_REQUEST("tpa", "expired.request"),
    TPA_EXIST("tpa", "exist");







    private final String key;

    EnumLang(String path, String type) {
        this(path + (!path.equals("") ? "." : "") + MaEssentials.MODID + "." + type);
    }

    EnumLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }

}
