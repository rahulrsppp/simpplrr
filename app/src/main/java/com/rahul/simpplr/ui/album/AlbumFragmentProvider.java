package com.rahul.simpplr.ui.album;

import com.rahul.simpplr.ui.albumtracks.AlbumTrackFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class AlbumFragmentProvider {

    @ContributesAndroidInjector
    abstract AlbumFragment provideAlbumFragmentFactory();

    @ContributesAndroidInjector
    abstract AlbumTrackFragment provideAlbumTrackFragmentFactory();
}
