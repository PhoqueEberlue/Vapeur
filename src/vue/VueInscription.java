package vue;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.geometry.Pos;
import controleur.ControleurConnexion;

public class VueInscription extends VueVBoxParent {

    private TextField pseudo;
    private TextField email;
    private PasswordField motDePasse;
    
    public VueInscription(VueVapeur vueVapeur){
        //création du VBox avec la bonne apparence
        super(vueVapeur);
        this.controleurConnexion = new ControleurConnexion(this.vueVapeur, this);

        //initialisation des zones de textes
        this.pseudo = new TextField();
        this.email = new TextField();
        this.motDePasse = new PasswordField();

        //instructions pour créer un compte
        Label lInfo = new Label("Choose a username less than 10 characters and avoid spaces.");
        lInfo.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 16;");
        lInfo.setMaxWidth(600);
        lInfo.setWrapText(true);

        //légende des zones de texte
        Label lPseudo = new Label("Username");
        Label lEmail = new Label("E-mail address");
        Label lPassword = new Label("Password");
        VBox vbPseudo = new VBox(lPseudo, this.pseudo);
        VBox vbEmail = new VBox(lEmail, this.email);
        VBox vbPassword = new VBox(lPassword, this.motDePasse);
        
        //boutons pour annuler ou se connecter
        HBox hbAccount = new HBox();
        hbAccount.setSpacing(40);
        hbAccount.setAlignment(Pos.CENTER);
        Button bCancel = new Button("CANCEL");
        Button bLogin = new Button("CREATE ACCOUNT");
        hbAccount.getChildren().addAll(bCancel, bLogin);

        //ajout des éléments a VueInscription
        this.getChildren().addAll(lInfo, vbPseudo, vbEmail, vbPassword, hbAccount);
        
        //on applique un style pour les différents éléments
        appliquerStyle();
    }

    public String getPseudo() {
        return this.pseudo.getText();
    }

    public String getEmail() {
        return this.email.getText();
    }

    public String getMotDePasse() {
        return this.motDePasse.getText();
    }

}