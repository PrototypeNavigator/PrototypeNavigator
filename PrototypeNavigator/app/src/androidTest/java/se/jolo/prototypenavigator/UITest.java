package se.jolo.prototypenavigator;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.jolo.prototypenavigator.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Holstad on 29/03/16.
 */
@RunWith(AndroidJUnit4.class)
public class UITest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void openRouteTest(){
        onView(withText("Välj rutt")).perform(click());
    }

    @Test
    public void mapButtonTest(){
        onView(withText("Välj rutt")).perform(click());
        onView(withText("-")).perform(click()).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("+")).perform(click()).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.my_toolbar)).perform(click());

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Visa stop")).perform(click());
    }
}
