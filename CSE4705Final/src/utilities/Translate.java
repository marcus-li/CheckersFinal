package utilities;
import java.util.HashMap;


public class Translate {
	private static String ntsTranslation[] = new String[36];
	private static HashMap<String, Integer> stnTranslation = new HashMap<String, Integer>();
	
	private Translate(){
		//don't make instances of this class
	}
	
	
	
	public static String  nativeMoveToServer(int[] steps)
		{
			String s="";
			for(int i = 0 ; i < steps.length-1;i++)
				{
					s=s+ nativePositionToServer(steps[i])+":";
				}
			s=s+ nativePositionToServer(steps[steps.length-1]);
			return s;
		}

	public static String nativePositionToServer(int position) 
	{
		//assume this will be called with a valid index to save time. 
		//If there is an error we will lose anyways.
		return ntsTranslation[position];
	}
	
	public static int[] serverMoveToNative(String e) {
		//uses hashmap stnTranslation to convert server strings to our int representation
		String[] moves = e.split(":\\(");
		int[] move = new int[moves.length];
		move[0]=stnTranslation.get(moves[0]);
		//we split by ":(" so need to reinsert "(" for our translation function
		for(int i = 1 ;i <moves.length;i++)
			move[i] = stnTranslation.get("("+moves[i]);	
		return move;
	}
	
	
	
	//this should be called once to initialize the two translation structures.
	public static void initialize()
		{
			//uses ntsTranslation array to output string based on array location
			//should not use spaces 0, 9, 18, 27 
			ntsTranslation[1] = "(7:1)";
			ntsTranslation[2] = "(7:3)";
			ntsTranslation[3] = "(7:5)";
			ntsTranslation[4] = "(7:7)";
			
			ntsTranslation[5] = "(6:0)";
			ntsTranslation[6] = "(6:2)";
			ntsTranslation[7] = "(6:4)";
			ntsTranslation[8] = "(6:6)";
			
			ntsTranslation[10] = "(5:1)";
			ntsTranslation[11] = "(5:3)";
			ntsTranslation[12] = "(5:5)";
			ntsTranslation[13] = "(5:7)";
			
			ntsTranslation[14] = "(4:0)";
			ntsTranslation[15] = "(4:2)";
			ntsTranslation[16] = "(4:4)";
			ntsTranslation[17] = "(4:6)";
			
			ntsTranslation[19] = "(3:1)";
			ntsTranslation[20] = "(3:3)";
			ntsTranslation[21] = "(3:5)";
			ntsTranslation[22] = "(3:7)";
			
			ntsTranslation[23] = "(2:0)";
			ntsTranslation[24] = "(2:2)";
			ntsTranslation[25] = "(2:4)";
			ntsTranslation[26] = "(2:6)";
			
			ntsTranslation[28] = "(1:1)";
			ntsTranslation[29] = "(1:3)";
			ntsTranslation[30] = "(1:5)";
			ntsTranslation[31] = "(1:7)";
			
			ntsTranslation[32] = "(0:0)";
			ntsTranslation[33] = "(0:2)";
			ntsTranslation[34] = "(0:4)";
			ntsTranslation[35] = "(0:6)";
			
			
			//Hashmap initialization
			
			//row 7
			stnTranslation.put("(7:1)", 1);
			stnTranslation.put("(7:3)", 2);
			stnTranslation.put("(7:5)", 3);
			stnTranslation.put("(7:7)", 4);
			//row 6
			stnTranslation.put("(6:0)", 5);
			stnTranslation.put("(6:2)", 6);
			stnTranslation.put("(6:4)", 7);
			stnTranslation.put("(6:6)", 8);
			
			stnTranslation.put("(5:1)", 10);
			stnTranslation.put("(5:3)", 11);
			stnTranslation.put("(5:5)", 12);
			stnTranslation.put("(5:7)", 13);
			
			stnTranslation.put("(4:0)", 14);
			stnTranslation.put("(4:2)", 15);
			stnTranslation.put("(4:4)", 16);
			stnTranslation.put("(4:6)", 17);
			
			stnTranslation.put("(3:1)", 19);
			stnTranslation.put("(3:3)", 20);
			stnTranslation.put("(3:5)", 21);
			stnTranslation.put("(3:7)", 22);
			
			stnTranslation.put("(2:0)", 23);
			stnTranslation.put("(2:2)", 24);
			stnTranslation.put("(2:4)", 25);
			stnTranslation.put("(2:6)", 26);
			
			stnTranslation.put("(1:1)", 28);
			stnTranslation.put("(1:3)", 29);
			stnTranslation.put("(1:5)", 30);
			stnTranslation.put("(1:7)", 31);
			
			stnTranslation.put("(0:0)", 32);
			stnTranslation.put("(0:2)", 33);
			stnTranslation.put("(0:4)", 34);
			stnTranslation.put("(0:6)", 35);
			
		}
	
	
	public static int[] bitMaskToIntArray(long whites, long blacks, long kings)
	{
		int[] pieces = new int[36];
		for(int i = 1 ; i <36;i++)
			{
				if(((blacks&1)&(kings&1))==1)
					{
						pieces[i] = -2;
					}
				else if((blacks&1)==1)
					{
						pieces[i] = -1;
					}
				else if(((whites&1)&(kings&1))==1)
					{
						pieces[i] = 2;
						
					}
				else if((whites&1)==1)
					{
						pieces[i] = 1;
					}
				
				blacks = blacks>>1;
				whites = whites>>1;
				kings = kings>>1;
			}
		return pieces;
	}



	public static long[] arrayToBitMapping(int[] pieces)
		{
			//======convert to bit representation for fast evaluation
			long kings = 0;
			long whites = 0;
			long blacks = 0;
			for(int i = 35 ; i >0;i--)
				{
					kings = kings<<1; whites = whites<<1; blacks = blacks<<1;
					if(pieces[i]==0)
						{
							continue;
						}
					else if(pieces[i]<0)
						{
							//black piece
							blacks++;
							if(pieces[i]==-2)
								kings++;
						}
					else
						{
							//white piece
							whites++;
							if(pieces[i]==2)
								kings++;
						}	
				}
			return new long[]{blacks,whites,kings};
			
			
			
		}
	
	
}
