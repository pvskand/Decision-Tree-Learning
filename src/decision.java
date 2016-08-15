
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class decision {

	int n=0,m=0;
	int [][]array;
	int []testset=new int[1000];
	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		decision obj=new decision();
		obj.input();
	}

	private void input() throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		int counter=0,i=0,j=0;
		try(BufferedReader br = new BufferedReader(new FileReader("ticdata2000.txt"))) 
		{
			String line = br.readLine();

			while (line != null) {
				//sb.append(line);
				//sb.append(System.lineSeparator());
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
							//testset=new int[1000][m+1];
						}
					String []different=new String[m+1];
					different=line.split("	");
					for(i=0;i<=m;i++)
					{
						array[j][i]=Integer.parseInt(different[i]);
					}
					//if(counter==3)
					//System.out.println(array[0][0]);

					j++;
				}

				line = br.readLine();
			}
			//System.out.println(n+"  "+m+" "+array[0][79]);
			for(j=0;j<m+1;j++)
			{
				System.out.print(array[0][j]+" ");
			}
		}
		random();

	}
	

	private void random() {
		// TODO Auto-generated method stub
		int i=0;
		ArrayList<Integer> num=new ArrayList<Integer>();
		
		for(i=0;i<n;i++)
		{
			num.add(i);
		}
		Collections.shuffle(num);
		 System.out.println();
		 for (i=0; i<1000; i++) {
	            testset[i]=num.get(i);
	        }
		//System.out.println(testset[0]+"  "+num.get(i)+" "+num.get(1000));
		
	}

}

