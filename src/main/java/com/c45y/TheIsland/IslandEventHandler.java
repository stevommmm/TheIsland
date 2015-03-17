/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.c45y.TheIsland;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author c45y
 */
public class IslandEventHandler implements Listener {
    
    private final TheIsland plugin;

    
    public IslandEventHandler(TheIsland instance) {
        plugin = instance;
    }
    
    
    /*
     * On world load force our resource chest to be a chest
    */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent event) {
        if ( event.getWorld().getEnvironment() == Environment.NORMAL) {
            plugin.resourceChest = event.getWorld().getBlockAt(
                    plugin.getConfig().getInt("resourcechest.x", 0),
                    plugin.getConfig().getInt("resourcechest.y", 90),
                    plugin.getConfig().getInt("resourcechest.z", 0)
            );
            plugin.resourceChest.setType(Material.CHEST);
        }
        // Make sure we always have a block to spawn on, this is protected onBlockBreak below
        event.getWorld().getSpawnLocation().getBlock().setType(Material.GLOWSTONE);
    }
    
    /* 
     * Protect our two important blocks, spawn and the resource chest
    */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        // Protect the resource chest
        if (event.getBlock().getX() == plugin.resourceChest.getX()
                && event.getBlock().getY() == plugin.resourceChest.getY()
                && event.getBlock().getZ() == plugin.resourceChest.getZ()) {
            event.setCancelled(true);
            return;
        }
        // Protect the spawn block, so we always have somewhere to place users
        if (event.getBlock().getX() == event.getBlock().getWorld().getSpawnLocation().getX()
                && event.getBlock().getY() == event.getBlock().getWorld().getSpawnLocation().getY()
                && event.getBlock().getZ() == event.getBlock().getWorld().getSpawnLocation().getZ()) {
            event.setCancelled(true);
        }
    }
    
    /* 
     * Protect our two important blocks, spawn and the resource chest
    */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.getBlock().getX() == plugin.resourceChest.getX()
                && event.getBlock().getY() == plugin.resourceChest.getY()
                && event.getBlock().getZ() == plugin.resourceChest.getZ()) {
            event.setCancelled(true);
            return;
        }
        // Protect the spawn block, so we always have somewhere to place users
        if (event.getBlock().getX() == event.getBlock().getWorld().getSpawnLocation().getX()
                && event.getBlock().getY() == event.getBlock().getWorld().getSpawnLocation().getY()
                && event.getBlock().getZ() == event.getBlock().getWorld().getSpawnLocation().getZ()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        plugin.deathCounter++;
        if (plugin.shouldReplenish()) {
            plugin.getServer().broadcastMessage(ChatColor.AQUA + "Chest filled!");
            plugin.fillResourceBlock();
        }
        if (plugin.getConfig().getBoolean("player.permdeath")) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                player.setBanned(true);
                player.kickPlayer("You are quite dead now, goodbye.");
            }
        }
    }
    
    
    /* 
     * In the event pvp is disabled, prevent direct damage, as well as arrors and potions
    */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!plugin.getConfig().getBoolean("player.pvp")) {
            return;
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            event.setCancelled(true);
        }
        else if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                event.setCancelled(true);
            }
        }
        else if (event.getDamager() instanceof Potion) {
            Potion potion = (Potion) event.getDamager();
            if (!potion.isSplash()) {
                return;
            }
            // Stop player splash potions hurting other players, messes with witches
            for(PotionEffect potionEffect : potion.getEffects()) {
                if((potionEffect.getType() == PotionEffectType.HARM) ||
                   (potionEffect.getType() == PotionEffectType.POISON) ||
                   (potionEffect.getType() == PotionEffectType.WEAKNESS)) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
    
    
    
}
