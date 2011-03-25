import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ResourceMetaData implements Serializable
{
    private static final long serialVersionUID = 1L;
    public String name;
    public int ver;
    public List<byte[]> nonImgLayers = new LinkedList<byte[]>();
    public Map<Layer, byte[]> imgFlags = new HashMap<Layer, byte[]>();
    
}
