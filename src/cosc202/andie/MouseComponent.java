package cosc202.andie;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
/**
 * Class with mouse listeners and events
 * inspired by https://www.daniweb.com/programming/software-development/threads/342036/how-to-select-images-by-using-a-rectangular-area#post1453252
 * 
 * Having accessors makes frame invisible
 */
public class MouseComponent extends JPanel implements MouseMotionListener, MouseListener{
    private Rectangle selected;
    private Point point;
    private Boolean status = false;

    /**
     * Constructor
     */
    public MouseComponent(int x, int y){
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        setOpaque(false);
        setSize(x,y);
    }
    /**
     * Get rectangle.
     * @return selected rectangle
     */
    public Rectangle getRect(){
        return selected;
    }

    /**
     * Get status
     * @return status
     */
    public Boolean getStatus(){
        return status;
    }

    /**
    * Paint component for rectangle
    */
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
            if(selected != null){
                Graphics2D g2d = (Graphics2D)(g);
                g2d.draw(selected);
        }
    }

    /**
    * Mouse pressed event
    */
    public void mousePressed(MouseEvent e){
        point = e.getPoint();
        selected = new Rectangle(point);
    }

    /**
    * Mouse dragged event 
    */
    public void mouseDragged(MouseEvent e){
        selected.setBounds((int)Math.min(point.x,e.getX()), (int)Math.min(point.y,e.getY()), (int)Math.abs(e.getX()-point.x), (int)Math.abs(e.getY()-point.y));
        repaint();
    }

    /**
    * Mouse released event
    */
    public void mouseReleased(MouseEvent e){
        status = true;
    }
        
    /**
    * Unused methods required for interface
    */
    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}