package logic;

public class PrimeChecker {
    public static boolean isPrime(int n) {
        // Angka < 2 bukan prima
        if (n < 2) return false;
        // 2 adalah prima
        if (n == 2) return true;
        // Cek pembagi
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}