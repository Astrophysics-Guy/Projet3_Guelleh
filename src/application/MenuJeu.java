package application;

import java.util.ArrayList;

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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * 
 * @author mh.guelleh
 *
 */
public class MenuJeu extends Stage {
	private Stage primaryStage;
	
	private ArrayList<Voiture> arrVoituresGrille = new ArrayList<>();
	
	private Configuration configuration;
	
	private Thread thread;
	
	private Label lblCompteur;
	private int intCompteur = 0;
	
	private final int intPosXInitial = 49, intPosYInitial = 69, intTauxEspaceEntreVoitures = 71, intTauxTranslation = 71;
	private int[][] intGrille = new int[6][6];
	
	/**
	 * 
	 * @param primaryStage
	 * @param intDifficulté
	 * @param imageViewsLogoGrille
	 * @param arrVoitures
	 * @param arrImageVoitures
	 */
	public MenuJeu(Stage primaryStage, int intDifficulté, ImageView[] imageViewsLogoGrille, 
					ArrayList<Voiture> arrVoituresFichier, ArrayList<ImageVoiture> arrImageVoitures) {
		// TODO Auto-generated constructor stub
		this.primaryStage = primaryStage;
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root,1200,800);

		TilePane tilePaneLogo = new TilePane();
		tilePaneLogo.setAlignment(Pos.CENTER);
		tilePaneLogo.setPadding(new Insets(15));
		tilePaneLogo.getChildren().add(imageViewsLogoGrille[0]);
		
		HBox hBoxPrincipal = new HBox();
		//hBoxPrincipal.setAlignment(Pos.BASELINE_LEFT);
		hBoxPrincipal.setSpacing(150);
		
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
		for (int y = 0; y < intGrille.length; y++) for (int x = 0; x < intGrille.length; x++) intGrille[x][y] = 0; 
		
		for (int j = 0; j < arrVoituresFichier.size(); j++) 
			for (int i = 0; i < arrImageVoitures.size(); i++) {
				String strCouleurImage = arrImageVoitures.get(i).getStrCouleur(), 
						strOrientationImage = arrImageVoitures.get(i).getStrOrientation(), 
						strTypeAutoImage = arrImageVoitures.get(i).getStrTypeAuto();
				
				String strCouleurFichier = arrVoituresFichier.get(j).getStrCouleur(), 
						strOrientationFichier = arrVoituresFichier.get(j).getStrOrientation(), 
						strTypeAutoFichier = arrVoituresFichier.get(j).getStrLongueur().equals("2") ? "auto" : "camion";
				
				int intImagePosX = Integer.parseInt(arrVoituresFichier.get(j).getStrPosX()) * intTauxEspaceEntreVoitures + intPosXInitial + intTauxTranslation * 0, 
						intImagePosY = Integer.parseInt(arrVoituresFichier.get(j).getStrPosY()) * intTauxEspaceEntreVoitures + intPosYInitial + intTauxTranslation * 0;
				
				ImageView imageView = new ImageView(new Image(arrImageVoitures.get(i).getStrNomFichier()));
				imageView.setLayoutX(intImagePosX);
				imageView.setLayoutY(intImagePosY);
				
				if (strCouleurImage.equals(strCouleurFichier) && strOrientationImage.equals(strOrientationFichier) && strTypeAutoImage.equals(strTypeAutoFichier)) {
					// ajout des 1 dans les emplacements pris par les voitures
					intGrille[Integer.parseInt(arrVoituresFichier.get(j).getStrPosX())][Integer.parseInt(arrVoituresFichier.get(j).getStrPosY())] = 1;
					System.out.println(arrVoituresFichier.get(j).getStrPosX() + " x, " + 
										arrVoituresFichier.get(j).getStrPosY() + " y, " + arrVoituresFichier.get(j).getStrLongueur() + " longueur, " + 
										arrVoituresFichier.get(j).getStrCouleur());
					
					if (strOrientationFichier.equals("H")) 
						for (int a = 0; a < Integer.parseInt(arrVoituresFichier.get(j).getStrLongueur()); a++) // reste des pos horizontales de la voiture
							if (arrVoituresFichier.get(j).getStrCouleur().equals("rouge")) intGrille[Integer.parseInt(arrVoituresFichier.get(j).getStrPosX()) + a] // posX
																									[Integer.parseInt(arrVoituresFichier.get(j).getStrPosY())] = 2; // posY
							else intGrille[Integer.parseInt(arrVoituresFichier.get(j).getStrPosX()) + a] // posX
									[Integer.parseInt(arrVoituresFichier.get(j).getStrPosY())] = 1; // posY
					else 
						for (int b = 0; b < Integer.parseInt(arrVoituresFichier.get(j).getStrLongueur()); b++) // reste des pos verticales de la voiture
							if (arrVoituresFichier.get(j).getStrCouleur().equals("rouge")) intGrille[Integer.parseInt(arrVoituresFichier.get(j).getStrPosX())] // posX
																							[Integer.parseInt(arrVoituresFichier.get(j).getStrPosY()) + b] = 2; // posY
							else intGrille[Integer.parseInt(arrVoituresFichier.get(j).getStrPosX())] // posX
									[Integer.parseInt(arrVoituresFichier.get(j).getStrPosY()) + b] = 1; // posY
					
					arrVoituresGrille.add(arrVoituresFichier.get(j)); // ajout des voitures dans la grille
					
					paneAutos.getChildren().add(imageView); // ajout des images
				}
			}
		
