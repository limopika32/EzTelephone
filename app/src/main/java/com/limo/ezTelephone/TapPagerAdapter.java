package com.limo.ezTelephone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TapPagerAdapter extends FragmentStateAdapter {
    protected TapPagerAdapter(MainActivity fragment) {
        super(fragment);
    }
    /**
     * 指定されたタブの位置(position) に対応するタブページ（Fragment）を作成する
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = new Page2();
                break;

            case 2:
                fragment = new Page3();
                break;

            default:
                // include case 0
                fragment = new PageMain();
        }
        return fragment;
    }
    /**
     * タブの数を返す
     */
    @Override
    final public int getItemCount() {
        return 3;
    }
}