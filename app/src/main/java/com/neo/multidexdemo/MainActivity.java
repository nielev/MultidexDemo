package com.neo.multidexdemo;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.neo.multidexdemo.classloader.AssetsMultiDexLoader;
import com.neo.multidexdemo.classloader.BundleClassLoaderManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Nielev
 *
 */
public class MainActivity extends AppCompatActivity {

    private ViewGroup mLl_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //DexClassLoader
            BundleClassLoaderManager.install(getApplicationContext());
        }else {
            //PathClassLoader
            AssetsMultiDexLoader.install(getApplicationContext());
        }

        mLl_ad = (ViewGroup) findViewById(R.id.ll_ad);

        showAd();

    }

    /**
     * 以反射的方式调用资源里apk的方法
     */
    private void showAd(){
        try {
            //PathClassLoader
//			Class<?> clazz = Class.forName("");
            //DexClassLoader
            Class<?> clazz = BundleClassLoaderManager.loadClass(getApplicationContext(), "com.oray.googlead.GoogleAdUtils");
            Method method = clazz.getMethod("showAD", ViewGroup.class, Context.class,String.class);
            method.invoke(null, mLl_ad, getApplicationContext(),getString(R.string.banner_ad_unit_id));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
