package client;

import gui.CheckersFrame;

import java.util.Scanner;

import utilities.*;

public class InternalServer
	{
		private int[] _pieces = new int[]{0,-1,-1,-1,-1,-1,-1,-1,-1,0,-1,-1,-1,-1,0,0,0,0,0,0,0,0,0,1,1,1,1,0,1,1,1,1,1,1,1,1};
		private Scanner sc = new Scanner(System.in);
		private CheckersFrame _GUI;

	
		
	
	
    	//_pieces = new int[]{0,0,0,-1,-1,-1,0,-1,-1,0,-2,-1,-1,-1,-1,0,0,0,0,1,0,0,0,1,1,1,1,0,0,0,1,1,1,1,1,1};
//		_pieces = new int[] {0,
//    			  0,    0 ,   0,   0
//    		  ,0,   -0,    0,   0       ,0//9
//    		  ,   0,    1,    1,   0
//    		  ,0,   -2,    0,   0        ,0//18
//    		  ,   0,    0,    0,   0
//    		  ,0,    0,    0,   0        ,0 //27
//    		  ,   0,    1,    0,   0
//    		  ,0,    0,    0,   0
//    			};
		
		
		   //Start the program
				public static void main(String[] args){
					Translate.initialize();//initialize our translation function	
					new InternalServer();
					}
		
		public InternalServer()
			{
				//after about 80 moves we should switch algorithms
				//double[] alphaWeights = new double[] {10,10,2,2};//alpha computer weights
				double[] alphaWeights = new double[]{4,2,1,0.8};//alpha computer weights
				//assume betaWeights is our current best guess at a winning algorithm.
				double[] betaWeights = new double[] {4,2,1,0.3} ;//beta computer weights
				
				System.out.println("choose mode:\nPlayer vs Comp :pvc\nComp vs Server: cvs\nComp vs. Comp: cvc");
				String mode = sc.nextLine();
				if(mode.equals("pvc"))
					{
						_GUI = new CheckersFrame();
			    		_GUI.updatePieces(_pieces);
						playerVsComp(betaWeights);
					}
				else if(mode.equals("cvs"))
					{
						new CheckersClient(betaWeights);
					}
				else
					{
						_GUI = new CheckersFrame();
			    		_GUI.updatePieces(_pieces);
						compVsComp(new ComputerPlayer(betaWeights,"black"),new ComputerPlayer(alphaWeights,"white"));
			    		System.out.println("black: betaWeights; white: alphaWeights");
			    		decideWinner(_pieces);
			    		compVsComp(new ComputerPlayer(alphaWeights,"black"),new ComputerPlayer(betaWeights,"white"));
			    		System.out.println("black: alphaWeights; white: betaWeights");
			    		decideWinner(_pieces);
			    		
					}
	
			}
	
		
		private void decideWinner(int[] pieces)
			{
				long[] bwk = Translate.arrayToBitMapping(pieces);
				long blacks = bwk[0];
				long whites = bwk[1];
				long kings  = bwk[2];
				
				int blackCount = Long.bitCount(blacks);
				int whiteCount = Long.bitCount(whites);
				int blackKings = Long.bitCount(blacks&kings);
				int whiteKings = Long.bitCount(whites&kings);
				
				System.out.println("blacks: " +blackCount+ "; black Kings: " +blackKings +"; whites: " +whiteCount+ "; white kings: " + whiteKings);
				
			}

		private void compVsComp(ComputerPlayer black, ComputerPlayer white)
			{
				_pieces = new int[]{0,-1,-1,-1,-1,-1,-1,-1,-1,0,-1,-1,-1,-1,0,0,0,0,0,0,0,0,0,1,1,1,1,0,1,1,1,1,1,1,1,1}; 
				int moveCount = 0;

				try
					{
				while(true)
					{
						moveCount++;
						if(moveCount>100)
							break;
							Thread.sleep(50);
						_GUI.updatePieces(_pieces);
						if(computerMove(black)==-1)
							{
							System.out.println("white wins");
							break;
							}
						
						moveCount++;
						Thread.sleep(50);
							
						_GUI.updatePieces(_pieces);
						if(computerMove(white)==-1)
							{
							System.out.println("black wins");
							break;
							}
					}	
				
				Thread.sleep(2000);
					}
				catch (InterruptedException e)
					{		e.printStackTrace();	}
			}


		private void playerVsComp(double[] weights)
			{
				_pieces = new int[]{0,-1,-1,-1,-1,-1,-1,-1,-1,0,-1,-1,-1,-1,0,0,0,0,0,0,0,0,0,1,1,1,1,0,1,1,1,1,1,1,1,1};
				ComputerPlayer comp = new ComputerPlayer(weights,"white");
				while(true)
					{
						
						playerMove(sc.nextLine());
						_GUI.updatePieces(_pieces);
						Testing.start();
						if(computerMove(comp)==-1)
							{
								Testing.endAndReport();
							System.out.println("player wins");
							break;
							}
						Testing.endAndReport();
						_GUI.updatePieces(_pieces);
					}
				
			}


		private int computerMove(ComputerPlayer cp)
			{
				int[] bestMove = cp.getBestMove(_pieces);// MiniMax.minimaxDecision(_pieces, _alphaFunction);
				if(bestMove==null)
					{
						return -1;
					}
//				for(int i =0;i< bestMove.length;i++)
//					{
//						System.out.print(bestMove[i]+ " ");
//					}
				//	System.out.println("Computer " + cp.getColor() + " move");
				_pieces = MoveGenerator.result(cp.getColor(), bestMove, _pieces);
				return 0;
			}
		private void playerMove(String s)
			{
				String[] stringMove = s.split(",");
				int[] move = null;
				try{
				if(stringMove.length==2)
					move = new int[]{Integer.parseInt(stringMove[0].trim()),Integer.parseInt(stringMove[1].trim())};
				else
					{
						move = new int[18];
						for(int i = 0 ; i < stringMove.length;i++)
							{
								move[i] = Integer.parseInt(stringMove[i].trim());
							}
					}
				}
				catch(NumberFormatException e)
					{
						System.out.println("invalid input");
					}
				_pieces = MoveGenerator.result(-1, move, _pieces);
				
				
			}
		


public void printBoardState()
	{
		for(int i = 0 ; i < 36;i++)
			{
				System.out.print( _pieces[i]+ ",");
			}
		System.out.println("");
	}
	}
