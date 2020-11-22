package jdbc;

import modele.Invitation;
import modele.Partie;
import modele.Utilisateur;
import vue.VueVapeur;

import javax.naming.InitialContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InvitationJDBC {

    private static ConnexionMySQL laConnexion = VueVapeur.getConnexionMySQL();

    public static int maxIdInvitation() throws SQLException{
        Statement st=laConnexion.createStatement();

        ResultSet rs=st.executeQuery("select MAX(idinv) from INVITATION;");
        rs.next();

        return rs.getInt(1);
    }

    public static int getNewIdInvitation() throws SQLException{
        return maxIdInvitation()+1;
    }

    //SELECT
    public static String getDateInv(int idinv) throws SQLException {
        Statement st=laConnexion.createStatement();
        // exécution de la requête
        ResultSet rs=st.executeQuery("select dateinv from INVITATION where idinv=" + idinv +";");
        // chargement de la 1ere ligne du résultat
        rs.next();
        // consultation de cette ligne
        String res = rs.getString(1);
        rs.close();
        return res;
    }

    public static String getEtatInv(int idinv) throws SQLException {
        Statement st=laConnexion.createStatement();
        // exécution de la requête
        ResultSet rs=st.executeQuery("select etatinv from INVITATION where idinv=" + idinv +";");
        // chargement de la 1ere ligne du résultat
        rs.next();
        // consultation de cette ligne
        String res = rs.getString(1);
        rs.close();
        return res;
    }

    public static int getIdUtExp(int idinv) throws SQLException {
        Statement st=laConnexion.createStatement();
        // exécution de la requête
        ResultSet rs=st.executeQuery("select idut_exp from INVITATION where idinv=" + idinv +";");
        // chargement de la 1ere ligne du résultat
        rs.next();
        // consultation de cette ligne
        int res= rs.getInt(1);
        rs.close();
        return res;
    }

    public static int getIdUtDest(int idinv) throws SQLException {
        Statement st=laConnexion.createStatement();
        // exécution de la requête
        ResultSet rs=st.executeQuery("select idut_dest from INVITATION where idinv=" + idinv +";");
        // chargement de la 1ere ligne du résultat
        rs.next();
        // consultation de cette ligne
        int res= rs.getInt(1);
        rs.close();
        return res;
    }

    public static String getMsgInv(int idinv) throws SQLException {
        Statement st=laConnexion.createStatement();
        // exécution de la requête
        ResultSet rs=st.executeQuery("select msginv from INVITATION where idinv=" + idinv +";");
        // chargement de la 1ere ligne du résultat
        rs.next();
        // consultation de cette ligne
        String res = rs.getString(1);
        rs.close();
        return res;
    }

    public static ArrayList<Invitation> getListeInvitation(int IdUt) throws SQLException {
        ArrayList<Invitation> res = new ArrayList<>();
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("select * from INVITATION where idut_dest = "+ IdUt+" and DATEDIFF(CURDATE(), dateinv)<=24;");

        PreparedStatement ps = laConnexion.prepareStatement("delete from INVITATION where idut_dest= "+IdUt+" and DATEDIFF(CURDATE(), dateinv)>=24");
        ps.executeUpdate();

        while(rs.next()){
            int idinv = rs.getInt(1);
            String dateinv = rs.getString(2);
            String etatinv = rs.getString(3);
            int idut_exp = rs.getInt(4);
            int idut_dest = rs.getInt(5);
            String msginv = rs.getString(6);
            res.add(new Invitation(idinv, etatinv, idut_exp, idut_dest, msginv));
        }
        rs.close();
        return res;
    }

    public static Invitation getInvitationWithId(int idinv) throws SQLException {
        return new Invitation(idinv, getEtatInv(idinv), getIdUtExp(idinv), getIdUtDest(idinv), getMsgInv(idinv));
    }

    public static void ajouterInvitation(Invitation invitation) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement("insert into INVITATION(idinv, dateinv, etatinv, idut_exp, idut_dest, msginv) values (?,now(), ?, ?, ?, ?)");

        ps.setInt(1, invitation.getIdinv());
        ps.setString(2, invitation.getEtatinv());
        ps.setInt(3, invitation.getIdut_exp());
        ps.setInt(4, invitation.getIdut_dest());
        ps.setString(5, invitation.getMsginv());
        ps.executeUpdate();
    }

    public static Invitation newInvitation(int idExp, int idDest, String Msg) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement("insert into INVITATION(idinv, dateinv, etatinv, idut_exp, idut_dest, msginv) values (?,now(), ?, ?, ?, ?)");
        int id = getNewIdInvitation();
        ps.setInt(1, id);
        ps.setString(2, "E");
        ps.setInt(3, idExp);
        if(idDest == -1){
            ps.setNull(4, java.sql.Types.INTEGER);
        }
        else{
            ps.setInt(4, idDest);
        }
        ps.setString(5, Msg);
        ps.executeUpdate();
        return new Invitation(id,"A", idExp, idDest, Msg);
    }

    public static int getInvitationAll() throws SQLException{
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("Select idinv from INVITATION where etatinv = 'E' and idut_dest IS NULL order by dateinv desc;");
        rs.next();
        int res;
        if(rs.first()){
            res= rs.getInt(1);
        }
        else {
            res = -1;
        }
        rs.close();
        return res;
    }

    //SETTER

    public static void setEtatInv(int idinv, String etatinv) throws SQLException{
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("update INVITATION set etatinv='"+ etatinv +"' where idinv=" + idinv + ";");
        rs.close();
    }

    public static void setDest(int idinv, int idMoi) throws SQLException{
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("update INVITATION set idut_dest='"+ idMoi +"' where idinv=" + idinv + ";");
        rs.close();
    }

    public static void supprimerInvitation(int idinv) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement("delete from INVITATION where idinv = ?");

        ps.setInt(1, idinv);
        ps.executeUpdate();
    }
}

