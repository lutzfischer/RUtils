/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.utils.statistic;


/**
 *
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class TestEnum {
    static int FIELDS_count = 0;
    private enum FIELDS {
        FIELD_2("Field 1", 1, 8 ),
        FIELD_3("Field 2", 2, 7) ,
        FIELD_4("Field 3", 3, 9);

        private final String description;
        private final int number;
        private final int size;
        private final String exampleValue;

        FIELDS(String description, int number, int size) {
          this.description = description;
          this.number = number;
          this.size = size;
          this.exampleValue = "A" + (FIELDS_count++) + "X";
        }

        public String getDescription() {
          return description;
        }

        public int getNumber() {
          return number;
        }

        public int getSize() {
          return size;
        }

        public String getExampleValue() {
          return exampleValue;
        }
    }
    
    public static void main(String[] args) {
        FIELDS e = FIELDS.FIELD_2;
        System.out.println(e.exampleValue);
        e = FIELDS.FIELD_4;
        System.out.println(e.exampleValue);
        Runtime runtime = Runtime.getRuntime();
        String s1;
        String[] s2 = new String[10000];
        String[] s3 = new String[10000];
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        long fm1 = runtime.freeMemory();
        long mm1 = runtime.maxMemory();
        long tm1 = runtime.totalMemory();

        s1="ABC";
        System.gc();
        System.gc();
        long fm2 = runtime.freeMemory();
        long mm2 = runtime.maxMemory();
        long tm2 = runtime.totalMemory();

        

        System.gc();
        System.gc();
        System.gc();
        System.gc();
        
        
        for (int i = 0; i< 10000;i++) {
            s2[i] = new String("DEF" + i);
        }

        System.gc();
        System.gc();
        System.gc();
        System.gc();
        long fm3 = runtime.freeMemory();
        long mm3 = runtime.maxMemory();
        long tm3 = runtime.totalMemory();
        
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        for (int i = 0; i< 10000;i++) {
            s3[i] = s1 + s2[i];
        }
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        String s4= s1+s2;
        long fm4 = runtime.freeMemory();
        long mm4 = runtime.maxMemory();
        long tm4 = runtime.totalMemory();
        
        System.out.println(fm1 + ", " + tm1+ ", " + mm1 + ", " + (tm1-fm1));
        System.out.println(fm2 + ", " + tm2+ ", " + mm2 + ", " + (tm2-fm2));
        System.out.println(fm3 + ", " + tm3+ ", " + mm3 + ", " + (tm3-fm3));
        System.out.println(fm4 + ", " + tm4+ ", " + mm4 + ", " + (tm4-fm4));
        
    }

}
