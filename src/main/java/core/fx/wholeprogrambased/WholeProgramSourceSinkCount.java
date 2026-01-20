package core.fx.wholeprogrambased;

import api.SootFX;
import core.fx.base.Feature;
import resource.SourceSinkIndex;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class WholeProgramSourceSinkCount extends WholeProgramMethodBasedFEU<Long>{

    private static SourceSinkIndex sourceSinkIndex = null;

    public WholeProgramSourceSinkCount() {
        String serPath;
        if(SootFX.isAPK){
            serPath = "src/main/resources/SourceAndSinksApk.ser";
        }
        else {
            serPath = "src/main/resources/SourceAndSinksJar.ser";
        }
        if (sourceSinkIndex == null) {
            sourceSinkIndex = loadIndex(serPath);
        }
    }

    @Override
    protected Feature<Long> extractWithMethods(CallGraph cg, Set<SootMethod> methods) {
        long count = 0;
        methods = methods.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        for (SootMethod method : methods) {
            if (!method.isConcrete()) continue;
            try {
                Body body = method.retrieveActiveBody();
                for (Unit unit : body.getUnits()) {
                    Stmt stmt = (Stmt) unit;
                    if (stmt.containsInvokeExpr()) {
                        String sig = stmt.getInvokeExpr().getMethod().getSignature();
                        if (isRelevantSignature(sig)) {
                            count++;
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return new Feature<>(this.getClass().getSimpleName(), count);
    }

    protected abstract boolean isRelevantSignature(String signature);

    private SourceSinkIndex loadIndex(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(path)))) {
            return (SourceSinkIndex) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new SourceSinkIndex(); // Empty index if failed
        }
    }

    protected Set<String> getSourceSignatures() {
        return sourceSinkIndex.sources;
    }

    protected Set<String> getSinkSignatures() {
        return sourceSinkIndex.sinks;
    }


}
