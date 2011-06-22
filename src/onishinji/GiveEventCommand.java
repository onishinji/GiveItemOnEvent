package onishinji;

import java.awt.Color;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveEventCommand implements CommandExecutor {
 
 

    private GiveItemOnEvent plugin;

    public GiveEventCommand(GiveItemOnEvent giveItemOnEvent) {
        // TODO Auto-generated constructor stub
        plugin = giveItemOnEvent;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        
        String[] split = args;
         
        
        if(split.length < 1 || split.length > 1)
        {
            player.sendMessage("Euh, je dois activer ce coffre pour quel évènement ?");
            return true;
        }
        
        if (plugin.eventExist(split[0])) {
            plugin.activePlayer(player, split[0]);
            player.sendMessage("Il faut maintenant cliquer sur un coffre pour l'activer");

        }
        else
        {
            player.sendMessage(ChatColor.RED  +" Je ne connais pas cet évènement");
        }
 
      
        return true;
    } 
 
    
}
