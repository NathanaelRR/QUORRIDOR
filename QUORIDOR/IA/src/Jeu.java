package src; 
import java.util.ArrayList;
import java.util.Scanner;

import src.Grille;
import src.Ia;
import src.Joueur;

public class Jeu  {

    private Grille grille;
    private Joueur joueur1;
    private Joueur joueur2;

    public Jeu (Grille grille,Joueur joueur1,Joueur joueur2){
        this.grille = grille;
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
    }

    public void setGrille (Grille grille){
        this.grille = grille;
    }

    public void setJoueur1 (Joueur joueur1){
        this.joueur1 = joueur1;
    }

    public void setJoueur2 (Joueur joueur2){
        this.joueur2 = joueur2;
    }

    public Grille getGrille (){
        return this.grille;
    }

    public Joueur getJoueur1 (){
        return this.joueur1;
    }

    public Joueur getJoueur2 (){
        return this.joueur2;
    }

    //main a bouger dans une classe a part
    public static void main(String[] args){
        //définition des joueurs
        Joueur j1 = new Joueur("J1",8, 0);
        Joueur j2 = new Joueur("J2",8, 16);
        Ia2 ia = new Ia2("J2",8, 16, 0);
        Ia ia2 = new Ia("J2",0, 16);

        boolean messDir = true;
        boolean messAct = true;

        //définition de la grille
        Grille jeu = new Grille(17, j1, ia);
        Jeu main = new Jeu(jeu, j1, ia);

        //premier affichage de la grille de départ
        String r = jeu.toString();
        System.out.println(r);

        //BOUCLE DE JEU : ATTENTION AUX WHILE(s), NE PAS TOUCHER TANT QUE CE N'EST PAS NECESSAIRE
        //Boucle principale
        Boolean partieEnCours = true;
        while (partieEnCours) {
            if (jeu.getJoueurCourant().getClass() == Joueur.class){
            //début de tour, on affiche le joueur qui doit jouer
            System.out.println("Joueur courant :" + jeu.getJoueurCourant());
            //définition de l'objet de lecture du terminal
            Scanner scanner = new Scanner(System.in);

            //Boucle des actions possibles, boucle tant qu'une action n'a pas été faite, permet aussi de quitter le jeu
            Boolean actionError = true;
            while (actionError) {
                //proposition des actions
                System.out.println("choisissez une action : tapez bouger pour bouger, mur poser un mur, quit pour arrêter la partie");
                String action = scanner.next();

                //action bouger
                if (action.equals("bouger")) {
                    
                    //Boucle du choix de direction, applique les tests et boucle tant que le joueur choisit des directions dans lesquelles il ne peut pas bouger
                    //ou s'il tape autre chose que des directions, il peut aussi faire retour si necessaire
                    Boolean moveError = true;
                    while (moveError) {
                        //proposition des directions
                        messAct = false;
                        System.out.println("Taper haut, droite, bas ou gauche pour choisir votre direction, tapez retour pour choisir une autre action");
                        
                        String direction = scanner.next().toLowerCase();//on change tout en minuscule pour éviter les fautes de frappe
                        //test de la direction
                        if (direction.equals("haut")) {//si haut
                            if (jeu.mouvementPossible(jeu.getJoueurCourant(), 0)) {//vérifie si le mouvement est possible
                                jeu.movePlayer(jeu.getJoueurCourant(), 0);//si oui on applique le mouvement
                                System.out.println("Mouvement effectué");//confirmation
                                moveError = false;//on quitte la boucle des erreurs de mot pour la direction
                                actionError = false;//on quitte la boucle des actions pour laisser la place a l'autre joueur
                                
                            } else {
                                System.out.println("Impossible de bouger vers le haut");//on prévient que le mouvement n'est pas possible
                            }

                        } else if (direction.equals("droite")) {
                            if (jeu.mouvementPossible(jeu.getJoueurCourant(), 1)) {
                                jeu.movePlayer(jeu.getJoueurCourant(), 1);
                                System.out.println("Mouvement effectué");
                                moveError = false;
                                actionError = false;
                                
                            } else {
                                System.out.println("Impossible de bouger vers la droite");
                            }

                        } else if (direction.equals("bas")) {
                            if (jeu.mouvementPossible(jeu.getJoueurCourant(), 2)) {
                                jeu.movePlayer(jeu.getJoueurCourant(), 2);
                                System.out.println("Mouvement effectué");
                                moveError = false;
                                actionError = false;
                                
                            } else {
                                System.out.println("Impossible de bouger vers le bas");
                            }

                        } else if (direction.equals("gauche")) {
                            if (jeu.mouvementPossible(jeu.getJoueurCourant(), 3)) {
                                jeu.movePlayer(jeu.getJoueurCourant(), 3);
                                System.out.println("Mouvement effectué");
                                moveError = false;
                                actionError = false;
                            } else {
                                System.out.println("Impossible de bouger vers la gauche");
                            }

                        } else if (direction.equals("retour")) {//si la personne tape retour on quitte la boucle des directions pour retourner dans celle des actions
                            moveError = false;
                        } else { //sinon, le mot tapé n'est pas reconnu, on reste dans la boucle
                            System.out.println("Mot non reconnu veuillez réessayer");
                        }
                    }
                //si l'action choisie est de quitter le jeu
                } else if (action.equals("quit")) {
                    messAct = false;
                    actionError = false;//on quitte la boucle des actions
                    partieEnCours = false;//on quitte la boucle de jeu
                //si l'action choisie est un mur
                } if (action.equals("mur")){
                    //on rentre dans la boucle de vérification des murs
                    messAct = false;
                    Boolean wallError = true;
                    while (wallError) {
                        //on récupère les emplacements de base des murs (voir amélioration possible au niveau de la fonction)
                        ArrayList<String> emplacements = jeu.getEmplacementsMurs();
                        //on explique comment donner les coordonées
                        System.out.println("choisissez des coordonnées pour le centre du mur au même format que le message suivant (par exemple 1:1), ou retour");
                        //on donne les coordonnées possibles
                        System.out.println(emplacements);
                        messDir = true;
                        //on récupère l'input du joueur
                        String centreMur = scanner.next();
                        //si le joueur tape retour
                        if (centreMur.equals("retour")){
                            wallError = false; //on retourne au choix des actions
                        } else { //sinon on considère qu'il a essayé de taper une 
                            
                            for (String s : emplacements) { //pour chaque emplacement dans la liste
                                if (s.equals(centreMur)) { //on vérifie si ça correspond a l'emplacement donné par le joueur
                                    //si oui, on entre dans la boucle du choix de direction
                                    boolean directionError = true;
                                    while(directionError){
                                        //on demande une direction
                                        if (messDir){
                                            System.out.println("Choisissez une direction : h pour horizontal, v pour vertical");
                                        }
                                            String direction = scanner.next(); //on recupère la réponse
                                        if (direction.equals("v") || direction.equals("h")){ //on vérifie que ça corresponde au bon format
                                            
                                            if (jeu.murPossible(jeu.getJoueurCourant(), centreMur)&&ia2.murLegal(jeu.getJoueurCourant().getCoords(), jeu, centreMur, direction)){ //on vérifie que le mur peut être posé
                                                jeu.placerMur(jeu.getJoueurCourant(), centreMur, direction);//si oui on place le mur
                                                directionError = false; //on quitte les boucles pour laisser la place a l'autre joueur
                                                wallError = false;
                                                actionError = false;
                                                messDir = false;
                                            }
                                            
                                        }
                                        //si le joueur a tapé autre chose que v ou h ou que la vérification de la possibilité échoue
                                        // on retourne dans la boucle du choix de coordonnée où les tests ne peut pas bloquer
                                        // A AMELIORER POUR LES MESSAGES DE RETOUR
                                        //on quitte la boucle de choix de direction
                                        if (messDir) {
                                            System .out.println("erreur lors du placement du mur, choisissez un autre emplacement ou une autre direction");     
                                        }
                                        
                                        directionError = false;
                                    }
                                }
                            }

                        }
                    }
                } else {
                    if (messAct) {
                        //si le mot fourni n'est pas une action
                        System.out.println(action);
                        //on en redemande
                        System.out.println("Mot non reconnu, veuillez réessayer");
                    }
                }
            }
        } else if (jeu.getJoueurCourant().getClass() == Ia2.class){
            
            Node start = new Node(null, jeu.getJoueurCourant().getX(), jeu.getJoueurCourant().getY(), 0, 0);
            ArrayList<Node> goal = new ArrayList<>();
            for (int i = 0; i < 17; i++){
                        goal.add(new Node(null, i, 0, 0, 0));
                }
            
            ArrayList<Node> result = ia.bestFirstSearch(jeu, start, goal);
            System.out.println(result);
            jeu.moveIA(ia, result.get(1));
        }
        
            //en fin de tour :
            System.out.println(jeu);//on print la grille avec l'objet grille.toString
            System.out.println(jeu.getJoueurCourant());
            jeu.switchPlayer();//on change de joueur
            //Test de fin de partie a chaque fin de tour
            if (jeu.getJoueur1().getY() == jeu.getSize()- 1){//si le joueur 1 est sur la  dernière ligne
                System.out.println("J1 gagne la partie");//on averit de la victoire de J1
                partieEnCours = false;//on termine la boucle de jeu
            } else if (jeu.getJoueur2().getY() == 0) { //si J2 a atteint la ligne du haut
                System.out.println("J2 gagne la partie");//on averit de la victoire de J2
                partieEnCours = false;//on termine la boucle de jeu
            }
        }
    }

}
