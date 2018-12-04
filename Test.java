import java.io.*;
import java.util.*;

public class Test {

	public static void main(String[] args) {

		try {
			TreeMap<Long, String> values = new TreeMap<>();
			String PATH = System.getProperty("user.dir");
			File logPath = new File(PATH + "/csTimes.log");
			Scanner in = new Scanner(logPath);
			while (in.hasNext()) {
				String line = in.next();
				if (line.isEmpty())
					continue;
				String[] fields = line.split(":::");
				values.put(Long.valueOf(fields[0]), fields[1]);
			}
			in.close();

			String oldValue = null;
			int counter = 1;
			for (String value : values.values()) {
				String[] line;
				if (oldValue != null && counter % 2 == 0) {
					line = value.split("-");
					String[] previousLine = oldValue.split("---");
					if (previousLine[0].equals(line[0])) {
						if (previousLine[1].equalsIgnoreCase("Begin") && line[1].equalsIgnoreCase("Finish")) {
							oldValue = null;
							counter++;
							continue;
						}
					} else if ((previousLine[1].equalsIgnoreCase("Begin") && line[1].equalsIgnoreCase("Begin"))
							|| (previousLine[1].equalsIgnoreCase("Finish") && line[1].equalsIgnoreCase("Finish"))) {
						System.out.println("Program not working...");
						return;
					} else if (previousLine[1].equalsIgnoreCase("Begin") && line[1].equalsIgnoreCase("Finish")) {
						System.out.println("Program not working...");
						return;
					}
				}
				oldValue = value;
				counter++;
			}
			System.out.println("Program is working..!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
