package io.loyloy.hubcore;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleMessage
{
    private static final int FADE_IN = 9;
    private static final int FADE_OUT = 9;

    public static void showMessage( Player player, String title, String subTitle, int time, int fadeIn, int fadeOut )
    {
        PlayerConnection craftPlayer = ((CraftPlayer)player).getHandle().playerConnection;

        PacketPlayOutTitle titleBig = new PacketPlayOutTitle( PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a( "{'text': ''}" ).a(title), fadeIn, time, fadeOut );
        PacketPlayOutTitle titleSmall = new PacketPlayOutTitle( PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a( "{'text': ''}" ).a(subTitle), fadeIn, time, fadeOut );

        craftPlayer.sendPacket(titleBig);
        craftPlayer.sendPacket(titleSmall);
    }

    public static void showMessage( Player player, String title, String subTitle, int time )
    {
        showMessage( player, title, subTitle, time, FADE_IN, FADE_OUT );
    }
}