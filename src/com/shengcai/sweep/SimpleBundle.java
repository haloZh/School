package com.shengcai.sweep;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class SimpleBundle implements BundleActivator
{
    private BundleContext mcontext = null;
    public void start(BundleContext context) throws Exception
    {
        System.out.println("你好我是插件,我将为你展示启动acitivty我已经启动了 我的BundleId为："+context.getBundle().getBundleId());
    }
   
    public void stop(BundleContext context)
    {
    	System.err.println("你好我是插件,我被停止了 我的BundleId为："+context.getBundle().getBundleId());
      
    }
	
}
