import java.util.HashMap;


public class Translate {
	private static String ntsTranslation[] = new String[36];
	private static HashMap<String, Integer> stnTranslation = new HashMap<String, Integer>();
	
	private Translate(){
		//don't make instances of this class
	}
	
	public static void nativeToServer(int e) {
		//translating our numbers into the server's representation
		//uses ntsTranslation array to output string based on array location
		//should not use spaces 0, 9, 18, 27 
		
		ntsTranslation[1] = "(1:7)";
		ntsTranslation[2] = "(3:7)";
		ntsTranslation[3] = "(5:7)";
		ntsTranslation[4] = "(7:7)";
		ntsTranslation[5] = "(0:6)";
		ntsTranslation[6] = "(2:6)";
		ntsTranslation[7] = "(4:6)";
		ntsTranslation[8] = "(6:6)";
		ntsTranslation[10] = "(1:5)";
		ntsTranslation[11] = "(3:5)";
		ntsTranslation[12] = "(5:5)";
		ntsTranslation[13] = "(7:5)";
		ntsTranslation[14] = "(0:4)";
		ntsTranslation[15] = "(2:4)";
		ntsTranslation[16] = "(4:4)";
		ntsTranslation[17] = "(6:4)";
		ntsTranslation[19] = "(1:3)";
		ntsTranslation[20] = "(3:3)";
		ntsTranslation[21] = "(5:3)";
		ntsTranslation[22] = "(7:3)";
		ntsTranslation[23] = "(0:2)";
		ntsTranslation[24] = "(2:2)";
		ntsTranslation[25] = "(4:2)";
		ntsTranslation[26] = "(6:2)";
		ntsTranslation[28] = "(1:1)";
		ntsTranslation[29] = "(3:1)";
		ntsTranslation[30] = "(5:1)";
		ntsTranslation[31] = "(7:1)";
		ntsTranslation[32] = "(0:0)";
		ntsTranslation[33] = "(2:0)";
		ntsTranslation[34] = "(4:0)";
		ntsTranslation[35] = "(6:0)";
		
		System.out.println(ntsTranslation[e]);
	}
	
	public static void serverToNative(String e) {
		//uses hashmap stnTranslation to convert server strings to our int representation
		//do not use spaces 0, 9, 18, 27
		
		stnTranslation.put("(1:7)", 1);
		stnTranslation.put("(3:7)", 2);
		stnTranslation.put("(5:7)", 3);
		stnTranslation.put("(7:7)", 4);
		stnTranslation.put("(0:6)", 5);
		stnTranslation.put("(2:6)", 6);
		stnTranslation.put("(4:6)", 7);
		stnTranslation.put("(6:6)", 8);
		stnTranslation.put("(1:5)", 10);
		stnTranslation.put("(3:5)", 11);
		stnTranslation.put("(5:5)", 12);
		stnTranslation.put("(7:5)", 13);
		stnTranslation.put("(0:4)", 14);
		stnTranslation.put("(2:4)", 15);
		stnTranslation.put("(4:4)", 16);
		stnTranslation.put("(6:4)", 17);
		stnTranslation.put("(1:3)", 19);
		stnTranslation.put("(3:3)", 20);
		stnTranslation.put("(5:3)", 21);
		stnTranslation.put("(7:3)", 22);
		stnTranslation.put("(0:2)", 23);
		stnTranslation.put("(2:2)", 24);
		stnTranslation.put("(4:2)", 25);
		stnTranslation.put("(6:2)", 26);
		stnTranslation.put("(1:1)", 28);
		stnTranslation.put("(3:1)", 29);
		stnTranslation.put("(5:1)", 30);
		stnTranslation.put("(7:1)", 31);
		stnTranslation.put("(0:0)", 32);
		stnTranslation.put("(2:0)", 33);
		stnTranslation.put("(4:0)", 34);
		stnTranslation.put("(6:0)", 35);
		
		System.out.println(stnTranslation.get(e));
	}
	
	
}
