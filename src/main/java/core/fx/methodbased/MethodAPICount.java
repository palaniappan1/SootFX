package core.fx.methodbased;

import api.SootFX;
import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import resource.APICallStats;
import resource.SourceSinkIndex;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public abstract class MethodAPICount implements MethodFEU<Long> {

    private static SourceSinkIndex sourceSinkIndex = null;

    public MethodAPICount(){
        checkIndexLoad();
    }

    public void checkIndexLoad(){
        String serPath = SootFX.isAPK
                ? "src/main/resources/SourceAndSinksApk.ser"
                : "src/main/resources/SourceAndSinksJar.ser";
        if (sourceSinkIndex == null) {
            sourceSinkIndex = loadIndex(serPath);
        }
    }

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

    @Override
    public Feature<Long> extract(SootMethod target) {
        APICallStats stats = analyzeMethod(target);
        return extractCountFromMethod(target, stats);
    }

    private APICallStats analyzeMethod(SootMethod target) {
        if(!target.hasActiveBody()) return new APICallStats(0,0,0,0,0);
        long crypto = 0, reflection = 0, source = 0, sink = 0;
        for(Unit unit: target.getActiveBody().getUnits()){
            Stmt stmt = (Stmt) unit;
            if(stmt.containsInvokeExpr()){
                String signature = stmt.getInvokeExpr().getMethod().getSignature();
                if(FxUtil.isReflectiveCall(stmt.getInvokeExpr().getMethodRef())){
                    reflection++;
                }
                else if(FxUtil.isCryptoPackage(stmt.getInvokeExpr().getMethodRef().getDeclaringClass().getPackageName())){
                    crypto++;
                }
                else if(getSourceSignatures().contains(signature)){
                    System.out.println("Source API " + signature);
                    source++;
                }
                else if(getSinkSignatures().contains(signature)){
                    System.out.println("Sink API " + signature);
                    sink++;
                }
            }
        }
        int trap = target.getActiveBody().getTraps().size();
        APICallStats apiCallStats = new APICallStats(crypto, reflection, source, sink, trap);
        System.out.println("Target Method: " + target);
        System.out.println(apiCallStats + "\n****");
        return apiCallStats;
    }

    public abstract Feature<Long> extractCountFromMethod(SootMethod method, APICallStats stats);
}