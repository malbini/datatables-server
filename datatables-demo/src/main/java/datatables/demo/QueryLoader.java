package datatables.demo;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class QueryLoader {
    public String load(String resourceName) {
        InputStream is = QueryLoader.class.getResourceAsStream(resourceName);

        if(is == null) {
            throw new RuntimeException("Cannot find resource: [" + resourceName + "]");
        }

        try {
            return IOUtils.toString(is);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
