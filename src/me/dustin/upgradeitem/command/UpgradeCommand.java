package me.dustin.upgradeitem.command;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import api.praya.myitems.builder.lorestats.LoreStatsEnum;
import me.dustin.upgradeitem.api.UpgradeItemAPI;
import me.dustin.upgradeitem.gui.UpgradeItemGui;
import me.dustin.upgradeitem.main.AtomicUpgradeItem;

public class UpgradeCommand implements CommandExecutor {
	
	private static Plugin pl = AtomicUpgradeItem.getPlugin(AtomicUpgradeItem.class);
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if ( !(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		
		switch( args.length) {
			case 4:
				if ( args[0].equals("get")) {
					switch( args[1]) {
						case "Protection":
							ItemStack protectionPaper = new ItemStack(
									Material.getMaterial(pl.getConfig().getString("UpgradeItem.Protection.Type"))
									);
							ItemMeta protectionPaperMeta = protectionPaper.getItemMeta();
							protectionPaperMeta.setDisplayName(
									pl.getConfig().getString("UpgradeItem.Protection.Name").replaceAll("&", "§"));
							protectionPaperMeta.setLore(
									UpgradeItemAPI.getColorLore(pl.getConfig().getStringList("UpgradeItem.Protection.Lore")));
							protectionPaper.setItemMeta(protectionPaperMeta);
							
							try {
								int amount = Integer.valueOf(args[2]);
								for ( int i = 0; i < amount; i++) {
									p.getInventory().addItem(protectionPaper);
								}
								return true;
							} catch(Exception e) {
								p.sendMessage("§cSố không hợp lệ");
								return true;
							}
						case "Upchance":
							
							ItemStack upchance = new ItemStack(Material
									.getMaterial(pl.getConfig().getString("UpgradeItem.Upchance.Type")));
							ItemMeta upchanceItemMeta = upchance.getItemMeta();
							upchanceItemMeta.setLore( UpgradeItemAPI
									.getColorLore(pl.getConfig().getStringList("UpgradeItem.Upchance.Lore")));
							upchanceItemMeta.setDisplayName(
									pl.getConfig().getString("UpgradeItem.Upchance.Name").replaceAll("&", "§"));
							upchance.setItemMeta(upchanceItemMeta);
							
							try {
								int amount = Integer.valueOf(args[2]);
								for ( int i = 0; i < amount; i++) {
									p.getInventory().addItem(upchance);
								}
								return true;
							} catch(Exception e) {
								p.sendMessage("§cSố không hợp lệ");
								return true;
							}
						case "Stone":
							
							ItemStack item = UpgradeItemAPI.getStoneByLevel(Integer.parseInt(args[2]));
							try {
								int amount = Integer.valueOf(args[3]);
								for ( int i = 0; i < amount; i++) {
									p.getInventory().addItem(item);
								}
								return true;
							} catch(Exception e) {
								p.sendMessage("§cSố không hợp lệ");
								return true;
							}
					}	
				}
				break;
			case 1:
				if ( args[0].equals("check")) {
					
					ConfigurationSection sec = pl.getConfig().getConfigurationSection("Stats.Level"+1);
					Map<String, Object> validStat = sec.getValues(false);
					
					for ( String statName : validStat.keySet()) {
						LoreStatsEnum stat = LoreStatsEnum.get(statName);
						p.sendMessage(stat.toString());
					}
				}
				break;
			default:
				p.sendMessage("CC");
				break;
				
		}
		
		
		return true;

	}

}
