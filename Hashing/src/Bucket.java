import java.util.ArrayList;
public class Bucket {

	int numberofemptyspaces;
	int size = 10;
	int array[];
	Bucket overflow;
	int overflowhappended = 0;
	
	
	public Bucket() {
		
		numberofemptyspaces = size;
		array = new int[size];
		overflow=null;

	}
	
	
	public void addValues(int value){
		
		if(this.numberofemptyspaces > 0 && this.overflowhappended==0){
			int i = this.size - this.numberofemptyspaces;
			this.array[i] = value;
			this.numberofemptyspaces = this.numberofemptyspaces - 1;
		}
		else if(this.overflowhappended == 1){
			this.overflow.addValues(value);
		}
		else{
			this.overflowhappended = 1;
			this.overflow = new Bucket();
			LinearHashing.overflowBuckets.add(overflow);
			this.overflow.addValues(value);
		}
		
	}
	
	public int search(int value){
		
		for (int i = 0; i < array.length; i++) {
			if(array[i] == value)
				{	
					return 1;
				}
			else
				continue;
		}
		if(overflowhappended == 1)
			return overflow.search(value);
		return -1;
		}
	
	
	public ArrayList<Integer> get(){
		ArrayList<Integer> elements = new ArrayList<>();
		for(int i =0;i<array.length-numberofemptyspaces;i++){
		//for (int i = 0; i < array.length; i++) {
		//	if(array[i] != -1 )
			elements.add(array[i]);
		}
		return elements;
	}
	
	
	public int overflow(){
		if(overflowhappended == 0)
		{
			return 0;
		}
		else
			return overflowhappended;
	
	}
	
	public void clear(){
	     numberofemptyspaces = size;
		 this.overflow = null;
		 overflowhappended=0;
		
	}
		
		
		
	}
	
	
	
	
