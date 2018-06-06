package it.polito.tdp.seriea.model;

public class SquadraEPunti implements Comparable<SquadraEPunti>{
	private Team team;
	private int punti;
	
	
	
	
	public SquadraEPunti(Team team) {
		super();
		this.team = team;
		this.punti = 0;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public int getPunti() {
		return punti;
	}
	public void setPunti(int punti) {
		this.punti = punti;
	}
	public void vince() {
		punti +=3;
	}
	public void pareggia() {
		punti +=1;
	}
	public int compareTo(SquadraEPunti t2) {
		return -(this.punti-t2.getPunti());
		
	}
}
