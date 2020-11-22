package modele;

import java.sql.SQLException;
import java.util.ArrayList;
import jdbc.InvitationJDBC;
import jdbc.PartieJDBC;
import jdbc.UtilisateurJDBC;

public class Invitation {
    private int idinv;
    private String etatinv;
    private int idut_exp;
    private int idut_dest;
    private String msginv;

    public Invitation(int idinv, String etatinv, int idut_exp, int idut_dest, String msginv) {
        this.idinv = idinv;
        this.etatinv = etatinv;
        this.idut_exp = idut_exp;
        this.idut_dest = idut_dest;
        this.msginv = msginv;
    }

    public int getIdinv() {
        return this.idinv;
    }

    public String getEtatinv() {
        return this.etatinv;
    }

    public int getIdut_exp() {
        return this.idut_exp;
    }

    public int getIdut_dest() {
        return this.idut_dest;
    }

    public String getMsginv() {
        return this.msginv;
    }

    public ArrayList<Invitation> getListeInvitation(int Idut) throws SQLException {
        return InvitationJDBC.getListeInvitation(Idut);
    }

    @Override
    public String toString() {
        return "\nInvitation{" +
                "idinv=" + idinv +
                ", etatinv='" + etatinv + '\'' +
                ", idut_exp=" + idut_exp +
                ", idut_dest=" + idut_dest +
                ", msginv='" + msginv + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj){
      if (obj == this) {
          return true;
      }
      if (obj == null || obj.getClass() != this.getClass()) {
          return false;
      }
      return this.idinv == ((Invitation)obj).getIdinv();
    }

}
