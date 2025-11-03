package src; 

import java.util.ArrayList;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.PriorityQueue;

//cette classe contient la grille et le fonctionnement du jeu pour le moment : Le main sera par la suite mis dans une classe jeu a part appellant cette classe.
//la grille a pour taille 2x-1, x étant le nombre de cases de largeur et de longeur UTILISABLE par le joueur, le reste des cases pour les murs
//Les indices impairs:pairs sont les murs, les pairs:pairs les joueurs, les impairs:impairs les emplacements des murs
//pour size = 3 :
//  0  1  2
//0 J1 X
//1    X
//2    X
public class Grille {
    private int size = 17; //taille par défaut de la grille
    private String [][] grille;//variable de la grille en forme terminal qui contiendra nos éléments
    private Joueur j1, j2;//les joueurs préalablement initialisés
    private ArrayList<Mur> mursPresents;//liste des objets murs instanciée mais vide au début
    private Joueur joueurCourant;//variable permettant d'identifier le joueur courant dans la boucle de jeu
    private Ia ia1Funct = new Ia("ia1", 0, 0);

    public Grille(int size, Joueur j1, Joueur j2){
        grille = new String[size][size];//on créé la grille en fonction de la taille fournie
        //on récupère les joueurs
        this.setJoueur1(j1);
        this.setJoueur2(j2);
        //Le joueur 1 est le premier joueur, cette ligne peut changer
        this.joueurCourant = j1;
        //on instancie l'Arraylist des murs mais vide
        this.mursPresents = new ArrayList<>();
        //on place graphiquement les joueurs sur la grille dans le terminal
        grille[this.j1.getX()][this.j1.getY()] = "J1";
        grille[this.j2.getX()][this.j2.getY()] = "J2";

    }

    //Getters et Setters
    public void setJoueur1(Joueur j1){
        this.j1=j1;
    }
    public void setJoueur2(Joueur j2){
        this.j2=j2;
    }

    public String[][] getGrille(){
        return this.grille;
    }

    public boolean isMur(Node node){
        if (this.grille[node.x][node.y] != null){
            if (this.grille[node.x][node.y].equals(" X")){
                return true;
            } else {
                return false;
            }   
        }
        return false;
    }

    public boolean isJoueur(Node node){
        if (this.grille[node.x][node.y] != null){
            if (this.grille[node.x][node.y].equals("J1") || this.grille[node.x][node.y].equals("J2")){
                return true;
            } else {
                return false;
            }   
        }
        return false;
    }

    public void setGrille(String[][] grille){
        this.grille = grille;
    }
    public Joueur getJoueur1(){
        return this.j1;
    }
    public Joueur getJoueur2(){
        return this.j2;
    }

    //Cette fonction vérifie si le mur peut être posé par le joueur au coordonées et dans la direction fournie
    //On fournit le joueur pour vérifier le nombre de murs restants
    //on fournit les coordonnées et la direction pour vérifier la possibilité
    public boolean murPossible(Joueur j, String coords){
        //variable du résulat
        boolean res = false;
        String[] coordonnees = coords.split(":");//on sépare le format x:y en [x,y] dans un tableau
        int x = Integer.parseInt(coordonnees[0]);//on transforme le premier char en int
        int y = Integer.parseInt(coordonnees[1]);//pareil pour le deuxième
        //test pour vérifier si un mur existe a l'emplacement x,y
        boolean existe = false;
        for(Mur m : mursPresents){
            if (m.getX() == x && m.getY() == y){
                existe = true;
            }
        }
        //test du nombre de murs restannt du joueur j
        if(!existe && j.getNbMur() != 0){
            res = true;
        } else if (j.getNbMur() == 0){
            System.out.println("Vous n'avez plus de murs !!!");
        }

    // MANQUANT : VERIFIER SI LE JOUEUR A TOUJOURS UN CHEMIN VERS SA VICTOIRE, VERIFIER SI LE MUR SE POSE SUR LE BORD D'UN AUTRE
        return res; // true si on peut poser, false sinon
    }

