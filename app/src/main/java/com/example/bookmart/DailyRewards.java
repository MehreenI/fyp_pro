//package com.example.bookmart;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.media.Image;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.example.manager.CoinManager;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//import java.util.concurrent.TimeUnit;
//
//public class DailyRewards extends AppCompatActivity {
//
//    private ActivityDailyRewardsBinding actBinding;
//    private final String TAG = "DailyRewards";
//
//    private static final String PREF_NAME = "DailyRewardPrefs";
//    private static final String KEY_LAST_LOGIN_DATE = "lastLoginDate";
//    private static final String KEY_CONSECUTIVE_LOGINS = "consecutiveLogins";
//
//    private static final int MAX_CONSECUTIVE_DAYS = 7;
//    private ImageView[] rewardImages = new ImageView[MAX_CONSECUTIVE_DAYS];
//
//
//    private SharedPreferences sharedPreferences;
//    private boolean rewardClaimed;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        actBinding = ActivityDailyRewardsBinding.inflate(getLayoutInflater());
//        setContentView(actBinding.getRoot());
//
//        sharedPreferences = getSharedPreferences("com.example.oldbooks", Context.MODE_PRIVATE);
//
//        rewardClaimed = sharedPreferences.getBoolean("rewardClaimed", false);
//
//        rewardImages[0] = actBinding.imgreward1;
//        rewardImages[1] = actBinding.imgreward2;
//        rewardImages[2] = actBinding.imgreward3;
//        rewardImages[3] = actBinding.imgreward4;
//        rewardImages[4] = actBinding.imgreward5;
//        rewardImages[5] = actBinding.imgreward6;
//        rewardImages[6] = actBinding.imgreward7;
//
//        Log.d(TAG, "saved rewardClaimed: " + rewardClaimed);
//        initialize();
//    }
//
//    private void resetConsecutiveLogins() {
//        sharedPreferences.edit().putInt(KEY_CONSECUTIVE_LOGINS, 0).apply();
//        RedrawRewards(sharedPreferences.getInt(KEY_CONSECUTIVE_LOGINS, 0));
//    }
//
//    public void initialize(){
//        Date currentDate = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        String todayDate = sdf.format(currentDate);
//
//        String lastLoginDate = sharedPreferences.getString(KEY_LAST_LOGIN_DATE, "");
//        if(lastLoginDate.equals(""))
//        {
//            lastLoginDate = todayDate;
//        }
//        Log.d(TAG, "saved rewardClaimed: " + rewardClaimed);
//        if (!todayDate.equals(lastLoginDate) || !rewardClaimed) {
//            Log.d(TAG, "saved rewardClaimed: " + 1);
//
//            sharedPreferences.edit().putBoolean("rewardClaimed", false).apply();
//            int consecutiveLogins = sharedPreferences.getInt(KEY_CONSECUTIVE_LOGINS, 0);
//            try {
//                Log.d(TAG, "saved rewardClaimed: " + 2);
//
//                Date lastLogin = sdf.parse(lastLoginDate);
//                long dayDifference = TimeUnit.DAYS.convert(currentDate.getTime() - lastLogin.getTime(), TimeUnit.MILLISECONDS);
//                Log.d(TAG, "saved rewardClaimed: dayDifference " + dayDifference);
//
//                if (dayDifference > 1) {
//                    Log.d(TAG, "saved rewardClaimed: " + 4);
//                    resetConsecutiveLogins();
//                }else{
//                    if (consecutiveLogins == MAX_CONSECUTIVE_DAYS) {
//                        Log.d(TAG, "saved rewardClaimed: " + 5);
//                        resetConsecutiveLogins();
//                    } else {
//                        Log.d(TAG, "saved rewardClaimed: " + 6);
//                        RedrawRewards(consecutiveLogins);
//                    }
//                    Log.d(TAG, "saved rewardClaimed: " + 7);
//                    sharedPreferences.edit().putString(KEY_LAST_LOGIN_DATE, todayDate).apply();
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }else {
//            startActivity(new Intent(this,MainActivity.class));
//        }
//    }
//
//    public void RedrawRewards(int day){
//        for (int i = 0; i < MAX_CONSECUTIVE_DAYS; i++) {
//            rewardImages[i].setImageResource(R.drawable.giftpng);
//        }
//        if(!rewardClaimed)
//        {
//            rewardImages[day].setImageResource(R.drawable.gift_get);
//            actBinding.btnclaimreward.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ClaimReward(day);
//                }
//            });
//        }else{
//            startActivity(new Intent(this,MainActivity.class));
//        }
//    }
//
//    public void ClaimReward(int day){
//        Log.d(TAG, "ClaimReward: " + day);
//        switch (day)
//        {
//            case 1:
//                AppController.getInstance().getManager(CoinManager.class).addCoins(4);
//                break;
//            case 2:
//                AppController.getInstance().getManager(CoinManager.class).addCoins(5);
//                break;
//            case 3:
//                AppController.getInstance().getManager(CoinManager.class).addCoins(6);
//                break;
//            case 4:
//                AppController.getInstance().getManager(CoinManager.class).addCoins(7);
//                break;
//            case 5:
//                AppController.getInstance().getManager(CoinManager.class).addCoins(8);
//                break;
//            case 6:
//                AppController.getInstance().getManager(CoinManager.class).addCoins(9);
//                break;
//            case 7:
//                AppController.getInstance().getManager(CoinManager.class).addCoins(10);
//                break;
//            default:
//                break;
//        }
//
//        sharedPreferences.edit().putBoolean("rewardClaimed", true).apply();
//        sharedPreferences.edit().putInt(KEY_CONSECUTIVE_LOGINS, day).apply();
//        Toast.makeText(this, "Reward Claimed", Toast.LENGTH_LONG);
//        startActivity(new Intent(this,MainActivity.class));
//
//    }
//}