import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class WriteProperty {
    private static File file = new File("db.properties");
    private static Properties p = null;

    public static void saveProperties() throws IOException
    {
        FileOutputStream fr = new FileOutputStream(file);
        p.store(fr, "Properties");
        fr.close();
        System.out.println("After saving properties: " + p);
    }

    public static void loadProperties()throws IOException
    {
        FileInputStream fi=new FileInputStream(file);
        p.load(fi);
        fi.close();
        System.out.println("After Loading properties: " + p);
    }

    public static Properties createProperties()
    {
        if (p == null)
        {
            p = new Properties();
            p.setProperty("url","jdbc:mysql://localhost:3306");
            p.setProperty("user","mpontsasa");
            p.setProperty("password", "Alma.AlmA323");
            p.setProperty("activeDB","none");
        }

        return p;
    }
}
