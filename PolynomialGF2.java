import java.util.Arrays;

public class PolynomialGF2 {

	final PolynomialGF2 ZERO = null;
	final static PolynomialGF2 ONE = new PolynomialGF2(new boolean[] {true});
	private boolean[] a;
	
	public PolynomialGF2() { //Default Constructor
		a = ONE.toArray(); // a ist Polynom "1"
	}
	
	public PolynomialGF2(boolean[] a) { //Constructor
		if (!a[a.length-1]) { //Trimme Array, wenn letzter Koeffizient = 0
			a = trim(a);
		}
		this.a = a; //Setze Referenz auf �bergebenen Koeffizientenarray
	}
	
	public static void main(String[] args) {
		//Test 1
		System.out.println("i |  hash | x^i");
		System.out.println("-----------------------");
		for (int i = 0; i < 7; i++) {
			boolean[] test1 = {true, true, false, true};
			PolynomialGF2 pol1 = new PolynomialGF2().shift(ONE, i); //1, x, x^2, x^3, ..., x^6
			PolynomialGF2 pol2 = new PolynomialGF2(test1); //x^3 + x + 1
			PolynomialGF2 ergPol = pol1.mod(pol2); // erg = {1, x, x^2, ..., x^6} % x^3 + x + 1
			
			System.out.println(i + " |\t" + ergPol.hashCode() + " | " + ergPol);
		}
		
		System.out.println();
		
		//Test 2
		String[] sym = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
		System.out.println("  |   0   1   2   3   4   5   6   7   8   9   a   b   c   d   e   f");
		System.out.println("-------------------------------------------------------------------");
		for (int i = 0; i < sym.length; i++) {
			System.out.print(sym[i] + " |");
			for (int j = 0; j < sym.length; j++) {
				String hex = "";
				hex += sym[i] + sym[j]; //Zusammensetzung der Hex-Zahl
				int dec = Integer.parseInt(hex, 16); //Hex in Dec
				PolynomialGF2 erg = new PolynomialGF2(ONE.toArray()); //erg = 1;
				PolynomialGF2 timPol = new PolynomialGF2(new boolean[] {true, true}); //timPol = x + 1
				for (int k = 0; k < dec; k++) {
					erg = erg.times(timPol); //erg = timPol^dec
				}
				PolynomialGF2 modPol = new PolynomialGF2(new boolean[] {true, true, false, true, true, false, false, false, true}); //modPol = x^8 + x^4 + x^3 + x + 1
				erg = erg.mod(modPol); //erg = ((x+1)^dec) % (x^8 + x^4 + x^3 + x + 1)
				String binaryString = Arrays.toString(erg.toArray()).replace("true", "1").replace("false", "0").replace("[", "").replace("]", "").replace(", ", ""); //Bilde bin String aus bool String
				binaryString = new StringBuilder(binaryString).reverse().toString(); //Umdrehen der bit-repr�sentierung
				int decimal = Integer.parseInt(binaryString, 2); //Bilde dec aus dem bin String
				String hexStr = Integer.toString(decimal, 16); //Bilde hex aus dec
				if (hexStr.length() == 1) { //Design, adde 0 falls einstellig
					hexStr = "0" + hexStr;
				}
				System.out.print("  " + hexStr); //Gib die umgeformten hex-Zahlen aus
			}
			System.out.println();
		}
	}
	
	public boolean[] trim(boolean[] arr) { //Entferne anfangs-nullen
		int t = arr.length;
		for (int i = arr.length-1; i >= 0; i--) {
			if (arr[i]) {
				t = i+1;
				i = -1;
			}
		}
	
		boolean[] newArr = new boolean[t];//anlegen und überschreiben in ein neues array
		for (int i = 0; i < newArr.length; i++) {
			newArr[i] = arr[i];
		}
		return newArr;
	}
	
	public boolean[] toArray() { //Gib Arraykopie von a des Objektes aus
		return a.clone();
	}
	
	public boolean isZero() {
		return a == null;
	}
	
	public boolean isOne() {
		return a.length == 1;
	}

	public PolynomialGF2 clone(PolynomialGF2 pgf2) {
		boolean[] arr = pgf2.toArray();
		boolean[] newArr = new boolean[arr.length];
		for (int i = 0; i < newArr.length; i++) {
			newArr[i] = arr[i];
		}
		return new PolynomialGF2(newArr);
	}
	
