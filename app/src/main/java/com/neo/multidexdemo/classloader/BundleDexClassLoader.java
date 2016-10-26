package com.neo.multidexdemo.classloader;

import dalvik.system.DexClassLoader;

/**
 * @author Nielev
 *
 * @Description bundle的类加载器
 */
public class BundleDexClassLoader extends DexClassLoader {

	public BundleDexClassLoader(String dexPath, String optimizedDirectory,
			String libraryPath, ClassLoader parent) {
		super(dexPath, optimizedDirectory, libraryPath, parent);
	}

}
