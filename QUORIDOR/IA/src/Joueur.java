package src;

public class Joueur {
    private int x;
    private int y;

    private int nbMur;
    private String name;

    public int getNbMur() {
        return nbMur;
    }

    public void setNbMur(int nbMur) {
        this.nbMur = nbMur;
    }

    public Joueur(String name, int x, int y){
        this.x=x;
        this.y=y;
        this.name = name;
        this.nbMur = 10;
    }

    public String getCoords(){
        return this.getX() + ":" + this.getY();
    }

    public void setX(int x){
        this.x= x;
    }
    public void setY(int y){
        this.y= y;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String getName(){
        return this.name;
    }

    public Integer getBut(){ // rnevoie la ligne objectif d'un joueur, ligne 16 pour J1, ligne 0 pour J2
        Integer but = 0;
        if((this.name).equals("J1")){
            but=16;
        }
        return but;
    }

    public String toString(){
        return this.getName();
    }
}