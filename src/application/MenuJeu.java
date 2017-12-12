package application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 *
 * @author mh.guelleh
 *
 */
public class MenuJeu extends Stage {
	private Stage primaryStage;
	private int intDifficulte;
	private ImageView[] imageViewsLogoGrille;
	private ArrayList<Voiture> arrVoituresFichier;
	private ArrayList<ImageVoiture> arrImageVoitures;

	private ArrayList<Voiture> arrVoituresGrille = new ArrayList<>();

	private Configuration configuration;
	private ArrayList<Configuration> arrConfiguration = new ArrayList<>();

	private Thread thread;

	private Label lblCompteur;
	private int intCompteur = 0;

	private final int intPosXInitial = 49, intPosYInitial = 69, intTauxEspaceEntreVoitures = 71, intTauxTranslation = 71;
	private int[][] intGrille = new int[6][6];

	/**
	 *
	 * @param primaryStage
	 * @param intDifficulte
	 * @param imageViewsLogoGrille
	 * @param arrVoituresFichier
	 * @param arrImageVoitures
	 */
	public MenuJeu(Stage primaryStage, int intDifficulte, ImageView[] imageViewsLogoGrille,
				   ArrayList<Voiture> arrVoituresFichier, ArrayList<ImageVoiture> arrImageVoitures) {
		// TODO Auto-generated constructor stub
		this.primaryStage = primaryStage;
		this.intDifficulte = intDifficulte;
		this.imageViewsLogoGrille = imageViewsLogoGrille;
		this.arrVoituresFichier = arrVoituresFichier;
		this.arrImageVoitures = arrImageVoitures;

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

		this.setTitle((intDifficulte == 1) ? "Difficulte facile" :
				((intDifficulte == 2) ? "Difficulte moyen" : "Difficulte difficile"));
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
		Pane paneImageView = new Pane();
		paneImageView.getChildren().add(imageViewsLogoGrille[1]);

		// Voitures
		Pane paneAutos = new Pane();

		// Initialisation a zero de la grille
		for (int y = 0; y < intGrille.length; y++)
			for (int x = 0; x < intGrille.length; x++) intGrille[x][y] = 0;

		// Associe chaque image de voiture a chaque voiture du fichier 
		for (Voiture voiture : arrVoituresFichier)
			for (ImageVoiture imageVoiture : arrImageVoitures) {
				// pour la voiture image
				String strCouleurImage = imageVoiture.getStrCouleur(),
						strOrientationImage = imageVoiture.getStrOrientation(),
						strTypeAutoImage = imageVoiture.getStrTypeAuto();

				// pour la voiture fichier
				String strCouleurFichier = voiture.getStrCouleur(),
						strOrientationFichier = voiture.getStrOrientation(),
						strTypeAutoFichier = voiture.getStrLongueur().equals("2") ? "auto" : "camion";

				// verifie si la voiture image j est egal au voiture fichier i 
				if (strCouleurImage.equals(strCouleurFichier) && strOrientationImage.equals(strOrientationFichier) &&
						strTypeAutoImage.equals(strTypeAutoFichier)) {
					// la position que va prendre l'image
					int intImagePosX = Integer.parseInt(voiture.getStrPosX()) * intTauxEspaceEntreVoitures + intPosXInitial + intTauxTranslation * 0,
							intImagePosY = Integer.parseInt(voiture.getStrPosY()) * intTauxEspaceEntreVoitures + intPosYInitial + intTauxTranslation * 0;

					// l'image
					ImageView imageView = new ImageView(new Image(imageVoiture.getStrNomFichier()));
					imageView.setLayoutX(intImagePosX);
					imageView.setLayoutY(intImagePosY);

					// ajout des 1 dans les emplacements pris par les voitures dans la grille 6x6
					intGrille[Integer.parseInt(voiture.getStrPosX())][Integer.parseInt(voiture.getStrPosY())] = 1;
					/*System.out.println(voiture.getStrPosX() + " x, " +
										voiture.getStrPosY() + " y, " + voiture.getStrLongueur() + " longueur, " +
										voiture.getStrCouleur());*/

					// remplissage de la grille 6x6 avec la longueur du reste de la voiture 
					if (strOrientationFichier.equals("H")) // Orientation horizontale
						for (int a = 0; a < Integer.parseInt(voiture.getStrLongueur()); a++) { // reste des pos horizontales de la voiture
							if (voiture.getStrCouleur().equals("rouge"))
								intGrille[Integer.parseInt(voiture.getStrPosX()) + a] // posX de la voiture rouge
										[Integer.parseInt(voiture.getStrPosY())] = 2; // posY de la voiture rouge
							else intGrille[Integer.parseInt(voiture.getStrPosX()) + a] // posX
									[Integer.parseInt(voiture.getStrPosY())] = 1; // posY
						}
					else // Orientation verticale
						for (int b = 0; b < Integer.parseInt(voiture.getStrLongueur()); b++) { // reste des pos verticales de la voiture
							if (voiture.getStrCouleur().equals("rouge"))
								intGrille[Integer.parseInt(voiture.getStrPosX())] // posX de la voiture rouge
										[Integer.parseInt(voiture.getStrPosY()) + b] = 2; // posY de la voiture rouge
							else intGrille[Integer.parseInt(voiture.getStrPosX())] // posX
									[Integer.parseInt(voiture.getStrPosY()) + b] = 1; // posY
						}

					arrVoituresGrille.add(voiture); // ajout des voitures dans la grille

					paneAutos.getChildren().add(imageView); // ajout des images
				}
			}

		// affichage 
		System.out.println();
		System.out.println("Grille " + intDifficulte);
		for (int y = 0; y < intGrille.length; y++) {
			for (int x = 0; x < intGrille.length; x++) System.out.print(intGrille[x][y] + " | ");
			System.out.println("");
		}
		System.out.println("\n\n\n\n\n");

		Voiture voitureRouge = null;
		for (Voiture voiture : arrVoituresGrille)
			if (voiture.getStrCouleur().equals("rouge")) voitureRouge = voiture;
		configuration = new Configuration(intGrille, arrVoituresGrille, voitureRouge, 0, null);

		paneMain.getChildren().addAll(paneImageView, paneAutos);

		vBoxGrille.getChildren().addAll(paneMain);

		return vBoxGrille;
	}

