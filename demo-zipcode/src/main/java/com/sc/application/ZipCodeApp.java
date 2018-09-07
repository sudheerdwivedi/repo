package com.sc.application;

import com.sc.util.ZipCodeUtils;
import com.sc.model.ZipCodeRange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple command-line application to test the ZIP code classes.
 */
public class ZipCodeApp {

    /**
     * Default runnable method.
     * @param args Optional ZIP code values to be checked to see if they have been excluded {@code (see: excludeZipCode.txt)}
     */
    public static void main(String[] args) {
        List<ZipCodeRange> inputRanges = new ArrayList<>();
        /*
            - ranges would typically be read from database
            - for this demo, we allow ranges to be ready from a text file
        */

        try (BufferedReader br = new BufferedReader(new InputStreamReader(ZipCodeApp.class.getResourceAsStream("/excludeZipCode.txt")))) {
            for (String line; (line = br.readLine()) != null;) {
                inputRanges.add(new ZipCodeRange(line));
            }
        }
        catch (IOException e) {
            // this would normally be written to a log file instead of stderr
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            // this would never be used in production code and is here only for this demo app
            System.exit(1);
        }
        System.out.println("\nRaw input ranges:\n" + inputRanges);

        List<ZipCodeRange> excludes = ZipCodeUtils.consolidate(inputRanges);
        System.out.println("\nConsolidated exclusion ranges:\n" + excludes);

        // if any command-line arguments, assume they are ZIP codes to test for exclusion, comparing against the
        // ranges read in from excludeZipCode.txt
        if (args.length > 0) {
            System.out.println();
            for (String arg : args) {
                System.out.println("ZIP code '" + arg + "' excluded: " + ZipCodeUtils.isExcluded(arg, excludes));
            }
        }
    }

}
