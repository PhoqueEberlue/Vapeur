package modele;

import jdbc.MessageJDBC;
import jdbc.UtilisateurJDBC;

import java.sql.SQLException;
import java.util.ArrayList;

public class Message {
  private int destinataireId;
  private int expediteurId;
  private int msgid;
  private String texte;

  public Message(int destinataireId, int expediteurId, String texte) throws SQLException{
    this.destinataireId = destinataireId;
    this.expediteurId = expediteurId;
    this.texte = texte;
    this.msgid = MessageJDBC.getNewIdMessage();
  }

  public Message(int destinataireId, int expediteurId, String texte, int msgid){
    this.destinataireId = destinataireId;
    this.expediteurId = expediteurId;
    this.texte = texte;
    this.msgid = msgid;
  }

  // GETTERS
  public int getDestinataireId() {
    return this.destinataireId;
  }

  public int getExpediteurId() {
    return this.expediteurId;
  }

  public int getIdMessage() {
    return this.msgid;
  }

  public String getTexte() {
    return this.texte;
  }

  public boolean getLuMessage() throws SQLException {
    String statue = MessageJDBC.getLuMessage(this.msgid);

    return statue.equalsIgnoreCase("O");
  }

  public ArrayList<Message> getListeMessage(int IdUtenvoyer, int IdUtrecu) throws SQLException{
    return MessageJDBC.getListeMessage(IdUtenvoyer, IdUtrecu);
  }

  // SETTERS
  public void setTexte(String texte) {
    this.texte = texte;
  }

  public void setLuMessage() throws SQLException{
    MessageJDBC.setLuMessage(this.msgid);
  }

  private int genererId() throws SQLException {
    return MessageJDBC.getNewIdMessage();
  }

  public String toString() {
    return "id destinataire: " + this.getDestinataireId() + "\n" +
            "id expediteur: " + this.getExpediteurId() + "\n" +
            "Texte: " + this.getTexte() + "\n" +
            "id Message: " + this.getIdMessage();
  }

  @Override
  public boolean equals(Object obj){
    if (obj == this) {
        return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
        return false;
    }
    return this.msgid == ((Message)obj).getIdMessage();
  }

}