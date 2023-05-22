package org.java.nation.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/nation";
		String user = "root";
		String password = "root";
		
		
				try(Connection con = DriverManager.getConnection(url, user, password)){
		    
			String sql = " SELECT c.name, c.country_id, r.name, c2.name"
					+ " FROM countries c "
					+ " JOIN regions r ON c.region_id = r.region_id "
					+ " JOIN continents c2 ON r.continent_id = c.region_id "
					+ " ORDER  BY c.name";
			
			System.out.print("Lista delle nazioni ordinate per nome: \n");
			
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					
					while(rs.next()) {
						
						final String countryName = rs.getString(1);
						final int countryId = rs.getInt(2);
						final String regionName = rs.getString(3);
						final String continentName = rs.getString(4);
						
						System.out.println(countryName + " - " + countryId + " - " 
								+ regionName + " - " + continentName);
					}
				}				
			} catch (SQLException ex) {
				System.err.println("Query not well formed");
			}
		} catch (SQLException ex) {
			System.err.println("Error during connection to db");
		}
	}

}
