public class ClasificariRecord {

    private String cod;
    private String description;
    private int minDur;
    private int maxDur;

    public ClasificariRecord(String cod, String description, int minDur, int maxDur) {
        this.cod = cod;
        this.description = description;
        this.minDur = minDur;
        this.maxDur = maxDur;
    }

    public String getCod() {
        return cod;
    }

    public String getDescription() {
        return description;
    }

    public int getMinDur() {
        return minDur;
    }

    public int getMaxDur() {
        return maxDur;
    }
}


