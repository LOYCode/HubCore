package io.loyloy.hubcore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class AnnounceRunnable extends BukkitRunnable
{
    private HubCore plugin;
    private Random random;
    private int lastPick;

    public AnnounceRunnable( HubCore plugin )
    {
        this.plugin = plugin;
        this.random = new Random();
        this.lastPick = 0;
    }

    @Override
    public void run()
    {
        plugin.reloadConfig();
        List<String> messages = plugin.getConfig().getStringList( "announcements" );
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        int pick;
        String message;

        do
        {
            pick = random.nextInt( messages.size() );
        }
        while( pick == lastPick );

        lastPick = pick;
        message = messages.get( pick );

        for( Player player : players )
        {
            new Announcement( player, message ).send();
        }
    }

    public class Announcement
    {
        private Player player;
        private String message;

        public Announcement( Player player, String message )
        {
            this.player = player;
            this.message = message;
        }

        private String format()
        {
            message = HubCore.getMol() + message;
            message = ChatColor.translateAlternateColorCodes( '&', message );
            message = message.replace( "%player%", player.getDisplayName() );
            //message = message.replace( "%money%", LoyCore.economy.format( LoyCore.economy.getBalance( player ) ) );
            return message;
        }

        private void enact()
        {
            if( message.contains( "%heal%" ) )
            {
                player.setHealth( player.getMaxHealth() );
                message = message.replace( "%heal%", "" );
            }
            if( message.contains( "%hug%" ) )
            {
                TitleMessage.showMessage( player, "", ChatColor.LIGHT_PURPLE + "❤❤❤ Molly" + ChatColor.LIGHT_PURPLE + " hugged you! ❤❤❤", 50 );
                message = message.replace( "%hug%", "" );
            }
            if( message.contains( "%play=" ) )
            {
                String[] split = message.split( "%play=" );
                message = split[0];
                Bukkit.getServer().dispatchCommand( player.getServer().getConsoleSender(), "midi " + split[1] );
            }
        }

        public boolean send()
        {
            enact();
            player.sendMessage( format() );
            return true;
        }
    }
}