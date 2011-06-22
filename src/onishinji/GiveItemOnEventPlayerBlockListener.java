package onishinji;

import java.util.ArrayList;

import models.MyLocation;
import models.SLAPI;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class GiveItemOnEventPlayerBlockListener extends BlockListener {

    private GiveItemOnEvent plugin;

    public GiveItemOnEventPlayerBlockListener(GiveItemOnEvent giveItemOnEvent) {
        // TODO Auto-generated constructor stub
        plugin = giveItemOnEvent;
    }
    
    public void onBlockBreak(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        this.checkAndRemoveCoffre(block, event.getPlayer());
    }
    
    public void onBlockBurn(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        this.checkAndRemoveCoffre(block, event.getPlayer()); 
    }
    
    private void checkAndRemoveCoffre(Block block, Player player)
    {  
        MyLocation toRemove = null;
        ArrayList<MyLocation> coffres = plugin.getAllCoffres();
        for(MyLocation loc : coffres)
        {
            Location currentLoc = new Location(block.getWorld(), loc.getX(), loc.getY(), loc.getZ());
            
            if(currentLoc.equals(block.getLocation()))
            { 
                toRemove = loc;
                if( plugin.eventExist(loc.name))
                {
                    player.sendMessage("Ce coffre servait pour '" + plugin.getEvent(loc.name).displayName + "' ... Il faudra le réactiver si tu le pose à nouveau.");
                    plugin.removeChest(toRemove.name, toRemove);
                    plugin.saveChest(toRemove.name);
                }
            }
            
        } 
    }

}
