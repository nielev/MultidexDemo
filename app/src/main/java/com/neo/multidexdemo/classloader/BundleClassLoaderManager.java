package com.neo.multidexdemo.classloader;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;

import dalvik.system.DexClassLoader;

/**
 * @author nielev
 * 
 * @Description
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class BundleClassLoaderManager {

	public static List<BundleDexClassLoader> bundleDexClassLoaderList = new ArrayList<BundleDexClassLoader>();

	/**
	 * 加载Assets里的apk文件
	 * @param context
	 */
	public static void install(Context context) {
		AssetsManager.copyAllAssetsApk(context);
		// 获取dex文件列表
		File dexDir = context.getDir(AssetsManager.APK_DIR,
				Context.MODE_PRIVATE);
		File[] szFiles = dexDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(AssetsManager.FILE_FILTER);
			}
		});
		for (File f : szFiles) {
			System.out.println("debug:load file:" + f.getName());
			BundleDexClassLoader bundleDexClassLoader = new BundleDexClassLoader(
					f.getAbsolutePath(), dexDir.getAbsolutePath(), null,
					context.getClassLoader());
			//替换PathClassLoader为DexClassLoader
			loadApkClassLoader(bundleDexClassLoader,context);
			bundleDexClassLoaderList.add(bundleDexClassLoader);
		}
	}
	
	/**
	 * 查找类
	 * 
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(Context context,String className) throws ClassNotFoundException {
		try {
			Class<?> clazz = context.getClassLoader().loadClass(className);
			if (clazz != null) {
				System.out.println("debug: class find in main classLoader");
				return clazz;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (BundleDexClassLoader bundleDexClassLoader : bundleDexClassLoaderList) {
			try {
				Class<?> clazz = bundleDexClassLoader.loadClass(className);
				if (clazz != null) {
					System.out.println("debug: class find in bundle classLoader");
					return clazz;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		throw new ClassCastException(className + " not found exception");
	}
	@SuppressLint("NewApi")
	private static void loadApkClassLoader(DexClassLoader dLoader,Context context){
		try{

	        String filesDir = context.getCacheDir().getAbsolutePath();
	        String libPath = filesDir + File.separator +"GoogleAd.apk";
	        
	        // 配置动态加载环境
			Object currentActivityThread = RefInvoke.invokeStaticMethod(
					"android.app.ActivityThread", "currentActivityThread",
					new Class[] {}, new Object[] {});//获取主线程对象
			String packageName = context.getPackageName();//当前apk的包名
			ArrayMap mPackages = (ArrayMap) RefInvoke.getFieldOjbect(
					"android.app.ActivityThread", currentActivityThread,
					"mPackages");
			WeakReference wr = (WeakReference) mPackages.get(packageName);
			RefInvoke.setFieldOjbect("android.app.LoadedApk", "mClassLoader",
					wr.get(), dLoader);
			
			Log.i("demo", "classloader:"+dLoader);

		}catch(Exception e){
			Log.i("demo", "load apk classloader error:"+Log.getStackTraceString(e));
		}
		
	}
}
