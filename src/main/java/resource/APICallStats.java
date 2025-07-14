package resource;

public class APICallStats {
    long crypto; long reflection; long source; long sink; long traps;

    public APICallStats(long crypto, long reflection, long source, long sink, long traps){
        this.crypto = crypto;
        this.reflection = reflection;
        this.sink = sink;
        this.source = source;
        this.traps = traps;
    }

    public long getCrypto() {
        return crypto;
    }

    public long getReflection() {
        return reflection;
    }

    public long getSink() {
        return sink;
    }

    public long getSource() {
        return source;
    }

    public long getTraps() {
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
