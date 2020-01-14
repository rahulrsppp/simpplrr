package com.rahul.simpplr.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


@SuppressLint("Registered")
public  abstract class BaseActivity<V extends ViewDataBinding, T extends ViewModel> extends DaggerAppCompatActivity {

    private V viewBinding;
    private T viewModel;

    @Inject
    Context context;

    protected abstract int setLayoutId();
    protected abstract T setViewModel();

    public V getViewBinding() {
        return viewBinding;
    }

    public T getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataBinding();
    }

    private void setDataBinding() {
        viewBinding = DataBindingUtil.setContentView(this, setLayoutId());
        viewModel = (viewModel == null? setViewModel() : viewModel);
        viewBinding.executePendingBindings();
    }

    protected void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}