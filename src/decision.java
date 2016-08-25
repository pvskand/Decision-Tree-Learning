import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
/*Then main Decision Tree class that has all the functionalities of Decision Tree	*/
public class decision {
	
	/*n = number of instances
	 * m = number of attributes
	 * ninstances = number of instances to pick for the training set
	 * ntrees = number of trees used for feature bagging using random forests
	 * maxHeight = maximum height of the tree in the early stopping criteria
	 */
	int n=0,m=0,ninstances=1000,ntrees=9,maxHeight=5; 
	int npos, nneg; 	// num of 1's and 0's in training set
	int ipos=0,ineg=0;  
	float nones=0,nzeroes=0; 
	int [][]array;			// those instances that are used in making the tree  
	int [][]originalArray;	// storeing the dataset
	int []trainingSet = new int[ninstances]; // training instances
	int []testSet;							// validation set
	Node head=null;							// head node
	int [] nPos;
	int [] nNeg;
	int height = 0;							// height of the tree
	int stopping = 0;						// to handle the stopping criteria
	int [] validInstance = new int[ninstances];	// valid instances 
	int z1=0,z2=0,z=0;						// counting number of 1's and 0's 
	int []attributesCount;					
	int exptno=1;							// initializing experiment number
	ArrayList<Integer> training=new ArrayList<Integer>(); // array list to store the indices of the randomly generated training set
	Node []heads;									// array of nodes
	Stack<Node> s=new Stack<Node>();				// stack used for pruning
	static PrintWriter writer;						// for output
	int accuracyNo=0;						

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		decision obj=new decision();			// object of main class
		writer=new PrintWriter("output.txt");	// PrintWriter to write in the output text file

		int i,nnodes=0;
		obj.input(args[0]);    					// filename taken as input via command line argument
		obj.originalArray=obj.array.clone();	// clone the array so as not to lose info
		obj.attributesCount=new int[obj.m];		// counts how many times an attribute has been used
		double errors[] = {0.5,1,5,10};			// noise percentage to be added
		double accuracy=0;					

		obj.exptno=Integer.parseInt(args[1]);	// taking input of experiment number
		
		/*In this experiment we construct a simple decision tree first and then use early stopping criteria*/
		
		if(obj.exptno==1)						// experiment number is 1
		{
			writer.println("--------------Without any Stopping criteria---------------"+"\n");
			obj.makeTree();		// constructing the tree
			writer.println("Height of the tree 		 = " + obj.height);
			
			accuracy=obj.test(obj.head,0);	// calculating the accuracy on test set
			writer.println("Accuracy on test set 	 = "+accuracy);
			accuracy=obj.test(obj.head,1);  // calculating the accuracy on training set
			writer.println("Accuracy on training set = "+accuracy);
			
			obj.printAfterTest();

			obj.stopping = 1;
			obj.height = 0;
//------------------------------------------Without stopping criteria ends -------------------------------------------
			
			writer.println("\n"+"-------------With Stopping Criteria (Max Information Gain = 0)-----------"+"\n");
			obj.makeTree();  // constructing the tree
			writer.println("Height of the tree 		 = " + obj.height);
			
			accuracy=obj.test(obj.head,0);  // calculating the accuracy on test set
			writer.println("Accuracy on test set	 = "+accuracy);
			accuracy=obj.test(obj.head,1);   // calculating the accuracy on training set
			writer.println("Accuracy on training set = "+accuracy);
			
			obj.printAfterTest();


			obj.stopping = 2;
			obj.height = 0;

			writer.println("\n"+"-------------With Stopping Criteria (Max height of tree = "+obj.maxHeight+")-----------");
			obj.makeTree();    // constructing the tree
			writer.println("Height of the tree 		 = " + obj.height);
			
			accuracy=obj.test(obj.head,0);   // calculating the accuracy on test set
			writer.println("Accuracy on test set 	 = "+accuracy);
			accuracy=obj.test(obj.head,1);   // calculating the accuracy on training set
			writer.println("Accuracy on training set = "+accuracy);
			
			obj.printAfterTest();
			obj.stopping=0;

		}
		if(obj.exptno == 2)
		{
			/*This experiment involves adding noise to the data set and then finding the accuracy*/
			
			for(i=0;i<errors.length;i++)
			{
				writer.println("Tree made with "+errors[i]+ "% noise");
				int changedInstances = obj.inputError(errors[i]);    // adding noise
				obj.makeTree();		// constructing the tree
				accuracy=obj.test(obj.head,0);   // calculating the accuracy on test set
				writer.println("Accuracy on test set	 = "+accuracy);
				accuracy=obj.test(obj.head,1);  // calculating the accuracy on training set
				writer.println("Accuracy on training set = "+accuracy);
				
				obj.printAfterTest();
				
				obj.resetArray(changedInstances);  // reset the array to default

				writer.println("\n"+"-------------------------------------------------------------------------------"+"\n");
			}
		}
		else if(obj.exptno==3)
		{
			/*This experiment does pruning (error reduced) */
			obj.makeTree(); // making the tree
			writer.println("Height of the tree 		 = " + obj.height);
			
			double accuracy1=obj.test(obj.head,0);
			writer.println("Accuracy on test set 	 = "+accuracy1);
			accuracy=obj.test(obj.head,1);
			writer.println("Accuracy on training set = "+accuracy);
			accuracy=accuracy1;
			nnodes=obj.printAfterTest();
			
			writer.println("\n"+"----------------------Pruning starts-------------------"+"\n");
			obj.leafNodes(obj.head);			// finding the leaf nodes
			obj.pruningNew(obj.head,nnodes);	// pruning the tree 

		}
		else if(obj.exptno==4)
		{
			/*This experiment constructs 9 trees and then evauates the instances and outputs the accuracy*/
			obj.forest();
		}

