/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rappsilber.utils.ms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author lfischer
 */
public class Composition {
    String formular;
    Double monomass;
    static Pattern parts = Pattern.compile("(?<symbol>[A-Z][a-z]*)(?:\\((?<isoid>[0-9]+)\\))?(?<count>[+-]?[0-9]*)");
    
    public static double formula2mass(String formula) {
        Matcher m = parts.matcher(formula);
        Double mass = 0d;
        while (m.find()) {
            String iso = m.group("isoid");
            Atom a = Atom.SymbolToAtom.get(m.group("symbol"));
            String sc = m.group("count");
            int count = 1;
            if (sc.length() > 0) {
                count = Integer.parseInt(sc);
            }
            if (iso != null && !iso.trim().isEmpty()) {
                Integer isoID = new Integer(iso);
                mass += a.isotopeToMass.get(isoID)*count;
            } else {
                mass += a.mono_mass*count;
            }
        }
        return mass;
    }

    
    public static void main(String[] args) {
        String formula[] = {"H2 H(2)5C9C(13)1S-1Br"};
        if (args.length > 0) {
            formula=args;
        }
        for (String f : formula)
            System.out.println(f + " : " +Composition.formula2mass(f));
    }
}
