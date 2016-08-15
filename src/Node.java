
public class Node {

	int attributeNumber;
	int size;
	int positiveClassLabels;
	int negativeClassLabels;
	Node []childList;
	int depth;
	Node parent;


public Node (int attributeNumber, int size, int positiveClassLabels, int negativeClassLabels,int depth, Node []childList, Node parent)
{
	this.attributeNumber = attributeNumber;
	this.size = size;
	this.positiveClassLabels = positiveClassLabels;
	this.negativeClassLabels = negativeClassLabels;
	childList = new Node[size];
	this.depth = depth;
	this.parent = parent;
	
}


}