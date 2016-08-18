import java.util.ArrayList;

public class Node {

    int attributeNumber;
    int size;
    int npos;
    int nneg;
    int []positiveClassLabels;
    int []negativeClassLabels;
    int depth;
    int label;
    double entropy;
    Node []childList;
    Node parent;


    public Node (int attributeNumber, int size,int npos,int nneg, int []positiveClassLabels, int []negativeClassLabels,int depth,double entropy,  Node []childList, Node parent)
    {
   	 this.attributeNumber = attributeNumber;
   	 this.size = size;
   	 this.npos=npos;
   	 this.nneg=nneg;
   	 this.positiveClassLabels = positiveClassLabels;
   	 this.negativeClassLabels = negativeClassLabels;
   	 childList = new Node[size];
   	 this.depth = depth;
   	 this.parent = parent;    
   	 this.entropy=setEntropy();
   	 this.label=-1;
    }
    public double setEntropy()
    {
   	 int size=npos+nneg;

   	 double pPos,pNeg;
   	 if(npos==0&&nneg==0)
   	 {
   		 pPos=0;
   		 pNeg=0;
   	 }
   	 else
   	 {
   		 pPos= npos/(double)size;
   		 pNeg = nneg/(double)size;
   	 }
   	 if(pPos==0||pNeg==0)
   	 {
   		 entropy=0;
   	 }
   	 else
   	 {
   		 entropy = (float)(-pPos*(Math.log(pPos)/Math.log(2)))-(float) (pNeg*(Math.log(pNeg)/Math.log(2)));
   	 }
   	 return entropy;
    }
}
