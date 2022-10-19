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
	static List<PlzEinwohnerEintrag>listeDerEinwohnerproPlz=new ArrayList<PlzEinwohnerEintrag>();
	int bevoelkerungAnOrtOderPlz=0;
	double flaecheVonOrtPlzsOderPlz=0.0;
	String ort="";
	@FXML
    private TextField tfPLZOrt;
	@FXML
    private Button buttonClose;

    @FXML
    private Button buttonSearchPlacesOrPostalNumbers;
    
    @FXML
    private Label labelBevoelkerung;

    @FXML
    private Label labelBevoelkerungsdichte;

    @FXML
    private Label labelFlaeche;
    
    @FXML
    private Label labelPlzOrt;

    @FXML
    private CheckBox checkBoxPLZSuchen;

    @FXML
    void handleButtonClose(ActionEvent event) {
    	stage.close();
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
    void handleButtonSearchPlacesOrPostalNumbers(ActionEvent event) throws IOException{
		plzOderOrtDatenAusdruecken();
    }
	private void plzOderOrtDatenAusdruecken() {
		bevoelkerungAnOrtOderPlz=0;
		flaecheVonOrtPlzsOderPlz=0.0;
		ort="";
		if(checkBoxPLZSuchen.isSelected()) {
    		plzDatenBekommenUndAusdrucken();
    	}else {
    		ortDatenBekommenUndAusdrucken();
    	}
		labeslsSetzen(bevoelkerungAnOrtOderPlz, flaecheVonOrtPlzsOderPlz,ort);
	}

	private void ortDatenBekommenUndAusdrucken() {
		for(int i=0;i<listeDerEinwohnerproPlz.size();i++) {
			if(listeDerEinwohnerproPlz.get(i).ort.equals(tfPLZOrt.getText())) {
				int bevoelkerungplz=zugehoerigeEintraegeAusdruckenUndBevoelkerungAddieren(listeDerEinwohnerproPlz.get(i).einwohner, i);
				bevoelkerungAnOrtOderPlz += bevoelkerungplz;
				flaecheVonOrtPlzsOderPlz+=parseStringIntoDouble(listeDerEinwohnerproPlz.get(i).quadratkilometer);
			}else {
				continue;
			}
		}
		ort=tfPLZOrt.getText();
	}

	private void plzDatenBekommenUndAusdrucken() {
		for(int i=0;i<listeDerEinwohnerproPlz.size();i++) {
			if(listeDerEinwohnerproPlz.get(i).plz.equals(tfPLZOrt.getText())) {
				int bevoelkerungplz=zugehoerigeEintraegeAusdruckenUndBevoelkerungAddieren(listeDerEinwohnerproPlz.get(i).einwohner, i);
				bevoelkerungAnOrtOderPlz += bevoelkerungplz;
				flaecheVonOrtPlzsOderPlz+=parseStringIntoDouble(listeDerEinwohnerproPlz.get(i).quadratkilometer);
				ort=listeDerEinwohnerproPlz.get(i).ort;
			}else {
				continue;
			}
		}
	}
	private void labeslsSetzen(int bevoelkerungAnOrtOderPlz, double flaecheVonOrtPlzsOderPlz, String ort2) {
		labelBevoelkerung.setText(""+bevoelkerungAnOrtOderPlz);
		labelFlaeche.setText(""+flaecheVonOrtPlzsOderPlz);
		labelBevoelkerungsdichte.setText(""+((bevoelkerungAnOrtOderPlz*1.0)/flaecheVonOrtPlzsOderPlz));
		labelPlzOrt.setText(ort2);
	}
	private int zugehoerigeEintraegeAusdruckenUndBevoelkerungAddieren(String bevoelkerung, int i) {
		System.out.println(listeDerEinwohnerproPlz.get(i).toString());
		int bevoelkerungAnOrtOderPlz=0;
		bevoelkerungAnOrtOderPlz+=parseStringIntoInteger(bevoelkerung);
		return bevoelkerungAnOrtOderPlz;
	}

	private double parseStringIntoDouble(String quadratkilometer) {
		int kommaPunkt=0;
		for (int i=0;i<quadratkilometer.length();i++) {
			if(quadratkilometer.charAt(i)=='.') {
				kommaPunkt=i;
			}
		}
		String[] quadratkilometerDoubleString=new String [2];
		
		quadratkilometerDoubleString[0]=quadratkilometer.substring(0, kommaPunkt);
		quadratkilometerDoubleString[1]=quadratkilometer.substring(kommaPunkt+1);
		double flaeche=0.0;
		for(int j=0;j<quadratkilometerDoubleString[0].length();j++) {
			flaeche+=Math.pow(10.0, quadratkilometerDoubleString[0].length()-j-1)*(quadratkilometerDoubleString[0].charAt(j)-47);
		}
		for(int k=0;k<quadratkilometerDoubleString[1].length();k++) {
			flaeche+=Math.pow(10.0, -k)*(quadratkilometerDoubleString[1].charAt(k)-47);
		}
		return flaeche;
	}
	private int parseStringIntoInteger(String einwohner) {
		int laenge=einwohner.length();
		int zahl=0;
		for(int i=0;i<laenge;i++) {
			zahl+=Math.pow(10, laenge-i-1)*(einwohner.charAt(i)-47);
		}
		return zahl;   
	}
	private static Connection verbindungAufmachen() throws SQLException {
		Connection c = DriverManager.getConnection(

					"jdbc:mariadb://localhost:3019/newdatabase2",

					"root", "Rkotrab8.7,0");
		return c;
	}

    
	
public static void showDialog() throws IOException, SQLException {
    	
   	 	Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("plz_einwohner.fxml"));
        Parent parent = fxmlLoader.load();
        
        PlzEinwohnerController ctrl=fxmlLoader.getController();
       
        Connection c = verbindungAufmachen();
    	listeDerEinwohnerproPlz = ctrl.erzeugePlzListe(c);
        
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
