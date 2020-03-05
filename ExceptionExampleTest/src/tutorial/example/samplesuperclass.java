package tutorial.example;

import java.io.IOException;

//Java program to illustrate the 
//concept of inheritance 

//base class 
class Bicycle 
{ 
	// the Bicycle class has two fields 
	public int gear; 
	public int speed; 
		
	// the Bicycle class has one constructor 
	public Bicycle(int gear, int speed) 
	{ 
		this.gear = gear; 
		this.speed = speed; 
		try {
			this.test1Exception(0);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
		
	// the Bicycle class has three methods 
	public void applyBrake(int decrement) 
	{ 
		speed -= decrement; 
	} 
		
	public void speedUp(int increment) 
	{ 
		speed += increment; 
	} 
	 void test1Exception(int num)throws IOException, ClassNotFoundException{ 
	     if(num==1)
	        throw new IOException("IOException Occurred");
	     else
	        throw new ClassNotFoundException("ClassNotFoundException");
	  }
	// toString() method to print info of Bicycle 
	public String toString() 
	{ 
		return("No of gears are "+gear 
				+"\n"
				+ "speed of bicycle is "+speed); 
	} 
} 

//derived class 
class MountainBike extends Bicycle 
{ 
	
	// the MountainBike subclass adds one more field 
	public int seatHeight; 

	// the MountainBike subclass has one constructor 
	public MountainBike(int gear,int speed, 
						int startHeight) throws ClassNotFoundException, IOException 
	{ 
		// invoking base-class(Bicycle) constructor 
		super(gear, speed); 
		this.testException(1);
		seatHeight = startHeight; 
	} 
		
	// the MountainBike subclass adds one more method 
	public void setHeight(int newValue) 
	{ 
		seatHeight = newValue; 
	} 
	  void testException(int num)throws IOException, ClassNotFoundException{ 
		     if(num==1)
		        throw new IOException("IOException Occurred");
		     else
		        throw new ClassNotFoundException("ClassNotFoundException");
		  }
	
	// overriding toString() method 
	// of Bicycle to print more info 
	@Override
	public String toString() 
	{ 
		return (super.toString()+ 
				"\nseat height is "+seatHeight); 
	} 
	
} 

//driver class 
public class samplesuperclass 
{ 
	public static void main(String args[]) throws ClassNotFoundException, IOException 
	{ 
		
		MountainBike mb = new MountainBike(3, 100, 25); 
		System.out.println(mb.toString()); 
	}
}