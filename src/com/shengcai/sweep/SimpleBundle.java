package com.shengcai.sweep;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class SimpleBundle implements BundleActivator
{
    private BundleContext mcontext = null;
    public void start(BundleContext context) throws Exception
    {
        System.out.println("������ǲ��,�ҽ�Ϊ��չʾ����acitivty���Ѿ������� �ҵ�BundleIdΪ��"+context.getBundle().getBundleId());
    }
   
    public void stop(BundleContext context)
    {
    	System.err.println("������ǲ��,�ұ�ֹͣ�� �ҵ�BundleIdΪ��"+context.getBundle().getBundleId());
      
    }
	
}
