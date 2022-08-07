package yusama125718.potionprotect;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static yusama125718.potionprotect.PotionProtect.*;

public class Event implements Listener{
    public Event(PotionProtect plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void BrewingStandFuelEvent(BrewingStandFuelEvent event) {
        if (!operation) return;
        if (!allowitem.containsKey(event.getFuel().getType())) return;
        for (Integer number : allowitem.get(Material.BLAZE_POWDER)) {       //確認処理
            if (!event.getFuel().hasItemMeta()) return;
            if (event.getFuel().getItemMeta().getCustomModelData() == number) return;
        }
        event.setCancelled(true);       //キャンセル処理
    }

    @EventHandler
    public void BrewEvent(BrewEvent event) {
        if (!operation) return;
        if (event.getContents().getIngredient().getType() == Material.GOLDEN_CARROT && event.getContents().getIngredient().hasItemMeta()){
            if (event.getContents().getIngredient().getItemMeta().getCustomModelData() == 3){
                System.out.println("potion create");
                event.getResults().get(0).setType(Material.DRAGON_BREATH);
                return;
            }
        }
        if (!allowitem.containsKey(Objects.requireNonNull(event.getContents().getIngredient()).getType())) return;
        for (Integer number : allowitem.get(event.getContents().getIngredient().getType())) {       //確認処理
            if (!event.getContents().getIngredient().hasItemMeta()) return;
            if (event.getContents().getIngredient().getItemMeta().getCustomModelData() == number) return;
        }
        event.setCancelled(true);       //キャンセル処理
    }
}
