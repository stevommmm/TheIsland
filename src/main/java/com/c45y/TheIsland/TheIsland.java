/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.c45y.TheIsland;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author c45y
 */
public class TheIsland extends JavaPlugin {

    private final IslandEventHandler listener = new IslandEventHandler(this);
    private ConcurrentHashMap<String, List<String>> voteList = new ConcurrentHashMap<String, List<String>>();
    public Block resourceChest;
    public int requiredVotes;
    public int deathCounter = 0;


    @Override
    public void onEnable() {        
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        requiredVotes = getConfig().getInt("vote.required");
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        if (resourceChest != null) {
            setResourceBlock(resourceChest);
        }
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidGen();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        
        if (command.getName().equalsIgnoreCase("vote")) {
            if (args.length > 0) {
                final String target_player = getServer().getPlayer(args[0]).getName();
                if (voteList.containsKey(target_player)) {
                    if (voteList.get(target_player).contains(sender.getName())) {
                        sender.sendMessage(ChatColor.AQUA + "You can't vote more than once");
                        return true;
                    }
                    int currentCount = voteList.get(target_player).size();
                    voteList.get(target_player).add(sender.getName());
                    getServer().broadcastMessage(ChatColor.AQUA + sender.getName() + " also voted to kick " + target_player + ". Current count: " + currentCount);
                    return true;
                } else {
                    voteList.put(target_player, new ArrayList<String>( Arrays.asList(sender.getName())));
                    getServer().broadcastMessage(ChatColor.AQUA + "Vote to kick " + target_player + " started by " + sender.getName() + ".");
                    getServer().getScheduler().runTaskLater(this, new Runnable() {
                        public void run() {
                            if (voteList.get(target_player).size() >= requiredVotes) {
                                getServer().getPlayer(target_player).setWhitelisted(false);
                                getServer().getPlayer(target_player).setBanned(true);
                                getServer().getPlayer(target_player).kickPlayer("You have been voted off this particular island.");
                                getServer().broadcastMessage(ChatColor.AQUA + "Player " + target_player + " has been removed by vote.");
                            } else {
                                getServer().broadcastMessage(ChatColor.AQUA + "Vote to kick " + target_player + " failed with " + voteList.get(target_player).size() + " votes.");
                            }
                            voteList.remove(target_player);
                        }
                    }, 1200);
                    return true;
                }
                
            }
            
        }
        
        if (!(sender.hasPermission("island.op"))) {
            return false;
        }

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("resourcechest")) {
            Block block = player.getTargetBlock( null, 5);
            if (block.getType() == Material.CHEST) {
                setResourceBlock(block);
                resourceChest = block;
                player.sendMessage(ChatColor.GOLD + "Location set for resource chest");
                return true;
            }
        }
        
        return false;
    }
    
    // Helper functions
    
    public boolean shouldReplenish() {
        int maxdeath = getConfig().getInt("resourcechest.replenish");
        
        // Every X deaths we fill the chest
        if (deathCounter >= maxdeath) {
            deathCounter = 0;
            return true;
        }
        
        // Every death we have a chance to fill the chest
        Random rand = new Random();
        int randomNum = rand.nextInt((maxdeath - deathCounter) + 1);
        
        return randomNum == 1;
    }
    
    
    public void setResourceBlock(Block block) {
        if (block != null) {
            getConfig().set("resourcechest.x", block.getLocation().getBlockX());
            getConfig().set("resourcechest.y", block.getLocation().getBlockY());
            getConfig().set("resourcechest.z", block.getLocation().getBlockZ());
            saveConfig();
        }
    }

    /*
     * Append a bunch of items to the resource chest
    */
    public void fillResourceBlock() {
        if (resourceChest != null) {
            if (resourceChest.getType() == Material.CHEST) {
                Chest chest = (Chest) resourceChest.getState();
                chest.getInventory().addItem(new ItemStack(Material.SAPLING, 1));
                chest.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 3));
                chest.getInventory().addItem(new ItemStack(Material.SAND, 1));
                chest.getInventory().addItem(new ItemStack(Material.COAL, 30));
            }
        }
    }
}