		writer.close();
	}

	/*This function prunes the tree by taking the head node and number of nodes in the tree before pruning*/
	private void pruningNew(Node current, int nnodes)
	{
		double accuracy = test(current,0); 	// finding the accuracy on unpruned tree
		int nodesReduced=0;					// number of nodes reduced so far
		Node []prevChildList=null;int []prevValues=null;int prevLabel=0,in=0;	// to copy the Nodes incase if the accuracy doesnt reduce
		while(!s.isEmpty())			// while stack is not empty
		{
			in=0;

			Node temp1 = s.peek();	// get the top node
			Node temp = null;

			if(temp1.depth<=1)		// pop if the depth is less than 1
			{	
				s.pop();
				continue;
			}
			else
			{
				temp = temp1.parent;	// take the parent
				s.pop();				// pop the node
				s.push(temp);			// push the parent

				if(temp.childList!=null)
				{
					in=1;
					prevChildList=temp.childList.clone();		//copying values of the pruned tree
					prevValues=temp.values.clone();				// cloning the values
					prevLabel=temp.label;						// assigning the label
				}

				temp.childList=null;
				temp.values=null;
				if(temp.positiveClassLabels.length>temp.negativeClassLabels.length)
					temp.label=1;
				else
					temp.label=0;

				if(test(current,0)>=accuracy)	// if the accuracy is high
				{
					in=0;
					temp=temp1;
					temp.childList=null;
					temp.values=null;
					if(temp.positiveClassLabels.length>temp.negativeClassLabels.length)
						temp.label=1;   // if more number of +ve labels then assign 1
					else
						temp.label=0;	// else assign -ve
					if(test(current,0)!=accuracy)
					{
						nodesReduced++;		// number of nodes reduces

						accuracy=test(current,0);
						writer.println("Prediction accuracy on validation set = "+accuracy);	
					}

				}


				if(in==1)
				{	// state variable
					temp.childList=prevChildList.clone();
					temp.values=prevValues.clone();
					temp.label=prevLabel;		// assign the label
				}

			}
		}
		writer.println("\nAccuracy after pruning on test set = "+test(current,0));
		writer.println("Accuracy after pruning on training set = "+test(current,1));
	}

	/*Stores the leaf nodes of a tree in a list*/
	private void leafNodes(Node current)
	{
		if(current!=null)
		{
			if(current.childList!=null)
			{
				for(int i=0;i<current.childList.length;i++)
				{
					leafNodes(current.childList[i]);	// recursive call on child
				}
			}
			else if(current.childList == null)
			{	
				s.push(current);		// push the node into the list
			}
		}
	}
	/*This generates the forest in the experiment number 4*/
	private void forest() {

		int i,j,k;
		int featureSize=9;	// total trees
		double accuracy=0;	//
		heads=new Node[ntrees];	// array of tree's head

		ArrayList<Integer> featureShuffled=new ArrayList<Integer>();	// array list to shuffle the features (randomize)
		for(i=0;i<m;i++)
		{
			featureShuffled.add(i);         	
		}
		for(i=0;i<ntrees;i++)
		{	
			writer.println("\n-----------------Tree no. "+(i+1)+" ----------------");

			Collections.shuffle(featureShuffled);		// shuffle the features
			array=new int[n][featureSize+1];			// make an array to store instances
			for(j=0;j<n;j++)
			{
				for(k=0;k<featureSize+1;k++)
				{
					if(k==featureSize)
					{
						array[j][k]=originalArray[j][m];
					}
					else
					{
						array[j][k]=originalArray[j][featureShuffled.get(k)];
					}
				}
			}
			m=array[0].length-1;
			heads[i]=makeTree();			// call make tree on this array
			writer.println("Height of the tree 		 = " + height);
			
			accuracy=test(heads[i],0);			// testing on test set
			writer.println("Accuracy on test set 	 = "+accuracy);
			accuracy=test(heads[i],1);			// accuracy on training set
			writer.println("Accuracy on training set = "+accuracy);
			printAfterTest();
			m=originalArray[0].length-1;
		}
		m=array[0].length-1;
		writer.println("\n"+"---------------------------Statistics of ensemble----------------------");		
		writer.println("\n"+"Effect of number of trees in the forest on train and test accuracies");
		testForest();			// Call test Forest to find the accuracy
		m=originalArray[0].length-1;
	}

	/*Find the accuracy of the Random Forest*/
	private void testForest() {

		z=n-ninstances;		// number of instances
		int i,j,k=0;
		int trueCases=0,falseCases=0,trueCasesTraining=0;

		for(k=1;k<=heads.length;k++)
		{
			trueCases=0;falseCases=0;
			trueCases=getTrueCasesForest(k,testSet);	        // gets true cases for the test set 
			trueCasesTraining=getTrueCasesForest(k,trainingSet);// gets true cases for the training set
		
			writer.println(k+" "+trueCasesTraining/(float)trainingSet.length*100+" "+trueCases/(float)z*100);

		}
	}
	/*This gets the true cases in the forest*/
	private int getTrueCasesForest(int k, int[] arrayAccuracy)
	{
		int trueCases=0,falseCases=0; // true and false cases
		int ones=0,zeroes=0,temp=0,i,j; // ones and zeros

		for(i=0;i<arrayAccuracy.length;i++)
		{
			ones=0;zeroes=0;
			for(j=0;j<k;j++)
			{
				temp=traversal(i,heads[j],testSet);
				if(temp==1)
				{
					ones++;	// storing ones
				}
				else if(temp==0)
				{
					zeroes++;	// storing zeros
				}
			}

			if(ones>zeroes)
			{
				temp=1;		// if majority is 1 then temp = 1
			}
			else
			{
				temp=0;		// else temp stores 0
			}

			if(temp==array[arrayAccuracy[i]][m])
			{
				trueCases++;		// true cases while comparing with test set
			}
			else
			{
				falseCases++;
			}
		}
		return trueCases;		// return true cases
	}

	/*Changes the instances to add noise to the dataset*/
	private int inputError(double error)
	{
		double changedInstances = (error * ninstances)/(double)100; // find number of instances on which noise is to be added

		for(int i=0;i<ninstances;i++)
		{
			training.add(trainingSet[i]);         
		}
		Collections.shuffle(training);
		for(int i=0;i<(int)changedInstances;i++)
		{    
			if(array[training.get(i)][m] == 1)
			{
				array[training.get(i)][m] = 0;		// interchange labels
			}
			else 
				array[training.get(i)][m] = 1;		// interchange labels
		}
		int c=0;
		for(int i=0;i<ninstances;i++)
		{
			if(array[trainingSet[i]][m]==1)
				c++;
		}
		npos = c;
		nneg = ninstances - c;
		return (int) changedInstances;		// return number of changed instances
	}
	/*Reset array i.e dataset to initial dataset*/
	private void resetArray(int changedInstance)
	{    
		for(int i=0;i<changedInstance;i++)
		{
			if(array[training.get(i)][m] == 1)
				array[training.get(i)][m] = 0;	// interchange
			else
				array[training.get(i)][m] = 1;	// interchange labels
		}
		int c=0;
		for(int i=0;i<ninstances;i++)
		{
			if(array[trainingSet[i]][m]==1)
				c++;
		}
		npos = c;
		nneg = ninstances - c;
	}

	/*make tree*/
	private Node makeTree() {

		int i=0;
		attributesCount=new int[m];

		nPos = new int[npos];
		nNeg = new int[nneg];
		ipos=0;ineg=0;
		/*Storing the positive and negative class instances*/
		for(i=0;i<ninstances;i++)
		{
			if(array[trainingSet[i]][m]==1)
			{
				nPos[ipos] = trainingSet[i];
				ipos++;
			}
			else
			{
				nNeg[ineg] = trainingSet[i];
				ineg++;
			}


		}
		head=new Node(0,0,npos,nneg,nPos,nNeg,0,0,null,null);	// initializing head

		head.attribu=new int[m];
		for(i=0;i<ninstances;i++)
			validInstance[i]=1;		// insert valid instances


		z1=0;z2=0;
		tree(head);		// call tree function to construct the tree
		writer.println("Attribute used for splitting at root = "+head.attributeNumber);

		return head;		// return the head of the tree
	}
	/*Printing the nodes in the tree after the making*/
	private int printAfterTest(){
		int i,totalAttri=0;

		writer.println("\n"+"Number of times an attribute is used as the splitting function");
		for(i=0;i<m;i++)
		{
			totalAttri += attributesCount[i];
			writer.println(i + " " + attributesCount[i]);
		}

		writer.println("\n"+"Total no of nodes in the tree = "+totalAttri);	// print the nodes
		return totalAttri;		//return 
	}
	/*testing on the validation set*/
	private double test(Node current,int accuracyNumber) {

		if(accuracyNumber==0)
		{
			z=n-ninstances;

			int i,j;
			int denominator=n-ninstances;
			double accuracy=0;
			int trueCases=0,falseCases=0;

			for(i=0;i<z;i++)
			{
				int temp=traversal(i,current,testSet);
				if(temp==array[testSet[i]][m])
				{
					trueCases++;		// find number of test cases passed
				}
				else
				{
					falseCases++;		// find number of test cases failed
				}
			}
			accuracy=trueCases/(float)z*100;	// calculate accuracy
			return accuracy;		// return accuracy of the testing
		}
		else 
		{
			z=trainingSet.length;

			int i,j;
			int denominator=n-ninstances;
			double accuracy=0;
			int trueCases=0,falseCases=0;

			for(i=0;i<z;i++)
			{

				int temp=traversal(i,current,trainingSet);
				if(temp==array[trainingSet[i]][m])
				{
					trueCases++;		// find number of test cases passed
				}
				else
				{
					falseCases++;		// find number of test cases failed
				}
			}
			accuracy=trueCases/(float)z*100;	// calculate accuracy
			return accuracy;		// return accuracy of the testing
		}

	}
	/*Traverses the tree while testing*/
	private int traversal(int i, Node current, int[] set) {
		if(current.childList==null)
		{  
			return current.label; 	// if child is null then return the current label
		}
		else{
			int currentAttribute=current.attributeNumber;
			int j=0;
			for(j=0;j<current.childList.length;j++)
			{
				if(array[set[i]][currentAttribute]==current.values[j])
				{
					return traversal(i,current.childList[j],set);	// else recursive call 
				}
			}

			if(current.positiveClassLabels.length>current.negativeClassLabels.length)
			{
				return 1;	// +ve label
			}
			else
			{
				return 0;	// -ve label
			}
		}


	}
	/*Makes the tree*/
	private void tree(Node current) {
		int i=0,maxinfogainattribute=0;	 // max information gain attribute
		double infogain=0,maxinfogain=0; // max information gain

		int count=0;
		for(i=0;i<current.attribu.length;i++)
		{
			if(current.attribu[i]==-1)	// if the node is already used in the path 
				count++;
		}
		if(current.depth > height)
			height = current.depth;

		if(current.negativeClassLabels.length==0&&current.positiveClassLabels.length==0)
		{   
			return;	// if no instance availaible
		}
		else if(count==m)
		{	// if the depth is highest
			if(current.positiveClassLabels.length>current.negativeClassLabels.length)
			{
				z1+=current.positiveClassLabels.length+current.negativeClassLabels.length;
				current.label=1;	// assign dominant label
				return;
			}
			else
			{
				z2+=current.negativeClassLabels.length+current.positiveClassLabels.length;    
				current.label=0;	// assign dominant label
				return;
			}
		}
		else if(current.negativeClassLabels.length==0)	// if -ve label are 0
		{
			z1+=current.positiveClassLabels.length;

			current.label=1;			// assign +ve label
			return;
		}
		else if(current.positiveClassLabels.length==0) // if +ve label are 0
		{
			z2+=current.negativeClassLabels.length;    

			current.label=0; 	// assign -ve label
			return;
		}
		else if(stopping==2&&current.depth==maxHeight)
		{	// if there is stopping criteria of second kind
			if(current.positiveClassLabels.length>current.negativeClassLabels.length)
			{
				z1+=current.positiveClassLabels.length+current.negativeClassLabels.length;
				current.label=1;	// majority
				return;
			}
			else
			{
				z2+=current.negativeClassLabels.length+current.positiveClassLabels.length;    
				current.label=0;	// majority
				return;
			}
		}
		else
		{
			for(i=0;i<m;i++)
			{
				if(current.attribu[i]!=-1)
				{
					infogain=calcinfogain(i,current);
					if(infogain>maxinfogain)
					{
						maxinfogain=infogain;	   // maximum info gain
						maxinfogainattribute=i;    //best classified example.

					}
				}

			}

			if(stopping == 1)
			{	// first stopping criteria
				
				if(maxinfogain==0)            //early stopping
				{   
					if(current.positiveClassLabels.length>current.negativeClassLabels.length)
					{
						z1+=current.positiveClassLabels.length+current.negativeClassLabels.length;
						current.label=1;
						return;
					}
					else
					{
						z2+=current.negativeClassLabels.length+current.positiveClassLabels.length;    
						current.label=0;
						return;
					}


				}
			}
			if(maxinfogain==0)
			{	
				// for no stopping criteria
				for(i=0;i<current.attribu.length;i++)
				{
					if(current.attribu[i]!=-1)
					{
						maxinfogain=infogain;
						maxinfogainattribute=i;
						break;
					}
				}
				current.attributeNumber=maxinfogainattribute;
				current.attribu[maxinfogainattribute]=-1;
				current=split(current,maxinfogainattribute);
				attributesCount[current.attributeNumber]++;	// keep count of attributes in the attribute array 

			}
			else
			{    
				current.attributeNumber=maxinfogainattribute;
				current.attribu[maxinfogainattribute]=-1;	// make it used by assigning -1

				current=split(current,maxinfogainattribute);
				attributesCount[current.attributeNumber]++;		// count the attributes 

			}
			for(i=0;i<current.childList.length;i++)
			{

				tree(current.childList[i]);			// recursive call
			}

			return;

		}
	}
	/*Split the node function*/
	private Node split(Node current, int attributeno) {

		int i,j,k=0,childno=0,min=array[0][attributeno],max=array[0][attributeno];
		for(i=0;i<n;i++)
		{
			if(array[i][attributeno]<min)
				min=array[i][attributeno];		// minimum attribute value
			if(array[i][attributeno]>max)
				max=array[i][attributeno];    // max attribute value
		}

		both []store=new both[max+1];		// store the values
		for(i=0;i<store.length;i++)
			store[i]=new both();

		for(j=0;j<current.positiveClassLabels.length;j++)
		{
			store[array[current.positiveClassLabels[j]][attributeno]].posi++;	// store +ve labels
		}
		for(j=0;j<current.negativeClassLabels.length;j++)
		{
			store[array[current.negativeClassLabels[j]][attributeno]].negi++;    // store -ve labels
		}

		int ctr=0;
		for(i=0;i<max+1;i++)
		{
			if(!(store[i].posi==0&&store[i].negi==0))
				ctr++;
		}
		current.childList=new Node[ctr];
		current.values=new int[ctr];

		for(i=0;i<max+1;i++)
		{
			k=0;
			if(store[i].posi==0&&store[i].negi==0)
				continue;
			else
			{
				Node temp=new Node(0,0,store[i].posi,store[i].negi,null,null,0,0,null,null);
				temp.positiveClassLabels=new int[temp.npos];
				temp.negativeClassLabels=new int[temp.nneg];

				for(j=0;j<current.positiveClassLabels.length;j++)
				{
					if(array[current.positiveClassLabels[j]][current.attributeNumber]==i)
					{
						temp.positiveClassLabels[k]=current.positiveClassLabels[j];
						k++;
					}
				}
				k=0;
				for(j=0;j<current.negativeClassLabels.length;j++)
				{
					if(array[current.negativeClassLabels[j]][current.attributeNumber]==i)
					{
						temp.negativeClassLabels[k]=current.negativeClassLabels[j];
						k++;
					}
				}
	
				current.childList[childno]=temp;
				current.childList[childno].parent=current;
				current.childList[childno].depth=current.depth+1;	// assign depth
				current.childList[childno].attribu=new int[m];
				current.childList[childno].attribu=current.attribu.clone();	// clone in order to avoid clash/repetetion
				current.values[childno]=i;	// store ith attribute as the child
				childno++;			// increase the child index
			}
		}
		return current;
	}
	/*Calculate information gain*/
	private double calcinfogain(int attributeno,Node current) {

		int i,j,min=array[0][attributeno],max=array[0][attributeno];
		for(i=0;i<n;i++)
		{
			if(array[i][attributeno]<min)
				min=array[i][attributeno];	// min value attribute
			if(array[i][attributeno]>max)
				max=array[i][attributeno];		// max value attribute    
		}
		int size=max-min+1;
		both []store=new both[max+1];
		for(i=0;i<store.length;i++)
			store[i]=new both();
		double infogain=current.entropy;		// find the entropy of parent

		for(j=0;j<current.positiveClassLabels.length;j++)
		{

			store[array[current.positiveClassLabels[j]][attributeno]].posi++;	// +ve labels
		}
		for(j=0;j<current.negativeClassLabels.length;j++)
		{
			store[array[current.negativeClassLabels[j]][attributeno]].negi++;	// -ve labels
		}

		for(j=0;j<max+1;j++)
		{
			double childEntropy,pPos=0,pNeg=0;	// child entrpoy
			int total=store[j].posi+store[j].negi;


			if(store[j].posi==0&&store[j].negi==0)
			{
				pPos=0;
				pNeg=0;
			}
			else
			{
				pPos = store[j].posi/(double)total;	// probablity of +ve
				pNeg = store[j].negi/(double)total; // probablity of -ve
			}

			if(pPos==0||pNeg==0)
			{
				childEntropy=0;
			}
			else
			{
				childEntropy = (float)(-pPos*(Math.log(pPos)/Math.log(2)))-(float) (pNeg*(Math.log(pNeg)/Math.log(2))); // find child entropy
			}

			infogain=infogain-childEntropy*((store[j].posi+store[j].negi)/(double) (current.positiveClassLabels.length+current.negativeClassLabels.length));
			// calculates infomation gain 
		}

		return infogain;		// return
	}
	/*Takes input from given file and find the ratio of +ve to -ve in the dataset*/
	public void input(String filename) throws FileNotFoundException, IOException {

		int counter=0,i=0,j=0;
		float ratio;
		try(BufferedReader br = new BufferedReader(new FileReader(filename)))
		{
			String line = br.readLine();

			while (line != null) {
				counter++;
				if(counter==1)
				{
					n=Integer.parseInt(line);
				}
				else if(counter==2)
				{
					m=Integer.parseInt(line);
				}
				else{
					if(counter==3)
					{
						array=new int[n][m+1];
					}
					String []different=new String[m+1];
					different=line.split("	");
					for(i=0;i<=m;i++)
					{
						array[j][i]=Integer.parseInt(different[i]);
					}

					if(array[j][m]==1)
						nones++;
					else
						nzeroes++;

					j++;
				}

				line = br.readLine();
			}
			ratio=nones/nzeroes*ninstances;		// maintains the ratio of +ve to -ve
		}
		random(ratio);

	}
	/*It randomly selects instances as the training set and maintains the ratio of +ve and -ve class labels*/
	public void random(float ratio) {
		// Picks 1000 random Instances
		int i=0;
		npos=(int) ratio;nneg=ninstances-npos;
		testSet=new int[n-ninstances];
		ArrayList<Integer> one=new ArrayList<Integer>();
		ArrayList<Integer> zero=new ArrayList<Integer>();

		for(i=0;i<n;i++)
		{
			if(array[i][m]==1)
				one.add(i);
			else
				zero.add(i);
		}
		if(npos>one.size())
		{
			npos=one.size();
			nneg=ninstances-npos;
		}
		Collections.shuffle(one);	// shuffles +ve instances
		Collections.shuffle(zero);	// shuffles -ve instances
		for (i=0; i<npos; i++) {
			trainingSet[i]=one.get(i);
		}
		int j=0;
		for (i=npos; i<ninstances; i++) {
			trainingSet[i]=zero.get(j);
			j++;
		}
		//making test set
		int k=0;
		for(i=j;i<nzeroes;i++){
			testSet[k]=zero.get(i);
			k++;
		}
		for(i=npos;i<nones;i++){
			testSet[k]=one.get(i);
			k++;
		}

	}

	
}






