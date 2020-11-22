package controleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.application.Platform;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.util.Duration;
import jdbc.InvitationJDBC;
import jdbc.PartieJDBC;
import jdbc.UtilisateurJDBC;
import modele.Invitation;
import modele.Utilisateur;
import vue.VueJeux;
import vue.VueVBoxParent;
import vue.VueVapeur;

public class ControleurJeux implements EventHandler<ActionEvent> {

    private VueVapeur vueVapeur;
    private VueJeux vueJeux;
    private Timeline timeline;

    public ControleurJeux(VueVapeur vueVapeur, VueJeux vueJeux){
        this.vueVapeur = vueVapeur;
        this.vueJeux = vueJeux;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        String texte = ((Button) actionEvent.getTarget()).getText();
        switch (texte){
            case "SEARCH FOR A GAME":
                int idInv = -1;
                try {
                    idInv = InvitationJDBC.getInvitationAll();
                } catch (SQLException e) {
                    creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");
                }
                if(idInv != -1){ //PARTIE TROUVE
                    try {
                        this.vueVapeur.initPuissance4(InvitationJDBC.getIdUtExp(idInv), VueVapeur.getClient().getIdUtilisateur());
                        InvitationJDBC.setDest(idInv, VueVapeur.getClient().getIdUtilisateur());
                        InvitationJDBC.setEtatInv(idInv, "A");
                    } catch (SQLException e) {
                        creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");
                    }
                    this.vueVapeur.showVue(this.vueVapeur.getVuePuissance4());
                }
                else{ //CREATION DUNE PARTIE
                    Invitation inv;
                    try {
                        inv = InvitationJDBC.newInvitation(VueVapeur.getClient().getIdUtilisateur(), -1, "oui");
                        this.timeline = this.oppenentWaiter(this, inv.getIdinv());
                        Alert fenetreAttenteJoueur = new Alert(Alert.AlertType.CONFIRMATION);
                        fenetreAttenteJoueur.setTitle("Waiting for another player...");
                        fenetreAttenteJoueur.setHeaderText("Please wait while you find some one to play against.");
                        Optional<ButtonType> result3 = fenetreAttenteJoueur.showAndWait();
                        if (result3.get() == ButtonType.CANCEL){
                            InvitationJDBC.supprimerInvitation(inv.getIdinv());
                            this.timeline.stop();
                        }
                    } catch (SQLException e) {
                        creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");
                    }
                }
                break;
            case "INVITE A FRIEND":
                try {
                    List<Utilisateur> listeamis = UtilisateurJDBC.getListeAmis(VueVapeur.getClient().getIdUtilisateur());
                    List<String> choices = new ArrayList<>();
                    for(Utilisateur utilisateur : listeamis){
                        choices.add(utilisateur.getNomUtilisateur());
                    }
                    try {
                        ChoiceDialog<String> dialog = new ChoiceDialog<>(listeamis.get(0).getNomUtilisateur(), choices);
                        dialog.setTitle("System message");
                        dialog.setHeaderText("Invite a friend");
                        dialog.setContentText("Please choose a friend from your friend list:");

                        Optional<String> result = dialog.showAndWait();
                        if (result.isPresent()){
                            Invitation invitation = new Invitation(InvitationJDBC.getNewIdInvitation(), "E", VueVapeur.getClient().getIdUtilisateur(), UtilisateurJDBC.getIdUt(result.get()), VueVapeur.getClient().getNomUtilisateur()+" invited "+result.get());
                            InvitationJDBC.ajouterInvitation(invitation);
                            this.timeline = this.oppenentWaiter(this, invitation.getIdinv());
                        }
                    } catch (IndexOutOfBoundsException e){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("System message");
                        alert.setHeaderText("Impossible invitation");
                        alert.setContentText("Your friend list is empty.");
                        alert.showAndWait();
                    }
                } catch(SQLException ignore){creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");}
                break;
            default:
                creerAlerte("Option " + texte + " inconnue", "Problème de définition des boutons");
        }
    }

    private Timeline oppenentWaiter(ControleurJeux cj, int idInv) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        actionEvent -> {
                            try {
                                if(InvitationJDBC.getEtatInv(idInv).equals("A")){
                                    try {
                                        cj.vueVapeur.initPuissance4(VueVapeur.getClient().getIdUtilisateur(), InvitationJDBC.getIdUtDest(idInv));
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    cj.vueVapeur.showVue(this.vueVapeur.getVuePuissance4());
                                    cj.timeline.stop();
                                }
                            } catch (SQLException e) {
                                creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");
                            }
                        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        return timeline;
    }

    private void creerAlerte(String header, String body){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("System Message");
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.showAndWait();
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