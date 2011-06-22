package onishinji;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import models.EventParser;
import models.Item;
import models.MyLocation;
import models.SLAPI;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GiveItemOnEvent extends JavaPlugin {

    private final GiveItemOnEventPlayerListener playerListener = new GiveItemOnEventPlayerListener(this);
    private final GiveItemOnEventPlayerBlockListener blockListiner = new GiveItemOnEventPlayerBlockListener(this);
    private HashMap<String, ArrayList<MyLocation>> _coffresByEvent;
    HashMap<Player, HashMap<String, Boolean>> _playerActiveCommands;
    ArrayList<HashMap<Player, String>> _playerFood = new ArrayList<HashMap<Player, String>>();

    private ArrayList<models.Event> events;

    // ////////////////////////////////////////////////////////////////////////////////
    // INIT PARTS
    // ////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDisable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnable() {
        // TODO Auto-generated method stub

        _playerActiveCommands = new HashMap<Player, HashMap<String, Boolean>>();
        _playerFood = new ArrayList<HashMap<Player, String>>();

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListiner, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BURN, blockListiner, Priority.Normal, this);

        // Register our commands
        getCommand("active-event").setExecutor(new GiveEventCommand(this));

        // Creation répertoire
        File testInstall = new File("plugins/giveitemonevent");
        if (!testInstall.exists()) {
            testInstall.mkdir();
        }

        File sample = new File(testInstall, "events.xml");
        if (!sample.exists()) {
            try {
                FileWriter sortie = new FileWriter(sample);

                sortie.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.getProperty("line.separator") + "<events>" + System.getProperty("line.separator") + "\t<event>" + System.getProperty("line.separator") + "\t\t<name>SampleTest</name>" + System.getProperty("line.separator") + "\t\t<description>Sample Test message success</description>" + System.getProperty("line.separator") + "\t\t<is_active>true</is_active>" + System.getProperty("line.separator") + "\t\t<period>monday tuesday wednesday thursday friday saturday sunday</period>" + System.getProperty("line.separator")
                        + "\t\t<hourMin>00</hourMin>" + System.getProperty("line.separator") + "\t\t<hourMax>14</hourMax>" + System.getProperty("line.separator") + "\t\t<items>" + System.getProperty("line.separator") + "\t\t\t<item>" + System.getProperty("line.separator") + "\t\t\t\t<object_id>4</object_id>" + System.getProperty("line.separator") + "\t\t\t\t<number>10</number>" + System.getProperty("line.separator") + "\t\t\t\t<errorMsg>Not more cobblestone, go miner.</errorMsg>" + System.getProperty("line.separator") + "\t\t\t</item>" + System.getProperty("line.separator") + "\t\t</items>"
                        + System.getProperty("line.separator") + "\t</event>" + System.getProperty("line.separator") + "</events>");
                sortie.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        this.parseEvents();

    }

    public void parseEvents() {
        _coffresByEvent = new HashMap<String, ArrayList<MyLocation>>();

        // Lecture du répertoire
        try {
            File[] files = SLAPI.listFiles("plugins", "giveitemonevent");
            for (File currentFile : files) {
                if (currentFile.getName().contains(".bin")) {

                    String name = currentFile.getName().replaceAll(".bin", "");

                    ArrayList<MyLocation> test;
                    test = (ArrayList<MyLocation>) SLAPI.load(currentFile.getPath());

                    _coffresByEvent.put(name, test);

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        EventParser parser = new EventParser("plugins/giveitemonevent/events.xml");
        events = parser.getEvents();
    }

    // ////////////////////////////////////////////////////////////////////////////////
    // PLAYERS PARTS
    // ////////////////////////////////////////////////////////////////////////////////

    public void activePlayer(Player player, String eventName) {

        if (_playerActiveCommands.containsKey(player)) {
            HashMap<String, Boolean> test = _playerActiveCommands.get(player);
            _playerActiveCommands.remove(player);

        }

        Boolean bool = new Boolean(true);
        HashMap<String, Boolean> test = new HashMap<String, Boolean>();
        test.put(eventName, bool);

        _playerActiveCommands.put(player, test);
    }

    // ////////////////////////////////////////////////////////////////////////////////
    // CHEST PARTS
    // ////////////////////////////////////////////////////////////////////////////////

    public ArrayList<MyLocation> getAllCoffres() {
        // TODO Auto-generated method stub
        ArrayList<MyLocation> allLocations = new ArrayList<MyLocation>();
        for (Iterator iter = _coffresByEvent.entrySet().iterator(); iter.hasNext();) {
            Map.Entry ent = (Map.Entry) iter.next();
            // La clé de la HashMap
            String key = (String) ent.getKey();
            // La Valeur de la HashMap
            ArrayList<MyLocation> valeur = (ArrayList<MyLocation>) ent.getValue();
            for (MyLocation loc : valeur) {
                loc.name = key;
                allLocations.add(loc);
            }
        }
        return allLocations;
    }

    public void addLocationCoffre(Player player, MyLocation myLocation, String eventName) {

        if (this.eventExist(eventName)) {

            if (!_coffresByEvent.containsKey(eventName)) {
                ArrayList<MyLocation> listCoffre = new ArrayList<MyLocation>();
                listCoffre.add(myLocation);

                System.out.println("_coffresByEvent vide ");
                player.sendMessage("Tu as activé ce coffre pour '" + this.getEvent(eventName).displayName + "'");

                _coffresByEvent.put(eventName, listCoffre);
            } else {
                ArrayList<MyLocation> list = _coffresByEvent.get(eventName);

                boolean coffreExist = false;
                for (MyLocation tmp : list) {
                    if (tmp.getX() == myLocation.getX() && tmp.getY() == myLocation.getY() && tmp.getZ() == myLocation.getZ()) {
                        coffreExist = true;
                    }
                }

                if (!coffreExist) {
                    player.sendMessage("Tu as activé ce coffre pour '" + this.getEvent(eventName).displayName + "'");
                    list.add(myLocation);

                    _coffresByEvent.remove(eventName);
                    _coffresByEvent.put(eventName, list);

                } else {
                    player.sendMessage(ChatColor.RED + "Ce coffre est déjà lié à '" + this.getEvent(eventName).displayName + "'");
                }
            }

            if (_coffresByEvent.containsKey(eventName)) {
                try {
                    SLAPI.save(_coffresByEvent.get(eventName), "plugins/giveitemonevent/" + eventName + ".bin");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

    }

    public void removeChest(String eventName, MyLocation toRemove) {

        if (this.eventExist(eventName)) {

            // TODO Auto-generated method stub
            if (_coffresByEvent.containsKey(eventName)) {
                ArrayList<MyLocation> locations = _coffresByEvent.get(eventName);
                locations.remove(toRemove);

                _coffresByEvent.remove(eventName);
                _coffresByEvent.put(eventName, locations);

                if (_coffresByEvent.containsKey(eventName)) {
                    try {
                        SLAPI.save(_coffresByEvent.get(eventName), "plugins/giveitemonevent/" + eventName + ".bin");
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void saveChest(String eventName) {
        if (this.eventExist(eventName)) {

            if (_coffresByEvent.containsKey(eventName)) {
                try {
                    SLAPI.save(_coffresByEvent.get(eventName), "plugins/giveitemonevent/" + eventName + ".bin");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////
    // XML PARTS
    // ////////////////////////////////////////////////////////////////////////////////

    boolean eventExist(String eventName) {

        for (models.Event e : this.events) {
            if (e.name.equals(eventName)) {
                return true;
            }
        }
        return false;
    }

    models.Event getEvent(String eventName) {

        for (models.Event e : this.events) {
            if (e.name.equals(eventName)) {
                return e;
            }
        }

        return null;
    }

    public boolean hasItemsForPlayer(Player player) {

        for (models.Event e : this.events) {
            if (e.isAvailableForCurrentDate()) {

                if (!this.PlayerHasReceiveFood(player, e.name) && e.isActive == true) {
                    return true;
                }
            }
        }

        return false;
    }

    public void getItemForPlayer(Player player) {
        for (models.Event e : this.events) {
            if (e.isAvailableForCurrentDate() && !this.PlayerHasReceiveFood(player, e.name) && e.isActive == true) {
                if (_coffresByEvent.containsKey(e.name)) {

                    ArrayList<MyLocation> chestsForEvent = _coffresByEvent.get(e.name);

                    if (chestsForEvent.size() > 0) {

                        boolean playerHasOneObject = false;

                        for (Item item : e.items) {

                            int nbBreadGive = 0;
                            for (MyLocation loc : chestsForEvent) {
                                if (nbBreadGive < item.number) {
                                    Block block = player.getWorld().getBlockAt(loc.getX(), loc.getY(), loc.getZ());

                                    if (block.getState() instanceof ContainerBlock) {
                                        ContainerBlock chest = (ContainerBlock) block.getState();

                                        if (chest != null) {
                                            Inventory inventaire = chest.getInventory();

                                            int nbIci = item.number - nbBreadGive;

                                            for (int i = 0; i < inventaire.getSize(); i++) {

                                                if (nbBreadGive < item.number) {
                                                    ItemStack stack = inventaire.getItem(i);
                                                    if (stack.getType() != Material.AIR) {

                                                        if (stack.getTypeId() == item.objectId) {

                                                            for (int nb = 0; nb < nbIci; nb++) {
                                                                if (stack.getAmount() > 0) {
                                                                    nbBreadGive = nbBreadGive + 1;
                                                                    stack.setAmount(stack.getAmount() - 1);

                                                                    if (stack.getAmount() == 0) {
                                                                        inventaire.setItem(i, null);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (nbBreadGive > 0) {

                                playerHasOneObject = true;

                                this.setPlayerFood(player, e.name);

                                for (int i = 0; i < nbBreadGive; i++) {
                                    ItemStack t = new ItemStack(Material.getMaterial(item.objectId), 1);
                                    Location loc = player.getLocation();
                                    loc.setX(loc.getX());
                                    player.getWorld().dropItem(loc, t);
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + " " + item.errorMsg);
                            }

                        }

                        if (playerHasOneObject) {
                            player.sendMessage(ChatColor.GREEN + "" + e.description);
                        }
                    }

                    else {
                        player.sendMessage("Aucun coffre n'est disponible pour l'évènement: " + e.displayName);
                    }

                } else {
                    player.sendMessage("Aucun coffre n'est disponible pour l'évènement: " + e.displayName);
                }

            }
        }
    }

    private void setPlayerFood(Player player, String eventName) {

        HashMap<Player, String> test = new HashMap<Player, String>();
        test.put(player, eventName);

        _playerFood.add(test);

    }

    private boolean PlayerHasReceiveFood(Player player, String eventName) {

        HashMap<Player, String> test = new HashMap<Player, String>();
        test.put(player, eventName);

        for (HashMap<Player, String> pfood : _playerFood) {
            if (pfood.equals(test)) {
                return true;
            }
        }

        return false;

    }

}
