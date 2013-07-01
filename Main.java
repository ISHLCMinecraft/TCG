package ISHLC.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TheCatchGame extends JavaPlugin implements Listener {

  ArrayList<Player> buying = new ArrayList<Player>();
	ArrayList<Player> delay = new ArrayList<Player>();
	String s = "";
	Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		PluginDescriptionFile pdf = this.getDescription();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		this.logger.info("[" + pdf.getName() + "] " + pdf.getName() + " v"
				+ pdf.getVersion() + " has been Enabled!");
		File config = new File(getDataFolder() + "config.yml");
		if (!config.exists()) {
			this.saveDefaultConfig();
		}
		saveConfig();
	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pdf = this.getDescription();
		this.logger.info("[" + pdf.getName() + "] " + pdf.getName()
				+ " has been Disabled!");
		saveConfig();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		for (Player ol : getServer().getOnlinePlayers()) {
			ol.getInventory().setHelmet(null);
			ol.getInventory().setChestplate(null);
			ol.getInventory().setLeggings(null);
			ol.getInventory().setBoots(null);
		}
		e.setJoinMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA
				+ "TheCatchGame" + ChatColor.DARK_AQUA + "] "
				+ ChatColor.DARK_RED + e.getPlayer().getName() + ChatColor.RED
				+ " has joined. He is the Catcher now!");
		Player p = e.getPlayer();
		s = p.getName();
		PlayerInventory plin = p.getInventory();
		plin.clear();
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta hm = (LeatherArmorMeta) helmet.getItemMeta();
		hm.setColor(Color.RED);
		LeatherArmorMeta cm = (LeatherArmorMeta) chestplate.getItemMeta();
		cm.setColor(Color.RED);
		LeatherArmorMeta pm = (LeatherArmorMeta) pants.getItemMeta();
		pm.setColor(Color.RED);
		LeatherArmorMeta bm = (LeatherArmorMeta) boots.getItemMeta();
		bm.setColor(Color.RED);
		helmet.setItemMeta(hm);
		chestplate.setItemMeta(cm);
		pants.setItemMeta(pm);
		boots.setItemMeta(bm);
		p.getInventory().setHelmet(helmet);
		p.getInventory().setChestplate(chestplate);
		p.getInventory().setLeggings(pants);
		p.getInventory().setBoots(boots);
		m(p, ChatColor.DARK_RED + "You are the catcher!");
		Location l = p.getLocation();
		World w = p.getWorld();
		w.strikeLightningEffect(l);

		if (getConfig().contains("ranks." + p.getName())) {
			String rank = getConfig().getString("ranks." + p.getName());
			if (rank.equalsIgnoreCase("owner")) {
				String oldName = p.getName();
				String tn = "§b§l" + oldName + "§r";
				p.setPlayerListName(tn);
				p.setDisplayName(p.getPlayerListName());
			} else if (rank.equalsIgnoreCase("admin")) {
				String oldName = p.getName();
				String tn = "§d§l" + oldName + "§r";
				p.setPlayerListName(tn);
				p.setDisplayName(p.getPlayerListName());
			} else if (rank.equalsIgnoreCase("vip")) {
				String oldName = p.getName();
				String tn = "§3" + oldName + "§r";
				p.setPlayerListName(tn);
				p.setDisplayName(p.getPlayerListName());
			} else if (rank.equalsIgnoreCase("first")) {
				String oldName = p.getName();
				String tn = "§9§o" + oldName + "§r";
				p.setPlayerListName(tn);
				p.setDisplayName(p.getPlayerListName());
			} else {
				String oldName = p.getName();
				String tn = "§e" + oldName + "§r";
				p.setPlayerListName(tn);
				p.setDisplayName(p.getPlayerListName());
			}
		} else {
			String oldName = p.getName();
			String tn = "§e" + oldName.replaceAll("§b§l", "").replaceAll("§d§l", "")
					.replaceAll("§3", "") + "§r";
			p.setPlayerListName(tn);
			p.setDisplayName(tn);
		}
	}

	@EventHandler
	public void ondead(PlayerDeathEvent e) {
		e.setDeathMessage(null);
		e.setDeathMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA
				+ "TheCatchGame" + ChatColor.DARK_AQUA + "] "
				+ ChatColor.DARK_RED + e.getEntity().getName() + ChatColor.GOLD
				+ " dead and will be the " + ChatColor.GREEN + "Catcher"
				+ ChatColor.GOLD + ".");
		e.getDrops().clear();
		getConfig().set("CatchPoints." + e.getEntity().getName(), getConfig().getInt("CatchPoints." + e.getEntity().getName()) - 5);
		if(s == e.getEntity().getName()){
			getConfig().set("CatchPoints." + e.getEntity().getName(), getConfig().getInt("CatchPoints." + e.getEntity().getName()) - 5);
			m(e.getEntity(), ChatColor.RED + "Your lose more 5 CatchPoints for dying while you are the catcher.");
		}
		m(e.getEntity(), ChatColor.RED + "Your lose 5 CatchPoints for dying.");
		saveConfig();
	}

	@EventHandler
	public void onEDBE(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player catcher = (Player) e.getDamager();
			Player tCatch = (Player) e.getEntity();
			if (!delay.contains(catcher)) {
				if (s == catcher.getName()) {
					if (getConfig()
							.contains("CatchPoints." + catcher.getName())) {
						int u = getConfig().getInt(
								"CatchPoints." + catcher.getName());
						u++;
						getConfig().set("CatchPoints." + catcher.getName(), u);
						saveConfig();
					} else {
						getConfig().set("CatchPoints." + catcher.getName(), 1);
						saveConfig();
					}
					m(catcher, ChatColor.GREEN + "You gained " + ChatColor.AQUA
							+ "1" + ChatColor.GREEN
							+ " CatchPoints for catching " + ChatColor.RED
							+ tCatch.getName() + ChatColor.GREEN + ".");
					m(catcher,
							ChatColor.GREEN
									+ "You have now "
									+ ChatColor.AQUA
									+ getConfig().getInt(
											"CatchPoints." + catcher.getName())
									+ ChatColor.GREEN + " CatchPoints.");
					for (Player ol : getServer().getOnlinePlayers()) {
						ol.getInventory().setHelmet(null);
						ol.getInventory().setChestplate(null);
						ol.getInventory().setLeggings(null);
						ol.getInventory().setBoots(null);
					}
					s = tCatch.getName();
					ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
					ItemStack chestplate = new ItemStack(
							Material.LEATHER_CHESTPLATE);
					ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS);
					ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
					LeatherArmorMeta hm = (LeatherArmorMeta) helmet
							.getItemMeta();
					hm.setColor(Color.RED);
					LeatherArmorMeta cm = (LeatherArmorMeta) chestplate
							.getItemMeta();
					cm.setColor(Color.RED);
					LeatherArmorMeta pm = (LeatherArmorMeta) pants
							.getItemMeta();
					pm.setColor(Color.RED);
					LeatherArmorMeta bm = (LeatherArmorMeta) boots
							.getItemMeta();
					bm.setColor(Color.RED);
					helmet.setItemMeta(hm);
					chestplate.setItemMeta(cm);
					pants.setItemMeta(pm);
					boots.setItemMeta(bm);
					tCatch.getInventory().setHelmet(helmet);
					tCatch.getInventory().setChestplate(chestplate);
					tCatch.getInventory().setLeggings(pants);
					tCatch.getInventory().setBoots(boots);
					m(tCatch, ChatColor.DARK_RED + "You are the catcher!");
					Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "["
							+ ChatColor.AQUA + "TheCatchGame"
							+ ChatColor.DARK_AQUA + "] " + ChatColor.DARK_RED
							+ tCatch.getName() + ChatColor.RED
							+ " is the Catcher!");
					Location l = tCatch.getLocation();
					World w = tCatch.getWorld();
					w.strikeLightningEffect(l);
					delay1(tCatch);
					e.setDamage(0);
				} else {
					e.setCancelled(true);
				}
			} else {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onmove(PlayerMoveEvent e) {
		e.getPlayer().setFoodLevel(15);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		for (Player ol : getServer().getOnlinePlayers()) {
			ol.getInventory().setHelmet(null);
			ol.getInventory().setChestplate(null);
			ol.getInventory().setLeggings(null);
			ol.getInventory().setBoots(null);
		}
		Player p = e.getPlayer();
		s = p.getName();
		PlayerInventory plin = p.getInventory();
		plin.clear();
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta hm = (LeatherArmorMeta) helmet.getItemMeta();
		hm.setColor(Color.RED);
		LeatherArmorMeta cm = (LeatherArmorMeta) chestplate.getItemMeta();
		cm.setColor(Color.RED);
		LeatherArmorMeta pm = (LeatherArmorMeta) pants.getItemMeta();
		pm.setColor(Color.RED);
		LeatherArmorMeta bm = (LeatherArmorMeta) boots.getItemMeta();
		bm.setColor(Color.RED);
		helmet.setItemMeta(hm);
		chestplate.setItemMeta(cm);
		pants.setItemMeta(pm);
		boots.setItemMeta(bm);
		p.getInventory().setHelmet(helmet);
		p.getInventory().setChestplate(chestplate);
		p.getInventory().setLeggings(pants);
		p.getInventory().setBoots(boots);
		Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA
				+ "TheCatchGame" + ChatColor.DARK_AQUA + "] "
				+ ChatColor.DARK_RED + p.getName() + ChatColor.RED
				+ " is the Catcher" /*
									 * + ChatColor.DARK_AQUA +
									 * " becouse he dead"
									 */
				+ ChatColor.RED + "!");
		m(p, ChatColor.DARK_RED + "You are the catcher!");
		World w = p.getWorld();
		lightning(w, p);
	}

	@EventHandler
	public void onSign(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[jump]")) {
			if (Integer.parseInt(e.getLine(1)) > 0) {
				e.setLine(2, "§3§lgo up!");
			} else {
				e.setLine(2, "§3§lgo down!");
			}
			e.setLine(0, "");
			e.setLine(3, "§c§l" + e.getLine(1));
			e.setLine(1, "§3§lClick here to");
			m(e.getPlayer(), ChatColor.GREEN + "Jump sign created!");
		} else if (e.getLine(0).equalsIgnoreCase("[heal]")) {
			e.setLine(0, "");
			e.setLine(1, "§a§lClick here to");
			e.setLine(2, "§a§lmax your");
			e.setLine(3, "§a§lhealth!");
			m(e.getPlayer(), ChatColor.GREEN + "Heal sign created!");
		} else if (e.getLine(0).equalsIgnoreCase("[speed]")) {
			e.setLine(0, "§e§lClick here");
			if (e.getLine(1).isEmpty()) {
				e.setLine(3, "§9§lfree!");
			} else {
				e.setLine(3, "§c§l" + e.getLine(1) + "§a§l CP!");
			}
			e.setLine(1, "§e§lto get");
			e.setLine(2, "§e§lspeed for");
			m(e.getPlayer(), ChatColor.GREEN + "Speed sign created!");
		} else if (e.getLine(0).equalsIgnoreCase("[shop]")) {
			if (!e.getLine(2).isEmpty()) {
				e.setLine(0, "§b" + e.getLine(2) + " §a§lCP");
				if (e.getLine(1).equalsIgnoreCase("speed")) {
					e.setLine(1, "§d§oClick here");
					e.setLine(2, "§d§oto get");
					e.setLine(3, "§5§ospeed");
				} else if (e.getLine(1).equalsIgnoreCase("jumper")) {
					e.setLine(1, "§d§oClick here");
					e.setLine(2, "§d§oto get");
					e.setLine(3, "§5§ojumper");
				} else if (e.getLine(1).equalsIgnoreCase("heal")) {
					e.setLine(1, "§d§oClick here");
					e.setLine(2, "§d§oto get");
					e.setLine(3, "§5§oheal");
				} else {
					m(e.getPlayer(), ChatColor.RED
							+ "Can't create shop without reward!");
					e.setCancelled(true);
				}
			} else {
				if (!e.getLine(1).equalsIgnoreCase("jumper")) {
					e.setCancelled(true);
					m(e.getPlayer(), ChatColor.RED
							+ "Can't create shop without price!");
				} else {
					e.setLine(0, "");
					e.setLine(1, "§d§oClick here");
					e.setLine(2, "§d§oto get");
					e.setLine(3, "§5§ojumper");
				}
			}
		}
	}

	@EventHandler
	public void onbreak(BlockBreakEvent e) {
		if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			/*
			 * e.setCanclled(true); final Block b = e.getBlock(); final Material
			 * m = b.getType(); b.setType(Material.BEDROCK);
			 * getServer().getScheduler().scheduleSyncDelayedTask(this, new
			 * Runnable() {
			 * 
			 * @Override public void run() { b.setType(m); } }, 20L * 2);
			 */
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onplace(BlockPlaceEvent e) {
		final Block b = e.getBlock();
		Block br = b.getLocation().add(0, 0, 1).getBlock();
		Block bl = b.getLocation().add(1, 0, 0).getBlock();
		Block bf = b.getLocation().add(0, 0, -1).getBlock();
		Block bb = b.getLocation().add(-1, 0, 0).getBlock();
		if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			if (b.getType().equals(Material.GLOWSTONE)
					&& (br.getType().equals(Material.AIR) || br.getType()
							.equals(Material.GLASS))
					&& (bl.getType().equals(Material.AIR) || bl.getType()
							.equals(Material.GLASS))
					&& (bf.getType().equals(Material.AIR) || bf.getType()
							.equals(Material.GLASS))
					&& (bb.getType().equals(Material.AIR) || bb.getType()
							.equals(Material.GLASS))) {
				b.setType(Material.GLASS);
				getServer().getScheduler().scheduleSyncDelayedTask(this,
						new Runnable() {
							@Override
							public void run() {
								b.setType(Material.AIR);
							}
						}, 20L * 2);
			} else {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void invclick(InventoryClickEvent e) {
		if (e.getSlotType().equals(SlotType.ARMOR)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (buying.contains(e.getPlayer())) {
			int amount = 0;
			if (e.getMessage().matches("[0-9]+")) {
				amount = Integer.parseInt(e.getMessage());
				Player p = e.getPlayer();
				int cp = getConfig().getInt("CatchPoints." + p.getName());
				if ((!(amount > 2304)) || (!(amount >= 0))) {
					if (cp - amount >= 0) {
						getConfig().set("CatchPoints." + p.getName(),
								cp - amount);
						p.sendMessage(ChatColor.DARK_AQUA + "["
								+ ChatColor.GREEN + "CatchShop"
								+ ChatColor.DARK_AQUA + "] " + ChatColor.RESET
								+ "" + ChatColor.GOLD + "You get "
								+ ChatColor.LIGHT_PURPLE + amount
								+ ChatColor.GOLD + " jumpers!");
						ItemStack jumper = new ItemStack(Material.GLOWSTONE,
								amount);
						ItemMeta im = jumper.getItemMeta();
						im.setDisplayName("§6Jumper");
						char br = '"';
						ArrayList<String> lore = new ArrayList<String>();
						lore.add("§aThe §bJumper §aAllow you to §9" + br
								+ "fly" + br + " §ain the game.");
						lore.add("§aJust place it on the ground and jump on it!");
						im.setLore(lore);
						jumper.setItemMeta(im);
						p.getInventory().addItem(jumper);
						buying.remove(p);
						e.setCancelled(true);
					} else {
						p.sendMessage(ChatColor.DARK_AQUA + "["
								+ ChatColor.GREEN + "CatchShop"
								+ ChatColor.DARK_AQUA + "] " + ChatColor.RED
								+ "You don't have enough " + ChatColor.GREEN
								+ "CP" + ChatColor.RED + " to buy "
								+ ChatColor.LIGHT_PURPLE + amount
								+ ChatColor.RED + " jumpers!");
						e.setCancelled(true);
					}
				} else {
					p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.GREEN
							+ "CatchShop" + ChatColor.DARK_AQUA + "] "
							+ ChatColor.RED
							+ "You can't buy more then 2304 or less then 0 jumpers!");
					e.setCancelled(true);
				}
			} else if (e.getMessage().equalsIgnoreCase("cancel")) {
				if (buying.contains(e.getPlayer())) {
					buying.remove(e.getPlayer());
					e.getPlayer().sendMessage(
							ChatColor.DARK_AQUA + "[" + ChatColor.GREEN
									+ "CatchShop" + ChatColor.DARK_AQUA + "] "
									+ ChatColor.AQUA + "OK, see you later...");
					e.setCancelled(true);
				}
			} else {
				e.getPlayer().sendMessage(
						ChatColor.DARK_AQUA + "[" + ChatColor.GREEN
								+ "CatchShop" + ChatColor.DARK_AQUA + "] "
								+ ChatColor.YELLOW + e.getMessage()
								+ ChatColor.RED + " is not a vaild number!");
				e.setCancelled(true);
			}
		} else {
			if (s == e.getPlayer().getName()) {
				if (e.isCancelled() == false) {
					e.setCancelled(true);
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "["
							+ ChatColor.RED + "Catcher" + ChatColor.DARK_RED
							+ "] " + ChatColor.RESET + e.getPlayer().getDisplayName()
							+ ": " + e.getMessage());
				}
			} else {
				e.setCancelled(true);
				Bukkit.broadcastMessage(e.getPlayer().getDisplayName() + ": "
						+ e.getMessage());
			}
		}
	}

	@EventHandler
	public void onquit(PlayerQuitEvent e){
		getConfig().set("CatchPoints." + e.getPlayer().getName(), getConfig().getInt("CatchPoints." + e.getPlayer().getName()) - 5);
		if(s == e.getPlayer().getName()){
			getConfig().set("CatchPoints." + e.getPlayer().getName(), getConfig().getInt("CatchPoints." + e.getPlayer().getName()) - 5);
		}
		saveConfig();
	}
	
	@EventHandler
	public void onclick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType().equals(Material.WALL_SIGN)
					|| e.getClickedBlock().getType().equals(Material.SIGN)
					|| e.getClickedBlock().getType().equals(Material.SIGN_POST)) {
				Sign s = (Sign) e.getClickedBlock().getState();
				if (s.getLine(2).startsWith("§3§l")) {
					int u = Integer.parseInt(s.getLine(3)
							.replaceAll("§c§l", ""));
					e.getPlayer().teleport(
							e.getPlayer().getLocation().add(0, u, 0));
					if (u > 0) {
						m(e.getPlayer(), ChatColor.BLUE + "" + ChatColor.BOLD
								+ "Telepoted up!");
					} else {
						m(e.getPlayer(), ChatColor.BLUE + "" + ChatColor.BOLD
								+ "Telepoted down!");
					}
					e.setCancelled(true);
					e.getPlayer().openInventory(e.getPlayer().getInventory());
					e.getPlayer().closeInventory();
				} else if (s.getLine(1).startsWith("§a§l")) {
					e.getPlayer().setHealth(20);
					e.getPlayer().setFireTicks(0);
					m(e.getPlayer(), ChatColor.AQUA + "You have been healed!");
				} else if (s.getLine(1).startsWith("§e§l")) {
					if (!e.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
						if (!s.getLine(3).equalsIgnoreCase("§9§lfree!")) {
							if (!e.getPlayer().hasPotionEffect(
									PotionEffectType.SPEED)) {
								int c = Integer.parseInt(s.getLine(3)
										.replaceAll("§c§l", "")
										.replaceAll("§a§l CP!", "")
										.replaceAll(" ", ""));
								if (getConfig().getInt(
										"CatchPoints."
												+ e.getPlayer().getName()) >= c) {
									e.getPlayer().removePotionEffect(
											PotionEffectType.SPEED);
									e.getPlayer().addPotionEffect(
											new PotionEffect(
													PotionEffectType.SPEED,
													5000, 0));
									m(e.getPlayer(), ChatColor.BLUE
											+ "The Magic speeded you...");
									getConfig().set(
											"CatchPoints."
													+ e.getPlayer().getName(),
											getConfig().getInt(
													"CatchPoints."
															+ e.getPlayer()
																	.getName())
													- c);
									e.getPlayer()
											.sendMessage(
													ChatColor.DARK_AQUA
															+ "["
															+ ChatColor.DARK_GREEN
															+ "CathcPoints"
															+ ChatColor.DARK_AQUA
															+ "] "
															+ ChatColor.GREEN
															+ "You have now "
															+ ChatColor.RED
															+ getConfig()
																	.getInt("CatchPoints."
																			+ e.getPlayer()
																					.getName())
															+ ChatColor.GREEN
															+ " CatchPoints.");
								} else {
									e.getPlayer()
											.sendMessage(
													ChatColor.DARK_AQUA
															+ "["
															+ ChatColor.DARK_GREEN
															+ "CathcPoints"
															+ ChatColor.DARK_AQUA
															+ "] "
															+ ChatColor.RED
															+ "You don't have enough CatchPoints to get speed.");
								}
							} else {
								m(e.getPlayer(), ChatColor.RED
										+ "You alredy have speedboost!");
							}
						} else {
							if (!e.getPlayer().hasPotionEffect(
									PotionEffectType.SPEED)) {
								e.getPlayer()
										.addPotionEffect(
												new PotionEffect(
														PotionEffectType.SPEED,
														5000, 0));
								m(e.getPlayer(), ChatColor.BLUE
										+ "The Magic speeded you...");
							} else {
								m(e.getPlayer(), ChatColor.RED
										+ "You alredy have speedboost!");
							}
						}
					} else {
						m(e.getPlayer(), ChatColor.RED
								+ "You alredy have speedboost!");
					}
				} else if (s.getLine(1).startsWith("§d§o")) {
					if (s.getLine(3).contains("speed")) {
						int cost = Integer
								.parseInt(s.getLine(0)
										.replaceAll(" §a§lCP", "")
										.replaceAll("§b", ""));
						Player p = e.getPlayer();
						int cp = getConfig().getInt(
								"CatchPoints." + p.getName());
						if (cp >= cost) {
							for (PotionEffect pe : p.getActivePotionEffects()) {
								if (pe.getType().equals(PotionEffectType.SPEED)) {
									p.removePotionEffect(pe.getType());
								}
							}
							getConfig().set("CatchPoints." + p.getName(),
									cp - cost);
							m(p, ChatColor.BLUE
									+ "You get speed boost for 20 minutes!");
							p.addPotionEffect(new PotionEffect(
									PotionEffectType.SPEED, 120000, 1));
						}
					} else if (s.getLine(3).contains("heal")) {
						int cost = Integer
								.parseInt(s.getLine(0)
										.replaceAll(" §a§lCP", "")
										.replaceAll("§b", ""));
						Player p = e.getPlayer();
						int cp = getConfig().getInt(
								"CatchPoints." + p.getName());
						if (cp >= cost) {
							for (PotionEffect pe : p.getActivePotionEffects()) {
								if (pe.getType().equals(
										PotionEffectType.REGENERATION)) {
									p.removePotionEffect(pe.getType());
								}
							}
							getConfig().set("CatchPoints." + p.getName(),
									cp - cost);
							m(p, ChatColor.BLUE
									+ "You get regeneration for 20 minutes!");
							p.addPotionEffect(new PotionEffect(
									PotionEffectType.REGENERATION, 120000, 1));
						}
					} else if (s.getLine(3).contains("jumper")) {
						if (!buying.contains(e.getPlayer())) {
							buying.add(e.getPlayer());
							e.getPlayer()
									.sendMessage(
											ChatColor.DARK_AQUA
													+ "["
													+ ChatColor.GREEN
													+ "CatchShop"
													+ ChatColor.DARK_AQUA
													+ "] "
													+ ChatColor.GREEN
													+ "How many jumper do you want to buy?");
						}
					}
				}
			} else {
				if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
					if (e.getClickedBlock().getType().equals(Material.WOOD_DOOR)) {
						e.setCancelled(false);
					}
				}
			}
		}
	}

	public void lightning(final World w, final Player l) {
		getServer().getScheduler().scheduleSyncDelayedTask(this,
				new Runnable() {
					@Override
					public void run() {
						w.strikeLightningEffect(l.getLocation());
					}
				}, 5L);
	}

	public void delay1(final Player p) {
		Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA
				+ "TheCatchGame" + ChatColor.DARK_AQUA + "] " + ChatColor.GREEN
				+ "5 seconds to run!!!");
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 500, 5));
		delay.add(p);
		getServer().getScheduler().scheduleSyncDelayedTask(this,
				new Runnable() {
					@Override
					public void run() {
						delay.remove(p);
						p.removePotionEffect(PotionEffectType.SLOW);
						Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "["
								+ ChatColor.AQUA + "TheCatchGame"
								+ ChatColor.DARK_AQUA + "] "
								+ ChatColor.DARK_RED + p.getName()
								+ ChatColor.RED + " is now allowed to catch.");
					}
				}, 20L * 5);
	}

	@EventHandler
	public void onlog(PlayerLoginEvent e){
		if(getConfig().getString("ranks." + e.getPlayer().getName()).equalsIgnoreCase("admin")){
			e.setResult(Result.ALLOWED);
		} else if(getConfig().getString("ranks." + e.getPlayer().getName()).equalsIgnoreCase("owner")){
			e.setResult(Result.ALLOWED);
		} else if(getConfig().getString("ranks." + e.getPlayer().getName()).equalsIgnoreCase("vip")){
			e.setResult(Result.ALLOWED);
		}
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		if (commandLabel.equalsIgnoreCase("catchpoints")
				|| commandLabel.equalsIgnoreCase("cp")) {
			p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.DARK_GREEN
					+ "CathcPoints" + ChatColor.DARK_AQUA + "] "
					+ ChatColor.GREEN + "You have " + ChatColor.RED
					+ getConfig().getInt("CatchPoints." + p.getName())
					+ ChatColor.GREEN + " CatchPoints.");
		} else if (commandLabel.equalsIgnoreCase("catchpointsset")
				|| commandLabel.equalsIgnoreCase("cpset")) {
			if (p.isOp()) {
				if (args.length == 1) {
					if (args[0].matches("[0-9]+")) {
						int tcp = Integer.parseInt(args[0]);
						getConfig().set("CatchPoints." + p.getName(), tcp);
						p.sendMessage(ChatColor.DARK_AQUA + "["
								+ ChatColor.DARK_GREEN + "CathcPoints"
								+ ChatColor.DARK_AQUA + "] " + ChatColor.GREEN
								+ "You have now " + ChatColor.LIGHT_PURPLE
								+ tcp + ChatColor.GREEN + " CP!");
						saveConfig();
					} else {
						p.sendMessage(ChatColor.DARK_AQUA + "["
								+ ChatColor.DARK_GREEN + "CathcPoints"
								+ ChatColor.DARK_AQUA + "] " + ChatColor.YELLOW
								+ args[0] + ChatColor.RED
								+ " is not a vaild number!");
					}
				} else {
					p.sendMessage(ChatColor.DARK_AQUA + "["
							+ ChatColor.DARK_GREEN + "CathcPoints"
							+ ChatColor.DARK_AQUA + "] " + ChatColor.RED
							+ "Too few arguments!");
				}
			} else {
				p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.DARK_GREEN
						+ "CathcPoints" + ChatColor.DARK_AQUA + "] "
						+ ChatColor.RED
						+ "You don't have the permission to do that!");
			}
		}
		return false;
	}

	public void m(Player p, String me) {
		p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA
				+ "TheCatchGame" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET
				+ me);
	}
}
