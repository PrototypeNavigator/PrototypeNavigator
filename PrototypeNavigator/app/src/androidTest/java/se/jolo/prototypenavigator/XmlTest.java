package se.jolo.prototypenavigator;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by Joel on 2016-02-08.
 */
public class XmlTest extends ApplicationTest{

    @Test
    public void test(){
        File xml = new File(getContext().getResources().getXml(R.xml.routexmltest));

        assertTrue(xml.exists());
    }
}
