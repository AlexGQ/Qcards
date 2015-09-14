

/**
 * Created by Alex on 12/09/2015.
 */



    /*
 * Copyright 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.qcards;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;



@RunWith(AndroidJUnit4.class)
//@SdkSuppress(minSdkVersion = 18)
@LargeTest
public class BasicInstrumentationTest
       extends ActivityInstrumentationTestCase2<MainActivity>

    {

    private MainActivity mActivity;

    public BasicInstrumentationTest() {        super(MainActivity.class);    }

    private static final String BASIC_SAMPLE_PACKAGE
            = "com.example.qcards";



    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Espresso does not start the Activity for you we need to do this manually here.
        mActivity = getActivity();
    }

    public void testPreconditions() {
        assertThat(mActivity, notNullValue());
    }

  /*

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String STRING_TO_BE_TYPED = "UiAutomator";

    UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the blueprint app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkPreconditions() {
        assertThat(mDevice, notNullValue());
    }

    @Test
    public void testClickActionBarItem() {
        // Type text and then press the button.

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "button4"))
                .click();

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "et_name"))
                .setText(STRING_TO_BE_TYPED);

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "et_lastName"))
                .setText(STRING_TO_BE_TYPED);

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "et_email"))
                .setText(STRING_TO_BE_TYPED);

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "et_password"))
                .setText(STRING_TO_BE_TYPED);



        // Verify the test is displayed in the Ui

        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "button1"))
                .click();

        UiObject2 changedText = mDevice
                .wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "et_password")),
    //                    500 /* wait 500ms *///);
     /*   assertThat(changedText.getText(), is(equalTo(STRING_TO_BE_TYPED)));

    }*/

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
   /* private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }*/




    @Test
    public void testClickActionBarItem() {
        // We make sure the contextual action bar is hidden.
        //onView(withId(R.id.hide_contextual_action_bar))
          //      .perform(click());

        // Click on the icon - we can find it by the r.Id.
        onView(withId(R.id.button4))
                .perform(click());

        // Type text and then press the button.
        onView(withId(R.id.et_name)).perform(typeText("Alex"),
                closeSoftKeyboard());

        onView(withId(R.id.et_lastName)).perform(typeText("G"),
                closeSoftKeyboard());

        onView(withId(R.id.et_email)).perform(typeText("ag@gmail.com"),
                closeSoftKeyboard());

        //onView(withId(R.id.et_password)).perform(typeText("hipeople"),scrollTo(),
          //      closeSoftKeyboard());

        //onView(withId(is(R.id.button1))).perform(closeSoftKeyboard(),click());

        // Click on a given operation button
        //onView(withId(R.id.button1)).perform(click());





        // Verify that we have really clicked on the icon by checking the TextView content.
        //onView(withId(R.id.text_action_bar_result))
          //      .check(matches(withText("Save")));
    }
    /*@Test
    public void testChangeText_newActivity() {

        // Type text and then press the button.
        onView(withId(R.id.et_name)).perform(typeText("Hola"),
                closeSoftKeyboard());

        // Click on a given operation button
        onView(withId(R.id.button1)).perform(click());
    }*/

}
//gradlew connectedAndroidTest
//gradlew cC
