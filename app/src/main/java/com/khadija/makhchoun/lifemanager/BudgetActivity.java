package com.khadija.makhchoun.lifemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.khadija.makhchoun.lifemanager.Fragment.DashBoardFragment;

import static com.khadija.makhchoun.lifemanager.R.id.bottom_navigation;

public class BudgetActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem tabItem1,tabItem2,tabItem3;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    private DashBoardFragment dashboardFragment;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        dashboardFragment=new DashBoardFragment();

        tabLayout=(TabLayout)findViewById(R.id.tablayout1);
        tabItem1=(TabItem)findViewById(R.id.tab1);
        tabItem2=(TabItem)findViewById(R.id.tab2);
        tabItem3=(TabItem)findViewById(R.id.tab3);
        viewPager=(ViewPager)findViewById(R.id.vpager);

        pageAdapter=new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        // initialize and asign variable
        BottomNavigationView bottomNavigationView=findViewById(bottom_navigation);
        // set home selected
        bottomNavigationView.setSelectedItemId(R.id.budget);

        frameLayout=findViewById(R.id.main_frame);

        // perform item selectedListener


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notes:
                        startActivity(new Intent(getApplicationContext(),BlocNotesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(), com.khadija.makhchoun.lifemanager.BudgetActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.todolist:
                        startActivity(new Intent(getApplicationContext(),ToDoListActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.documents:
                        startActivity(new Intent(getApplicationContext(),Documents.class));
                        overridePendingTransition(0,0);
                        return true;
                    default:
                        return false;

                }

            }
        });



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition()==1 || tab.getPosition()==2)
                    pageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //listen for scroll or page change
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

}