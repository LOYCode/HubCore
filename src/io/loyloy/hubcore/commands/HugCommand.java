package io.loyloy.hubcore.commands;

import io.loyloy.hubcore.HubCore;
import io.loyloy.hubcore.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HugCommand implements CommandExecutor
{
    public HugCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if ( ( sender instanceof Player ) )
        {
            if( sender.hasPermission( "loy.hug" ) )
            {
                if( args.length > 0 )
                {
                    String senderName = ((Player)sender).getDisplayName();

                    for( Player player : Bukkit.getServer().getOnlinePlayers() )
                    {
                        player.sendMessage( ChatColor.GRAY + "❤ " + senderName + ChatColor.GRAY + " hugged " + args[0] + " ❤" );

                        if( args[0].equalsIgnoreCase( player.getName() ) || args[0].equalsIgnoreCase( ChatColor.stripColor( player.getDisplayName() ) ) )
                        {
                            TitleMessage.showMessage( player, "", ChatColor.LIGHT_PURPLE + "❤❤❤ " + senderName + ChatColor.LIGHT_PURPLE + " hugged you! ❤❤❤", 50 );
                        }
                    }
                }
                else
                {
                    sender.sendMessage( HubCore.getPfx() + ChatColor.LIGHT_PURPLE + "Hug someone with /hug <PlayerName> :D" );
                }
                return true;
            }
        }
        return true;
    }
}
