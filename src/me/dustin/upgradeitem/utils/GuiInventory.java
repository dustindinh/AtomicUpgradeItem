package me.dustin.upgradeitem.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.dustin.upgradeitem.main.AtomicUpgradeItem;

public class GuiInventory implements InventoryHolder {
	private Inventory inv;
	AtomicGuiItem[] actions;

	public GuiInventory(int size, String title) {
		inv = Bukkit.createInventory(this, size, title);
		actions = new AtomicGuiItem[size];
	}

	@Override
	public Inventory getInventory() {
		return inv;
	}
	public ItemStack getItemBySlot( int slot) {
		return inv.getItem(slot);
	}
	public void setItem(int slot, AtomicGuiItem item) {
		if (slot >= inv.getSize())
			return;
		inv.setItem(slot, item.getItem());
		actions[slot] = item;
	}
	
	
	public void setItem(int slot, AtomicGuiItem item, String name) {
		
		item.getItem().getItemMeta().setDisplayName(name+ item.getItem().getItemMeta().getDisplayName());
		if (slot >= inv.getSize())
			return;
		inv.setItem(slot, item.getItem());
		actions[slot] = item;
	}

	public void onClick(InventoryClickEvent e) {

		AtomicGuiItem i = actions[e.getSlot()];
		
		if (i != null) {
			i.onClick(e);
		} else {
			
			e.setCancelled(true);
		}
		
	}
	public void onClose(InventoryCloseEvent e) {
		
		Player p = (Player) e.getPlayer();
		
		if ( AtomicUpgradeItem.itemInGui2.containsKey(p.getName())) {
			
			for ( ItemStack i : AtomicUpgradeItem.itemInGui2.get(p.getName())) {
				p.getInventory().addItem(i);
			}
		
			AtomicUpgradeItem.itemInGui2.remove(p.getName());
		}
		Bukkit.getScheduler().cancelTasks(AtomicUpgradeItem.getPlugin(AtomicUpgradeItem.class));
		
	}
}
