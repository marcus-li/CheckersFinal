package client;

import gui.CheckersFrame;
import utilities.Evaluation;
import utilities.MiniMax;


public class ComputerPlayer
	{
		private Evaluation _eFunction;
		private int _color=1;
		public ComputerPlayer(double[] weights, String color)
			{
				if(color.equals("black"))
				_color = -1;
				_eFunction = new Evaluation(weights);
			}
		
		public int[] getBestMove(int[] _pieces)
			{	
				return MiniMax.minimaxDecision(_pieces, _eFunction, _color);
			}

		public int getColor()
			{
				return _color;
			}
	}
