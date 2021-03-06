package com.tenjava.entries.joaopms.t1.upgrade;

import org.bukkit.Material;

import java.util.Map;

public class Upgrade {
    private String upgradeName;
    private UpgradeType upgradeType;
    private int upgradeLevel;
    private Map<Material, Integer> upgradeMaterials;

    public Upgrade(String upgradeName, UpgradeType upgradeType, int upgradeLevel, Map<Material, Integer> upgradeMaterials) {
        this.upgradeName = upgradeName;
        this.upgradeType = upgradeType;
        this.upgradeLevel = upgradeLevel;
        this.upgradeMaterials = upgradeMaterials;
    }

    /**
     * Gets the materials needed for the upgrade
     *
     * @return Map with the materials and their quantity
     */
    public Map<Material, Integer> getMaterials() {
        return upgradeMaterials;
    }

    /**
     * Gets the upgrade name
     *
     * @return Upgrade name
     */
    public String getName() {
        return upgradeName;
    }

    /**
     * Gets the upgrade type
     *
     * @return Upgrade type
     */
    public UpgradeType getType() {
        return upgradeType;
    }

    /**
     * Gets the upgrade level
     *
     * @return Upgrade level
     */
    public int getLevel() {
        return upgradeLevel;
    }
}
