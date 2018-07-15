package com.clianz.cloudant.cloudfoundry;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ConfigReader {

	public String readFile(String filename) throws IOException {
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());

		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			scanner.close();

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();
	}
}
