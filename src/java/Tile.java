import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class Tile extends Layer {
	int id;
	int w;
	char t;

	public Tile(byte[] buf) throws Exception {
	    t = (char)HavenTextureResExtractor.ub(buf[0]);
	    id = HavenTextureResExtractor.ub(buf[1]);
	    w = HavenTextureResExtractor.uint16d(buf, 2);

		img = ImageIO.read(new ByteArrayInputStream(buf, 4, buf.length - 4));
	    if(img == null) throw(new Exception("Invalid image data "));
	}

	public void init() {}
}
