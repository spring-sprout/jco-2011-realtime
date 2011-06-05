package org.springsprout.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.ChunkedOutputStream;

public class Chat extends HttpServlet {

    private static final long serialVersionUID = 1918380010081387861L;
    
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        ChunkedOutputStream out = new ChunkedOutputStream(res.getOutputStream());
        try {
            res.setContentType("application/json; charset=utf-8;");
            res.setHeader("Connection", "close");
            for(int i = 0 ; i < 10 ; i++){
                res.getOutputStream().print("{data:"+i+"}");
                out.flush();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(out != null){
                try{
                    out.close();
                }catch (Exception e) {
                }
            }
        }
    }

}