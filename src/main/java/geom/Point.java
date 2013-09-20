package geom;

public class Point {

    public double x;
    public double y;

    public Point()
    {
        this(0, 0);
    }

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double distance(Point p)
    {
        double pow1 = (x - p.x);
        pow1 *= pow1;

        double pow2 = (y - p.y);
        pow2 *= pow2;

        return Math.sqrt(pow1 + pow2);
    }

    public String toString()
    {
        return "{\"x\":" + x + "," + "\"y\":"+y+"}";
    }

    public Point add(Point p) {
        return new Point(x + p.x, y + p.y);
    }

    public Point subtract(Point p) {
        return new Point(x - p.x, y - p.y);
    }

    public Point clone()
    {
        return new Point(x, y);
    }

}
