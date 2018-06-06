package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Team(res.getString("team"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Team> listTeamsSeason(Season stagione) {
		String sql = "SELECT DISTINCT HomeTeam FROM matches  WHERE Season = ? ORDER BY HomeTeam" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, stagione.getSeason());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Team(res.getString("HomeTeam"))) ;
	//			System.out.println(res.getString("HomeTeam"));
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	
	
	public List<Match> listMatchSeason(Season stagione, List<Team> squadre) {
		List<Match> result = new LinkedList<Match>();
		String sql = "SELECT * FROM matches  WHERE Season = ? ORDER BY HomeTeam" ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, stagione.getSeason());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
// int id, Season season, String div, LocalDate date, Team homeTeam, Team awayTeam, int fthg, int ftag, String ftr
				int id = res.getInt("match_id");
				String div = res.getString("Div");
				LocalDate date = res.getDate("Date").toLocalDate();
				int fthg = res.getInt("FTHG");
				int ftag = res.getInt("FTAG");
				String ftr = res.getString("FTR");
				String stringSeason = res.getString("Season");
				String stringHomeTeam = res.getString("HomeTeam");
				String stringAwayTeam = res.getString("AwayTeam");
				
				Season season = getSeasonByString(stringSeason);
				Team homeTeam = getTeamByString(stringHomeTeam,season);
				Team awayTeam = getTeamByString(stringAwayTeam,season);
				Match mtemp = new Match(id,season,div,date,homeTeam,awayTeam,fthg,ftag,ftr);
				result.add( mtemp) ;
	//			System.out.println(res.getString("HomeTeam"));
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		
//		if(match!=null)
//		return match;
//		else {
//			System.out.println("ERRORE nel metodo listMatchSeason");
//		return null;		}
				
	}

	private Team getTeamByString(String string, Season season) {
		for(Team t: this.listTeamsSeason(season)) {
			if(t.getTeam().compareTo(string)==0){
				return t;
			}
		}
		return null;
	}

	private Season getSeasonByString(String stringSeason) {
		for(Season s: this.listSeasons()) {
			if(s.getSeason()==Integer.parseInt(stringSeason))
				return s;
		}
		return null;
	}

}
