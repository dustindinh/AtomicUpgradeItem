package me.dustin.upgradeitem.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.dustin.upgradeitem.api.UpgradeItemAPI;
import me.dustin.upgradeitem.command.UpgradeCommand;
import me.dustin.upgradeitem.gui.UpgradeItemGui;
import me.dustin.upgradeitem.utils.GuiInventory;

public class AtomicUpgradeItem extends JavaPlugin implements Listener {
	
	
	public static Map<String, ItemStack> itemInGui = new HashMap<>();
	public static Map<String, List<ItemStack>> itemInGui2 = new HashMap<>();
	
	public void onEnable() {
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		this.getCommand("cuonghoa").setExecutor(new UpgradeCommand());
	

        getServer().getConsoleSender().sendMessage("§aPlugin by Dustin");
		
        getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onClick(InventoryClickEvent e) {
				
				try {
					
					Inventory inv = e.getClickedInventory();
					if (inv.getHolder() != null && inv.getHolder() instanceof GuiInventory)
						((GuiInventory) inv.getHolder()).onClick(e);
					
				} catch( Exception ex) {
					e.setCancelled(true);
				}

			}
        	@EventHandler
        	public void onClose(InventoryCloseEvent e) {
				try {

					Inventory inv = e.getInventory();
					if (inv.getHolder() != null && inv.getHolder() instanceof GuiInventory) {
						((GuiInventory) inv.getHolder()).onClose(e);
					}
				
				} catch( Exception ex) {
					
				}
        	}
		}, this);

        
        getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler

			public void onInteract(PlayerInteractEvent e) {
				
				if ( e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					
					if ( e.getClickedBlock().getType().toString() == "ANVIL") {
						Player p = e.getPlayer();
						ItemStack inHand = e.getPlayer().getEquipment().getItemInMainHand();
						if ( inHand.getType().equals(Material.AIR)) {
							p.sendMessage("§cHãy cầm trang bị trên tay trước");
							e.setCancelled(true);
							return;
						}
						if ( !UpgradeItemAPI.isValidMaterial(inHand)) {
							p.sendMessage("§cTrang bị trên tay không thể cường hoá");
							e.setCancelled(true);
							return;
						}
						Location blockLoc = new Location( Bukkit.getWorld(getConfig().getString("AnvilLocation.World")),
								Double.valueOf(getConfig().getString("AnvilLocation.x")),
								Double.valueOf(getConfig().getString("AnvilLocation.y")),
								Double.valueOf(getConfig().getString("AnvilLocation.z")));
						
						if (  e.getClickedBlock().getLocation().equals(blockLoc)) {
							
							UpgradeItemGui gui = new UpgradeItemGui(p);
							p.openInventory(gui.getInventory());
							e.setCancelled(true);
							
						}
        			}
	
				
				}
			}
		}, this);
		
	}
	
	public void onDisable() {};
}
