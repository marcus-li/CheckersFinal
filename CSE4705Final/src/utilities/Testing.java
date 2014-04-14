package utilities;
import java.util.ArrayList;


public class Testing
	{
		//how to use: call Testing.start() from anywhere
		// call testing.end()
		
		
		private static long startTime;
		private static long endTime;
		private Testing(){}//do not instantiate
		public static void start()
			{
				startTime = System.nanoTime();//gets current run time nanoseconds
			}
		
		public static void endAndReport()
			{
				endTime = System.nanoTime();//same thing but end time, take the difference to get runtime
				System.out.println("time to completion: " + (endTime-startTime) + "ns");
			}
		
		
		//Testing results: 
		//speed of arraylist vs arrays:
		//time to completion: 219359498ns arraylist
		//time to completion: 129506780ns array --> lets go with arrays
		
		//results of 120,000 games never saw more than 21 possible moves in one turn 
		//result of 250,000 games randomly generated never saw more than 7 jumps in one move, let each move be an integer array of size 20
		
		
		
			    
		
		//-------arraylist vs array test
//		Testing.start();
//		for(int i = 0 ; i < 50000;i++)
//			{
//				ArrayList<String> list = new ArrayList<String>();
//				for(int j = 0 ; j<100; j++)
//					{
//						list.add(("hello" + i));
//					}
//				String s = "";
//				for(int j = 0; j<list.size();j++)
//					{
//						s= (String) list.get(j);
//					}
//			}
//		Testing.endAndReport();
//		
//		Testing.start();
//		for(int i = 0 ; i < 50000;i++)
//			{
//				int movecount = 0;
//				String[] list = new String[100];
//				for(int j = 0 ; j<100; j++)
//					{	
//						list[j] = "hello" + i;
//						movecount++;
//					}
//				String s;
//				for(int j = 0 ; j<movecount;j++)
//					{
//						s=list[j];
//					}
//			}
//		Testing.endAndReport();
	}
