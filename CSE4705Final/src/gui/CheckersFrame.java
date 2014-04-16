package gui;


		import java.awt.Color;
		import javax.swing.JFrame;
		import javax.swing.WindowConstants;


		public class CheckersFrame extends JFrame
			{
				GUIBoard _board;
				private boolean _hasPieces=false;
				
				public CheckersFrame()
					{

						setTitle("Checkers");
						getContentPane().setBackground(Color.BLACK);
						setVisible(true);
						setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
						_board = new GUIBoard();
						add(_board);
						_board.setLocation(15, 15);
						
						setSize(_board.getHeight()+50, _board.getHeight()+75);
						
					}
				
				//when the state of the game changes update the pieces
				public void updatePieces(int[] pieces)
					{
						_board.updatePieces(pieces);
						_hasPieces = true;
					}

				public boolean hasPieces()
					{
						// TODO Auto-generated method stub
						return _hasPieces;
					}
				

			}

	
