package de.greenman1805.ranks;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin {
	public static String PluginChannel = "BungeeCord";
	public static String HomeDir = System.getProperty("user.home");
	public static YamlConfiguration ranks;

	public static Chat chat;
	public static Economy econ;
	public static Permission perm;
	public static String prefix = "§8[§9Ranks§8] §f";
	public static Main plugin;

	public static String host;
	public static String port;
	public static String user;
	public static String password;
	public static String database;

	@Override
	public void onEnable() {
		plugin = this;
		setupConfig();
		getValues();
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, PluginChannel);
		checkDatabase();
		setupChatAndEconomyAndPermission();
		new RankListener();
		getCommand("ranks").setExecutor(new RankCommand());
	}

	private void checkDatabase() {
		if (!MySQL.openConnection()) {
			this.setEnabled(false);
			System.out.println("MySQL connection failed!");
			return;
		}
	}

	private void setupConfig() {
		ranks = YamlConfiguration.loadConfiguration(new File(HomeDir + "//ranks.yml"));
		ranks.addDefault("MySQL.host", "localhost");
		ranks.addDefault("MySQL.port", "3306");
		ranks.addDefault("MySQL.user", "user");
		ranks.addDefault("MySQL.password", "user");
		ranks.addDefault("MySQL.database", "database");
		ranks.options().copyDefaults(true);
		try {
			ranks.save(new File(HomeDir + "//ranks.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getValues() {
		host = ranks.getString("MySQL.host");
		port = ranks.getString("MySQL.port");
		user = ranks.getString("MySQL.user");
		password = ranks.getString("MySQL.password");
		database = ranks.getString("MySQL.database");

		for (String rank : ranks.getConfigurationSection("Ranks").getKeys(false)) {
			int playtime = ranks.getInt("Ranks." + rank + ".playtime");
			List<String> description = ranks.getStringList("Ranks." + rank + ".description");
			Material material = Material.getMaterial(ranks.getString("Ranks." + rank + ".material"));
			new Rank(rank,playtime, description, material);
		}

	}

	@Override
	public void onDisable() {
		MySQL.closeConnection();
	}

	private boolean setupChatAndEconomyAndPermission() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Chat> rspChat = getServer().getServicesManager().getRegistration(Chat.class);
		if (rspChat == null) {
			return false;
		}
		RegisteredServiceProvider<Permission> rspPerm = getServer().getServicesManager().getRegistration(Permission.class);
		if (rspPerm == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rspEconomy = getServer().getServicesManager().getRegistration(Economy.class);
		if (rspEconomy == null) {
			return false;
		}
		chat = rspChat.getProvider();
		econ = rspEconomy.getProvider();
		perm = rspPerm.getProvider();
		return chat != null && econ != null && perm != null;
	}

}
