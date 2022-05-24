package cn.dreamchase.android.seven.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.EdgeEffectCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.dreamchase.android.seven.LauncherActivity;
import cn.dreamchase.android.seven.R;
import cn.dreamchase.android.seven.home.HomeActivity;

public class FirstLauncherActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private int[] images = new int[]{
            R.drawable.yin_dao_ye_1,
            R.drawable.yin_dao_ye_2,
            R.drawable.yin_dao_ye_3
    };

    // 显示图片View
    private List<View> imageViews = new ArrayList<>() ;
    // 显示圆点view
    private List<ImageView> tips = new ArrayList<>();
    private ViewGroup viewGroup;

    /**
     * Android中可以的ListView，ScrollView，RecyclerView等滑动到界面的边界的时候会出现一个半透明的颜色边
     * 框。这个边框就是Android的边缘效果。主要是类EdgeEffect，EdgeEffectCompat管理。效果如下图
     */
    private EdgeEffect rightEffect;

    private TextView gotoMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_first);

        viewGroup = findViewById(R.id.viewGroup);
        viewPager = findViewById(R.id.viewPager);
        gotoMain = findViewById(R.id.tv_goto_main);

        try {
            Field rightEdgeField = viewPager.getClass().getDeclaredField("mRightEdge");

            if (rightEdgeField != null) {
                rightEdgeField.setAccessible(true);

                rightEffect = (EdgeEffect) rightEdgeField.get(viewPager);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setImageResource(images[i]);

            imageViews.add(imageView);
        }

        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10,10));
            if (i == 0){
                imageView.setBackgroundResource(R.drawable.icon_first_launcher_page_select_one);
            }else {
                imageView.setBackgroundResource(R.drawable.icon_first_launcher_page_normal);
            }

            tips.add(imageView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            viewGroup.addView(imageView, layoutParams);
        }


        viewPager.setAdapter(new PreviewImageAdapter());
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        gotoMain.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_goto_main) {
                gotoMain();
            }
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == images.length - 1) {
                gotoMain.setVisibility(View.VISIBLE);
            }else {
                gotoMain.setVisibility(View.INVISIBLE);
            }
            setImageBackground(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (rightEffect != null && !rightEffect.isFinished()) {
                gotoMain();
            }
        }
    };

    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.size(); i++) {
            if (i == selectItems) {
                if (i == 0) {
                    tips.get(i).setBackgroundResource(R.drawable.icon_first_launcher_page_select_one);
                }else if (i == 1) {
                    tips.get(i).setBackgroundResource(R.drawable.icon_first_launcher_page_select_two);

                }else if (i == 2) {
                    tips.get(i).setBackgroundResource(R.drawable.icon_first_launcher_page_select_three);
                }
            }else {
                tips.get(i).setBackgroundResource(R.drawable.icon_first_launcher_page_normal);
            }
        }
    }


    public class PreviewImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (position < imageViews.size()) {
                ((ViewPager)container).removeView(imageViews.get(position));
            }
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ((ViewPager)container).addView(imageViews.get(position));
            return imageViews.get(position);
        }
    }


    private void gotoMain() {
        setFirstLauncherBoolean();
        Intent intent = new Intent(FirstLauncherActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void setFirstLauncherBoolean() {
        SharedPreferences sharedPreferences = getSharedPreferences("android", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LauncherActivity.FIRST_LAUNCHER,true);
        editor.commit();
    }
}
