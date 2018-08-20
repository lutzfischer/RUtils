/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rappsilber.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 *
 * @author Lutz Fischer <lfischer@staffmail.ed.ac.uk>
 */
public class BooleanConvert {
    static class YesNo{
        Pattern Yes;
        Pattern No;

        public YesNo(Pattern Yes, Pattern No) {
            this.Yes = Yes;
            this.No = No;
        }
        
    }
    
    static HashMap<String,YesNo> languagePairs = new HashMap<>();
    static HashMap<Locale,YesNo> localPairs = new HashMap<>();
    static YesNo defaultYesNo = new YesNo(Pattern.compile("TRUE|T|1(\\.0*)?|-1(\\.0*)?|Y|YES|\\+",Pattern.CASE_INSENSITIVE),Pattern.compile("FALSE|F|0(\\.0*)?|N|NO|\\-",Pattern.CASE_INSENSITIVE));
    
    static {
        languagePairs.put("en",defaultYesNo);
        languagePairs.put("af",new YesNo(Pattern.compile("ja|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nee|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ast",new YesNo(Pattern.compile("sí|s",Pattern.CASE_INSENSITIVE),Pattern.compile("non|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("be",new YesNo(Pattern.compile("так|tak|т",Pattern.CASE_INSENSITIVE),Pattern.compile("не|ne|н",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("bg",new YesNo(Pattern.compile("да|da|д",Pattern.CASE_INSENSITIVE),Pattern.compile("не|ne|н",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("bm",new YesNo(Pattern.compile("awɔ",Pattern.CASE_INSENSITIVE),Pattern.compile("ayi",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("br",new YesNo(Pattern.compile("ya|y",Pattern.CASE_INSENSITIVE),Pattern.compile("nann|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("bs",new YesNo(Pattern.compile("da|d",Pattern.CASE_INSENSITIVE),Pattern.compile("ne|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ca",new YesNo(Pattern.compile("sí|s",Pattern.CASE_INSENSITIVE),Pattern.compile("no|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ce",new YesNo(Pattern.compile("хiаъ|haə",Pattern.CASE_INSENSITIVE),Pattern.compile("хiан-хiа|han-ha",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("cs",new YesNo(Pattern.compile("ano|a",Pattern.CASE_INSENSITIVE),Pattern.compile("ne|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("cy",new YesNo(Pattern.compile("yes|y",Pattern.CASE_INSENSITIVE),Pattern.compile("no|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("da",new YesNo(Pattern.compile("ja|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nej|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("de",new YesNo(Pattern.compile("ja|j|r|richtig|1|\\+",Pattern.CASE_INSENSITIVE),Pattern.compile("nein|n|f|falsch|0|\\-",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ee",new YesNo(Pattern.compile("e|e",Pattern.CASE_INSENSITIVE),Pattern.compile("ao|a",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("eo",new YesNo(Pattern.compile("jes|j",Pattern.CASE_INSENSITIVE),Pattern.compile("ne|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("es",new YesNo(Pattern.compile("sí|s",Pattern.CASE_INSENSITIVE),Pattern.compile("no|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("et",new YesNo(Pattern.compile("jah|j",Pattern.CASE_INSENSITIVE),Pattern.compile("ei|e",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("eu",new YesNo(Pattern.compile("bai|b",Pattern.CASE_INSENSITIVE),Pattern.compile("ez|e",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("fi",new YesNo(Pattern.compile("kyllä|k",Pattern.CASE_INSENSITIVE),Pattern.compile("ei|e",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("fo",new YesNo(Pattern.compile("ja|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nei|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("fr",new YesNo(Pattern.compile("oui|o",Pattern.CASE_INSENSITIVE),Pattern.compile("non|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("fur",new YesNo(Pattern.compile("sì|s",Pattern.CASE_INSENSITIVE),Pattern.compile("no|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("gd",new YesNo(Pattern.compile("yes|y",Pattern.CASE_INSENSITIVE),Pattern.compile("no|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("gl",new YesNo(Pattern.compile("si|s",Pattern.CASE_INSENSITIVE),Pattern.compile("non|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("gsw",new YesNo(Pattern.compile("ja|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nein|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ha",new YesNo(Pattern.compile("eh|e",Pattern.CASE_INSENSITIVE),Pattern.compile("a'a|a",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("haw",new YesNo(Pattern.compile("ʻae",Pattern.CASE_INSENSITIVE),Pattern.compile("ʻaʻole",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("hr",new YesNo(Pattern.compile("da|d",Pattern.CASE_INSENSITIVE),Pattern.compile("ne|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("hu",new YesNo(Pattern.compile("igen|i",Pattern.CASE_INSENSITIVE),Pattern.compile("nem|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("is",new YesNo(Pattern.compile("já|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nei|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("it",new YesNo(Pattern.compile("sì|s",Pattern.CASE_INSENSITIVE),Pattern.compile("no|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("iw",new YesNo(Pattern.compile("|ken|כן",Pattern.CASE_INSENSITIVE),Pattern.compile("|loh|לא",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ja",new YesNo(Pattern.compile("はい|hai",Pattern.CASE_INSENSITIVE),Pattern.compile("いいえ|īe",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ka",new YesNo(Pattern.compile("დიახ|diakh|კი|k’i|ჰო|ho|ხო|kho|დ",Pattern.CASE_INSENSITIVE),Pattern.compile("არა|ara|ა",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("kw",new YesNo(Pattern.compile("ya|y",Pattern.CASE_INSENSITIVE),Pattern.compile("na|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ln",new YesNo(Pattern.compile("ee|e",Pattern.CASE_INSENSITIVE),Pattern.compile("te|t",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("lt",new YesNo(Pattern.compile("taip|jo|t",Pattern.CASE_INSENSITIVE),Pattern.compile("ne|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("lv",new YesNo(Pattern.compile("jā|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nē|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("mg",new YesNo(Pattern.compile("eny|e",Pattern.CASE_INSENSITIVE),Pattern.compile("tsia|t",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("mk",new YesNo(Pattern.compile("да|da|д",Pattern.CASE_INSENSITIVE),Pattern.compile("не|ne|н",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ms",new YesNo(Pattern.compile("ya|y",Pattern.CASE_INSENSITIVE),Pattern.compile("tidak|tak|t",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("mt",new YesNo(Pattern.compile("iva|i",Pattern.CASE_INSENSITIVE),Pattern.compile("le|l",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("nb",new YesNo(Pattern.compile("ja|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nei|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ne",new YesNo(Pattern.compile("हो|ho",Pattern.CASE_INSENSITIVE),Pattern.compile("होइन|hoena",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("nl",new YesNo(Pattern.compile("ja|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nee|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("nn",new YesNo(Pattern.compile("ja|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nei|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("no",new YesNo(Pattern.compile("ja|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nei|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("pl",new YesNo(Pattern.compile("tak|t",Pattern.CASE_INSENSITIVE),Pattern.compile("nie|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("pt",new YesNo(Pattern.compile("sim|s",Pattern.CASE_INSENSITIVE),Pattern.compile("não|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ro",new YesNo(Pattern.compile("da|d",Pattern.CASE_INSENSITIVE),Pattern.compile("nu|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ru",new YesNo(Pattern.compile("да|da|д",Pattern.CASE_INSENSITIVE),Pattern.compile("нет|net|н",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("sk",new YesNo(Pattern.compile("áno|á",Pattern.CASE_INSENSITIVE),Pattern.compile("nie|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("sl",new YesNo(Pattern.compile("da|d",Pattern.CASE_INSENSITIVE),Pattern.compile("ne|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("sr",new YesNo(Pattern.compile("да|da|д",Pattern.CASE_INSENSITIVE),Pattern.compile("не|ne|н",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("sv",new YesNo(Pattern.compile("ja|j",Pattern.CASE_INSENSITIVE),Pattern.compile("nej|n",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("tr",new YesNo(Pattern.compile("evet|e",Pattern.CASE_INSENSITIVE),Pattern.compile("hayır|h",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("ug",new YesNo(Pattern.compile("|hä’ä|ھەئە|shundaq|شۇنداق",Pattern.CASE_INSENSITIVE),Pattern.compile("|yaq|ياق|ämäs|ئەمەس",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("uk",new YesNo(Pattern.compile("так|tak|т",Pattern.CASE_INSENSITIVE),Pattern.compile("ні|ni|н",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("uz",new YesNo(Pattern.compile("ha|h",Pattern.CASE_INSENSITIVE),Pattern.compile("yo'q|y",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("yo",new YesNo(Pattern.compile("bẹẹ ni",Pattern.CASE_INSENSITIVE),Pattern.compile("bẹẹ kọ|ó ti|ra ra",Pattern.CASE_INSENSITIVE)));
        languagePairs.put("zu",new YesNo(Pattern.compile("yebo|y",Pattern.CASE_INSENSITIVE),Pattern.compile("cha|c",Pattern.CASE_INSENSITIVE)));
        
        for (Locale l : Locale.getAvailableLocales()) {
            YesNo yn  = languagePairs.get(l.getLanguage());
            if (yn == null)
                yn = defaultYesNo;
            localPairs.put(l,yn);
        }
    }

    private YesNo yn;
    private String yes;
    private String no;
    
    
    protected BooleanConvert() {
    }

    public BooleanConvert(Locale l) {
        this.yn=localPairs.get(l);
        if (yn == null)
            yn = defaultYesNo;
        
        yes = yn.Yes.toString();
        if (yes.contains("|"))
            yes = yes.substring(0,yes.indexOf("|"));
        no = yn.No.toString();
        if (no.contains("|"))
            no = no.substring(0,no.indexOf("|"));
    }

    
    public Boolean parseBoolean(String v) {
        if (yn.Yes.matcher(v).matches())
            return true;
        if (yn.No.matcher(v).matches())
            return false;
        if (defaultYesNo.Yes.matcher(v).matches())
            return true;
        if (defaultYesNo.No.matcher(v).matches())
            return false;
        return null;
    }

    public String toString(boolean v)  {
        if (v) 
            return yes;
        else
            return no;
    }
    
}
