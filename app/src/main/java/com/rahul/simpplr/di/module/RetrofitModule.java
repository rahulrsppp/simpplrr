package com.rahul.simpplr.di.module;

import com.rahul.simpplr.db.AppPreferencesHelper;
import com.rahul.simpplr.db.PreferencesHelper;
import com.rahul.simpplr.di.APIInterface;
import com.rahul.simpplr.di.utility.GsonScalarConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module(includes = ViewModelModule.class)
public class RetrofitModule {

    private static final int TIMEOUT = 40;
    private static final String BASE_URL = "https://api.spotify.com/v1/";

    @Inject
    PreferencesHelper preferencesHelper;


    @Provides
    APIInterface getApiInterface(Retrofit retrofit){
        return  retrofit.create(APIInterface.class);
    }

    @Provides
     OkHttpClient  getOkHttpClient(){
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request originalRequest = chain.request();

            Request.Builder newRequest = originalRequest.newBuilder();
            newRequest.build();

            Request request = newRequest.build();
            return chain.proceed(request);
        }).connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();

    }

    @Singleton
    @Provides
    Retrofit  getRetrofitInstance(OkHttpClient okHttpClient){
        return new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new GsonScalarConverterFactory()).build();
    }
}
