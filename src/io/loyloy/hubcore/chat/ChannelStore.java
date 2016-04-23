package io.loyloy.hubcore.chat;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class ChannelStore
{
    private HashMap<Player, Character> channels;

    public ChannelStore()
    {
        this.channels = new HashMap<>();
    }

    public char getPlayerChannel(Player p)
    {
        if (channels.containsKey(p))
        {
            return channels.get(p);
        }
        else
        {
            return 'g';
        }
    }

    public void setPlayerChannel(Player p, char channel)
    {
        channels.put(p, channel);
    }
}

