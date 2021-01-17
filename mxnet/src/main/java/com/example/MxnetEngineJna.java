package com.example;

import ai.djl.engine.Engine;
import ai.djl.mxnet.engine.MxEngine;
import ai.djl.mxnet.jna.JnaUtils;
import ai.djl.mxnet.jna.LibUtils;
import ai.djl.ndarray.NDManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: MxnetEngineJna
 * Description: JNA test
 * Author: James Zow
 * Date: 2020/11/11 20:48
 * Version:
 **/
public class MxnetEngineJna {

    public Logger log = LoggerFactory.getLogger(MxnetEngineJna.class);

    public static void main(String [] args){
        runExample();
    }

    public static void runExample(){
        int a  = JnaUtils.getVersion();
        String mxengine_v = MxEngine.getInstance().getVersion();
        String mxengine_name = MxEngine.getInstance().getEngineName();
        int b  = JnaUtils.getGpuCount();
  //      long [] c =  JnaUtils.getGpuMemory(Device.defaultDevice());
        String libName = LibUtils.getLibName();

        Engine engine =  MxEngine.getEngine(mxengine_name);

        System.out.println(a);
        System.out.println(b);
  //      System.out.println(c);
        System.out.println(mxengine_name);
        System.out.println(mxengine_v);

        try {
            NDManager manager = NDManager.newBaseManager();
       //     MxNDManager mxNDManager = new MxNDManager(Device.defaultDevice(),MxEngine.getInstance().getVersion()).newSubManager();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
