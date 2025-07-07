from py4j.java_gateway import JavaGateway
from IPython.display import display
import converter

# obtaning the API handle
gateway = JavaGateway()
sootFX = gateway.entry_point.sootFX()

# listing all method features
availableFeatures = sootFX.listAllMethodFeatures()
for f in availableFeatures:
    print(f.getName())

# extract all method features
sootFX.addClassPath("/Users/palaniappanmuthuraman/WorkSpace/LinearConstantPropagation/IDELinearConstantAnalysisClientSootUp/src/test/resources/latest/commons-codec-1.15.jar")
extracted_features = sootFX.extractAllMethodFeatures()
df = converter.to_dataframe(extracted_features)

# extract selected method features
selected_features = gateway.jvm.java.util.ArrayList()
selected_features.add('MethodAssignStmtCount')
selected_features.add('MethodBranchCount')
extracted_features = sootFX.extractMethodFeaturesInclude(selected_features)
df = converter.to_dataframe(extracted_features)
display(df)
