import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class Layer implements Serializable
{
    private static final long serialVersionUID = 1L;
    public BufferedImage img;
    public abstract void init();
}
