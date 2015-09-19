

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

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.notNullValue;
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

        setActivityInitialTouchMode(true);

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
    public void testClickActionBarItem() throws InterruptedException {

//--------------------------------------------------------------------------------------------------
// Sing in
// -------------------------------------------------------------------------------------------------

        // SigInActivity.java
        // Click on the Button Sign In
        onView(withId(R.id.button4))
                .perform(click());

        // Type text (Name) and close keyboard
        onView(withId(R.id.et_name)).perform(typeText("Qcards"),
                closeSoftKeyboard());

        // Type text (Surname) and close keyboard
        onView(withId(R.id.et_lastName)).perform(typeText("Automated_Test"),
                closeSoftKeyboard());

        // Type text (E.mail)
        onView (withId (R.id.et_email))
                .check(matches(isDisplayed()))
                .perform(scrollTo(), typeText("yourqcards@gmail.com")
                );
        // Type text (Password) and then press the button.
        onView(withId(R.id.et_password))
                .perform(typeText("hello people")
                );

        //onView(withId(is(R.id.button1))).perform(closeSoftKeyboard(),click());

        // now, click on sign in.
        onView(withId(R.id.button1))
                     .perform(scrollTo(), click());

//--------------------------------------------------------------------------------------------------
// Create a new Qcard
// -------------------------------------------------------------------------------------------------

        // DisplayCardsLayouts.java
        // Swipe to see Qcards
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.pager)).perform(swipeLeft());

        // Click on the image to select the Qcard
        onView(allOf(withId(R.id.card_image), isDisplayed())).perform(click());

        // EditCardActivity.java
        // Type name that will be included in the card
        onView(withId(R.id.EditTextName)).perform(scrollTo(), typeText("Pancho"));
        // Type phone number that will be included in the card
        onView(withId(R.id.EditTextPhone)).perform(scrollTo(), typeText("123"));

        // Click on the action bar button save
        onView(withId(R.id.action_save))
                .perform(click());

        // Press back
        onView(withId(android.R.id.home))
                .perform(click());

//--------------------------------------------------------------------------------------------------
// Go to Groups and create a Group for friends
// -------------------------------------------------------------------------------------------------

        // MainActivity.java
        // Swipe to see Groups
        onView(withId(R.id.pager)).perform(swipeLeft());

        Thread.sleep(1000);

        // Click on the action bar button create
        onView(withId(R.id.action_new))
                .perform(click());

        // Type group name
        onView(withId(R.id.txt_your_name)).perform(typeText("Friends"));

        // Click on ok to create the group
        onView(allOf(withText("Ok")))
                .perform(click());

        // Open the overflow menu from contextual action mode.
        //openContextualActionModeOverflowMenu();

        // Click on the card created
        //onView(allOf(withText("Pancho")))
          //      .perform(click());
        //onView(allOf(withText("Pancho")))
              //.perform(click());
        //onData(anything()).inAdapterView(withId(R.id.pinnedListViewCab)).atPosition(0).perform(click());
        //onData(allOf(withText("Pancho"),isFocusable())).inAdapterView(withId(R.id.pinnedListViewCab)).atPosition(0).perform(click());
        //onData(allOf(withText("Pancho"),isFocusable())).perform(click());
        //onData(anything()).inAdapterView(allOf(withText("Pancho"), hasFocus())).atPosition(0).perform(click());
        //onData(allOf(withText("Pancho"))).inAdapterView(withId(R.id.pinnedListViewCab)).perform(click());
        Thread.sleep(1000);

        // Click on the action bar button save
        onView(withId(R.id.action_group))
                .perform(click());

        Thread.sleep(1000);
        // Swipe to see cards again
        onView(withId(R.id.pager)).perform(swipeRight());

//--------------------------------------------------------------------------------------------------
// Click on a Qcard
// -------------------------------------------------------------------------------------------------

        // Click on the card created
        onView(allOf(withText("Pancho")))
                .perform(click());

//--------------------------------------------------------------------------------------------------
// Edit Qcard
// -------------------------------------------------------------------------------------------------

        // Click on the action bar button edit
        onView(withId(R.id.action_edit))
                .perform(click());

        // EditCardActivity.java
        // Type name that will be included in the card
        onView(withId(R.id.EditTextEmail)).perform(scrollTo(), typeText("pancho@gmail.com"));

        // Click on the action bar button save
        onView(withId(R.id.action_save))
                .perform(click());

        // Press back
        onView(withId(android.R.id.home))
                .perform(click());

