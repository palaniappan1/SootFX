package resource;

public enum SRMCategory {
    SOURCE, SINK, SANITIZER;

    public static SRMCategory fromString(String value) {
        if("source".equalsIgnoreCase(value)){
            return SOURCE;
        }
        else if("sink".equalsIgnoreCase(value)){
            return SINK;
        }
        else if("sanitizer".equalsIgnoreCase(value)){
            return SANITIZER;
        }
        else{
//            throw new IllegalArgumentException("Unknown SRM category: " + value);
            System.out.println("Unknown Category " + value);
        }
        return null;
    }
}
