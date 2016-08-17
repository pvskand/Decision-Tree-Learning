
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//Node(attributeno,size,npos,nneg,depth,entropy,child[],parent)
public class decision {

	int n=0,m=0;
	int npos,nneg;
	int [][]array;
	int []trainingSet = new int[1000];
	Node head=null;
	int []attributes;

	public static void main(String[] args) throws FileNotFoundException, IOException {

		int i;
		decision obj=new decision();
		obj.input();	//input function

		obj.head=new Node(0,0,obj.npos,obj.nneg,0,0,null,null,null);

		obj.head.instances=new int[1000];
		for(i=0;i<1000;i++)
			obj.head.instances[i]=1;
		obj.attributes=new int[obj.m];
		//System.out.println("npos="+obj.npos+"nneg="+obj.nneg);
		//System.out.println(obj.head.entropy);
		obj.tree(obj.head);
	}


	private void tree(Node current) {
		// TODO Auto-generated method stub
		int i=0;
		double infogain=0,maxinfogain=0;
		for(i=0;i<1;i++)
		{
			if(attributes[i]!=-1)
			{
				infogain=calcinfogain(i,current);
				if(infogain>maxinfogain)
				{
					maxinfogain=infogain;
				}
			}
		}
		System.out.println("yo");
	}


	private double calcinfogain(int attributeno,Node current) {
		// TODO Auto-generated method stub
		int i,j,min=array[0][attributeno],max=array[0][attributeno];
		for(i=0;i<n;i++)
		{
			if(array[i][attributeno]<min)
				min=array[i][attributeno];
			if(array[i][attributeno]>max)
				max=array[i][attributeno];	
		}
		current.size=max-min+1;
		current.childList=new Node[current.size];

		double infogain=current.entropy;

		for(i=0;i<current.size;i++)
		{
			int localnpos=0,localnneg=0;
			for(j=0;j<1000;j++)
			{
				if(array[trainingSet[j]][attributeno]==i+1)
				{
				//	current.instances[]=trainingSet[j];
					if(array[trainingSet[j]][85]==1)
						localnpos++;
					else
						localnneg++;
				}

			}
		}

		//System.out.println("min="+min+"max="+max);
		return 0;
	}


	public void input() throws FileNotFoundException, IOException {

		int counter=0,i=0,j=0;
		float ratio,nones=0,nzeroes=0;
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

					if(array[j][85]==1)
						nones++;
					else
						nzeroes++;

					j++;
				}

				line = br.readLine();
			}

			ratio=nones/nzeroes*1000;
		}
		//System.out.println(ratio);
		random(ratio);

	}


	public void random(float ratio) {
		// Picks 1000 random Instances
		int i=0;
		npos=(int) ratio;nneg=1000-npos;
		ArrayList<Integer> one=new ArrayList<Integer>();
		ArrayList<Integer> zero=new ArrayList<Integer>();

		for(i=0;i<n;i++)
		{
			if(array[i][85]==1)
				one.add(i);
			else
				zero.add(i);
		}
		Collections.shuffle(one);
		Collections.shuffle(zero);
		for (i=0; i<npos; i++) {
			trainingSet[i]=one.get(i);
		}
		int j=0;
		for (i=npos; i<1000; i++) {
			trainingSet[i]=zero.get(j);
			j++;
		}

		System.out.println(Arrays.toString(trainingSet));



	}


}



