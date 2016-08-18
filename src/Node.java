import java.util.ArrayList;

public class Node {

	int attributeNumber;
	int size;
	int []positiveClassLabels;
	int []negativeClassLabels;
	int depth;
	double entropy;
	Node []childList;
	Node parent;
	

	public Node (int attributeNumber, int size, int []positiveClassLabels, int []negativeClassLabels,int depth,double entropy,  Node []childList, Node parent)
	{
		this.attributeNumber = attributeNumber;
		this.size = size;
		this.positiveClassLabels = positiveClassLabels;
		this.negativeClassLabels = negativeClassLabels;
		childList = new Node[size];
		this.depth = depth;
		this.parent = parent;	
		this.entropy=setEntropy();
	}
	public double setEntropy()
	{
		int size=positiveClassLabels.length+negativeClassLabels.length;
		double pPos = positiveClassLabels.length/(double)size;
		double pNeg = negativeClassLabels.length/(double)size;
		entropy = (float)(-pPos*(Math.log(pPos)/Math.log(2)))-(float) (pNeg*(Math.log(pNeg)/Math.log(2)));
		return entropy;
	}
}