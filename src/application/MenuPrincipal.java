package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringTokenizer;

/**
 *
 * @author mh.guelleh
 *
 */
public class MenuPrincipal extends Application {
	private final String strPath = "Donnees" + System.getProperty("file.separator");
	private final String[] strFichiers = {strPath + "f1.txt", strPath + "f2.txt", strPath + "f3.txt"};

	private ArrayList<Voiture> arrVoituresFacile = new ArrayList<>(), arrVoituresMoyen = new ArrayList<>(),
			arrVoituresDiff = new ArrayList<>();

	private ArrayList<ImageVoiture> arrImageVoitures = new ArrayList<>();

	private ImageView[] imageViews, imageViewsLogoGrille;
	private Button btnFacile, btnMoyen, btnDiff;

	private Stage stage;

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;

			for (int i = 0; i < strFichiers.length; i++) lireFichierText(i);
			//for (int i = 0; i < arrAutoFacile.size(); i++) System.out.println(arrAutoFacile.get(i).toString());
			
			/*
			 * String strTypeAuto = strAuto.substring(0, 4), strOrientation = strAuto.substring(5, 6), strCouleur = strAuto.substring(7, strAuto.length() - 4);
				
				if (strAuto.substring(5, 6).equals("H")) 
					System.out.println(file.getName() + " " + strAuto.substring(7, strAuto.length() - 4));
					arrImageVoitures.add(new AutoImage(strTypeAuto, strOrientation, strCouleur));
				else 
					System.out.println(file.getName() + " " + strAuto.substring(7, strAuto.length() - 4));
					arrImageVoitures.add(new AutoImage(strTypeAuto, strOrientation, strCouleur));

				System.out.println();
			 */
			/*
			 * if (strAuto.substring(7, 8).equals("H")) 
					System.out.println(file.getName() + " " + strAuto.substring(9, strAuto.length() - 4));
				else 
					System.out.println(file.getName() + " " + strAuto.substring(9, strAuto.length() - 4));
				
				System.out.println();
			 */
			final File[] files = new File("Images").listFiles();
			for (File file : files) if (file.isFile()) {
				if (file.getName().substring(0, 4).equals("auto"))
					arrImageVoitures.add(new ImageVoiture(file.getName(), // Nom fichier
							file.getName().substring(0, 4), // Type d'auto
							file.getName().substring(5, 6), // Orientation
							file.getName().substring(7, file.getName().length() - 4))); // Couleur


				else if (file.getName().substring(0, 6).equals("camion"))
					arrImageVoitures.add(new ImageVoiture(file.getName(), // Nom fichier
							file.getName().substring(0, 6), // Type d'auto
							file.getName().substring(7, 8), // Orientation
							file.getName().substring(9, file.getName().length() - 4))); // Couleur
			}
			//for (AutoImage i : arrImageVoitures) System.out.println(i);

