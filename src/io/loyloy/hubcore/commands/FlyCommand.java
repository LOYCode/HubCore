package io.loyloy.hubcore.commands;

import io.loyloy.hubcore.HubCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor
{
    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if ( ( sender instanceof Player ) )
        {
            if( sender.hasPermission( "loy.fly" ) )
            {
                Player p = (Player) sender;

                if( args.length == 0 )
                {
                    togglePlayerFly( p );
                }
                else if ( sender.hasPermission( "loy.fly.other" ) )
                {
                    Player other = Bukkit.getPlayer( args[0] );

                    if( other == null )
                    {
                        p.sendMessage( HubCore.getPfx() + ChatColor.RED + "Sorry that player is not online :(" );
                        return true;
                    }

                    togglePlayerFly( other );

                    p.sendMessage( HubCore.getPfx() + "Flight enabled for " + other.getDisplayName() );
                }
                return true;
            }
            else
            {
                sender.sendMessage( HubCore.getPfx() + ChatColor.RED + "Sorry, no permission to fly!" );
            }
        }
        return true;
    }

    private void togglePlayerFly( Player p )
    {
        if( p.getAllowFlight() )
        {
            p.setAllowFlight( false );
            p.sendMessage( HubCore.getPfx() + ChatColor.RED + "++ Flight Disabled ++" );
        }
        else
        {
            p.setAllowFlight( true );
            p.sendMessage( HubCore.getPfx() + "++ Flight Enabled ++" );
        }
    }
}
