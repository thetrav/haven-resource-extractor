import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


public class HavenAudioResMaker
{
    private static final String CHARSET_NAME = "US-ASCII";
    private static final Charset CHARSET = Charset.forName(CHARSET_NAME);
    
    private static final String MAGIC_HAVEN_SIG = "Haven Signature 1";
    private static final byte[] HAVEN_SIG = MAGIC_HAVEN_SIG.getBytes(CHARSET);
    
    private static final String MAGIC_HAVEN_AUDIO_TYPE_NAME = "audio";
    private static final byte[] HAVEN_AUDIO_TYPE_NAME = MAGIC_HAVEN_AUDIO_TYPE_NAME.getBytes(CHARSET);
    
    public static void main(String[] args)
    {
        try
        {
            System.out.println("making file...");
            FileOutputStream out = new FileOutputStream("test.res");
            try
            {
                System.out.println("writing sig...");
                out.write(HAVEN_SIG);
                
                System.out.println("writing version...");
                int version = 0xFFFF;
                System.out.println("version:"+version);
                byte[] versionBytes = new byte[2];
                collectBytesViaTruncate(version, versionBytes);
                
                out.write(versionBytes);
                
                System.out.println("writing type name...");
                out.write(HAVEN_AUDIO_TYPE_NAME);
                
                out.write(0x00);
                
                System.out.println("reading input file...");
                final File oggFile = new File("bark.ogg");
                final long length = oggFile.length();
                System.out.println("file length:"+length);
                
                System.out.println("writing data length...");
                final byte[] dataLengthBytes = new byte[4];
                collectBytesViaTruncate((int)length, dataLengthBytes);
                out.write(dataLengthBytes);
                
                System.out.println("writing data...");
                final InputStream in = new FileInputStream(oggFile);
                try
                {
                    for(int i=0; i<length; i+=Integer.MAX_VALUE)
                    {
                        final long remaining = length - i;
                        if (remaining < Integer.MAX_VALUE)
                        {
                            transfer(in, out, (int)remaining);
                        }
                        else
                        {
                            transfer(in, out, Integer.MAX_VALUE);
                        }
                    }
                }
                finally
                {
                    in.close();
                }
            }
            finally
            {
                System.out.println("attempting to flush output...");
                out.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("fin");
    }

    private static void collectBytesViaTruncate(int version, byte[] versionBytes)
    {
        for(int i=0; i< versionBytes.length; i++)
        {
            versionBytes[versionBytes.length-1-i] = collectSignificantByte(version, i);
        }
    }

    private static byte collectSignificantByte(int version, final int shifts)
    {
        final byte b2 = (byte) ((version >> 8*shifts) & 0xFF);
        return b2;
    }

    private static void transfer(final InputStream in, final FileOutputStream out, final int bytes) throws IOException
    {
        final byte[] buf = new byte[bytes];
        final int readCount = in.read(buf);
        if(readCount != buf.length)
        {
            throw new IOException("bad read count");
        }
        out.write(buf);
    }
}
