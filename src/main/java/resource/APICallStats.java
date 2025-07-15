package resource;

public class APICallStats {
    boolean crypto; boolean reflection; boolean source; boolean sink; boolean traps;

    public APICallStats(boolean crypto, boolean reflection, boolean source, boolean sink, boolean traps){
        this.crypto = crypto;
        this.reflection = reflection;
        this.sink = sink;
        this.source = source;
        this.traps = traps;
    }

    public boolean getCrypto() {
        return crypto;
    }

    public boolean getReflection() {
        return reflection;
    }

    public boolean getSink() {
        return sink;
    }

    public boolean getSource() {
        return source;
    }

    public boolean getTraps() {
        return traps;
    }


    @Override
    public String toString() {
        return "crypto : " + crypto +
                " reflection: " + reflection +
                " sink: " + sink +
                " source: " + source +
                "trap: " + traps;
    }
}
