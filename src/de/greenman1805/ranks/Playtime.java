package de.greenman1805.ranks;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class Playtime {
	
	
	public static int getPlaytime(UUID uuid) {
		MySQL.checkConnection();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT playtime FROM playtime WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt("playtime");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
