package bonfire.apps.pos.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import bonfire.apps.pos.R;

public class SectionStateAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private List<Fragment> mFragmentList = new ArrayList<>();

    public SectionStateAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
      /*  if(position == 0){
            return new FoodFragment();
        }
        else if (position == 1){
            return new DrinksFragment();
        }
        else if (position == 2){
            return new ShishaFragment();
        }
        else*/

       // Log.e("POSITION OF FRAG",String.valueOf(position));
            return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
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
        return 3;
    }


}
