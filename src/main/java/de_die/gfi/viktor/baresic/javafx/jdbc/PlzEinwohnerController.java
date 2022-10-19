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
    	Connection c = verbindungAufmachen();
		plzOderOrtDatenAusdruecken(c);
    }
	private void plzOderOrtDatenAusdruecken(Connection c) throws SQLException {
		int bevoelkerungAnOrtOderPlz=0;
		double flaecheVonOrtPlzsOderPlz=0.0;
		if(checkBoxPLZAnzeigen.isSelected()) {
    		for(int i=0;i<listeDerEinwohnerproPlz.size();i++) {
    			if(listeDerEinwohnerproPlz.get(i).plz.equals(tfPLZOrt.getText())) {
    				System.out.println(listeDerEinwohnerproPlz.get(i).toString());
    			}else {
    				continue;
    			}
    		}
    	}else {
    		for(int i=0;i<listeDerEinwohnerproPlz.size();i++) {
    			if(listeDerEinwohnerproPlz.get(i).ort.equals(tfPLZOrt.getText())) {
    				System.out.println(listeDerEinwohnerproPlz.get(i).toString());
    			}else {
    				continue;
    			}
    		}
    	}
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
