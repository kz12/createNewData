import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BasicNode fulltree = new CircleNode(Data.mammals);
		fulltree.init();
//		System.out.println(fulltree);
		fulltree.generateNewData();
//		System.out.println(fulltree.storeNewData());
//		System.out.println(fulltree.lengthbr);
//		System.out.println(Data.cross_species);
		PrintStream out = null;
		try {
		    out = new PrintStream(new FileOutputStream("mammals.txt"));
		    out.print(fulltree.storeNewData());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
		    if (out != null) out.close();
		    System.out.println("over");
		}
	}

}