//--------------------------------------------------------------------------------------------------
// Create a new Qcard
// -------------------------------------------------------------------------------------------------

        // Click on the action bar button new
        //onView(allOf(withId(R.id.action_new), isDisplayed())).perform(click());
        onView(withId(R.id.action_new))
                .perform(click());

        // DisplayCardsLayouts.java
        // Swipe to see Qcards
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.pager)).perform(swipeLeft());

        // Click on the image to select the Qcard
        onView(allOf(withId(R.id.card_image), isDisplayed())).perform(click());

        // EditCardActivity.java
        // Type name that will be included in the card
        onView(withId(R.id.EditTextName)).perform(scrollTo(), typeText("Lancelot"));
        // Type phone number that will be included in the card
        onView(withId(R.id.EditTextPhone)).perform(scrollTo(), typeText("321"));

        // Click on the action bar button save
        onView(withId(R.id.action_save))
                .perform(click());

        // Press back
        onView(withId(android.R.id.home))
                .perform(click());

//--------------------------------------------------------------------------------------------------
// Sort by Date
// -------------------------------------------------------------------------------------------------

        Thread.sleep(1000);
        // Click on the action bar button sort by
        //onView(allOf(withId(R.id.action_new), isDisplayed())).perform(click());

        //onView(withContentDescription("Sort by"))
              //.perform(click());

        //openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withId(R.id.action_sortby))
              .perform(click());

        //onView(allOf(withId(R.id.action_sortby)))
          //      .perform(click());

        onView(allOf(withText("Date modified")))
                .perform(click());

        onView(allOf(withText("Yes")))
                .perform(click());

        Thread.sleep(1000);

//--------------------------------------------------------------------------------------------------
// Create a Group and include a Qcard
// -------------------------------------------------------------------------------------------------

        onView(allOf(withId(R.id.im_expand), hasSibling(withText("Pancho"))))
                .perform(click());

        onView(allOf(withText("Group")))
                .perform(click());

        onView(allOf(withText("Create new group")))
                .perform(click());

        // Type group name
        onView(withId(R.id.txt_your_name)).perform(typeText("My Qcards"));

        // Click on ok to create the group
        onView(allOf(withText("Ok")))
                .perform(click());

        Thread.sleep(1000);

        onView(allOf(withText("My Qcards")))
                .perform(click());

        onView(allOf(withText("Ok")))
                .perform(click());

        // MainActivity.java
        // Swipe to see Groups
        onView(withId(R.id.pager)).perform(swipeLeft());

        Thread.sleep(1000);

        onView(allOf(withText("My Qcards")))
                .perform(click());

        Thread.sleep(1000);

        onView(allOf(withText("My Qcards")))
                .perform(click());

        // MainActivity.java
        // Swipe to see Groups
        onView(withId(R.id.pager)).perform(swipeRight());

        //onView(allOf(withId(R.id.im_expand), hasSibling(withText("Pancho"))))
          //      .perform(click());

        //R.id.iv_icon

//--------------------------------------------------------------------------------------------------
// Navigation Drawer
// -------------------------------------------------------------------------------------------------

        openDrawer(R.id.drawer_layout);

        onData(anything()).inAdapterView(withId(R.id.left_drawer)).atPosition(3).perform(click());

        Thread.sleep(1000);
        pressBack();
        // Press back
        //onView(withId(android.R.id.home))
          //      .perform(click());

        //closeDrawer(R.id.drawer_layout);
        Thread.sleep(2000);
        /*openDrawer(R.id.drawer_layout);
        closeDrawer(R.id.drawer_layout);
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
*/

        //Thread.sleep(5000);





        //onData(anything()).inAdapterView(withId(R.id.list_view)).atPosition(0).perform(click());
        /**
         * Test opening the Navigation Drawer and pressing the back button.
         * Espresso: openDrawer, pressBack
         */

        //Espresso.pressBack();
        //openDrawer(R.id.drawer_layout);
        //pressBack();

        //onView(isRoot()).perform(waitAtLeast(2000));



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
