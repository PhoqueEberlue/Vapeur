package controleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Optional;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import jdbc.InvitationJDBC;
import jdbc.PartieJDBC;
import jdbc.UtilisateurJDBC;
import modele.Partie;
import modele.Utilisateur;
import puissance4.Grille;
import vue.VuePuissance4;
import vue.VueVapeur;

public class ControleurPuissance4 implements EventHandler<ActionEvent> {

    private Timeline timeline;
    private VueVapeur vueVapeur;
    private VuePuissance4 vuePuissance4;
    private Utilisateur joueur1;
    private Utilisateur joueur2;
    private Boolean isPlayer1;
    private Boolean isYourTurn;
    private Grille grille;
    private int partyId;
    private boolean isEnd;

    public ControleurPuissance4(VueVapeur vueVapeur, VuePuissance4 vuePuissance4) throws SQLException{
        this.vueVapeur = vueVapeur;
        this.vuePuissance4 = vuePuissance4;
        this.joueur1 = UtilisateurJDBC.getUtilisateurWithId(vuePuissance4.getIdJoueur1());
        this.joueur2 = UtilisateurJDBC.getUtilisateurWithId(vuePuissance4.getIdJoueur2());
        this.isPlayer1 = VueVapeur.getClient().getIdUtilisateur() == this.joueur1.getIdUtilisateur();
        this.isYourTurn = VueVapeur.getClient().getIdUtilisateur() == this.joueur1.getIdUtilisateur();
        if(this.isPlayer1){
            this.partyId = PartieJDBC.newPartie(this.joueur1.getIdUtilisateur(), this.joueur2.getIdUtilisateur());
        }
        else{
            this.partyId = PartieJDBC.getMaxId() + 1; //y'a des probabilité pour qu'on choppe pas le bon ID, mais j'ai aucune autre idée plus efficace, à part de communiquer l'id dans le champs message d'invitation.
        }
        this.grille = new Grille(this.isPlayer1);
        this.vuePuissance4.updateGrille(this.grille.getGrille());
        this.isEnd = false;
        this.timeline = this.refresher(this);
    }

    /*
    Alert fenetreSurrendAutreJoueur = new Alert(Alert.AlertType.INFORMATION);
    fenetreSurrendAutreJoueur.setTitle("You won!");
    fenetreSurrendAutreJoueur.setHeaderText("your oppenent surrended, ez pz");
    fenetreSurrendAutreJoueur.showAndWait();
     */

    @Override
    public void handle(ActionEvent actionEvent){
        String texte = ((Button) actionEvent.getTarget()).getText();
        if(texte.equals("GIVE UP")){
            Alert fenetreAttenteJoueur = new Alert(Alert.AlertType.CONFIRMATION);
            fenetreAttenteJoueur.setTitle("Surrender");
            fenetreAttenteJoueur.setHeaderText("Do you really want to surrend this game ?");
            Optional<ButtonType> result3 = fenetreAttenteJoueur.showAndWait();
            if (result3.get() == ButtonType.OK){
                try {
                    PartieJDBC.setNumEtape(this.partyId, -1);
                    PartieJDBC.setScore(this.partyId, !this.isPlayer1, 43 - PartieJDBC.getNumEtape(this.partyId));
                } catch (SQLException e) {
                    creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");
                }
                this.vueVapeur.showVue(this.vueVapeur.getVueJeux());
                this.timeline.pause();
            }
        }
        else if(texte.equals("↓")){
            if(this.isYourTurn){
                int col = Integer.parseInt(((Button)actionEvent.getTarget()).getId());
                this.grille.ajoutPionGrille(col);
                this.isYourTurn = false;
                try {
                    PartieJDBC.setEtatPartie(this.partyId, this.grille.toJson());
                    PartieJDBC.incNumEtape(this.partyId);
                    this.vuePuissance4.updateGrille(this.grille.getGrille());
                } catch (SQLException e) {
                    creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");
                }
                if(this.grille.getVictoireGlobal()){
                    try {
                        PartieJDBC.setScore(this.partyId, this.isPlayer1, 43 - PartieJDBC.getNumEtape(this.partyId));
                        PartieJDBC.setNumEtape(this.partyId, -1);
                    } catch (SQLException e) {
                        creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");
                    }
                    this.vueVapeur.showVue(this.vueVapeur.getVueJeux());
                    Alert fenetreSurrendAutreJoueur = new Alert(Alert.AlertType.INFORMATION);
                    fenetreSurrendAutreJoueur.setTitle("You won!");
                    fenetreSurrendAutreJoueur.setHeaderText("You beat you oppenent, great job.");
                    fenetreSurrendAutreJoueur.show();
                    this.timeline.stop();
                }
            }
        }
    }

