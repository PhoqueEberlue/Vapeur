package executable;

import jdbc.ConnexionMySQL;
import jdbc.InvitationJDBC;
import jdbc.UtilisateurJDBC;
import modele.Invitation;
import vue.VueVapeur;

import java.sql.SQLException;

public class ExecutableInvitation {
    public static void main (String[] args) throws ClassNotFoundException, SQLException {
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        connexionMySQL.connecter();
        Invitation invitation = new Invitation(InvitationJDBC.getNewIdInvitation(), "E", 2, 1, "Vous avez été invité");
        //InvitationJDBC.ajouterInvitation(invitation);
    }

}