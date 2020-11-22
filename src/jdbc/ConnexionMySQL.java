package jdbc;
import java.sql.*;

public class ConnexionMySQL {

	private Connection mysql;
	private boolean connecte = false;

	public ConnexionMySQL() throws ClassNotFoundException{
		Class.forName("org.mariadb.jdbc.Driver");
	}

	public void connecter() throws SQLException {
		String nomServeur = "46.105.92.223";
		String nomBase = "db11a";
		String nomLogin = "groupe11a";
		String motDePasse = "20@info!iuto11a";
		this.mysql = DriverManager.getConnection("jdbc:mariadb://" + nomServeur + ":3306/" + nomBase, nomLogin, motDePasse);
		this.connecte = true;
	}

	public void close() throws SQLException {
		this.mysql.close();
		this.connecte = false;
	}

	public boolean isConnecte() {
		return this.connecte;
	}

	public Blob createBlob()throws SQLException{
		return this.mysql.createBlob();
	}

	public Statement createStatement() throws SQLException {
		return this.mysql.createStatement();
	}

	public PreparedStatement prepareStatement(String requete) throws SQLException{
		return this.mysql.prepareStatement(requete);
	}
	
}
