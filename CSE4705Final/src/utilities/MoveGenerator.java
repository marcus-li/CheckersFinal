package utilities;

import java.util.LinkedList;


public class MoveGenerator
	{
		private static final long invalidMask =              0b100000000100000000100000000L;//filter 8 17 and 26		
		private static  long _blacks=0;//0b11111111011110000000000000000000000L
		private static  long _whites=0;//0b00000000000000000000001111011111111L
		private static  long _kings =0;//0b00000000000000000000000000000000000L
		private static String _player;
		
		//moveCount is a persistent count on the next free position in our moves array.
		private static int moveCount;

		
	private MoveGenerator()
		{/*private constructor, this is a utility, it is not to be instantiated.*/}
				
	//return a new array of pieces given a player, move, and board
	public static int[] result(String player, int[] move, int[] board)
		{
			int[] newBoard = new int[36];
			System.arraycopy(board, 0, newBoard, 0, 36);
			if(player.equals("black"))
				{
					//length of 2 means regular move, length of 18 means jump move
					if(move.length==2)
						{
							newBoard[move[1]]=newBoard[move[0]];//move old piece to new piece
							newBoard[move[0]]=0;				//clear old piece
							if(move[1]==32 || move[1] ==33||move[1]==34||move[1]==35)
								{
									newBoard[move[1]]=-2;//change to king
								}
						}
					else
						{
							int inc = 2; //[0,2,4,...,10] the array ends on an even piece
							
							//go through the even pieces
							while(!(move[inc]==0))
								{
									newBoard[move[inc-1]] = 0; //1,3,5...
									inc=inc+2;
								}
							newBoard[move[inc-2]]=newBoard[move[0]];//move old piece to new piece
							newBoard[move[0]]=0;
							
							if(move[inc-2]==32 || move[inc-2] ==33||move[inc-2]==34||move[inc-2]==35)
								{
									newBoard[move[inc-2]]=-2;//change to king
								}
						}
				}
			else
				{
					//length of 2 means regular move, length of 18 means jump move
					if(move.length==2)
						{
							newBoard[move[1]]=newBoard[move[0]];//move old piece to new piece
							newBoard[move[0]]=0;				//clear old piece
							if(move[1]==1 || move[1] ==2||move[1]==3||move[1]==4)
								{
									newBoard[move[1]]=-2;//change to king (for whites)
								}
						}
					else
						{
							int inc = 2; //[0,2,4,...,10] the array ends on an even piece
							
							//go through the even pieces
							while(!(move[inc]==0))
								{
									newBoard[move[inc-1]] = 0; //1,3,5...
									inc=inc+2;
								}
							newBoard[move[inc-2]]=newBoard[move[0]];//move old piece to new piece
							newBoard[move[0]]=0;
							
							if(move[inc-2]==1 || move[inc-2] ==2||move[inc-2]==3||move[inc-2]==4)
								{
									newBoard[move[inc-2]]=-2;//change to king (for whites)
								}
						}
				}
			return newBoard;
		}
	
	
	
	
	
	
	//in testing 250,000 games, I didn't find more than 21 moves ever available so we will use an int[21][?] 
	//We have two types of moves distinguished by the size of the array.
	//an array of size 2 means a regular move, whereas an array of size 18 means jump move
	//regular moves follow the syntax [start,end]
	//jumps: [start, enemy, 1, enemy,2,enemy ...end] so we can also evaluate how many pieces were taken and where
	public static int[][] legalMoves(long blacks, long whites, long kings, String player)
		{
			_player = player;
			_blacks = blacks;
			_whites = whites;
			_kings  = kings;
			
			//an array container for holding all possible moves represented as arrays
			int[][] moveContainer = new int[21][];

			//we are passing a pointer to container which is being updated by jump and basic moves
			//so these methods do not return anything, but do modify "moveContainer" which is returned
			jumpMoves(blacks, whites, kings, moveContainer);
			if(moveContainer[0]==null)//no jump moves found
			basicMoves(blacks,whites, kings, moveContainer);
			return moveContainer;
		}

	//compute jump moves, if jump moves are available it then calls another function which expands moves recursively
	private static void jumpMoves(long blacks, long whites, long kings,
			int[][] container)
		{

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
					ul = (whites>>5)&blacks; 	//find all jumpable whites down and left
					ur = (whites>>4)&blacks; 	//up right(-4)
					dl = (k<<4)&blacks;			//down left(+4) (kings only) 
					dr = (k<<5)&blacks;			//down right (+5)(kings only)
					
					//phase two, check all continuing squares which are free
					dr = dr<<5;
					dl = dl<<4;
					ul = ul>>5; 
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
			LinkedList<Integer> toExpand = new LinkedList<Integer>();
			 moveCount = 0; 
			for(int i = 1 ; i < 36; i++)
			{
				if((dl&1)==1)
					{
						container[moveCount] = new int[18];
						container[moveCount][0]= i-8;
						container[moveCount][1]=i-4;
						container[moveCount][2]=i;
						toExpand.add(moveCount);
						moveCount++;
					}
				if((dr&1)==1)
					{
						container[moveCount] = new int[18];
						container[moveCount][0]= i-10;
						container[moveCount][1]=i-5;
						container[moveCount][2]=i;
						toExpand.add(moveCount);
						moveCount++;
					}
				if((ul&1)==1)
					{
						container[moveCount] = new int[18];
						container[moveCount][0]= i+10;
						container[moveCount][1]=i+5;
						container[moveCount][2]=i;
						toExpand.add(moveCount);
						moveCount++;
					}
				if((ur&1)==1)
					{
						container[moveCount] = new int[18];
						container[moveCount][0]= i+8;
						container[moveCount][1]=i+4;
						container[moveCount][2]=i;
						toExpand.add(moveCount);
						moveCount++;
					}
				
				ul = ul>>1;
				ur = ur>>1;
				dl = dl>>1;
				dr = dr>>1;
			}
			
			//explore continuations of the path we are taking
			while(toExpand.size()>0)
				{
					expandJump(toExpand.remove(),container);
				}		
		}

	private static void expandJump(int moveIndex, int[][] container)
		{
			int[] currentMove = container[moveIndex];//the move to expand
			//1,3,5,7.. are pieces which have been removed
			int inc = 0;		//where we will expand our new move from 
			int j = 0;			//even odd count
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
			
			newKings = takenPieces^_kings; //siphon off kings which have been taken
			
			//determine if the current piece is a king, if so update its location
			if((oldPosition&_kings)>>(currentMove[0]-1)==1)
					newKings = (oldPosition^newKings) ^ newPosition;
				
			if(_player.equals("black"))
				{
					newBlacks = (oldPosition^_blacks)^newPosition; 
					newWhites = _whites^takenPieces; //if piece was taken remove it from new long
				}
			else
				{
					newBlacks = _blacks^takenPieces;
					newWhites = (oldPosition^_whites)^newPosition;
				}

			//=========================================================================
			//at this point:
			//blacks, whites, and kings are updated based on if this piece has jumped
			//=========================================================================
			//we basically mimic the jump moves algorithm now except instead of for all pieces,
			//we only expand from the last calculated jump position.
			
			long invalidspaces = newWhites|newBlacks|invalidMask;
			long ur=0,ul=0,k=0,dr = 0,dl=0; 
			
			if(_player.equals("black"))
				{
					
					//phase 1, enumerate opponent pieces in adjacent squares
					dl = (newPosition<<4)&newWhites;
					dr = (newPosition<<5)&newWhites;
					k  =  newPosition&newKings;			
					ul = (k>>5)&newWhites;			
					ur = (k>>4)&newWhites;			
		
				}
			else if(_player.equals("white"))
				{
					
					
					//phase 1, check all opposing pieces in adjacent squares
					k = newPosition&newKings;
					ul = (newPosition>>5)&newBlacks; //find all jumpable newWhites down and left
					ur = (newPosition>>4)&newBlacks;//up right(-4)
					dl = (k<<4)&newBlacks;//down left(+4) (newKings only) 
					dr = (k<<5)&newBlacks;//down right (+5)(newKings only)
				}
			
			
			//phase two, continue to next spot in same direction
			dl = dl<<4;					//down left +8
			dr = dr<<5; 				//down right +10
			ul = ul>>5;					//up left-10
			ur = ur>>4;					//up right -8
			
			//invalidate moves which land in bad spots
			dl = (dl&invalidspaces)^dl; 
			dr = (dr&invalidspaces)^dr;
			ul = (ul&invalidspaces)^ul;
			ur = (ur&invalidspaces)^ur;
			
			//count number of ways we can expand from this position
			int jumpBranchFactor = Long.bitCount(dl|dr|ul|ur);	
			if(jumpBranchFactor==0)
				return;
			
			//for each move expand a copy of the move in the index we are evaluating.
			//for the last branch edit the current move directly.
			int expandFrom = inc;
			LinkedList<Integer> toExpand = new LinkedList<Integer>();
			
			//as far as the run time goes, this looks like a large loop, but really there will be at most (not likely)
			//three true conditions within this loop, since one can jump at most three ways from a jump position.
			//this will do 34*(4 checks+4 shifts) + 3 checks if true + about 3*8 operations for true
			//so for each move this loop will take around 250 operations
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
									container[moveCount] = new int[18];
									copyArray(container[moveIndex],container[moveCount]);
									container[moveCount][expandFrom] = i-5;
									container[moveCount][expandFrom+1] = i;
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
									container[moveCount] = new int[18];
									copyArray(container[moveIndex],container[moveCount]);
									container[moveCount][expandFrom] = i+5;
									container[moveCount][expandFrom+1] = i;
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
									container[moveCount] = new int[18];
									copyArray(container[moveIndex],container[moveCount]);
									container[moveCount][expandFrom] = i+4;
									container[moveCount][expandFrom+1] = i;
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
					//shift all bits left
					ul = ul>>1;
					ur = ur>>1;
					dl = dl>>1;
					dr = dr>>1;
				}
			
			//for each move which we have been able to expand, try to expand it further recursively.
			for(int i = 0 ; i<toExpand.size();i++)
				{
					expandJump(toExpand.get(i),container);
				}
		}

	//very fast specialized array copy
	private static void copyArray(int[] from, int[] to)
		{
			for(int i = 0 ; i< to.length;i++)
				to[i]=from[i];
		}

	//just a quick function to print a long with a message
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
			else
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
						container[moveCount] = new int[]{i-4,i};
						moveCount++;
					}
				if((downRight&1)==1)
					{
						container[moveCount] = new int[]{i-5,i};
						moveCount++;
					}
				if((upLeft&1)==1)
					{
						container[moveCount] = new int[]{i+5,i};
						moveCount++;
					}
				if((upRight&1)==1)
					{
						container[moveCount] = new int[]{i+4,i};
						moveCount++;
					}
				
				upLeft = upLeft>>1;
				upRight = upRight>>1;
				downLeft = downLeft>>1;
				downRight = downRight>>1;
			}
			
		}
	
	}


/*	
Example bit operations
long kings   =     0b00000000000000000000000000000000000L;
	long blacks  = 0b00000000000000000000001111011111111L;
	long whites  = 0b11111111011110000000000000000000000L;
                          111111110111100000000000000000 shift right if all pieces move left up
	                      111011110000000000000000000000 get invalid whites and xor for valid moves
                             100000111100000000000000000
	                               111000000000000000000
*/	
