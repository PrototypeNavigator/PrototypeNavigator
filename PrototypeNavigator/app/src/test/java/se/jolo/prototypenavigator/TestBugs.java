package se.jolo.prototypenavigator;

import org.junit.Before;
import org.junit.Test;

import se.jolo.prototypenavigator.activities.Launcher;
import se.jolo.prototypenavigator.utils.Locator;

import static junit.framework.Assert.assertNotNull;

public class TestBugs {

    private Locator locator;

    @Before
    public void setUpTestLocations() {
        Launcher activity = new Launcher();
        locator = new Locator(activity.getApplicationContext(), activity);
    }

    @Test
    public void testLocations() {
        assertNotNull(locator.getLocation());
    }
}
