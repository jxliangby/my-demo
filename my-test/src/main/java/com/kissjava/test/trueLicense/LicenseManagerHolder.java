package com.kissjava.test.trueLicense;

import java.io.File;

import com.kissjava.license.keymgr.LicenseManager;

import net.java.truelicense.core.License;
import net.java.truelicense.core.LicenseConsumerContext;
import net.java.truelicense.core.LicenseConsumerManager;
import net.java.truelicense.core.LicenseManagementException;
import net.java.truelicense.core.io.Source;

public class LicenseManagerHolder {
	
	   
	public void install(){
		  LicenseConsumerManager manager = LicenseManager.get();
	        LicenseConsumerContext context = manager.context();
	        String path = LicenseManagerHolder.class.getResource("/").getPath();
	        Source source = context.fileStore(new File(path+File.separator+"kissjava.lic"));
	        try {
	            manager.install(source);
	            License view = manager.view();
	            String extra = view.getExtra().toString();
	            System.out.println("========================");
	            System.out.println(extra);
	            System.out.println(view.getNotAfter());
	 
	            manager.verify();
	            System.out.println("=============SUCCESS===========");
	        } catch (LicenseManagementException e) {
	            System.out.println("=============FAILTRUE===========");
	            e.printStackTrace();
	        }

	}
	
	
	public static void main(String[] args){
		LicenseManagerHolder lh = new LicenseManagerHolder();
		lh.install();
	}
}
