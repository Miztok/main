package de_die.gfi.viktor.baresic.javafx.jdbc;

public class PlzEinwohnerEintrag {
	String plz;
	String ort;
	String einwohner;
	String quadratkilometer;

	public PlzEinwohnerEintrag(String plz, String ort, String einwohner, String quadratkilometer) {
		this.plz = plz;
		this.ort = ort;
		this.einwohner = einwohner;
		this.quadratkilometer = quadratkilometer;
	}

	@Override
	public String toString() {
		return "PlzEinwohnerEintrag [plz=" + plz + ", ort=" + ort + ", einwohner=" + einwohner + ", quadratkilometer="
				+ quadratkilometer + "]";
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getEinwohner() {
		return einwohner;
	}

	public void setEinwohner(String einwohner) {
		this.einwohner = einwohner;
	}

	public String getQuadratkilometer() {
		return quadratkilometer;
	}

	public void setQuadratkilometer(String quadratkilometer) {
		this.quadratkilometer = quadratkilometer;
	}
}
