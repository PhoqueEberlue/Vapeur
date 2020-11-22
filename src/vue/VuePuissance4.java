package vue;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import controleur.ControleurPuissance4;
import puissance4.Colonne;

import java.sql.SQLException;
import java.util.List;

public class VuePuissance4 extends BorderPane {
    
    private ControleurPuissance4 controleurPuissance4;
    private VueVapeur vueVapeur;
    
    private int idJoueur1;
    private int idJoueur2;
    private Label lNomJoueur1;
    private Label lNomJoueur2;
    private Label lNbJetons1;
    private Label lNbJetons2;


    private Label lAffichageMessage;
    
    private static GridPane grille = creerGrille();

    public VuePuissance4 (VueVapeur vueVapeur, int idJoueur1, int idJoueur2) {
        
        this.idJoueur1 = idJoueur1;
        this.idJoueur2 = idJoueur2;

        this.vueVapeur = vueVapeur;
        try{ this.controleurPuissance4 = new ControleurPuissance4(this.vueVapeur, this); }
        catch(SQLException ignore){ System.out.println("Erreur SQL"); }

        this.setPrefSize(this.vueVapeur.getLargeur(), this.vueVapeur.getHauteur());
        Image imgFond;
        try{ imgFond = new Image("./src/img/vuePuissance4/image_fond.png", 1350, 765, false, true); }
        catch(IllegalArgumentException e){ imgFond = new Image("./img/vuePuissance4/image_fond.png", 1350, 765, false, true); }
        this.setBackground(new Background(new BackgroundImage(imgFond, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.setCenter(grille);
        this.setTop(creerBoutons());
        this.setLeft(partieGauche());
        this.setRight(partieDroite());
        this.setBottom(partieBasse());
    }

    public HBox creerBoutons(){
        HBox hbBoutons = new HBox();
        hbBoutons.setSpacing(60);
        hbBoutons.setPadding(new Insets(10));
        hbBoutons.setAlignment(Pos.CENTER);

        for(int i = 0; i < 7; i++){
            Button bouton = new Button("↓");
            bouton.setPrefSize(40, 40);
            bouton.setOnAction(this.controleurPuissance4);
            bouton.setId("" + i);
            bouton.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
            hbBoutons.getChildren().add(bouton);
        }

        return hbBoutons;
    }

    public static GridPane creerGrille(){
        GridPane gridpane = new GridPane();
        gridpane.setStyle("-fx-hgap: 10; -fx-vgap: 10; -fx-alignment: center;");
        for(int i = 0; i < 7; i++){ gridpane.getColumnConstraints().add(new ColumnConstraints(90)); }
        for(int i = 0; i < 6; i++){ gridpane.getRowConstraints().add(new RowConstraints(90)); }

        Image imgFond;
        try{ imgFond = new Image("./src/img/vuePuissance4/grille.png", 710, 610, false, true); }
        catch(IllegalArgumentException e){ imgFond = new Image("./img/vuePuissance4/grille.png", 710, 610, false, true); }
        gridpane.setBackground(new Background(new BackgroundImage(imgFond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

        return gridpane;
    }

    public VBox partieGauche(){
        //le pseudo du joueur 1
        this.lNomJoueur1 = new Label("Joueur1");
        this.lNomJoueur1.setStyle("-fx-text-fill: "+Palette.JAUNE+"; -fx-font-size: 36;");

        //le nombre de jetons restants
        ImageView ivPionJaune = getPionJaune();
        this.lNbJetons1 = new Label("x" + "21");
        this.lNbJetons1.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 30;");
        HBox hbJetons = new HBox(ivPionJaune, this.lNbJetons1);
        hbJetons.setSpacing(20);
        hbJetons.setAlignment(Pos.CENTER_LEFT);

        //un VBox pour contenir le tout
        VBox vbJoueur1 = new VBox(this.lNomJoueur1, hbJetons);
        vbJoueur1.setSpacing(30);
        vbJoueur1.setPadding(new Insets(20));

        return vbJoueur1;
    }

    public VBox partieDroite(){
        //le pseudo du joueur 2
        this.lNomJoueur2 = new Label("Joueur2");
        this.lNomJoueur2.setStyle("-fx-text-fill: "+Palette.ROUGE+"; -fx-font-size: 36;");

        //le nombre de jetons restants
        ImageView ivPionRouge = getPionRouge();
        this.lNbJetons2 = new Label("21" + "x");
        this.lNbJetons2.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 30;");
        HBox hbJetons = new HBox(this.lNbJetons2, ivPionRouge);
        hbJetons.setSpacing(20);
        hbJetons.setAlignment(Pos.CENTER_RIGHT);

        //un VBox pour contenir le tout
        VBox vbJoueur2 = new VBox(this.lNomJoueur2, hbJetons);
        vbJoueur2.setSpacing(30);
        vbJoueur2.setPadding(new Insets(20));
        vbJoueur2.setAlignment(Pos.TOP_RIGHT);

        return vbJoueur2;
    }

    public HBox partieBasse(){
        //un bouton pour déclarer forfait et mettre le jeu en pause
        Button bAbandonner = new Button("GIVE UP");
        bAbandonner.setOnAction(this.controleurPuissance4);
        bAbandonner.setPrefSize(300, 50);
        bAbandonner.setStyle("-fx-background-color: "+Palette.DEFAITE+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
        Button bPause = new Button("PAUSE");
        bPause.setPrefSize(300, 50);
        bPause.setStyle("-fx-background-color: "+Palette.DEFAITE+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");

        //un label pour afficher un timer
        this.lAffichageMessage = new Label();
        this.lAffichageMessage.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 34; -fx-alignment: center;");
        HBox.setHgrow(this.lAffichageMessage, Priority.ALWAYS);
        this.lAffichageMessage.setMaxWidth(Double.MAX_VALUE);

        //un Hbox pour contenir le tout
        HBox hbActions = new HBox(bAbandonner, this.lAffichageMessage, bPause);
        hbActions.setPadding(new Insets(20));
        hbActions.setAlignment(Pos.CENTER);

        return hbActions;
    }
    
    public ImageView getPionJaune(){
        ImageView ivPion = new ImageView();
        try{ ivPion.setImage(new Image("./src/img/vuePuissance4/PionJaune.png")); }
        catch(IllegalArgumentException e){ ivPion.setImage(new Image("./img/vuePuissance4/PionJaune.png")); }
        return ivPion;
    }
    
    public ImageView getPionRouge(){
        ImageView ivPion = new ImageView();
        try{ ivPion.setImage(new Image("./src/img/vuePuissance4/PionRouge.png")); }
        catch(IllegalArgumentException e){ ivPion.setImage(new Image("./img/vuePuissance4/PionRouge.png")); }
        return ivPion;
    }
    
    public int getIdJoueur1(){
        return this.idJoueur1;
    }
    
    public int getIdJoueur2(){
        return this.idJoueur2;
    }
    
    public void updateGrille(List<Colonne> etatpartie){
        this.clearGrille();
        for(int v = 0; v < 7; v++){
            for(int h = 0; h < 6; h++){
                if(etatpartie.get(v).getPion(h).getVraiCouleur().equals("r")){
                    grille.add(getPionRouge(), v, h);
                }
                else if(etatpartie.get(v).getPion(h).getVraiCouleur().equals("j")){
                    grille.add(getPionJaune(), v, h);
                }
            }
        }
    }

    public void clearGrille(){
        grille.getChildren().clear();
    }

    public void changerMessage(String message){
        this.lAffichageMessage.setText(message);
    }

    public void changerNomJoueur1(String nom) {
        this.lNomJoueur1.setText(nom);
    }

    public void changerNomJoueur2(String nom) {
        this.lNomJoueur2.setText(nom);
    }

    public void changerNombreJeton1(int nb) {
        this.lNbJetons1.setText("x" + nb);
    }

    public void changerNombreJeton2(int nb) {
        this.lNbJetons2.setText(nb + "x");
    }

}