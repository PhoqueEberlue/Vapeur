package jdbc;

import modele.Message;
import modele.Utilisateur;
import vue.VueVapeur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MessageJDBC {

    private static ConnexionMySQL laConnexion = VueVapeur.getConnexionMySQL();

    public static int maxIdMessage() throws SQLException{
        Statement st=laConnexion.createStatement();

        ResultSet rs=st.executeQuery("select MAX(idmsg) from MESSAGE;");
        rs.next();

        return rs.getInt(1);
    }

    public static int getNewIdMessage() throws SQLException{
        return maxIdMessage()+1;
    }

    public static String getMessage(int IdMsg) throws SQLException{
        Statement st=laConnexion.createStatement();

        ResultSet rs=st.executeQuery("select contenumsg from MESSAGE where idmsg="+IdMsg);
        rs.next();

        String res = rs.getString(1);
        return res;
    }

    public static int getExpediteur(int IdMsg) throws SQLException{
        Statement st=laConnexion.createStatement();

        ResultSet rs=st.executeQuery("select idut from MESSAGE where idmsg="+IdMsg);
        rs.next();

        int res = rs.getInt(1);
        return res;
    }

    public static int getDestinataire(int IdMsg) throws SQLException{
        Statement st=laConnexion.createStatement();

        ResultSet rs=st.executeQuery("select idut from RECEVOIR where idmsg="+IdMsg);
        rs.next();

        int res = rs.getInt(1);
        return res;
    }

    public static void ajouterMessage(Message message) throws SQLException {
        PreparedStatement ps = laConnexion.prepareStatement("insert into MESSAGE(idmsg, datemsg, contenumsg, idut) values (?,now(),?,?)");

        ps.setInt(1, message.getIdMessage());
        ps.setString(2, message.getTexte());
        ps.setInt(3, message.getExpediteurId());
        ps.executeUpdate();

        PreparedStatement ps2 = laConnexion.prepareStatement("insert into RECEVOIR(idmsg, idut, lu) values (?,?,?)");

        ps2.setInt(1, message.getIdMessage());
        ps2.setInt(2, message.getDestinataireId());
        ps2.setString(3, "N");
        ps2.executeUpdate();
    }

    public static void supprimerMessage(int IdMsg) throws SQLException{
        PreparedStatement ps2 = laConnexion.prepareStatement("delete from RECEVOIR where idmsg = ?");

        ps2.setInt(1, IdMsg);
        ps2.executeUpdate();

        PreparedStatement ps = laConnexion.prepareStatement("delete from MESSAGE where idmsg = ?");

        ps.setInt(1, IdMsg);
        ps.executeUpdate();
    }

    public static void updateMessage(int IdMsg, String message) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement(
                "update MESSAGE SET datemsg = now(), contenumsg = ? where idmsg = "+IdMsg);

        ps.setString(1, message);
        ps.executeUpdate();
    }

    public static String getLuMessage(int IdMsg) throws SQLException{
        Statement st=laConnexion.createStatement();

        ResultSet rs=st.executeQuery("select lu from RECEVOIR where idmsg = "+IdMsg);
        rs.next();

        return rs.getString(1);
    }

    public static void setLuMessage(int IdMsg) throws SQLException{
        PreparedStatement ps = laConnexion.prepareStatement(
                "update RECEVOIR SET lu = ? where idmsg = "+IdMsg);

        ps.setString(1, "O");
        ps.executeUpdate();
    }

    /**
    public static Message getMessageWithId(int IdMsg) throws SQLException {
        return new Message(getExpediteur(IdMsg), getExpediteur(IdMsg), getMessage(IdMsg), IdMsg);
    }*/

    public static ArrayList<Message> getListeMessage(int idutenvoyer, int idutrecu) throws SQLException{
        ArrayList<Message> liste = new ArrayList<>();
        Statement s=laConnexion.createStatement();
        ResultSet rs=s.executeQuery("(SELECT idmsg, contenumsg, datemsg, idut as Expediteur\n" +
                "FROM MESSAGE\n" +
                "WHERE idut="+idutenvoyer+" and idmsg in (select idmsg from RECEVOIR where idut="+idutrecu+"))\n" +
                "UNION\n" +
                "(SELECT idmsg, contenumsg, datemsg, idut as Expediteur\n" +
                "FROM MESSAGE\n" +
                "WHERE idut="+idutrecu+" and idmsg in (select idmsg from RECEVOIR where idut="+idutenvoyer+")) \n" +
                "order by datemsg;");
        while(rs.next()){
            int idmsg=rs.getInt("idmsg");
            String texte=rs.getString("contenumsg");
            String date=rs.getString("datemsg");
            int idut=rs.getInt("Expediteur");

            if(idut==idutenvoyer){
                Message message = new Message(idutrecu, idutenvoyer, texte, idmsg);
                liste.add(message);
            } else {
                Message message = new Message(idutenvoyer, idutrecu, texte, idmsg);
                liste.add(message);
            }
        }
        rs.close();
        return liste;
    }
}
