import java.util.*;
import java.lang.*;
import java.io.*;
import java.awt.Desktop;

public class Main {

    final static int DeltaOne = 0x11111111;
    final static int DeltaTwo = 0x22222222;
    final static long K[] = new long[4];
    static long L[] = new long[3];
    static long R[] = new long[3];

    public static void main(String[] args) throws IOException {

        L[1] = 0x00000000;
        L[2] = 0x00000000;
        R[1] = 0x00000000;
        R[2] = 0x00000000;

        System.out.println("Decryption:");
        PrintStream obj = new PrintStream(new File("tst_Decryption.txt"));
        List<String> list = new ArrayList<>();
        Scanner stdin = new Scanner(System.in);

        for(int i = 0; i < 4; i++) {
            // input 8-bit Hex strings for K values
            System.out.println("Please input K[" + i + "] in Hex String(w/o '0x'): ");
            String input = stdin.next();
            list.add(input);
            // create 0xhexstring
            String X = "0x" + input;
            Long y = Long.parseLong(input, 16);
            K[i] = y;
            //System.out.println(K[i]);
            //System.out.println(y);
        }

        System.out.println("Please input L[2] in Hex String(w/o '0x'): ");
        String inputL = stdin.next();
        // create 0xhexstring
        String X = "0x" + inputL;
        Long y = Long.parseLong(inputL, 16);
        L[2] = y;
        //System.out.println("L[2] = " +  L[2]);

        System.out.println("Please input R[2] in Hex String(w/o '0x'): ");
        String inputR = stdin.next();
        // create 0xhexstring
        String T = "0x" + inputR;
        Long q = Long.parseLong(inputR, 16);
        R[2] = q;
        //System.out.println("R[2] = " +  R[2]);

        stdin.close();

        String[] arr = list.toArray(new String[0]);
        System.out.println();
        System.out.println("K Array is " + Arrays.toString(arr));

        decrypt();

        for(int i = 2; i > -1; i--) {
            obj.println("L[" + (i) + "] = " + Long.toHexString(L[i]) +
                    "    R[" + (i) + "] = " + Long.toHexString(R[i]));
        }

        obj.close();

        //text file, should be opening in default text editor
        File file = new File("tst_Decryption.txt");

        //first check if Desktop is supported by Platform or not
        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }

        // OPEN FILE
        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) desktop.open(file);

    }

    private static void decrypt() {
        System.out.println("L[2] = " + Long.toHexString(L[2]) + "    R[2] = " + Long.toHexString(R[2]));
        int j = 0;

        for(int i = 2; i > 0; i--) {
            long a = (L[i] << 4) + K[i-j];
            long b;
            // toggle between them
            if(j == 0) {
                b = L[i] + DeltaTwo;
            } else {
                b = L[i] + DeltaOne;
            }
            long c = (L[i] >> 5) + K[i + 1 -j];
            long d = a + b + c;
            long e = R[i] - d;
            L[i - 1] = e;
            R[i - 1] = L[i];
            System.out.println("L[" + (i-1) + "] = " + Long.toHexString(L[i-1]) +
                    "    R[" + (i-1) + "] = " + Long.toHexString(R[i-1]));
            if(j == 0) {
                j = 1;
            } else {
                j = 0;
            }
        }
        return;
    }

}
