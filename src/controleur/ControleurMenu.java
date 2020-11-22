package controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import vue.Palette;
import vue.VueProfil;
import vue.VueVapeur;

import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurMenu implements EventHandler<ActionEvent> {

    private VueVapeur vueVapeur;
    private ArrayList<Button> listeOnglets;

    public ControleurMenu(VueVapeur vueVapeur, ArrayList<Button> listeOnglets){
        this.vueVapeur = vueVapeur;
        this.listeOnglets = listeOnglets;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button bouton = (Button)actionEvent.getTarget();
        String texte = bouton.getText();
        String id = bouton.getId();
        switch (texte){
            case "GAMES":
                updateCouleur(id);
                this.vueVapeur.showVue(this.vueVapeur.getVueJeux());
                break;
            case "SOCIAL":
                updateCouleur(id);
                this.vueVapeur.showVue(this.vueVapeur.getVueSocial());
                break;
            case "MATCH HISTORY":
                updateCouleur(id);
                this.vueVapeur.showVue(this.vueVapeur.getVueMatchHistory());
                break;
            case "INVITATIONS":
                updateCouleur(id);
                this.vueVapeur.showVue(this.vueVapeur.getVueInvitations());
                break;
            case "ADMIN":
                updateCouleur(id);
                this.vueVapeur.showVue(this.vueVapeur.getVueAdmin());
                break;
            default:
                updateCouleur(id);
                VueProfil vueProfil = this.vueVapeur.getVueProfil();
                vueProfil.miseAJourAffichage();
                this.vueVapeur.showVue(vueProfil);
        }
    }

    public void updateCouleur(String id){
        for(Button bouton : this.listeOnglets){
            bouton.setStyle("-fx-text-fill: "+ Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_1);
        }
        this.listeOnglets.get(Integer.parseInt(id)).setStyle("-fx-text-fill: "+Palette.BOUTON+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_1);
    }
}
