package io.loyloy.hubcore;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener
{
    @EventHandler
    public void onJoin( PlayerJoinEvent event )
    {
        Player player = event.getPlayer();
        if( player == null )
        {
            return;
        }

        event.setJoinMessage( ChatColor.YELLOW + "+ " + ChatColor.GRAY + event.getPlayer().getDisplayName() );

        //MOTD
        player.sendMessage( "" );
        player.sendMessage( "§b§m---------------------------------------------------" ); //Strike
        player.sendMessage( "§eHello §f" + player.getDisplayName() + "§e, Welcome to Loy Hub!" );
        player.sendMessage( "§b§m---------------------------------------------------" ); //Strike
        player.sendMessage( "" );
    }

    @EventHandler
    public void onLeave( PlayerQuitEvent event )
    {
        event.setQuitMessage( ChatColor.RED + "- " + ChatColor.GRAY + event.getPlayer().getDisplayName() );
    }
}
