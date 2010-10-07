package net.sf.umlspeed.util;

public abstract class Util {
    
    /** Looks in findin for all occurrences of find and replaces them with replacewith 
     * @param findin The string to find occurrences in
     * @param find The string to find
     * @param replacewith The string to replace found occurrences with
     * @return A string with all occurrences of find replaced.
     */
    public static String replace(String findin, String find, String replacewith) {
        
        StringBuffer sb = new StringBuffer(findin);
        int i = 0;
        try {
            while (i <= sb.length() - find.length()) {
                if (sb.substring(i, i + find.length()).equalsIgnoreCase(find)) {
                    sb.replace(i, i + find.length(), replacewith);
                }
                i++;
            }
        }
        catch (StringIndexOutOfBoundsException e) {
            // We hit the end of the string - do nothing and carry on
        }
        return sb.toString();
    }
    
    /**
     * Splits a string by a particular char and returns an array of 
     * strings. If there are no occurrences of the split char, the
     * original string is returned in an array of 1 item.
     * @param splitstring The string to be split
     * @param splitchar The char to split on
     * @return An array of strings
     */
    public static String[] split(String splitstring, String splitchar) {
       
        splitstring = splitstring.trim();
        
        // If there is only one element, just return that
        if (splitstring.indexOf(splitchar) == -1) {
            String[] rets = new String[1];
            rets[0] = splitstring;
            return rets;
        }
        
        // Find how many there are
        int tot = 0;
        int lpos = splitstring.indexOf(splitchar);
        while (lpos != -1) {
            tot++;
            lpos = splitstring.indexOf(splitchar, lpos + 1);
        }
        tot++;
        
        // Create our new array
        String[] rets = new String[tot];
        tot = 0;
        lpos = 0;
        int spos = splitstring.indexOf(splitchar);
        while (spos != -1) {
            // Add into the array
            rets[tot] = splitstring.substring(lpos, spos);
            tot++;
            lpos = spos + 1;
            spos = splitstring.indexOf(splitchar, lpos);
        }
        
        // Include last word
        rets[tot] = splitstring.substring(lpos, splitstring.length());
        
        // Return it
        return rets;
    }
}
