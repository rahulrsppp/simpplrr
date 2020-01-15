package com.rahul.simpplr.di.module;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.rahul.simpplr.di.utility.ViewModelKey;
import com.rahul.simpplr.ui.album.AlbumViewModel;
import com.rahul.simpplr.ui.login.LoginViewModel;
import com.rahul.simpplr.utility.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    /*@Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel getMainViewModel(MainViewModel mainViewModel);*/

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel getLoginViewModel(LoginViewModel loginViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(AlbumViewModel.class)
    abstract ViewModel getAlbumViewModel(AlbumViewModel albumViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);


}
