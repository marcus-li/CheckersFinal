package utilities;

import gui.CheckersFrame;
import gui.GUIBoard;

import java.util.List;

public class MiniMax
	{
		//private static CheckersFrame maxStuckFrame= new CheckersFrame();
		
		//returns the best move based on 
		private static int _searchDepth = 10;
		private static Evaluation _eFunction;
		private static int _player;
		private static double _globalMax = -99999999;
		private static double _globalMin = 999999999;
		private MiniMax(){}
		
		public static int[] minimaxDecision(int[] gameState, Evaluation eFunction, int color){	
			
			_player = color;//integer 1 or -1 so to switch from default we just multiply all references by -1
		
			_eFunction = eFunction; //evaluation function we are using
			long[] toLongs = Translate.arrayToBitMapping(gameState);
			int[][] validMoves = MoveGenerator.legalMoves(toLongs[0], toLongs[1], toLongs[2], _player);
			if(validMoves[0]==null)
				{
					System.out.println("there are no moves for max to make.");
					return null;
				}
			
				//maximize the values that Min could possibly choose, initialize this to be the first action
					int[] bestMove = null;
					int inc = 0;
					double alphaMax=-9000000;
					while(validMoves[inc]!=null)
						{
							//we want to maximize the value of the next state
							//whose value is the minimum of it's successors states
							int[] nextState = MoveGenerator.result(_player , validMoves[inc], gameState);
							double nextValue = minValue(nextState,0,_globalMax,_globalMin);
							if(nextValue>alphaMax)
								{
								alphaMax = nextValue;
								bestMove = validMoves[inc];
								}	
							
							
							inc++;
						}
					
			return bestMove;
		}
		
		
		//opponent turn
		private static double minValue(int[] currentState, int searchDepth, double alphaMaxIn, double betaMinIn){
			
			double alphaMax = alphaMaxIn;
			double betaMin = betaMinIn;
			
			if(searchDepth>=_searchDepth)
				{
					if(_player==-1)
						{
							return _eFunction.evaluateBlack(currentState);//next move is opponents move
						}
						return _eFunction.evaluateWhite(currentState);
				}
					
			
			//get possible moves and return the worst one
			long[] toLongs = Translate.arrayToBitMapping(currentState);
			int[][] validMoves = MoveGenerator.legalMoves(toLongs[0], toLongs[1], toLongs[2], -_player);
			if(validMoves[0]==null) return 10000;//if opponent can't move then this is good
			
			//TODO further pruning to decide whether it is worth looking exploring deeper nodes
			
			
			int inc = 0;
			while(validMoves[inc]!=null)
				{
					//result of moving from current state for white, the opponent
					int[] nextState = MoveGenerator.result(-_player , validMoves[inc], currentState);
					//value of next move
					double value = maxValue(nextState,searchDepth+1,alphaMax, betaMin);//find the value of this state recursively
					
					if(value<betaMin)
						betaMin = value;
					if(betaMin<=alphaMax)//the maximizer one call up would never choose this option
						return betaMin;
					inc++;
				}
		
			
			return betaMin;
		}
		
		
		//our turn again
		private static double maxValue(int[] state, int searchDepth,double alphaMaxIn, double betaMinIn)
			{
					double alphaMax= alphaMaxIn;
					double betaMin = betaMinIn;
					
					
				//get our possible moves
					long[] toLongs = Translate.arrayToBitMapping(state);
					int[][] validMoves = MoveGenerator.legalMoves(toLongs[0], toLongs[1], toLongs[2], _player);
					
					if(validMoves[0]==null) return -10000;//if we can't move this is bad and we don't want to end up here	
					
					//TODO we can define more pruning rules to determine whether we should continue searching
					
					int inc = 0;
					//for each child node
					while(validMoves[inc]!=null)
						{
							//recursively find the best value
							int[] nextState = MoveGenerator.result(_player , validMoves[inc], state);
							double value = minValue(nextState,searchDepth+1,alphaMax,betaMin);
							if(value>alphaMax)//if we have done better then update our return value
									alphaMax = value;
							if(alphaMax>=betaMin)//the minimizer one call up would never let this happen
								return alphaMax;//so we return alpha
							inc++;
						}
				return alphaMax;
			}	
	}
