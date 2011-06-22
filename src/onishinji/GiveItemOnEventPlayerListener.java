package onishinji;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import models.MyLocation;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GiveItemOnEventPlayerListener extends PlayerListener {

    private GiveItemOnEvent plugin;

    public GiveItemOnEventPlayerListener(GiveItemOnEvent giveItemOnEvent) {
        // TODO Auto-generated constructor stub
        plugin = giveItemOnEvent;
    }
    

    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getClickedBlock() != null && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();

            // On regarde la valeur dans le wall sign
            if (block.getType() == Material.CHEST) {
                // On cherche un coffre
                if (plugin._playerActiveCommands.containsKey(player)) {

                    HashMap<String, Boolean> test = plugin._playerActiveCommands.get(player);
                    
                    String eventName = test.keySet().iterator().next();
                    
                    MyLocation loc = new MyLocation(block.getLocation());
                    loc.name = eventName;
                    
                    plugin.addLocationCoffre(player,loc, eventName);
                }

            } else {

                if (plugin._playerActiveCommands.containsKey(player)) {
                    player.sendMessage("J'avais dit un coffre ! Pas un vulgaire " + block.getType());
                }
            }

            plugin._playerActiveCommands.remove(player);
        }
    }
    

    @SuppressWarnings("deprecation")
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(plugin.hasItemsForPlayer(player))
        {
            plugin.getItemForPlayer(player);
        } 
    }
 

}
