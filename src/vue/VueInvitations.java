package vue;

import controleur.ControleurInvitation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import jdbc.InvitationJDBC;
import jdbc.UtilisateurJDBC;
import modele.Invitation;
import modele.Utilisateur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class VueInvitations extends VueBorderPaneParent {

    private ArrayList<Pane> invitParJeu;
    private ControleurInvitation controleurInvitation;

    private ArrayList<Invitation> invitations;
    private VBox vbInvitations;

    public VueInvitations(VueVapeur vueVapeur){
        super(vueVapeur);

        this.invitations = new ArrayList<>();
        this.controleurInvitation = new ControleurInvitation(this.vueVapeur, this);
        this.setLeft(partieGauche());
        this.setCenter(partieCentral());
    }

    public VBox partieGauche(){
        //initialisation de la VBox qui va afficher les jeux
        VBox vbJeux = new VBox();
        vbJeux.setPrefWidth(250);
        vbJeux.setAlignment(Pos.TOP_LEFT);
        vbJeux.setStyle("-fx-background-color: "+Palette.GRIS_2);
        
        //création des éléments de la liste de jeux
        ArrayList<Button> listeBoutons = new ArrayList<>();
        ArrayList<String> jeux = new ArrayList<>(Jeux.LISTE_JEUX);
        jeux.add(0, "All games");
        for(String jeu : jeux){
            Button bouton = new Button(jeu);
            bouton.setPrefWidth(250);
            bouton.setPadding(new Insets(10, 10, 10, 10));
            bouton.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_2);
            vbJeux.getChildren().add(bouton);
            //bouton.setOnAction(this.controleurInvitation);
            listeBoutons.add(bouton);
        }
        //le premier élément est sélectionné par defaut
        listeBoutons.get(0).setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_4);

        return vbJeux;
    }

    public ScrollPane partieCentral(){
        this.vbInvitations = new VBox();
        this.vbInvitations.setSpacing(20);
        this.vbInvitations.setPadding(new Insets(40, 40, 40, 40));

        try{ this.invitations = InvitationJDBC.getListeInvitation(VueVapeur.getClient().getIdUtilisateur()); }
        catch(SQLException ignore){}

        this.vbInvitations.getChildren().addAll(listeVBoxInvitations());

        //on ajoute les invitations dans un ScrollPane pour les voir même si elles dépassent de l'écran
        ScrollPane spInvitations = new ScrollPane(this.vbInvitations);
        spInvitations.setStyle("-fx-background: "+Palette.GRIS_4+"; -fx-border-color: "+Palette.GRIS_4);
        spInvitations.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return spInvitations;
    }

    public void miseAJourAffichage(){
        ArrayList<Invitation> newInvitations = new ArrayList<>();
        try{ newInvitations = VueVapeur.getClient().getInvitation(); }
        catch(SQLException ignore){}

        if(!this.invitations.equals(newInvitations)){
            this.invitations = newInvitations;

            this.vbInvitations.getChildren().clear();
            this.vbInvitations.getChildren().addAll(listeVBoxInvitations());
        }
    }

    private ArrayList<VBox> listeVBoxInvitations(){
        ArrayList<VBox> listeVBox = new ArrayList<>();

        for(Invitation invitation : this.invitations){
            Label lInvitation = new Label();
            lInvitation.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            try{ lInvitation.setText(UtilisateurJDBC.getPseudoUt(invitation.getIdut_exp()) + " invited you to play a game of " + Jeux.P4_NOM + "!"); }
            catch(SQLException ignore){}

            Button bAccepter = new Button("ACCEPT");
            bAccepter.setId(invitation.getIdinv() +"");
            bAccepter.setOnAction(this.controleurInvitation);
            bAccepter.setPrefSize(250, 50);
            bAccepter.setStyle("-fx-background-color: "+Palette.BOUTON+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
            Button bRefuser = new Button("REFUSE");
            bRefuser.setId(invitation.getIdinv() + "");
            bRefuser.setOnAction(this.controleurInvitation);
            bRefuser.setPrefSize(250, 50);
            bRefuser.setStyle("-fx-background-color: "+Palette.DEFAITE+"; -fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 18; -fx-font-weight: bold;");
            HBox hbBoutons = new HBox(bAccepter, bRefuser);
            hbBoutons.setSpacing(40);
            
            VBox vbInvitation = new VBox(lInvitation, hbBoutons);
            vbInvitation.setStyle("-fx-background-color: "+Palette.GRIS_3+"; -fx-spacing: 30; -fx-alignment: center-left; -fx-fill-width: true;");
            vbInvitation.setPadding(new Insets(20));
            vbInvitation.setAlignment(Pos.TOP_CENTER);

            listeVBox.add(vbInvitation);
        }
        return listeVBox;
    }
    
}