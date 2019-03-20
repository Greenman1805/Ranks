package de.greenman1805.ranks;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.nametagedit.plugin.NametagEdit;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RankListener implements Listener {

	public RankListener() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		
		if (!p.hasPermission("ranks.exempt")) {
			Rank r = Rank.getPlayerRank(p);
//			String world = null;
//			Main.perm.playerAddGroup(world, p, r.name);
			PermissionUser pu = PermissionsEx.getUser(p);
			pu.addGroup(r.name);
		}

		String prefix = Rank.getPlayerPrefix(p);
		p.setDisplayName(prefix + p.getName());
		NametagEdit.getApi().setPrefix(p, Rank.getPlayerPrefixColor(p));

		sendInfoOnPlayerLogin(p);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		NametagEdit.getApi().clearNametag(p);
		if (!p.hasPermission("ranks.exempt")) {
			for (String group : Main.chat.getPlayerGroups(p)) {
//				String world = null;
//				Main.perm.playerRemoveGroup(world, p, group);
				PermissionUser pu = PermissionsEx.getUser(p);
				pu.removeGroup(group);
			}
		}
	}

	public void sendInfoOnPlayerLogin(final Player p) {
		Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {

			@Override
			public void run() {
				String displayname = p.getDisplayName();
				double shards = Main.econ.getBalance(p);
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("PlayerInfo");
				out.writeUTF(p.getUniqueId().toString());
				out.writeUTF(displayname);
				out.writeUTF(shards + "");
				p.sendPluginMessage(Main.plugin, Main.PluginChannel, out.toByteArray());
			}

		}, 20);
	}
}
