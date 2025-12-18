package logic;

public class PrimeChecker {
    public static boolean isPrime(int n) {
        // Angka kurang dari 2 bukan prima
        if (n < 2) return false;
        // Cek pembagi
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}