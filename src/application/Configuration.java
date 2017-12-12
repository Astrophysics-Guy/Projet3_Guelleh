package application;

import java.util.ArrayList;

public class Configuration {
	private int[][] intGrille = new int[6][6];
	private ArrayList<Voiture> arrVoitures = new ArrayList<>();
	private Voiture voitureDeplace;
	private int intDeplacement;
	private Configuration configurationPrecedente;

	public Configuration(int[][] intGrille, ArrayList<Voiture> arrVoitures, Voiture voitureDeplace, int intDeplacement,
						 Configuration configurationPrecedente) {
		this.intGrille = intGrille;
		this.arrVoitures = arrVoitures;
		this.voitureDeplace = voitureDeplace;
		this.intDeplacement = intDeplacement;
		this.configurationPrecedente = configurationPrecedente;
	}

	/**
	 * @return the intGrille
	 */
	public int[][] getIntGrille() {
		return intGrille;
	}

	/**
	 * @param intGrille the intGrille to set
	 */
	public void setIntGrille(int[][] intGrille) {
		this.intGrille = intGrille;
	}

	/**
	 * @return the arrVoitures
	 */
	public ArrayList<Voiture> getArrVoitures() {
		return arrVoitures;
	}

	/**
	 * @param arrVoitures the arrVoitures to set
	 */
	public void setArrVoitures(ArrayList<Voiture> arrVoitures) {
		this.arrVoitures = arrVoitures;
	}

	/**
	 * @return the voitureDeplace
	 */
	public Voiture getVoitureDeplace() {
		return voitureDeplace;
	}

	/**
	 * @param voitureDeplace the voitureDeplace to set
	 */
	public void setVoitureDeplace(Voiture voitureDeplace) {
		this.voitureDeplace = voitureDeplace;
	}

	/**
	 * @return the intDeplacement
	 */
	public int getIntDeplacement() {
		return intDeplacement;
	}

	/**
	 * @param intDeplacement the intDeplacement to set
	 */
	public void setIntDeplacement(int intDeplacement) {
		this.intDeplacement = intDeplacement;
	}

	/**
	 * @return the configurationPrecedente
	 */
	public Configuration getConfigurationPrecedente() {
		return configurationPrecedente;
	}

	/**
	 * @param configurationPrecedente the configurationPrecedente to set
	 */
	public void setConfigurationPrecedente(Configuration configurationPrecedente) {
		this.configurationPrecedente = configurationPrecedente;
	}
}
