package application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

/**
 *
 * @author mh.guelleh
 *
 */
public class MenuJeu extends Stage {
	// Valeurs initialisees quand le MenuJeu est appele
	private String strDifficulte = null;
	private ImageView[] imageViewsLogoGrille = null;
	private ArrayList<Voiture> arrVoituresFichier = null;
	private ArrayList<ImageVoiture> arrImageVoitures = null;

	private ArrayList<Voiture> arrVoituresGrille = null;

	private LinkedList<Configuration> listConfiguration = null;
	private ArrayList<Configuration> arrConfigurationVisite = null;

	// Pour le compteur
	private Thread thread = null;
	private Label lblCompteur = null;
	private int intCompteur = 0;

	private final int intPosXInitial = 49, intPosYInitial = 69, intTauxEspaceEntreVoitures = 71,
			intTauxTranslation = 71; // utilise quand on effectue des deplacements des arrVoituresFichier.get(i)s dans la grille d'affichage
	private int[][] intGrille = null;

	private int intNumVoitureRouge = 0;

	private Text textSlider;

	/**
	 *
	 * @param strDifficulte
	 * @param imageViewsLogoGrille
	 * @param arrVoituresFichier
	 * @param arrImageVoitures
	 */
	public MenuJeu(String strDifficulte, ImageView[] imageViewsLogoGrille,
				   ArrayList<Voiture> arrVoituresFichier, ArrayList<ImageVoiture> arrImageVoitures) {
		// TODO Auto-generated constructor stub
		this.strDifficulte = strDifficulte;
		this.imageViewsLogoGrille = imageViewsLogoGrille;
		this.arrVoituresFichier = arrVoituresFichier;
		this.arrImageVoitures = arrImageVoitures;

		arrVoituresGrille = new ArrayList<>();
		listConfiguration = new LinkedList<>();
		arrConfigurationVisite = new ArrayList<>();

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root,1200,800);

		TilePane tilePaneLogo = new TilePane();
		tilePaneLogo.setAlignment(Pos.CENTER);
		tilePaneLogo.setPadding(new Insets(15));
		tilePaneLogo.getChildren().add(imageViewsLogoGrille[0]);

		HBox hBoxPrincipal = new HBox();
		//hBoxPrincipal.setAlignment(Pos.BASELINE_LEFT);
		hBoxPrincipal.setSpacing(150);

		hBoxPrincipal.getChildren().addAll(vBoxGrille(), vBoxDroite());

		root.setPadding(new Insets(25));
		root.setTop(tilePaneLogo);
		root.setBottom(hBoxPrincipal);
		//root.setAlignment(hBoxPrincipal, Pos.BASELINE_LEFT);
		root.setBackground(background(new Image("jaunenoir.gif", 1250, 1260, false, false)));

		this.setTitle("Difficulte " + strDifficulte);
		this.setScene(scene);
		this.setResizable(false);
		this.show();

