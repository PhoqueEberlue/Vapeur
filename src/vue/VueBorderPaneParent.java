package vue;
import javafx.scene.layout.BorderPane;

public class VueBorderPaneParent extends BorderPane {
    
    protected VueVapeur vueVapeur;

    public VueBorderPaneParent(VueVapeur vueVapeur){
        super();
        
        this.vueVapeur = vueVapeur;
        this.setPrefSize(this.vueVapeur.getLargeur(), this.vueVapeur.getHauteur());
    }

}