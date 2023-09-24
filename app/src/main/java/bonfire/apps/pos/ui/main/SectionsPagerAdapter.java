package bonfire.apps.pos.ui.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import bonfire.apps.pos.R;

import bonfire.apps.pos.fragments.DrinksFragment;
import bonfire.apps.pos.fragments.FoodFragment;
import bonfire.apps.pos.fragments.ShishaFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.food,R.string.drinks,R.string.shisha};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position == 0){
            return new FoodFragment();
        }
        else if (position == 1){
            return new DrinksFragment();
        }
        else if (position == 2){
            return new ShishaFragment();
        }
        else
            return null;
        }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.food);
            case 1:
                return mContext.getString(R.string.drinks);
            case 2:
                return mContext.getString(R.string.shisha);
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void refreshData(int position){
        switch (position){
            case 0:
                 new FoodFragment().foodList();
            case 1:
                new DrinksFragment().drinkList();
            case 2:
                new ShishaFragment().foodList();

        }
    }




}