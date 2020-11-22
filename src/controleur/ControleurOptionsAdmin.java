package controleur;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import vue.Palette;
import vue.VueAdmin;

public class ControleurOptionsAdmin implements EventHandler<ActionEvent> {

    private VueAdmin vueAdmin;

    public ControleurOptionsAdmin(VueAdmin vueAdmin){
        this.vueAdmin = vueAdmin;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button bouton = (Button)actionEvent.getTarget();
        String texte = bouton.getText();
        int id = Integer.parseInt(bouton.getId());
        updateCouleur(id);
        this.vueAdmin.showFiche(id);
    }

    public void updateCouleur(int id){
        for(Button bouton : this.vueAdmin.getListeOptions()){
            bouton.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_2);
        }
        this.vueAdmin.getListeOptions().get(id).setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_4);
    }

}
