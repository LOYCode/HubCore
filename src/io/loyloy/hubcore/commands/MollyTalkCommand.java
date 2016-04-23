package io.loyloy.hubcore.commands;

import io.loyloy.hubcore.HubCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class MollyTalkCommand implements CommandExecutor
{
    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( ! sender.hasPermission( "loy.mollytalk" ) )
        {
            sender.sendMessage( HubCore.getPfx() + ChatColor.RED + "Sorry you don't have permission for that!" );
            return true;
        }

        if( args.length < 1 )
        {
            sender.sendMessage( HubCore.getPfx() + "Usage: /mt <message> to speak as molly." );
            return true;
        }

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        String msg = "";

        for( String word : args )
        {
            msg = msg + word + " ";
        }

        msg = HubCore.getMol() + msg;
        msg = ChatColor.translateAlternateColorCodes( '&', msg );

        for( Player player : players )
        {
            player.sendMessage( msg.replace( "%player%", player.getDisplayName() ) );
        }

        return true;
    }
}