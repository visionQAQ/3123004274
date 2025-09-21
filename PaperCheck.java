import java.io.*;
import java.nio.file.*;

public class PaperCheck {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("用法: java EditDistancePaperCheck [原文路径] [抄袭版路径] [答案文件路径]");
            return;
        }

        try {
            String originalText = readFile(args[0]);
            String plagiarizedText = readFile(args[1]);

            double similarity = calculateEditDistanceSimilarity(originalText, plagiarizedText);
            String result = String.format("%.2f", similarity * 100);

            writeFile(args[2], result);
            System.out.println("编辑距离相似度: " + result + "%");

        } catch (IOException e) {
            System.err.println("错误: " + e.getMessage());
        }
    }

    private static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8").trim();
    }

    private static void writeFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes("UTF-8"));
    }

    private static int calculateEditDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(
                                    dp[i - 1][j] + 1,     // 删除
                                    dp[i][j - 1] + 1),    // 插入
                            dp[i - 1][j - 1] + cost // 替换
                    );
                }
            }
        }
        return dp[m][n];
    }

    private static double calculateEditDistanceSimilarity(String text1, String text2) {
        int maxLength = Math.max(text1.length(), text2.length());
        if (maxLength == 0) return 1.0;

        int editDistance = calculateEditDistance(text1, text2);
        return 1.0 - (double) editDistance / maxLength;
    }
}