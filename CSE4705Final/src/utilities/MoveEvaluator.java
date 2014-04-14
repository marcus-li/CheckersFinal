package utilities;

import java.util.LinkedList;


public class MoveEvaluator
	{
		private static final long invalidMask =              0b100000000100000000100000000L;//filter 8 17 and 26
		private static final long startGameWhites  = 0b11111111011110000000000000000000000L;
		private static final long startGameBlacks  = 0b00000000000000000000001111011111111L;
		private static final long startGameKings   = 0b00000000000000000000000000000000000L;
	//	                                               00000000000000000000000000000000000
	//                                                 11111111011110000000000000000000000
	//	                                               00000000000000000000001111011111111
		
		private static  long _blacks=0;
		private static  long _whites=0;
		private static  long _kings =0;
		
		
		private static String _player;
		private static int moveCount;
		private static LinkedList<Integer> _toExpand;
		
		
		
		
	public static int[] getBestMove(int[] _pieces, String player)
		{
			_player = player;
			
			
			//System.out.println((startGameWhites|startGameBlacks));
			//convert our pieces so they can be bit manipulated
			long[] map = Translate.arrayToBitMapping(_pieces);
			long whites  = map[0];
			long blacks = map[1];
			long kings = map[2];
			
			
//			Testing.start();
//			for(int i = 0 ; i<100000;i++)
//			Translate.bitMaskToIntArray(whites, blacks, kings);	
//			Testing.endAndReport();
			
//			System.out.println(Long.toBinaryString(kings));
//			System.out.println(Long.toBinaryString(whites));
//			System.out.println(Long.toBinaryString(blacks));
			
			//generate all possible moves
			int[][] possibleMoves =  legalMoves(blacks,whites,kings,player);
			
			//find the best move from legal moves
			int[] bestMove = evaluationFunction(possibleMoves);
			
			
			return bestMove;//something like [1,6,11,7,3] would mean 1 moves to 11 (by jump) then moves to 3 (by jump)
			
		}
		

	//in testing 250,000 games, I didn't find more than 21 moves ever available so we will use an int[21][?] 
	public static int[][] legalMoves(long blacks, long whites, long kings, String player)
		{
			_player = player;
			//an array container for holding all possible moves represented as arrays
			int[][] moveContainer = new int[21][];
			
			//for moves:
			//an array of size 2 means a regular move, whereas an array of size 18 means jump move
			//regular moves follow the syntax [start,end]
			//jumps: [start, enemy, 1, enemy,2,enemy ...end] so we can also evaluate how many pieces were taken and where
				_blacks = blacks;
				_whites = whites;
				_kings  = kings;
				jumpMoves(blacks, whites, kings, moveContainer);
			if(moveContainer[0]==null)//no jump moves found
				basicMoves(blacks,whites, kings, moveContainer);
			
		//	System.out.println(moveContainer[0][0]);
			return moveContainer;
			
		}


	/**
	 * jump moves 
	 *
	 * 
	 */
	private static void jumpMoves(long blacks, long whites, long kings,
			int[][] container)
		{
			
//			private static final long invalidMask =              0b100000000100000000100000000L;//filter 8 17 and 26
//			private static final long startGameWhites  = 0b11111111011110000000000000000000000L;
//			private static final long startGameBlacks  = 0b00000000000000000000001111011111111L;
//			private static final long startGameKings   = 0b00000000000000000000000000000000000L;
			//make a mask for black + 4 with whites	 (piece<<4)&whites
			//make a mask for black + 5 with whites  (piece<<5)&whites
			//make a mask for black kings (blackPieces&kings)
			//make a mask for black kings - 4 with whites (black kings>>4)&whites
			//make a mask for black kings - 5 with whites (black kings>>5)&whites
			
			
			
			
//			example of how to check spaces			
//			for(int i = 0 ; i < 35; i++)
//				{
//					if((invalidspaces>>i&1) ==1)
//					System.out.println(i+1);
//				}
			
			//all locations where we may not move
			long invalidspaces = whites|blacks|invalidMask;
			long dl=0; //find all jumpable whites down and left
			long dr=0;//down and right
			long  k=0;
			long ul=0;
			long ur=0;
			if(_player.equals("black"))
				{
					
					//phase 1, enumerate opponent pieces in adjacent squares
					dl = (blacks<<4)&whites; 	//down and left
					dr = (blacks<<5)&whites;	//down and right
					k  =  blacks&kings;			
					ul = (k>>5)&whites;			//up left
					ur = (k>>4)&whites;			//up right
					
					//phase two, following pieces
					dl = dl<<4;					//down left +8
					dr = dr<<5; 				//down right +10
					ul = ul>>5;					//up left-10
					ur = ur>>4;					//up right -8
					
					//strip invalid moves
					dl = (dl&invalidspaces)^dl; 
					dr = (dr&invalidspaces)^dr;
					ul = (ul&invalidspaces)^ul;
					ur = (ur&invalidspaces)^ur;
						
					
					
				}
			else if(_player.equals("white"))
				{
					
					
					//phase 1, check all opposing pieces in adjacent squares
					k = whites&kings;
					ul = (whites>>5)&blacks; //find all jumpable whites down and left
					ur = (whites>>4)&blacks;//up right(-4)
					dl = (k<<4)&blacks;//down left(+4) (kings only) 
					dr = (k<<5)&blacks;//down right (+5)(kings only)
					
					//phase two, check all continuing squares which are free
					dr = dr<<5;
					dl = dl<<4;
					ul = ul>>5; //up left(-10 total)
					ur = ur>>4; 
					//xor invalid moves to filter them out
					ul = (ul&invalidspaces)^ul;				
					ur = (ur&invalidspaces)^ur;
					dl = (dl&invalidspaces)^dl;
					dr = (dr&invalidspaces)^dr;
						
				}
			
			//if no available jump moves return.
			if((dl|dr|ul|ur)==0)
			return;

			//==========if jump moves are available==========
		//	System.out.println( _player + " jumps available");
			_toExpand = new LinkedList<Integer>();
			
			//int[] pieces = Translate.bitMaskToIntArray(whites, blacks, kings);
			 moveCount = 0; 
			for(int i = 1 ; i < 36; i++)
			{
				if((dl&1)==1)
					{
						//System.out.println((i-4)+ "->"+i);
						container[moveCount] = new int[18];
						container[moveCount][0]= i-8;
						container[moveCount][1]=i-4;
						container[moveCount][2]=i;
						_toExpand.add(moveCount);
						moveCount++;
					}
				if((dr&1)==1)
					{
						//System.out.println((i-5)+ "->"+i);
						container[moveCount] = new int[18];
						container[moveCount][0]= i-10;
						container[moveCount][1]=i-5;
						container[moveCount][2]=i;
						_toExpand.add(moveCount);
						moveCount++;
					}
				if((ul&1)==1)
					{
						//System.out.println((i+5)+ "->"+i);
						container[moveCount] = new int[18];
						container[moveCount][0]= i+10;
						container[moveCount][1]=i+5;
						container[moveCount][2]=i;
						_toExpand.add(moveCount);
						moveCount++;
					}
				if((ur&1)==1)
					{
						//System.out.println((i+4)+ "->"+i);
						container[moveCount] = new int[18];
						container[moveCount][0]= i+8;
						container[moveCount][1]=i+4;
						container[moveCount][2]=i;
						_toExpand.add(moveCount);
						moveCount++;
					}
				
				ul = ul>>1;
				ur = ur>>1;
				dl = dl>>1;
				dr = dr>>1;
			}
			
			//System.out.println(_toExpand.size());
			while(_toExpand.size()>0)
				{
					expandJump(_toExpand.remove(),container);
				}
					
		}

	private static void expandJump(int moveIndex, int[][] container)
		{
			int[] currentMove = container[moveIndex];//the move to expand
			//1,3,5,7.. are pieces which have been removed
			int inc = 0;//where we will expand our new move from 
			int j = 0;//even odd count
			long oldPosition=1<<(currentMove[0]-1);
			long newPosition=0;
			long newBlacks = 0;
			long newWhites = 0;
			long newKings = 0;
			long takenPieces = 0;//a new mask to cut out the taken pieces
			while(currentMove[inc]!=0)
				{	
					if(j==0)
						{
							newPosition = 1<<(currentMove[inc]-1);
							j=1;
						}
					else
						{
							takenPieces = takenPieces + (1<<(currentMove[inc]-1));
							//System.out.println(currentMove[inc]+" taken ");
							j=0;
						}
					inc++;
				}
			//System.out.println(Long.toBinaryString(newPosition)+" "+ Long.toBinaryString(takenPieces));
			newKings = takenPieces^_kings;
			if((oldPosition&_kings)>>(currentMove[0]-1)==1)
				{
					//moves our king to the new position which we know is free so we can use xor
					newKings = (oldPosition^newKings) ^ newPosition;
					//System.out.println("old king" + Long.toBinaryString(_kings));
					//System.out.println("new king" + Long.toBinaryString(newKings));
				}
			
		
			if(_player.equals("black"))
				{
					newBlacks = (oldPosition^_blacks)^newPosition;
					newWhites = _whites^takenPieces; //  if piece was taken remove it from new long
				}
			else
				{
					newBlacks = _blacks^takenPieces;
					newWhites = (oldPosition^_whites)^newPosition;
				}
			
//		
			
			
			
			//=========================================================================
			long invalidspaces = newWhites|newBlacks|invalidMask;
			long dl=0; //find all jumpable newWhites down and left
			long dr=0;//down and right
			long  k=0;
			long ul=0;
			long ur=0;
			if(_player.equals("black"))
				{
					
					//phase 1, enumerate opponent pieces in adjacent squares
					dl = (newPosition<<4)&newWhites; 	//down and left
					dr = (newPosition<<5)&newWhites;	//down and right
					k  =  newPosition&newKings;			
					ul = (k>>5)&newWhites;			//up left
					ur = (k>>4)&newWhites;			//up right
					
					//phase two, following pieces
					dl = dl<<4;					//down left +8
					dr = dr<<5; 				//down right +10
					ul = ul>>5;					//up left-10
					ur = ur>>4;					//up right -8
					
					//strip invalid moves
					dl = (dl&invalidspaces)^dl; 
					dr = (dr&invalidspaces)^dr;
					ul = (ul&invalidspaces)^ul;
					ur = (ur&invalidspaces)^ur;
						
					
					
				}
			else if(_player.equals("white"))
				{
					
					
					//phase 1, check all opposing pieces in adjacent squares
					k = newPosition&newKings;
					ul = (newPosition>>5)&newBlacks; //find all jumpable newWhites down and left
					ur = (newPosition>>4)&newBlacks;//up right(-4)
					dl = (k<<4)&newBlacks;//down left(+4) (newKings only) 
					dr = (k<<5)&newBlacks;//down right (+5)(newKings only)
					
					//phase two, check all continuing squares which are free
					dr = dr<<5;
					dl = dl<<4;
					ul = ul>>5; //up left(-10 total)
					ur = ur>>4; 
					//xor invalid moves to filter them out
					ul = (ul&invalidspaces)^ul;				
					ur = (ur&invalidspaces)^ur;
					dl = (dl&invalidspaces)^dl;
					dr = (dr&invalidspaces)^dr;
						
				}
			
			//printLong("newBlacks" ,newBlacks);
		//	printLong("newKings" ,newKings);
		//	printLong("newWhites" ,newWhites);
		
			//if no available jump moves return.
			int jumpBranchFactor = Long.bitCount(dl|dr|ul|ur);
			//System.out.println("jump Branch Factor " + jumpBranchFactor);
			if(jumpBranchFactor==0)
				return;
			
			
			
			//add to current move
			
			//for all except the last move we will copy the current move into a new spot
			//jumpBranchFactor
			int expandFrom = inc;
			LinkedList<Integer> toExpand = new LinkedList<Integer>();
			for(int i = 1 ; i < 36; i++)
				{
					if((dl&1)==1)
						{
							if(jumpBranchFactor>1)
								{
									
									//copy move into new array
									container[moveCount] = new int[18];
									copyArray(container[moveIndex],container[moveCount]);
									//put in new removed piece and jump location
									container[moveCount][expandFrom] = i-4;
									container[moveCount][expandFrom+1] = i;
									//increment move count
									toExpand.add(moveCount);
									moveCount++;
								}
							else
								{
								//	System.out.println((i-4)+ "->"+i);
									container[moveIndex][expandFrom] = i-4;
									container[moveIndex][expandFrom+1]=i;
									toExpand.add(moveIndex);
								}
							jumpBranchFactor--;
							
						}
					if((dr&1)==1)
						{
							if(jumpBranchFactor>1)
								{
									//copy move into new array
									container[moveCount] = new int[18];
									copyArray(container[moveIndex],container[moveCount]);
									//put in new removed piece and jump location
									container[moveCount][expandFrom] = i-5;
									container[moveCount][expandFrom+1] = i;
									//increment move count
									toExpand.add(moveCount);
									moveCount++;
									
								}
							else
								{
									container[moveIndex][expandFrom] = i-5;
									container[moveIndex][expandFrom+1]=i;
									toExpand.add(moveIndex);
								}
							jumpBranchFactor--;
						}
					if((ul&1)==1)
						{
							
							if(jumpBranchFactor>1)
								{
									//copy move into new array
									container[moveCount] = new int[18];
									copyArray(container[moveIndex],container[moveCount]);
									//put in new removed piece and jump location
									container[moveCount][expandFrom] = i+5;
									container[moveCount][expandFrom+1] = i;
									//increment move count
									toExpand.add(moveCount);
									moveCount++;
								}
							else
								{
									container[moveIndex][expandFrom] = i+5;
									container[moveIndex][expandFrom+1]=i;
									toExpand.add(moveIndex);
								}
							jumpBranchFactor--;
						}
					if((ur&1)==1)
						{
							
							if(jumpBranchFactor>1)
								{
									//copy move into new array
									container[moveCount] = new int[18];
									copyArray(container[moveIndex],container[moveCount]);
									//put in new removed piece and jump location
									container[moveCount][expandFrom] = i+4;
									container[moveCount][expandFrom+1] = i;
									//increment move count
									toExpand.add(moveCount);
									moveCount++;
								}
							else
								{
									container[moveIndex][expandFrom] = i+4;
									container[moveIndex][expandFrom+1]=i;
									toExpand.add(moveIndex);
								}
							jumpBranchFactor--;
						}
					
					ul = ul>>1;
					ur = ur>>1;
					dl = dl>>1;
					dr = dr>>1;
				}
			
			for(int i = 0 ; i<toExpand.size();i++)
				{
					expandJump(toExpand.get(i),container);
				}
			
		}





	private static void copyArray(int[] from, int[] to)
		{
			for(int i = 0 ; i< to.length;i++)
				{
					to[i]=from[i];
					//System.out.println(from[i] + " " + to[i]);
				}
			
		}


	private static void printLong(String string, long blacks)
		{
			System.out.println(string + " " + Long.toBinaryString(blacks));
			
		}


	private static void basicMoves(long blacks, long whites, long kings,
			int[][] container)
		{
			System.out.println("basicMoves called");
			//can't move to any positions corresponding to invalid positions
			long invalidSpots = whites|blacks|invalidMask;
			long downLeft ;
			long downRight ;
			long upLeft ;
			long upRight;
			if(_player.equals("black"))
				{
					 downLeft = blacks<<4;
					 downRight = blacks<<5;
					 upLeft = (blacks&kings)>>5;
					 upRight = (blacks&kings)>>4;
				
			
	
				}
			else//if _player == white
				{
					
					upLeft = whites>>5;
					upRight = whites>>4;
					downLeft = (whites&kings)<<4;
					downRight = (whites&kings)<<5;
					
					
				}
			
			
			//filter out spots we can't be in
			downLeft = (downLeft&invalidSpots)^downLeft;
			downRight = (downRight&invalidSpots)^downRight;
			upLeft = (upLeft&invalidSpots)^upLeft;
			upRight = (upRight&invalidSpots)^upRight;
			
			 moveCount = 0; 
			for(int i = 1 ; i < 36; i++)
			{
				
				if((downLeft&1)==1)
					{
						//System.out.println((i-4)+ "->"+i);
						container[moveCount] = new int[]{i-4,i};
						moveCount++;
					}
				if((downRight&1)==1)
					{
						//System.out.println((i-5)+ "->"+i);
						container[moveCount] = new int[]{i-5,i};
						moveCount++;
					}
				if((upLeft&1)==1)
					{
						//System.out.println((i+5)+ "->"+i);
						container[moveCount] = new int[]{i+5,i};
						moveCount++;
					}
				if((upRight&1)==1)
					{
						//System.out.println((i+4)+ "->"+i);
						container[moveCount] = new int[]{i+4,i};
						moveCount++;
					}
				
				upLeft = upLeft>>1;
				upRight = upRight>>1;
				downLeft = downLeft>>1;
				downRight = downRight>>1;
			}
			
		}

	private static int[] evaluationFunction(int[][] possibleMoves)
		{
			// TODO Auto-generated method stub
			return null;
		}

	
	
	//Example bit operations
	//long kings   =     0b00000000000000000000000000000000000L;
		//long blacks  = 0b00000000000000000000001111011111111L;
		//long whites  = 0b11111111011110000000000000000000000L;
	                          //111111110111100000000000000000 shift right if all pieces move left up
		                      //111011110000000000000000000000 get invalid whites and xor for valid moves
	                             //100000111100000000000000000
		                               //111000000000000000000
		
	}
