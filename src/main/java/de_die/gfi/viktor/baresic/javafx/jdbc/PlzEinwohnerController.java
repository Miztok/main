package de_die.gfi.viktor.baresic.javafx.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PlzEinwohnerController {
	static List<PlzEinwohnerEintrag> listeDerEinwohnerproPlz = new ArrayList<>();
	static List<PlzEinwohnerEintrag> listeDerZuLoeschendenEintraegen = new ArrayList<>();
	static List<PlzEinwohnerEintrag> listeDerZuHinzufuegendenEintraegen = new ArrayList<>();

	int bevoelkerungAnOrtOderPlz = 0;

	double flaecheVonOrtPlzsOderPlz = 0.0;

	String ort = "";

	ObservableList<PlzEinwohnerEintrag> suchErgebnissenListe;

	@FXML
	private TextField tfPLZOrt;

	@FXML
	private TextField tfEinwohnerEintragHinzufuegung;

	@FXML
	private TextField tfFlaechenEintragHinzufuegung;

	@FXML
	private TextField tfOrtEintragHinzufuegung;

	@FXML
	private TextField tfPlzEintragHinzufuegung;

	@FXML
	private Button buttonClose;

	@FXML
	private Button buttonSearchPlacesOrPostalNumbers;

	@FXML
	private Button buttonShowWholeTable;

	@FXML
	private Button buttonEintragHinzufuegen;

	@FXML
	private Button buttonEintragLoeschen;
	
	@FXML
    private Button buttonTerminate;
	
	
	@FXML
	private Label labelBevoelkerung;

	@FXML
	private Label labelBevoelkerungsdichte;

	@FXML
	private Label labelFlaeche;

	@FXML
	private Label labelPlzOrt;

	@FXML
	private TableView<PlzEinwohnerEintrag> tableviewPlzOrtEinwohnerFlaeche;

	@FXML
	private TableColumn<PlzEinwohnerEintrag, String> tbcEinwohner;

	@FXML
	private TableColumn<PlzEinwohnerEintrag, String> tbcOrt;

	@FXML
	private TableColumn<PlzEinwohnerEintrag, String> tbcPlz;

	@FXML
	private TableColumn<PlzEinwohnerEintrag, String> tbcQuadratkilometer;
	
	


	@FXML
	void handleButtonEintragHinzufuegen(ActionEvent event) {
		boolean plzGueltig = untersuchenObNachOrtOderPlzEintraegengesucht(tfPlzEintragHinzufuegung.getText());
		boolean ortGueltig = untersucheObOrtNameGueltig(tfOrtEintragHinzufuegung.getText());
		boolean flaecheGueltig = untersucheObFlaecheEintragGueltig(tfFlaechenEintragHinzufuegung.getText());
		boolean einwohnerZahlGueltig = untersuchenObEinwohnerzahlEingabeGueltigIst(
				tfEinwohnerEintragHinzufuegung.getText());
		if (plzGueltig && ortGueltig && flaecheGueltig && einwohnerZahlGueltig) {
			PlzEinwohnerEintrag einNeuerEintrag = new PlzEinwohnerEintrag(tfOrtEintragHinzufuegung.getText(),
					tfPlzEintragHinzufuegung.getText(), tfEinwohnerEintragHinzufuegung.getText(),
					tfFlaechenEintragHinzufuegung.getText());
			listeDerEinwohnerproPlz.add(einNeuerEintrag);
			listeDerZuHinzufuegendenEintraegen.add(einNeuerEintrag);
		} else {
			
			ungueltigeAngabenAnzeigen(plzGueltig, ortGueltig, einwohnerZahlGueltig, flaecheGueltig);
			
		}
	}

	private void ungueltigeAngabenAnzeigen(boolean plzGueltig, boolean ortGueltig, boolean einwohnerZahlGueltig,
			boolean flaecheGueltig) {
		Alert a = new Alert(AlertType.WARNING);
		
		String ungueltigeEingaben = "Sie haben folgendes ungültig eingegeben: ";

		if (!plzGueltig) {
			ungueltigeEingaben += "PLZ, ";
		}
		if (!ortGueltig) {
			ungueltigeEingaben += "Ort, ";
		}
		if (!flaecheGueltig) {
			ungueltigeEingaben += "Fläche, ";
		}
		if (!einwohnerZahlGueltig) {
			ungueltigeEingaben += "Einwohnerzahl, ";
		}

		ungueltigeEingaben = ungueltigeEingaben.substring(0, ungueltigeEingaben.length()-2); // Entfernen des letzten Kommas
		
		ungueltigeEingaben += ". Bitte geben Sie diese Eingaben (auch) richtig ein.";
		a.setContentText(ungueltigeEingaben);
		a.showAndWait();
	}

	@FXML
	void handleButtonEintragLoeschen(ActionEvent event) {
		int ausgewaehlteReihe = tableviewPlzOrtEinwohnerFlaeche.getSelectionModel().getSelectedIndex();
		String zuLoeschendeEintragOrt = tbcOrt.getCellData(ausgewaehlteReihe);
		String zuLoeschendeEintragPlz = tbcPlz.getCellData(ausgewaehlteReihe);
		for (int i = 0; i < listeDerEinwohnerproPlz.size(); i++) {
			if (listeDerEinwohnerproPlz.get(i).ort.equals(zuLoeschendeEintragOrt)
					&& listeDerEinwohnerproPlz.get(i).plz.equals(zuLoeschendeEintragPlz)) {
				listeDerZuLoeschendenEintraegen.add(listeDerEinwohnerproPlz.get(i));
				listeDerEinwohnerproPlz.remove(i);
			}
		}

	}

	@FXML
    void handleButtonTerminate(ActionEvent event) {
		
		stage.close();
    }

	@FXML
	void handleButtonClose(ActionEvent event) throws SQLException {
		
		veraenderungenInDieDatenbankEintragen();
		
		stage.close();
	}

	private void veraenderungenInDieDatenbankEintragen() throws SQLException {
		Connection c = verbindungAufmachen();
		Statement stmt = c.createStatement();
		int spaltenAnzahl=0;
		ResultSet rs=stmt.executeQuery("SELECT * FROM plz_einwohner");
		while(rs.next()) {
			spaltenAnzahl++;
		}
		for(int i=0;i<listeDerZuLoeschendenEintraegen.size();i++) {
			PreparedStatement pstmt1=c.prepareStatement(" DELETE FROM plz_einwohner WHERE plz=? AND ort=? AND einwohner=? AND quadratkilometer=?");
			pstmt1.setString(1, listeDerZuLoeschendenEintraegen.get(i).plz);
			pstmt1.setString(2, listeDerZuLoeschendenEintraegen.get(i).ort);
			pstmt1.setString(3, listeDerZuLoeschendenEintraegen.get(i).einwohner);
			pstmt1.setString(4, listeDerZuLoeschendenEintraegen.get(i).quadratkilometer);
			for(int j=0;j<spaltenAnzahl;j++) {
				ResultSet rs5=pstmt1.executeQuery();
			}
		}
		PreparedStatement pstmt2=c.prepareStatement("INSERT INTO plz_einwohner ( plz , ort , einwohner , quadratkilometer ) VALUES ( ?, ?, ? , ?)");
		for (int k=0;k<listeDerZuHinzufuegendenEintraegen.size();k++) {
			pstmt2.setString(1, listeDerZuHinzufuegendenEintraegen.get(k).plz);
			pstmt2.setString(2, listeDerZuHinzufuegendenEintraegen.get(k).ort);
			pstmt2.setString(3, listeDerZuHinzufuegendenEintraegen.get(k).einwohner);
			pstmt2.setString(4, listeDerZuHinzufuegendenEintraegen.get(k).quadratkilometer);
			ResultSet rs3=pstmt2.executeQuery();
		}
	}

	@FXML
	void handleButtonShowWholeTable(ActionEvent event) {
		suchErgebnissenListe.clear();
		for (int i = 0; i < listeDerEinwohnerproPlz.size(); i++) {
			PlzEinwohnerEintrag eintragVonPlzDaten = new PlzEinwohnerEintrag(listeDerEinwohnerproPlz.get(i).ort,
					listeDerEinwohnerproPlz.get(i).plz, listeDerEinwohnerproPlz.get(i).einwohner,
					listeDerEinwohnerproPlz.get(i).quadratkilometer);
			suchErgebnissenListe.add(eintragVonPlzDaten);
		}
	}

	private List<PlzEinwohnerEintrag> erzeugePlzListe(Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet resultSet = stmt.executeQuery("SELECT * FROM plz_einwohner");

		List<PlzEinwohnerEintrag> liste = new ArrayList();
		while (resultSet.next()) {
			String plz = resultSet.getString("plz");
			String ort = resultSet.getString("ort");
			String einwohner = resultSet.getString("einwohner");
			String quadratkilometer = resultSet.getString("quadratkilometer");
			liste.add(new PlzEinwohnerEintrag(ort, plz, einwohner, quadratkilometer));
		}

		System.out.println("Index 0 - 10: " + liste.subList(0, 10));
		return liste;
	}

	@FXML
	void handleButtonSearchPlacesOrPostalNumbers(ActionEvent event) throws IOException {
		plzOderOrtDatenAusdruecken();
	}

	private void plzOderOrtDatenAusdruecken() {
		bevoelkerungAnOrtOderPlz = 0;
		flaecheVonOrtPlzsOderPlz = 0.0;
		ort = "";
		String sucheZeichenAbfolge = tfPLZOrt.getText();
		boolean suchtNachPlz = untersuchenObNachOrtOderPlzEintraegengesucht(sucheZeichenAbfolge);
		if (suchtNachPlz) {
			plzDatenBekommenUndAusdrucken(sucheZeichenAbfolge);
		} else {
			ortDatenBekommenUndAusdrucken(sucheZeichenAbfolge);
		}
		labelsSetzen(bevoelkerungAnOrtOderPlz, flaecheVonOrtPlzsOderPlz, ort);
	}

	private boolean untersuchenObNachOrtOderPlzEintraegengesucht(String sucheZeichenAbfolge) {
		if (sucheZeichenAbfolge.length() > 5 || sucheZeichenAbfolge.length() < 5) {
			return false;
		}
		for (int i = 0; i < 5; i++) {
			if (!Character.isDigit(sucheZeichenAbfolge.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private boolean untersuchenObEinwohnerzahlEingabeGueltigIst(String einwohnerZahlEingabe) {
		if (einwohnerZahlEingabe.length() > 10) {
			return false;
		}
		try {
			if (einwohnerZahlEingabe.length() == 10) {
				return Long.valueOf(einwohnerZahlEingabe) <= 2147483647 && Long.valueOf(einwohnerZahlEingabe) > 0;
			} else if (einwohnerZahlEingabe.length() < 10 && einwohnerZahlEingabe.length() > 0) {
				return Long.valueOf(einwohnerZahlEingabe) > 0;
			}
		} catch (InputMismatchException | NumberFormatException ne) {
			return false;
		}
		return false;
	}

	private boolean untersucheObFlaecheEintragGueltig(String flaecheEintrag) {
		try {

			double zahl = Double.valueOf(flaecheEintrag);
			return (zahl > 0.0001 && zahl < 100000);

		} catch (InputMismatchException | NumberFormatException ne) {
			return false;
		}

	}

	private boolean untersucheObOrtNameGueltig(String ortName) {
		if(ortName.length()==0) {
			return false;
		}
		int zaehlerOrtnameAbschnitt = 0;

		for (int i = 0; i < ortName.length(); i++) {
			char theChar = ortName.charAt(i);
			if (zaehlerOrtnameAbschnitt == 0) {
				if (!Character.isUpperCase(theChar)) {
					return false; // Fehler
				}
				zaehlerOrtnameAbschnitt++;
			} else {
				if (theChar == ' ' || theChar == '-') {
					zaehlerOrtnameAbschnitt = 0;
				} else if (theChar == ',') {
					if (ortName.charAt(i + 1) == ' ' || ortName.charAt(i + 1) == '-') {
						continue;
					}
					zaehlerOrtnameAbschnitt = 0;

				} else if (Character.isLowerCase(theChar)) {
					zaehlerOrtnameAbschnitt++;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	private void ortDatenBekommenUndAusdrucken(String sucheZeichenAbfolge) {
		suchErgebnissenListe.clear();
		for (int i = 0; i < listeDerEinwohnerproPlz.size(); i++) {
			if (listeDerEinwohnerproPlz.get(i).ort.equals(sucheZeichenAbfolge)) {
				int bevoelkerungplz = zugehoerigeEintraegeAusdruckenUndBevoelkerungAddieren(
						listeDerEinwohnerproPlz.get(i).einwohner, i);
				bevoelkerungAnOrtOderPlz += bevoelkerungplz;
				flaecheVonOrtPlzsOderPlz += Double.valueOf(listeDerEinwohnerproPlz.get(i).quadratkilometer);
				PlzEinwohnerEintrag eintragVonPlzDaten = new PlzEinwohnerEintrag(listeDerEinwohnerproPlz.get(i).ort,
						listeDerEinwohnerproPlz.get(i).plz, listeDerEinwohnerproPlz.get(i).einwohner,
						listeDerEinwohnerproPlz.get(i).quadratkilometer);
				suchErgebnissenListe.add(eintragVonPlzDaten);
			} else {
				continue;
			}
		}
		ort = tfPLZOrt.getText();
	}

	private void plzDatenBekommenUndAusdrucken(String sucheZeichenAbfolge) {
		suchErgebnissenListe.clear();
		for (int i = 0; i < listeDerEinwohnerproPlz.size(); i++) {
			if (listeDerEinwohnerproPlz.get(i).plz.equals(sucheZeichenAbfolge)) {
				int bevoelkerungplz = zugehoerigeEintraegeAusdruckenUndBevoelkerungAddieren(
						listeDerEinwohnerproPlz.get(i).einwohner, i);
				bevoelkerungAnOrtOderPlz += bevoelkerungplz;
				flaecheVonOrtPlzsOderPlz += Double.valueOf(listeDerEinwohnerproPlz.get(i).quadratkilometer);
				PlzEinwohnerEintrag eintragVonPlzDaten = new PlzEinwohnerEintrag(listeDerEinwohnerproPlz.get(i).ort,
						listeDerEinwohnerproPlz.get(i).plz, listeDerEinwohnerproPlz.get(i).einwohner,
						listeDerEinwohnerproPlz.get(i).quadratkilometer);
				suchErgebnissenListe.add(eintragVonPlzDaten);
				ort = listeDerEinwohnerproPlz.get(i).ort;
			} else {
				continue;
			}
		}
	}

	private void labelsSetzen(int bevoelkerungAnOrtOderPlz, double flaecheVonOrtPlzsOderPlz, String ort2) {
		labelBevoelkerung.setText("" + bevoelkerungAnOrtOderPlz);
		labelFlaeche.setText("" + flaecheVonOrtPlzsOderPlz);
		labelBevoelkerungsdichte.setText("" + ((bevoelkerungAnOrtOderPlz * 1.0) / flaecheVonOrtPlzsOderPlz));
		labelPlzOrt.setText(ort2);
	}

	private int zugehoerigeEintraegeAusdruckenUndBevoelkerungAddieren(String bevoelkerung, int i) {
		System.out.println(listeDerEinwohnerproPlz.get(i).toString());
		int bevoelkerungAnOrtOderPlz = 0;
		bevoelkerungAnOrtOderPlz += Integer.valueOf(bevoelkerung);
		return bevoelkerungAnOrtOderPlz;
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

		PlzEinwohnerController ctrl = fxmlLoader.getController();

		Connection c = verbindungAufmachen();
		listeDerEinwohnerproPlz = ctrl.erzeugePlzListe(c);

		ctrl = fxmlLoader.getController();

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
		stage = new Stage();
		tbcOrt.setCellValueFactory(new PropertyValueFactory<>("ort"));
		tbcPlz.setCellValueFactory(new PropertyValueFactory<>("plz"));
		tbcEinwohner.setCellValueFactory(new PropertyValueFactory<>("einwohner"));
		tbcQuadratkilometer.setCellValueFactory(new PropertyValueFactory<>("quadratkilometer"));
		suchErgebnissenListe = FXCollections.observableArrayList();
		tableviewPlzOrtEinwohnerFlaeche.setItems(suchErgebnissenListe);
		tableviewPlzOrtEinwohnerFlaeche.setPlaceholder(new Label(""));
	}
}
