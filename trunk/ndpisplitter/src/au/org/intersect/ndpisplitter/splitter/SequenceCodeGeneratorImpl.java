/**
 * Copyright (C) Intersect 2009.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

/**
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Rev$
 */
public class SequenceCodeGeneratorImpl implements SequenceCodeGenerator
{
    private static Map<Integer, String> sequenceCodeMap = new HashMap<Integer, String>();

    static
    {
        initialiseSequenceCodes();
    }

    public String getSequenceCode(int sequence)
    {
        if (sequence <= 0)
        {
            throw new IllegalArgumentException("Sequence number must be greater than zero, was " + sequence);
        }
        String code = "";
        int divided = sequence;
        int loopNumber = 1;
        while (divided > 0)
        {
            int remainder = divided % 26;
            if (remainder == 0)
            {
                remainder = 26;
                divided = divided - 1;
            }
            divided = divided / 26;
            code = sequenceCodeMap.get(remainder) + code;
            loopNumber++;
        }
        return code;
    }

    private static void initialiseSequenceCodes()
    {
        sequenceCodeMap.put(1, "a");
        sequenceCodeMap.put(2, "b");
        sequenceCodeMap.put(3, "c");
        sequenceCodeMap.put(4, "d");
        sequenceCodeMap.put(5, "e");
        sequenceCodeMap.put(6, "f");
        sequenceCodeMap.put(7, "g");
        sequenceCodeMap.put(8, "h");
        sequenceCodeMap.put(9, "i");
        sequenceCodeMap.put(10, "j");
        sequenceCodeMap.put(11, "k");
        sequenceCodeMap.put(12, "l");
        sequenceCodeMap.put(13, "m");
        sequenceCodeMap.put(14, "n");
        sequenceCodeMap.put(15, "o");
        sequenceCodeMap.put(16, "p");
        sequenceCodeMap.put(17, "q");
        sequenceCodeMap.put(18, "r");
        sequenceCodeMap.put(19, "s");
        sequenceCodeMap.put(20, "t");
        sequenceCodeMap.put(21, "u");
        sequenceCodeMap.put(22, "v");
        sequenceCodeMap.put(23, "w");
        sequenceCodeMap.put(24, "x");
        sequenceCodeMap.put(25, "y");
        sequenceCodeMap.put(26, "z");
    }
}
