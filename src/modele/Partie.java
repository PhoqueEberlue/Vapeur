package modele;

import static java.lang.Integer.max;

public class Partie {
    private int idpa;
    private String debutpa;
    private int numetape;
    private String etatpartie;
    private int idut_1;
    private int idut_2;
    private int score_1;
    private int score_2;
    private final int maxScore;

    public Partie(int idpa, String debutpa, int numetape, String etatpartie, int idut_1, int idut_2, int score_1, int score_2){
        this.idpa = idpa;
        this.debutpa = debutpa;
        this.numetape = numetape;
        this.etatpartie = etatpartie;
        this.idut_1 = idut_1;
        this.idut_2 = idut_2;
        this.score_1 = score_1;
        this.score_2 = score_2;
        this.maxScore = max(this.score_1, this.score_2);
    }

    public int getIdpa() {
        return idpa;
    }

    public String getDebutpa() {
        return debutpa;
    }

    public String getEtatpartie() {
        return etatpartie;
    }

    public int getNumetape() {
        return numetape;
    }

    public int getIdut_1() {
        return idut_1;
    }

    public int getIdut_2() {
        return idut_2;
    }

    public int getScore_1() {
        return score_1;
    }

    public int getScore_2() {
        return score_2;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setIdpa(int idpa) {
        this.idpa = idpa;
    }

    public void setDebutpa(String debutpa) {
        this.debutpa = debutpa;
    }

    public void setNumetape(int numetape) {
        this.numetape = numetape;
    }

    public void setEtatpartie(String etatpartie) {
        this.etatpartie = etatpartie;
    }

    public void setIdut_1(int idut_1) {
        this.idut_1 = idut_1;
    }

    public void setIdut_2(int idut_2) {
        this.idut_2 = idut_2;
    }

    public void setScore_1(int score_1) {
        this.score_1 = score_1;
    }

    public void setScore_2(int score_2) {
        this.score_2 = score_2;
    }

    @Override
    public String toString() {
        return "\nPartie{" +
                "idpa=" + idpa +
                ", debutpa='" + debutpa + '\'' +
                ", numetape=" + numetape +
                ", etatpartie='" + etatpartie + '\'' +
                ", idut_1=" + idut_1 +
                ", idut_2=" + idut_2 +
                ", score_1=" + score_1 +
                ", score_2=" + score_2 +
                "}";
    }

    @Override
    public boolean equals(Object obj){
      if (obj == this) {
          return true;
      }
      if (obj == null || obj.getClass() != this.getClass()) {
          return false;
      }
      return this.idpa == ((Partie)obj).getIdpa();
    }

}
