package com.tenjava.entries.joaopms.t1.event;

import com.tenjava.entries.joaopms.t1.upgrade.UpgradeManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UpgradeCraftEvent implements Listener {
    @EventHandler
    public void anvilName(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory inventory = event.getInventory();
        // Checks if we are working with an anvil
        if (!(inventory instanceof AnvilInventory))
            return;

        InventoryView inventoryView = event.getView();
        int rawSlot = event.getRawSlot();
        // Checks if the raw slot is the same as the inventory view
        if (rawSlot != inventoryView.convertSlot(rawSlot))
            return;

        /*
            Checks if the raw slot is 2

            Slot 0 = Left Item
            Slot 1 = Right Item
            Slot 2 = Final Item
         */
        if (rawSlot != 2)
            return;

        // Gets the result item
        ItemStack item = event.getCurrentItem();

        // Checks if the item is null
        if (item == null || item.getType() == Material.AIR)
            return;

        ItemMeta itemMeta = item.getItemMeta();
        String itemName = itemMeta.getDisplayName();

        // Checks if the item's name is null
        if (itemName == null)
            return;

        // Lower case the item's name
        itemName = itemName.toLowerCase();

        // Checks if the item's name starts with "vehicle upgrade:"
        if (!itemName.startsWith("vehicle upgrade:"))
            return;

        // Removes the chest prefix
        String upgradeName = itemName.replace("vehicle upgrade:", "");
        // Removes unnecessary spaces
        upgradeName = upgradeName.trim();

        // Checks if the upgrade exists
        if (!UpgradeManager.doesUpgradeExist(upgradeName)) {
            // Send an error message to the player
            player.sendMessage("§cThat upgrade doesn't exist! Do §4/vupgrades §cto see what upgrades are available!");
            // Play a sound to the player
            player.playSound(player.getLocation(), Sound.DONKEY_ANGRY, 1F, 1F);
            // Cancel the event
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void chestClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();
        // Checks if the action is the one we want and the block is a chest
        if (!(action == Action.LEFT_CLICK_BLOCK && block.getType() == Material.CHEST))
            return;

        // Checks if the item is null
        ItemStack item = event.getItem();
        if (item == null)
            return;

        // Checks if the item is a stick
        if (item.getType() != Material.STICK)
            return;

        ItemMeta itemMeta = item.getItemMeta();
        String itemName = itemMeta.getDisplayName();

        // Checks if the stick's name is null
        if (itemName == null)
            return;

        // Checks if the stick's name is "Upgrade Wand"
        if (!itemName.equalsIgnoreCase("Upgrade Wand"))
            return;

        Chest chest = (Chest) block.getState();
        String chestName = chest.getBlockInventory().getName().toLowerCase();

        // Checks if the chest's name starts with "vehicle upgrade:"
        if (!chestName.startsWith("vehicle upgrade:"))
            return;

        ItemStack[] chestContents = chest.getBlockInventory().getContents();
        List<ItemStack> validChestItems = new ArrayList<>();

        for (int i = 0; i < chestContents.length; i++) {
            // Checks if the item is null
            if (chestContents[i] == null)
                continue;

            // Adds the item to the valid chest items list
            validChestItems.add(chestContents[i]);
        }

        // Removes the chest prefix
        String upgradeName = chestName.replace("vehicle upgrade:", "");
        // Removes unnecessary spaces
        upgradeName = upgradeName.trim();

        // Cancel the event
        event.setCancelled(true);

        // Checks if the upgrade can be crafted
        if (!UpgradeManager.canCraftUpgrade(upgradeName, validChestItems)) {
            return;
        }

        Location blockLocation = block.getLocation();
        for (int i = 0; i < 4; i++)
            block.getWorld().strikeLightningEffect(blockLocation);
        chest.getBlockInventory().clear();

        block.breakNaturally();
        System.out.println("random thing goes here");
    }
}
