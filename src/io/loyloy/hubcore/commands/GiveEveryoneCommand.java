package io.loyloy.hubcore.commands;

import io.loyloy.hubcore.HubCore;
import io.loyloy.hubcore.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class GiveEveryoneCommand implements CommandExecutor
{
    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( ! (sender instanceof Player) )
        {
            sender.sendMessage( HubCore.getPfx() + "You must be a player to do this." );
            return true;
        }

        if( ! sender.hasPermission( "loy.giveeveryone" ) )
        {
            sender.sendMessage( HubCore.getPfx() + ChatColor.RED + "You don't have permissions for this!" );
            return true;
        }

        Player p = (Player) sender;
        ItemStack item = p.getItemInHand();

        if( item.getType().equals( Material.AIR ) )
        {
            sender.sendMessage( HubCore.getPfx() + ChatColor.RED + "You cant send air!" );
            return true;
        }

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        int didNotGet = 0;

        for( Player player : players )
        {
            Inventory i = player.getInventory();

            if( i.firstEmpty() != -1 )
            {
                i.addItem( item );
                TitleMessage.showMessage( player, "", ChatColor.GREEN + "You got an item from " + ChatColor.YELLOW + p.getDisplayName() + ChatColor.GREEN + ", check your inv!", 80 );
            }
            else
            {
                didNotGet++;
            }
        }

        sender.sendMessage( HubCore.getPfx() + "You sent an item to all players!" );
        sender.sendMessage( HubCore.getPfx() + "Players that did not get the item: " + ChatColor.YELLOW + didNotGet );

        return true;
    }
}
