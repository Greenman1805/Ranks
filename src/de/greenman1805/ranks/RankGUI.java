package de.greenman1805.ranks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class RankGUI implements Listener {
	Inventory inv;

	public RankGUI(Player p) {
		Rank r = Rank.getPlayerRank(p);
		inv = Bukkit.createInventory(null, 27, "§8▶ §9Rangübersicht §8◀");

		ItemStack gap = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		setItemName(gap, "§f", null);

		for (int i = 0; i < 9; i++) {
			inv.setItem(i, gap);
		}
		for (int i = 18; i < 27; i++) {
			inv.setItem(i, gap);
		}

		inv.setItem(9, createPlayerHead(p, r));
		ItemStack gap1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
		setItemName(gap1, "§6Ränge ▶", null);
		inv.setItem(10, gap1);

		int i = 11;
		for (int k = Rank.ranks.size() - 1; k >= 0; k--) {
			Rank ranks = Rank.ranks.get(k);
			ItemStack item = new ItemStack(ranks.material, 1);
			setItemName(item, ranks.getPrefixColor() + ranks.name, ranks.description);
			inv.setItem(i, item);
			i++;
		}

		ItemStack info = new ItemStack(Material.PAPER, 1);
		setItemName(info, "§6§oWeitere Infos...", null);
		setItemName(gap1, "§6◀ Ränge ", null);
		inv.setItem(16, gap1);
		inv.setItem(17, info);

		
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.plugin);
		
		p.openInventory(inv);
	}

	private static void setItemName(ItemStack item, String name, List<String> lore_list) {
		ItemMeta meta;
		meta = item.getItemMeta();
		meta.setLore(lore_list);
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}

	private ItemStack createPlayerHead(Player p, Rank r) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta skull = (SkullMeta) head.getItemMeta();
		skull.setDisplayName("§7Dein Rang: " + r.getPrefixColor() + r.name);
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add("§6Playtime:");
		int minutes = Playtime.getPlaytime(p.getUniqueId());
		int hours = (int) TimeUnit.HOURS.convert(minutes, TimeUnit.MINUTES);
		minutes = minutes - hours * 60;
		lore.add("§6" + hours + " §7Stunden §6" + minutes + " §7Minuten");
		skull.setLore(lore);
		skull.setOwningPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
		head.setItemMeta(skull);
		return head;
	}

	@EventHandler
	public void clickedOnItemInShop(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getClickedInventory() != null) {
			if (e.getInventory().equals(inv)) {
				if (e.isLeftClick()) {
					if (!e.getCurrentItem().getType().name().equalsIgnoreCase("AIR")) {
						if (e.getCurrentItem().getType() == Material.PAPER) {
							p.sendMessage("§6Ränge: §fhttps://skyshard.de/minecraft/");
						}
					}
				}

				e.setCancelled(true);
			}
		}
	}

}
