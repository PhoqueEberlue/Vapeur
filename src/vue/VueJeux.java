package vue;

import controleur.ControleurJeux;
import controleur.ControleurPuissance4;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Arrays;

public class VueJeux extends VueBorderPaneParent {
    private ControleurJeux controleurJeux;

    public VueJeux(VueVapeur vueVapeur){
        super(vueVapeur);
        this.controleurJeux = new ControleurJeux(this.vueVapeur, this);
        this.setLeft(partieGauche());
        this.setCenter(partieCentrale());
    }

    public VBox partieGauche(){
        //initialisation de la VBox qui va afficher les jeux
        VBox vbJeux = new VBox();
        vbJeux.setPrefWidth(250);
        vbJeux.setStyle("-fx-background-color: "+Palette.GRIS_2);

        //création des éléments de la liste de jeux
        ArrayList<Button> listeBoutons = new ArrayList<>();
        for(String jeu : Jeux.LISTE_JEUX){
            Button bouton = new Button(jeu);
            bouton.setPrefWidth(250);
            bouton.setPadding(new Insets(10, 10, 10, 10));
            bouton.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_2);
            vbJeux.getChildren().add(bouton);
            listeBoutons.add(bouton);
        }
        //le premier élément est sélectionné par defaut
        listeBoutons.get(0).setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_4);

        return vbJeux;
    }

    public VBox partieCentrale(){
        //header
        ImageView ivPuissance4 = new ImageView();
        try{
            ivPuissance4.setImage(new Image("./src/img/" + Jeux.P4_MINIATURE));
        } catch(IllegalArgumentException e){
            ivPuissance4.setImage(new Image("./img/" + Jeux.P4_MINIATURE));
        }
        Label lTitre = new Label(Jeux.P4_NOM);
        lTitre.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 30; -fx-font-weight: bold;");
        Label lType = new Label(Jeux.P4_TYPE);
        lType.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24;");

        ImageView ivEtoile = new ImageView();
        try{
            ivEtoile.setImage(new Image("./src/img/star.png"));
        } catch(IllegalArgumentException e){
            ivEtoile.setImage(new Image("./img/star.png"));
        }

        Label lNombre = new Label(Jeux.P4_NOMBRE_LIKES);
        lNombre.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");

        HBox hbLikes = new HBox(ivEtoile, lNombre);
        hbLikes.setAlignment(Pos.CENTER_LEFT);
        hbLikes.setSpacing(5);
        VBox vbTitres = new VBox(lTitre, lType, hbLikes);
        vbTitres.setSpacing(10);
        HBox hbHeader = new HBox(ivPuissance4, vbTitres);
        hbHeader.setSpacing(20);

        //description
        Label lDescription = new Label(Jeux.P4_DESCRIPTION);
        lDescription.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18;");
        lDescription.setWrapText(true);

        //captures d'écran
        HBox hbCaptures = new HBox();
        hbCaptures.setSpacing(30);
        for(String capture : Jeux.P4_CAPTURES){
            ImageView ivCapture = new ImageView();
            try{
                ivCapture.setImage(new Image("./src/img/" + capture));
            } catch(IllegalArgumentException e){
                ivCapture.setImage(new Image("./img/" + capture));
            }
            hbCaptures.getChildren().add(ivCapture);
        }

        //les boutons
        Button bRechercher = new Button("SEARCH FOR A GAME");
        bRechercher.setOnAction(this.controleurJeux);
        Button bInviter = new Button("INVITE A FRIEND");
        bInviter.setOnAction(this.controleurJeux);
        bRechercher.setPrefSize(300, 50);
        bRechercher.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
        bInviter.setPrefSize(300, 50);
        bInviter.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
        HBox hbBoutons = new HBox(bRechercher, bInviter);
        hbBoutons.setSpacing(40);
        
        //on met tout dans un VBox
        VBox vbInfo = new VBox(hbHeader, lDescription, hbCaptures, hbBoutons);
        vbInfo.setPadding(new Insets(40, 40, 40, 40));
        vbInfo.setSpacing(20);

        return vbInfo;
    }

}