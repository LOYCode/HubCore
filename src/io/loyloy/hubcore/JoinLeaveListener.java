package io.loyloy.hubcore;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class JoinLeaveListener implements Listener
{
    private HashMap<String, Location> spawns;
    private HashMap<Player, Location> waits;

    public JoinLeaveListener(HashMap<String, Location> spawns) {
        this.spawns = spawns;
        this.waits = new HashMap<Player, Location>();
    }

    public void setSpawns(HashMap<String, Location> spawns) {
        this.spawns = spawns;
    }

    @EventHandler
    public void onJoin( PlayerJoinEvent event )
    {
        Player player = event.getPlayer();
        if( player == null )
        {
            return;
        }

        event.setJoinMessage( ChatColor.YELLOW + "+ " + ChatColor.GRAY + event.getPlayer().getName() );

        //MOTD
        player.sendMessage( "" );
        player.sendMessage( "§7§m---------------------------------------------------" ); //Strike
        player.sendMessage( "§fHello §c" + player.getDisplayName() + "§f, Welcome to Loy Hub!" );
        player.sendMessage( "§7§m---------------------------------------------------" ); //Strike
        player.sendMessage( "" );

        if(waits.get(player)!=null) {
            player.teleport(waits.get(player));
            waits.remove(player);
        }
    }

    @EventHandler
    public void onLeave( PlayerQuitEvent event )
    {
        event.setQuitMessage( ChatColor.RED + "- " + ChatColor.GRAY + event.getPlayer().getName() );
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event ) {
        Location l = spawns.get(event.getHostname());
        if(l!=null)
            waits.put(event.getPlayer(), l);
    }
}
