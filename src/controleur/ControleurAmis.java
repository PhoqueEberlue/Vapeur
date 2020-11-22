package controleur;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import modele.Utilisateur;
import vue.Palette;
import vue.VueSocial;
import vue.VueVapeur;

public class ControleurAmis implements EventHandler<MouseEvent> {

    private VueSocial vueSocial;
    private static Utilisateur client = VueVapeur.getClient();

    public ControleurAmis(VueSocial vueSocial){
        this.vueSocial = vueSocial;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        int id;
        if(mouseEvent.getTarget() instanceof HBox){
            id = Integer.parseInt(((HBox)mouseEvent.getTarget()).getId());
        } else {
            id = Integer.parseInt(((Node)mouseEvent.getTarget()).getParent().getId());
        }
        updateCouleur(id);
        this.vueSocial.showFicheAmi(id);
    }

    public void updateCouleur(int id){
        for(HBox hbox : this.vueSocial.getListeHBoxAmis()){
            hbox.setStyle("-fx-background-color: "+ Palette.GRIS_2);
        }
        this.vueSocial.getListeHBoxAmis().get(id).setStyle("-fx-background-color: "+Palette.GRIS_4);
        this.vueSocial.setAmiActuel(this.vueSocial.getListeAmis().get(id));
    }

}
