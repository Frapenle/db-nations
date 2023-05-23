package org.java.nation.db;

import java.math.BigDecimal;
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
					+ " JOIN continents c2 ON r.continent_id = c2.continent_id "
					+ " WHERE c.name LIKE ? " // ? segnaposto per evitare sql injection
					+ " ORDER  BY c.name;";
			
//			MILESTONE 2
//			System.out.print("Lista delle nazioni ordinate per nome: \n");
			
//			MILESTONE 3
			System.out.print("Inserisci testo per filtrare la ricerca: ");
			String inputSearch = sc.nextLine();
			
			
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				 ps.setString(1, "%" + inputSearch + "%"); // assegno al primo ? il valore inserito dall'utente
				
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
//			BONUS MILESTRONE 4
			System.out.println("Inserisci l'id della nazione per maggiori info: ");
			int inputId = sc.nextInt();
			sc.nextLine();
//			Language SQL
			String sqlLanguage = " SELECT l.`language`"
					+ " FROM countries c "
					+ "JOIN country_languages cl ON c.country_id = cl.country_id "
					+ "JOIN languages l ON cl.language_id = l.language_id "
					+ "WHERE c.country_id = ?;";
			try (PreparedStatement psLanguage = con.prepareStatement(sqlLanguage)) {
			    psLanguage.setInt(1, inputId);
			    
			    try (ResultSet rsLanguage = psLanguage.executeQuery()) {
			        System.out.println("Lingue parlate: ");
			        while (rsLanguage.next()) {
			            String language = rsLanguage.getString(1);
			            System.out.print(language + ", ");
			        }
			    }
			} catch (SQLException ex) {
			    System.err.println("Query per la lingua non valida");
			}

//			More info SQL
			String sqlMoreInfo = " SELECT cs.population, cs.gdp, cs.`year` "
					+ " FROM country_stats cs "
					+ " WHERE cs.country_id = ? "
					+ " ORDER BY cs.`year` DESC  "
					+ " LIMIT 1;";
			try (PreparedStatement psMoreInfo = con.prepareStatement(sqlMoreInfo)) {
			    psMoreInfo.setInt(1, inputId);

			    try (ResultSet rsMoreInfo = psMoreInfo.executeQuery()) {
			        System.out.println("\n\nInformazioni aggiuntive piu recenti: ");
			        while (rsMoreInfo.next()) {
			            int population = rsMoreInfo.getInt(1);
			            BigDecimal gdp = rsMoreInfo.getBigDecimal(2);
			            int year = rsMoreInfo.getInt(3);
			            
			            System.out.println("Popolazione: " + population);
			            System.out.println("GDP: " + gdp);
			            System.out.println("Anno: " + year);
			        }
			    }
			} catch (SQLException ex) {
			    System.err.println("More info non valida");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
