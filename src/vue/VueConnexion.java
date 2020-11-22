package vue;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;
import controleur.ControleurConnexion;

public class VueConnexion extends VueVBoxParent {

    private TextField pseudo;
    private PasswordField motDePasse;
    
    public VueConnexion(VueVapeur vueVapeur){
        //création du VBox avec la bonne apparence
        super(vueVapeur);
        this.controleurConnexion = new ControleurConnexion(this.vueVapeur, this);

        //initialisation des zones de textes
        this.pseudo = new TextField();
        this.motDePasse = new PasswordField();

        //légende des zones de texte
        Label lPseudo = new Label("Username");
        Label lPassword = new Label("Password");
        VBox vbPseudo = new VBox(lPseudo, this.pseudo);
        VBox vbPassword = new VBox(lPassword, this.motDePasse);

        //boutons pour annuler ou se connecter
        HBox hbLogin = new HBox();
        hbLogin.setSpacing(40);
        hbLogin.setAlignment(Pos.CENTER);
        Button bCancel = new Button("QUIT");
        Button bLogin = new Button("LOG IN");
        hbLogin.getChildren().addAll(bCancel, bLogin);

        //barre de séparation
        Rectangle separation = new Rectangle(520, 2);
        separation.setStyle("-fx-fill: "+Palette.GRIS_5);

        //label et bouton pour s'inscrire
        HBox hbAccount = new HBox();
        hbAccount.setSpacing(15);
        hbAccount.setAlignment(Pos.CENTER);
        Label lAccount = new Label("No Vapeur account?");
        Button bAccount = new Button("CREATE AN ACCOUNT");
        hbAccount.getChildren().addAll(lAccount, bAccount);

        //ajout des éléments a VueConnexion
        this.getChildren().addAll(vbPseudo, vbPassword, hbLogin, separation, hbAccount);
        
        //on applique un style pour les différents éléments
        appliquerStyle();
    }

    public String getPseudo() {
        return this.pseudo.getText();
    }

    public String getMotDePasse() {
        return this.motDePasse.getText();
    }

}