package tasks;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;         //to parse text files
import org.jsoup.Jsoup;           //to parse html files
import org.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument; //to parse pdf files
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Aishwarya_John_Task4 {
	public static int editDistance(String w1, String w2) {
		int length1 = w1.length();
		int length2 = w2.length();

	//	2D array arr with dimensions [len1+] x [len2+]
		int[][] arr = new int[length1 + 1][length2 + 1];
		// Initialize the first column of arr with the values 0 to len1
		for (int i = 0; i <= length1; i++) {
			arr[i][0] = i;
		}
		// Initialize the first row of arr with the values 0 to len2
		for (int j = 0; j <= length2; j++) {
			arr[0][j] = j;
		}

		// Iterate through the characters of word1 and word2
		for (int i = 0; i < length1; i++) {
			char char1 = w1.charAt(i);
			for (int j = 0; j < length2; j++) {
				char char2 = w2.charAt(j);

				// If the last characters of the two substrings are equal
				if (char1 == char2) {
					// Update the arr value for substrings of length (i+1) and (j+1)
					// to be the same as the arr value for substrings of length i and j
					arr[i + 1][j + 1] = arr[i][j];
				} else {
					int replace = arr[i][j] + 1;           // 1. Replace c1 with c2
					int insert = arr[i][j + 1] + 1;        // 2. Insert c2
					int delete = arr[i + 1][j] + 1;        // 3. Delete c1
					// Update the arr value for substrings of length (i+1) and (j+1) to be the minimum
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					arr[i + 1][j + 1] = min;
				}
			}
		}

		return arr[length1][length2];
	}
	
	public static List<String> findSimilarWords(String word, String fp) throws IOException {
        List<String> similarWords = new ArrayList<String>();			//array list similarWords store all the words similar to the given word in the file

        // Extract text based on the file's extension
        String extractedtext = null; // variable to store the text extracted
        //if file is a pdf
        if (fp.endsWith(".pdf")) {
        	
            PDDocument document = PDDocument.load(new File(fp)); 		//variable document will have the loaded pdf file
            PDFTextStripper stripper = new PDFTextStripper();          //PDFTextStripper extracts the text from a pdf file
            extractedtext = stripper.getText(document); 			  //extractedtext variable stores the text extracted from the pdf file
            document.close(); 										 //once the text is extracted, close the document
        } 
        //if file is an html file
        else if (fp.endsWith(".html") || fp.endsWith(".htm"))       //if file extension ends with either".html" or ".htm", it is an HTML file
        {
            Document doc = Jsoup.parse(new File(fp), "UTF-8");      // Jsoup will parse the HTML file
            extractedtext = doc.body().text();						// extractedtext will have the extracted content from the html file
        } 

        else if (fp.startsWith("http")) { // check if it is a webpage
            Document doc = Jsoup.connect(fp).get(); // connect to the URL 
            extractedtext = doc.body().text(); // extractedtext variable stores the text extracted from the webpage
        }
        
      //if file is a .txt file
        else if (fp.endsWith(".txt")) 								
        {		
            BufferedReader br = new BufferedReader(new FileReader(fp));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");                   //keeps appending all the words of the txt file
            }
            br.close();
            extractedtext = sb.toString();						//contains extracted content from the txt file
        }

        // Split the extracted text into words and search for similar words
        String[] words = extractedtext.split("\\s+");             //declared a string array words to store the extracted words from the file and will split characters at whitespace
        for (String wordy : words) {								// wordy is an iterator
        	 if (!similarWords.contains(wordy)) {				// if the arraylist SimilarWords does not already contain the word, then add it, else skip
        		 similarWords.add(wordy);						//if conditions are fulfilled, add the words to the array list similarWords
        	 }
        }
        
        return similarWords;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Enter a word for which other similar words need to be found from the file: "); //take input word form the user
        Scanner sc=new Scanner(System.in);
        String word=sc.next();
        String filepath = "https://en.wikipedia.org/wiki/Main_Page";	//give file path, only give one file at a time to run the program
        List<String> similarWords = new ArrayList<String>();  //array list similarWords stores all similarwords
       
        similarWords.addAll(findSimilarWords(word, filepath)); //call findSimilarWords function
        
        
        Collections.sort(similarWords, (a, b) -> editDistance(word, a) - editDistance(word, b));  //sorting the array list

        //printing results
        int count = 0;								//initializing count variable to 0
        System.out.println("The most similar words in ascending order are\n");
        for (String displaywords : similarWords) {		//displaywords will iterate through al the words in the array list
            int distance = editDistance(word, displaywords); //calculate edit distance
            
            System.out.println(displaywords + ": " + distance);
            count++;                              //increment count as the words are displayed

        }
        System.out.println("Total no. of words in the file for which edit distance was found were: "+count); //displays total no. of words in the file
        

    }
}

	