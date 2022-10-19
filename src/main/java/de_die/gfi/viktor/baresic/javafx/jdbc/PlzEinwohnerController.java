package de_die.gfi.viktor.baresic.javafx.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlzEinwohnerController{
	int bevoelkerungAnOrtOderPlz=0;
	double flaecheVonOrtPlzsOderPlz=0.0;
	List<PlzEinwohnerEintrag>listeDerEinwohnerproPlz=new ArrayList<PlzEinwohnerEintrag>();
	@FXML
    private TextField tfPLZOrt;
	@FXML
    private Button buttonClose;

    @FXML
    private Button buttonOK;
    
    @FXML
    private Label labelBevoelkerung;

    @FXML
    private Label labelBevoelkerungsdichte;

    @FXML
    private Label labelFlaeche;

    @FXML
    private CheckBox checkBoxPLZAnzeigen;

    @FXML
    void handleButtonClose(ActionEvent event) {
    	stage.close();
    }
    @FXML
    void handleButtonGetTable(ActionEvent event) throws SQLException {
    	Connection c = verbindungAufmachen();
    	listeDerEinwohnerproPlz = erzeugePlzListe(c);
    	
    }
	private List<PlzEinwohnerEintrag> erzeugePlzListe(Connection c) throws SQLException {
		Statement stmt=c.createStatement();
    	ResultSet resultSet = stmt.executeQuery("SELECT * FROM plz_einwohner");  	
    	
    	List<PlzEinwohnerEintrag> liste = new ArrayList();
    	while(resultSet.next()) {
    		String plz=resultSet.getString("plz");
    		String ort=resultSet.getString("ort");
    		String einwohner=resultSet.getString("einwohner");
    		String quadratkilometer=resultSet.getString("quadratkilometer");
    		liste.add(new PlzEinwohnerEintrag(plz,ort,einwohner,quadratkilometer));
    	}
   
    	System.out.println("Index 0 - 10: "+  liste.subList(0, 10));
    	return liste;
	}

    @FXML
    void handleButtonOK(ActionEvent event) throws IOException, SQLException {
		plzOderOrtDatenAusdruecken();
    }
    
	private void plzOderOrtDatenAusdruecken() {
		if(checkBoxPLZAnzeigen.isSelected()) {
    		eintragFuerPLZBekommenAusdruckenUndBevoelkerungUndFlaecheBerechnen();
    	}else {
    		eintraegeFuerOrtBekommenAusdruckenUndBevoelkerungUndFlaecheBerechnen();
    	}
		labeslsSetzen(bevoelkerungAnOrtOderPlz, flaecheVonOrtPlzsOderPlz);
		bevoelkerungAnOrtOderPlz=0;
		flaecheVonOrtPlzsOderPlz=0.0;
	}
	private void eintragFuerPLZBekommenAusdruckenUndBevoelkerungUndFlaecheBerechnen() {
		for(int i=0;i<listeDerEinwohnerproPlz.size();i++) {
			if(listeDerEinwohnerproPlz.get(i).plz.equals(tfPLZOrt.getText())) {
				zugehoerigeEintraegeAusdruckenUndBevoelkuerungUndFlaecheAddieren(i);
			}else {
				continue;
			}
		}
	}
	private void eintraegeFuerOrtBekommenAusdruckenUndBevoelkerungUndFlaecheBerechnen() {
		for(int i=0;i<listeDerEinwohnerproPlz.size();i++) {
			if(listeDerEinwohnerproPlz.get(i).ort.equals(tfPLZOrt.getText())) {
				zugehoerigeEintraegeAusdruckenUndBevoelkuerungUndFlaecheAddieren(i);
			}else {
				continue;
			}
		}
	}
	
	private void zugehoerigeEintraegeAusdruckenUndBevoelkuerungUndFlaecheAddieren(int i) {
		bevoelkerungAnOrtOderPlz = zugehoerigeEintraegeAusdruckenUndBevoelkerungAddieren(bevoelkerungAnOrtOderPlz, i);
		flaecheVonOrtPlzsOderPlz+=parseStringIntoDouble(listeDerEinwohnerproPlz.get(i).quadratkilometer);
	}
	
	private void labeslsSetzen(int bevoelkerungAnOrtOderPlz, double flaecheVonOrtPlzsOderPlz) {
		labelBevoelkerung.setText(""+bevoelkerungAnOrtOderPlz);
		labelFlaeche.setText(""+flaecheVonOrtPlzsOderPlz);
		labelBevoelkerungsdichte.setText(""+((bevoelkerungAnOrtOderPlz*1.0)/flaecheVonOrtPlzsOderPlz));
		
	}
	
	private int zugehoerigeEintraegeAusdruckenUndBevoelkerungAddieren(int bevoelkerungAnOrtOderPlz, int i) {
		System.out.println(listeDerEinwohnerproPlz.get(i).toString());
		bevoelkerungAnOrtOderPlz+=parseStringIntoInteger(listeDerEinwohnerproPlz.get(i).einwohner);
		return bevoelkerungAnOrtOderPlz;
	}

	private double parseStringIntoDouble(String quadratkilometer) {
		int kommaPunkt=0;
		kommaPunkt = kommaStelleBerechnen(quadratkilometer, kommaPunkt);
		String[] quadratkilometerDoubleString=new String [2];
		quadratkilometerDoubleString[0]=quadratkilometer.substring(0, kommaPunkt);
		quadratkilometerDoubleString[1]=quadratkilometer.substring(kommaPunkt+1);
		double flaeche=0.0;
		for(int j=0;j<quadratkilometerDoubleString[0].length();j++) {
			flaeche+=Math.pow(10, quadratkilometerDoubleString[0].length()-j-1)*(quadratkilometerDoubleString[0].charAt(j)-47);
		}
		for(int j=0;j<quadratkilometerDoubleString[1].length();j++) {
			flaeche+=Math.pow(10, -j-1)*(quadratkilometerDoubleString[1].charAt(j)-47);
		}
		return flaeche;
	}
	
	private int kommaStelleBerechnen(String quadratkilometer, int kommaPunkt) {
		for (int i=0;i<quadratkilometer.length();i++) {
			if(quadratkilometer.charAt(i)=='.') {
				kommaPunkt=i;
				break;
			}
		}
		return kommaPunkt;
	}
	
	private int parseStringIntoInteger(String einwohner) {
		int laenge=einwohner.length();
		int zahl=0;
		for(int i=0;i<laenge;i++) {
			zahl+=Math.pow(10, laenge-i-1)*(einwohner.charAt(i)-47);
		}
		return zahl;   
	}
	
	private Connection verbindungAufmachen() throws SQLException {
		Connection c = DriverManager.getConnection(

					"jdbc:mariadb://localhost:3019/newdatabase2",

					"root", "Rkotrab8.7,0");
		return c;
	}

    
	
public static void showDialog() throws IOException {
    	
   	 	Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("plz_einwohner.fxml"));
        Parent parent = fxmlLoader.load();
        
        PlzEinwohnerController ctrl=fxmlLoader.getController();
       
        ctrl=fxmlLoader.getController();
        
        ctrl.setStage(stage);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.showAndWait();
        
    }

Stage stage;

public Stage getStage() {
	return stage;
}

public void setStage(Stage stage) {
	initialize();
	this.stage = stage;
}



public void initialize() {
	stage= new Stage();	
}
}
