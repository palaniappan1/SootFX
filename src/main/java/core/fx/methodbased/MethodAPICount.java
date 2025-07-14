package core.fx.methodbased;

import api.SootFX;
import core.fx.FxUtil;
import core.fx.base.Feature;
import core.fx.base.MethodFEU;
import resource.SourceSinkIndex;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public abstract class MethodAPICount implements MethodFEU<Long> {

    long numberOfCryptoCall;

    long numberOfReflectiveCall;

    long numberOfSourceStmts;

    long numberOfSinkStmts;

    long numberOfTryCatchBlocks;

    private static SourceSinkIndex sourceSinkIndex = null;

    public MethodAPICount(){
        checkIndexLoad();
    }

    public void checkIndexLoad(){
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
        resetCounters();
        countAPICalls(target);
        return extractCountFromMethod(target);
    }

    public void resetCounters(){
        this.numberOfCryptoCall = 0;
        this.numberOfReflectiveCall = 0;
        this.numberOfSourceStmts = 0;
        this.numberOfSinkStmts = 0;
        this.numberOfTryCatchBlocks = 0;
    }

    private void countAPICalls(SootMethod target) {
        if(!target.hasActiveBody()) return;
        checkIndexLoad();
        for(Unit unit: target.getActiveBody().getUnits()){
            Stmt stmt = (Stmt) unit;
            if(stmt.containsInvokeExpr()){
                if(FxUtil.isReflectiveCall(stmt.getInvokeExpr().getMethodRef())){
                    numberOfReflectiveCall++;
                }
                else if(FxUtil.isCryptoPackage(stmt.getInvokeExpr().getMethodRef().getDeclaringClass().getPackageName())){
                    numberOfCryptoCall++;
                }
                else if(getSourceSignatures().contains(stmt.getInvokeExpr().getMethod().getSignature())){
                    numberOfSourceStmts++;
                }
                else if(getSinkSignatures().contains(stmt.getInvokeExpr().getMethod().getSignature())){
                    numberOfSinkStmts++;
                }
            }
        }
        numberOfTryCatchBlocks = target.getActiveBody().getTraps().size();
    }

    public abstract Feature<Long> extractCountFromMethod(SootMethod method);
}
