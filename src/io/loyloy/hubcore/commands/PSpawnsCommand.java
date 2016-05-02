package io.loyloy.hubcore.commands;

import io.loyloy.hubcore.HubCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Thomasims on 01/05/2016.
 */
public class PSpawnsCommand implements CommandExecutor {

    private HubCore plugin;

    public PSpawnsCommand(HubCore hubCore) {
        plugin = hubCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args )
    {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }
        Player player = (Player) sender;
        if(player.hasPermission("loy.setspawn")) {
            if(args.length<1) {
                player.sendMessage("Available sub-commands:");
                player.sendMessage(" /setpspawn set [name]");
                player.sendMessage(" /setpspawn new [name]");
                player.sendMessage(" /setpspawn remove [name]");
                player.sendMessage(" /setpspawn list");
                player.sendMessage(" /setpspawn address [name] [...]");
                return true;
            }
            ConfigurationSection spawns = plugin.getConfig().getConfigurationSection("spawns");
            switch(args[0]) {
                case "set": //setpspawn set [name]
                    if(args.length!=2)
                        return false;
                    if(!spawns.contains(args[1])) {
                        player.sendMessage("No server with name \""+args[1]+"\", have you created it?");
                        return true;
                    }
                    ConfigurationSection srvspawns = spawns.getConfigurationSection(args[1]);
                    Location loc = player.getLocation();
                    srvspawns.set("x",loc.getX());
                    srvspawns.set("y",loc.getY());
                    srvspawns.set("z",loc.getZ());
                    srvspawns.set("pitch",loc.getPitch());
                    srvspawns.set("yaw",loc.getYaw());
                    player.sendMessage("Position succesfully set for server "+args[1]);
                    break;
                case "new": //setpspawn new [name]
                    if(args.length!=2)
                        return false;
                    if(spawns.contains(args[1])) {
                        player.sendMessage("Server with name \""+args[1]+"\" already exists.");
                        return true;
                    }
                    ConfigurationSection newsrvspawns = spawns.createSection(args[1]);
                    newsrvspawns.set("x",0.0f);
                    newsrvspawns.set("y",0.0f);
                    newsrvspawns.set("z",0.0f);
                    newsrvspawns.set("pitch",0.0f);
                    newsrvspawns.set("yaw",0.0f);
                    newsrvspawns.set("addresses",new ArrayList<String>());
                    player.sendMessage("New server \""+args[1]+"\"succesfully created.");
                    break;
                case "remove":
                    if(args.length!=2)
                        return false;
                    if(!spawns.contains(args[1])) {
                        player.sendMessage("No server with name \""+args[1]+"\", have you created it?");
                        return true;
                    }
                    spawns.set(args[1],null);
                    player.sendMessage("Server \""+args[1]+"\"succesfully removed.");
                    break;
                case "list":
                    Set<String> keys = spawns.getKeys(false);
                    String msg = "Servers currently set: ";
                    if (keys!=null)
                        for(String key: keys) {
                            msg = msg + key + ", ";
                        }
                    msg = msg.substring(0,msg.length()-2);
                    player.sendMessage(msg);
                    break;
                case "teleport":
                    if(args.length!=2)
                        return false;
                    if(!spawns.contains(args[1])) {
                        player.sendMessage("No server with name \""+args[1]+"\", have you created it?");
                        return true;
                    }
                    ConfigurationSection tpspawnp = spawns.getConfigurationSection(args[1]);
                    Location tploc = new Location(
                            Bukkit.getServer().getWorlds().get(0),
                            tpspawnp.getDouble("x"),
                            tpspawnp.getDouble("y"),
                            tpspawnp.getDouble("z"),
                            (float) tpspawnp.getDouble("yaw"),
                            (float) tpspawnp.getDouble("pitch")
                    );
                    player.teleport(tploc);
                    break;
                case "address": //setpspawn address [name] ...
                    if(args.length>1 && !spawns.contains(args[1])) {
                        player.sendMessage("No server with name \""+args[1]+"\", have you created it?");
                        return true;
                    }
                    if(args.length<3) {
                        player.sendMessage("Available sub-options:");
                        player.sendMessage(" /setpspawn address [name] add [address]");
                        player.sendMessage(" /setpspawn address [name] remove [address]");
                        player.sendMessage(" /setpspawn address [name] list");
                        return true;
                    }
                    ConfigurationSection addrspawns = spawns.getConfigurationSection(args[1]);
                    List<String> addresses = addrspawns.getStringList("addresses");
                    switch(args[2]) {
                        case "add": //setpspawn address [name] add [address]
                            if(args.length!=4)
                                return false;
                            if(!args[3].contains(":"))
                                args[3] = args[3]+":25565";
                            if(addresses.contains(args[3])) {
                                player.sendMessage("Address is already set for that server");
                                return true;
                            }
                            addresses.add(args[3]);
                            player.sendMessage("Added address to the server.");
                            break;
                        case "remove": //setpspawn address [name] remove [address]
                            if(args.length!=4)
                                return false;
                            if(!args[3].contains(":"))
                                args[3] = args[3]+":25565";
                            if(!addresses.contains(args[3])) {
                                player.sendMessage("Address isn't set for that server");
                                return true;
                            }
                            addresses.remove(args[3]);
                            player.sendMessage("Removed address from the server.");
                            break;
                        case "list": //setpspawn address [name] list
                            if(args.length!=3)
                                return false;
                            String amsg = "Addresses currently assigned: ";
                            for(String addr: addresses) {
                                amsg = amsg + addr + ", ";
                            }
                            amsg = amsg.substring(0,amsg.length()-2);
                            player.sendMessage(amsg);
                            break;
                        default:
                            player.sendMessage("Available sub-options:");
                            player.sendMessage(" /setpspawn address [name] add [address]");
                            player.sendMessage(" /setpspawn address [name] remove [address]");
                            player.sendMessage(" /setpspawn address [name] list");
                    }
                    addrspawns.set("addresses",addresses);
                    break;
                default:
                    player.sendMessage("Available sub-commands:");
                    player.sendMessage(" /setpspawn set [name]");
                    player.sendMessage(" /setpspawn new [name]");
                    player.sendMessage(" /setpspawn remove [name]");
                    player.sendMessage(" /setpspawn list");
                    player.sendMessage(" /setpspawn address [name] [...]");

            }
            plugin.getConfig().set("spawns",spawns);
            plugin.saveConfig();
            plugin.joinLeaveListener.setSpawns(plugin.setupSpawns(spawns));
        } else {
            player.sendMessage( HubCore.getPfx() + ChatColor.RED + "Sorry no permission!" );
        }
        return true;
    }
}
