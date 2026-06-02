// ***From Lab Files***
public class KMP{
    public static String searchKMP(String pattern, String text)
    {
        int M = pattern.length();
        int N = text.length();
        String indexes = "";

        // Preprocess the pattern (calculate lps[] array)
        // lps[] will hold the longest prefix suffix values for pattern
        int[] lps = computeLPSArray(pattern);

        int i = 0; // index for txt[]
        int j = 0; // index for pat[]
        while (i < N) {
            if (pattern.charAt(j) == text.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                indexes += (i - j) + "  ";
                j = lps[j - 1];
            }

            // mismatch after j matches
            else if (i < N && pattern.charAt(j) != text.charAt(i)) {
                // Do not match lps[0..lps[j-1]] characters, they will match anyway
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }

        return indexes;
    }

    static int[] computeLPSArray(String pattern) {
        int M = pattern.length();
        int lps[] = new int[M];
        // length of the previous longest prefix suffix
        int len = 0;
        int i = 1;
        lps[0] = 0; // lps[0] is always 0

        // the loop calculates lps[i] for i = 1 to M-1
        while (i < M) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            }
            else // (pat[i] != pat[len])
            {
                if (len != 0) {
                    len = lps[len - 1];
                }
                else // if (len == 0)
                {
                    lps[i] = len;
                    i++;
                }
            }
        }

        return lps;
    }
}