    //Fonction plaçant le mur "réelement"
    //Le paramètre joueur est là pour lui enlever un mur
    //Les autres paramètres servent pour le positionnement du mur
    public void placerMur(Joueur j, String coords, String direction){
        //récupération des coordonnées
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);
        //on enlève un mur au joueur
        j.setNbMur(j.getNbMur()-1);
        //on créé un objet mur
        Mur mur = new Mur(direction, x, y);
        mursPresents.add(mur);
        //on ajoute le mur dans la grille
        //Note : bien appliquer une chaîne identifiable dans la grille, la vérification de la présence d'un mur passe par la chaine dans la case
        if (direction.equals("v")){
            this.grille[x][y] = " X";
            this.grille[x][y+1] = " X";
            this.grille[x][y-1] = " X";
        } else if (direction.equals("h")){
            this.grille[x][y] = " X";
            this.grille[x+1][y] = " X";
            this.grille[x-1][y] = " X";
        }
        //on avertit le joueur du nombre de mur restant
        System.out.println("il vous reste " + j.getNbMur() + " murs");
    }

    //Fonction permettant de vérifier si le joueur peut se déplacer dans une direction
    //on fournit le joueur pour verifier ses coordonnées
    //on fournit la direction pour la vérification
    public boolean mouvementPossible(Joueur j, int direction){
        //variable du résultat
        Boolean r = false;
        //On définit une variable du joueur adverse pour tester la collision entre joueurs;
        Joueur adversaire;
        if (j.getName() == "J1"){
            adversaire = this.j2;
        } else {
            adversaire = this.j1;
        }

        //Test pour la collision d'un mur, on se sert de la présence de la chaine de caractère pour vérifier
        boolean mur = false; //deviendra true si le joueur recontre un mur
        if(direction == 0){ //vers le haut
            if (j.getY()!=0 && (grille[j.getX()][j.getY() -1])!= null) { 
                if (grille[j.getX()][j.getY() - 1].equals(" X")) { //on vérifie si il y a un mur
                    mur = true; //si mur, alors on préviens
                }
            }
        } else if(direction == 1){ //pareil vers la droite
            if (j.getX()!=this.getSize() -1 && (grille[j.getX() +1][j.getY()])!= null) {
                if (grille[j.getX() + 1][j.getY()].equals(" X")) {
                    mur = true;
                }
            }
        } else if(direction == 2){ //pareil vers le bas
            if(j.getY()!=this.getSize()-1 && (grille[j.getX()][j.getY() +1 ])!= null) {
                if (grille[j.getX()][j.getY() + 1].equals(" X")) {
                    mur = true;
                }
            }
        } else if(direction == 3){ //pareil vers la gauche
            if (j.getX()!=0 && (grille[j.getX() -1][j.getY()])!= null) {
                if (grille[j.getX() - 1][j.getY()].equals(" X")) {
                    mur = true;
                }
            }
        }

        //Test global, se servant du test du mur et verifiant aussi la présence de l'autre joueur
        //le code est étrange dans les test je me suis mélangé dans l'ordre mais ça marche
        if (direction == 0){
            if (j.getY() >= 2 && !(j.getY() -2 == adversaire.getY() && j.getX() == adversaire.getX()) && !mur){
                r = true; // veut dire que le mouvement est possible
            }
        } else if (direction == 1 && !(j.getX() +2 == adversaire.getX() && j.getY() == adversaire.getY()) && !mur){
            if (j.getX() <= size-2){
                r = true;
            }
        } else if (direction == 2 && !(j.getY() +2 == adversaire.getY() && j.getX() == adversaire.getX()) && !mur){
            if (j.getY() <= size - 2){
                r = true;
            }
        } else if (direction == 3 && !(j.getX() -2 == adversaire.getX() && j.getY() == adversaire.getY()) && !mur){
            if (j.getX() >= 2){
                r = true;
            }
        } else {
            r = false;
        }
        return r; //true si le mouvement est possible, false sinon
    }

    public ArrayList<Node> mursDispos(Joueur j){
        ArrayList<Node> mursDispos = new ArrayList<Node>();
        ArrayList<String> emplacements = this.getEmplacementsMurs();
        for (String emplacement : emplacements){
            if (this.murPossible(j, emplacement)){
                mursDispos.add(new Node(null, Integer.parseInt(emplacement.split(":")[0]), Integer.parseInt(emplacement.split(":")[1]), 0, 0));
            }
        }
        return mursDispos;
    }

    public ArrayList<Node> casesAccessibles(Joueur j){
        ArrayList<Node> mouvementsPossibles = new ArrayList<Node>();
        for (int i = 0; i < 4; i++){
            if (this.mouvementPossible(j, i)){
                if (i == 0){
                    mouvementsPossibles.add(new Node(null, j.getX(), j.getY() - 2, 0, 0));
                } else if (i == 1){
                    mouvementsPossibles.add(new Node(null, j.getX() + 2, j.getY(), 0, 0));
                } else if (i == 2){
                    mouvementsPossibles.add(new Node(null, j.getX(), j.getY() + 2, 0, 0));
                } else if (i == 3){
                    mouvementsPossibles.add(new Node(null, j.getX() - 2, j.getY(), 0, 0));
                }
            }
        }
        return mouvementsPossibles;
    }

    //cette fonction récupère les emplacements initiaux possible de tout les murs dans une liste
    //une amélioration possible serait d'appeller cette fonction a chaque fois que mur est demandé en enlevant de la liste les coordonnées déjà prises par un autre mur
    //les coordonnées possibles sont les centres des murs, dont les coordonnées sont un couple de valeurs impaires selon la logique de notre grille
    public ArrayList<String> getEmplacementsMurs(){
        ArrayList<String> emplacements = new ArrayList<>(); // l'Arraylist de sortie
        for(int i = 0; i < this.getSize(); i++){ //pour chaque colonne
            for(int j = 0; j < this.getSize(); j++){ //pour chaque ligne
                if ((i % 2) == 1 && (j % 2) == 1) { //si les deux coordonnées sont impairs
                    if (grille[i][j] == null){ //si la case est vide
                        emplacements.add(i+":"+j); //on ajoute ce couple au tableau de retour
                    }
                }
            }
        }
        return emplacements; //on retourne le tableau
    }

    public void movePlayerAvecCoords(int x, int y){
        grille[this.joueurCourant.getX()][this.joueurCourant.getY()] = "  ";//on efface graphiquement le joueur de la grille
        this.joueurCourant.setX(x);
        this.joueurCourant.setY(y);
        grille[this.joueurCourant.getX()][this.joueurCourant.getY()] = this.joueurCourant.getName(); //on affiche le joueur
    }

    public void movePlayer(Joueur j, int direction) {
        //0 pour en haut, 1 pour droite, 2 pour bas, 3 pour gauche
        grille[j.getX()][j.getY()] = "  ";//on efface graphiquement le joueur de la grille
        if (direction == 0) {//haut
            j.setY(j.getY() - 2);//on change la position du joueur
            grille[j.getX()][j.getY()] = j.getName(); //on affiche le joueur
        } else if (direction == 1) {
            j.setX(j.getX() + 2);
            grille[j.getX()][j.getY()] = j.getName();
        } else if (direction == 2) {
            j.setY(j.getY() + 2);
            grille[j.getX()][j.getY()] = j.getName();
        } else if (direction == 3) {
            j.setX(j.getX() - 2);
            grille[j.getX()][j.getY()] = j.getName();
        }

    }
    
    public void moveIA(Ia2 j, Node caseArrivee) {
        //0 pour en haut, 1 pour droite, 2 pour bas, 3 pour gauche
        grille[j.getX()][j.getY()] = "  ";//on efface graphiquement le joueur de la grille
        grille[j.getX()][j.getY()] = j.getName(); //on affiche le joueur
    }


