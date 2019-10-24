package me.dustin.upgradeitem.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.dustin.upgradeitem.api.UpgradeItemAPI;
import me.dustin.upgradeitem.main.AtomicUpgradeItem;
import me.dustin.upgradeitem.utils.GuiInventory;
import me.dustin.upgradeitem.utils.AtomicGuiItem;

public class UpgradeItemGui extends GuiInventory{

	private List<String> playerRunningTask = new ArrayList<>();
	private List<ItemStack> listItemInGui = new ArrayList<>();
	private static Plugin pl = AtomicUpgradeItem.getPlugin(AtomicUpgradeItem.class);
	private double totalChance = 0;
	public UpgradeItemGui(Player p) {
		
		
		super(54, pl.getConfig().getString("UpgradeItemGui.Title").replaceAll("&", "§"));
		Bukkit.getScheduler().getPendingTasks().clear();
		AtomicGuiItem border = new AtomicGuiItem(Material.STAINED_GLASS_PANE, 1, (short) 15, " ") {
			@Override
			public void onClick( InventoryClickEvent e) {
				e.setCancelled(true);
			}			
		};
		

		AtomicGuiItem fill = new AtomicGuiItem(Material.STAINED_GLASS_PANE, 1, (short) 7, " ") {
			@Override
			public void onClick( InventoryClickEvent e) {
				e.setCancelled(true);
			}			
		};
		
		ItemStack protection = UpgradeItemAPI.getGuiItemByName("Protection");
		
		AtomicGuiItem protectionItem = new AtomicGuiItem(protection) {
			@Override
			public void onClick( InventoryClickEvent e) {
				
				ItemStack protectionPaper = UpgradeItemAPI.getItemByName("Protection");
				
				if ( e.getCurrentItem().isSimilar(protectionPaper)) {
					if ( p.getInventory().containsAtLeast(protectionPaper, 1)) {
						
						ItemStack newItem = new ItemStack(e.getCurrentItem());
						
						newItem.setAmount(e.getCurrentItem().getAmount()+1);
						listItemInGui.add(protectionPaper);
						AtomicUpgradeItem.itemInGui2.put(p.getName(), listItemInGui);
						p.getInventory().removeItem(protectionPaper);
						p.updateInventory();
						e.setCurrentItem(newItem);
						e.setCancelled(true);
						return ;
						
					}
				}
				if ( p.getInventory().containsAtLeast(protectionPaper, 1)) {
					
					listItemInGui.add(protectionPaper);
					AtomicUpgradeItem.itemInGui2.put(p.getName(), listItemInGui);
					p.getInventory().removeItem(protectionPaper);
					p.updateInventory();
					e.setCurrentItem(protectionPaper);
					e.setCancelled(true);
					return;
					
				}
				p.sendMessage("§cTrong túi bạn không có bùa an toàn");
				
				e.setCancelled(true);
			}
		};

		
		ItemStack upchance = UpgradeItemAPI.getGuiItemByName("Upchance");
		
		AtomicGuiItem upchanceItem = new AtomicGuiItem(upchance) {
			@Override
			public void onClick( InventoryClickEvent e) {
				
				ItemStack upchancePaper = UpgradeItemAPI.getItemByName("Upchance");
				
				if ( e.getCurrentItem().equals(upchancePaper)) {
					p.sendMessage("§cĐã bỏ bùa an toàn vào rồi ");
					e.setCancelled(true);
					return;
				}
				if ( p.getInventory().containsAtLeast(upchancePaper, 1)) {
					
					listItemInGui.add(upchancePaper);
					AtomicUpgradeItem.itemInGui2.put(p.getName(), listItemInGui);
					p.getInventory().removeItem(upchancePaper);
					p.updateInventory();
					e.setCurrentItem(upchancePaper);
					e.setCancelled(true);
					return;
					
				}
				p.sendMessage("§cTrong túi bạn không có bùa an toàn");
				
				e.setCancelled(true);
			}
		};
		
		AtomicGuiItem Processing = new AtomicGuiItem(
				Material.getMaterial(pl.getConfig().getString("UpgradeItemGui.Processing.Type")), 1,
				(short) pl.getConfig().getInt("UpgradeItemGui.Processing.Data"), " ") {
			@Override
			public void onClick( InventoryClickEvent e) {
				e.setCancelled(true);
			}			
		};
		
		
		ItemStack upgradeItemStack = UpgradeItemAPI.getGuiItemByName("Upgrade");
		ItemMeta imUpgrade = upgradeItemStack.getItemMeta();
		imUpgrade.setLore(UpgradeItemAPI.repalceAllLore(imUpgrade.getLore(), "%chance%", "0"));
		upgradeItemStack.setItemMeta(imUpgrade);
		
		AtomicGuiItem confirmUpgrade = new AtomicGuiItem(upgradeItemStack) {
			@Override
			public void onClick(InventoryClickEvent e) {
				if ( playerRunningTask.contains(p.getName())) {
					p.sendMessage("§cĐang trong quá trình cường hoá");
					e.setCancelled(true);
					return;
				}
				if ( p.getEquipment().getItemInMainHand().getType().equals(Material.AIR)) {
					p.sendMessage("§cHãy cầm trang bị cần cường hoá trên tay");
					e.setCancelled(true);
					return;
				} else if( !UpgradeItemAPI.isValidMaterial(p.getEquipment().getItemInMainHand())) {
					p.sendMessage("§cTrang bị trên tay bạn không thể cường hoá");
					e.setCancelled(true);
					return;
				} else if ( !UpgradeItemAPI.hasAnyLoreStat(p.getEquipment().getItemInMainHand())) {
					p.sendMessage("§cTrang bị không có thuộc tính có thể cường hoá");
					e.setCancelled(true);
					return;
				}
				
				playerRunningTask.add(e.getWhoClicked().getName());
				
				BukkitTask task = new BukkitRunnable() {
					int slot = 0;
					@Override
					public void run() {
						
						UpgradeItemGui.this.setItem(pl.getConfig()
								.getIntegerList("UpgradeItemGui.PreProcess.Slots")
								.get(slot),
								Processing);
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
						slot++;
						
						// End of array, starting check slot 
						if ( slot == 9) {
							this.cancel();
							playerRunningTask.remove(p.getName());
							updatePreProcess();
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
							
							p.getInventory().setItemInMainHand( UpgradeItemAPI.getItemUpgraded(
									p.getInventory().getItemInMainHand()
									, UpgradeItemAPI.getItemLevel(
											p.getInventory().getItemInMainHand())+1));

							UpgradeItemGui.this.setItem(49, updateChanceButton(,p,1));
							
							UpgradeItemGui.this.setItem(13,
									new AtomicGuiItem(p.getEquipment().getItemInMainHand() ) {
								@Override
								public void onClick(InventoryClickEvent e) {
									e.setCancelled(true);
								}
							});
						}

					}
					
				}.runTaskTimer(pl, 0, 4);
				UpgradeItemGui.this.setItem(49, updateChanceButton(this,p,1));
				e.setCancelled(true);
			}
		};
		
		
		ItemStack stone1 = UpgradeItemAPI.getGuiItemByName("Stone1");
		
		AtomicGuiItem stoneItemLv1 = new AtomicGuiItem(stone1) {
			@Override
			public void onClick( InventoryClickEvent e) {
				
				if ( playerRunningTask.contains(p.getName())) {
					p.sendMessage("§cĐang trong quá trình cường hoá");
					e.setCancelled(true);
					return;
				}
				if ( p.getEquipment().getItemInMainHand().getType().equals(Material.AIR)) {
					p.sendMessage("§cHãy cầm trang bị cần cường hoá trên tay");
					e.setCancelled(true);
					return;
				} else if( !UpgradeItemAPI.isValidMaterial(p.getEquipment().getItemInMainHand())) {
					p.sendMessage("§cTrang bị trên tay bạn không thể cường hoá");
					e.setCancelled(true);
					return;
				} else if ( !UpgradeItemAPI.hasAnyLoreStat(p.getEquipment().getItemInMainHand())) {
					p.sendMessage("§cTrang bị không có thuộc tính có thể cường hoá");
					e.setCancelled(true);
					return;
				}
				
				ItemStack stone = UpgradeItemAPI.getStoneByLevel(1);
				
				if ( e.getCurrentItem().isSimilar(stone)) {
					if ( p.getInventory().containsAtLeast(stone, 1)) {
						
						ItemStack newItem = new ItemStack(e.getCurrentItem());
						newItem.setAmount(e.getCurrentItem().getAmount()+1);
						
						putItemToCallBackList(stone, p);
						
						p.getInventory().removeItem(stone);
						p.updateInventory();
						e.setCurrentItem(newItem);
						e.setCancelled(true);
								
						return ;
						
					}
				}
				if ( p.getInventory().containsAtLeast(stone, 1)) {
					
					putItemToCallBackList(stone, p);
					
					p.getInventory().removeItem(stone);
					p.updateInventory();
					e.setCurrentItem(stone);
					e.setCancelled(true);
					
					
					UpgradeItemGui.this.setItem(49, updateChanceButton(confirmUpgrade,p,1));
					
					return;

				}
				
				p.sendMessage("§cTrong túi bạn không có Đá Cường Hoá 1");
				e.setCancelled(true);	
			}
		};

		ItemStack stone2 = UpgradeItemAPI.getGuiItemByName("Stone2");
		
		AtomicGuiItem stoneItemLv2 = new AtomicGuiItem(stone2) {
			@Override
			public void onClick( InventoryClickEvent e) {
				if (!UpgradeItemAPI.isValidMaterial(p.getInventory().getItemInMainHand())) {
					p.sendMessage("§cHãy cầm trên tay trang bị hợp lệ");
					e.setCancelled(true);
					return;
				};
				
				ItemStack stone = UpgradeItemAPI.getStoneByLevel(2);
				if ( e.getCurrentItem().isSimilar(stone)) {
					if ( p.getInventory().containsAtLeast(stone, 1)) {
						
						ItemStack newItem = new ItemStack(e.getCurrentItem());
						newItem.setAmount(e.getCurrentItem().getAmount()+1);
						
						putItemToCallBackList(stone, p);
						
						p.getInventory().removeItem(stone);
						p.updateInventory();
						e.setCurrentItem(newItem);
						e.setCancelled(true);
						return ;
						
					}
				}
				if ( p.getInventory().containsAtLeast(stone, 1)) {
					
					putItemToCallBackList(stone, p);
					
					p.getInventory().removeItem(stone);
					p.updateInventory();
					e.setCurrentItem(stone);
					e.setCancelled(true);
					
					UpgradeItemGui.this.setItem(49, updateChanceButton(confirmUpgrade,p,2));
					
					return;
					
				}
				p.sendMessage("§cTrong túi bạn không có Đá Cường Hoá 2");
				
				e.setCancelled(true);
					
			}
		};

		
		ItemStack stone3 = UpgradeItemAPI.getGuiItemByName("Stone3");
		
		AtomicGuiItem stoneItemLv3 = new AtomicGuiItem(stone3) {
			@Override
			public void onClick( InventoryClickEvent e) {
				if (!UpgradeItemAPI.isValidMaterial(p.getInventory().getItemInMainHand())) {
					p.sendMessage("§cHãy cầm trên tay trang bị hợp lệ");
					e.setCancelled(true);
					return;
				};
				ItemStack stone = UpgradeItemAPI.getStoneByLevel(3);
				if ( e.getCurrentItem().isSimilar(stone)) {
					if ( p.getInventory().containsAtLeast(stone, 1)) {
						
						ItemStack newItem = new ItemStack(e.getCurrentItem());
						newItem.setAmount(e.getCurrentItem().getAmount()+1);
						
						putItemToCallBackList(stone, p);
						
						p.getInventory().removeItem(stone);
						p.updateInventory();
						e.setCurrentItem(newItem);
						e.setCancelled(true);
						return ;
						
					}
				}
				if ( p.getInventory().containsAtLeast(stone, 1)) {
					
					putItemToCallBackList(stone, p);
					
					p.getInventory().removeItem(stone);
					p.updateInventory();
					e.setCurrentItem(stone);
					e.setCancelled(true);
					
					UpgradeItemGui.this.setItem(49, updateChanceButton(confirmUpgrade,p,3));
					
					return;
					
				}
				p.sendMessage("§cTrong túi bạn không có Đá Cường Hoá 3");
				
				e.setCancelled(true);
					
			}
		};

		
		ItemStack stone4 = UpgradeItemAPI.getGuiItemByName("Stone4");
		
		AtomicGuiItem stoneItemLv4 = new AtomicGuiItem(stone4) {
			@Override
			public void onClick( InventoryClickEvent e) {
				if (!UpgradeItemAPI.isValidMaterial(p.getInventory().getItemInMainHand())) {
					p.sendMessage("§cHãy cầm trên tay trang bị hợp lệ");
					e.setCancelled(true);
					return;
				};
				ItemStack stone = UpgradeItemAPI.getStoneByLevel(4);
				if ( e.getCurrentItem().isSimilar(stone)) {
					if ( p.getInventory().containsAtLeast(stone, 1)) {
						
						ItemStack newItem = new ItemStack(e.getCurrentItem());
						newItem.setAmount(e.getCurrentItem().getAmount()+1);
						
						putItemToCallBackList(stone, p);
						
						p.getInventory().removeItem(stone);
						p.updateInventory();
						e.setCurrentItem(newItem);
						e.setCancelled(true);
						return ;
						
					}
				}
				if ( p.getInventory().containsAtLeast(stone, 1)) {
					
					putItemToCallBackList(stone, p);
					
					p.getInventory().removeItem(stone);
					p.updateInventory();
					e.setCurrentItem(stone);
					e.setCancelled(true);
					
					UpgradeItemGui.this.setItem(49, updateChanceButton(confirmUpgrade,p,4));
					
					return;
					
				}
				p.sendMessage("§cTrong túi bạn không có Đá Cường Hoá 4");
				
				e.setCancelled(true);
					
			}
		};

		ItemStack stone5 = UpgradeItemAPI.getGuiItemByName("Stone5");
		
		AtomicGuiItem stoneItemLv5 = new AtomicGuiItem(stone5) {
			@Override
			public void onClick( InventoryClickEvent e) {
				if (!UpgradeItemAPI.isValidMaterial(p.getInventory().getItemInMainHand())) {
					p.sendMessage("§cHãy cầm trên tay trang bị hợp lệ");
					e.setCancelled(true);
					return;
				};
				ItemStack stone = UpgradeItemAPI.getStoneByLevel(5);
				if ( e.getCurrentItem().isSimilar(stone)) {
					if ( p.getInventory().containsAtLeast(stone, 1)) {
						
						ItemStack newItem = new ItemStack(e.getCurrentItem());
						newItem.setAmount(e.getCurrentItem().getAmount()+1);
						
						putItemToCallBackList(stone, p);
						
						p.getInventory().removeItem(stone);
						p.updateInventory();
						e.setCurrentItem(newItem);
						e.setCancelled(true);
						return ;
						
					}
				}
				if ( p.getInventory().containsAtLeast(stone, 1)) {
					
					putItemToCallBackList(stone, p);
					
					p.getInventory().removeItem(stone);
					p.updateInventory();
					e.setCurrentItem(stone);
					e.setCancelled(true);
					
					UpgradeItemGui.this.setItem(49, updateChanceButton(confirmUpgrade,p,5));
					
					return;
					
				}
				p.sendMessage("§cTrong túi bạn không có Đá Cường Hoá 5");
				e.setCancelled(true);
					
			}
		};
		AtomicGuiItem upgradeItem = new AtomicGuiItem(p.getEquipment().getItemInMainHand()) {
			@Override
			public void onClick(InventoryClickEvent e) {
			
				e.setCancelled(true);
			}
		};
		AtomicGuiItem preProcess = new AtomicGuiItem(
				Material.getMaterial(pl.getConfig().getString("UpgradeItemGui.PreProcess.Type")), 1,
				(short) pl.getConfig().getInt("UpgradeItemGui.PreProcess.Data"), " ") {
			@Override
			public void onClick( InventoryClickEvent e) {
				e.setCancelled(true);
			}			
		};

		
		ItemStack helpItemStack = UpgradeItemAPI.getGuiItemByName("Help");
		
		AtomicGuiItem helpItem = new AtomicGuiItem(helpItemStack) {
			@Override
			public void onClick(InventoryClickEvent e) {
				
				e.setCancelled(true);
			}
		};
		

		
		
		 pl.getConfig().getIntegerList("UpgradeItemGui.Border.Slots").forEach( (i)-> {
			 setItem(i, border);
		 });
		 pl.getConfig().getIntegerList("UpgradeItemGui.Fill.Slots").forEach( (i)-> {
			 setItem(i, fill);
		 });
		 pl.getConfig().getIntegerList("UpgradeItemGui.PreProcess.Slots").forEach( (i)-> {
			 setItem(i, preProcess);
		 });
		 
		setItem(13, upgradeItem);
		setItem(31, helpItem);
		setItem(49, confirmUpgrade);
		setItem(11, protectionItem);
		setItem(15, upchanceItem);
		setItem(38, stoneItemLv1);
		setItem(39, stoneItemLv2);
		setItem(40, stoneItemLv3);
		setItem(41, stoneItemLv4);
		setItem(42, stoneItemLv5);

	}
	
