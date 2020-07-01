package org.humanhelper.travel.integration.awd;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author Андрей
 * @since 05.09.15
 */
public class AwdRouteResolver {

    public static void main(String[] arg) {
        AwdRouteResolver routeResolver = new AwdRouteResolver();
        routeResolver.readFile("/Users/admin/Downloads/9cdf212c-a98e-47a0-a891-fcefd572f8e2.jpeg");
    }


    public void readFile(String path) {
        try {
            parse(FileUtils.openInputStream(new File(path)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse(InputStream stream) {
        Metadata metadata = getMetadata(stream);
        for (GpsDirectory directory : metadata.getDirectoriesOfType(GpsDirectory.class)) {
            System.out.println(directory.getGeoLocation());
        }
    }

    private Metadata getMetadata(InputStream stream) {
        try {
            return ImageMetadataReader.readMetadata(stream);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
