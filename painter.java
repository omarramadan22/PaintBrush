import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class painter extends Applet implements ItemListener {

    private List<Shape> shapes = new ArrayList<>();
	 private Stack<Action> actionStack = new Stack<>();
    private int startX, startY, endX, endY;
  private boolean drawing = false;
    private boolean drawEnabled = false;
    private boolean fillEnabled = false;
    private ColorButton  blueButton, redButton, greenButton ;
	private Button rectangleButton , ovalButton , lineButton , freeHandButton , eraserButton , clearButton ;
    private Checkbox fillCheckbox;
    private Color currentColor = Color.black;
     private int currentDrawing;
   

    abstract class Shape {
        int x1, y1, x2, y2;

     public Shape() {}
	
        abstract void draw(Graphics g);
    }

    public class Rectangle extends Shape {
        private Color color;
          private boolean fillEnabled;


        public Rectangle(int x1, int y1, int x2, int y2, Color color, boolean fillEnabled) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.fillEnabled = fillEnabled;
        }

        void draw(Graphics g) {
            if (fillEnabled) {
                g.setColor(color);
                g.fillRect(x1, y1, x2, y2);
            } else {
                g.setColor(color);
                g.drawRect(x1, y1, x2, y2);
            }
        }
    }

    public class Oval extends Shape {
        private Color color;
        private boolean fillEnabled;

        public Oval(int x1, int y1, int x2, int y2, Color color, boolean fillEnabled) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.fillEnabled = fillEnabled;
        }

        void draw(Graphics g) {
            if (fillEnabled) {
                g.setColor(color);
                g.fillOval(x1, y1, x2, y2);
            } else {
                g.setColor(color);
                g.drawOval(x1, y1, x2, y2);
            }
        }
    }

    public class Line extends Shape {
        private Color color;

        public Line(int x1, int y1, int x2, int y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }

        void draw(Graphics g) {
            g.setColor(color);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public class FreehandLine extends Shape {
        private Color color;

        public FreehandLine(int x1, int y1, int x2, int y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }

        void draw(Graphics g) {
            g.setColor(color);
            g.drawLine(x1, y1, x2, y2);
        }
    }
	
	 public class Eraser extends Shape {
        private Color color;
		
        public Eraser(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        void draw(Graphics g) {
            g.setColor(Color.white);
            g.drawRect(x1, y1, 20, 20);
	        g.fillRect(x1, y1, 20, 20);
        }
    }
    public void init() {
		
        Panel buttonPanel = new Panel(); 
        buttonPanel.setLayout(new GridLayout(2, 4)); 
		
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseListener());

       rectangleButton = new Button("Rectangle");
        rectangleButton.addActionListener(new RectangleListener());
        buttonPanel.add(rectangleButton);

        ovalButton = new Button("Oval");
        ovalButton.addActionListener(new OvalListener());
        buttonPanel.add(ovalButton);

       lineButton = new Button("Line");
        lineButton.addActionListener(new LineListener());
        buttonPanel.add(lineButton);

        blueButton = new ColorButton("", Color.blue, new ColorListener(Color.blue));
        buttonPanel.add(blueButton);

        redButton = new ColorButton("", Color.red, new ColorListener(Color.red));
        buttonPanel.add(redButton);

        greenButton = new ColorButton("", Color.green, new ColorListener(Color.green));
        buttonPanel.add(greenButton);

       freeHandButton = new Button("Free Hand");
        freeHandButton.addActionListener(new FreehandListener());
        buttonPanel.add(freeHandButton);

        clearButton = new Button("Clear");
        clearButton.addActionListener(new ClearListener());
        buttonPanel.add(clearButton);
		add(buttonPanel);

        eraserButton  = new Button("Eraser");
        eraserButton.addActionListener(new EraserListener());
        buttonPanel.add(eraserButton);
		add(buttonPanel); 
		
        fillCheckbox = new Checkbox("Fill shape", false);
        fillCheckbox.addItemListener(new FillCheckboxListener());
        add(fillCheckbox);
		
        Button undoButton = new Button("Undo");
		undoButton.addActionListener(new UndoListener());
		 buttonPanel.add(undoButton) ;
        add(buttonPanel);
    }

    public void paint(Graphics g) {
		
        for (Shape shape : shapes) {
            shape.draw(g);
        }
        // Draw the current shape being drawn
        if (drawing) {
        if (currentDrawing == 0) {
			if(fillEnabled){
				  g.setColor(currentColor);
            g.fillRect(Math.min(startX, endX), Math.min(startY, endY),
                    Math.abs(endX - startX), Math.abs(endY - startY));
			}
            g.setColor(Color.black);
            g.drawRect(Math.min(startX, endX), Math.min(startY, endY),
                    Math.abs(endX - startX), Math.abs(endY - startY));
        } else if (currentDrawing == 1) {
			if(fillEnabled){
				 g.setColor(currentColor);
            g.fillOval(Math.min(startX, endX), Math.min(startY, endY),
                    Math.abs(endX - startX), Math.abs(endY - startY));
			}
            g.setColor(Color.black);
            g.drawOval(Math.min(startX, endX), Math.min(startY, endY),
                    Math.abs(endX - startX), Math.abs(endY - startY));
        } else if (currentDrawing == 2) {
            g.setColor(currentColor);
            g.drawLine(startX, startY, endX, endY);
        }
   }
}
private void setButtonSize(Button button) {
        button.setPreferredSize(new Dimension(300, 200)); 
    }

    class ColorButton extends Button {
        private Color color;

        public ColorButton(String label, Color color, ActionListener actionListener) {
            super("");
            this.color = color;
            addActionListener(actionListener);
        }

       public void paint(Graphics g) {
            // Draw color icon over the entire button
            g.setColor(color);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw button border (optional)
            g.setColor(Color.black);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }
		
    private class MyMouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (drawEnabled) {
                startX = e.getX();
                startY = e.getY();
                drawing = true;
           }
        }

        public void mouseDragged(MouseEvent e) {
           if (drawEnabled  &&  drawing ) {
                endX = e.getX();
                endY = e.getY();
                if (currentDrawing == 3) {
                   shapes.add(new FreehandLine(startX, startY, endX, endY, getCurrentColor()));
                    startX = endX;
                   startY = endY;
               }else if (currentDrawing == 4) {
                    shapes.add(new Eraser(startX, startY, endX, endY));
                    startX = endX;
                    startY = endY;
                }
                repaint();
           }
        }

        public void mouseReleased(MouseEvent e) {
           if (drawEnabled && drawing
		   ) {
                endX = e.getX();
                endY = e.getY();
                drawing = false ;
				
                Shape newShape = null;

            if (currentDrawing == 0) {
                newShape = new Rectangle(Math.min(startX, endX), Math.min(startY, endY),
                        Math.abs(endX - startX), Math.abs(endY - startY), getCurrentColor(), getFillEnabled());
            } else if (currentDrawing == 1) {
                newShape = new Oval(Math.min(startX, endX), Math.min(startY, endY),
                        Math.abs(endX - startX), Math.abs(endY - startY), getCurrentColor(), getFillEnabled());
            } else if (currentDrawing == 2) {
                newShape = new Line(startX, startY, endX, endY, getCurrentColor());
            } 
			else if (currentDrawing == 3) {
                newShape = new FreehandLine(startX, startY, endX, endY, getCurrentColor());
            } else  {
                newShape = new Eraser(startX, startY, endX, endY);
            }
			
            if (newShape != null) {
                shapes.add(newShape);
                 actionStack.push(new Action(newShape));
                repaint();
            }
        }
    }
	}
    class RectangleListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            drawEnabled = true;
            currentDrawing = 0;
            repaint();
        }
    }
	
    class OvalListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            drawEnabled = true;
            currentDrawing = 1;
            repaint();
        }
    }

    class LineListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
           drawEnabled = true;
            currentDrawing = 2;
            repaint();
        }
    }

    class FreehandListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
           drawEnabled = true;
            currentDrawing = 3;
            repaint();
        }
    }
	
	class EraserListener implements ActionListener {
       public void actionPerformed(ActionEvent ev) {
           drawEnabled = true;
           currentDrawing = 4;
          repaint();
        }
    }

    class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            shapes.clear();
            repaint();
        }
    }

    class ColorListener implements ActionListener {
        private Color color;

        public ColorListener(Color color) {
            this.color = color;
        }

        public void actionPerformed(ActionEvent ev) {
            setCurrentColor(color);
        }
    }

    class FillCheckboxListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            fillEnabled = fillCheckbox.getState();
            repaint();
        }
    }

    private void setCurrentColor(Color color) {
        currentColor = color;
    }

    private Color getCurrentColor() {
        return currentColor;
    }

    private boolean getFillEnabled() {
        return fillEnabled;
    } 
	  class UndoListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (!shapes.isEmpty()) {
                Action lastAction = actionStack.pop();
                shapes.remove(lastAction.shape);
                repaint();
            }
        }
    }

    class Action {
        Shape shape;

        public Action(Shape shape) {
            this.shape = shape;
        }
    }
	  public void itemStateChanged(ItemEvent e) {}
     
}
