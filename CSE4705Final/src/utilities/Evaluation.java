package utilities;

public class Evaluation
	{
		private double[] _weights;
		private long borderMask = 0b111111000000011000000011000000011111L;
		private long blacksCloseToKingMask = 0b111111111111000000000000000000000000L;
		private long whitesCloseToKingMask = 0b000000000000000000000000000111111111L;
		public Evaluation(double[] weights){
			_weights = weights;
		}
	
	
		
		
		
		//assuming black
		public double evaluateBlack(int[] pieces)
			{
				long[] pieces2 = Translate.arrayToBitMapping(pieces);
				long blacks = pieces2[0];
				long whites = pieces2[1];
				long kings = pieces2[2];
				long blackRegularPieces = blacks^(blacks&kings);
				//long whiteRegularPiecse = whites^(whites&kings);
				
				int borderCount  = Long.bitCount(borderMask&blacks);
				//Desirable for non-king pieces to try and become kings, so black pieces in second to last row good
				
				int blacksCloseToKings = Long.bitCount(blackRegularPieces&blacksCloseToKingMask);
				
				//raw score kings and pieces
				int pieceValue = Long.bitCount(blacks)-Long.bitCount(whites);
				//a bonus for having kings
				int kingValue = Long.bitCount(blacks&kings)-Long.bitCount(whites&kings);
			
				
				return _weights[0]*pieceValue+_weights[1]*kingValue+_weights[2]*blacksCloseToKings+_weights[3]*borderCount;
				
			}

	

				//assuming white
				public double evaluateWhite(int[] pieces)
					{
						long[] pieces2 = Translate.arrayToBitMapping(pieces);
						long blacks = pieces2[0];
						long whites = pieces2[1];
						long kings = pieces2[2];
						int borderCount  = Long.bitCount(borderMask&whites);
						//desireable for non-king pieces to try and become kings, so black pieces in second to last row good
						long whiteRegularPieces = whites^(whites&kings);
						
						int whitesCloseToKing = Long.bitCount(whiteRegularPieces&whitesCloseToKingMask);
						
						int pieceValue = Long.bitCount(whites)-Long.bitCount(blacks);
						int kingValue = Long.bitCount(whites&kings)-Long.bitCount(blacks&kings);
						
						return _weights[0]*pieceValue+_weights[1]*kingValue+_weights[2]*whitesCloseToKing+_weights[3]*borderCount;
						
					}

	





//assuming black
public double evaluate2(int[] pieces)
	{
		int piece = 0;
		int kings = 0;
		for(int i = 0 ; i < 36;i++)
			{
				if(pieces[i]==-1)
					piece=piece+2;
				else if(pieces[i]==-2)
					kings = kings+3;
				else if(pieces[i]==1)
					piece = piece-2;
				else if (pieces[i]==2)
					kings = kings-3;
			}
		
		return _weights[0]*piece+_weights[1]*kings;
		
	}
	}
/*
 * 
				long[] pieces2 = Translate.arrayToBitMapping(pieces);
				long blacks = pieces2[0];
				long whites = pieces2[1];
				long kings = pieces2[2];
				
				
				
				int pieceValue = Long.bitCount(blacks)-Long.bitCount(whites);
				int kingValue = Long.bitCount(blacks&kings)-Long.bitCount(whites&kings);
				
				//phase 1, check all opposing pieces in adjacent squares
				long k = whites&kings;
				long ul = (whites>>5)&blacks; 	//find all jumpable whites down and left
				long ur = (whites>>4)&blacks; 	//up right(-4)
				long dl = (k<<4)&blacks;			//down left(+4) (kings only) 
				long dr = (k<<5)&blacks;			//down right (+5)(kings only)
				
				//phase two, check all continuing squares which are free
				dr = dr<<5;
				dl = dl<<4;
				ul = ul>>5; 
				ur = ur>>4; 
				
				long invalidspaces = 0b100000000100000000100000000L;
				//xor invalid moves to filter them out
				ul = (ul&invalidspaces )^ul;				
				ur = (ur&invalidspaces)^ur;
				dl = (dl&invalidspaces)^dl;
				dr = (dr&invalidspaces)^dr;		
				//all of whites jump moves
				int neighborCanEatMe = Long.bitCount((dl|dr|ul|ur));
				
				*/
