package magmaout.furryappet.api.scripts.engine;

public class FurryappetCompileException extends Exception {
    private final int line;
    private final int column;

    public FurryappetCompileException(String message, int line, int column) {
        super(message);
        this.line = line;
        this.column = column;
    }
    public int getLine() {
        return line;
    }
    public int getColumn() {
        return column;
    }
}
