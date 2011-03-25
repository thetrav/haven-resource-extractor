import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class HavenTextureResExtractor
{

    public static List<Layer> load(InputStream in, ResourceMetaData meta) throws Exception
    {
        final String sig = "Haven Resource 1";
        final byte[] sigBuf = new byte[sig.length()];
        readall(in, sigBuf);
        if (!sig.equals(new String(sigBuf)))
        {
            throw (new Exception("Invalid res signature"));
        }
        final byte[] verBuf = new byte[2];
        readall(in, verBuf);
        meta.ver = uint16d(verBuf, 0);
        TestTextureExtractor.log("ver="+meta.ver);
        final List<Layer> layers = new LinkedList<Layer>();
        outer: while (true)
        {
            final StringBuilder tbuf = new StringBuilder();
            while (true)
            {
                byte bb;
                int ib;
                if ((ib = in.read()) == -1)
                {
                    if (tbuf.length() == 0)
                        break outer;
                    throw (new Exception("Incomplete resource "));
                }
                bb = (byte) ib;
                if (bb == 0)
                    break;
                tbuf.append((char) bb);
            }
            final byte[] lenBuf = new byte[4];
            readall(in, lenBuf);
            int len = int32d(lenBuf, 0);
            TestTextureExtractor.log("len="+len);
            final byte[] layerBuf = new byte[len];
            readall(in, layerBuf);
            final String layerTypeName = tbuf.toString();
            TestTextureExtractor.log("layerType="+layerTypeName);
            if ("image".equals(layerTypeName))
            {
                TestTextureExtractor.log("image layer found pairing image with extra data");
                final Image img = new Image(layerBuf);
                layers.add(img);
                final ByteArrayOutputStream imgFlags = new ByteArrayOutputStream();
                for(int i = 0; i < 11; i++)
                {
                    imgFlags.write(layerBuf[i]);
                }
                meta.imgFlags.put(img, imgFlags.toByteArray());
            }
            else if("tile".equals(layerTypeName))
            {
                TestTextureExtractor.log("tile layer found, pairing image with extra data");
                final Tile tile = new Tile(layerBuf);
                layers.add(tile);
                final ByteArrayOutputStream imgFlags = new ByteArrayOutputStream();
                for(int i = 0; i < 3; i++)
                {
                    imgFlags.write(layerBuf[i]);
                }
	    		meta.imgFlags.put(tile, imgFlags.toByteArray());
            }
            else
            {
                TestTextureExtractor.log("non image layer found:" + layerTypeName);
                byte[] layerNameBytes = layerTypeName.getBytes();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bytes.write(layerNameBytes);//layer name
                byte nullByte = 0;
                bytes.write(nullByte);//null terminator
                bytes.write(lenBuf);//byte length
                bytes.write(layerBuf);//byte data
                meta.nonImgLayers.add(bytes.toByteArray());
            }
        }
        for (Layer l : layers)
            l.init();
        return layers;
    }

    private static void readall(InputStream in, byte[] buf) throws Exception
    {
        int ret, off = 0;
        while (off < buf.length)
        {
            ret = in.read(buf, off, buf.length - off);
            if (ret < 0)
                throw (new Exception("Incomplete resource "));
            off += ret;
        }
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
        return    (ub(buf[off]) 
                + (ub(buf[off + 1]) * 256) 
                + (ub(buf[off + 2]) * 65536) 
                + (ub(buf[off + 3]) * 16777216));
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

}