	/**
	 *
	 * @return vBoxDroite Le vBox pour le chronometre et les boutons
	 */
	private VBox vBoxDroite() {
		VBox vBoxDroite = new VBox();
		vBoxDroite.setAlignment(Pos.TOP_CENTER);
		vBoxDroite.setSpacing(110);
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
				btnQuitter = new Button("Retourner au menu");

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
		});

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
		});

		btnQuitter.setFont(font());
		btnQuitter.setAlignment(Pos.CENTER);
		btnQuitter.setOnMouseEntered(new enterMousEvent());
		btnQuitter.setOnMouseExited(new exitMousEvent());
		btnQuitter.setOnAction(t -> {
			t.consume();

			fermerProgramme(this);
		});

		vBoxDroite.getChildren().addAll(lblCompteur, btnSolution, btnReinitialiser, btnQuitter);

		return vBoxDroite;
	}

	/**
	 *Methode pour changer la couleur du texte des boutons quand on hover
	 */
	private class enterMousEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			((Button) event.getSource()).setTextFill(Color.RED);
		}
	}

	/**
	 * Methode pour changer la couleur du texte des boutons quand on hover
	 */
	private class exitMousEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			((Button) event.getSource()).setTextFill(Color.BLACK);
		}
	}

	// Autres methodes 
	/**
	 * Ferme la fenetre MenuJeu et affiche la fenetre MenuPrincipal
	 * @param stage Le primaryStage qui va etre afficher
	 */
	private void fermerProgramme(Stage stage) {
		thread.interrupt();

		primaryStage.show(); //////////////////////// NE PAS OUBLIER DE L'ACTIV? A LA FIN !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		stage.close();
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
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
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
		}
	};
}
