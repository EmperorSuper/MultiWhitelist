package me.EmperorSuper.MultiWhitelist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class MainClass extends JavaPlugin implements Listener,CommandExecutor,TabCompleter {
	public void onEnable() {
		loadConfigManager();
		if (Yamls.Data.getData().getList("ActiveLists") == null) {
			ArrayList<String> List = new ArrayList<String>();
			Yamls.Data.getData().set("ActiveLists", List);
			Yamls.Data.saveData();
		}
		if (Yamls.Data.getData().getList("Lists") == null) {
			ArrayList<String> List = new ArrayList<String>();
			Yamls.Data.getData().set("Lists", List);
			Yamls.Data.saveData();
			permissions();
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		if (this.getConfig().getString("KickMessage") == null) {
			this.getConfig().set("KickMessage", "&cYou are not whitelisted");
			this.saveConfig();
		}
	}
	
	
	public void onDisable() {
		
		
	}
	
	public void loadConfigManager(){
		Yamls.Data = new Yamls();
		Yamls.Data.setup();
		Yamls.Data.saveData();
		Yamls.Data.reloadData();
	}
	
	String version = this.getDescription().getVersion();
	
	public String prefix = ChatColor.translateAlternateColorCodes('&', "&2&l[&3MW&2&l]&r");
	public void helpmessage(CommandSender player) {
		player.sendMessage(ChatColor.DARK_GREEN + "-------=======MultiWhitelist Help=======-------");
		player.sendMessage(ChatColor.DARK_GREEN + "/MultiWhitelist " + ChatColor.DARK_AQUA + "CreateList " + ChatColor.YELLOW + "<ListName> " + ChatColor.GRAY + "-" + ChatColor.BLUE + " Create a new whitelist.");
		player.sendMessage(ChatColor.DARK_GREEN + "/MultiWhitelist " + ChatColor.DARK_AQUA + "DeleteeList " + ChatColor.YELLOW + "<ListName> " + ChatColor.GRAY + "-" + ChatColor.BLUE + " Delete a whitelist.");
		player.sendMessage(ChatColor.DARK_GREEN + "/MultiWhitelist " + ChatColor.DARK_AQUA + "Add " + ChatColor.YELLOW + "<ListName> <PlayerName> " + ChatColor.GRAY + "-" + ChatColor.BLUE + " Add a player to a whitelist.");
		player.sendMessage(ChatColor.DARK_GREEN + "/MultiWhitelist " + ChatColor.DARK_AQUA + "Remove " + ChatColor.YELLOW + "<ListName> <PlayerName> " + ChatColor.GRAY + "-" + ChatColor.BLUE + " Remove a player from a whitelist.");
		player.sendMessage(ChatColor.DARK_GREEN + "/MultiWhitelist " + ChatColor.DARK_AQUA + "Activate " + ChatColor.YELLOW + "<ListName> " + ChatColor.GRAY + "-" + ChatColor.BLUE + " Activate a whitelist.");
		player.sendMessage(ChatColor.DARK_GREEN + "/MultiWhitelist " + ChatColor.DARK_AQUA + "DeActivate " + ChatColor.YELLOW + "<ListName> " + ChatColor.GRAY + "-" + ChatColor.BLUE + " DeActivate a whitelist.");
		player.sendMessage(ChatColor.DARK_GREEN + "/MultiWhitelist " + ChatColor.DARK_AQUA + "Whitelists " + ChatColor.GRAY + "-" + ChatColor.BLUE + " Shows a list of all the whitelists.");
		player.sendMessage(ChatColor.DARK_GREEN + "/MultiWhitelist " + ChatColor.DARK_AQUA + "ActivatedLists " + ChatColor.GRAY + "-" + ChatColor.BLUE + " Shows a list of all the activated whitelists.");
		player.sendMessage(ChatColor.DARK_GREEN + "/MultiWhitelist " + ChatColor.DARK_AQUA + "Clear " + ChatColor.YELLOW + "<ListName> " + ChatColor.GRAY + "-" + ChatColor.BLUE + " Clear a whitelist.");
		player.sendMessage(ChatColor.DARK_GREEN + "/MultiWhitelist " + ChatColor.DARK_AQUA + "Reload " + ChatColor.GRAY + "-" + ChatColor.BLUE + " Reloads the config and data files.");
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Version: " + version);
	}
	
	public static String ActiveListslist() {
		StringBuilder x = new StringBuilder();
		for(int i=0; i < Yamls.Data.getData().getList("ActiveLists").size();){
			x.append(Yamls.Data.getData().getList("ActiveLists").get(i).toString() + ChatColor.BLUE + ", " + ChatColor.YELLOW);
			i = i + 1;
		}
		String y = x.toString();
		return y.substring(0, y.length() -4);
	}
	
	public static String Listslist() {
		StringBuilder x = new StringBuilder();
		for(int i=0; i < Yamls.Data.getData().getList("Lists").size();){
			x.append(Yamls.Data.getData().getList("Lists").get(i).toString() + ChatColor.BLUE + ", " + ChatColor.YELLOW);
			i = i + 1;
		}
		String y = x.toString();
		return y.substring(0, y.length() -4);
	}
	
	void permissions() {
		for(int i=0; i < Yamls.Data.getData().getList("Lists").size();i++){
			String perm = "MultiWhitelist." + Yamls.Data.getData().getList("Lists").get(i) + ".bypass";
			Permission permission = new Permission(perm);
			this.getServer().getPluginManager().addPermission(permission);
		}
		Permission BypassPermission = new Permission(Bypass);
		Permission CMDPermission = new Permission(Command);
		this.getServer().getPluginManager().addPermission(BypassPermission);
		this.getServer().getPluginManager().addPermission(CMDPermission);
	}
	
	public String Command = "MultiWhitelist.Manage";
	public String Bypass = "MultiWhitelist.bypass";
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("MultiWhitelist")) {
			if (sender.hasPermission(Command)) {
				if (!(args.length < 1)) {
					if (args[0].equalsIgnoreCase("reload")) {
						this.reloadConfig();
						Yamls.Data.reloadData();
						if (Yamls.Data.getData().getList("ActiveLists") == null) {
							ArrayList<String> List = new ArrayList<String>();
							Yamls.Data.getData().set("ActiveLists", List);
							Yamls.Data.saveData();
							permissions();
						}
						sender.sendMessage(prefix + ChatColor.BLUE + "Plugin data and config reloaded");
					}
					
					
					if (args[0].equalsIgnoreCase("CreateList")) {
						if (args.length < 2) {
							sender.sendMessage(prefix + ChatColor.RED + "/MultiWhitelist CreateList <WhitelistName>");
						} else {
							if (!(Yamls.Data.getData().getList("Lists") == null)) {
								if (Yamls.Data.getData().getList("Lists").contains(args[1].toLowerCase())) {
									sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " already exists");
								} else {
									ArrayList<String> List = (ArrayList<String>)Yamls.Data.getData().getList("Lists");
									List.add(args[1].toLowerCase());
									Yamls.Data.getData().set("Lists", List);
									Yamls.Data.saveData();
									sender.sendMessage(prefix + ChatColor.BLUE + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.BLUE + " has been created");
								}
							} else {
								ArrayList<String> List = new ArrayList<String>();
								List.add(args[1].toLowerCase());
								Yamls.Data.getData().set("Lists", List);
								Yamls.Data.saveData();
								sender.sendMessage(prefix + ChatColor.BLUE + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.BLUE + " has been created");
							}
						}
					}
					
					if (args[0].equalsIgnoreCase("add")) {
						if (args.length < 3) {
							sender.sendMessage(prefix + ChatColor.RED + "/MultiWhitelist Add <WhitelistName> <PlayerName>");
						} else {
							if (Yamls.Data.getData().getList("Lists").contains(args[1].toLowerCase())) {
								if (!(Yamls.Data.getData().getList(args[1].toLowerCase()) == null)) {
									if (Yamls.Data.getData().getList(args[1].toLowerCase()).contains(args[2].toLowerCase())) {
										sender.sendMessage(prefix + ChatColor.YELLOW + args[2] + ChatColor.RED + " is already on the whitelist " + ChatColor.YELLOW + args[1]);
									} else {
										ArrayList<String> List = (ArrayList<String>)Yamls.Data.getData().getList(args[1].toLowerCase());
										List.add(args[2].toLowerCase());
										Yamls.Data.getData().set(args[1].toLowerCase(), List);
										Yamls.Data.saveData();
										sender.sendMessage(prefix + ChatColor.YELLOW + args[2] + ChatColor.BLUE + " has been added to the whitelist " + ChatColor.YELLOW + args[1]);
									}
								} else {
									ArrayList<String> List = new ArrayList<String>();
									List.add(args[2].toLowerCase());
									Yamls.Data.getData().set(args[1].toLowerCase(), List);
									Yamls.Data.saveData();
									sender.sendMessage(prefix + ChatColor.YELLOW + args[2] + ChatColor.BLUE + " has been added to the whitelist " + ChatColor.YELLOW + args[1]);
								}
							} else {
								sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
							}
						}
					}
					
					
					
					if (args[0].equalsIgnoreCase("remove")) {
						if (args.length < 3) {
							sender.sendMessage(prefix + ChatColor.RED + "/MultiWhitelist Remove <WhitelistName> <PlayerName>");
						} else {
							if (!(Yamls.Data.getData().getList("Lists") == null)) {
								if (Yamls.Data.getData().getList("Lists").contains(args[1].toLowerCase())) {
									if (!(Yamls.Data.getData().getList(args[1].toLowerCase()) == null)) {
										if (!Yamls.Data.getData().getList(args[1].toLowerCase()).contains(args[2].toLowerCase())) {
											sender.sendMessage(prefix + ChatColor.YELLOW + args[2] + ChatColor.RED + " is not on the whitelist " + ChatColor.YELLOW + args[1]);
										} else {
											ArrayList<String> List = (ArrayList<String>)Yamls.Data.getData().getList(args[1].toLowerCase());
											List.remove(args[2].toLowerCase());
											Yamls.Data.getData().set(args[1].toLowerCase(), List);
											Yamls.Data.saveData();
											sender.sendMessage(prefix + ChatColor.YELLOW + args[2] + ChatColor.BLUE + " has been removed from the whitelist " + ChatColor.YELLOW + args[1]);
										}
									} else {
										sender.sendMessage(prefix + ChatColor.YELLOW + args[2] + ChatColor.RED + " is not on the whitelist " + ChatColor.YELLOW + args[1]);
									}
								} else {
									sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
								}
							} else {
								sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
							}
						}
					}
					
					
					if (args[0].equalsIgnoreCase("DeleteList")) {
						if (args.length < 2) {
							sender.sendMessage(prefix + ChatColor.RED + "/MultiWhitelist DeleteList <Whitelist>");
						} else {
							if (!(Yamls.Data.getData().getList("Lists") == null)) {
								if (Yamls.Data.getData().getList("Lists").contains(args[1].toLowerCase())) {
									Yamls.Data.getData().set(args[1].toLowerCase(), null);
									ArrayList<String> List = (ArrayList<String>)Yamls.Data.getData().getList("Lists");
									List.remove(args[1].toLowerCase());
									Yamls.Data.getData().set("Lists", List);
									Yamls.Data.saveData();
									if (Yamls.Data.getData().getList("ActiveLists").contains(args[1].toLowerCase())) {
										ArrayList<String> List1 = (ArrayList<String>)Yamls.Data.getData().getList("ActiveLists");
										List1.remove(args[1].toLowerCase());
										Yamls.Data.getData().set("ActiveLists", List1);
										Yamls.Data.saveData();
										sender.sendMessage(prefix + ChatColor.BLUE + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.BLUE + " has been deleted and deactivated");
									} else {
										sender.sendMessage(prefix + ChatColor.BLUE + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.BLUE + " has been deleted");
									}
								} else {
									sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
								}
							} else {
								sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
							}
						}
					}
					
					
					
					if (args[0].equalsIgnoreCase("Activate")) {
						if (args.length < 2) {
							sender.sendMessage(prefix + ChatColor.RED + "/MultiWhitelist Activate <Whitelist>");
						} else {
							if (Yamls.Data.getData().getList("ActiveLists").contains(args[1].toLowerCase())) {
								sender.sendMessage(prefix + ChatColor.BLUE + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.BLUE + " is already activated");
							} else {
								if (!(Yamls.Data.getData().getList("Lists") == null)) {
									if (Yamls.Data.getData().getList("Lists").contains(args[1].toLowerCase())) {
										if (!(Yamls.Data.getData().getList("ActiveLists") == null)) {
											ArrayList<String> List = (ArrayList<String>)Yamls.Data.getData().getList("ActiveLists");
											List.add(args[1].toLowerCase());
											Yamls.Data.getData().set("ActiveLists", List);
											Yamls.Data.saveData();
											sender.sendMessage(prefix + ChatColor.BLUE + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.BLUE + " has been activated");
										} else {
											ArrayList<String> List = new ArrayList<String>();
											List.add(args[1].toLowerCase());
											Yamls.Data.getData().set("ActiveLists", List);
											Yamls.Data.saveData();
											sender.sendMessage(prefix + ChatColor.BLUE + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.BLUE + " has been activated");
										}
									} else {
										sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
									}
								} else {
									sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
								}
							}
						}
					}
					
					if (args[0].equalsIgnoreCase("DeActivate")) {
						if (args.length < 2) {
							sender.sendMessage(prefix + ChatColor.RED + "/MultiWhitelist DeActivate <Whitelist>");
						} else {
							if (Yamls.Data.getData().getList("ActiveLists") == null) {
								sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " is not activated");
							} else {
								if (!(Yamls.Data.getData().getList("Lists") == null)) {
									if (Yamls.Data.getData().getList("Lists").contains(args[1].toLowerCase())) {
										if (!(Yamls.Data.getData().getList("ActiveLists") == null)) {
											if (Yamls.Data.getData().getList("ActiveLists").contains(args[1].toLowerCase())) {
												ArrayList<String> List = (ArrayList<String>)Yamls.Data.getData().getList("ActiveLists");
												List.remove(args[1].toLowerCase());
												Yamls.Data.getData().set("ActiveLists", List);
												Yamls.Data.saveData();
												sender.sendMessage(prefix + ChatColor.BLUE + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.BLUE + " has been deactivated");
											} else {
												sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " is not activated");
											}
										} else {
											sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " is not activated");
										}
									} else {
										sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
									}
								} else {
									sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
								}
							}
						}
					}
					
					
					
					if (args[0].equalsIgnoreCase("ActivatedLists")) {
						if (Yamls.Data.getData().getList("ActiveLists") == null) {
							sender.sendMessage(prefix + ChatColor.RED + "There are no whitelists activated");
						} else {
							if (!(Yamls.Data.getData().getList("ActiveLists").size() < 1)) {
								sender.sendMessage(prefix + ChatColor.BLUE + "Activated whitelists: " +ChatColor.YELLOW + ActiveListslist());
							} else {
								sender.sendMessage(prefix + ChatColor.RED + "There are no whitelists activated");
							}
						}
					}
					
					
					
					if (args[0].equalsIgnoreCase("Whitelists")) {
						if (Yamls.Data.getData().getList("Lists") == null) {
							sender.sendMessage(prefix + ChatColor.RED + "There are no whitelists");
						} else {
							if (!(Yamls.Data.getData().getList("Lists").size() < 1)) {
								sender.sendMessage(prefix + ChatColor.BLUE + "Whitelists: " +ChatColor.YELLOW + Listslist());
							} else {
								sender.sendMessage(prefix + ChatColor.RED + "There are no whitelists");
							}
						}
					}
					
					
					if (args[0].equalsIgnoreCase("Clear")) {
						if (args.length < 2) {
							sender.sendMessage(prefix + ChatColor.RED + "/MultiWhitelist Clear <Whitelist>");
						} else {
							if (!(Yamls.Data.getData().getList("Lists") == null)) {
								if (Yamls.Data.getData().getList("Lists").contains(args[1].toLowerCase())) {
									Yamls.Data.getData().set(args[1].toLowerCase(), null);
									Yamls.Data.saveData();
									sender.sendMessage(prefix + ChatColor.BLUE + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.BLUE + " has been cleared");
								} else {
									sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
								}
							} else {
								sender.sendMessage(prefix + ChatColor.RED + "The whitelist " + ChatColor.YELLOW + args[1] + ChatColor.RED + " does not exist");
							}

						}
					}
					
					List<String> commands = new ArrayList<String>();
					commands.add("Add".toLowerCase());
					commands.add("Remove".toLowerCase());
					commands.add("CreateList".toLowerCase());
					commands.add("DeleteList".toLowerCase());
					commands.add("Activate".toLowerCase());
					commands.add("DeActivate".toLowerCase());
					commands.add("ActivatedLists".toLowerCase());
					commands.add("Reload".toLowerCase());
					commands.add("Whitelists".toLowerCase());
					commands.add("Clear".toLowerCase());
					
					if (!commands.contains(args[0].toLowerCase())) {
						helpmessage(sender);
					}
					
				} else {
					helpmessage(sender);
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Insufficient permissions");
			}
		}
		return false;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String alias, String[] args) {
		List<String> list = new ArrayList<>();
		if (args.length == 1) {
			if ("Add".toLowerCase().startsWith(args[0].toLowerCase())) {
				list.add("Add");
			}
			if ("Remove".toLowerCase().startsWith(args[0].toLowerCase())) {
				list.add("Remove");
			}
			if ("CreateList".toLowerCase().startsWith(args[0].toLowerCase())) {
				list.add("CreateList");
			}
			if ("DeleteList".toLowerCase().startsWith(args[0].toLowerCase())) {
				list.add("DeleteList");
			}
			if ("Activate".toLowerCase().startsWith(args[0].toLowerCase())) {
				list.add("Activate");
			}
			if ("DeActivate".toLowerCase().startsWith(args[0].toLowerCase())) {
				list.add("DeActivate");
			}
			if ("ActivatedLists".toLowerCase().startsWith(args[0].toLowerCase())) {
				list.add("ActivatedLists");
			}
			if ("Reload".toLowerCase().startsWith(args[0].toLowerCase())) {
				list.add("Reload");
			}
			if ("Whitelists".toLowerCase().startsWith(args[0].toLowerCase())) {
				list.add("Whitelists");
			}
			if ("Clear".toLowerCase().startsWith(args[0].toLowerCase())) {
				list.add("Clear");
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("DeleteList")) {
				ArrayList<String> lists = new ArrayList<>();
				lists.addAll((Collection<? extends String>) Yamls.Data.getData().getList("Lists"));
				for(String i : lists){
					if (i.toLowerCase().startsWith(args[1].toLowerCase())) {
						list.add(i);
					}
				}
			}
			if (args[0].equalsIgnoreCase("Add")) {
				ArrayList<String> lists = new ArrayList<>();
				lists.addAll((Collection<? extends String>) Yamls.Data.getData().getList("Lists"));
				for(String i : lists){
					if (i.toLowerCase().startsWith(args[1].toLowerCase())) {
						list.add(i);
					}
				}
			}
			if (args[0].equalsIgnoreCase("Remove")) {
				ArrayList<String> lists = new ArrayList<>();
				lists.addAll((Collection<? extends String>) Yamls.Data.getData().getList("Lists"));
				for(String i : lists){
					if (i.toLowerCase().startsWith(args[1].toLowerCase())) {
						list.add(i);
					}
				}
			}
			if (args[0].equalsIgnoreCase("Activate")) {
				ArrayList<String> lists = new ArrayList<>();
				lists.addAll((Collection<? extends String>) Yamls.Data.getData().getList("Lists"));
				for(String i : lists){
					if (i.toLowerCase().startsWith(args[1].toLowerCase())) {
						list.add(i);
					}
				}
			}
			if (args[0].equalsIgnoreCase("DeActivate")) {
				ArrayList<String> lists = new ArrayList<>();
				lists.addAll((Collection<? extends String>) Yamls.Data.getData().getList("ActiveLists"));
				for(String i : lists){
					if (i.toLowerCase().startsWith(args[1].toLowerCase())) {
						list.add(i);
					}
				}
			}
			if (args[0].equalsIgnoreCase("Clear")) {
				ArrayList<String> lists = new ArrayList<>();
				lists.addAll((Collection<? extends String>) Yamls.Data.getData().getList("Lists"));
				for(String i : lists){
					if (i.toLowerCase().startsWith(args[1].toLowerCase())) {
						list.add(i);
					}
				}
			}
		}
		
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("Add")) {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (player.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
						list.add(player.getName());
					}
				}
			}
			if (args[0].equalsIgnoreCase("Remove")) {
				if (!(Yamls.Data.getData().getStringList(args[1].toString().toLowerCase()) == null)) {
					if (!(Yamls.Data.getData().getStringList(args[1].toString().toLowerCase()).size() == 0)) {
						ArrayList<String> lists = new ArrayList<>();
						lists.addAll((Collection<? extends String>) Yamls.Data.getData().getList(args[1]));
						for(String i : lists){
							if (i.toLowerCase().startsWith(args[2].toLowerCase())) {
								list.add(i);
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player player = e.getPlayer();
		if (!(Yamls.Data.getData().getList("ActiveLists") == null)) {
			if (!(Yamls.Data.getData().getList("ActiveLists").size() == 0)) {
				boolean iswhitelisted = false;
				for(int i=0;i<Yamls.Data.getData().getList("ActiveLists").size();i++){
					if (!(Yamls.Data.getData().getList((String) Yamls.Data.getData().getList("ActiveLists").get(i).toString()) == null)) {
						if (Yamls.Data.getData().getList((String) Yamls.Data.getData().getList("ActiveLists").get(i).toString()).contains(e.getPlayer().getName().toLowerCase())) {
							iswhitelisted = true;
						}
					}
					if (player.hasPermission("MultiWhitelist." + Yamls.Data.getData().getList("ActiveLists").get(i).toString() + ".bypass")) {
						iswhitelisted = true;
					}
					if (player.hasPermission("MultiWhitelist.Bypass")) {
						iswhitelisted = true;
					}
					if (player.isOp()) {
						iswhitelisted = true;
					}
				}
				if (iswhitelisted == false) {
					e.setResult(Result.KICK_OTHER);
					e.setKickMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("KickMessage")));
				}
				iswhitelisted = false;
			}
		}
	}
	
	
	
	
	
}
