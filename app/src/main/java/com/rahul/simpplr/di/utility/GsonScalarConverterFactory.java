package com.rahul.simpplr.di.utility;

import android.util.Xml;

import com.google.gson.GsonBuilder;
import com.rahul.simpplr.di.Gson;
import com.rahul.simpplr.di.Scalar;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GsonScalarConverterFactory extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Scalar.class) {
                return ScalarsConverterFactory.create().responseBodyConverter(type, annotations, retrofit);
            }
            if (annotation.annotationType() == Gson.class) {
                return GsonConverterFactory.create(new com.google.gson.Gson()).responseBodyConverter(type, annotations, retrofit);
            }
        }
        return GsonConverterFactory.create(new com.google.gson.Gson()).responseBodyConverter(type, annotations, retrofit);
    }
}