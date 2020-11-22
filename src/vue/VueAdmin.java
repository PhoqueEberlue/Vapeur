package vue;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import controleur.ControleurActionsAdmin;
import controleur.ControleurOptionsAdmin;
import controleur.ControleurSocial;
import jdbc.PartieJDBC;
import jdbc.UtilisateurJDBC;
import modele.Partie;
import modele.Utilisateur;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class VueAdmin extends VueBorderPaneParent {

    private ArrayList<Button> listeOptions;
    private ArrayList<Region> fichesAdmin;
    private ControleurActionsAdmin controleurActionsAdmin;

    private ArrayList<HBox> listeHBoxUsers;

    public VueAdmin(VueVapeur vueVapeur){
        super(vueVapeur);

        this.controleurActionsAdmin = new ControleurActionsAdmin(this.vueVapeur, this);
        
        this.setLeft(partieGauche());

        this.fichesAdmin = new ArrayList<>();
        this.fichesAdmin.add(ficheStatistics());
        this.fichesAdmin.add(ficheUsers());
        this.fichesAdmin.add(ficheMatches());

        this.setCenter(this.fichesAdmin.get(0));
    }

    public VBox partieGauche(){
        VBox vbOptions = new VBox();
        vbOptions.setPrefWidth(250);
        vbOptions.setStyle("-fx-background-color: "+Palette.GRIS_2);
        
        String[] options = {"Statistics", "Users", "Matches"};
        this.listeOptions = new ArrayList<>();
        ControleurOptionsAdmin controleurOptionsAdmin = new ControleurOptionsAdmin(this);

        int id = 0;
        for(String option : options){
            Button bouton = new Button(option);
            bouton.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_2);
            bouton.setPrefWidth(250);
            bouton.setOnAction(controleurOptionsAdmin);
            bouton.setId("" + id++);
            vbOptions.getChildren().add(bouton);
            this.listeOptions.add(bouton);
        }
        this.listeOptions.get(0).setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_4);
        
        return vbOptions;
    }

    public VBox ficheStatistics(){
        Label lTitre = new Label("Global statistics");
        try {
            float moyenne = 0;
            int nb_totalpartiesfini = PartieJDBC.getNbPartieFini();
            int nb_totalparties = PartieJDBC.getNbPartieGlobal();
            int nb_totalpartiesprogress = PartieJDBC.getNbPartieProgress();
            if(PartieJDBC.getNbPartieGlobal()!=0){
                moyenne = PartieJDBC.getNbScoreMoyenneGlobal();
            }

            Label lstat1 = new Label("Number of games finished: " + nb_totalpartiesfini);
            Label lstat2 = new Label("Number of games in progress: " + nb_totalpartiesprogress);
            Label lstat3 = new Label("Number of games total: " + nb_totalparties);
            Label lstat4 = new Label("Average scores: " + moyenne);

            VBox vbStats = new VBox(lTitre, lstat1, lstat2, lstat3, lstat4);
            vbStats.setPadding(new Insets(40));
            vbStats.setSpacing(20);

            for(Node label : vbStats.getChildren()){
                ((Label)label).setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            }
            lTitre.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 26; -fx-font-weight: bold;");

            return vbStats;
        } catch (SQLException ignore){
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

    public ScrollPane ficheUsers(){
        ArrayList<Utilisateur> users = new ArrayList<>();
        try{ users = UtilisateurJDBC.getListeJoueur(); }
        catch(SQLException ignore){}

        VBox vbUsers = new VBox();
        vbUsers.setSpacing(20);
        vbUsers.setPadding(new Insets(40));

        this.listeHBoxUsers = new ArrayList<>();

        for(Utilisateur user : users){
            ImageView ivPP = new ImageView();
            try{
                ivPP.setImage(new Image("./src/img/" + user.getAvatarUrl()));
            } catch(IllegalArgumentException e){
                ivPP.setImage(new Image("./img/" + user.getAvatarUrl()));
            }
            Label lNom = new Label(user.getNomUtilisateur());
            lNom.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22; -fx-font-weight: bold;");
            Label lEmail = new Label(user.getEmail());
            lEmail.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
            VBox vbInfo1 = new VBox(lNom, lEmail);
            vbInfo1.setSpacing(10);
            vbInfo1.setAlignment(Pos.CENTER_LEFT);
            
            Label lRole = new Label((user.getRole().equals("admin")) ? "Administrator" : "User");
            lRole.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22; -fx-font-weight: bold;");
            Label lEtat = new Label((user.getEtat().equals("O")) ? "Online" : (user.getEtat().equals("H")) ? "Offline" : "Blocked");
            lEtat.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
            VBox vbInfo2 = new VBox(lRole, lEtat);
            vbInfo2.setId("etat");
            vbInfo2.setSpacing(10);
            vbInfo2.setAlignment(Pos.CENTER_RIGHT);
            HBox.setHgrow(vbInfo2, Priority.ALWAYS);
            vbInfo2.setMaxWidth(Double.MAX_VALUE);

            Button bCommuniquer = new Button("CHAT");
            bCommuniquer.setId("" + user.getIdUtilisateur());
            bCommuniquer.setOnAction(this.controleurActionsAdmin);
            bCommuniquer.setPrefSize(150, 50);
            bCommuniquer.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
            Button bBloquer = new Button((user.getEtat().equals("B")) ? "UNBLOCK" : "BLOCK");
            bBloquer.setId("" + user.getIdUtilisateur());
            bBloquer.setOnAction(this.controleurActionsAdmin);
            bBloquer.setPrefSize(150, 50);
            bBloquer.setStyle("-fx-background-color: "+Palette.DEFAITE+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
            if(user.getRole().equals("admin")){
                bBloquer.setDisable(true);
            }
            HBox hbBoutons = new HBox(bCommuniquer, bBloquer);
            hbBoutons.setSpacing(40);
            hbBoutons.setAlignment(Pos.CENTER_RIGHT);

            HBox hbUser = new HBox(ivPP, vbInfo1, vbInfo2, hbBoutons);
            hbUser.setStyle("-fx-background-color: "+Palette.GRIS_3+"; -fx-spacing: 30; -fx-alignment: center-left; -fx-fill-width: true;");
            hbUser.setPadding(new Insets(20));
            hbUser.setId("" + user.getIdUtilisateur());

            vbUsers.getChildren().addAll(hbUser);
            this.listeHBoxUsers.add(hbUser);
        }

        ScrollPane spUsers = new ScrollPane(vbUsers);
        spUsers.setStyle("-fx-background: "+Palette.GRIS_4+"; -fx-border-color: "+Palette.GRIS_4);
        spUsers.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return spUsers;
    }

    public ScrollPane ficheMatches(){
        ArrayList<Partie> matches = new ArrayList<>();
        try{ matches = PartieJDBC.getListePartie(); }
        catch(SQLException ignore){}

        VBox vbMatches = new VBox();
        vbMatches.setSpacing(20);
        vbMatches.setPadding(new Insets(40));

        for(Partie matche : matches){
            Label lJ1 = new Label();
            lJ1.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            try{ lJ1.setText(UtilisateurJDBC.getPseudoUt(matche.getIdut_1())); }
            catch(SQLException ignore){}
            Label lScore1 = new Label("" + matche.getScore_1());
            lScore1.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22; -fx-font-weight: bold;");
            VBox vbJ1 = new VBox(lJ1, lScore1);
            vbJ1.setStyle("-fx-alignment: center; -fx-spacing: 10;");

            Label lVS = new Label("VS");
            lVS.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 26; -fx-font-weight: bold;");

            Label lJ2 = new Label();
            lJ2.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            try{ lJ2.setText(UtilisateurJDBC.getPseudoUt(matche.getIdut_2())); }
            catch(SQLException ignore){}
            Label lScore2 = new Label("" + matche.getScore_2());
            lScore2.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22; -fx-font-weight: bold;");
            VBox vbJ2 = new VBox(lJ2, lScore2);
            vbJ2.setStyle("-fx-alignment: center; -fx-spacing: 10;");

            Label lJeu = new Label(Jeux.P4_NOM);
            lJeu.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20; -fx-font-weight: bold;");
            // Label lEtat = new Label(matche.getEtatpartie());
            // lEtat.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
            VBox vbJeu = new VBox(lJeu);
            vbJeu.setStyle("-fx-alignment: center; -fx-spacing: 10;");

            Label lTemps = new Label("Start date");
            lTemps.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            Label lDate = new Label(matche.getDebutpa());
            lDate.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            VBox vbDate = new VBox(lTemps, lDate);
            vbDate.setStyle("-fx-alignment: center; -fx-spacing: 10;");

            HBox hbMatche = new HBox(vbJ1, lVS, vbJ2, vbJeu, vbDate);
            hbMatche.setStyle("-fx-background-color: "+Palette.GRIS_3+"; -fx-spacing: 30; -fx-alignment: center-left; -fx-fill-width: true;");
            hbMatche.setPadding(new Insets(20));

            vbMatches.getChildren().addAll(hbMatche);
        }

        ScrollPane spMatches = new ScrollPane(vbMatches);
        spMatches.setStyle("-fx-background: "+Palette.GRIS_4+"; -fx-border-color: "+Palette.GRIS_4);
        spMatches.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return spMatches;
    }

    public void showFiche(int id){
        this.setCenter(this.fichesAdmin.get(id));
    }

    public ArrayList<Button> getListeOptions(){
        return this.listeOptions;
    }

    public void miseAJourUsers(int id, String etat, String option){
        for(HBox hb : this.listeHBoxUsers){
            if(id == Integer.parseInt(hb.getId())){
                for(Node n : hb.getChildren()){
                    if(n instanceof VBox){
                        if(!Objects.isNull(n.getId())){
                            ((Label)((VBox)n).getChildren().get(1)).setText(etat);
                        }
                    }
                    if(n instanceof HBox){
                        ((Button)((HBox)n).getChildren().get(1)).setText(option);
                    }
                }
            }
        }
    }
    
}