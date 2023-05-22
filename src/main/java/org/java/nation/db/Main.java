package org.java.nation.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/nation";
		String user = "root";
		String password = "root";
		
		

		try (Scanner sc = new Scanner(System.in);
				Connection con = DriverManager.getConnection(url, user, password)){
		    
			String sql = " SELECT c.name, c.country_id, r.name, c2.name"
					+ " FROM countries c "
					+ " JOIN regions r ON c.region_id = r.region_id "
					+ " JOIN continents c2 ON r.continent_id = c.region_id "
					+ " WHERE c.name LIKE ? "
					+ " ORDER  BY c.name";
			
//			MILESTONE 2
//			System.out.print("Lista delle nazioni ordinate per nome: \n");
			
//			MILESTONE 3
			System.out.print("Inserisci testo per filtrare la ricerca: ");
			String inputSearch = sc.nextLine();
			
			
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				 ps.setString(1, "%" + inputSearch + "%");
				
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
