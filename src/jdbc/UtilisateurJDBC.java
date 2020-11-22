package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modele.Utilisateur;
import vue.VueVapeur;

public class UtilisateurJDBC {

    private static ConnexionMySQL laConnexion = VueVapeur.getConnexionMySQL();

    public static int maxIdJoueur() throws SQLException{
        Statement st=laConnexion.createStatement();

        ResultSet rs=st.executeQuery("select MAX(idut) from UTILISATEUR;");
        rs.next();

        return rs.getInt(1);
    }

    public static int getNewIdJoueur() throws SQLException{
        return maxIdJoueur()+1;
    }

    public static void ajouterJoueur(Utilisateur utilisateur) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement("insert into UTILISATEUR(idut, pseudout, emailut, mdput, activeut, nomrole) values (?,?,?,password(?),?,?)");

        ps.setInt(1, utilisateur.getIdUtilisateur());
        ps.setString(2, utilisateur.getNomUtilisateur());
        ps.setString(3, utilisateur.getEmail());
        ps.setString(4, utilisateur.getMotDePasse());
        ps.setString(5, "O");
        ps.setString(6, utilisateur.getRole());
        ps.executeUpdate();
    }

    public static void ajouterAmi(int id1, int id2) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement("insert into ETREAMI(idut_1, idut_2) values (?,?)");

        ps.setInt(1, id1);
        ps.setInt(2, id2);
        ps.executeUpdate();
    }

    public static void supprimerAmi(int id1, int id2) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement("delete from ETREAMI where idut_1=? and idut_2=?");

        ps.setInt(1, id1);
        ps.setInt(2, id2);
        ps.executeUpdate();

        PreparedStatement ps1 = laConnexion.prepareStatement("delete from ETREAMI where idut_1=? and idut_2=?");

        ps1.setInt(1, id2);
        ps1.setInt(2, id1);
        ps1.executeUpdate();
    }

    public static void supprimerJoueur(Utilisateur utilisateur) throws SQLException{

        PreparedStatement ps = laConnexion.prepareStatement("delete from UTILISATEUR where idut = ?");

        ps.setInt(1, utilisateur.getIdUtilisateur());
        ps.executeUpdate();
    }

    public static void updateUtilisateur(Utilisateur utilisateur) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement(
                "update UTILISATEUR SET pseudout = ?, emailut = ?, mdput = password(?), activeut = ?, nomrole = ? where idut = "+utilisateur.getIdUtilisateur());

        ps.setString(1, utilisateur.getNomUtilisateur());
        ps.setString(2, utilisateur.getEmail());
        ps.setString(3, utilisateur.getMotDePasse());
        ps.setString(4, utilisateur.getEtat());
        ps.setString(5, utilisateur.getRole());
        ps.executeUpdate();
    }

    public static void setEmailUt(int IdUt, String email) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement(
                "update UTILISATEUR SET emailut = ? where idut="+IdUt);

        ps.setString(1, email);
        ps.executeUpdate();
    }

    public static void setPasswordUt(int IdUt, String motdepasse) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement(
                "update UTILISATEUR SET mdput = password(?) where idut="+IdUt);

        ps.setString(1, motdepasse);
        ps.executeUpdate();
    }

    public static void setActiveUt(int IdUt, String etat) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement(
                "update UTILISATEUR SET activeut = ? where idut="+IdUt);

        ps.setString(1, etat);
        ps.executeUpdate();
    }

    public static void setPseudoUt(int IdUt, String pseudo) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement(
                "update UTILISATEUR SET pseudout = ? where idut="+IdUt);

        ps.setString(1, pseudo);
        ps.executeUpdate();
    }

    public static void setRoleUt(int IdUt, String role) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement(
                "update UTILISATEUR SET nomrole = ? where idut="+IdUt);

        ps.setString(1, role);
        ps.executeUpdate();
    }

    public static Boolean pseudoDansBD(String pseudo) throws SQLException{
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("select pseudout from UTILISATEUR where pseudout='"+ pseudo +"';");
        return rs.first();
    }

    public static Boolean verificationBD(String pseudo, String motdepasse) throws SQLException{
        Statement s=laConnexion.createStatement();
        ResultSet rs=s.executeQuery("Select pseudout, mdput from UTILISATEUR where pseudout='"+pseudo+"' and mdput=password('"+motdepasse+"');");
        rs.next();
        return rs.first();
    }

    public static int getIdUt(String pseudo) throws SQLException{
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("select idut from UTILISATEUR where pseudout='"+ pseudo +"';");
        if(rs.next()){
            int res= rs.getInt(1);
            rs.close();
            return res;
        } else {
            rs.close();
            return -1;
        }
    }

    public static String getPseudoUt(int IdUt) throws SQLException {
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("select pseudout from UTILISATEUR where idut="+ IdUt);
        rs.next();
        String res= rs.getString(1);
        rs.close();
        return res;
    }

    public static String getEmailUt(int IdUt) throws SQLException {
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("select emailut from UTILISATEUR where idut="+ IdUt);
        rs.next();
        String res= rs.getString(1);
        rs.close();
        return res;
    }


    public static String getMdpUt(int IdUt) throws SQLException {
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("select mdput from UTILISATEUR where idut="+ IdUt);
        rs.next();
        String res= rs.getString(1);
        rs.close();
        return res;
    }

    public static String getActiveUt(int IdUt) throws SQLException {
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("select activeut from UTILISATEUR where idut="+ IdUt);
        rs.next();
        String res= rs.getString(1);
        rs.close();
        return res;
    }

    public static String getRole(int IdUt) throws SQLException {
        Statement st=laConnexion.createStatement();
        ResultSet rs=st.executeQuery("select nomrole from UTILISATEUR where idut="+ IdUt);
        rs.next();
        String res= rs.getString(1);
        rs.close();
        return res;
    }

    public static ArrayList<Utilisateur> getListeAmis(int idut) throws SQLException{
        ArrayList<Utilisateur> liste = new ArrayList<>();
        Statement s=laConnexion.createStatement();
        ResultSet rs=s.executeQuery("Select idut_1, idut_2 from ETREAMI where idut_1="+idut+" or idut_2="+idut);
        while(rs.next()){
            int id1=rs.getInt("idut_1");
            int id2=rs.getInt("idut_2");

            if(id1!=idut){
                Utilisateur utilisateur = getUtilisateurWithId(id1);
                liste.add(utilisateur);
            } else {
                Utilisateur utilisateur = getUtilisateurWithId(id2);
                liste.add(utilisateur);
            }
        }
        rs.close();
        return liste;
    }

    public static Utilisateur getUtilisateurWithId(int IdUt) throws SQLException {
        return new Utilisateur(IdUt, getPseudoUt(IdUt), getMdpUt(IdUt), getEmailUt(IdUt), getRole(IdUt), getActiveUt(IdUt));
    }

    public static ArrayList<Utilisateur> getListeJoueur() throws SQLException{
        ArrayList<Utilisateur> liste = new ArrayList<>();
        Statement s=laConnexion.createStatement();
        ResultSet rs=s.executeQuery("Select idut from UTILISATEUR;");
        while(rs.next()){
            int IdUt = rs.getInt(1);
            Utilisateur utilisateur = getUtilisateurWithId(IdUt);
            liste.add(utilisateur);
        }
        rs.close();
        return liste;
    }
}
