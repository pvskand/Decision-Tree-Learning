
public class Node {

	int attributeNumber;
	int size;
	int positiveClassLabels;
	int negativeClassLabels;
	Node []childList;
	int depth;
	Node parent;
	double entropy;


	public Node (int attributeNumber, int size, int positiveClassLabels, int negativeClassLabels,int depth, Node []childList, Node parent,double entropy)
	{
		this.attributeNumber = attributeNumber;
		this.size = size;
		this.positiveClassLabels = positiveClassLabels;
		this.negativeClassLabels = negativeClassLabels;
		childList = new Node[size];
		this.depth = depth;
		this.parent = parent;	
		this.entropy=entropy;
	}
	public void setEntropy()
	{
		int size=positiveClassLabels+negativeClassLabels;
		double pPos = positiveClassLabels/(double)size;
		double pNeg = negativeClassLabels/(double)size;
		entropy = (float)(-pPos*(Math.log(pPos)/Math.log(2)))-(float) (pNeg*(Math.log(pNeg)/Math.log(2)));
	}
}