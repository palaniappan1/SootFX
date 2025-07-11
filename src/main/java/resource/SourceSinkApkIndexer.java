package resource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceSinkApkIndexer {

    private static final Pattern LINE_PATTERN = Pattern.compile("^(<.*?>)\\s*->\\s*_(SOURCE|SINK)_$");

    public static void main(String[] args) {

        String outputPath = "src/main/resources/SourceAndSinksApk.ser";
        SourceSinkIndex index = new SourceSinkIndex();

        try (InputStream is = SourceSinkApkIndexer.class.getClassLoader().getResourceAsStream("SourcesAndSinks.txt")) {
            if (is == null) {
                System.err.println("Could not find SourcesAndSinks.txt in resources folder!");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("%")) continue;

                    Matcher matcher = LINE_PATTERN.matcher(line);
                    if (matcher.matches()) {
                        String methodSig = matcher.group(1).trim();
                        String type = matcher.group(2).toUpperCase();

                        if (type.equals("SOURCE")) {
                            index.sources.add(methodSig);
                        } else if (type.equals("SINK")) {
                            index.sinks.add(methodSig);
                        }
                        else{
                            System.out.println("This does not belong anywhere" + methodSig);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read or parse SourcesAndSinks.txt from resources.");
            e.printStackTrace();
            return;
        }

        System.out.println("Parsed sources: " + index.sources.size());
        System.out.println("Parsed sinks: " + index.sinks.size());

        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(outputPath)))) {
            out.writeObject(index);
            System.out.println("Serialized index to " + outputPath);
        } catch (IOException e) {
            System.err.println("Failed to write serialized file.");
            e.printStackTrace();
        }
    }
}
