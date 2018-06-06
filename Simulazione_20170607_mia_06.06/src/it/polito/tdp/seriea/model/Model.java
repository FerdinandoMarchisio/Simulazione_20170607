package it.polito.tdp.seriea.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	private List<Season> stagioni = new LinkedList<Season>();
	private SerieADAO sdao = new SerieADAO();
	private List<Team> squadre = new LinkedList<>() ;
	private Graph<Team,DefaultWeightedEdge> graph;
	private List<SquadraEPunti> punteggi = new LinkedList<SquadraEPunti>();
	
	public List<Season> caricaStagioni() {
		for(Season s :sdao.listSeasons())
			stagioni.add(s);
		if(stagioni!= null)
		return stagioni;
		else
			System.out.println("ERRORE nel metodo caricaStagioni, 'stagioni'==null ");
			return null;
	}

	public void caricaPartiteDellaStagione(Season scelta) {
	
		this.graph=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.graph.removeAllEdges(this.graph.edgeSet());
		this.graph.removeAllVertices(this.graph.vertexSet());
		squadre.removeAll(squadre);
		squadre = sdao.listTeamsSeason(scelta);
		punteggi.removeAll(punteggi);
		for(Team t : squadre) {
			t.setPunti(0);
			graph.addVertex(t);
			SquadraEPunti stemp = new SquadraEPunti(t);
			stemp.setPunti(0);
			punteggi.add(stemp);
		}
		aggiungiArchi(graph,scelta,squadre);
		
		
	}

	private void aggiungiArchi(Graph<Team, DefaultWeightedEdge> graph, Season scelta, List<Team> squadre) {
		//devo generare gli archi orientati e pesati per ogni coppia di partite della stagione
		/* nel DAO posso interrogare il db e farmi restituire una tabella di Match
		 * dopodichè creo l'arco
		 */
		List<Match> partite = new LinkedList<Match>(sdao.listMatchSeason(scelta,squadre));
		for(Match m : partite) {
			// devo aggiungere i grafi orientati
			String risultato = m.getFtr();
			//creo i vari archi col peso giusto
			if(risultato.compareTo("H")==0) {
				//vinto casa, peso 1
			Graphs.addEdge(this.graph , m.getHomeTeam() , m.getAwayTeam() , 1);
				}
			else if(risultato.compareTo("A")==0) {
				//vinto trasferta, peso -1
				Graphs.addEdge(this.graph , m.getHomeTeam() , m.getAwayTeam() , -1);
					
			}
			else if(risultato.compareTo("D")==0) {
				//pareggio, peso 0
				Graphs.addEdge(this.graph , m.getHomeTeam() , m.getAwayTeam() , 0);
					
			}
		}
		this.generaClassifica();
		// a questo punto dovrei aver caricato completamente il grafo
		// genero la classifica
	
		
	}

	public void generaClassifica() {
			
			for(DefaultWeightedEdge e:graph.edgeSet()) {
				
				if(graph.getEdgeWeight(e)>0.0) {
					//Squadra di casa vince
					trovaSquadra(graph.getEdgeSource(e)).vince();
				
					graph.getEdgeSource(e).vince();
					}
				else if(graph.getEdgeWeight(e)<0.0) {
					//Squadra ospite vince
					trovaSquadra(graph.getEdgeTarget(e)).vince();
					
					graph.getEdgeTarget(e).vince();
					}
				else {
					trovaSquadra(graph.getEdgeSource(e)).pareggia();
					trovaSquadra(graph.getEdgeTarget(e)).pareggia();
			
					graph.getEdgeSource(e).pareggia();
					graph.getEdgeTarget(e).pareggia();
				}
				graph.getEdgeSource(e).setTeam("AAAA");
			}
		}
	

	private SquadraEPunti trovaSquadra(Team team) {
		for(SquadraEPunti s:punteggi) {
			if(s.getTeam().getTeam().compareTo(team.getTeam())==0) {
				return s;
			}
		}
		return null;
	}

	public String stampaClassifica() {
		// devo fare il return di una stringa composta da più righe
		// in ogni riga punteggio e squadra
		String daReturn="";
	//	this.squadre.sort(null);
		Collections.sort(punteggi);
		for(SquadraEPunti t:punteggi) {
			if(daReturn.compareTo("")!=0)
				daReturn+=("\n");
			daReturn+=(t.getPunti()+" "+t.getTeam());
		}
		for(Team t:graph.vertexSet())
			System.out.println(t.getTeam()+"  ");
		return daReturn;
	}

	public String stampaClassifica2() {
		String daReturn="";
		List<Team> classifica = new LinkedList<>(graph.vertexSet());
	//	List<Team> classifica = new LinkedList<>(squadre);
			Collections.sort(classifica);
			for(Team t:classifica) {
				if(daReturn.compareTo("")!=0)
					daReturn+=("\n");
				daReturn+=(t.getPunti()+" "+t.getTeam());
			}
			return daReturn;
	}
}
