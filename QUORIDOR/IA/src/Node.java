package src;

public class Node implements Comparable<Node> {
        public Node parent;
        public int x, y;
        public double g;
        public double h;
        public double f;
        
        Node(Node parent, int x, int y, double g, double h) {
            this.parent = parent;
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
        }

        public void setF(double f) {
            this.f = f;
        }

        @Override
        public int compareTo(Node o) {
            if (x == o.x && y == o.y) {
                return 0;
            } else {
                return 1;
            }
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }