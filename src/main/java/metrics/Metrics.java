package metrics;

public class Metrics {

    private static long startTime;
    private static long endTime;
    private static int comparisonCount;
    private static int unionCount;
    private static int insertCount;
    private static int deleteCount;

    public static void startTimer() {
        startTime = System.nanoTime();
        comparisonCount = 0;
        unionCount = 0;
        insertCount = 0;
        deleteCount = 0;
    }

    public static void stopTimer() {
        endTime = System.nanoTime();
    }

    public static long getExecutionTime() {
        return (endTime - startTime) / 1000000;
    }

    public static void incrementComparisons() {
        comparisonCount++;
    }

    public static void incrementUnions() {
        unionCount++;
    }

    public static void incrementInserts() {
        insertCount++;
    }

    public static void incrementDeletes() {
        deleteCount++;
    }

    public static int getComparisonCount() {
        return comparisonCount;
    }

    public static int getUnionCount() {
        return unionCount;
    }

    public static int getInsertCount() {
        return insertCount;
    }

    public static int getDeleteCount() {
        return deleteCount;
    }

    public static void printMetrics() {
        System.out.println("Execution Time: " + getExecutionTime() + " ms");
        System.out.println("Comparisons: " + getComparisonCount());
        System.out.println("Unions: " + getUnionCount());
        System.out.println("Inserts: " + getInsertCount());
        System.out.println("Deletes: " + getDeleteCount());
    }
}
