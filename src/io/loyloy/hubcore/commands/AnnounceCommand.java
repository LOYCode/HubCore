package io.loyloy.hubcore.commands;

import io.loyloy.hubcore.HubCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AnnounceCommand implements CommandExecutor
{
    private HubCore plugin;

    public AnnounceCommand( HubCore plugin )
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( sender.hasPermission( "loy.announce" ) )
        {
            if( args.length >= 1 )
            {
                List<String> messages = plugin.getConfig().getStringList( "announcements" );

                if( args[0].equalsIgnoreCase( "list" ) )
                {
                    int id = 0;
                    sender.sendMessage( ChatColor.YELLOW + "*- Announcements -*" );
                    sender.sendMessage( ChatColor.YELLOW + "ID : Message" );
                    for( String message : messages )
                    {
                        sender.sendMessage( ChatColor.GREEN + "" + id + " : " + message );
                        id++;
                    }
                }

                if( args[0].equalsIgnoreCase( "add" ) )
                {
                    if( args.length == 1 )
                    {
                        sender.sendMessage( ChatColor.GREEN + "/announce add <msg>" );
                        return true;
                    }

                    String newMessage = "";
                    for( int i = 1 ; i < args.length ; i++ )
                    {
                        newMessage += args[i] + " ";
                    }

                    messages.add( newMessage );

                    plugin.getConfig().set( "announcements", messages );
                    sender.sendMessage( ChatColor.GREEN + "Yay your message has been added! ;3" );
                    plugin.saveConfig();
                }

                if( args[0].equalsIgnoreCase( "del" ) )
                {
                    if( args.length == 1 )
                    {
                        sender.sendMessage( ChatColor.GREEN + "/announce del <msg id>" );
                        return true;
                    }

                    try
                    {
                        int id = Integer.parseInt( args[1] );
                        messages.remove( id );
                    }
                    catch( Exception e )
                    {
                        sender.sendMessage( ChatColor.RED + "That is not a valid id!" );
                        return true;
                    }

                    plugin.getConfig().set( "announcements", messages );
                    sender.sendMessage( ChatColor.GREEN + "You removed a message! :O" );
                    plugin.saveConfig();
                }
            }
            else
            {
                sender.sendMessage( ChatColor.YELLOW + "*- Announce Help -*" );
                sender.sendMessage( ChatColor.GREEN + "/announce list" );
                sender.sendMessage( ChatColor.GREEN + "/announce add <msg>" );
                sender.sendMessage( ChatColor.GREEN + "/announce del <msg id>" );
            }
        }

        return true;
    }
}
