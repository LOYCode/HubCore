package io.loyloy.hubcore;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CompassListener implements Listener
{
    private ItemStack compass;
    private static final String COMPASS_NAME = ChatColor.YELLOW + "Server Selector";

    public CompassListener()
    {
        ItemStack myItem = new ItemStack( Material.COMPASS, 1 );
        ItemMeta im = myItem.getItemMeta();
        im.setDisplayName( COMPASS_NAME );
        myItem.setItemMeta( im );

        compass = myItem;
    }

    @EventHandler
    public void onJoin( PlayerJoinEvent event )
    {
        Player player = event.getPlayer();

        if( player == null || !player.isOnline() )
        {
            return;
        }

        if( player.getInventory().contains( compass ) )
        {
            return;
        }

        player.getInventory().addItem( compass );
    }
}