//autres setters / getters
    public void setJoueurCourant(Joueur j){
        this.joueurCourant = j;
    }

    public Joueur getJoueurCourant(){
        return this.joueurCourant;
    }

    public int getSize(){
        return this.size;
    }

    //Fonction changeant le joueur courant en fin de tour
    public void switchPlayer(){
        if (this.getJoueurCourant() == j1){
            this.setJoueurCourant(j2);
        } else {
            this.setJoueurCourant(j1);
        }
    }

    public boolean partieFinie(){
        if (this.getJoueurCourant().getName() == "J1"){
        if (this.getSize() -1 == this.getJoueurCourant().getY()){
            return true;
        } else {
            return false;
        }
        } else {
            if (this.getJoueurCourant().getY() == 0){
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean cheminsVersArrivee(String coords, Grille grille, String centreMur, String directionMur) {
        if (ia1Funct.murLegal(coords, grille, centreMur, directionMur)) {
            return true;
        } else {
            return false;
        }
    }

    //Redéfinition de toString pour l'affichage de la grille à chaque étape
    public String toString(){
        String r = "";//définition d'une chaine vide
        for (int j=0; j<size; j++){ // début de ligne
            r += "|"; //barre de début de ligne
            for(int i=0; i<size; i++){
                if (this.grille[i][j] == null){ //case vide
                    r += "  ";//on affiche un blanc
                } else { //case pleine
                    r += this.grille[i][j]; //on affiche ce qu'elle contient (mur ou joueur)
                }
                r += "|";//fin de case
            }
            r += "\n";//fin de ligne
        }

        return r; //on retoune la grille a afficher
    }

}