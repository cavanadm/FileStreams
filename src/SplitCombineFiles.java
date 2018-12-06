//Program By: Dylan Cavanaugh
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class SplitCombineFiles {

	public static void main(String[] args) {
		//Create File to split and put into the splitFolder
		File splitThis = new File(".\\splitFolder\\splitThis.txt");
		
		//Create all of the Files for the Combine method which will be stored in the combineFolder
		File combine1 = new File(".\\combineFolder\\combineThis1.txt");
		File combine2 = new File(".\\combineFolder\\combineThis2.txt");
		File combine3 = new File(".\\combineFolder\\combineThis3.txt");
		File combine4 = new File(".\\combineFolder\\combineThis4.txt");
		File combine5 = new File(".\\combineFolder\\combineThis5.txt");

		//Try with resources all of the above files into FileWriters so they are able to be written into
		try (FileWriter f = new FileWriter(splitThis);
				FileWriter fw1 = new FileWriter(combine1);
				FileWriter fw2 = new FileWriter(combine2);
				FileWriter fw3 = new FileWriter(combine3);
				FileWriter fw4 = new FileWriter(combine4);
				FileWriter fw5 = new FileWriter(combine5);) {
			
			//Write to the split file
			f.write("this is the text that will be split into multiple files");
		
			//Write data into the combine files
			fw1.write("combine " + 1 + " has been added; ");
			fw2.write("combine " + 2 + " has been added; ");
			fw3.write("combine " + 3 + " has been added; ");
			fw4.write("combine " + 4 + " has been added; ");
			fw5.write("combine " + 5 + " has been added; ");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Create the number of files to split into then execute the split with the file and the number of files
		int splitNum = 5;
		SplitFile(splitThis, splitNum);
		
		//Create file that group of files will be combined into then execute combine of the files
		File combineHere = new File(".\\combineFolder\\combined.txt");
		CombineFile(combineHere, combine1, combine2, combine3, combine4, combine5);

	}

	public static void SplitFile(File f, int num) {
		//Determine the number of bytes for each file
		int parts = (int) f.length() / num;

		//Create the inputStream for the input file
		try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));) {
			//Create new file to dump new data into outside of loop so will be reinstantiated every loop
			File fil = null;

			//x is just the counter for naming the new files generated
			int x = 1;
			
			//Before anything starts, the length of the original file is output
			System.out.println("Starting file: " + f.length());
			
			//This loop creates each new file
			while (x <= num) {
				//Uniquely name each new file created
				fil = new File(".\\splitFolder\\splitThis" + x + ".txt");
				//The output stream is created inside the loop so that each file will be on a new stream
				BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(fil));

				//This loop takes parts from above and loops through and writes out the appropriate bytes
				//to each file determined by the parts variable
				for (int i = 0; i <= parts; i++) {
					int n = input.read();
					//If the stream reaches the end, -1 will be the result and end output to the current file
					if (n > -1)
						output.write(n);

				}
				//After the loop increment the "counter"
				x++;
				
				//Close the outputStream or nothing will work I found out after an hour and a half
				output.close();
				
				//Every time the new file has the information, output the length of the new file generated
				System.out.println("File " + x + ": " + fil.length());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void CombineFile(File combine, File... f) {
		//Because the file to be written to will be the same every time, the FileWriter
		//has to be outside the ForEach loop underneath, otherwise it will be overwritten every iteration
		try (FileWriter output = new FileWriter(combine);) {
			//For each file from the VarArgs
			for (File fil : f) {
				//Create InputStream
				try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(fil));) {
					
					//Since this method wants to go to the end of each file and append to the end,
					//this makes it so as long as there are bytes to read, it will continue to read
					while (input.available() > 0) {
						
						//Read in the input then write it out to the combine file
						int n = input.read();
						if (n > -1)
							output.write(n);
					}
				}
				//Retrieve the name from input file then give the length
				System.out.println(fil.getName() + ": " + fil.length());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//After all is said and done, output the length of the combined file
			System.out.println("Total Combine: " + combine.length());
		}

	}

}
