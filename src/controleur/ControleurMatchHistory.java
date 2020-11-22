package controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import vue.Jeux;
import vue.VueMatchHistory;
import vue.VueVapeur;

public class ControleurMatchHistory implements EventHandler<ActionEvent> {

    private VueVapeur vueVapeur;
    private VueMatchHistory vueMatchHistory;

    public ControleurMatchHistory(VueVapeur vueVapeur, VueMatchHistory vueMatchHistory){
        this.vueVapeur = vueVapeur;
        this.vueMatchHistory = vueMatchHistory;
        Jeux.TIMELINES.put("partie", refresher(this));
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button bouton = (Button)actionEvent.getTarget();
        String texte = bouton.getText();
        switch (texte) {
            case "CONTINUE":

                break;
        }
    }

    private Timeline refresher(ControleurMatchHistory controleurMatchHistory) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0),
                actionEvent -> {
                    controleurMatchHistory.vueMatchHistory.miseAJourAffichage();
                }
            ),
           new KeyFrame(Duration.seconds(5))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }
    
}