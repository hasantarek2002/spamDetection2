package NaiveBayes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JOptionPane;

public class Train {
	HashMap<String, Word> words = new HashMap<String, Word>();
	BufferedWriter out;
	BufferedWriter trainOut;
	BufferedWriter testOut;
	ArrayList<Line> spamLine = new ArrayList<Line>();
	ArrayList<Line> hamLine = new ArrayList<Line>();
	int tp, tn, fp, fn;
	
	double accuracy[]=new double[10];
	double precision[]=new double[10];
	double recall[]=new double[10];
	double fScore[]=new double[10];

	public void trainAndTestFile() throws IOException {
		readFromFile();
		int spamTrainData = spamLine.size() * 90;
		spamTrainData /= 100;
		int hamTrainData = hamLine.size() * 90;
		hamTrainData /= 100;
		int spamTestData = spamLine.size() - spamTrainData;
		int hamTestData = hamLine.size() - hamTrainData;

		System.out.println("spam train data: " + spamTrainData);
		System.out.println("ham train data: " + hamTrainData);
		System.out.println("spam test data: " + spamTestData);
		System.out.println("ham test data: " + hamTestData);

		/*
		 * writeToTestTrainFile(spamLine, hamLine, spamTrainData, hamTrainData);
		 * writeToTestFile(spamLine, hamLine, spamTestData, hamTestData);
		 * 
		 * 
		 * train("testTrain.txt"); filter("test.txt");
		 * //System.out.println("map size "+words.size());
		 * System.out.println(tp+"    "+tn+"      "+fp+"      "+fn);
		 * System.out.println(tp+tn+fp+fn); initializeLineFromTrainFile();
		 */

		for (int i = 0; i < 10; i++) {
			System.out.println("test >>>>>> "+(i+1));
			System.out.println("///////////////////////////");
			tp = 0;
			tn = 0;
			fp = 0;
			fn = 0;
			writeToTestTrainFile(spamLine, hamLine, spamTrainData, hamTrainData);
			writeToTestFile(spamLine, hamLine, spamTestData, hamTestData);
			train("testTrain.txt");
			filter("test.txt");
			// System.out.println("map size "+words.size());
			System.out.println("TP : "+tp + "  TN : " + tn + "  FP :  " + fp + "  FN :  " + fn);
			System.out.println(tp + tn + fp + fn);

			accuracy[i] = (double) (tp + tn) / (tp + tn + fp + fn);
			//System.out.println("accuracy :" + accuracy);
			//System.out.println("accuracy :" + accuracy[i]);
			
			precision[i] = (double) (tp) / (tp + fp);
			//System.out.println("precision : " + precision);
			//System.out.println("precision : " + precision[i]);

			recall[i] = (double) (tp) / (tp + fn);
			//System.out.println("recall : " + recall);
			//System.out.println("recall : " + recall[i]);

			fScore[i] = (double) (2 * recall[i] * precision[i]) / (recall[i] + precision[i]);
			//System.out.println("F Score : " + fScore[i]);

			initializeLineFromTrainFile();
		}
		
		double accuracySum=0,precisionSum=0,recallSum=0,fScoreSum=0;
		for(int i=0;i<10; i++) {
			accuracySum+=accuracy[i];
			precisionSum+=precision[i];
			recallSum+=recall[i];
			fScoreSum+=fScore[i];
		}
		
		
		System.out.println("Final accuracy: "+(accuracySum/10));
		System.out.println("Final Precision: "+(precisionSum/10));
		System.out.println("Final Recall: "+(recallSum/10));
		System.out.println("Final F Score: "+(fScoreSum/10));

	}

	public void initializeLineFromTrainFile() {
		for (int i = 0; i < spamLine.size(); i++) {
			spamLine.get(i).setVisited(false);
		}
		for (int i = 0; i < hamLine.size(); i++) {
			hamLine.get(i).setVisited(false);
		}
		words.clear();
		// System.out.println("map size "+words.size());

	}

