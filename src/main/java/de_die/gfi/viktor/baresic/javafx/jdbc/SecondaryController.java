package de_die.gfi.viktor.baresic.javafx.jdbc;

import java.io.IOException;

import javafx.fxml.FXML;

public class SecondaryController {

	@FXML
	private void switchToPrimary() throws IOException {
		App.setRoot("primary");
	}
}