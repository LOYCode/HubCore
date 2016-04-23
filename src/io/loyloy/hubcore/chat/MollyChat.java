package io.loyloy.hubcore.chat;

import io.loyloy.hubcore.HubCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class MollyChat implements Listener
{
    private HashMap<String, HashSet<String> > replies;
    private String lastMsg = "";
    private long lastTime;

    private ChannelStore cs;

    public MollyChat( HubCore p, ChannelStore cs )
    {
        this.cs = cs;

        replies = new HashMap<>();
        lastTime = System.currentTimeMillis();

        List<String> rawReplies = p.getConfig().getStringList( "replies" );

        // "Gosh that's clever!=joy+cat+horse"

        for( String rawReplie : rawReplies )
        {
            String[] temp = rawReplie.split( "=" );

            String reply = ChatColor.translateAlternateColorCodes( '&', HubCore.getMol() + temp[0] );
            String wordsString = temp[1];

            String[] wordsArray = wordsString.split( "\\+" );
            HashSet<String> words = new HashSet<>();

            for( String word : wordsArray )
            {
                words.add( word );
            }

            replies.put( reply, words );
        }
    }

    @EventHandler
    public void onChat( AsyncPlayerChatEvent event )
    {
        if ( cs.getPlayerChannel( event.getPlayer() ) != 'g' )
        {
            return;
        }

        String reply = getReply( event.getMessage() );

        if( reply == null )
        {
            return;
        }

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        for( Player player : players )
        {
            player.sendMessage( reply.replace( "%player%", player.getDisplayName() ) );
        }
    }

    public String getReply( String msg )
    {
        msg = formatMsg( msg );

        for( Map.Entry<String, HashSet<String>> reply : replies.entrySet() )
        {
            HashSet<String> keys = reply.getValue();
            String[] wordsArray = msg.split( "\\s+" ); // Split msg into a list.
            Collection<String> words = Arrays.asList( wordsArray );

            boolean isSubset = words.containsAll( keys ); // Check if the msg contains all keys.

            if( isSubset )
            {
                // Anti Spam for duplicated messages.
                if( lastMsg.equals( reply.getKey() ) )
                {
                    return null;
                }

                long time = System.currentTimeMillis();
                if( time <= (lastTime + 10 * 1000) )
                {
                    return null;
                }

                lastTime = time;
                lastMsg = reply.getKey();

                return reply.getKey();
            }
        }

        return null;
    }

    private String formatMsg( String msg )
    {
        msg = msg.replace( '?', ' ' );
        msg = msg.replace( '!', ' ' );
        msg = msg.replace( '.', ' ' );
        msg = msg.replace( ',', ' ' );
        msg = msg.toLowerCase();

        return msg;
    }
}