package com.example.bookmart;


import android.app.Activity;

import com.example.manager.CoinManager;
import com.example.manager.FirebaseManager;
import com.example.manager.UserManager;

import java.util.HashMap;
import java.util.Map;

public class AppController {

    //region Singleton
    private static AppController instance;
    public static synchronized AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }
    //endregion Singleton

    //region Attributes
    public static String userId;
    private Activity currentActivity;
    //endregion Attributes

    private AppController() {
        addManager(FirebaseManager.class, new FirebaseManager());
        addManager(CoinManager.class, new CoinManager());
        addManager(UserManager.class, new UserManager());
    }

    private Map<Class<?>, Manager> managerMap = new HashMap<>();

    private void addManager(Class<?> managerClass, Manager manager) {
        managerMap.put(managerClass, manager);
    }
    @SuppressWarnings("unchecked")
    public <T extends Manager> T getManager(Class<T> managerClass) {
        return (T) managerMap.get(managerClass);
    }



    //region Getter/Setter
    public Activity getCurrentActivity() {
        return currentActivity;
    }
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }
    //endregion Getter/Setter
}

