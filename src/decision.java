
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class decision {

	int n=0,m=0;
	int [][]array;
	int []trainingSet = new int[1000];
	
	public static void main(String[] args) throws FileNotFoundException, IOException {

		decision obj=new decision();
		obj.input();
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
			}	//while loop  ends
		ratio=nones/nzeroes*1000;
		}
		random(ratio);

	}


	public void random(float ratio) {
		// Picks 1000 random Instances
		int i=0,p=(int) ratio;
		
		ArrayList<Integer> one=new ArrayList<Integer>();
		ArrayList<Integer> zero=new ArrayList<Integer>();

		for(i=0;i<n;i++)
		{
			if(array[i][85]==1)
				{
					one.add(i);
				}
		}
		Collections.shuffle(one);
		
		for (i=0; i<p; i++) {
			trainingSet[i]=one.get(i);
		}
			//random ones added
		for(i=0;i<n;i++)
		{
			if(array[i][85]==0)
				{
					zero.add(i);
				}
		}
		Collections.shuffle(zero);
		for (i=p; i<1000; i++) {
			trainingSet[i]=zero.get(i);
		}
			//random zeroes takens
			
		System.out.println(Arrays.toString(trainingSet));

	}


}



