
public class Testing
	{
		//how to use: call Testing.start() from anywhere
		// call testing.end()
		
		
		private static long startTime;
		private static long endTime;
		private Testing(){}//do not instantiate
		public static void start()
			{
				startTime = System.nanoTime();
			}
		public static void endAndReport()
			{
				endTime = System.nanoTime();
				System.out.println("time to completion: " + (endTime-startTime) + "ns");
			}
		
		
	}
