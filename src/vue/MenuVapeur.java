package vue;

import controleur.ControleurMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;

import java.util.ArrayList;

public class MenuVapeur extends HBox{

    private VueVapeur vueVapeur;
    private ControleurMenu controleurMenu;
    private ArrayList<Button> listeOnglets;
    private String[] onglets = {
        "GAMES",
        "SOCIAL",
        "MATCH HISTORY",
        "INVITATIONS",
        "ADMIN"
    };
    private Button bProfil;

    public MenuVapeur(VueVapeur vueVapeur){
        super();
        this.setPrefHeight(90);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(5, 5, 5, 5));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: "+Palette.GRIS_1+"; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 5);");

        this.vueVapeur = vueVapeur;

        //le logo
        ImageView ivLogo = new ImageView();
        try{
            ivLogo.setImage(new Image("./src/img/logoVapeur.png"));
        } catch(IllegalArgumentException e){
            ivLogo.setImage(new Image("./img/logoVapeur.png"));
        }
        this.getChildren().add(ivLogo);

        //les onglets
        this.listeOnglets = new ArrayList<>();
        this.controleurMenu = new ControleurMenu(this.vueVapeur, this.listeOnglets);
        int id = 0;
        for(String onglet : this.onglets){
            Button bouton = new Button(onglet);
            bouton.setId("" + id++);
            bouton.setOnAction(this.controleurMenu);
            bouton.setPrefHeight(80);
            bouton.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_1);
            this.getChildren().add(bouton);
            this.listeOnglets.add(bouton);
        }
        this.listeOnglets.get(0).setStyle("-fx-text-fill: "+Palette.BOUTON+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_1);

        //le bouton admin est retiré si l'utilisateur n'est admin
        if(!(VueVapeur.getClient().getRole().equals("admin"))){
            this.getChildren().remove(this.getChildren().size()-1);
        }

        //le profile
        HBox hbProfil = new HBox();
        hbProfil.setAlignment(Pos.CENTER_RIGHT);
        hbProfil.setSpacing(10);

        Rectangle separation = new Rectangle(2, 80);
        separation.setStyle("-fx-fill: "+Palette.GRIS_5);
        
        ImageView ivUser = new ImageView();
        try{
            ivUser.setImage(new Image("./src/img/defaultUser.png"));
        } catch(IllegalArgumentException e){
            ivUser.setImage(new Image("./img/defaultUser.png"));
        }
        
        this.bProfil = new Button(VueVapeur.getClient().getNomUtilisateur());
        this.bProfil.setPrefHeight(80);
        this.bProfil.setId("" + id);
        this.bProfil.setOnAction(this.controleurMenu);
        this.bProfil.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_1);
        this.listeOnglets.add(bProfil);
        
        hbProfil.getChildren().addAll(separation, ivUser, bProfil);
        HBox.setHgrow(hbProfil, Priority.ALWAYS);
        hbProfil.setMaxWidth(Double.MAX_VALUE);
        this.getChildren().add(hbProfil);
    }

}
