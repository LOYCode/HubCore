package io.loyloy.hubcore.commands;

import io.loyloy.hubcore.HubCore;
import io.loyloy.hubcore.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertCommand implements CommandExecutor
{
    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( sender.hasPermission( "loy.alert" ) )
        {
            if( args.length >= 1 )
            {
                String message = "";
                for( String word : args )
                {
                    message += word + " ";
                }
                message = message.trim();
                message = ChatColor.translateAlternateColorCodes( '&', message );

                for( Player player : Bukkit.getServer().getOnlinePlayers() )
                {
                    message = message.replace( "%player%", player.getDisplayName() );

                    TitleMessage.showMessage( player, "", message, 120 );
                }
            }
            else
            {
                sender.sendMessage( HubCore.getPfx() + "/alert <message>" );
            }
        }
        else
        {
            sender.sendMessage( HubCore.getPfx() + ChatColor.RED + "Sorry no permission!" );
        }
        return true;
    }
}
