public class OperatiuniTableDisplayer<T> extends TableDisplayer<T>{

    public OperatiuniTableDisplayer(String nrInventar) {
        super();
        this.nrInventar = nrInventar;
    }

    private String nrInventar;

    public String getNrInventar() {
        return nrInventar;
    }

    public void setNrInventar(String nrInventar) {
        this.nrInventar = nrInventar;
    }
}
