import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

// ViewModel for cell

public class CellViewModel extends JPanel implements MouseListener
{

    private Cell _model;

    private boolean _isHighlighted;

    private LifeController _controller;

    public CellViewModel(LifeController controller, int x, int y)
    {

        _model = new Cell(x, y);

        // Background color inherited from JPanel
        //setBackground(Color.WHITE);

        _model.Alive = false;
        _isHighlighted = false;
        _controller = controller;

        addMouseListener(this);
    }

    public Cell getCell()
    {
        return _model;
    }

    public int getX()
    {
        return _model.getX();
    }

    public int getY()
    {
        return _model.getY();
    }

    public boolean isAlive()
    {
        return _model.Alive;
    }

    public void set()
    {
        _model.Alive = true;
        trigger_update();
    }

    public void kill()
    {
        _model.Alive = false;
        trigger_update();
    }

    public boolean isHighlighted()
    {
        return _isHighlighted;
    }

    public void highlight()
    {
        _isHighlighted = true;
        trigger_update();
    }

    public void unhighlight()
    {
        _isHighlighted = false;
        trigger_update();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        if (_isHighlighted)
        {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRect(0, 0, getWidth(), getHeight());
        }
        if (_model.Alive)
        {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, this.getWidth()-1, this.getHeight()-1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        _controller.cellClicked(_model.getX(), _model.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e)  { }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        _controller.cellEntered(_model.getX(), _model.getY());
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        _controller.cellExited(_model.getX(), _model.getY());
    }

    public void trigger_update() {
        repaint();

        // Schedule update call

        new Thread(new Runnable() {
            public void run()
            {
                try
                {
                    Thread.sleep(5);
                } catch (InterruptedException e)
                {
                    // Do nothing
                }
                repaint();
            }
        }).start();
    }
}
