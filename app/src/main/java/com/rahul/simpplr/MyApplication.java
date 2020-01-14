package com.rahul.simpplr;



import com.rahul.simpplr.di.component.ApplicationComponent;
import com.rahul.simpplr.di.component.DaggerApplicationComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class MyApplication extends DaggerApplication {


    /*public static ApplicationComponent getAppComponent() {
        return appComponent;
    }*/


    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        ApplicationComponent component = DaggerApplicationComponent.builder().application(this).build();
        component.inject(this);

        return component;
    }


}
