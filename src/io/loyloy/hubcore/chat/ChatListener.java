package io.loyloy.hubcore.chat;

import io.loyloy.hubcore.HubCore;
import net.md_5.bungee.api.chat.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatListener implements Listener
{
    private static final Chat chat = HubCore.chat;
    private static final Permission perm = HubCore.permission;

    private final SendPacketThread sendThread;

    private ChannelStore cs;

    public ChatListener( ChannelStore cs )
    {
        this.sendThread = new SendPacketThread();
        this.cs = cs;
    }

    @EventHandler( priority= EventPriority.LOW )
    public void onChat( AsyncPlayerChatEvent e )
    {

        if ( e.isCancelled() )
        {
            return;
        }
        e.setCancelled(true);

        Player p = e.getPlayer();
        if( p == null )
        {
            return;
        }

        ChatColor msgColor = ChatColor.WHITE;
        switch( cs.getPlayerChannel( p ) )
        {
            default:
                break;
        }

        String name = p.getDisplayName();
        String prefix = chat.getPlayerPrefix( p );
        String suffix = chat.getPlayerSuffix( p );
        String group = perm.getPrimaryGroup( p ).substring( 0, 1 ).toUpperCase() + perm.getPrimaryGroup( p ).substring( 1 );
        String msg = e.getMessage();

        //Name Tooltip
        String nameToolTip = "";
        nameToolTip += ChatColor.RED + "Rank " + ChatColor.GRAY + group;
        nameToolTip += "\n" + ChatColor.WHITE + "Name " + ChatColor.GRAY + p.getName();

        if( p.hasPermission( "loy.chat.color" ) )
        {
            msg = ChatColor.translateAlternateColorCodes( '&', msg );
        }
        else
        {
            msg = ChatColor.stripColor( msg );
        }

        TextComponent tcPrefix = new TextComponent( ChatColor.GRAY + prefix );

        TextComponent tcSuffix = new TextComponent( ChatColor.GREEN + suffix );
        //tcSuffix.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( ChatColor.GREEN + group ).create() ) );

        TextComponent tcName = new TextComponent( ChatColor.YELLOW + name );
        tcName.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder( nameToolTip ).create() ) );

        List<String> links = getLinks(msg);
        if( links.size() > 0 )
        {
            msg = msg.replace( links.get(0), ChatColor.UNDERLINE + "Click Meh" + msgColor );
        }

        msg = msgColor + msg;

        TextComponent tcMessage = new TextComponent( msg );
        if( links.size() > 0 )
        {
            tcMessage.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, links.get(0) ) );
            tcMessage.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( links.get(0) ).create() ) );
        }

        TextComponent tcFinal = new TextComponent( "" );
        tcFinal.addExtra( tcPrefix );
        tcFinal.addExtra( " " );
        tcFinal.addExtra( tcName );
        //tcFinal.addExtra( " " );
        tcFinal.addExtra( tcSuffix );
        tcFinal.addExtra( " " );
        tcFinal.addExtra( tcMessage );

        System.out.print( "<" + p.getName() + ">: " + msg ); //Print to console

        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        List<Player> recipients = new ArrayList<>();

        for( Player receiver : onlinePlayers )
        {
            recipients.add( receiver );
        }

        if( recipients.size() < 2 )
        {
            p.sendMessage( ChatColor.YELLOW + "* " + ChatColor.GRAY + "There is no one around to hear you..." );
        }

        sendThread.run( recipients, tcFinal );
    }

    private class SendPacketThread extends Thread
    {
        public void run( List<Player> players, BaseComponent message )
        {
            for( Player player : players )
            {
                player.spigot().sendMessage( message );
            }
        }
    }

    private static List<String> getLinks(String msg)
    {
        String[] words = msg.split( " " );

        List<String> links = new ArrayList<>();

        for( String word : words )
        {
            if( word.contains( "www." )
                    || word.contains( "http://" )
                    || word.contains( "https://" )
                    || word.contains( ".com" )
                    || word.contains( ".io" )
                    || word.contains( ".net" ) )
            {
                links.add( word );
            }
        }

        return links;
    }
}
