package com.example.qcards.hviewcards;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.qcards.UtilsPics;
import com.example.qcards.tabpanel.Tab1Activity;


/**
 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
 * sequence.
 */


public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	private boolean isLayout;
	private int ContactId;	
	private int nCards;
	private int posiCard;
	
    //public ScreenSlidePagerAdapter (android.support.v4.app.FragmentManager fm, boolean misLayout, int mContactId, int ncrs, int mposiCard) {
	public ScreenSlidePagerAdapter (android.support.v4.app.FragmentManager fm, boolean misLayout, int mContactId, int ncrs) {
    	super(fm);
    	isLayout = misLayout;
    	ContactId = mContactId;
    	nCards = ncrs;
    	//posiCard = mposiCard;
    }
    
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
    	android.support.v4.app.Fragment newFragment;
    	
    	if (isLayout){
    		newFragment = ScreenSlidePageFragment.create(position);
    	}else
    	{
    		//newFragment = ScreenSlicePageFragCards.create(position, ContactId, posiCard);
    		newFragment = ScreenSlicePageFragCards.create(position, ContactId);
    	}	
    	
    	return 	newFragment;
    }
    @Override
    public int getCount() {
        //return NUM_PAGES;
    	//ScreenSlidePageFragment.create(position);
    	/*int nc;
    	if (isLayout){ 
    		nc = nCards - Tab1Activity.posi;
    	}else{
    		nc = nCards;
    	}
        return nc;*/
    	return nCards;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
    	//if (isLayout){
    		return "LAYOUT " + (position + 1);
    	/*}
    	else
    	{
    		return "LAYOUT " + (position + 1);
    	}*/
    }
}
