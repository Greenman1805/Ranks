package de.greenman1805.ranks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Rank {
	String name;
	int playtime;
	Material material;
	List<String> description;
	public static List<Rank> ranks = new ArrayList<Rank>();

	public Rank(String name, int playtime, List<String> description, Material material) {
		this.name = name;
		this.playtime = playtime;
		this.description = description;
		this.material = material;
		ranks.add(this);
	}

	private static Rank getRankForPlaytime(int playtime) {
		int hours = (int) TimeUnit.HOURS.convert(playtime, TimeUnit.MINUTES);
		for (Rank r : ranks) {
			if (hours >= r.playtime && r.playtime >= 0) {
				return r;
			}
		}
		return null;
	}
	
	
	public String getPrefixColor() {
		String world = null;
		String prefix = Main.chat.getGroupPrefix(world, name).substring(0, 2);
		return ChatColor.translateAlternateColorCodes('&', prefix);
	}

	public static Rank getPlayerRank(Player p) {
		return Rank.getRankForPlaytime(Playtime.getPlaytime(p.getUniqueId()));
	}

	public static String getPlayerPrefixColor(Player p) {
		String prefix = Main.chat.getPlayerPrefix(p).substring(0, 2);
		return ChatColor.translateAlternateColorCodes('&', prefix);
	}

	public static String getPlayerPrefix(Player p) {
		String prefix = Main.chat.getPlayerPrefix(p);
		return ChatColor.translateAlternateColorCodes('&', prefix);
	}

}
