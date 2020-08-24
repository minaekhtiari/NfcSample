package com.example.nfcsample;

import org.junit.Test;

import androidx.test.core.app.ActivityScenario;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest {
    @Test
 public  void  testActivity_inView() {
        Object activityScenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.main)).check(matches(isDisplayed()));
    }


}