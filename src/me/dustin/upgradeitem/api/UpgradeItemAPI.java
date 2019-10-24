package me.dustin.upgradeitem.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import api.praya.myitems.builder.element.Element;
import api.praya.myitems.builder.lorestats.LoreStatsEnum;
import api.praya.myitems.builder.lorestats.LoreStatsOption;
import api.praya.myitems.main.MyItemsAPI;
import api.praya.myitems.manager.game.ElementManagerAPI;
import api.praya.myitems.manager.game.LoreStatsManagerAPI;
import me.dustin.upgradeitem.main.AtomicUpgradeItem;

public class UpgradeItemAPI {
	private static Plugin pl = AtomicUpgradeItem.getPlugin(AtomicUpgradeItem.class);
	public static List<String> getColorLore(List<String> list) {
		List<String> colored = new ArrayList<>();
		
		for ( String str : list) {
			colored.add( str.replaceAll("&", "§"));
		}
		return colored;
	}
	
	public static boolean isValidMaterial(ItemStack i) {
		
		if (!pl.getConfig().getStringList("ValidMaterial").contains(i.getType().toString()))
			return false;
		return true;
	}
	public static boolean hasAnyLoreStat(ItemStack i) {
				
		 for ( LoreStatsEnum stat : LoreStatsEnum.values()) {
			 if ( MyItemsAPI.getInstance().getGameManagerAPI().getLoreStatsManagerAPI().hasLoreStats(i, stat)) {
				 return true;
			 }
		 }
		
		 return false;
	}
	
	public static List<String> repalceAllLore(List<String> lore, String inlist, String replace) {
		
		List<String> newList = new ArrayList<>();
		for ( int i = 0; i < lore.size(); i++ ) {
			newList.add(lore.get(i).replaceAll(inlist, replace));
		}
		
		return newList;
	}
	public static ItemStack getItemProtection() {
		ItemStack protection = new ItemStack(Material
				.getMaterial(pl.getConfig().getString("UpgradeItemGui.Protection.Type")));
		ItemMeta protectionItemMeta = protection.getItemMeta();
		protectionItemMeta.setLore( UpgradeItemAPI
				.getColorLore(pl.getConfig().getStringList("UpgradeItemGui.Protection.Lore")));
		protectionItemMeta.setDisplayName(
				pl.getConfig().getString("UpgradeItemGui.Protection.Name").replaceAll("&", "§"));
		
		protection.setItemMeta(protectionItemMeta);
		
		return protection;
	}
	
	public static ItemStack getItemByName(String name) {
		ItemStack item = new ItemStack(
				Material.getMaterial(pl.getConfig().getString("UpgradeItem."+name+".Type"))
				,1);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(
				pl.getConfig().getString("UpgradeItem."+name+".Name").replaceAll("&", "§"));
		im.setLore(
				UpgradeItemAPI.getColorLore(pl.getConfig().getStringList("UpgradeItem."+name+".Lore")));
		item.setItemMeta(im);
		
		return item;
	}
	
	public static boolean isContainStoneList(List<ItemStack> list, ItemStack i) {
		for ( ItemStack ilist : list) {
			if ( ilist.isSimilar(i)) {
				return true;
			}
		}
		return false;
	}
	
	public static ItemStack increaseAmountOfItem(ItemStack i, int amount) { 
		
		ItemStack newItem = new ItemStack(i.getType(), i.getAmount() + amount);
		ItemMeta im = i.getItemMeta();
		newItem.setItemMeta(im);
		return newItem;
		
	}
	public static ItemStack getGuiItemByName(String name) {
		
		ItemStack item = new ItemStack(Material
				.getMaterial(pl.getConfig().getString("UpgradeItemGui."+name+".Type")));
		ItemMeta im = item.getItemMeta();
		im.setLore( UpgradeItemAPI
				.getColorLore(pl.getConfig().getStringList("UpgradeItemGui."+name+".Lore")));
		im.setDisplayName(
				pl.getConfig().getString("UpgradeItemGui."+name+".Name").replaceAll("&", "§"));
		item.setItemMeta(im);
		
		
		return item;
	}
	
	public static ItemStack getStoneByLevel(int level) {
		
		ItemStack item = new ItemStack(Material
				.getMaterial(pl.getConfig().getString("UpgradeItem.Stone"+level+".Type")));
		ItemMeta im = item.getItemMeta();
		im.setLore( UpgradeItemAPI
				.getColorLore(pl.getConfig().getStringList("UpgradeItem.Stone"+level+".Lore")));
		im.setDisplayName(
				pl.getConfig().getString("UpgradeItem.Stone"+level+".Name").replaceAll("&", "§"));
		item.setItemMeta(im);
		
		
		return item;
	}
	
