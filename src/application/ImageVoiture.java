package application;

/**
 *
 * @author MH.Guelleh
 *
 */
public class ImageVoiture {
	private String strNomFichier, strTypeAuto, strOrientation, strCouleur;

	/**
	 *
	 * @param strTypeAuto
	 * @param strOrientation
	 * @param strCouleur
	 */
	public ImageVoiture(String strNomFIchier, String strTypeAuto, String strOrientation, String strCouleur) {
		this.strNomFichier = strNomFIchier;
		this.strTypeAuto = strTypeAuto;
		this.strOrientation = strOrientation;
		this.strCouleur = strCouleur;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AutoImage [strTypeAuto=" + strTypeAuto + ", strOrientation=" + strOrientation + ", strCouleur="
				+ strCouleur + "]";
	}

	/**
	 * @return the strTypeAuto
	 */
	public String getStrNomFichier() {
		return strNomFichier;
	}
	/**
	 * @param strNomFichier the strTypeAuto to set
	 */
	public void setStrNomFichier(String strNomFichier) {
		this.strNomFichier = strNomFichier;
	}

	/**
	 * @return the strTypeAuto
	 */
	public String getStrTypeAuto() {
		return strTypeAuto;
	}
	/**
	 * @param strTypeAuto the strTypeAuto to set
	 */
	public void setStrTypeAuto(String strTypeAuto) {
		this.strTypeAuto = strTypeAuto;
	}

	/**
	 * @return the strOrientation
	 */
	public String getStrOrientation() {
		return strOrientation;
	}
	/**
	 * @param strOrientation the strOrientation to set
	 */
	public void setStrOrientation(String strOrientation) {
		this.strOrientation = strOrientation;
	}

	/**
	 * @return the strCouleur
	 */
	public String getStrCouleur() {
		return strCouleur;
	}
	/**
	 * @param strCouleur the strCouleur to set
	 */
	public void setStrCouleur(String strCouleur) {
		this.strCouleur = strCouleur;
	}
}
