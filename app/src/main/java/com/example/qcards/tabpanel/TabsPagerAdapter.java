package com.example.qcards.tabpanel;

import java.util.HashMap;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.example.qcards.R;
import com.example.qcards.common.view.SlidingTabLayout;
//import com.example.qcards.tabpanel1.Tab2Activity;

public class TabsPagerAdapter extends FragmentPagerAdapter {
  private static HashMap<Integer, Fragment> mPageReferenceMap = new HashMap<Integer, Fragment>();
  public static final int INDEX_TAB_1 = 0;
  public static final int INDEX_TAB_2 = 1;
  private String[] tabTitles;
  Context mContext;
  
  public TabsPagerAdapter(FragmentManager fm, Context c) {
	  super(fm);
	  mContext = c;
  }

  @Override
  public Fragment getItem(int index) {
  	
      switch (index) 
      {
      case INDEX_TAB_1:
          // All cards
    	  Fragment fragmentTab1 = new Tab1Activity();
    	    mPageReferenceMap.put(index, fragmentTab1);
    	    return fragmentTab1;
    	  
          //return new Tab1Activity();
      case INDEX_TAB_2:
      	// Groups
    	  Fragment fragmentTab2 = new Tab2Activity();
  	      mPageReferenceMap.put(index, fragmentTab2);
  	    return fragmentTab2;
          //return new Tab2Activity();
      }

      return null;
  }
  
  /**
   * Return the title of the item at {@code position}. This is important as what this method
   * returns is what is displayed in the {@link SlidingTabLayout}.
   * <p>
   * Here we construct one using the position value, but for real application the title should
   * refer to the item's contents.
   */
  @Override
  public CharSequence getPageTitle(int position) {
      // load slide menu items
	  tabTitles = mContext.getResources().getStringArray(R.array.tabTitles);
	  return tabTitles[position];

      //return "Item " + (position + 1);
  }
  
  @Override
  public int getCount() {
      // get item count - equal to number of tabs
      return 2;
  }
  
  public void destroyItem(View container, int position, Object object) {
	    //super.destroyItem(container, position, object);
	    mPageReferenceMap.remove(position);
	}
  
  public static Fragment getFragment(int key) {
      return mPageReferenceMap.get(key);
  }
  
  


}