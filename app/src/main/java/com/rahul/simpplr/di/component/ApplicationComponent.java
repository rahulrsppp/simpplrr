package com.rahul.simpplr.di.component;

import android.app.Application;

import com.rahul.simpplr.MyApplication;
import com.rahul.simpplr.di.module.ActivityBindingModule;
import com.rahul.simpplr.di.module.AppContextModule;
import com.rahul.simpplr.di.module.AppModule;
import com.rahul.simpplr.di.module.RetrofitModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AppContextModule.class, RetrofitModule.class, AppModule.class, AndroidSupportInjectionModule.class, ActivityBindingModule.class})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

   // MainActivityComponent getMainActivityComponent(ActivityContextModule activityContextModule);


    void inject(MyApplication application);



    @Component.Builder
    interface Builder{

       @BindsInstance
        Builder application(Application application);

        ApplicationComponent  build();
    }
}

