package core.fx.methodbased;

import api.SootFX;
import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import fj.test.Bool;
import resource.APICallStats;
import resource.SourceSinkIndex;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MethodAPICount implements MethodFEU<Boolean> {

    private static SourceSinkIndex sourceSinkIndex = null;

    private static final Map<String, APICallStats> sharedStatsCache = new ConcurrentHashMap<>();

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
    public Feature<Boolean> extract(SootMethod target) {
        String key = target.getDeclaringClass().getName() + ":" + target.getSubSignature();
        APICallStats stats = sharedStatsCache.computeIfAbsent(key, k -> {
            System.out.println("Value not present, so computing fresh for the target " + target + " and this method has active body: " + target.hasActiveBody());
            return analyzeMethod(target);
        });
        return extractCountFromMethod(target, stats);
    }

    private APICallStats analyzeMethod(SootMethod target) {
        if(!target.hasActiveBody()) return new APICallStats(false, false, false, false, false);
        boolean crypto = false, reflection = false, source = false, sink = false;
        for(Unit unit: target.getActiveBody().getUnits()){
            Stmt stmt = (Stmt) unit;
            if(stmt.containsInvokeExpr()){
                String signature = stmt.getInvokeExpr().getMethod().getSignature();
                if(FxUtil.isReflectiveCall(stmt.getInvokeExpr().getMethodRef())){
                    reflection = true;
                }
                else if(FxUtil.isCryptoPackage(stmt.getInvokeExpr().getMethodRef().getDeclaringClass().getPackageName())){
                    crypto = true;
                }
                else if(getSourceSignatures().contains(signature)){
                    System.out.println("Source API " + target);
                    source = true;
                }
                else if(getSinkSignatures().contains(signature)){
                    System.out.println("Sink API " + target);
                    sink = true;
                }
            }
        }
        boolean trap = !target.getActiveBody().getTraps().isEmpty();
        return new APICallStats(crypto, reflection, source, sink, trap);
    }

    public abstract Feature<Boolean> extractCountFromMethod(SootMethod method, APICallStats stats);
}