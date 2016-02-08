package se.jolo.prototypenavigator.route.xml;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

import se.jolo.prototypenavigator.route.model.Route;

/**
 * Created by Joel on 2016-02-08.
 */
public final class Deserializer {

    private File xml;
    private Serializer serializer;

    public Deserializer() {
        this.serializer = new Persister();
    }

    public void deserializeXml() {

        try {
            serializer.read(Route.class, xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
