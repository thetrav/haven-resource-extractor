import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class HavenTexureResEncoder
{
    public static void save(String layerType, InputStream in, OutputStream out, byte[] imgFlags) throws Exception
    {
        final byte[] layerName = layerType.getBytes();
        out.write(layerName); // layer name
        out.write(0); // null terminator

        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        for(int i= in.read(); i>=0; i = in.read())
        {
            outStream.write(i);
        }
        final byte[] imageBytes = outStream.toByteArray();

        // img length
        byte[] buf = new byte[4];
        int32e(imageBytes.length+imgFlags.length, buf, 0);
        out.write(buf);

        // img flags
        out.write(imgFlags);
        
        // img
        out.write(imageBytes);
        TestTextureExtractor.log("wrote "+layerType+" of len:" + imageBytes.length);
    }

    static byte sb(int b)
    {
        if (b > 127)
            return ((byte) (-256 + b));
        else
            return ((byte) b);
    }

    static void uint16e(int num, byte[] buf, int off)
    {
        buf[off] = sb(num & 0xff);
        buf[off + 1] = sb((num & 0xff00) >> 8);
    }

    static void uint32e(long num, byte[] buf, int off)
    {
        buf[off] = sb((int) (num & 0xff));
        buf[off + 1] = sb((int) ((num & 0xff00) >> 8));
        buf[off + 2] = sb((int) ((num & 0xff0000) >> 16));
        buf[off + 3] = sb((int) ((num & 0xff000000) >> 24));
    }

    static int uint16d(byte[] buf, int off)
    {
        return (ub(buf[off]) + (ub(buf[off + 1]) * 256));
    }

    static int ub(byte b)
    {
        if (b < 0)
            return (256 + b);
        else
            return (b);
    }

    static int int32d(byte[] buf, int off)
    {
        long u = uint32d(buf, off);
        if (u > Integer.MAX_VALUE)
            return ((int) ((((long) Integer.MIN_VALUE) * 2) - u));
        else
            return ((int) u);
    }

    static long uint32d(byte[] buf, int off)
    {
        return (ub(buf[off]) + (ub(buf[off + 1]) * 256) + (ub(buf[off + 2]) * 65536) + (ub(buf[off + 3]) * 16777216));
    }

    static int int16d(byte[] buf, int off)
    {
        int u = uint16d(buf, off);
        if (u > 32767)
            return (-65536 + u);
        else
            return (u);
    }

    static Coord imgsz(BufferedImage img)
    {
        return (new Coord(img.getWidth(), img.getHeight()));
    }

    static void int32e(final int num, byte[] buf, int off)
    {
        if (num < 0)
            uint32e(0x100000000L + ((long) num), buf, off);
        else
            uint32e(num, buf, off);
    }
}
