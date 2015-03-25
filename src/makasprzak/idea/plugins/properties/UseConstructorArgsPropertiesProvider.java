package makasprzak.idea.plugins.properties;

import com.intellij.psi.PsiClass;

public class UseConstructorArgsPropertiesProvider implements PropertiesProvider {

    private final ConstructorPropertiesAnalyzer constructorPropertiesAnalyzer;

    public UseConstructorArgsPropertiesProvider(ConstructorPropertiesAnalyzer constructorPropertiesAnalyzer) {
        this.constructorPropertiesAnalyzer = constructorPropertiesAnalyzer;
    }

    @Override
    public void getProperties(PsiClass psiClass, PropertiesConsumer consumer) {
        consumer.consume(constructorPropertiesAnalyzer.getBiggestConstructorArgs(psiClass));
    }
}
