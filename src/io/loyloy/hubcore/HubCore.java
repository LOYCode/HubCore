package io.loyloy.hubcore;

import io.loyloy.hubcore.chat.ChannelStore;
import io.loyloy.hubcore.chat.ChatCommand;
import io.loyloy.hubcore.chat.ChatListener;
import io.loyloy.hubcore.chat.MollyChat;
import io.loyloy.hubcore.commands.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class HubCore extends JavaPlugin
{
    public static Permission permission = null;
    public static Chat chat = null;

    private static final String PREFIX = ChatColor.YELLOW + "[Loy]" + ChatColor.GREEN + " ";
    private static final String MOLLY = ChatColor.GRAY + "Server " + ChatColor.AQUA + "Molly" + ChatColor.WHITE + " ";

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig(); // Makes a config if one does not exist.

        setupPermissions();
        setupChat();

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        PluginManager pm = getServer().getPluginManager();

        getCommand( "alert" ).setExecutor( new AlertCommand() );
        getCommand( "announce" ).setExecutor( new AnnounceCommand( this ) );
        getCommand( "emeralds" ).setExecutor( new EmeraldsCommand() );
        getCommand( "fly" ).setExecutor( new FlyCommand() );
        getCommand( "giveeveryone" ).setExecutor( new GiveEveryoneCommand() );
        getCommand( "hug" ).setExecutor( new HugCommand() );
        getCommand( "mollytalk" ).setExecutor( new MollyTalkCommand() );

        //Misc
        pm.registerEvents( new JoinLeaveListener(), this );

        //Loy Chat module
        ChannelStore channelStore = new ChannelStore();
        ChatListener chatListener = new ChatListener( channelStore );
        pm.registerEvents( chatListener, this );
        pm.registerEvents( new MollyChat( this, channelStore ), this );
        getCommand( "chat" ).setExecutor( new ChatCommand( channelStore ) );
        getCommand( "playertalk" ).setExecutor( new PlayerTalkCommand( chatListener ) );

        // Announcements
        if( getConfig().getStringList( "announcements" ).size() >= 2 )
        {
            scheduler.scheduleSyncRepeatingTask( this, new AnnounceRunnable( this ), 3600, 3600 );
        }
        else
        {
            getLogger().info( "Please enter at least 2 announcements for announcements to enable!" );
        }
    }

    public static String getPfx() { return PREFIX; }

    public static String getMol() { return MOLLY; }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }
}
