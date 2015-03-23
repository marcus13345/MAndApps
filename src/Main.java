import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import MAndEngine.Engine;

public class Main {
	public static void main(String[] args) {
		try{
			Scanner scan = new Scanner(new FileInputStream(new File("./AppList.cfg")));
			
			ArrayList<String> list = new ArrayList<String>();
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				list.add("" + line);
				
			}
			
			String[] classes = new String[list.size()];
			classes = list.toArray(classes);
			
			Engine engine = new Engine(classes, true);
			engine.run();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
