import static java.lang.Math.PI;


public class Coord
{
    private static final long serialVersionUID = 1L;
    public int x, y;
    public static Coord z = new Coord(0, 0);

    public Coord(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Coord(Coord c)
    {
        this(c.x, c.y);
    }

    public Coord()
    {
        this(0, 0);
    }

    public Coord(java.awt.Dimension d)
    {
        this(d.width, d.height);
    }

    public static Coord sc(double a, double r)
    {
        return (new Coord((int) (Math.cos(a) * r), -(int) (Math.sin(a) * r)));
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof Coord))
            return (false);
        Coord c = (Coord) o;
        return ((c.x == x) && (c.y == y));
    }

    public int compareTo(Coord c)
    {
        if (c.y != y)
            return (c.y - y);
        if (c.x != x)
            return (c.x - x);
        return (0);
    }

    public Coord add(int ax, int ay)
    {
        return (new Coord(x + ax, y + ay));
    }

    public Coord add(Coord b)
    {
        return (add(b.x, b.y));
    }

    public Coord sub(int ax, int ay)
    {
        return (new Coord(x - ax, y - ay));
    }

    public Coord sub(Coord b)
    {
        return (sub(b.x, b.y));
    }

    public Coord mul(int f)
    {
        return (new Coord(x * f, y * f));
    }

    public Coord mul(double f)
    {
        return (new Coord((int) (x * f), (int) (y * f)));
    }

    public Coord inv()
    {
        return (new Coord(-x, -y));
    }

    public Coord mul(Coord f)
    {
        return (new Coord(x * f.x, y * f.y));
    }

    public Coord div(Coord d)
    {
        int v, w;

        v = ((x < 0) ? (x + 1) : x) / d.x;
        w = ((y < 0) ? (y + 1) : y) / d.y;
        if (x < 0)
            v--;
        if (y < 0)
            w--;
        return (new Coord(v, w));
    }

    public Coord div(int d)
    {
        return (div(new Coord(d, d)));
    }

    public Coord mod(Coord d)
    {
        int v, w;

        v = x % d.x;
        w = y % d.y;
        if (v < 0)
            v += d.x;
        if (w < 0)
            w += d.y;
        return (new Coord(v, w));
    }

    public boolean isect(Coord c, Coord s)
    {
        return ((x >= c.x) && (y >= c.y) && (x < c.x + s.x) && (y < c.y + s.y));
    }

    public String toString()
    {
        return ("(" + x + ", " + y + ")");
    }

    public double angle(Coord o)
    {
        Coord c = o.add(this.inv());
        if (c.x == 0)
        {
            if (c.y < 0)
                return (-PI / 2);
            else
                return (PI / 2);
        }
        else
        {
            if (c.x < 0)
            {
                if (c.y < 0)
                    return (-PI + Math.atan((double) c.y / (double) c.x));
                else
                    return (PI + Math.atan((double) c.y / (double) c.x));
            }
            else
            {
                return (Math.atan((double) c.y / (double) c.x));
            }
        }
    }

    public double dist(Coord o)
    {
        long dx = o.x - x;
        long dy = o.y - y;
        return (Math.sqrt((dx * dx) + (dy * dy)));
    }
}
