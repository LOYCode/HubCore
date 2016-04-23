package io.loyloy.hubcore;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EmeraldEcon
{
    public EmeraldEcon()
    {
    }

    public static int getBalance( Player player )
    {
        if ( player == null || !player.hasPlayedBefore() || !player.isOnline() )
        {
            return 0;
        }

        int emeralds = 0;
        ItemStack[] items = player.getInventory().getContents();

        for ( ItemStack item : items )
        {
            if ( item == null )
            {
                continue;
            }

            if ( item.getType().equals( Material.EMERALD ) )
            {
                emeralds += item.getAmount();
            }
        }

        return emeralds;
    }

    public static void removeEmeralds( Player player, int amount )
    {
        if ( player == null || !player.hasPlayedBefore() || !player.isOnline() )
        {
            return;
        }

        for ( int i = amount; i > 0; i-- )
        {
            if ( player.getInventory().contains( Material.EMERALD ) )
            {
                int slot = player.getInventory().first( Material.EMERALD );
                ItemStack emeralds = player.getInventory().getItem( slot );
                int count = emeralds.getAmount();

                if ( count == 1 )
                {
                    player.getInventory().setItem( slot, new ItemStack( Material.AIR ) );
                }
                else
                {
                    emeralds.setAmount( count - 1 );
                    player.getInventory().setItem( slot, emeralds );
                }
            }
        }
    }

    public static void addEmeralds( Player player, int amount )
    {
        if ( player == null || !player.hasPlayedBefore() || !player.isOnline() )
        {
            return;
        }

        ItemStack emerald = new ItemStack( Material.EMERALD );

        for ( int i = amount; i > 0; i-- )
        {
            if ( (!player.getInventory().contains( emerald )) )
            {
                player.getInventory().addItem( emerald );
                continue;
            }

            int slot = player.getInventory().first( Material.EMERALD );
            ItemStack emeralds = player.getInventory().getItem( slot );
            int count = emeralds.getAmount();

            if ( count == 64 )
            {
                player.getWorld().dropItem( player.getLocation(), emerald );
            }
            else
            {
                emeralds.setAmount( count + 1 );
                player.getInventory().setItem( slot, emeralds );
            }
        }
    }
}

