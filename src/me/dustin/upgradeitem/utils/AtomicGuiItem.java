package me.dustin.upgradeitem.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dustin.upgradeitem.main.AtomicUpgradeItem;

public class AtomicGuiItem {

	private ItemStack item;
	
	public AtomicGuiItem(Material material, int amount, short data) {
		item = new ItemStack(material, amount, data);
	}
	
	public AtomicGuiItem(ItemStack i) {
		
		if ( i == null || i.getType().equals(Material.AIR) ) {
			
			item = new ItemStack(Material.BARRIER);
			ItemMeta im = item.getItemMeta();
			im.setDisplayName("§c Trên tay tróng ");
			item.setItemMeta(im);
			
		} else {
			
			item = new ItemStack(i);
			
			/*item = new ItemStack(i.getType());
			ItemMeta im = i.getItemMeta();
			
			item.setItemMeta(im);
			item.addUnsafeEnchantments(i.getEnchantments());*/
			
		}
	}
	public AtomicGuiItem(Material material, int amount, short data, String name) {
		item = new ItemStack(material, amount, data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		item.setItemMeta(im);
	}

	
	public AtomicGuiItem(Material material, int amount) {
		item = new ItemStack(material, amount);
	}

	public AtomicGuiItem(Material material) {
		item = new ItemStack(material);
	}

	public AtomicGuiItem(Material material, String name) {
		item = new ItemStack(material);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		item.setItemMeta(im);
	}

	public List<String> getItemLore() {
		return item.getItemMeta().getLore();
	}
	public void setLore(List<String> newLore) {
		ItemMeta im = item.getItemMeta();
		
		im.setLore(newLore);
		item.setItemMeta(im);
	
	}
	public void onClick(InventoryClickEvent e) {
		e.setCancelled(true);
		
	}

	public ItemStack getItem() {
		return item;
	}
	public Material getMaterial() {
		return item.getType();
	}
	public ItemStack setItem(ItemStack i) {
		return i;
	}
}