	public void writeToTestFile(ArrayList<Line> spamList, ArrayList<Line> hamList, int spamTestData, int hamTestData)
			throws IOException {
		/*
		 * BufferedWriter bw = null; FileWriter fw = null; String FILENAME = "test.txt";
		 */
		this.testOut = new BufferedWriter(new FileWriter("test.txt"));
		int spamListSize = spamList.size();
		int hamListSize = hamList.size();

		try {
			/*
			 * fw = new FileWriter(FILENAME, true); bw = new BufferedWriter(fw);
			 */
			Random rand = new Random();

			while (true) {
				int index = rand.nextInt(spamListSize);
				if (spamList.get(index).isVisited() == false) {
					// bw.write(spamList.get(index).getLine() + "\n");
					this.testOut.write(spamList.get(index).getLine() + "\n");
					spamList.get(index).setVisited(true);
					spamTestData--;
					if (spamTestData == 0)
						break;
				}
			}
			while (true) {
				int index = rand.nextInt(hamListSize);
				if (hamList.get(index).isVisited() == false) {
					// bw.write(hamList.get(index).getLine() + "\n");
					this.testOut.write(hamList.get(index).getLine() + "\n");
					hamList.get(index).setVisited(true);
					hamTestData--;
					if (hamTestData == 0)
						break;
				}
			}
			this.testOut.close();
			System.out.println("test file Write Done");
			/*
			 * if (bw != null) bw.close();
			 * 
			 * if (fw != null) fw.close();
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void writeToTestTrainFile(ArrayList<Line> spamList, ArrayList<Line> hamList, int spamTrainData,
			int hamTrainData) throws IOException {
		/*
		 * BufferedWriter bw = null; FileWriter fw = null; String FILENAME =
		 * "testTrain.txt";
		 */
		this.trainOut = new BufferedWriter(new FileWriter("testTrain.txt"));
		int spamListSize = spamList.size();
		int hamListSize = hamList.size();

		try {
			/*
			 * fw = new FileWriter(FILENAME, true); bw = new BufferedWriter(fw);
			 */
			Random rand = new Random();
			for (int i = 0; i < spamTrainData; i++) {
				int index = rand.nextInt(spamListSize);
				// bw.write(spamList.get(index).getLine() + "\n");
				this.trainOut.write(spamList.get(index).getLine() + "\n");
				spamList.get(index).setVisited(true);
			}
			for (int i = 0; i < hamTrainData; i++) {
				int index = rand.nextInt(hamListSize);
				// bw.write(hamList.get(index).getLine() + "\n");
				this.trainOut.write(hamList.get(index).getLine() + "\n");
				hamList.get(index).setVisited(true);
			}
			System.out.println("train file Write Done");
			this.trainOut.close();

			/*
			 * if (bw != null) bw.close();
			 * 
			 * if (fw != null) fw.close();
			 */

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void readFromFile() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("train.txt"));
		String line = in.readLine();
		while (line != null) {
			if (!line.equals("")) {
				String type = line.split("\t")[0];
				if (type.equals("spam")) {
					spamLine.add(new Line(line));
				} else {
					hamLine.add(new Line(line));
				}
			}
			line = in.readLine();
		}
		System.out.println("spam line: " + spamLine.size());
		System.out.println("ham line : " + hamLine.size());
		in.close();
	}

	// uses a train-file to make a hashmap containing all words, and their
	// probability of being spam
	public void train(String input) throws IOException {
		int totalSpamCount = 0;
		int totalHamCount = 0;
		BufferedReader in = new BufferedReader(new FileReader(input));
		String line = in.readLine();
		while (line != null) {
			if (!line.equals("")) {
				String type = line.split("\t")[0];
				String sms = line.split("\t")[1];
				for (String word : sms.split(" ")) {
					word = word.replaceAll("\\W", "");
					word = word.toLowerCase();
					Word w = null;
					if (words.containsKey(word)) {
						w = (Word) words.get(word);
					} else {
						w = new Word(word);
						words.put(word, w);
					}
					if (type.equals("ham")) {
						w.countHam();
						totalHamCount++;
					} else if (type.equals("spam")) {
						w.countSpam();
						totalSpamCount++;
					}
				}
			}
			line = in.readLine();
		}
		in.close();

		for (String key : words.keySet()) {
			words.get(key).calculateProbability(totalSpamCount, totalHamCount);
		}
	}

	// Takes the text to be analyzes as input, and produces predictions by form of
	// 'spam' or 'ham'
	public void filter(String inputFile) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(inputFile));
		this.out = new BufferedWriter(new FileWriter("predictions.txt"));
		String line = in.readLine();
		while (line != null) {
			if (!line.equals("")) {
				String type = line.split("\t")[0];
				String message = line.split("\t")[1];
				ArrayList<Word> sms = makeWordList(message);
				boolean isSpam = calculateBayes(sms);
				/*if (isSpam && type.equals("spam")) {
					tp++;
				} else if (isSpam && type.equals("ham")) {
					fp++;
				} else if (isSpam == false && type.equals("ham")) {
					fn++;
				} else {
					tn++;
				}*/
				if (isSpam && type.equals("spam")) {
					tp++;
				} else if (isSpam && type.equals("ham")) {
					fp++;
				} else if (isSpam == false && type.equals("ham")) {
					tn++;
					//fn++;
				} else {
					fn++;
					//tn++;
				}
				/*
				 * if (isSpam == true) this.out.write("spam"); else if (isSpam == false)
				 * this.out.write("ham");
				 */
			}
			this.out.newLine();
			line = in.readLine();
		}
		this.out.close();
		in.close();
	}

	// make an arraylist of all words in an sms, set probability of spam to 0.4 if
	// word is not known
	public ArrayList<Word> makeWordList(String sms) {
		ArrayList<Word> wordList = new ArrayList<Word>();
		for (String word : sms.split(" ")) {
			word = word.replaceAll("\\W", "");
			word = word.toLowerCase();
			Word w = null;
			if (words.containsKey(word)) {
				w = (Word) words.get(word);
			} else {
				w = new Word(word);
				w.setProbOfSpam(0.40f);
			}
			wordList.add(w);
		}
		return wordList;
	}

	// Applying Bayes rule and calculating probability of ham or spam. Return true
	// if spam, false if ham
	public boolean calculateBayes(ArrayList<Word> sms) {
		float probabilityOfPositiveProduct = 1.0f;
		float probabilityOfNegativeProduct = 1.0f;
		for (int i = 0; i < sms.size(); i++) {
			Word word = (Word) sms.get(i);
			probabilityOfPositiveProduct *= word.getProbOfSpam();
			probabilityOfNegativeProduct *= (1.0f - word.getProbOfSpam());
		}
		float probOfSpam = probabilityOfPositiveProduct / (probabilityOfPositiveProduct + probabilityOfNegativeProduct);
		if (probOfSpam > 0.9f)
			return true;
		else
			return false;
	}

}
