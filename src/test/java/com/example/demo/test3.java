package com.example.demo;

/**
 * ClassName: test3
 * Description: TODD
 * Author: James Zow
 * Date: 2020/8/31 0031 22:37
 * Version:
 **/
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * @author: YeJunwei  Date: 2017/3/16 Time: 17:32
 */
public class test3 {
    String ip = "47.114.1.215";
    String userName = "root";
    String pwd = "zw198311...";
    String path = "/home/bangsun";

    public void testSSH2() throws IOException {
        List<String> result = new ArrayList<>();
        Connection connection = new Connection(ip);
        connection.connect();//连接
        connection.authenticateWithPassword(userName, pwd);//认证
        Session session = connection.openSession();
        session.execCommand("service vsftpd status ");
        InputStream is = new StreamGobbler(session.getStdout());//获得标准输出流
        BufferedReader brs = new BufferedReader(new InputStreamReader(is));
        for (String line = brs.readLine(); line != null; line = brs.readLine()) {
            result.add(line);
        }
        System.out.println(result);
        if (session != null) {
            session.close();
        }
        session.close();
    }


}