		System.out.println();
		System.out.println("Grille " + intDifficulté);
		for (int y = 0; y < intGrille.length; y++) {
			for (int x = 0; x < intGrille.length; x++) System.out.print(intGrille[x][y] + " | ");
			System.out.println("");
		}
		System.out.println("\n\n\n\n\n");
		
		Voiture voiture = null;
		for (int i = 0; i < arrVoituresGrille.size(); i++) if (arrVoituresGrille.get(i).getStrCouleur().equals("rouge")) voiture = arrVoituresGrille.get(i); 
		configuration = new Configuration(intGrille, arrVoituresGrille, voiture, 0, null);
		
		paneMain.getChildren().addAll(paneImageView, paneAutos);
		
		vBoxGrille.getChildren().addAll(paneMain);
		
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
		
		btnSolution.setFont(font(25));
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
		btnReinitialiser.setFont(font(25));
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
		
		btnQuitter.setFont(font(25));
		btnQuitter.setAlignment(Pos.CENTER);
		btnQuitter.setOnMouseEntered(new enterMousEvent());
		btnQuitter.setOnMouseExited(new exitMousEvent());
		btnQuitter.setOnAction(t -> {
			t.consume();
			
			fermerProgramme(this);
		});
		
		vBoxDroite.getChildren().addAll(lblCompteur, btnSolution, btnReinitialiser, btnQuitter);
		
		hBoxPrincipal.getChildren().addAll(vBoxGrille, vBoxDroite);
		
		root.setPadding(new Insets(25));
		root.setTop(tilePaneLogo);
		root.setBottom(hBoxPrincipal);
		//root.setAlignment(hBoxPrincipal, Pos.BASELINE_LEFT);
		root.setBackground(background(new Image("jaunenoir.gif", 1250, 1260, false, false)));
		
		this.setTitle((intDifficulté == 1) ? "Difficulte facile" : 
			((intDifficulté == 2) ? "Difficulte moyen" : "Difficulte difficile"));
		this.setScene(scene);
		this.setResizable(false);
		this.show();

		this.setOnCloseRequest(e -> { 
			e.consume();
			
			fermerProgramme(this);
		});
	}
	
	private class enterMousEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			((Button) event.getSource()).setTextFill(Color.RED);
		}
	}
	private class exitMousEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			((Button) event.getSource()).setTextFill(Color.BLACK);
		}
	}
	
	// Autres methodes 
	/**
	 * 
	 * @param stage
	 */
	private void fermerProgramme(Stage stage) {
		thread.interrupt();
		
		primaryStage.show(); //////////////////////// NE PAS OUBLIER DE L'ACTIVÉ A LA FIN !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		stage.close();
	}
	/**
	 * 
	 * @param image
	 * @return
	 */
	private Background background(Image image) {
		return new Background(new BackgroundImage(image, 
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, 
				BackgroundSize.DEFAULT));
	}
	/**
	 * 
	 * @param color
	 * @return
	 */
	private Background background(Color color) {
		return new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
	}
	/**
	 * 
	 * @param color
	 * @return
	 */
	private Border border(Color color) {
		return new Border(new BorderStroke(color,
				new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.BEVEL, StrokeLineCap.BUTT, 10, 0, null),
				CornerRadii.EMPTY, new BorderWidths(2)));
	}
	/**
	 * 
	 * @param intSize
	 * @return
	 */
	private Font font(int intSize) {
		return Font.font("Serif", FontWeight.BOLD, intSize);
	}
	
	/**
	 * 
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