		this.setOnCloseRequest(e -> {
			e.consume();

			fermerProgramme(this);
		});
	}

	/**
	 *
	 * @return vBoxGrille Le VBox pour la grille, son affichage
	 */
	private VBox vBoxGrille() {
		VBox vBoxGrille = new VBox();
		vBoxGrille.setSpacing(15);
		//vBoxGrille.setAlignment(Pos.BASELINE_LEFT);

		Pane paneMain = new Pane();

		// Grille
		Pane paneGrille = new Pane();
		paneGrille.getChildren().add(imageViewsLogoGrille[1]);

		// Voitures
		Pane paneAutos = new Pane();

		// Initialisation a zero de la grille
		intGrille = new int[6][6];
		for (int y = 0; y < intGrille.length; y++)
			for (int x = 0; x < intGrille.length; x++) intGrille[x][y] = 0;

		// Associe chaque image de voiture a chaque voiture du fichier
		for (int i = 0; i < arrVoituresFichier.size(); i++)
			for (ImageVoiture imageVoiture : arrImageVoitures) {
				// pour la voiture image
				String strCouleurImage = imageVoiture.getStrCouleur(),
						strOrientationImage = imageVoiture.getStrOrientation(),
						strTypeAutoImage = imageVoiture.getStrTypeAuto();

				// pour la voiture fichier
				String strCouleurFichier = arrVoituresFichier.get(i).getStrCouleur(),
						strOrientationFichier = arrVoituresFichier.get(i).getStrOrientation(),
						strTypeAutoFichier = arrVoituresFichier.get(i).getStrLongueur().equals("2") ? "auto" : "camion";

				// verifie si la voitureimage j est egal au voiture fichier i
				if (strCouleurImage.equals(strCouleurFichier) && strOrientationImage.equals(strOrientationFichier) &&
						strTypeAutoImage.equals(strTypeAutoFichier)) {
					if (strCouleurFichier.equals("rouge")) intNumVoitureRouge = i+1;

					// la position que va prendre l'image
					int intImagePosX = Integer.parseInt(arrVoituresFichier.get(i).getStrPosX()) * intTauxEspaceEntreVoitures + intPosXInitial + intTauxTranslation * 0,
							intImagePosY = Integer.parseInt(arrVoituresFichier.get(i).getStrPosY()) * intTauxEspaceEntreVoitures + intPosYInitial + intTauxTranslation * 0;

					// l'image
					ImageView imageView = new ImageView(new Image(imageVoiture.getStrNomFichier()));
					imageView.setLayoutX(intImagePosX);
					imageView.setLayoutY(intImagePosY);

					/*System.out.println(arrVoituresFichier.get(i).getStrPosX() + " x, " +
										arrVoituresFichier.get(i).getStrPosY() + " y, " +
										arrVoituresFichier.get(i).getStrLongueur() + " longueur, " +
										arrVoituresFichier.get(i).getStrCouleur());*/

					// remplissage de la grille 6x6 avec les voitures
					if (strOrientationFichier.equals("H")) // Orientation horizontale
						for (int a = 0; a < Integer.parseInt(arrVoituresFichier.get(i).getStrLongueur()); a++) { // reste des pos horizontales de la voiture
							/*if (arrVoituresFichier.get(i).getStrCouleur().equals("rouge"))
								intGrille[Integer.parseInt(arrVoituresFichier.get(i).getStrPosX()) + a] // posX de la voiture rouge
										[Integer.parseInt(arrVoituresFichier.get(i).getStrPosY())] = i+1; // posY de la voiture rouge
							else */intGrille[Integer.parseInt(arrVoituresFichier.get(i).getStrPosX()) + a] // posX
									[Integer.parseInt(arrVoituresFichier.get(i).getStrPosY())] = i+1; // posY
						}
					else // Orientation verticale
						for (int b = 0; b < Integer.parseInt(arrVoituresFichier.get(i).getStrLongueur()); b++) { // reste des pos verticales de la voiture
							/*if (arrVoituresFichier.get(i).getStrCouleur().equals("rouge"))
								intGrille[Integer.parseInt(arrVoituresFichier.get(i).getStrPosX())] // posX de la arrVoituresFichier.get(i) rouge
										[Integer.parseInt(arrVoituresFichier.get(i).getStrPosY()) + b] = i+1; // posY de la arrVoituresFichier.get(i) rouge
							else */intGrille[Integer.parseInt(arrVoituresFichier.get(i).getStrPosX())] // posX
									[Integer.parseInt(arrVoituresFichier.get(i).getStrPosY()) + b] = i+1; // posY
						}

					//intGrille[4][2] = 2; Pos que la voiture rouge doit se trouver pour avoir un combinaison gagnante. Il faut toutefois verifier [5][2] aussi

					arrVoituresGrille.add(arrVoituresFichier.get(i)); // ajout des voiture dans la grille

					paneAutos.getChildren().add(imageView); // ajout des images
				}
			}

		// affichage de la grille
		System.out.println("Grille " + strDifficulte + "\n\nConfiguration initiale");
		afficherGrille(intGrille);

		Configuration configuration = new Configuration(intGrille, arrVoituresGrille, null, 0, null);
		Configuration configuration1 = new Configuration(cloneConfiguration(configuration));
		listConfiguration.add(configuration1);

		paneMain.getChildren().addAll(paneGrille, paneAutos);

		vBoxGrille.getChildren().addAll(paneMain);

		return vBoxGrille;
	}

	/**
	 * Affiche la grille
	 */
	private void afficherGrille(int[][] intGrille) {
		System.out.println("-----------------------");
		for (int y = 0; y < intGrille.length; y++) {
			for (int x = 0; x < intGrille.length; x++) System.out.print(intGrille[x][y] + " | ");
			System.out.println();
		}
		System.out.println("-----------------------\n\n");
		//System.out.println("Numero de la voiture rouge " + intNumVoitureRouge);
	}

	/**
	 *Solution du jeu
	 */
	private void solution() { // METTRE DANS LE EVENTHANDLER DU BOUTON RESOUDRE !!!!!!!!!!!!!!!!!!!!!!!
		while (listConfiguration.size() != 0 /*&&
				intGrille[4][2] != intNumVoitureRouge && intGrille[5][2] != intNumVoitureRouge*/) {
			Configuration configuration = new Configuration(listConfiguration.getFirst());
			listConfiguration.removeFirst();

			//System.out.println(configuration);

			if (configuration.getIntGrille()[4][2] == intNumVoitureRouge &&
					configuration.getIntGrille()[5][2] == intNumVoitureRouge) {
				System.out.println("Solution trouvee");
				break;
			}
			else {
				//System.out.println("Test");
				boolean blnVisite = false;
				for (Configuration configurationVisite : arrConfigurationVisite)
					if (configuration == configurationVisite) blnVisite = true; // Est dans la configuration

				if (!blnVisite) { // Ajoute dans la array si c'est pas dans la config
					arrConfigurationVisite.add(configuration);

					Configuration configurationClone = new Configuration(cloneConfiguration(configuration)); // Cloning la config

					for (int i = 0; i < configurationClone.getArrVoitures().size(); i++) {
						int[][] intGrilleConfigurationActuelle = configuration.getIntGrille();
						Voiture voiture = configurationClone.getArrVoitures().get(i);
						int intNumVoiture = i+1, intPosX = Integer.parseInt(voiture.getStrPosX()),
													intPosY = Integer.parseInt(voiture.getStrPosY()),
													intLongueur = Integer.parseInt(voiture.getStrLongueur());

						if (voiture.getStrOrientation().equals("H")) { // Voiture horizontale

							if (intPosX+intLongueur < 6 && intGrilleConfigurationActuelle[intPosX+intLongueur][intPosY] == 0) {
								System.out.println(voiture);
								voiture.setStrPosX(Integer.toString(intPosX+1));
								for (int m = 0; m < intLongueur; m++) {
									intGrilleConfigurationActuelle[intPosX][intPosY] = 0;
									intGrilleConfigurationActuelle[intPosX+m+1][intPosY] = intNumVoiture;
								}

								System.out.println("Deplacement de la voiture " + voiture.getStrCouleur() + " vers la droite");
								afficherGrille(intGrilleConfigurationActuelle);

								listConfiguration.addLast(new Configuration(intGrilleConfigurationActuelle, configurationClone.getArrVoitures(), voiture, 1, configuration));
							}

							/*if (intPosX-1 >= 0 && intGrilleConfigurationActuelle[intPosX-1][intPosY] == 0) {
								System.out.println(voiture);
								voiture.setStrPosX(Integer.toString(intPosX-1));
								for (int m = 0; m < intLongueur; m++) {
									intGrilleConfigurationActuelle[intPosX+m][intPosY] = 0;
									intGrilleConfigurationActuelle[intPosX+m-1][intPosY] = intNumVoiture;
								}

								System.out.println("Deplacement de la voiture " + voiture.getStrCouleur() + " vers la gauche");
								afficherGrille(intGrilleConfigurationActuelle);

								listConfiguration.addLast(new Configuration(intGrilleConfigurationActuelle, configurationClone.getArrVoitures(), voiture, -1, configuration));
							}*/
						}
						else {
							/*if (intPosY-1 >= 0 && intGrilleConfigurationActuelle[intPosX][intPosY-1] == 0) {
								System.out.println(voiture);
								voiture.setStrPosY(Integer.toString(intPosY-1));
								for (int m = 0; m < intLongueur; m++) {
									intGrilleConfigurationActuelle[intPosX][intPosY+m] = 0;
									intGrilleConfigurationActuelle[intPosX][intPosY+m-1] = intNumVoiture;
								}

								System.out.println("Deplacement de la voiture " + voiture.getStrCouleur() + " vers le haut " + intNumVoiture);
								afficherGrille(intGrilleConfigurationActuelle);

								listConfiguration.addLast(new Configuration(intGrilleConfigurationActuelle, configurationClone.getArrVoitures(), voiture, -1, configuration));
							}*/

							/*if (intPosY+intLongueur < 6 && intGrilleConfigurationActuelle[intPosX][intPosY+intLongueur] == 0) {
								System.out.println(voiture);
								voiture.setStrPosY(Integer.toString(intPosY+1));
								for (int m = 0; m < intLongueur; m++) {
									intGrilleConfigurationActuelle[intPosX][intPosY] = 0;
									intGrilleConfigurationActuelle[intPosX][intPosY+m+1] = intNumVoiture;
								}

								System.out.println("Deplacement de la voiture " + voiture.getStrCouleur() + " vers le bas " + intNumVoiture);
								afficherGrille(intGrilleConfigurationActuelle);

								listConfiguration.addLast(new Configuration(intGrilleConfigurationActuelle, configurationClone.getArrVoitures(), voiture, 1, configuration));
							}*/
						}
					}
				}
			}
		}
	}

	/**
	 *
	 * @return vBoxDroite Le vBox pour le chronometre et les boutons
	 */
	private VBox vBoxDroite() {
		VBox vBoxDroite = new VBox();
		vBoxDroite.setAlignment(Pos.TOP_CENTER);
		vBoxDroite.setSpacing(75);
		vBoxDroite.setPadding(new Insets(15));
		vBoxDroite.setPrefWidth(375);
		vBoxDroite.setBackground(background(Color.SILVER));

		lblCompteur = new Label();
		lblCompteur.setTextAlignment(TextAlignment.CENTER);
		lblCompteur.setAlignment(Pos.TOP_CENTER);
		lblCompteur.setFont(Font.font("Serif", FontWeight.BOLD, FontPosture.REGULAR, 50));
		lblCompteur.setTextFill(Color.GREEN);

		thread = new Thread(runnable);
		thread.start();

		Button btnSolution = new Button("Resoudre la grille"),
				btnReinitialiser = new Button("Reinitialiser la grille"),
				btnQuitter = new Button("Quitter le jeu");

		btnSolution.setMaxWidth(Double.MAX_VALUE);
		btnSolution.setFont(font());
		btnSolution.setAlignment(Pos.CENTER);
		btnSolution.setOnMouseEntered(new enterMousEvent());
		btnSolution.setOnMouseExited(new exitMousEvent());
		btnSolution.setOnAction(t -> {
			t.consume();

			lblCompteur.setTextFill(Color.RED);
			thread.interrupt();

			btnReinitialiser.setDisable(false);
			btnSolution.setDisable(true);

			solution();
		});

		btnReinitialiser.setMaxWidth(Double.MAX_VALUE);
		btnReinitialiser.setDisable(true);
		btnReinitialiser.setFont(font());
		btnReinitialiser.setAlignment(Pos.CENTER);
		btnReinitialiser.setOnMouseEntered(new enterMousEvent());
		btnReinitialiser.setOnMouseExited(new exitMousEvent());
		btnReinitialiser.setOnAction(t -> {
			t.consume();

			intCompteur = 0;
			lblCompteur.setTextFill(Color.GREEN);

			thread = new Thread(runnable);
			thread.start();

			btnReinitialiser.setDisable(true);
			btnSolution.setDisable(false);

			System.out.println("Configuration initiale");
			afficherGrille(intGrille);
		});

		btnQuitter.setMaxWidth(Double.MAX_VALUE);
		btnQuitter.setFont(font());
		btnQuitter.setAlignment(Pos.CENTER);
		btnQuitter.setOnMouseEntered(new enterMousEvent());
		btnQuitter.setOnMouseExited(new exitMousEvent());
		btnQuitter.setOnAction(t -> {
			t.consume();

			//System.out.println("\n\n\n\n\n");

			fermerProgramme(this);
		});

		VBox vBoxSlider = new VBox();
		vBoxSlider.setSpacing(10);
		vBoxSlider.setAlignment(Pos.CENTER);
		vBoxSlider.addEventHandler(MouseEvent.MOUSE_ENTERED, new enterMousEvent());
		vBoxSlider.addEventHandler(MouseEvent.MOUSE_EXITED, new exitMousEvent());

		textSlider = new Text("Vitesse de deplacement des autos");
		textSlider.setFont(font());
		textSlider.setTextAlignment(TextAlignment.CENTER);

		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		Slider slider = new Slider();
		slider.setMaxWidth(Double.MAX_VALUE);

		vBoxSlider.getChildren().addAll(textSlider, slider);

		vBoxDroite.getChildren().addAll(lblCompteur, btnSolution, btnReinitialiser, vBoxSlider, btnQuitter);

		return vBoxDroite;
	}

	/**
	 *Methode pour changer la couleur du texte des boutons quand on hover
	 */
	private class enterMousEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			if (event.getSource() instanceof  Button) ((Button) event.getSource()).setTextFill(Color.RED);
			else textSlider.setFill(Color.RED);
		}
	}

	/**
	 * Methode pour changer la couleur du texte des boutons quand on hover
	 */
	private class exitMousEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			if (event.getSource() instanceof Button) ((Button) event.getSource()).setTextFill(Color.BLACK);
			else textSlider.setFill(Color.BLACK);
		}
	}

	// Autres methodes 
	/**
	 * Quitte le programme
	 * @param stage Le primaryStage qui va etre afficher
	 */
	private void fermerProgramme(Stage stage) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Etes-vous sur de vouloir quitter le jeu ?");
		//alert.setContentText("Are you ok with this?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			// ... user chose OK
			thread.interrupt();
			stage.close();
		}
	}
	/**
	 *
	 * @param image
	 * @return new {@link BackgroundImage} Le background avec l'image desiree
	 */
	private Background background(Image image) {
		return new Background(new BackgroundImage(image,
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT));
	}
	/**
	 *
	 * @param color
	 * @return new {@link BackgroundFill} Le background avec la couleur desiree
	 */
	private Background background(Color color) {
		return new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
	}
	/**
	 *
	 * @param color
	 * @return new {@link Border} Le border avec la couleur desiree
	 */
	private Border border(Color color) {
		return new Border(new BorderStroke(color,
				new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.BEVEL, StrokeLineCap.BUTT, 10, 0, null),
				CornerRadii.EMPTY, new BorderWidths(2)));
	}

	/**
	 *
	 * @return new {@link Font}
	 */
	private Font font() {
		return Font.font("Serif", FontWeight.BOLD, 25);
	}

	/**
	 * Methode runnable pour le chronometre
	 */
	private Runnable runnable = () -> {
		// TODO Auto-generated method stub
		//lblHorloge.setText("");
		//System.out.println(Thread.currentThread().getName());

		try {
			while (!Thread.currentThread().isInterrupted()) {
				Platform.runLater(() -> {
					intCompteur++;

					//System.out.println(intCompteur);

					lblCompteur.setText(String.format("%02d:%02d:%02d",
							intCompteur/3600, (intCompteur%3600)/60, intCompteur%60));
				});

				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//System.out.println(Thread.currentThread().getName() + " arrete");
			//e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	};

	private Configuration cloneConfiguration(Configuration configuration) {
		int[][] intGrille = configuration.getIntGrille();
		int[][] intGrilleClone = new int[intGrille.length][intGrille.length];
		for (int j = 0; j < intGrille.length; j++)
			for (int i = 0; i < intGrille.length; i++) intGrilleClone[i][j] = intGrille[i][j];

		ArrayList<Voiture> arrVoitures = configuration.getArrVoitures();
		ArrayList<Voiture> arrVoituresClone = new ArrayList<>();
		for (Voiture voiture : arrVoitures) arrVoituresClone.add(voiture);

		Voiture voitureDeplace = configuration.getVoitureDeplace();
		Voiture voitureDeplaceClone = ((voitureDeplace == null) ? null : new Voiture(voitureDeplace.getStrCouleur(),
				voitureDeplace.getStrLongueur(), voitureDeplace.getStrPosX(), voitureDeplace.getStrPosY(),
				voitureDeplace.getStrOrientation()));

		int intDeplacement = configuration.getIntDeplacement();
		int intDeplacementClone = new Integer(intDeplacement);

		Configuration configurationPrecedente = configuration.getConfigurationPrecedente();
		Configuration configurationPrecedenteClone = ((configurationPrecedente == null) ? null : new Configuration
				(configurationPrecedente.getIntGrille(), configurationPrecedente.getArrVoitures(),
						configurationPrecedente.getVoitureDeplace(),configurationPrecedente.getIntDeplacement(),
						configurationPrecedente.getConfigurationPrecedente()));

		//System.out.println("Dans la methode clone");

		return new Configuration(intGrilleClone, arrVoituresClone, voitureDeplaceClone,
				intDeplacementClone, configurationPrecedenteClone);
	}
}
