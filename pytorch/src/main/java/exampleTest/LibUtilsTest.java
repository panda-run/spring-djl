package exampleTest;

import ai.djl.engine.Engine;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LibUtilsTest {

    public static void main(String [] args){
        // pytorch 下载的lib路径
        String libPath = "";
        load(libPath);
    }

    @BeforeClass
    public void setup() {
        System.setProperty(
                "ai.djl.pytorch.native_helper", "ai.djl.pytorch.integration.LibUtilsTest");
    }

    @AfterClass
    public void teardown() {
        System.setProperty("ai.djl.pytorch.native_helper", "");
    }

    @Test
    public void test() {
        Engine.getInstance();
    }

    public static void load(String path) {
        System.load(path); // NOPMD
    }
}
