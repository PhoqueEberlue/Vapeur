package vue;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import controleur.ControleurConnexion;

public class VueVBoxParent extends VBox {

    protected VueVapeur vueVapeur;
    protected ControleurConnexion controleurConnexion;
    private Label lLogo;
    
    public VueVBoxParent(VueVapeur vueVapeur){
        //création du VBox avec la bonne apparence
        super();
        this.setPrefSize(600, 600);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(40, 40, 40, 40));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: "+Palette.GRIS_2+"; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 5, 5);");

        this.vueVapeur = vueVapeur;

        //logo de l'application
        HBox hbLogo = new HBox();
        hbLogo.setSpacing(20);
        hbLogo.setAlignment(Pos.CENTER_LEFT);
        this.lLogo = new Label("VAPEUR");
        ImageView ivLogo = new ImageView();
        try{
            ivLogo.setImage(new Image("./src/img/logoVapeur.png"));
        } catch(IllegalArgumentException e){
            ivLogo.setImage(new Image("./img/logoVapeur.png"));
        }
        hbLogo.getChildren().addAll(ivLogo, lLogo);

        //ajout du logo à la vue
        this.getChildren().add(hbLogo);
    }

    public void appliquerStyle(){
        //on applique un style pour les différents éléments
        for(Node o : this.getChildren()){
            if(o instanceof Pane){
                for(Object o2 : ((Pane)o).getChildren()){
                    if(o2 instanceof Button){
                        ((Button)o2).setOnAction(this.controleurConnexion);
                        ((Button)o2).setPrefSize(300, 50);
                        ((Button)o2).setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
                    }
                    else if(o2 instanceof Label){
                        ((Label)o2).setPrefSize(250, 50);
                        ((Label)o2).setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
                    }
                    else if(o2 instanceof TextField){
                        ((TextField)o2).setStyle("-fx-background-color: "+Palette.GRIS_3+"; -fx-border-color: "+Palette.TEXTE+"; -fx-border-width: 1 1 1 1; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
                    }
                }
            }
        }
        this.lLogo.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 30; -fx-font-weight: bold;");
    }

    public String getPseudo(){
        return "";
    }

    public String getEmail(){
        return "";
    }

    public String getMotDePasse(){
        return "";
    }

}