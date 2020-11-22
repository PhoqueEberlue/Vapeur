package vue;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.animation.Timeline;

import jdbc.ConnexionMySQL;
import jdbc.UtilisateurJDBC;
import modele.Utilisateur;

import java.sql.SQLException;

public class VueVapeur extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static ConnexionMySQL connexionMySQL = null;

    static {
        try {
            connexionMySQL = new ConnexionMySQL();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Driver MySQL non trouvé !");
            System.exit(1);
        }
        try {
            connexionMySQL.connecter();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("JARRIVE PAS A ME CO !");
        }
    }

    private VueConnexion vueConnexion;
    private VueInscription vueInscription;
    private VueJeux vueJeux;
    private VueSocial vueSocial;
    private VueMatchHistory vueMatchHistory;
    private VueInvitations vueInvitations;
    private VueProfil vueProfil;
    private VueAdmin vueAdmin;
    private MenuVapeur menuVapeur;
    private VuePuissance4 vuePuissance4;

    // Utilisateur courant
    private static Utilisateur client;

    private Scene scene;

    private double largeur = 1350;
    private double hauteur = 765;

    public void init() throws SQLException {
        this.vueConnexion = new VueConnexion(this);
        this.vueInscription = new VueInscription(this);
    }

    @Override
    public void start(Stage primaryStage) {
        try{
            primaryStage.getIcons().add(new Image("./src/img/logoVapeur.png"));
        } catch(IllegalArgumentException e){
            primaryStage.getIcons().add(new Image("./img/logoVapeur.png"));
        }

        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setStyle("-fx-background-color: "+Palette.GRIS_4);
        this.scene = new Scene(flowPane, this.largeur, this.hauteur);
        showVue(this.vueConnexion);

        primaryStage.setScene(this.scene);
        primaryStage.setTitle("Vapeur");
        primaryStage.show();

        //déconnecte l'utilisateur si il ferme la fenêtre
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if(VueVapeur.client != null){
                    System.out.println("déconnection...");
                    try{ UtilisateurJDBC.setActiveUt(VueVapeur.client.getIdUtilisateur(), "H"); }
                    catch(SQLException ignore){}
                }
            }
        });
    }

    public static ConnexionMySQL getConnexionMySQL() {
        return connexionMySQL;
    }

    public double getLargeur(){
        return this.largeur;
    }

    public double getHauteur(){
        return this.hauteur;
    }

    public void initialisationDesVues(){
        this.menuVapeur = new MenuVapeur(this);
        this.vueJeux = new VueJeux(this);
        this.vueSocial = new VueSocial(this);
        this.vueMatchHistory = new VueMatchHistory(this);
        this.vueInvitations = new VueInvitations(this);
        this.vueProfil = new VueProfil(this);
        if(VueVapeur.client.getRole().equals("admin")){
            this.vueAdmin = new VueAdmin(this);
        }
    }

    public void showVue(Pane vue){
        FlowPane fp = ((FlowPane)this.scene.getRoot());
        fp.getChildren().clear();
        fp.getChildren().addAll(vue);
    }

    public VueConnexion getVueConnexion(){
        stopAllTimelines();
        return this.vueConnexion;
    }

    public VueInscription getVueInscription(){
        stopAllTimelines();
        return this.vueInscription;
    }

    public VueJeux getVueJeux(){
        stopAllTimelines();
        this.vueJeux.setTop(this.menuVapeur);
        return this.vueJeux;
    }

    public VueSocial getVueSocial(){
        stopAllTimelines();
        startTimeline("ami");
        this.vueSocial.setTop(this.menuVapeur);
        return this.vueSocial;
    }

    public VueMatchHistory getVueMatchHistory(){
        stopAllTimelines();
        startTimeline("partie");
        this.vueMatchHistory.setTop(this.menuVapeur);
        return this.vueMatchHistory;
    }

    public VueInvitations getVueInvitations(){
        stopAllTimelines();
        startTimeline("invitation");
        this.vueInvitations.setTop(this.menuVapeur);
        return this.vueInvitations;
    }

    public VueProfil getVueProfil(){
        stopAllTimelines();
        this.vueProfil.setTop(this.menuVapeur);
        return this.vueProfil;
    }

    public VueAdmin getVueAdmin(){
        stopAllTimelines();
        this.vueAdmin.setTop(this.menuVapeur);
        return this.vueAdmin;
    }

    public Pane getVueMessage(Utilisateur contact) throws SQLException{
        stopAllTimelines();
        VueMessage messagerie = new VueMessage(this, contact);
        startTimeline("message");
        messagerie.setTop(this.menuVapeur);
        return messagerie;
    }

    public VuePuissance4 getVuePuissance4(){
        stopAllTimelines();
        return this.vuePuissance4;
    }

    public void initPuissance4(int idJoueur1, int idJoueur2){
        this.vuePuissance4 = new VuePuissance4(this, idJoueur1, idJoueur2);
    }

    public static void setClient(Utilisateur client) {
        VueVapeur.client = client;
    }
    
    public static Utilisateur getClient() {
        return VueVapeur.client;
    }

    public void stopAllTimelines(){
        for(String key : Jeux.TIMELINES.keySet()){
            Jeux.TIMELINES.get(key).stop();
        }
    }

    public void startTimeline(String nomTimeline){
        Jeux.TIMELINES.get(nomTimeline).play();
    }
    
}
