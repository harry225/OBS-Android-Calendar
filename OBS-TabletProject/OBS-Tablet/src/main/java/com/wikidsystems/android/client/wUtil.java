/*--------------------------------------------------
| wUtil.java
|
| This class provides utility methods for
| the WiKIDAuth application.
|
| Version: 1.00
*-------------------------------------------------*/
package com.wikidsystems.android.client;

import java.util.Enumeration;
import java.util.Hashtable;
//import javax.microedition.lcdui.List;

public class wUtil {
    public static String printHash(Hashtable ht) {
        StringBuffer sb = new StringBuffer("");
        if (ht != null && !ht.isEmpty()) {
            sb.append("Contents of Hashtable:\n");
            Enumeration en = ht.keys();
            while (en.hasMoreElements()) {
                String k = (String) en.nextElement();
                sb.append("* " + k + " = ");
                sb.append(ht.get(k).toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static String[] splitIt(String s) {

        String[] temp1 = new String[32];

        int i = 0, a = 0, b = 0, c = 0;

        while ((i < s.length() - 1) && b < 32) {
            a = i;
            while (i < (s.length()) && s.charAt(i) != '&') {
                i++;
            }
            c = i;
            temp1[b] = s.substring(a, c);
            i++;
            b++;
        }
        String[] temp2 = new String[b];
        System.arraycopy(temp1, 0, temp2, 0, b);
        return temp2;
    }

    public static void printStringArray(String[] sa) {
        for (int i = 0; i <= sa.length - 1; i++) {
            System.out.println(i + " = " + sa[i]);
        }
    }

    public static Hashtable hashIt(String s) {
        Hashtable ht = new Hashtable();
        int i = 0, a = 0, b = 0, c = 0;
        if (s.length() > 0) {
            while (i < s.length()) {
                a = i;
                while (s.charAt(i) != '=' && i < s.length()) {
                    i++;
                }
                b = i;
                while (s.charAt(i) != '&' && i < (s.length() - 1)) {
                    i++;
                }
                if (i < (s.length() - 1)) {
                    c = i;
                } else {
                    c = i + 1;
                }
                ht.put(s.substring(a, b), s.substring(b + 1, c));
                i++;
            }
        }
        return ht;
    }

    protected static String[] getConfigOptions() {
        return new String[]{"New Domain", "Delete Domain"};
    }

//  protected static void updateList(wList l, String[] a)
//  {
//	if (DEBUG) System.out.println("BEGIN updateList::l.size(): " + l.size());
//	while(l.size()!=0)
//	{
//		l.delete(0);
//	}
//	if (DEBUG) System.out.println("Middle updateList::l.size():(should=0) " + l.size());
//	for(int i=0;i<a.length;i++)
//	{
//		if (DEBUG) System.out.println("Adding " + a[i]);
//		l.append(a[i],null);
//	}
//	if (DEBUG) System.out.println("END updateList::l.size(): " + l.size());
//  }

    public static String decode(String s) {
        StringBuffer stringbuffer = new StringBuffer();
        int i = s.length();
        byte byte0 = -1;
        int l = 0;
        int i1 = 0;
        int j1 = -1;
        for (; i1 < i; i1++) {
            int j;
            int k;
            switch (j = s.charAt(i1)) {
                case 37: // '%'
                    j = s.charAt(++i1);
                    int k1 = (Character.isDigit((char) j) ? j - 48 : (10 + Character.toLowerCase((char) j)) - 97) & 0xf;
                    j = s.charAt(++i1);
                    int l1 = (Character.isDigit((char) j) ? j - 48 : (10 + Character.toLowerCase((char) j)) - 97) & 0xf;
                    k = k1 << 4 | l1;
                    break;

                case 43: // '+'
                    k = 32;
                    break;

                default:
                    k = j;
                    break;

            }
            if ((k & 0xc0) == 128) {
                l = l << 6 | k & 0x3f;
                if (--j1 == 0)
                    stringbuffer.append((char) l);
            } else if ((k & 0x80) == 0)
                stringbuffer.append((char) k);
            else if ((k & 0xe0) == 192) {
                l = k & 0x1f;
                j1 = 1;
            } else if ((k & 0xf0) == 224) {
                l = k & 0xf;
                j1 = 2;
            } else if ((k & 0xf8) == 240) {
                l = k & 0x7;
                j1 = 3;
            } else if ((k & 0xfc) == 248) {
                l = k & 0x3;
                j1 = 4;
            } else {
                l = k & 0x1;
                j1 = 5;
            }
        }

        return stringbuffer.toString();
    }

    public static String encode(String s) {
        StringBuffer stringbuffer = new StringBuffer();
        int i = s.length();
        for (int j = 0; j < i; j++) {
            int k = s.charAt(j);
            if (65 <= k && k <= 90)
                stringbuffer.append((char) k);
            else if (97 <= k && k <= 122)
                stringbuffer.append((char) k);
            else if (48 <= k && k <= 57)
                stringbuffer.append((char) k);
            else if (k == 32)
                stringbuffer.append('+');
            else if (k == 45 || k == 95 || k == 46 || k == 33 || k == 126 || k == 42 || k == 40 || k == 41)
                stringbuffer.append((char) k);
            else if (k <= 127)
                stringbuffer.append(hex[k]);
            else if (k <= 2047) {
                stringbuffer.append(hex[0xc0 | k >> 6]);
                stringbuffer.append(hex[0x80 | k & 0x3f]);
            } else {
                stringbuffer.append(hex[0xe0 | k >> 12]);
                stringbuffer.append(hex[0x80 | k >> 6 & 0x3f]);
                stringbuffer.append(hex[0x80 | k & 0x3f]);
            }
        }

        return stringbuffer.toString();
    }

    static final String hex[] = {
            "%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07", "%08", "%09",
            "%0a", "%0b", "%0c", "%0d", "%0e", "%0f", "%10", "%11", "%12", "%13",
            "%14", "%15", "%16", "%17", "%18", "%19", "%1a", "%1b", "%1c", "%1d",
            "%1e", "%1f", "%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27",
            "%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f", "%30", "%31",
            "%32", "%33", "%34", "%35", "%36", "%37", "%38", "%39", "%3a", "%3b",
            "%3c", "%3d", "%3e", "%3f", "%40", "%41", "%42", "%43", "%44", "%45",
            "%46", "%47", "%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f",
            "%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57", "%58", "%59",
            "%5a", "%5b", "%5c", "%5d", "%5e", "%5f", "%60", "%61", "%62", "%63",
            "%64", "%65", "%66", "%67", "%68", "%69", "%6a", "%6b", "%6c", "%6d",
            "%6e", "%6f", "%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77",
            "%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f", "%80", "%81",
            "%82", "%83", "%84", "%85", "%86", "%87", "%88", "%89", "%8a", "%8b",
            "%8c", "%8d", "%8e", "%8f", "%90", "%91", "%92", "%93", "%94", "%95",
            "%96", "%97", "%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f",
            "%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7", "%a8", "%a9",
            "%aa", "%ab", "%ac", "%ad", "%ae", "%af", "%b0", "%b1", "%b2", "%b3",
            "%b4", "%b5", "%b6", "%b7", "%b8", "%b9", "%ba", "%bb", "%bc", "%bd",
            "%be", "%bf", "%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7",
            "%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf", "%d0", "%d1",
            "%d2", "%d3", "%d4", "%d5", "%d6", "%d7", "%d8", "%d9", "%da", "%db",
            "%dc", "%dd", "%de", "%df", "%e0", "%e1", "%e2", "%e3", "%e4", "%e5",
            "%e6", "%e7", "%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
            "%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7", "%f8", "%f9",
            "%fa", "%fb", "%fc", "%fd", "%fe", "%ff"
    };
// Base 62 (alphadecimal)
    private static final char[] cMap = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

// Base 59 (alphadecimal without 0,O,o)
/*	private static final char[] cMap= {'1','2','3','4','5','6','7','8','9','A','B','C','D','E','F',
									   'G','H','I','J','K','L','M','N','P','Q','R','S','T','U','V',
									   'W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l',
									   'm','n','p','q','r','s','t','u','v','w','x','y','z'};
*/

// Base 16 (hexadecimal)
//	private static final char[] cMap= {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};


    private static final int base = cMap.length;

    protected static String b62(long arg) {
        String b = "";
        long rem = arg;
        while (rem > 0) {
            for (int i = 10; i >= 0; i--) {
                if ((power(base, i)) <= rem) {
                    int ndx = (int) (rem / power(base, i));
                    b = b + cMap[ndx];
                    rem = (long) (rem - power(base, i) * ndx);
                } else if (b.length() > 0) {
                    b = b + "0";
                }
            }
        }
        return b;
    }

    protected static long power(int x, int n) {
        long b = 1;
        for (int a = 0; a < n; a++) {
            b *= x;
        }
        return b;
    }


    private static final boolean DEBUG = false;
}
