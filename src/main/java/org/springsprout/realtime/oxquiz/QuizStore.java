package org.springsprout.realtime.oxquiz;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class QuizStore {
    
    private static Iterator<String> quizs;
    
    static {
        Set<String> quizSet = new HashSet<String>();
        //quizSet.add("Firefox 4는 기본적으로 Web Socket을 지원한다.");
        quizSet.add("자바에서 어느 서블릿 컨테이너가 Comet을 지원하는지 알고 있다.");
        quizSet.add("웹소켓을 사용해 본 적이 있다.");
        quizSet.add("각 Comet 기술들의 동작방식을 설명할 수 있다.");
        quizSet.add("Comet을 사용하는 서비스를 개발한적이 있다.");
        //quizSet.add("Comet이라는 용어는 Alex Russell이 만들었다.");
        
        quizs = quizSet.iterator();
    }
    
    public static String next() {
        if(quizs.hasNext())
            return quizs.next();
        else
            return "";
    }

}
