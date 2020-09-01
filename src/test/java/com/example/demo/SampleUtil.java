package com.example.demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.mo.ServiceInstance;

public class SampleUtil {

	public static ServiceInstance createServiceInstance() throws RemoteException, MalformedURLException {
	   ServiceInstance si = new ServiceInstance(new URL("https://192.168.0.103/cent7"), "Administrator", "zw198311", true);
	   si.getSessionManager().setLocale("zh-CN"); // set locale for the content of all API result.
	   return si;
	}

}
