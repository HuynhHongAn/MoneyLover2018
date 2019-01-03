package com.nmcnpm.nhom10.moneylover;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

public class TransactionsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    ArrayList<TransactionModel> dataModels;
    ListView listView;
    private static TransactionAdapter adapter;

    FirebaseFirestore db;
    float totalPositive = 0f;
    float totalNegative = 0f;

    TextView tvNegativeTotal, tvPositiveTotal, tvTotalMoney;
    NumberFormat format;

    private static final String TAG = "TransactionsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        if (getIntent().getStringExtra("message") != null){
            Toast.makeText(getApplicationContext(), getIntent().getStringExtra("message"),
                    Toast.LENGTH_LONG).show();
        }

        tvNegativeTotal = findViewById(R.id.tvNegativeTotal);
        tvPositiveTotal = findViewById(R.id.tvPositiveTotal);
        tvTotalMoney = findViewById(R.id.tvTotalMoney);
        format = NumberFormat.getCurrencyInstance();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
//
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), TransactionCreateActivity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView=(ListView)findViewById(R.id.lvTransaction);

        dataModels= new ArrayList<>();

        db.collection("transactions")
                .orderBy("date")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Double amount = Double.valueOf(document.getData().get("amount").toString());
                                String name = document.getData().get("name").toString();
                                String date = document.getData().get("date").toString();
                                String note = document.getData().get("note").toString();
                                String wallet = document.getData().get("wallet").toString();

                                TransactionModel newTransaction = new TransactionModel(document.getId(), amount, name, date, note, wallet);
                                dataModels.add(newTransaction);
                                double tempAmount = newTransaction.getAmount();
                                if (tempAmount > 0){
                                    totalPositive += tempAmount;
                                } else {
                                    totalNegative += tempAmount;
                                }

                                Log.d(TAG, document.getId() + " => " + dataModels);

                            }

                            Collections.reverse(dataModels);

                            adapter = new TransactionAdapter(dataModels, TransactionsActivity.this);
                            tvNegativeTotal.setText(format.format(totalNegative));
                            tvPositiveTotal.setText(format.format(totalPositive));
                            tvTotalMoney.setText(format.format(totalNegative + totalPositive));

                            listView.setAdapter(adapter);

                            findViewById(R.id.progressBarHolder).setVisibility(View.INVISIBLE);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    TransactionModel dataModel= dataModels.get(position);
                                    Log.d(TAG, parent.getId() + " Hello world!");



//                                    Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getDate()+" API: "+dataModel.getAmount(), Snackbar.LENGTH_LONG)
//                                            .setAction("No action", null).show();

                                    Intent i = new Intent(getApplicationContext(), TransactionEditActivity.class);
                                    i.putExtra("transaction", dataModel);
                                    startActivity(i);

                                }
                            });
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        //adapter= new TransactionAdapter(dataModels,getApplicationContext());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_transaction) {
            //do nothing
        } else if (id == R.id.nav_wallets) {
            Intent intent = new Intent(this, WalletsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bills) {
            Intent intent = new Intent(this, BillsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_account_setting) {
            Intent intent = new Intent(this, AccountAndSettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TransactionsActivity.PlaceholderFragment newInstance(int sectionNumber) {
            TransactionsActivity.PlaceholderFragment fragment = new TransactionsActivity.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return TransactionsActivity.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
