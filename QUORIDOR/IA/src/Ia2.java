package src;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Ia2 extends Joueur{
    
    private int cost = 0;

    public Ia2(String name, int x, int y, int cost) {
        super(name, x, y);
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getHeuristic(int x, int y, int goalX, int goalY) {
        return Math.abs(x - goalX) + Math.abs(y - goalY);
    }


    public ArrayList<Node> bestFirstSearch(Grille grille, Node start, ArrayList<Node> goal) {
        
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();
        HashMap<Node, Node> parentMap = new HashMap<>();

        openList.add(start);
        closedList.add(start);

        
        parentMap.put(start, null);


        while (!openList.isEmpty()) {
            Node currentNode = openList.get(0);
            openList.remove(0);
            for (Node node : goal) {
                if (currentNode.compareTo(node) == 0) {
                    // Backtrack to construct the path
                    ArrayList<Node> path = new ArrayList<>();
                    Node node2 = currentNode;
                    while (node2 != null) {
                        path.add(0, node2);
                        node2 = parentMap.get(node2);
                    }
                    System.out.println("path : " + path);
                    return path;
                }
            }
            closedList.add(currentNode);

            Node[] neighbors = getNeighbors(currentNode, grille);
            for (Node neighbor : neighbors) {
                if (neighbor != null) {
                    
                    boolean isInOpen = false;
                    for (Node openNode : openList) {
                        if (openNode.compareTo(neighbor) == 0) {
                            isInOpen = true;
                            break;
                        }
                    }
                    boolean isInClosed = false;
                    for (Node closedNode : closedList) {
                        if (closedNode.compareTo(neighbor) == 0) {
                            isInOpen = true;
                            break;
                        }
                    }
                    if (!isInClosed && !isInOpen) {
                    
                        openList.add(neighbor);
                        parentMap.put(neighbor, currentNode);
                    }
                    
                }
            }
        }
        return null; // No path found
    }

    public Node[] getNeighbors(Node node, Grille grille) {
        // Implement this method to return the neighbors of a given node.
        Node[] neighbors = new Node[4];
        int x = node.x;
        int y = node.y;
        int g = (int) node.g;

        Node testMur;
        Node testJoueur;

        
        

        //voisin de gauche
        if (x > 0) {
            testMur = new Node(null, x - 1, y, 0, 0);
            testJoueur = new Node(null, x - 2, y, 0, 0);
            if (!grille.isMur(testMur) && !grille.isJoueur(testJoueur)){
                int h = getHeuristic(x - 2, y, grille.getJoueurCourant().getX(), grille.getJoueurCourant().getY());
                neighbors[0] = new Node(node, x - 2, y, g + 1, h);
            } else {
                neighbors[0] = null;
            }
        } else {
            neighbors[0] = null;
        } 
        //voisin de doite
        if (x < grille.getSize() - 2) {
            testMur = new Node(null, x + 1, y, 0, 0);
            testJoueur = new Node(null, x + 2, y, 0, 0);
            if (!grille.isMur(testMur) && !grille.isJoueur(testJoueur)){
                int h = getHeuristic(x + 2, y, grille.getJoueurCourant().getX(), grille.getJoueurCourant().getY());
                neighbors[1] = new Node(node, x + 2, y, g + 1, h);
            } else {
                neighbors[1] = null;
            }
        } else {
            neighbors[1] = null;
        }
        //voisin du haut
        if (y > 0) {
            testMur = new Node(null, x, y - 1, 0, 0);
            testJoueur = new Node(null, x, y - 2, 0, 0);
            if (!grille.isMur(testMur) && !grille.isJoueur(testJoueur)){
                int h = getHeuristic(x, y - 2, grille.getJoueurCourant().getX(), grille.getJoueurCourant().getY());
                neighbors[2] = new Node(node, x, y - 2, g + 1, h);
            } else {
                neighbors[2] = null;
            }
        } else {
            neighbors[2] = null;
        }
        //voisin du bas
        if (y < grille.getSize() - 2 ) {
            testMur = new Node(null, x, y + 1, 0, 0);
            testJoueur = new Node(null, x, y + 2, 0, 0);
            if (!grille.isMur(testMur) && !grille.isJoueur(testJoueur)){
                int h = getHeuristic(x, y + 2, grille.getJoueurCourant().getX(), grille.getJoueurCourant().getY());
                neighbors[3] = new Node(node, x, y + 2, g + 1, h);
            } else {
                neighbors[3] = null;
            }
        } else {
            neighbors[3] = null;
        }

        

        return neighbors;
        
    }

    public boolean murPossible(String centreMur, String direction, Grille grille){

       
        Grille copieGrille = new Grille(grille.getSize(), grille.getJoueur1(), grille.getJoueur2());
        for(int i = 0; i < grille.getSize(); i++){ //pour chaque colonne
            for(int j = 0; j < grille.getSize(); j++){ //pour chaque ligne
                copieGrille.getGrille()[i][j] = grille.getGrille()[i][j];
            }
        }
        this.placerFauxMur(copieGrille, centreMur, direction);

        ArrayList<Node> goalJ1 = new ArrayList<>(); 
        ArrayList<Node> goalJ2 = new ArrayList<>();
        for (int i = 0; i < copieGrille.getSize(); i++){
            goalJ1.add(new Node(null, i, copieGrille.getSize()-1, 0, 0));
            goalJ2.add(new Node(null, i, 0, 0, 0));
        } 
        ArrayList<Node> cheminJ1 = bestFirstSearch(copieGrille, new Node(null, grille.getJoueur1().getX(), grille.getJoueur1().getY(), 0, 0), goalJ1);
        ArrayList<Node> cheminJ2 = bestFirstSearch(copieGrille, new Node(null, grille.getJoueur2().getX(), grille.getJoueur2().getY(), 0, 0), goalJ2);

        if (cheminJ1 == null || cheminJ2 == null){
            return false;
        } else {
            return true;
        }
    }

    public void placerFauxMur(Grille grille, String coords, String direction){
        String[] coordonnees = coords.split(":");
        int x = Integer.parseInt(coordonnees[0]);
        int y = Integer.parseInt(coordonnees[1]);
        if (direction.equals("v")){
            grille.getGrille()[x][y] = " X";
            grille.getGrille()[x][y+1] = " X";
            grille.getGrille()[x][y-1] = " X";
        } else if (direction.equals("h")){
            grille.getGrille()[x][y] = " X";
            grille.getGrille()[x+1][y] = " X";
            grille.getGrille()[x-1][y] = " X";
        }
    }
}