			final String[] strImagesLogoGrille = {"logo.gif", "grille.gif"}; // Images logo et grille
			imageViewsLogoGrille = new ImageView[strImagesLogoGrille.length];
			for (int i = 0; i < strImagesLogoGrille.length; i++)
				imageViewsLogoGrille[i] = new ImageView(new Image(strImagesLogoGrille[i]));
			imageViewsLogoGrille[1].setId("grille");

			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,600,400);

			final String[] strImages = {"logo.gif", "mini_facile.png", "mini_moyen.png", "mini_diff.png"}; // Mini images acceuil
			imageViews = new ImageView[strImages.length];
			for (int i = 0; i < strImages.length; i++) {
				imageViews[i] = new ImageView(new Image(strImages[i]));

				if (i > 0) imageViews[i].setId(Integer.toString(i));
			}

			imageViews[1].setOnMouseClicked(new actionMouseEvent());
			imageViews[2].setOnMouseClicked(new actionMouseEvent());
			imageViews[3].setOnMouseClicked(new actionMouseEvent());

			// Change la couleur when hover pour les imageview
			imageViews[1].setOnMouseEntered(new enterMouseEvent());
			imageViews[2].setOnMouseEntered(new enterMouseEvent());
			imageViews[3].setOnMouseEntered(new enterMouseEvent());

			imageViews[1].setOnMouseExited(new exitMouseEvent());
			imageViews[2].setOnMouseExited(new exitMouseEvent());
			imageViews[3].setOnMouseExited(new exitMouseEvent());

			TilePane tilePaneLogo = new TilePane();
			tilePaneLogo.setAlignment(Pos.CENTER);
			tilePaneLogo.setPadding(new Insets(15));
			tilePaneLogo.getChildren().add(imageViews[0]);

			HBox hBoxPrincipal = new HBox();
			hBoxPrincipal.setAlignment(Pos.CENTER);
			hBoxPrincipal.setSpacing(60);

			VBox vBoxFacile = new VBox(), vBoxMoyen = new VBox(), vBoxDiff = new VBox();
			vBoxFacile.setSpacing(15);
			vBoxMoyen.setSpacing(15);
			vBoxDiff.setSpacing(15);

			vBoxFacile.setAlignment(Pos.CENTER);
			vBoxMoyen.setAlignment(Pos.CENTER);
			vBoxDiff.setAlignment(Pos.CENTER);

			btnFacile = new Button("Facile");
			btnMoyen = new Button("Moyen");
			btnDiff = new Button("Difficile");

			btnFacile.setId("1");
			btnMoyen.setId("2");
			btnDiff.setId("3");

			btnFacile.setFont(font());
			btnMoyen.setFont(font());
			btnDiff.setFont(font());

			btnFacile.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			btnMoyen.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			btnDiff.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			btnFacile.addEventHandler(MouseEvent.MOUSE_CLICKED, new actionMouseEvent());
			btnMoyen.addEventHandler(MouseEvent.MOUSE_CLICKED, new actionMouseEvent());
			btnDiff.addEventHandler(MouseEvent.MOUSE_CLICKED, new actionMouseEvent());

			// Change la couleur when hover pour les boutons
			btnFacile.setOnMouseEntered(new enterMouseEvent());
			btnMoyen.setOnMouseEntered(new enterMouseEvent());
			btnDiff.setOnMouseEntered(new enterMouseEvent());

			btnFacile.setOnMouseExited(new exitMouseEvent());
			btnMoyen.setOnMouseExited(new exitMouseEvent());
			btnDiff.setOnMouseExited(new exitMouseEvent());

			vBoxFacile.getChildren().addAll(imageViews[1], btnFacile);
			vBoxMoyen.getChildren().addAll(imageViews[2], btnMoyen);
			vBoxDiff.getChildren().addAll(imageViews[3], btnDiff);

			hBoxPrincipal.getChildren().addAll(vBoxFacile, vBoxMoyen, vBoxDiff);

			root.setPadding(new Insets(10));
			root.setTop(tilePaneLogo);
			root.setCenter(hBoxPrincipal);
			root.setAlignment(hBoxPrincipal, Pos.CENTER);
			root.setBackground(background(new Image("jaunenoir.gif")));

			primaryStage.setTitle("Choisir la difficulte du jeu");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

			/*primaryStage.setOnCloseRequest(e -> { //////////////////////// NE PAS OUBLIER DE L'ACTIV? A LA FIN
				e.consume();
				fermerProgramme(primaryStage);
			});*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @author mh.guelleh
	 *
	 */
	private class enterMouseEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			if (event.getSource() instanceof ImageView) { // Change la couleur when hover pour les imageview
				ImageView imageView = (ImageView) event.getSource();
				imageView.setEffect(new DropShadow(75, Color.WHITE));

				if (btnFacile.getId().equals(imageView.getId())) btnFacile.setTextFill(Color.RED);
				else if (btnMoyen.getId().equals(imageView.getId())) btnMoyen.setTextFill(Color.RED);
				else if (btnDiff.getId().equals(imageView.getId())) btnDiff.setTextFill(Color.RED);
			}
			else if (event.getSource() instanceof Button) { // Change la couleur when hover pour les boutons
				Button button = (Button) event.getSource();
				button.setTextFill(Color.RED);

				if (imageViews[1].getId().equals(button.getId())) imageViews[1].setEffect(new DropShadow(75, Color.WHITE));
				else if (imageViews[2].getId().equals(button.getId())) imageViews[2].setEffect(new DropShadow(75, Color.WHITE));
				else if (imageViews[3].getId().equals(button.getId())) imageViews[3].setEffect(new DropShadow(75, Color.WHITE));
			}
		}
	}
	/**
	 *
	 * @author mh.guelleh
	 *
	 */
	private class exitMouseEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			if (event.getSource() instanceof ImageView) { // Change la couleur when hover pour les imageview
				ImageView imageView = (ImageView) event.getSource();
				imageView.setEffect(null);

				if (btnFacile.getId().equals(imageView.getId())) btnFacile.setTextFill(Color.BLACK);
				else if (btnMoyen.getId().equals(imageView.getId())) btnMoyen.setTextFill(Color.BLACK);
				else if (btnDiff.getId().equals(imageView.getId())) btnDiff.setTextFill(Color.BLACK);
			}
			else if (event.getSource() instanceof Button) { // Change la couleur when hover pour les boutons
				Button button = (Button) event.getSource();
				button.setTextFill(Color.BLACK);

				if (imageViews[1].getId().equals(button.getId())) imageViews[1].setEffect(null);
				else if (imageViews[2].getId().equals(button.getId())) imageViews[2].setEffect(null);
				else if (imageViews[3].getId().equals(button.getId())) imageViews[3].setEffect(null);
			}
		}
	}
	/**
	 *
	 * @author mh.guelleh
	 *
	 */
	private class actionMouseEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			String strID = null;

			if (event.getSource() instanceof ImageView) strID = ((ImageView) event.getSource()).getId();
			else if (event.getSource() instanceof Button) strID = ((Button) event.getSource()).getId();

			new MenuJeu(stage, Integer.parseInt(strID), imageViewsLogoGrille, arrVoituresFacile, arrImageVoitures).show();
			stage.close();
		}
	}

	/**
	 *
	 * @param i
	 */
	private void lireFichierText(int i) {
		BufferedReader brFichier = null;

		try {
			brFichier = new BufferedReader(new FileReader(strFichiers[i]));
		}
		catch (FileNotFoundException e){

			e.printStackTrace();
		}

		try {
			String strLigne;

			while ((strLigne = brFichier.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(strLigne, ",");
				Voiture auto = new Voiture(st.nextToken().trim(), st.nextToken().trim(), st.nextToken().trim(),
						st.nextToken().trim(), st.nextToken().trim());

				if (i == 0) arrVoituresFacile.add(auto);
				else if (i == 1) arrVoituresMoyen.add(auto);
				else if (i == 2) arrVoituresDiff.add(auto);
			}

			//for (int i = 0; i < arrDVD.size(); i++) System.out.println(arrDVD.get(i));
		}
		catch (Exception e) {
			//System.out.println(e);
			e.printStackTrace();
		}
	}

	// Autres methodes 
	/**
	 *
	 * @param stage
	 */
	private void fermerProgramme(Stage stage) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Etes-vous sur de vouloir quitter ?");
		//alert.setContentText("Are you ok with this?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			// ... user chose OK
			stage.close();
		} else {
			// ... user chose CANCEL or closed the dialog
		}
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
	private Border border(Color color) {
		return new Border(new BorderStroke(color,
				new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.BEVEL, StrokeLineCap.BUTT, 10, 0, null),
				CornerRadii.EMPTY, new BorderWidths(2)));
	}

	/**
	 * 
	 * @return
	 */
	private Font font() {
		return Font.font("Serif", FontWeight.BOLD, 15);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
