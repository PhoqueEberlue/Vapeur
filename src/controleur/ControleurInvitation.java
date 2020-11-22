package controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import jdbc.InvitationJDBC;
import modele.Utilisateur;
import vue.*;

import java.sql.SQLException;

public class ControleurInvitation implements EventHandler<ActionEvent> {

    private VueVapeur vueVapeur;
    private VueInvitations vueInvitations;

    public ControleurInvitation(VueVapeur vueVapeur, VueInvitations vueInvitations){
        this.vueVapeur = vueVapeur;
        this.vueInvitations = vueInvitations;
        Jeux.TIMELINES.put("invitation", refresher(this));
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button bouton = (Button)actionEvent.getTarget();
        String texte = bouton.getText();
        int idInv = Integer.parseInt(bouton.getId());
        switch (texte) {
            case "ACCEPT":
                try {
                    this.vueVapeur.initPuissance4(InvitationJDBC.getIdUtExp(idInv), VueVapeur.getClient().getIdUtilisateur());
                    InvitationJDBC.setEtatInv(idInv, "A");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                this.vueVapeur.showVue(this.vueVapeur.getVuePuissance4());
                break;
            case "REFUSE":
                try {
                    System.out.println("suppresion invite");
                    InvitationJDBC.supprimerInvitation(idInv);
                    this.vueInvitations.miseAJourAffichage();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private Timeline refresher(ControleurInvitation controleurInvitation) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0),
                actionEvent -> {
                    controleurInvitation.vueInvitations.miseAJourAffichage();
                }
            ),
           new KeyFrame(Duration.seconds(5))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }
}
