package vue;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.util.Duration;

import controleur.ControleurMessage;
import controleur.ControleurPuissance4;
import jdbc.MessageJDBC;
import jdbc.PartieJDBC;
import modele.Message;
import modele.Utilisateur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VueMessage extends VueBorderPaneParent {

    private VueVapeur vueVapeur;

    private List<Message> listeMessages;
    private Utilisateur contact;
    private Utilisateur client;

    private TextField tfChatBar;
    private ScrollPane spMessages;

    public VueMessage(VueVapeur vueVapeur, Utilisateur contact) throws SQLException {
        super(vueVapeur);

        this.vueVapeur = vueVapeur;
        this.contact = contact;
        this.client = VueVapeur.getClient();
        this.listeMessages = new ArrayList<>();
        
        this.setCenter(panneauAffichage());
    }

    public void updateMessages() throws SQLException {
        ArrayList<Message> newMessages = MessageJDBC.getListeMessage(this.client.getIdUtilisateur(), this.contact.getIdUtilisateur());
        if(!this.listeMessages.equals(newMessages)){
            this.listeMessages = newMessages;
            this.spMessages.setContent(listeMessage());
            this.spMessages.setVvalue(this.spMessages.getVmax());
        }
    }

    private VBox panneauAffichage(){
        Label lTitre = new Label("Chatting with " + contact.getNomUtilisateur());
        lTitre.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 26; -fx-font-weight: bold;");

        this.spMessages = new ScrollPane(listeMessage());
        this.spMessages.setStyle("-fx-background: "+Palette.GRIS_5+"; -fx-border-color: "+Palette.GRIS_5+"; -fx-border-color: "+Palette.TEXTE+"; -fx-border-width: 1 1 1 1; -fx-fit-to-height: true;");
        this.spMessages.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.spMessages.setPadding(new Insets(20));
        this.spMessages.setVvalue(this.spMessages.getVmax());

        VBox vbPanneau = new VBox(lTitre, spMessages, creerChatBar());
        vbPanneau.setStyle("-fx-background-color: "+Palette.GRIS_4);
        vbPanneau.setPadding(new Insets(40));
        vbPanneau.setSpacing(40);

        return vbPanneau;
    }

    public HBox creerChatBar() {
        this.tfChatBar = new TextField();
        this.tfChatBar.setPrefWidth(500);
        this.tfChatBar.setPromptText("Your message");
        this.tfChatBar.setStyle("-fx-background-color: "+Palette.GRIS_5+"; -fx-border-color: "+Palette.TEXTE+"; -fx-border-width: 1 1 1 1; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");

        Button bEnvoyer = new Button("SEND");
        bEnvoyer.setPrefSize(200, 40);
        bEnvoyer.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
        bEnvoyer.setOnAction(new ControleurMessage(this));

        HBox vbChat = new HBox(this.tfChatBar, bEnvoyer);
        vbChat.setSpacing(20);
        vbChat.setAlignment(Pos.CENTER_LEFT);

        return vbChat;
    }

    public VBox listeMessage() {
        VBox listeMessages = new VBox();
        listeMessages.setSpacing(10);

        for (Message msg : this.listeMessages) {
            String nomExpediteur = (msg.getExpediteurId() == this.client.getIdUtilisateur()) ? this.client.getNomUtilisateur() : this.contact.getNomUtilisateur();
            Label lMessage = new Label("[" + nomExpediteur + "] " + msg.getTexte());
            lMessage.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18;");
            lMessage.setWrapText(true);
            
            listeMessages.getChildren().add(lMessage);
        }

        return listeMessages;
    }

    public void envoyerMessage() throws SQLException {
        String contenu = this.tfChatBar.getText();
        Message msg = new Message(this.contact.getIdUtilisateur(), this.client.getIdUtilisateur(), contenu);
        MessageJDBC.ajouterMessage(msg);
        this.updateMessages();
        this.tfChatBar.clear();
    }

    public boolean estVide(){
        String contenu = this.tfChatBar.getText();
        if(contenu.length() != 0){
            return false;
        }
        return true;
    }

}
