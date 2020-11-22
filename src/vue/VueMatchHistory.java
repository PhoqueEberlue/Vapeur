package vue;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;

import jdbc.PartieJDBC;
import jdbc.UtilisateurJDBC;
import modele.Partie;
import modele.Utilisateur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import controleur.ControleurMatchHistory;

public class VueMatchHistory extends VueBorderPaneParent {

    private ArrayList<Partie> parties;
    private VBox vbParties;
    private ControleurMatchHistory controleurMatchHistory;

    public VueMatchHistory(VueVapeur vueVapeur){
        super(vueVapeur);

        this.parties = new ArrayList<>();
        this.controleurMatchHistory = new ControleurMatchHistory(this.vueVapeur, this);
        
        this.setCenter(partieCentrale());
        this.setLeft(partieGauche());
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
            listeBoutons.add(bouton);
        }
        //le premier élément est sélectionné par defaut
        listeBoutons.get(0).setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 24; -fx-background-color: "+Palette.GRIS_4);

        return vbJeux;
    }

    public ScrollPane partieCentrale(){
        this.vbParties = new VBox();
        this.vbParties.setSpacing(20);
        this.vbParties.setPadding(new Insets(40, 40, 40, 40));

        try{ this.parties = PartieJDBC.getHistorique(VueVapeur.getClient().getIdUtilisateur()); }
        catch(SQLException ignore){}

        //création des fiches récapitulatives des parties
        this.vbParties.getChildren().addAll(listeHBoxMatches());

        //on ajoute les parties dans un ScrollPane pour les voir même si elles dépassent l'écran
        ScrollPane spParties = new ScrollPane(vbParties);
        spParties.setStyle("-fx-background: "+Palette.GRIS_4+"; -fx-border-color: "+Palette.GRIS_4);
        spParties.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return spParties;
    }

    private ArrayList<HBox> listeHBoxMatches(){
        ArrayList<HBox> listeHBox = new ArrayList<>();

        for(Partie partie : this.parties){
            Utilisateur j1 = null;
            Utilisateur j2 = null;
            try{
                j1 = UtilisateurJDBC.getUtilisateurWithId(partie.getIdut_1());
                j2 = UtilisateurJDBC.getUtilisateurWithId(partie.getIdut_2());
            } catch(SQLException ignore){}

            //un rectangle qui indique la victoire ou la défaite
            Rectangle indicateurVictoire = new Rectangle(10, 100);
            if(partie.getIdut_1() == VueVapeur.getClient().getIdUtilisateur()){
                if(partie.getScore_1() > partie.getScore_2()){
                    indicateurVictoire.setStyle("-fx-fill: "+Palette.VICTOIRE);
                } else {
                    indicateurVictoire.setStyle("-fx-fill: "+Palette.DEFAITE);
                }
            } else {
                if(partie.getScore_2() > partie.getScore_1()){
                    indicateurVictoire.setStyle("-fx-fill: "+Palette.VICTOIRE);
                } else {
                    indicateurVictoire.setStyle("-fx-fill: "+Palette.DEFAITE);
                }
            }

            //le pseudo de l'utilisateur au-dessus de son score
            Label lJoueurNom = new Label(j1.getNomUtilisateur());
            lJoueurNom.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            Label lJoueurPoints = new Label("" + partie.getScore_1());
            lJoueurPoints.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22; -fx-font-weight: bold;");
            VBox vbJoueur = new VBox(lJoueurNom, lJoueurPoints);
            vbJoueur.setStyle("-fx-alignment: center; -fx-spacing: 10;");

            //un label VS pour montrer les deux camps
            Label lVS = new Label("VS");
            lVS.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 26; -fx-font-weight: bold;");

            //le pseudo de l'adversaire au-dessus de son score
            Label lAdversaireNom = new Label(j2.getNomUtilisateur());
            lAdversaireNom.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            Label lAdversairePoints = new Label("" + partie.getScore_2());
            lAdversairePoints.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22; -fx-font-weight: bold;");
            VBox vbAdversaire = new VBox(lAdversaireNom, lAdversairePoints);
            vbAdversaire.setStyle("-fx-alignment: center; -fx-spacing: 10;");

            //le jeu et l'état de la partie : terminé ou en cours
            Label lJeu = new Label(Jeux.P4_NOM);
            lJeu.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20; -fx-font-weight: bold;");
            //Label lEtat = new Label(partie.getEtatpartie());
            //lEtat.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 20;");
            VBox vbJeu = new VBox(lJeu);
            vbJeu.setStyle("-fx-alignment: center; -fx-spacing: 10;");

            //le temps de la partie et la date de début
            Label lTemps = new Label("Start date");
            lTemps.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            Label lDate = new Label(partie.getDebutpa());
            lDate.setStyle("-fx-text-fill: "+Palette.TEXTE+"; -fx-font-size: 22;");
            VBox vbTemps = new VBox(lTemps, lDate);
            vbTemps.setStyle("-fx-alignment: center; -fx-spacing: 10;");

            //on ajoute le tout dans un HBox qu'on ajoute au VBox principal de la partie centrale
            HBox hbPartie = new HBox(indicateurVictoire, vbJoueur, lVS, vbAdversaire, vbJeu, vbTemps);
            hbPartie.setStyle("-fx-background-color: "+Palette.GRIS_3+"; -fx-spacing: 30; -fx-alignment: center-left; -fx-fill-width: true;");
            hbPartie.setPadding(new Insets(0, 30, 0, 0));

            listeHBox.add(hbPartie);
        }
        Collections.reverse(listeHBox);

        return listeHBox;
    }

    public void miseAJourAffichage(){
        ArrayList<Partie> newParties = new ArrayList<>();
        try{ newParties = VueVapeur.getClient().getHistorique(); }
        catch(SQLException ignore){}

        if(!this.parties.equals(newParties)){
            this.parties = newParties;

            this.vbParties.getChildren().clear();
            this.vbParties.getChildren().addAll(listeHBoxMatches());
        }
    }
    
}