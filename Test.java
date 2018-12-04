import java.io.*;
import java.util.*;

public class Test {

	public static void main(String[] args) {

		try {
			ArrayList<Long> times = new ArrayList<>();
			ArrayList<String> values = new ArrayList<>();
			String PATH = System.getProperty("user.dir");
			File logPath = new File(PATH + "/csTimes.log");
			Scanner in = new Scanner(logPath);
			while (in.hasNext()) {
				String line = in.next();
				if (line.isEmpty())
					continue;
				String[] fields = line.split(":::");
				times.add(Long.valueOf(fields[0]));
				values.add(fields[1]);
			}
			in.close();

			String oldValue = null;
			int counter = 1;
			for (String value : values) {
				if (oldValue != null && counter % 3 == 0) {
					String[] line = value.split("---");
					String[] previousLine = oldValue.split("---");					
					if (previousLine[0].equals(line[0])) {
						if (previousLine[1].equalsIgnoreCase("Begin") && line[1].equalsIgnoreCase("Finish")) {
							oldValue = null;
							counter++;
							continue;
						} else {
							System.out.println("Program not working...");
							return;
						}
					} else {
						System.out.println("Program not working...");
					}
				}
				oldValue = value;
				counter++;
			}
			System.out.println("Program is working..!");

			Long responseTime = 0L;
			Long reqTime = null;
			counter = 1;
			for (Long time : times) {
				if (counter % 3 == 1) {
					reqTime = time;
					counter++;
				} else if (counter % 3 == 2) {
					counter++;
				} else if (counter % 3 == 0) {
					responseTime = responseTime + (time - reqTime);
					counter++;
				}
			}
			responseTime = responseTime / (counter / 3);
			System.out.println("Average response time is: " + responseTime + "ms");

			/*Long throughput = 0L;
			Long startTime = null;
			counter = 1;
			for (Long time : times) {
				if (counter % 3 == 1) {
					counter++;
				} else if (counter % 3 == 2) {
					startTime = time;
					counter++;
				} else if (counter % 3 == 0) {
					throughput = throughput + (time - startTime);
					counter++;
				}
			}
			throughput = throughput / (counter / 3);
			System.out.println("Average throughput time is: " + throughput + "ms");*/
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
