// Cell Model

public class Cell
{
    private int _x;
    private int _y;

    public boolean Alive;

    public Cell(int x, int y)
    {
        _x = x;
        _y = y;
        Alive = false;
    }

    public int getX()
    {
        return _x;
    }

    public int getY()
    {
        return _y;
    }
}
