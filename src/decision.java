import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//Node(attributeno,size,npos,nneg,depth,entropy,child[],parent)
public class decision {

    int n=0,m=0,ninstances=1000;
    int npos, nneg;
    int ipos=0,ineg=0;
    float nones=0,nzeroes=0;
    int [][]array;
    int []trainingSet = new int[ninstances];
    int []testSet;
    Node head=null;
    int []attributes;
    int [] nPos;
    int [] nNeg;
    int [] validInstance = new int[ninstances];
    int z1=0,z2=0;


    public static void main(String[] args) throws FileNotFoundException, IOException {

   	 int i;
   	 decision obj=new decision();
   	 obj.input();   									 //input function
   	 //System.out.println(obj.npos);
   	 obj.nPos = new int[obj.npos];
   	 obj.nNeg = new int[obj.nneg];
   	 /*Storing the positive and negative class instances*/
   	 for(i=0;i<obj.ninstances;i++)
   	 {
   		 if(obj.array[obj.trainingSet[i]][obj.m]==1)
   		 {
   			 obj.nPos[obj.ipos] = obj.trainingSet[i];
   			 obj.ipos++;
   		 }
   		 else
   		 {
   			 obj.nNeg[obj.ineg] = obj.trainingSet[i];
   			 obj.ineg++;
   		 }


   	 } /* Checked! Its working :)*/
   	 obj.head=new Node(0,0,obj.npos,obj.nneg,obj.nPos,obj.nNeg,0,0,null,null);
   	 
   	 for(i=0;i<obj.ninstances;i++)
   		 obj.validInstance[i]=1;

   	 obj.attributes=new int[obj.m];
   	 //System.out.println("entropy = "+obj.head.entropy);
   	 //System.out.println(Arrays.toString(obj.head.negativeClassLabels));
   	 obj.tree(obj.head);
   	 System.out.println("z1="+obj.z1);
   	 System.out.println("z2="+obj.z2);
    
   	 //tree made
   	 //System.out.println(obj.head.childList[0].attributeNumber);
   	 obj.test(obj.head);
   	 
   	 
   	 System.out.println("-----------------ended---------------");
    }

    private void test(Node current) {
   	 // TODO Auto-generated method stub
   	 
   	 int denominator=m-ninstances;
   	 int numerator=0;
   	 
   	 
   	 
   	 
    }

    private void tree(Node current) {
   	 //System.out.println(current.attributeNumber);
   	 int i=0,maxinfogainattribute=0;
   	 double infogain=0,maxinfogain=0;
   	 //System.out.println("m="+m);
   	 //System.out.println("yo");
   	 if(current.negativeClassLabels.length==0&&current.positiveClassLabels.length==0)
   	 {
   		 return;
   	 }
   	 else if(current.negativeClassLabels.length==0)
   	 {
   		 z1+=current.positiveClassLabels.length;
   		 //System.out.println(z);
   		 current.label=1;
   		 return;
   	 }
   	 else if(current.positiveClassLabels.length==0)
   	 {
   		 z2+=current.negativeClassLabels.length;    
   		 current.label=0;
   		 return;
   	 }
   	 else
   	 {
   		 for(i=0;i<m;i++)
   		 {
   			 if(attributes[i]!=-1)
   			 {
   				 infogain=calcinfogain(i,current);
   				 if(infogain>maxinfogain)
   				 {
   					 maxinfogain=infogain;
   					 maxinfogainattribute=i;   	 //best classified example.
   				 }

   			 }
   		 }
   		 if(maxinfogain==0)
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
   		 
   		 current.attributeNumber=maxinfogainattribute;
   		 
   		 current=split(current,maxinfogainattribute);
   		 
   		 //System.out.println(current.childList[0]);
   		 
   		 for(i=0;i<current.childList.length;i++)
   		 {
   			 //System.out.println(i);
   			 tree(current.childList[i]);
   		 }
   		 
   		 return;
   		 //System.out.println("maxinfogainattribute = "+maxinfogainattribute);
   	 }
    }


