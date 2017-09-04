package com.raghu.testfire.app;

import android.support.multidex.MultiDexApplication;

public class MyApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

/*@Override
  protected void attachBaseContext(Context base) {
     super.attachBaseContext(base);
     MultiDex.install(this);
  }*/
