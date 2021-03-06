package com.piedpipergeeks.aviate;

import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreenUserActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener, ChatsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener
{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView mTextMessage;
    private SettingsHeader settingsHeader;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_user_calendar:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_user_area, new CalendarFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_user_clubs:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_user_area, new ChatsFragment());
                    fragmentTransaction.commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Abstract method ,has to be override
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_user);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_user_area, new ChatsFragment());
        fragmentTransaction.commit();
        setNavigationViewListner();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId())
        {
            case R.id.nav_sendfeedback:
                final Intent intent=new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "aviateapp@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback about service");
                startActivity(Intent.createChooser(intent,null));

                break;
            case R.id.nav_settings:
                Intent settings=new Intent(HomeScreenUserActivity.this,Settings.class);
                startActivity(settings);
                break;
            case R.id.nav_logout:
                new AlertDialog.Builder(this)
                        .setIcon(null)
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to log out ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //startActivity();
                                firebaseAuth.signOut();
                                Intent intent1=new Intent(HomeScreenUserActivity.this,MainActivity.class);
                                startActivity(intent1);
                                finish();

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                break;
        }
//        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setNavigationViewListner() {
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = (TextView) headerView.findViewById(R.id.usernamesettings_textview);

        SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String firstName = pref.getString("firstName", "");
        String lastName = pref.getString("lastName", "");

        navUserName.setText(firstName + " " + lastName);

        navigationView.setNavigationItemSelectedListener(this);
    }
    
//    public void onBackPressed() {
//        super.onBackPressed();
////        Toast.makeText(HomeScreenUserActivity.this,"HIE",Toast.LENGTH_SHORT).show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setMessage("Do you want to Exit?");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //if user pressed "yes", then he is allowed to exit from application
//                finish();
//            }
//        });
//        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //if user select "No", just cancel this dialog and continue with app
//                dialog.cancel();
//            }
//        });
//        AlertDialog alert=builder.create();
//        alert.show();
//    }
}
