
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class LinearHashing {
	
	public int buckettospilt = 0;
	public int index = 1;
	public static ArrayList<Integer> elements  = new ArrayList<>();
	Vector<Bucket> bucketsmain= new Vector<Bucket>(2);
	public static Vector<Bucket> overflowBuckets=new Vector<Bucket>();
	static int splitingCost=0;
	public LinearHashing() {
		
		bucketsmain.add(new Bucket());
		bucketsmain.add(new Bucket());
	
	}
	
	
	public int HashFunction(int i , int k){
		
		
		
		return (int) (k % Math.pow(2,i));
		
	}

	public static ArrayList<Integer> getElements(Bucket bucket)
	{
		elements.addAll(bucket.get());
		splitingCost++;
		if(bucket.overflowhappended==1)
		{
			LinearHashing.getElements(bucket.overflow);
		}
		return elements;
	}
	
	
	public  void split(){
		
		Bucket gbucket = new Bucket();
		bucketsmain.add(gbucket);
		//getelements
		LinearHashing.elements.clear();

		LinearHashing.getElements(bucketsmain.get(buckettospilt) );
		
		//reallocate value
		
		bucketsmain.get(buckettospilt).clear();
		//Rehash
		for (Integer i : LinearHashing.elements) {
			
			bucketsmain.get(HashFunction(index + 1, i)).addValues(i);
		}
		
		if(bucketsmain.get(buckettospilt).numberofemptyspaces<bucketsmain.get(buckettospilt).size)
		{   
			splitingCost++;
			Bucket b = bucketsmain.get(buckettospilt);
			while(b.overflow!=null)
			{
				splitingCost++;
				b = b.overflow;
			}
		}
		//last index
		int indi = bucketsmain.indexOf(gbucket);
		
		if(bucketsmain.get(index).numberofemptyspaces<bucketsmain.get(indi).size)
		{   
			splitingCost++;
			Bucket b = bucketsmain.get(indi);
			while(b.overflow!=null)
			{
				splitingCost++;
				b = b.overflow;
			}
		}
		
		
		if(buckettospilt == (Math.pow(2, index) - 1)  ){
			
			buckettospilt = 0;
			index = index + 1;
		}
		else
			buckettospilt++;
	}
	
	
	public void insert(int value){
	    
		int p = HashFunction(index, value);
		if(p < buckettospilt){
			bucketsmain.get(HashFunction(index+1, value)).addValues(value);
			LinearHashing.elements.clear();
			int spliting=splitingCost;
			int s = LinearHashing.getElements(bucketsmain.get(HashFunction(index+1, value))).size();
			//bucket size 
			splitingCost = spliting;
			if(s>bucketsmain.get(0).size && s%bucketsmain.get(0).size == 1){
				split();
			
			}
		}
		else
			{	bucketsmain.get(p).addValues(value);
		    
			LinearHashing.elements.clear();
			int spliting=splitingCost;
			int s = LinearHashing.getElements(bucketsmain.get(p)).size();
			splitingCost = spliting;
			//bucket size 
			if( s>bucketsmain.get(0).size && s%bucketsmain.get(0).size == 1){
				split();
			}
				
			}
		
	}
	public int  search(int value){
		
		int p = HashFunction(index, value);
		if(p < buckettospilt){
			
			return bucketsmain.get(HashFunction(index+1, value)).search(value);
			
		}
		else
		{
			return bucketsmain.get(HashFunction(index, value)).search(value);
			
		}
		
	}
	
	public static String choose(File f, int linenumber) throws FileNotFoundException
	  {
	     String result = null;
	     Random rand = new Random();
	     //System.out.println(linenumber);
	     int n = 1 + rand.nextInt(linenumber);
	     Scanner sc = new Scanner(f);
	     
	     for(int i = 0 ;i < n;i++)
	     {
	    	 sc.nextLine();
	    	 
	     }
	     result = sc.nextLine();

	     return result;      
	  }
	
	public  double metrictwo(int linenumber){
		double bs = 0;
		try {
			for(int i = 0;i<50;i++){
				String s = choose(new File("Dataset-Uniform.txt"),linenumber);
				int cost = search(Integer.parseInt(s));
				if(cost>0){
					bs += cost;
				}
			}	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bs/50;
		
		
	}
	
	
	public static void main(String[] args) {
		
		LinearHashing lh = new LinearHashing();
		int N=0,B,bc=lh.bucketsmain.get(0).size;
		float Utilization=0.0f;
		try {
			FileInputStream fstream = new FileInputStream("Dataset-Uniform.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			//output stream
		//	Writer out = new FileWriter("storageUtilization_40_H.txt",true);				
			String str;
			int a;
		//	Writer out2 = new FileWriter("lh_Search_metric_ub_10.txt",true);
		//	Writer out3 = new FileWriter("lh_SplitCost_u_40_--.txt",true);
			while((str=br.readLine())!=null && N<100000)
				{   splitingCost = 0;
					a = Integer.parseInt(str);
					lh.insert(a);
					N++;
					//B=lh.bucketsmain.size()+lh.overflowBuckets.size();
					//Utilization = (float)N/(bc*B);
					
					//out.write(String.valueOf(N)+","+String.valueOf(Utilization)+System.getProperty("line.separator"));
					
					
					//metric two
					/*if(N  % 5000 == 0){
						out.write(String.valueOf(N) + "," + String.valueOf(lh.metrictwo(N)) + System.getProperty("line.separator") );
					}*/
					
					//metric three
				//	out3.write(String.valueOf(N) + "," + String.valueOf(splitingCost) + System.getProperty("line.separator") );
				
					
					}
			// out3.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
		
	     for (Bucket b : lh.bucketsmain) {
			lh.elements.clear();
			System.out.println(lh.getElements(b));
			
		}
	
	
		
	}
	
	
}
