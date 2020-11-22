package jdbc;

import modele.Partie;
import vue.VueVapeur;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class PartieJDBC {

	private static ConnexionMySQL laConnexion = VueVapeur.getConnexionMySQL();

	//SELECT
	public static String getDebutPa(int idpartie) throws SQLException{
		Statement st=laConnexion.createStatement();
		// exécution de la requête
		ResultSet rs=st.executeQuery("select debutpa from PARTIE where idpa=" + idpartie +";");
		// chargement de la 1ere ligne du résultat
		rs.next();
		// consultation de cette ligne
		String res= rs.getString(1);
		rs.close();
		return res;
	}

	public static String getEtatPartie(int idpartie) throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select etatpartie from PARTIE where idpa=" + idpartie +";");
		rs.next();
		String res= rs.getString(1);
		rs.close();
		return res;
	}

	public static int getNumEtape(int idpartie) throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select numetape from PARTIE where idpa=" + idpartie +";");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static int getIdJoueur(int idpartie, boolean isJoueur1) throws SQLException{
		Statement st=laConnexion.createStatement();
		String var;
		if(!isJoueur1){
			var = "score_2";
		}
		else{
			var = "score_1";
		}
		ResultSet rs=st.executeQuery("select " + var + " from PARTIE where idpa=" + idpartie +";");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static int getScore(int idpartie, boolean isJoueur1) throws SQLException{
		Statement st=laConnexion.createStatement();
		String var;
		if(!isJoueur1){
			var = "score_2";
		}
		else{
			var = "score_1";
		}
		ResultSet rs=st.executeQuery("select " + var + " from PARTIE where idpa=" + idpartie +";");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static int getMaxId() throws SQLException{
		Statement st=laConnexion.createStatement();

		ResultSet rs=st.executeQuery("select max(idpa) from PARTIE;");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static int getNbWin(int IdUt) throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select count(idpa) from PARTIE where ((idut_1 = "+ IdUt +" and score_1 > score_2) or (idut_2 = "+ IdUt +" and score_1 < score_2)) and numetape = -1;");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static int getNbPartie(int IdUt) throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select count(idpa) from PARTIE where ((idut_1 = "+ IdUt +") or (idut_2 = "+ IdUt +")) and numetape = -1;");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static int getNbPartieFini() throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select count(idpa) from PARTIE where numetape = -1;");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static int getNbPartieGlobal() throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select count(idpa) from PARTIE;");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static int getNbPartieProgress() throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select count(idpa) from PARTIE where numetape!=-1;");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static int getNbScoreMoyenneGlobal() throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select avg(score_1) from PARTIE where numetape = -1;");
		ResultSet rs2=st.executeQuery("select avg(score_2) from PARTIE where numetape = -1;");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		rs2.next();
		int res2 = rs2.getInt(1);
		return (res+res2)/getNbPartieFini();
	}

	public static int getNbDraw(int IdUt) throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select count(idpa) from PARTIE where ((idut_1 = "+ IdUt +" or idut_2 = "+ IdUt +") and score_1 = score_2) and numetape = -1;");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static int getNbLose(int IdUt) throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select count(idpa) from PARTIE where ((idut_1 = "+ IdUt +" and score_1 < score_2) or (idut_2 = "+ IdUt +" and score_1 > score_2)) and numetape = -1;");
		rs.next();
		int res= rs.getInt(1);
		rs.close();
		return res;
	}

	public static ArrayList<Partie> getHistorique(int IdUt) throws SQLException{
		ArrayList<Partie> res = new ArrayList<>();
    	Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select * from PARTIE where idut_1 = "+ IdUt +" or idut_2 = "+ IdUt +";");
		while(rs.next()){
			int idpa = rs.getInt(1);
			String debutpa = rs.getString(2);
			int numetape = rs.getInt(3);
			String etatpartie = rs.getString(4);
			int idut_1 = rs.getInt(5);
			int idut_2 = rs.getInt(6);
			int score_1 = rs.getInt(7);
			int score_2 = rs.getInt(8);
			res.add(new Partie(idpa, debutpa, numetape, etatpartie, idut_1, idut_2, score_1, score_2));
		}
		rs.close();
		return res;
	}

	public static ArrayList<Partie> getListePartie() throws SQLException{
		ArrayList<Partie> res = new ArrayList<>();
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("select * from PARTIE;");
		while(rs.next()){
			int idpa = rs.getInt(1);
			String debutpa = rs.getString(2);
			int numetape = rs.getInt(3);
			String etatpartie = rs.getString(4);
			int idut_1 = rs.getInt(5);
			int idut_2 = rs.getInt(6);
			int score_1 = rs.getInt(7);
			int score_2 = rs.getInt(8);
			res.add(new Partie(idpa, debutpa, numetape, etatpartie, idut_1, idut_2, score_1, score_2));
		}
		rs.close();
		return res;
	}

	//UPDATE
	public static void setNumEtape(int idpartie, int numEtape) throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("update PARTIE set numetape='"+ numEtape +"' where idpa=" + idpartie + ";");
		rs.close();
	}

	public static void incNumEtape(int idpartie) throws SQLException{
		int numEtape = getNumEtape(idpartie) + 1;
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("update PARTIE set numetape='"+ numEtape +"' where idpa=" + idpartie + ";");
		rs.close();
	}

	public static void setEtatPartie(int idpartie, String json) throws SQLException{
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("update PARTIE set etatpartie='"+ json +"' where idpa=" + idpartie + ";");
		rs.close();
	}

	public static void setScore(int idpartie, boolean isJoueur1, int nouveauScore) throws SQLException{
		String var;
		if(!isJoueur1){
			var = "score_2";
		}
		else{
			var = "score_1";
		}
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("update PARTIE set "+ var +"='"+ nouveauScore +"' where idpa=" + idpartie + ";");
		rs.close();
	}

	//INSERT
	public static int newPartie(int idUt1, int idUt2) throws SQLException{
		int NewId = getMaxId() + 1;
		Statement st=laConnexion.createStatement();
		ResultSet rs=st.executeQuery("insert into PARTIE values("+ NewId +",now(),0,'ici le json de l''état de la partie', "+ idUt1 +", "+ idUt2 +",0,0)");
		rs.close();
		return NewId;
	}
}