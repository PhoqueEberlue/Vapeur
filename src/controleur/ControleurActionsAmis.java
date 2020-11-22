package controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import jdbc.InvitationJDBC;
import jdbc.UtilisateurJDBC;
import modele.Invitation;
import modele.Utilisateur;
import vue.Jeux;
import vue.VueSocial;
import vue.VueVapeur;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Optional;

public class ControleurActionsAmis implements EventHandler<ActionEvent> {

    private VueSocial vueSocial;
    private VueVapeur vueVapeur;
    private Timeline timeline;

    public ControleurActionsAmis(VueSocial vueSocial, VueVapeur vueVapeur){
        this.vueSocial = vueSocial;
        this.vueVapeur = vueVapeur;
        Jeux.TIMELINES.put("ami", refresher(this));
    }

    private Timeline refresher(ControleurActionsAmis controleurActionsAmis) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0),
                actionEvent -> {
                    controleurActionsAmis.vueSocial.miseAJourAffichage();
                }
            ),
           new KeyFrame(Duration.seconds(5))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button bouton = (Button)actionEvent.getTarget();
        String texte = bouton.getText();
        switch (texte){
            case "ADD A FRIEND":
                try{
                    if(UtilisateurJDBC.pseudoDansBD(vueSocial.getPseudo())){
                        if(dejaAmiAvec(vueSocial.getPseudo())){
                            creerAlerte("Already your friend", vueSocial.getPseudo() + " is already in your friend list.");
                        } else {
                            VueVapeur.getClient().addAmi(vueSocial.getPseudo());
                            this.vueSocial.miseAJourAffichage();
                            creerAlerte("Friend added", vueSocial.getPseudo() + " has been added to your friend list.");
                        }
                    } else {
                        creerAlerte("Unknown user", "The name " + vueSocial.getPseudo() + " does not exist.");
                    }
                } catch(SQLException ignore){creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");}
                break;
            case "REMOVE FROM FRIEND LIST":
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("System Message");
                alert.setHeaderText("Remove confirmation");
                alert.setContentText("Are you sure you want to remove " + this.vueSocial.getAmisActuel().getNomUtilisateur() + "\nfrom your friend list?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    try {
                        if(UtilisateurJDBC.pseudoDansBD(vueSocial.getAmisActuel().getNomUtilisateur())) {
                            VueVapeur.getClient().supprimerAmi(vueSocial.getAmisActuel().getNomUtilisateur());
                        } else {
                            Alert alert2 = new Alert(Alert.AlertType.ERROR);
                            alert2.setTitle("Warning System");
                            alert2.setHeaderText("Invalid username");
                            alert2.setContentText("The person you are trying to remove from your friends list no longer exists.");
                            alert2.showAndWait();
                        }
                    }
                    catch(SQLException ignore){creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");}
                    this.vueSocial.miseAJourAffichage();
                }
                break;
            case "INVITE TO PLAY":
                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                alert2.setTitle("System Message");
                alert2.setHeaderText("Invitation confirmation");
                alert2.setContentText("Are you sure you want to invite " + this.vueSocial.getAmisActuel().getNomUtilisateur() + "\nto a game of Connect 4?");
                Optional<ButtonType> result2 = alert2.showAndWait();
                if (result2.get() == ButtonType.OK){
                    try{
                        if(UtilisateurJDBC.pseudoDansBD(vueSocial.getAmisActuel().getNomUtilisateur())) {
                            Invitation invitation = new Invitation(InvitationJDBC.getNewIdInvitation(), "E", VueVapeur.getClient().getIdUtilisateur(), UtilisateurJDBC.getIdUt(this.vueSocial.getAmisActuel().getNomUtilisateur()), VueVapeur.getClient().getNomUtilisateur()+" invited "+this.vueSocial.getAmisActuel().getNomUtilisateur());
                            InvitationJDBC.ajouterInvitation(invitation);
                            this.timeline = this.oppenentWaiter(this, invitation.getIdinv());
                        } else {
                            Alert alert3 = new Alert(Alert.AlertType.ERROR);
                            alert3.setTitle("Warning System");
                            alert3.setHeaderText("Invalid username");
                            alert3.setContentText("The person you are trying to remove from your friends list no longer exists.");
                            alert3.showAndWait();
                        }
                    }
                    catch(SQLException ignore){creerAlerteSQL("Don't find connection with database.", "You probably did not connect your account to the platform.");}
                    this.vueSocial.miseAJourAffichage();
                }
                break;
            default:
                creerAlerte("Option " + texte + " inconnue", "Problème de définition des boutons");
        }
    }

    private void creerAlerte(String header, String body){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("System Message");
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.showAndWait();
    }

    private boolean dejaAmiAvec(String pseudo){
        for(Utilisateur ami : this.vueSocial.getListeAmis()){
            if(ami.getNomUtilisateur().equals(pseudo)){
                return true;
            }
        }
        return false;
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

    private Timeline oppenentWaiter(ControleurActionsAmis caa, int idInv) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        actionEvent -> {
                            try {
                                if(InvitationJDBC.getEtatInv(idInv).equals("A")){
                                    try {
                                        caa.vueVapeur.initPuissance4(VueVapeur.getClient().getIdUtilisateur(), InvitationJDBC.getIdUtDest(idInv));
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    caa.vueVapeur.showVue(this.vueVapeur.getVuePuissance4());
                                    caa.timeline.stop();
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
}
