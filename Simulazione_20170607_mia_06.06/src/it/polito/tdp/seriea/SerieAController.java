/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSeason"
    private ChoiceBox<Season> boxSeason; // Value injected by FXMLLoader

    @FXML // fx:id="boxTeam"
    private ChoiceBox<Team> boxTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private Model model=new Model();
    
    @FXML
    void handleCarica(ActionEvent event) {
    	txtResult.setText("");
    	Season scelta = boxSeason.getValue();
    	if(scelta!=null) {
    		model.caricaPartiteDellaStagione(scelta);
    		// devo poi chiamare un metodo che mi restituisca o una stringa da stampare 
    		// o una coppia Squadra / punteggio
    		// o una serie di stringhe  Squadra / punteggio
    		
    		String stampa = model.stampaClassifica();
    		String stampa2 = model.stampaClassifica2();
    		
    		txtResult.setText(stampa+"\n*-*-*\n"+stampa2);
    	}
    }

    @FXML
    void handleDomino(ActionEvent event) {
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxTeam != null : "fx:id=\"boxTeam\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";
    }

	public void setModel(Model modello) {
		this.model=modello;
		// devo caricare subito le varie stagioni
		boxSeason.getItems().addAll(model.caricaStagioni());
		
	}
}
