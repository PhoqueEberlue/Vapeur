package executable;

import jdbc.ConnexionMySQL;
import jdbc.PartieJDBC;

import java.sql.SQLException;

public class ExecutableP4Test {
    public static void main (String[] args) throws ClassNotFoundException, SQLException {
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        connexionMySQL.connecter();
        System.out.println(PartieJDBC.getEtatPartie(1));
        System.out.println(PartieJDBC.getNumEtape(1));
        System.out.println(PartieJDBC.getDebutPa(1));
        System.out.println(PartieJDBC.getIdJoueur(1,true));
        System.out.println(PartieJDBC.getIdJoueur(1,false));
        System.out.println(PartieJDBC.getScore(1,true));
        System.out.println(PartieJDBC.getScore(1,false));
        PartieJDBC.setNumEtape(1,10);
        PartieJDBC.setScore(1,true,999);
        System.out.println(PartieJDBC.getMaxId());
        System.out.println(PartieJDBC.newPartie(1,2));
        System.out.println(PartieJDBC.getNbWin(3));
        System.out.println(PartieJDBC.getNbLose(2));
        System.out.println(PartieJDBC.getHistorique(1));
    }
}