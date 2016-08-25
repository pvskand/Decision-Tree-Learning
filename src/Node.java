import java.util.ArrayList;
/*This is the node class of the tree. We have defined each parameter of the class.*/
public class Node {
    
    int attributeNumber;		// the attribute number in the dataset.
    int size;					// size of the child of this node
    int npos;					// number of positive elements in the chidlren
    int nneg;				    // number of negative elements in the children
    int []positiveClassLabels;  // list of positive elements in the children
    int []negativeClassLabels;  // list of negative elements in the children
    int depth;					// depth of the node in the tree
    int label;					// if a leaf node then which class label does it belong to
    int []attribu;				// to check if the current attribute is used as a node already in the path or not
    double entropy;				// calculates the entropy of the attribute
    Node []childList;			// List of all the children 
    int []values;				// Number of distinct values taken by the attribute
    Node parent;				// parent of the node

    /*Node class constructor*/
    public Node (int attributeNumber, int size,int npos,int nneg, int []positiveClassLabels, int []negativeClassLabels,int depth,double entropy,  Node []childList, Node parent)
    {
   	 this.attributeNumber = attributeNumber;	// assigining attribute number
   	 this.size = size;							// assigining size 
   	 this.npos=npos;							// assigining positive children
   	 this.nneg=nneg;						 	// assigining negative children
   	 this.positiveClassLabels = positiveClassLabels; // assigining +ve class labels
   	 this.negativeClassLabels = negativeClassLabels; // assigining -ve class labels
   	 childList = new Node[size];					// Allocation size memory to the list
   	 this.depth = depth;							// assigining the depth of the node
   	 this.parent = parent;    						// assigining parent of the node
   	 this.entropy=setEntropy();						// setting the entropy
   	 this.label=-1;									// initializing with label = -1
    }
    /*Setting the entropy of the node*/
    public double setEntropy()
    {
   	 int size=npos+nneg;	// total size

   	 double pPos,pNeg;		// +ve and -ve elements
   	 if(npos==0&&nneg==0)
   	 {
   		 pPos=0;
   		 pNeg=0;
   	 }
   	 else
   	 {
   		 pPos= npos/(double)size;  // probability of +ve
   		 pNeg = nneg/(double)size; // probability of -ve
   	 }
   	 if(pPos==0||pNeg==0)
   	 {
   		 entropy=0;			// if no instances of +ve and -ve then entropy = 0
   	 }
   	 else
   	 {
   		 entropy = (float)(-pPos*(Math.log(pPos)/Math.log(2)))-(float) (pNeg*(Math.log(pNeg)/Math.log(2))); // entropy formula
   	 }
   	 return entropy;	// return entropy
    }
}
