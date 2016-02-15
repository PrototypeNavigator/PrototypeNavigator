package se.jolo.prototypenavigator;

import android.content.Context;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.model.StopPoint;
import se.jolo.prototypenavigator.utils.FileLoadAndConvert;
import se.jolo.prototypenavigator.utils.JsonMapper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Joel on 2016-02-15.
 */
@RunWith(MockitoJUnitRunner.class)
public class ObjectTest {

    @Mock
    Context mockContext;

    @Mock
    private FileLoadAndConvert flac;

    @Mock
    private JsonMapper jm;

    @Mock
    private List<RouteItem> routeItems;

    private Route route;
    private RouteItem routeItem;
    private StopPoint stopPoint;

    @Before
    public void testInit() {
        MockitoAnnotations.initMocks(this);

        route = mock(Route.class);
        routeItem = mock(RouteItem.class);
        stopPoint = mock(StopPoint.class);

        routeItems.add(routeItem);
    }

    @Test
    public void testRouteNotNull() throws IOException, JSONException {
        when(jm.objectifyRoute()).thenReturn(new Route());

        route = jm.objectifyRoute();

        assertNotNull(route);
    }

    @Test
    public void testRouteName() throws IOException, JSONException {
        when(route.getName()).thenReturn(13757);

        int routeName = route.getName();

        assertEquals(routeName, 13757);
    }

    @Test
    public void testRouteRouteItemsNotEmpty() {
        when(route.getRouteItems()).thenReturn(routeItems);

        assertFalse(route.getRouteItems().isEmpty());
    }

    @Test
    public void testStopPointNotNull() {
        when(routeItem.getStopPoint()).thenReturn(new StopPoint());

        stopPoint = routeItem.getStopPoint();

        assertNotNull(stopPoint);
    }
}
