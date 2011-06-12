package org.springsprout.realtime.oxquiz;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class QuizStore {
    
    private static Iterator<String> quizs;
    
    static {
        init();
    }
    
    public static String next() {
        if(quizs.hasNext())
            return quizs.next();
        else
            return "";
    }
    
    public static void init() {
        Set<String> quizSet = new HashSet<String>();
        quizSet.add("자바에서 어느 서블릿 컨테이너가 Comet을 지원하는지 알고 있다.");
        quizSet.add("웹소켓을 사용해 본적이 있다.");
        quizSet.add("Comet을 사용하는 서비스를 개발한적이 있다.");
        quizSet.add("각 Comet 기술들의 동작방식을 설명할 수 있다.");
        quizSet.add("Comet의 여러가지 기술들에 대한 기본적인 개념들을 알고 있다.");
        quizSet.add("리얼타입웹 기술들에 대해서 들어본 적이 있다.");
        
        quizs = quizSet.iterator();
    }

}
