package vue;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import controleur.ControleurProfil;
import jdbc.PartieJDBC;
import modele.Partie;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

public class VueProfil extends VueBorderPaneParent {

    private ControleurProfil controleurProfil;
    
    private TextField tfPseudo;
    private TextField tfEmail;
    private PasswordField pfMotDePasse;
    private Button bEditer;
    private Button bValider;

    private Label lstat1;
    private Label lstat2;
    private Label lstat3;
    private Label lstat4;

    public VueProfil(VueVapeur vueVapeur){
        super(vueVapeur);
        
        this.controleurProfil = new ControleurProfil(this.vueVapeur, this);

        HBox hbProfil = new HBox(ficheProfil(), ficheStatistiques());
        hbProfil.setPadding(new Insets(20));
        hbProfil.setSpacing(40);
        this.setCenter(hbProfil);
    }

    public VBox ficheProfil(){
        Label lTitre = new Label("Your profile");
        lTitre.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 26; -fx-font-weight: bold;");
        
        ImageView ivPP = new ImageView();
        try{
            ivPP.setImage(new Image("./src/img/" + VueVapeur.getClient().getAvatarUrl()));
        } catch(IllegalArgumentException e){
            ivPP.setImage(new Image("./img/" + VueVapeur.getClient().getAvatarUrl()));
        }
        Button bImage = new Button("UPLOAD YOUR PICTURE");
        bImage.setOnAction(this.controleurProfil);
        bImage.setDisable(true);
        bImage.setPrefSize(300, 50);
        bImage.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
        HBox hbPP = new HBox(ivPP, bImage);
        hbPP.setSpacing(20);
        hbPP.setAlignment(Pos.CENTER_LEFT);

        Label lPseudo = new Label("Username");
        lPseudo.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
        this.tfPseudo = new TextField(VueVapeur.getClient().getNomUtilisateur());
        this.tfPseudo.setStyle("-fx-background-color: "+Palette.GRIS_4+"; -fx-border-color: "+Palette.TEXTE+"; -fx-border-width: 1 1 1 1; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
        this.tfPseudo.setDisable(true);
        VBox vbPseudo = new VBox(lPseudo, this.tfPseudo);
        vbPseudo.setSpacing(10);

        Label lEmail = new Label("E-mail address");
        lEmail.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
        this.tfEmail = new TextField(VueVapeur.getClient().getEmail());
        this.tfEmail.setStyle("-fx-background-color: "+Palette.GRIS_4+"; -fx-border-color: "+Palette.TEXTE+"; -fx-border-width: 1 1 1 1; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
        this.tfEmail.setDisable(true);
        VBox vbEmail = new VBox(lEmail, this.tfEmail);
        vbEmail.setSpacing(10);

        Label lMotDePasse = new Label("Password");
        lMotDePasse.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
        this.pfMotDePasse = new PasswordField();
        this.pfMotDePasse.setStyle("-fx-background-color: "+Palette.GRIS_4+"; -fx-border-color: "+Palette.TEXTE+"; -fx-border-width: 1 1 1 1; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
        this.pfMotDePasse.setDisable(true);
        VBox vbMotDePasse = new VBox(lMotDePasse, this.pfMotDePasse);
        vbMotDePasse.setSpacing(10);

        this.bEditer = new Button("EDIT YOUR PROFILE");
        this.bEditer.setOnAction(this.controleurProfil);
        this.bEditer.setPrefSize(300, 50);
        this.bEditer.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
        this.bValider = new Button("VALIDATE");
        this.bValider.setOnAction(this.controleurProfil);
        this.bValider.setPrefSize(300, 50);
        this.bValider.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
        this.bValider.setDisable(true);
        HBox hbBoutons = new HBox(this.bEditer, this.bValider);
        hbBoutons.setSpacing(40);

        Button bDeconnexion = new Button("LOG OUT");
        bDeconnexion.setOnAction(this.controleurProfil);
        bDeconnexion.setPrefSize(250, 50);
        bDeconnexion.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");

        VBox vbProfil = new VBox(lTitre, hbPP, vbPseudo, vbEmail, vbMotDePasse, hbBoutons, bDeconnexion);
        vbProfil.setPrefWidth(this.vueVapeur.getLargeur() / 2);
        vbProfil.setAlignment(Pos.TOP_LEFT);
        vbProfil.setPadding(new Insets(40, 40, 40, 40));
        vbProfil.setSpacing(20);
        vbProfil.setStyle("-fx-background-color: "+Palette.GRIS_3+"; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 5, 5);");

        return vbProfil;
    }

    public VBox ficheStatistiques(){
        Label lTitre = new Label("Your statistics");
        try {
            int nb_win = PartieJDBC.getNbWin(VueVapeur.getClient().getIdUtilisateur());
            int nb_lost = PartieJDBC.getNbLose(VueVapeur.getClient().getIdUtilisateur());
            int nb_party = PartieJDBC.getNbPartie(VueVapeur.getClient().getIdUtilisateur());
            double winrate = 0;

            if (nb_party>0){
                winrate = (nb_win/nb_party)*100;
            }

            this.lstat1 = new Label("Number of games played: " + nb_party);
            this.lstat2 = new Label("Number of games won: " + nb_win);
            this.lstat3 = new Label("Number of games lost: " + nb_lost);
            this.lstat4 = new Label("Winrate: " + winrate);
            VBox vbStats = new VBox(lTitre, this.lstat1, this.lstat2, this.lstat3, this.lstat4);

            vbStats.setPrefWidth(this.vueVapeur.getLargeur() / 2);
            vbStats.setAlignment(Pos.TOP_LEFT);
            vbStats.setPadding(new Insets(40, 40, 40, 40));
            vbStats.setSpacing(20);
            vbStats.setStyle("-fx-background-color: "+Palette.GRIS_3+"; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 5, 5);");

            for(Node label : vbStats.getChildren()){
                ((Label)label).setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            }
            lTitre.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 26; -fx-font-weight: bold;");
            return vbStats;
        }catch (SQLException ignore) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Exception System");
            alert2.setHeaderText("Don't find connection with database.");
            alert2.setContentText("You probably did not connect your account to the platform.");

            Exception ex = new SQLException("You probably did not connect your account to the platform.");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert2.getDialogPane().setExpandableContent(expContent);

            alert2.showAndWait();

            VBox vbStats = new VBox();
            return vbStats;
        }
    }

    public String getPseudo(){
        return this.tfPseudo.getText();
    }

    public String getEMail(){
        return this.tfEmail.getText();
    }

    public String getMotDePasse(){
        return this.pfMotDePasse.getText();
    }

    public void activerBoutonEditer(boolean active){
        this.bEditer.setDisable(active);
    }

    public void activerBoutonValider(boolean active){
        this.bValider.setDisable(active);
    }

    public void activerTextfield(boolean active) {
        this.tfPseudo.setDisable(active);
        this.tfEmail.setDisable(active);
        this.pfMotDePasse.setDisable(active);
    }

    public void miseAJourAffichage() {
        int nbWins = 0;
        int nbLoses = 0;
        int nbGames = 0;
        try{
            nbWins = PartieJDBC.getNbWin(VueVapeur.getClient().getIdUtilisateur());
            nbLoses = PartieJDBC.getNbLose(VueVapeur.getClient().getIdUtilisateur());
            nbGames = PartieJDBC.getNbPartie(VueVapeur.getClient().getIdUtilisateur());
        } catch(SQLException ignore){}
        double winrate = 0;
        if(nbGames > 0){ winrate = (nbWins / nbGames) * 100; }

        this.lstat1.setText("Number of games played: " + nbGames);
        this.lstat2.setText("Number of games won: " + nbWins);
        this.lstat3.setText("Number of games lost: " + nbLoses);
        this.lstat4.setText("Winrate: " + winrate);
    }
}