package org.springsprout.realtime.oxquiz;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class QuizStore {
    
    private static Iterator<String> quizs;
    
    static {
        Set<String> quizSet = new HashSet<String>();
        quizSet.add("Comet이라는 용어가 알고있다.");
        quizSet.add("Comet을 사용하는 서비스를 개발한적이 있다.");
        quizSet.add("Ajax는 Comet에 속한다.");
        
        quizs = quizSet.iterator();
    }
    
    public static String next() {
        if(quizs.hasNext())
            return quizs.next();
        else
            return "";
    }

}
