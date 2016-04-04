package se.jolo.prototypenavigator;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import se.jolo.prototypenavigator.task.Loader;
import se.jolo.prototypenavigator.activities.Launcher;
import se.jolo.prototypenavigator.model.Route;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by Holstad on 29/03/16.
 */
@RunWith(AndroidJUnit4.class)
public class PreLoadedFilesTest {
    @Rule
    public ActivityTestRule<Launcher> activityRule = new ActivityTestRule<>(
            Launcher.class);

    @Test
    public void checkPreLoadedFiles() throws IOException {
        Loader loader = new Loader(activityRule.getActivity());
        Route route = loader.getPreLoadedRoute("Route-14260A.xml");
        Log.d("RouteTest",route.getName());
    }
}
