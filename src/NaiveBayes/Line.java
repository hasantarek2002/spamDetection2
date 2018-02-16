package NaiveBayes;

public class Line {
	private String line;
	private boolean visited;
	
	public Line(String line) {
		this.line=line;
		visited=false;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	
}
