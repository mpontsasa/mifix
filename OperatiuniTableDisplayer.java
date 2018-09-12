import java.time.LocalDate;

public class OperatiuniTableDisplayer<T> extends TableDisplayer<T>{

    private String nrInventar;

    private LocalDate start;

    private LocalDate end;

    public OperatiuniTableDisplayer(String nrInventar, LocalDate start, LocalDate end) {
        super();
        this.nrInventar = nrInventar;
        this.start = start;
        this.end = end;
    }

    public String getNrInventar() {
        return nrInventar;
    }

    public void setNrInventar(String nrInventar) {
        this.nrInventar = nrInventar;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
