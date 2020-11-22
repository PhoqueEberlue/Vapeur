package modele;

import java.security.Key;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import jdbc.InvitationJDBC;
import jdbc.PartieJDBC;
import jdbc.UtilisateurJDBC;

public class Utilisateur {
  private String nomUtilisateur;
  private String avatarUrl;
  private String motDePasse;
  private int idUtilisateur;
  private String etat;
  private String email;
  private String role;
  // ---------------------------------------------------------------------- //
  //                         CONSTRUCTEUR                                   //
  // ---------------------------------------------------------------------- //

 public Utilisateur(int idUtilisateur, String nomUtilisateur, String motDePasse, String email, String role, String etat) {
    this.nomUtilisateur = nomUtilisateur;
    this.motDePasse = motDePasse;
    this.idUtilisateur = idUtilisateur;
    this.email = email;
    this.role = role;
    this.etat = etat;
    this.avatarUrl = "defaultUser.png";
  }

  // ---------------------------------------------------------------------- //




  // ---------------------------------------------------------------------- //
  //                          SETTERS                                       //
  // ---------------------------------------------------------------------- //

  public void setNomUtilisateur(String nomUtilisateur) {
    this.nomUtilisateur = nomUtilisateur;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public void setMotDePasse(String motDePasse) {
    this.motDePasse = motDePasse;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setConnexion(UtilisateurJDBC jdbc) throws SQLException{
    this.etat = "O";
    UtilisateurJDBC.setActiveUt(this.idUtilisateur, this.etat);
  }

  public void setDeconnection(UtilisateurJDBC jdbc) throws SQLException{
    this.etat = "H";
    UtilisateurJDBC.setActiveUt(this.idUtilisateur, this.role);
  }

  public void setBloquer(UtilisateurJDBC jdbc) throws SQLException {
    this.role = "B";
    UtilisateurJDBC.setActiveUt(this.idUtilisateur, this.role);
  }

  // ---------------------------------------------------------------------- //



  // ---------------------------------------------------------------------- //
  //                              GETTERS                                   //
  // ---------------------------------------------------------------------- //

  public String getNomUtilisateur() {
    return this.nomUtilisateur;
  }

  public String getRole() {
    return this.role;
  }

  public String getEmail() {
    return this.email;
  }

  public String getEtat(){
    return this.etat;
  }

  public String getAvatarUrl() {
    return this.avatarUrl;
  }

  public String getMotDePasse() {
    return this.motDePasse;
  }

  public int getIdUtilisateur() {
    return this.idUtilisateur;
  }

  public ArrayList<Utilisateur> getListeAmis() throws SQLException{
    return UtilisateurJDBC.getListeAmis(this.idUtilisateur);
  }

  public ArrayList<Partie> getHistorique() throws SQLException {
    return PartieJDBC.getHistorique(this.idUtilisateur);
  }

  public ArrayList<Invitation> getInvitation() throws  SQLException {
    return InvitationJDBC.getListeInvitation(this.idUtilisateur);
  }


  // ---------------------------------------------------------------------- //
  //                        METHODE PRINCIPALES                             //
  // ---------------------------------------------------------------------- //

  public boolean addAmi(String pseudo) throws SQLException{
    if(UtilisateurJDBC.pseudoDansBD(pseudo)){
      int id = UtilisateurJDBC.getIdUt(pseudo);
      if(id==-1){
        return false;
      } else {
        UtilisateurJDBC.ajouterAmi(this.idUtilisateur, id);
        return true;
      }
    } else {
      return false;
    }
  }

  public boolean supprimerAmi(String pseudo) throws SQLException{
      if(UtilisateurJDBC.pseudoDansBD(pseudo)){
          int id = UtilisateurJDBC.getIdUt(pseudo);
          if(id==-1){
              return false;
          } else {
              UtilisateurJDBC.supprimerAmi(this.idUtilisateur, id);
              return true;
          }
      } else {
          return false;
      }
  }

  public String encrypter(String mdp) {
    try 
    {
        String text = mdp;
        String key = "E6RMbfj4rhU9pHCA"; // 128 bit key
        // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return new String(encrypted);
    }
    catch(Exception e)
    {
        e.printStackTrace();
        return "error";
    }
  }

  @Override
  public String toString() {
    return "\nUtilisateur{" +
            "nomUtilisateur='" + nomUtilisateur + '\'' +
            ", avatarUrl='" + avatarUrl + '\'' +
            ", motDePasse='" + motDePasse + '\'' +
            ", idUtilisateur=" + idUtilisateur +
            ", etat='" + etat + '\'' +
            ", email='" + email + '\'' +
            ", role='" + role + '\'' +
            '}';
  }

  public void voirProfile() {
  }

  @Override
  public boolean equals(Object obj){
    if (obj == this) {
        return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
        return false;
    }
    return this.idUtilisateur == ((Utilisateur)obj).getIdUtilisateur();
  }

}