    private Node split(Node current, int attributeno) {
   	 // TODO Auto-generated method stub
   	 //System.out.println(attributeno);
   	 int i,j,k=0,l=0,childno=0,min=array[0][attributeno],max=array[0][attributeno];;
   	 for(i=0;i<n;i++)
   	 {
   		 if(array[i][attributeno]<min)
   			 min=array[i][attributeno];
   		 if(array[i][attributeno]>max)
   			 max=array[i][attributeno];    
   	 }
   	 int size=max-min+1;
   	 both []store=new both[max+1];
   	 for(i=0;i<store.length;i++)
   		 store[i]=new both();
   	 for(j=0;j<current.positiveClassLabels.length;j++)
   	 {
   		 //System.out.println(array[current.positiveClassLabels[j]][attributeno]);
   		 store[array[current.positiveClassLabels[j]][attributeno]].posi++;
   	 }
   	 for(j=0;j<current.negativeClassLabels.length;j++)
   	 {
   		 store[array[current.negativeClassLabels[j]][attributeno]].negi++;
   	 }
   	 int ctr=0;
   	 for(i=0;i<max+1;i++)
   	 {
   		 if(!(store[i].posi==0&&store[i].negi==0))
   			 ctr++;
   	 }
   	 //System.out.println(ctr);
   	 current.childList=new Node[ctr];
   	 
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
   			 
   		 //    System.out.println(current.attributeNumber);
   			 for(j=0;j<current.positiveClassLabels.length;j++)
   			 {
   				 if(array[current.positiveClassLabels[j]][current.attributeNumber]==i)
   				 {
   					 //System.out.println(k);
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
   			 temp.parent=current;
   			 
   			 current.childList[childno]=temp;
   			 childno++;
   		 }
   	 }
   	 return current;
    }


    private double calcinfogain(int attributeno,Node current) {

   	 int i,j,min=array[0][attributeno],max=array[0][attributeno];
   	 for(i=0;i<n;i++)
   	 {
   		 if(array[i][attributeno]<min)
   			 min=array[i][attributeno];
   		 if(array[i][attributeno]>max)
   			 max=array[i][attributeno];    
   	 }
   	 int size=max-min+1;
   	 //current.childList=new Node[current.size];
   	 both []store=new both[max+1];
   	 for(i=0;i<store.length;i++)
   		 store[i]=new both();
   	 double infogain=current.entropy;

   	 for(j=0;j<current.positiveClassLabels.length;j++)
   	 {
   		 //System.out.println(array[current.positiveClassLabels[j]][attributeno]);
   		 store[array[current.positiveClassLabels[j]][attributeno]].posi++;
   	 }
   	 for(j=0;j<current.negativeClassLabels.length;j++)
   	 {
   		 store[array[current.negativeClassLabels[j]][attributeno]].negi++;
   	 }

   	 for(j=0;j<max+1;j++)
   	 {
   		 double childEntropy,pPos=0,pNeg=0;
   		 int total=store[j].posi+store[j].negi;

   		 //System.out.println("posi="+store[j].posi+"negi"+store[j].negi);

   		 if(store[j].posi==0&&store[j].negi==0)
   		 {
   			 pPos=0;
   			 pNeg=0;
   		 }
   		 else
   		 {
   			 pPos = store[j].posi/(double)total;
   			 pNeg = store[j].negi/(double)total;
   		 }
   		 //System.out.println("pPos="+pPos+"pNeg"+pNeg);

   		 if(pPos==0||pNeg==0)
   		 {
   			 childEntropy=0;
   		 }
   		 else
   		 {
   			 childEntropy = (float)(-pPos*(Math.log(pPos)/Math.log(2)))-(float) (pNeg*(Math.log(pNeg)/Math.log(2)));
   		 }
   		 //System.out.println(childEntropy);
   		 infogain=infogain-childEntropy*((store[j].posi+store[j].negi)/(double) (current.positiveClassLabels.length+current.negativeClassLabels.length));

   	 }
   	 //System.out.println("info gain = "+infogain+" attribute no ="+attributeno);
   	 //System.out.println("min="+min+"max="+max);
   	 return infogain;
    }


    public void input() throws FileNotFoundException, IOException {

   	 int counter=0,i=0,j=0;
   	 float ratio;
   	 try(BufferedReader br = new BufferedReader(new FileReader("ticdata2000.txt")))
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
   		 ratio=nones/nzeroes*ninstances;
   	 }
   	 //System.out.println(ratio);
   	 random(ratio);

    }


    public void random(float ratio) {
   	 // Picks 1000 random Instances
   	 int i=0;
   	 npos=(int) ratio;nneg=ninstances-npos;
   	 testSet=new int[n-1000];
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
   	 //System.out.println(one.size());
   	 //System.out.println(npos);
   	 Collections.shuffle(one);
   	 Collections.shuffle(zero);
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
   	 
   	 //System.out.println(Arrays.toString(trainingSet));
   	 //System.out.println(Arrays.toString(testSet));
    }


}
