package com.rahul.simpplr.di.module;

import com.rahul.simpplr.ui.album.AlbumFragmentProvider;
import com.rahul.simpplr.ui.login.LoginActivity;
import com.rahul.simpplr.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = AlbumFragmentProvider.class)
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();
}
