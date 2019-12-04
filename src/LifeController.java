import javax.swing.*;
import java.util.LinkedList;

public class LifeController
{
    private SpatialView _space;

    private int _minSurvival;
    private int _maxSurvival;
    private int _minBirth;
    private int _maxBirth;
    private boolean _taurus = false;
    private int _step;

    private boolean _stop;

    public LifeController(SpatialView space, int minSurvival, int maxSurvival, int minBirth, int maxBirth, boolean taurus, int step)
    {
        _space = space;
        _minSurvival = minSurvival;
        _maxSurvival = maxSurvival;
        _minBirth = minBirth;
        _maxBirth = maxBirth;
        _taurus = taurus;
        _step = step;

        // ToDo allow for changing these

        _stop = false;
    }

    public void setTaurus(boolean taurus)
    {
        _taurus = taurus;
    }

    public void setMinSurvival(int min)
    {
        _minSurvival = min;
    }

    public void setMaxSurvival(int max)
    {
        _maxSurvival = max;
    }

    public void setMinBirth(int min)
    {
        _minBirth = min;
    }

    public void setMaxBirth(int max)
    {
        _maxBirth = max;
    }

    public void setStep(int step)
    {
        _step = step;
    }

    public void cellEntered(int x, int y)
    {
        _space.getCell(x, y).highlight();
    }

    public void cellExited(int x, int y)
    {
        _space.getCell(x, y).unhighlight();
    }

    public void cellClicked(int x, int y)
    {
        CellViewModel vm = _space.getCell(x, y);

        if (vm.isAlive())
            vm.kill();
        else
            vm.set();
    }

