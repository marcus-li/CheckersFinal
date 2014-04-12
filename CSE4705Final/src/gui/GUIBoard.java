package gui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

public class GUIBoard extends JPanel
	{
		
		Ellipse2D.Double circle;
		int _pieceSize=60;		
		int _newSize = 8*_pieceSize;
		
		int[] _pieces;
		
		public GUIBoard()
			{
				setVisible(true);
				this.setSize(_newSize,_newSize);
				this.setVisible(true);
			}
		
		public void updatePieces(int[] pieces)
			{
				_pieces = pieces;
				repaint();
			}
		
		
		
	
		@Override
		 public void paintComponent(Graphics g1)
		       {
		    	  
		          super.paintComponent(g1);
		        
		          int  size =Math.min(this.getParent().getWidth(), this.getParent().getHeight());
		         _pieceSize = size/8;
		         this.setSize(_pieceSize*8,_pieceSize*8);
		           Graphics2D g = (Graphics2D) g1;
		           
		           for(int x = 0 ; x < 8 ; x++)
		        	   {
		        		   for (int y = 0 ; y < 8; y++)
		        			   {
		        				   if (Math.pow(-1,x+y)==1){
						        		g.setColor(Color.DARK_GRAY);
						        		g.fillRect(x*_pieceSize, y*_pieceSize, _pieceSize, _pieceSize);
						        	}   
		        			   }
		        	   }
		           
		         
        		  // g.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        		  // g.setColor(Color.RED);
		           
		           drawPieces(g);
		       }
		
		
		
		
		private void drawPieces(Graphics2D g)
			{
		         
	        			drawPiece(g,_pieces[1],0,1);
	        			drawPiece(g,_pieces[2],0,3);
	        			drawPiece(g,_pieces[3],0,5);
	        			drawPiece(g,_pieces[4],0,7);
	        			
	        			drawPiece(g,_pieces[5],1,0);
	        			drawPiece(g,_pieces[6],1,2);
	        			drawPiece(g,_pieces[7],1,4);
	        			drawPiece(g,_pieces[8],1,6);
	        			
	        			drawPiece(g,_pieces[10],2,1);
	        			drawPiece(g,_pieces[11],2,3);
	        			drawPiece(g,_pieces[12],2,5);
	        			drawPiece(g,_pieces[13],2,7);
	        			
	        			drawPiece(g,_pieces[14],3,0);
	        			drawPiece(g,_pieces[15],3,2);
	        			drawPiece(g,_pieces[16],3,4);
	        			drawPiece(g,_pieces[17],3,6);
	        			
	        			drawPiece(g,_pieces[19],4,1);
	        			drawPiece(g,_pieces[20],4,3);
	        			drawPiece(g,_pieces[21],4,5);
	        			drawPiece(g,_pieces[22],4,7);
	        			
	        			drawPiece(g,_pieces[23],5,0);
	        			drawPiece(g,_pieces[24],5,2);
	        			drawPiece(g,_pieces[25],5,4);
	        			drawPiece(g,_pieces[26],5,6);
	        			
	        			drawPiece(g,_pieces[28],6,1);
	        			drawPiece(g,_pieces[29],6,3);
	        			drawPiece(g,_pieces[30],6,5);
	        			drawPiece(g,_pieces[31],6,7);
	        			
	        			drawPiece(g,_pieces[32],7,0);
	        			drawPiece(g,_pieces[33],7,2);
	        			drawPiece(g,_pieces[34],7,4);
	        			drawPiece(g,_pieces[35],7,6);

				
			}

		private void drawPiece(Graphics2D g,int type, int y, int x)
			{
				
				if(type==-1) //black
    				{
    					g.setColor( new 
		        				   Color(0,0,0));
    					g.fillOval(x*_pieceSize,y*_pieceSize, _pieceSize, _pieceSize);
    					g.setColor( new 
		        				   Color(50,50,50));
    					g.fillOval(x*_pieceSize+shift(0.8),y*_pieceSize+shift(0.8), scaleDown(0.8), scaleDown(0.8));
    					
    				}
    			else if(type==1)//white
    				{
    					
    					g.setColor( new 
		        				   Color(200,0,0));
    					g.fillOval(x*_pieceSize,y*_pieceSize, _pieceSize, _pieceSize);	
    					g.setColor( new 
		        				   Color(255,0,0));
    					g.fillOval(x*_pieceSize+shift(0.8),y*_pieceSize+shift(0.8), scaleDown(0.8), scaleDown(0.8));
    				}
    			else if(type==-2)//black king
    				{
    					g.setColor( new 
		        				   Color(255,255,0));
    					g.fillOval(x,y, _pieceSize, _pieceSize);
    					g.setColor( new 
		        				   Color(50,50,50));
    					g.fillOval(x+shift(0.8),y+shift(0.8), scaleDown(0.8), scaleDown(0.8));
    					
    				}
    			else if(type==2)//white king
    				{
    					g.setColor( new 
		        				   Color(255,255,0));
    					g.fillOval(x,y, _pieceSize, _pieceSize);	
    					g.setColor( new 
		        				   Color(255,0,0));
    					g.fillOval(x+shift(0.8),y+shift(0.8), scaleDown(0.8), scaleDown(0.8));
    					
    				}
    			
			}
		private int shift(double d)
			{
				int c = (int)Math.floor(0.5*(_pieceSize- scaleDown(0.8)));
				return c;
			}
		public int scaleDown(double scaling)
			{
				return (int) Math.floor(_pieceSize*scaling);
			}
	
	

	}
