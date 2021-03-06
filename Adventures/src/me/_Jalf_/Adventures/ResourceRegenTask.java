package me._Jalf_.Adventures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

public class ResourceRegenTask extends BukkitRunnable
{
	public Player player;
	public Main plugin;

	public ResourceRegenTask(Player player, Main plugin) 
	{
		this.player = player;
		this.plugin = plugin;
	}
	@Override
	public void run() 
	{
		if (!PlayerResource.playerNoResource.contains(player.getName()))
		{
			String resourceName = Methods.getPlayerResourceName(player.getName());
			
			int ymlResourceNow = plugin.getSaves().getInt(player.getName() + ".Resource." + resourceName + ".Now");
			
			int resourceNow = ScoreboardHandler.getScore(player.getName(), DisplaySlot.SIDEBAR, Bukkit.getOfflinePlayer(resourceName));
			
			if (ymlResourceNow > 0)
			{
				resourceNow = ymlResourceNow;
				plugin.getSaves().set(player.getName() + ".Resource." + resourceName + ".Now", 0);
				plugin.getSaves();
			}
			
			int resourceMax = plugin.getSaves().getInt(player.getName() + ".Resource." + resourceName + ".Max");

			switch (resourceName)
			{
			case "Mana":
				if (resourceNow != resourceMax)
				{
					resourceNow = resourceNow + plugin.getConfig().getInt("Resource." + resourceName + ".Regen");

					if (resourceNow > resourceMax)
					{
						resourceNow = resourceMax;
					}
				}
				break;
				
			case "Power":
				List<Entity> nearbyEntities = player.getNearbyEntities(15, 15, 15);

				if (nearbyEntities.isEmpty())
				{
					resourceNow = resourceNow - plugin.getConfig().getInt("Resource." + resourceName + ".Degen");
					if (resourceNow < 0)
					{
						resourceNow = 0;
					}
					break;
				}

				for (Entity entity : nearbyEntities)
				{							
					if (entity instanceof LivingEntity)
					{
						List<EntityType> entityTypes = new ArrayList<EntityType>();

						entityTypes.add(EntityType.BLAZE);
						entityTypes.add(EntityType.CAVE_SPIDER);
						entityTypes.add(EntityType.CREEPER);
						entityTypes.add(EntityType.ENDERMAN);
						entityTypes.add(EntityType.GHAST);
						entityTypes.add(EntityType.GIANT);
						entityTypes.add(EntityType.MAGMA_CUBE);
						entityTypes.add(EntityType.PIG_ZOMBIE);
						entityTypes.add(EntityType.SKELETON);
						entityTypes.add(EntityType.SLIME);
						entityTypes.add(EntityType.SPIDER);
						entityTypes.add(EntityType.WITCH);
						entityTypes.add(EntityType.WITHER);
						entityTypes.add(EntityType.ZOMBIE);

						if (entity instanceof Player)
						{
							/////////////////////////// if nearbyEntity is on team scoreboard then no regen
							Player selfPlayer = ((Player) entity).getPlayer();
							if (selfPlayer == player)
							{
								if (resourceNow != resourceMax)
								{
									resourceNow = resourceNow + plugin.getConfig().getInt("Resource." + resourceName + ".Regen");

									if (resourceNow > resourceMax)
									{
										resourceNow = resourceMax;
									}
									break;
								}
							}
						}
						else if (entityTypes.contains(entity.getType()))
						{
							if (resourceNow != resourceMax)
							{
								resourceNow = resourceNow + plugin.getConfig().getInt("Resource." + resourceName + ".Regen");

								if (resourceNow > resourceMax)
								{
									resourceNow = resourceMax;
								}
								break;
							}
						}
						else 
						{
							resourceNow = resourceNow - plugin.getConfig().getInt("Resource." + resourceName + ".Degen");
							if (resourceNow < 0)
							{
								resourceNow = 0;
							}
							break;
						}
					}
					else
					{
						resourceNow = resourceNow - plugin.getConfig().getInt("Resource." + resourceName + ".Degen");
						if (resourceNow < 0)
						{
							resourceNow = 0;
						}
						break;
					}
				}
				break;
				
			case "Fury":
				resourceNow = resourceNow - plugin.getConfig().getInt("Resource." + resourceName + ".Degen");
				if (resourceNow < 0)
				{
					resourceNow = 0;
				}
				break;
				
			case "Concentration":
				if (player.isSneaking())
				{
					if (resourceNow != resourceMax)
					{
						resourceNow = resourceNow + plugin.getConfig().getInt("Resource." + resourceName + ".Regen");

						if (resourceNow > resourceMax)
						{
							resourceNow = resourceMax;
						}
					}
				}
				else
				{
					resourceNow = resourceNow - plugin.getConfig().getInt("Resource." + resourceName + ".Degen");
					if (resourceNow < 0)
					{
						resourceNow = 0;
					}
				}
				break;
				
			case "Energy":
				if (resourceNow != resourceMax)
				{
					resourceNow = resourceNow + plugin.getConfig().getInt("Resource." + resourceName + ".Regen");

					if (resourceNow > resourceMax)
					{
						resourceNow = resourceMax;
					}

				}
				break;
			}
			ScoreboardHandler.changeBoardValue(player.getName(), DisplaySlot.SIDEBAR, Bukkit.getOfflinePlayer(resourceName), resourceNow, resourceName);
		}
		else 
		{
			PlayerResource.playerNoResource.remove(player.getName());
		}
	}
}
