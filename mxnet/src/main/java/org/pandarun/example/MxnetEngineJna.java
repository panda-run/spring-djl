/*
 * Copyright 2021 Apache All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.pandarun.example;

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