	public void updatePreProcess() {
		AtomicGuiItem preProcess = new AtomicGuiItem(
				Material.getMaterial(pl.getConfig().getString("UpgradeItemGui.PreProcess.Type")), 1,
				(short) pl.getConfig().getInt("UpgradeItemGui.PreProcess.Data"), " ") {
			@Override
			public void onClick( InventoryClickEvent e) {
				e.setCancelled(true);
			}			
		};
		 pl.getConfig().getIntegerList("UpgradeItemGui.PreProcess.Slots").forEach( (i)-> {
			 setItem(i, preProcess);
		 });

	}
	public AtomicGuiItem updateChanceButton(AtomicGuiItem ui, Player p, int stone) {
		
		
		double chance = UpgradeItemAPI.getLevelChance(stone,
				UpgradeItemAPI.getItemLevel(p.getInventory().getItemInMainHand())+1);
		totalChance += chance;
		
		ItemStack upgradeItemStack = UpgradeItemAPI.getGuiItemByName("Upgrade");
		
		List<String> preLore = upgradeItemStack.getItemMeta().getLore();

		if ( totalChance > 100) {
			totalChance = 100;
		} 
		ui.setLore(UpgradeItemAPI.repalceAllLore(preLore,
				"%chance%", String.valueOf(totalChance)));
		
		return ui;
		
	}
	public void putItemToCallBackList(ItemStack i, Player p) {
		listItemInGui.add(i);
		AtomicUpgradeItem.itemInGui2.put(p.getName(), listItemInGui);
	}
	public void removeStoneInList() {
		
	}

	public void checkItem(Player p, InventoryClickEvent e) {
		if ( playerRunningTask.contains(p.getName())) {
			p.sendMessage("§cĐang trong quá trình cường hoá");
			e.setCancelled(true);
			return;
		}
		if ( p.getEquipment().getItemInMainHand().getType().equals(Material.AIR)) {
			p.sendMessage("§cHãy cầm trang bị cần cường hoá trên tay");
			e.setCancelled(true);
			return;
		} else if( !UpgradeItemAPI.isValidMaterial(p.getEquipment().getItemInMainHand())) {
			p.sendMessage("§cTrang bị trên tay bạn không thể cường hoá");
			e.setCancelled(true);
			return;
		} else if ( !UpgradeItemAPI.hasAnyLoreStat(p.getEquipment().getItemInMainHand())) {
			p.sendMessage("§cTrang bị không có thuộc tính có thể cường hoá");
			e.setCancelled(true);
			return;
		}
	}

}
