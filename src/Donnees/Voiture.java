package Donnees;

/**
 *
 * @author MH.Guelleh
 *
 */
public class Voiture {
	private String strCouleur, strPosX, strPosY, strLongueur, strOrientation;

	/**
	 *
	 * @param strCouleur
	 * @param strLongueur
	 * @param strPosX
	 * @param strPosY
	 * @param strOrientation
	 */
	public Voiture(String strCouleur, String strLongueur, String strPosX, String strPosY, String strOrientation) {
		this.strCouleur = strCouleur;
		this.strLongueur = strLongueur;
		this.strPosX = strPosX;
		this.strPosY = strPosY;
		this.strOrientation = strOrientation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Auto [strCouleur=" + strCouleur + ", strLongueur="
				+ strLongueur + ", strPosX=" + strPosX + ", strPosY=" + strPosY + ", strOrientation=" + strOrientation + "]";
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

	/**
	 * @return the strPosX
	 */
	public String getStrPosX() {
		return strPosX;
	}
	/**
	 * @param strPosX the strPosX to set
	 */
	public void setStrPosX(String strPosX) {
		this.strPosX = strPosX;
	}

	/**
	 * @return the strPosY
	 */
	public String getStrPosY() {
		return strPosY;
	}
	/**
	 * @param strPosY the strPosY to set
	 */
	public void setStrPosY(String strPosY) {
		this.strPosY = strPosY;
	}

	/**
	 * @return the strLongueur
	 */
	public String getStrLongueur() {
		return strLongueur;
	}
	/**
	 * @param strLongueur the strLongueur to set
	 */
	public void setStrLongueur(String strLongueur) {
		this.strLongueur = strLongueur;
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
}