    private Timeline refresher(ControleurPuissance4 cp) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0),
                actionEvent -> {
                    this.vuePuissance4.changerNomJoueur1(this.joueur1.getNomUtilisateur());
                    this.vuePuissance4.changerNomJoueur2(this.joueur2.getNomUtilisateur());
                    if(cp.isPlayer1){
                        this.vuePuissance4.changerNombreJeton1(cp.grille.getNbPionJ());
                    }
                    else{
                        this.vuePuissance4.changerNombreJeton2(cp.grille.getNbPionR());
                    }
                    if (cp.isYourTurn) {
                        cp.vuePuissance4.changerMessage("It's your turn.");
                    } else {
                        cp.vuePuissance4.changerMessage("Waiting for the other player to play...");
                    }
                    int numEtape = 0;
                    try {
                        numEtape = PartieJDBC.getNumEtape(cp.partyId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(PartieJDBC.getNumEtape(cp.partyId) == -1 && !cp.grille.getVictoireGlobal()){ // SURREND de l'autre joueur
                             //je ne show pas l'arlete car elle ne s'affichage quand on perd :/
                            Alert fenetreSurrendAutreJoueur = new Alert(Alert.AlertType.INFORMATION);
                            fenetreSurrendAutreJoueur.setTitle("You win!");
                            fenetreSurrendAutreJoueur.setHeaderText("your oppenent surrended, ez pz");
                            fenetreSurrendAutreJoueur.show();
                            cp.vueVapeur.showVue(cp.vueVapeur.getVueJeux());
                            cp.timeline.stop();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (!cp.isYourTurn) {
                        if (cp.isPlayer1) {
                            try {
                                if(PartieJDBC.getNumEtape(cp.partyId) == -1){ // PERDU
                                    Alert fenetreLose = new Alert(Alert.AlertType.INFORMATION);
                                    fenetreLose.setTitle("You lose!");
                                    fenetreLose.setHeaderText("How sad :( , play better next time.");
                                    fenetreLose.show();
                                    cp.vueVapeur.showVue(cp.vueVapeur.getVueJeux());
                                    cp.timeline.stop();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if (numEtape % 2 == 0) { //si le num étape est pair, alors c'est au tour du joueur1
                                try {
                                    cp.grille.updateGrille(PartieJDBC.getEtatPartie(cp.partyId));
                                    cp.vuePuissance4.updateGrille(cp.grille.getGrille());
                                    this.vuePuissance4.changerNombreJeton2(cp.grille.getNbPionJ());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                cp.isYourTurn = true;
                            }
                        } else {
                            try {
                                if(PartieJDBC.getNumEtape(cp.partyId) == -1){ // PERDU
                                    Alert fenetreLose = new Alert(Alert.AlertType.INFORMATION);
                                    fenetreLose.setTitle("You lose!");
                                    fenetreLose.setHeaderText("How sad :( , play better next time.");
                                    fenetreLose.show();
                                    cp.vueVapeur.showVue(cp.vueVapeur.getVueJeux());
                                    cp.timeline.stop();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if (numEtape % 2 != 0) { //si le num étape est impaire, alors c'est au tour du joueur2
                                try {
                                    cp.grille.updateGrille(PartieJDBC.getEtatPartie(cp.partyId));
                                    cp.vuePuissance4.updateGrille(cp.grille.getGrille());
                                    this.vuePuissance4.changerNombreJeton1(cp.grille.getNbPionR()-1);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                cp.isYourTurn = true;
                            }
                        }
                    }
                }),
            new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        return timeline;
    }

    private void creerAlerteSQL(String header, String body){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception System");
        alert.setHeaderText(header);
        alert.setContentText(body);

        Exception ex = new SQLException(body);

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

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}