package resource;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SourceSinkJarIndexer {

    public static void main(String[] args) throws IOException {
        String outputPath = "src/main/resources/SourceAndSinksJar.ser";
        SourceSinkIndex index = new SourceSinkIndex();
        ObjectMapper mapper = new ObjectMapper();
        SRMWrapper srmWrapper = mapper.readValue(SourceSinkApkIndexer.class.getClassLoader().getResourceAsStream("srm-catalog.json"), SRMWrapper.class);
        for (SRMPojo method : srmWrapper.methods) {
            if(method.srmCategories == SRMCategory.SOURCE){
                index.sources.add(method.signature);
            }
            else if(method.srmCategories == SRMCategory.SINK){
                index.sinks.add(method.signature);
            }
            else{
                System.out.println("It does not belong anywhere as of now " + method.signature + " -----> " + method.srmCategories);
            }
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
