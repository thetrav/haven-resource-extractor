import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Image extends Layer implements Comparable<Image>
{
    private static final long serialVersionUID = 1L;
    public final int z, subz;
    public final boolean nooff;
    public final int id;
    private int gay = -1;
    public Coord sz;
    public Coord o;

    public Image(byte[] buf) throws Exception
    {
        z = HavenTextureResExtractor.int16d(buf, 0);
        subz = HavenTextureResExtractor.int16d(buf, 2);
        /* Obsolete flag 1: Layered */
        nooff = (buf[4] & 2) != 0;
        id = HavenTextureResExtractor.int16d(buf, 5);
        o = cdec(buf, 7);
        try
        {
            img = ImageIO.read(new ByteArrayInputStream(buf, 11, buf.length - 11));
        }
        catch (IOException e)
        {
            throw (new Exception(e));
        }
        if (img == null)
            throw (new Exception("Invalid image data "));
        sz = HavenTextureResExtractor.imgsz(img);
    }

    public Coord cdec(byte[] buf, int off)
    {
        return (new Coord(HavenTextureResExtractor.int16d(buf, off), HavenTextureResExtractor.int16d(buf, off + 2)));
    }

    private boolean detectgay()
    {
        for (int y = 0; y < sz.y; y++)
        {
            for (int x = 0; x < sz.x; x++)
            {
                if ((img.getRGB(x, y) & 0x00ffffff) == 0x00ff0080)
                    return (true);
            }
        }
        return (false);
    }

    public boolean gayp()
    {
        if (gay == -1)
            gay = detectgay() ? 1 : 0;
        return (gay == 1);
    }

    public int compareTo(Image other)
    {
        return (z - other.z);
    }

    public void init()
    {
    }
}
