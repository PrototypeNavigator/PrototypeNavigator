package se.jolo.prototypenavigator.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Holstad on 19/02/16.
 */
public class RouteItemTest {
    private RouteItem rI1;
    private RouteItem rI2;
    private RouteItem rI3;
    private RouteItem rI4;
    private RouteItem rI5;
    private List<StopPointItem> sPI;
    private StopPoint sP;

    @Before
    public void init() {
        rI1 = new RouteItem(1, 34, sP, sPI);
        rI2 = new RouteItem(2, 34, sP, sPI);
        rI3 = new RouteItem(3, 34, sP, sPI);
        rI5 = new RouteItem(1, 37, sP, sPI);
        rI4 = new RouteItem(4, 34, sP, sPI);
    }

    @Test
    public void testSorting() {
        List<RouteItem> routeItems = new ArrayList<>();
        routeItems.add(rI2);
        routeItems.add(rI4);
        routeItems.add(rI3);
        routeItems.add(rI1);
        routeItems.add(rI5);

        Collections.sort(routeItems);
        for(RouteItem routeItem : routeItems){
            System.out.println(routeItem.toString());
        }
        assertEquals(rI1.getOrder(),routeItems.get(0).getOrder());
    }
}