	public boolean equals(PolynomialGF2 pgf2) {
		boolean[] arr = pgf2.toArray();
		if (a.length != arr.length) { //Wenn L�nge nicht �bereinstimmt sind die polynome ungleich
			return false;
		}
		for (int i = 0; i < a.length; i++) { //Sobald ein bit unterschiedlich ist, sind die polynome ungleich
			if (a[i] != arr[i]) {
				return false;
			}
		}
		return true; //Sonst sind sie gleich
	}
	
	public int hashCode() { //bilde hashcode aus polynom
		int h = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i]) {
				h += Math.pow(2, i);
			}
		}
		return h;
	}
	
	public String toString() {
		String text = "";
		if (a.length > 2) { //wenn grad des polynoms mind. 2 ist
			text = "x^";
			text += a.length-1 + " ";
		}
		for (int i = a.length-2; i > 1; i--) { //adde die einzelnen x^i's zum string
			if (a[i]) {
				if (text.length() > 0) {
					text += "+ ";
				}
				text += "x^" + i + " ";
			}
		}
		if (a.length > 1 && a[1]) { //wenn an stelle 1 true, braucht man das "^" nicht mehr
			if (text.length() > 0) {
				text += "+ ";
			}
			text += "x ";
		}
		if (a.length > 0 && a[0]) { //wenn an stelle 0 true, braucht man das "x^" nicht mehr
			if (text.length() > 0) {
				text += "+ ";
			}
			text += "1";
		}
		return text;
	}
	
	public PolynomialGF2 times(PolynomialGF2 timPol) { //Polynom-multiplikation von a mit timPol
		boolean[] arr = timPol.toArray(); //Kopie des mit a zu multiplizierenden arrays
		boolean[] ergArr = new boolean[a.length + arr.length]; //l�nge des ergebnis-arrays ist die h�chste stelle nach verschieben des arrays um die h�chste stelle des anderen arrays
		PolynomialGF2 ergPol = new PolynomialGF2(ergArr);
		for (int i = 0; i < a.length; i++) {
			if (a[i]) {
				ergPol = ergPol.plus(shift(timPol, i)); //wenn an i-ter stelle 1, dann verschiebe timPol um i stellen und addiere ergebnis mit dem momentanen ergPol
			}
		}
		return ergPol;
	}
	
	public PolynomialGF2 plus(PolynomialGF2 addPol) { //Polynom addition von a mit addPol
		boolean[] addArr = addPol.toArray(); //Kopie von koeffizienten aus addPol
		int n = Math.max(a.length, addArr.length); //l�nge des sich ergebenden array nach addition: ist h�chste stelle der arrays
		boolean[] ergArr = new boolean[n];
		for (int i = 0; i < n; i++) {
			if (i < a.length && i < addArr.length) { //F�r alle stellen im array, f�hre bit-addition aus und speichere teilwerte in ergArr
				ergArr[i] = bitAdd(a[i], addArr[i]);
			} else {
				if (i < a.length) { //falls i im bereich nach arr.length, �bernehme werte aus a
					ergArr[i] = a[i];
				} else { //andersherum
					ergArr[i] = addArr[i];
				}
			}
		}
		return new PolynomialGF2(ergArr);
	}
	
	public PolynomialGF2 mod(PolynomialGF2 divPol) { //Rest-berechnung von polynomdivision a / divPol
		PolynomialGF2 tmp = new PolynomialGF2(this.toArray()); //Kopie von a
		while(degree(tmp) >= degree(divPol)) { //solange grad von tmp mind. grad von divPol ist, addiere divPol * (1 um graddifferenz zw. tmp und divPol verschoben) auf tmp
			tmp = tmp.plus(divPol.times(shift(ONE, degree(tmp) - degree(divPol))));
		}
		return tmp;
	}
	
	public boolean bitAdd(boolean val1, boolean val2) { //Bit addition
		return val1 != val2;
	}
	
	public boolean bitMul(boolean val1, boolean val2) { //Bit multiplikation
		return val1 && val2;
	}
	
	public int degree(PolynomialGF2 pgf2) { //Grad des polynoms pgf2
		return pgf2.toArray().length-1;
	}
	
	public PolynomialGF2 shift(PolynomialGF2 shiftPol, int k) { //verschiebe shiftPol um k stellen
		if (k <= 0) {
			return shiftPol;
		}
		boolean[] arr = shiftPol.toArray(); //kopie von shiftPol-koeffizienten
		boolean[] ergArr = new boolean[arr.length+k]; //Neue L�nge des arrays ist nun momentane l�nge plus verschiebungswert k
		for (int i = arr.length-1; i >= 0; i--) {
			ergArr[i+k] = arr[i]; //kopiere alle werte aus arr an neue position in ergArr
		}
		return new PolynomialGF2(ergArr);
	}
}