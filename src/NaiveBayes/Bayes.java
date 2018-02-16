package NaiveBayes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Bayes {

	public static void main(String[] args) throws IOException {
		Train t=new Train();
		t.trainAndTestFile();
	}

}