	public static double getLevelChance(int stone, int level) {
		
		return pl.getConfig().getDouble("UpgradeItem.Stone"+stone+".Chance.Level"+level);
		
	}
	public static int getItemLevel(ItemStack i) {
		
		if ( i.getItemMeta().getDisplayName() == null)
			return 0;
		String itemName = i.getItemMeta().getDisplayName().replaceAll("§", "");
		
		for ( int index = 1 ; index < 12; index++) {
			if ( itemName.contains("8[a+clo"+index+"8]")) {
				return index;
			}
		}
		return 0;
	}
	public static String getFormatLevel( int level ) {
		return " §8[§a+§c§l§o" + level +"§8]";
	}
	public static String getPreName(ItemStack i) {
	
		String itemName = i.getItemMeta().getDisplayName();
		
		if ( itemName == null) {
			itemName = i.getType().toString();
		}
		for ( int index = 1 ; index < 12; index++) {
			
			String temp = "§8[§a+§c§l§o"+index+"§8]";
			if ( itemName.contains(temp)) {
				return itemName.replace(" "+temp, "");
			}
		}
		
		return itemName;
		
	}
	
	public static double getChanceByItemLevel(int stoneLevel , int itemLevel) {
		return pl.getConfig().getDouble("UpgradeItem.Stone"+stoneLevel+".Chance.Level"+itemLevel);
	}
	
	public static List<ItemStack> getStoneList() {
		List<ItemStack> listStone = new ArrayList<>();
		for ( int i = 1; i < 6;i++) {
			listStone.add( getStoneByLevel(i));
		}
		return listStone;
	}
	
	public static ItemStack getItemFailed(ItemStack i, int level) {
		
		if ( level <= 3 && level >= 1) {
			return getItemUpgraded(i,level);
		}
		
		ItemStack upgradeItem = i;
		LoreStatsManagerAPI loreManage = MyItemsAPI.getInstance().getGameManagerAPI().getLoreStatsManagerAPI();
		ItemMeta upgradeItemMeta = upgradeItem.getItemMeta();
		List<String> upgradeItemLore = upgradeItemMeta.getLore();
		
		ConfigurationSection sec = pl.getConfig().getConfigurationSection("Stats.Level"+level);

		Map<String, Object> validStat = sec.getValues(false);
		
		for ( String statName : validStat.keySet()) {
			LoreStatsEnum stat = LoreStatsEnum.get(statName);
			
			if ( loreManage.hasLoreStats(upgradeItem, stat)) {
				Double currentValue = loreManage.getLoreValue(upgradeItem, stat, LoreStatsOption.CURRENT);
				Double newValue = currentValue + Double.valueOf(validStat.get(statName).toString());
				
				String currentStat = loreManage.getTextLoreStats(stat, currentValue);
				String newStat = loreManage.getTextLoreStats(stat, Math.round(newValue));
				
				for ( int index = 0; index < upgradeItemLore.size(); index++) {
					if ( upgradeItemLore.get(index).equals(currentStat)) {
						upgradeItemLore.set(index, newStat);
					}
				}
				
			}
		}
		if ( upgradeItem.getItemMeta().getDisplayName() == null) {
			upgradeItemMeta.setDisplayName(getPreName(upgradeItem)+ getFormatLevel(level));
		} else {
			upgradeItemMeta.setDisplayName(getPreName(upgradeItem)+ getFormatLevel(level));
		}
		upgradeItemMeta.setLore(upgradeItemLore);
		upgradeItem.setItemMeta(upgradeItemMeta);
		
		return upgradeItem;
	}

	public static ItemStack getItemUpgraded(ItemStack i, int level) {
		
		ItemStack upgradeItem = i;
		LoreStatsManagerAPI loreManage = MyItemsAPI.getInstance().getGameManagerAPI().getLoreStatsManagerAPI();
		ItemMeta upgradeItemMeta = upgradeItem.getItemMeta();
		List<String> upgradeItemLore = upgradeItemMeta.getLore();
		
		ConfigurationSection sec = pl.getConfig().getConfigurationSection("Stats.Level"+level);

		Map<String, Object> validStat = sec.getValues(false);
		
		for ( String statName : validStat.keySet()) {
			LoreStatsEnum stat = LoreStatsEnum.get(statName);
			
			if ( loreManage.hasLoreStats(upgradeItem, stat)) {
				Double currentValue = loreManage.getLoreValue(upgradeItem, stat, LoreStatsOption.CURRENT);
				Double newValue = currentValue + Double.valueOf(validStat.get(statName).toString());
				
				String currentStat = loreManage.getTextLoreStats(stat, currentValue);
				String newStat = loreManage.getTextLoreStats(stat, Math.round(newValue));
				
				for ( int index = 0; index < upgradeItemLore.size(); index++) {
					if ( upgradeItemLore.get(index).equals(currentStat)) {
						upgradeItemLore.set(index, newStat);
					}
				}
				
			}
		}
		if ( upgradeItem.getItemMeta().getDisplayName() == null) {
			upgradeItemMeta.setDisplayName(getPreName(upgradeItem)+ getFormatLevel(level));
		} else {
			upgradeItemMeta.setDisplayName(getPreName(upgradeItem)+ getFormatLevel(level));
		}
		upgradeItemMeta.setLore(upgradeItemLore);
		upgradeItem.setItemMeta(upgradeItemMeta);
		
		return upgradeItem;
	}
}