    public void clear()
    {
        final int factor = _space.getSpaceHeight();
        final int xcells = _space.getSpaceWidth() * _space.getSpaceHeight();
        _space.resetProgressBar(xcells, "Clearing");

        new Thread(new Runnable() {
            public void run()
            {
                for (int x = 0; x < _space.getSpaceWidth(); x++)
                {
                    final int cx = x;

                    for (int y = 0; y < _space.getSpaceHeight(); y++)
                    {
                        //final int cx = x;
                        final int cy = y;

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run()
                            {
                                _space.getCell(cx, cy).kill();
                                _space.reportProgress(cx * factor + cy);
                            }
                        });
                    }
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run()
                    {
                        _space.finish();
                    }
                });
            }
        }).start();
    }

    public void randomize()
    {
        final int factor = _space.getSpaceHeight();
        final int xcells = _space.getSpaceWidth() * _space.getSpaceHeight();
        _space.resetProgressBar(xcells, "Randomizing");

        new Thread(new Runnable() {
            public void run()
            {
                for (int x = 0; x < _space.getSpaceWidth(); x++)
                {
                    final int cx = x;

                    for (int y = 0; y < _space.getSpaceHeight(); y++)
                    {
                        //final int cx = x;
                        final int cy = y;

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run()
                            {
                                if (Math.random() < 0.5)
                                    _space.getCell(cx, cy).set();
                                else
                                    _space.getCell(cx, cy).kill();

                                _space.reportProgress(cx * factor + cy);
                            }
                        });
                    }
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run()
                    {
                        _space.finish();
                    }
                });
            }
        }).start();

        /*
        for (int x = 0; x < _space.getSpaceWidth(); x++)
        {
            for (int y = 0; y < _space.getSpaceHeight(); y++)
            {
                if (Math.random() < 0.5)
                    _space.getCell(x, y).set();
                else
                    _space.getCell(x, y).kill();
            }
        }
        */
    }

    public void step()
    {
        boolean[][] copy = new boolean[_space.getSpaceWidth()][_space.getSpaceHeight()];

        LinkedList<Cell> changed = new LinkedList<>();

        // Determine cell life
        for (int x = 0; x < _space.getSpaceWidth(); x++)
        {
            for (int y = 0; y < _space.getSpaceHeight(); y++)
            {
                copy[x][y] = getSurvival(_space.getCell(x, y));
                if (copy[x][y] != _space.getCell(x, y).isAlive())
                    changed.addFirst(_space.getCell(x, y).getCell());
            }
        }

        // Copy into space
        for (Cell c : changed)
        {
            int x = c.getX();
            int y = c.getY();

            if (copy[x][y])
                _space.getCell(x, y).set();
            else
                _space.getCell(x, y).kill();
        }
    }

    public void start()
    {
        _stop = false;

        new Thread(new Runnable() {
            public void run()
            {
                while (!_stop)
                {
                    try
                    {
                        step();
                        Thread.sleep(_step);
                    } catch (InterruptedException ex)
                    {
                        break;
                    }
                }
            }
        }).start();
    }

    public void stop()
    {
        _stop = true;
    }

    private boolean getSurvival(CellViewModel cell)
    {
        int xneighbors = getNumLivingNeighbors(cell.getX(), cell.getY());

        if (cell.isAlive() && xneighbors >= _minSurvival && xneighbors <= _maxSurvival)
        {
            return true;
        }
        else if (xneighbors >= _minBirth && xneighbors <= _maxBirth)
        {
            return true;
        }
        else return false;
    }

    private int getNumLivingNeighbors(int x, int y)
    {
        int xneighbors = 0;

        LinkedList<CellViewModel> neighbors = new LinkedList<CellViewModel>();

        if (x == 0)
        {
            if (y == 0) // Northwest corner
            {
                if (_taurus)
                {
                    neighbors.add(_space.getCell(_space.getSpaceWidth() - 1, _space.getSpaceHeight() - 1)); // Northwest
                    neighbors.add(_space.getCell(_space.getSpaceWidth() - 1, y)); // West
                    neighbors.add(_space.getCell(_space.getSpaceWidth() - 1, y + 1)); // Southwest

                    neighbors.add(_space.getCell(x, _space.getSpaceHeight() - 1)); // North
                    neighbors.add(_space.getCell(x + 1, _space.getSpaceHeight() - 1)); // Northeast
                }

                neighbors.add(_space.getCell(x + 1, y)); // East
                neighbors.add(_space.getCell(x + 1, y + 1)); // Southeast
                neighbors.add(_space.getCell(x, y + 1)); // South
            }

            else if (y == _space.getSpaceHeight() - 1) // Southwest corner
            {
                if (_taurus)
                {
                    neighbors.add(_space.getCell(_space.getSpaceWidth() - 1, y - 1)); // Northwest
                    neighbors.add(_space.getCell(_space.getSpaceWidth() - 1, y)); // West
                    neighbors.add(_space.getCell(_space.getSpaceWidth() - 1, 0)); // southwest

                    neighbors.add(_space.getCell(x, 0)); // South
                    neighbors.add(_space.getCell(x + 1, 0)); // Southeast
                }

                neighbors.add(_space.getCell(x, y - 1)); // North
                neighbors.add(_space.getCell(x + 1, y - 1)); // Northeast
                neighbors.add(_space.getCell(x + 1, y)); // East
            }

            else // West edge
            {
                if (_taurus)
                {
                    neighbors.add(_space.getCell(_space.getSpaceWidth() - 1, y - 1)); // Northwest
                    neighbors.add(_space.getCell(_space.getSpaceWidth() - 1, y)); // West
                    neighbors.add(_space.getCell(_space.getSpaceWidth() - 1, y + 1)); // Southwest
                }

                neighbors.add(_space.getCell(x, y - 1)); // North
                neighbors.add(_space.getCell(x, y + 1)); // South
                neighbors.add(_space.getCell(x + 1, y - 1)); // Northeast
                neighbors.add(_space.getCell(x + 1, y)); // East
                neighbors.add(_space.getCell(x + 1, y + 1)); // Southeast
            }
        }

        else if (x == _space.getSpaceWidth() - 1)
        {
            if (y == 0) // Northeast corner
            {
                if (_taurus)
                {
                    neighbors.add(_space.getCell(0, _space.getSpaceHeight() - 1)); // Northeast
                    neighbors.add(_space.getCell(0, y)); // East
                    neighbors.add(_space.getCell(0, y + 1)); // Southeast

                    neighbors.add(_space.getCell(x, _space.getSpaceHeight() - 1)); // North
                    neighbors.add(_space.getCell(x - 1, _space.getSpaceHeight() - 1)); // Northwest
                }

                neighbors.add(_space.getCell(x - 1, y)); // West
                neighbors.add(_space.getCell(x - 1, y + 1)); // Southwest
                neighbors.add(_space.getCell(x, y + 1)); // South
            }

            else if (y == _space.getSpaceHeight() - 1) // Southeast corner
            {
                if (_taurus)
                {
                    neighbors.add(_space.getCell(0, y - 1)); // Northeast
                    neighbors.add(_space.getCell(0, y)); // East
                    neighbors.add(_space.getCell(0, 0)); // Southeast

                    neighbors.add(_space.getCell(x, 0)); // South
                    neighbors.add(_space.getCell(x - 1, 0)); // Southwest
                }

                neighbors.add(_space.getCell(x - 1, y)); // West
                neighbors.add(_space.getCell(x - 1, y - 1)); // Northwest
                neighbors.add(_space.getCell(x, y - 1)); // North
            }

            else // East edge
            {
                if (_taurus)
                {
                    neighbors.add(_space.getCell(0, y - 1)); // Northeast
                    neighbors.add(_space.getCell(0, y)); // East
                    neighbors.add(_space.getCell(0, y + 1)); // Southeast
                }

                neighbors.add(_space.getCell(x, y - 1)); // North
                neighbors.add(_space.getCell(x - 1, y - 1)); // Northwest
                neighbors.add(_space.getCell(x - 1, y)); // West
                neighbors.add(_space.getCell(x - 1, y + 1)); // Southwest
                neighbors.add(_space.getCell(x, y + 1)); // South
            }
        }

        else if (y == 0) // North edge
        {
            if (_taurus)
            {
                neighbors.add(_space.getCell(x - 1, _space.getSpaceHeight() - 1)); // Northwest
                neighbors.add(_space.getCell(x, _space.getSpaceHeight() - 1)); // North
                neighbors.add(_space.getCell(x + 1, _space.getSpaceHeight() - 1)); // Northeast
            }

            neighbors.add(_space.getCell(x - 1, y)); // West
            neighbors.add(_space.getCell(x - 1, y + 1)); // Southwest
            neighbors.add(_space.getCell(x, y + 1)); // South
            neighbors.add(_space.getCell(x + 1, y + 1)); // Southeast
            neighbors.add(_space.getCell(x + 1, y)); // East
        }

        else if (y == _space.getSpaceHeight() - 1)
        {
            if (_taurus)
            {
                neighbors.add(_space.getCell(x - 1, 0)); // Southwest
                neighbors.add(_space.getCell(x, 0)); // South
                neighbors.add(_space.getCell(x + 1, 0)); // Southeast
            }

            neighbors.add(_space.getCell(x - 1, y)); // West
            neighbors.add(_space.getCell(x - 1, y - 1)); // Northwest
            neighbors.add(_space.getCell(x, y - 1)); // North
            neighbors.add(_space.getCell(x + 1, y - 1)); // Northeast
            neighbors.add(_space.getCell(x + 1, y)); // East
        }

        else
        {
            neighbors.add(_space.getCell(x - 1, y - 1)); // Northwest
            neighbors.add(_space.getCell(x, y - 1)); // North
            neighbors.add(_space.getCell(x + 1, y - 1)); // Northeast
            neighbors.add(_space.getCell(x + 1, y)); // East
            neighbors.add(_space.getCell(x + 1, y + 1)); // Southeast
            neighbors.add(_space.getCell(x, y + 1)); // South
            neighbors.add(_space.getCell(x - 1, y + 1)); // Southwest
            neighbors.add(_space.getCell(x - 1, y)); // West
        }

        for (CellViewModel vm : neighbors)
        {
            if (vm.isAlive())
                xneighbors++;
        }

        return xneighbors;
    }

}
