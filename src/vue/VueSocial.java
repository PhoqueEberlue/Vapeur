package vue;

import controleur.ControleurSocial;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import controleur.ControleurActionsAmis;
import controleur.ControleurAmis;
import jdbc.PartieJDBC;
import jdbc.UtilisateurJDBC;
import modele.Utilisateur;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class VueSocial extends VueBorderPaneParent {

    private TextField tfAjouterAmi;
    private HBox hbHeader;
    private HBox hbBoutons;
    private VBox vbAmis;
    private Label lTitre;

    private ArrayList<Utilisateur> amis;
    private ArrayList<HBox> listeHBoxAmis;
    private ArrayList<BorderPane> fichesAmis;
    private Utilisateur amiActuel;

    private ControleurAmis controleurAmis;
    private ControleurActionsAmis controleurActionsAmis;

    public VueSocial(VueVapeur vueVapeur){
        super(vueVapeur);

        //initialisation des attributs
        try{ this.amis = VueVapeur.getClient().getListeAmis(); }
        catch(SQLException ignore){}
        this.controleurAmis = new ControleurAmis(this);
        this.controleurActionsAmis = new ControleurActionsAmis(this, this.vueVapeur);

        this.setCenter(partieCentrale());
        this.setLeft(partieGauche());
    }

    public ScrollPane partieGauche(){
        //le titre
        this.lTitre = new Label("Friend list");
        this.lTitre.setPadding(new Insets(10, 10, 10, 10));
        this.lTitre.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-font-weight: bold;");

        //on initialise la VBox de la partie gauche
        this.vbAmis = new VBox(this.lTitre);
        this.vbAmis.setPrefWidth(250);
        this.vbAmis.setStyle("-fx-background-color: "+Palette.GRIS_2+"; -fx-alignment: top-center;");
        
        //création de la liste d'amis
        this.listeHBoxAmis = creerListeHBoxAmis();
        this.vbAmis.getChildren().addAll(this.listeHBoxAmis);

        //la liste est mis dans un scrollPane pour naviguer avec la molette
        ScrollPane spAmis = new ScrollPane(this.vbAmis);
        spAmis.setStyle("-fx-background: "+Palette.GRIS_2+"; -fx-border-color: "+Palette.GRIS_4);
        spAmis.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        return spAmis;
    }

    public BorderPane partieCentrale(){
        //header
        this.tfAjouterAmi = new TextField();
        this.tfAjouterAmi.setPromptText("Friend's username");
        this.tfAjouterAmi.setStyle("-fx-background-color: "+Palette.GRIS_4+"; -fx-border-color: "+Palette.TEXTE+"; -fx-border-width: 1 1 1 1; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
        Button bAjouterAmi = new Button("ADD A FRIEND");
        bAjouterAmi.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
        bAjouterAmi.setPrefSize(200, 40);
        bAjouterAmi.setOnAction(this.controleurActionsAmis);
        this.hbHeader = new HBox(this.tfAjouterAmi, bAjouterAmi);
        this.hbHeader.setAlignment(Pos.CENTER_LEFT);
        this.hbHeader.setSpacing(10);
        this.hbHeader.setPadding(new Insets(10, 10, 10, 10));
        this.hbHeader.setStyle("-fx-background-color: "+Palette.GRIS_3+"; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 5, 5);");
        Rectangle separation = new Rectangle(500, 2);

        //footer
        Button bCommuniquer = new Button("CHAT");
        bCommuniquer.setOnAction(new ControleurSocial(this.vueVapeur, this));
        Button bInviter = new Button("INVITE TO PLAY");
        bInviter.setOnAction(this.controleurActionsAmis);
        Button bSupprimer = new Button("REMOVE FROM FRIEND LIST");
        bSupprimer.setOnAction(this.controleurActionsAmis);
        this.hbBoutons = new HBox(bCommuniquer, bInviter, bSupprimer);
        this.hbBoutons.setSpacing(40);
        for(Node bouton : this.hbBoutons.getChildren()){
            ((Button)bouton).setMinSize(250, 50);
            ((Button)bouton).setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
        }

        //on créé la liste des fiches
        this.fichesAmis = creerFichesAmis();

        //une fiche vide est affichée par défaut
        return ficheVide();
    }
    
    public void showFicheAmi(int id){
        BorderPane fiche = this.fichesAmis.get(id);
        fiche.setTop(this.hbHeader);
        fiche.setBottom(this.hbBoutons);
        this.setCenter(fiche);
    }

    public void miseAJourAffichage(){
        ArrayList<Utilisateur> newAmis = new ArrayList<>();
        try{ newAmis = VueVapeur.getClient().getListeAmis(); }
        catch(SQLException ignore){}

        if(!this.amis.equals(newAmis)){
            this.amis = newAmis;

            this.listeHBoxAmis = creerListeHBoxAmis();
            this.fichesAmis = creerFichesAmis();
            this.vbAmis.getChildren().clear();
            this.vbAmis.getChildren().add(this.lTitre);
            this.vbAmis.getChildren().addAll(this.listeHBoxAmis);
            this.setCenter(ficheVide());
        }
    }

    private BorderPane ficheVide(){
        BorderPane ficheVide = new BorderPane();
        ficheVide.setPadding(new Insets(40, 40, 40, 40));
        ficheVide.setTop(this.hbHeader);
        return ficheVide;
    }
    
    public String getPseudo(){
        return this.tfAjouterAmi.getText();
    }

    public ArrayList<HBox> getListeHBoxAmis(){
        return this.listeHBoxAmis;
    }

    public ArrayList<Utilisateur> getListeAmis(){
        return this.amis;
    }

    public void setAmiActuel(Utilisateur ami){
        this.amiActuel = ami;
    }

    public Utilisateur getAmisActuel(){
        return this.amiActuel;
    }

    private ArrayList<HBox> creerListeHBoxAmis(){
        ArrayList<HBox> listeHBox = new ArrayList<>();
        //création de la liste d'amis
        int id = 0;
        for(Utilisateur ami : this.amis){
            //image de profil de l'ami et son pseudo
            ImageView ivPP = new ImageView();
            try{
                ivPP.setImage(new Image("./src/img/" + ami.getAvatarUrl()));
            } catch(IllegalArgumentException e){
                ivPP.setImage(new Image("./img/" + ami.getAvatarUrl()));
            }
            Label lNom = new Label(ami.getNomUtilisateur());
            lNom.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
            lNom.setId("" + id);

            //la HBox qui les contient
            HBox hbAmi = new HBox(ivPP, lNom);
            hbAmi.setSpacing(10);
            hbAmi.setAlignment(Pos.CENTER_LEFT);
            hbAmi.setPadding(new Insets(10, 5, 10, 5));
            hbAmi.setId("" + id++);
            hbAmi.setOnMousePressed(this.controleurAmis);

            //on l'ajoute à la liste
            listeHBox.add(hbAmi);
        }
        return listeHBox;
    }

    private ArrayList<BorderPane> creerFichesAmis(){
        ArrayList<BorderPane> listeFiches = new ArrayList<>();
        //création des fiches
        for(Utilisateur ami : this.amis){
            //profil de l'ami
            ImageView ivPP = new ImageView();
            try{
                ivPP.setImage(new Image("./src/img/" + ami.getAvatarUrl()));
            } catch(IllegalArgumentException e){
                ivPP.setImage(new Image("./img/" + ami.getAvatarUrl()));
            }
            ivPP.setFitWidth(120);
            ivPP.setPreserveRatio(true);
            Label lNom = new Label(ami.getNomUtilisateur());
            lNom.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20; -fx-font-weight: bold;");
            Label lEtat = new Label((ami.getEtat().equals("O")) ? "Online" : (ami.getEtat().equals("H")) ? "Offline" : "Blocked");
            lEtat.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18;");
            VBox vbInfo = new VBox(ivPP, lNom, lEtat);
            vbInfo.setSpacing(10);
            vbInfo.setPrefWidth(200);

            int nbWins = 0;
            int nbLoses = 0;
            int nbGames = 0;
            try{
                nbWins = PartieJDBC.getNbWin(ami.getIdUtilisateur());
                nbLoses = PartieJDBC.getNbLose(ami.getIdUtilisateur());
                nbGames = PartieJDBC.getNbPartie(ami.getIdUtilisateur());
            } catch(SQLException ignore){}
            double winrate = 0;
            if(nbGames > 0){ winrate = (nbWins / nbGames) * 100; }
            
            //statistiques de l'ami
            Label lstat1 = new Label("Number of games played: " + nbGames);
            Label lstat2 = new Label("Number of games won: " + nbWins);
            Label lstat3 = new Label("Number of games lost: " + nbLoses);
            Label lstat4 = new Label("Winrate: " + winrate);
            VBox vbStats = new VBox(lstat1, lstat2, lstat3, lstat4);
            vbStats.setSpacing(10);
            for(Node label : vbStats.getChildren()){
                ((Label)label).setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
            }

            //un HBox pour les contenir
            HBox hbAmi = new HBox(vbInfo, vbStats);
            hbAmi.setSpacing(20);
            hbAmi.setPadding(new Insets(30, 0, 30, 0));

            //un borderpane pour chaque fiche
            BorderPane bp = new BorderPane();
            bp.setPadding(new Insets(40, 40, 40, 40));
            bp.setCenter(hbAmi);
            listeFiches.add(bp);
        }
        return listeFiches;
    }
    
}