import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MFile implements AutoCloseable {
    private String filepath;
    private RandomAccessFile file;
    private MappedByteBuffer map;

    public MFile(String filepath) {
        this.filepath = filepath;
        this.file = null;
        this.map = null;
    }

    public MappedByteBuffer mapFile() {
        try {
            file = new RandomAccessFile(filepath, "r");
            FileChannel channel = file.getChannel();
            map = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void unmap() {
        try {
            if (map != null) {
                map.force();
                clean(map);
            }

            if (file != null) {
                file.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clean(MappedByteBuffer buffer) {
        if (buffer == null) {
            return;
        }
        sun.misc.Cleaner cleaner = ((sun.nio.ch.DirectBuffer) buffer).cleaner();
        if (cleaner != null) {
            cleaner.clean();
        }
    }


    public void close() {
        unmap();
    }

    public static void main(String[] args) {
     
        String filepath = "yout"; 
        try (MFile mfile = new MFile(filepath)) {
            if (mfile.mapFile() != null) {
               
            }
        }
    }
}
