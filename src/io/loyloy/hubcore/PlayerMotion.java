package io.loyloy.hubcore;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class PlayerMotion {

    private BukkitRunnable routine;
    private HubCore plugin;

    public PlayerMotion(HubCore pl) {
        this.plugin = pl;
    }

    public void pluginEnabled() {
         routine = new BukkitRunnable() {
            public void run() {
                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    World world = player.getWorld();
                    Location feet = player.getLocation();
                    Material gravel = world.getBlockAt(new Location(world, feet.getX(), feet.getY()-1, feet.getZ())).getType();
                    if (gravel==Material.GRAVEL) {
                        player.addPotionEffect(PotionEffectType.SPEED.createEffect((int) 60L, 1), true);
                    }
                }
            }
        };
        routine.runTaskTimer(this.plugin, 0L, 5L);
    }
    public void pluginDisabled() {
        this.routine.cancel();
    }
}
