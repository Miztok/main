module de_die.gfi.viktor.baresic.javafx.jdbc {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	requires java.sql;

	opens de_die.gfi.viktor.baresic.javafx.jdbc to javafx.fxml;

	exports de_die.gfi.viktor.baresic.javafx.jdbc;
}