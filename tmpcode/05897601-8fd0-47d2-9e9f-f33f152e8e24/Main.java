import java.util.Scanner;

/**
 * @author limincai
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] prices = new int[n];
        for (int i = 0; i < n; i++) {
            prices[i] = scanner.nextInt();
        }
        int ans = 0;
        for (int i = 1; i < n; i++) {
            ans += Math.max(prices[i] - prices[i - 1], 0);
        }
        System.out.println(ans);
    }
}

