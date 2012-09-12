import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class test extends JFrame {

    public test() {
       setTitle("Simple example");
       setSize(300, 200);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
    	ArrayList<Integer> a = new ArrayList<Integer>();
    	a.add(1);
    	ArrayList<Integer> b = (ArrayList<Integer>) a.clone();
    	b.add(3);
    	
    	
    	char str = 'A';
    	StdOut.println((int) 'A');
    }
}

