package de.greenman1805.ranks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}

		if (p != null) {

			if (cmd.getName().equalsIgnoreCase("ranks")) {
				if (args.length == 0) {
					new RankGUI(p);
				}
			}
		}
		return false;
	}

}