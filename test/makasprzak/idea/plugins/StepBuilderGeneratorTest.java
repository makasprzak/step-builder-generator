package makasprzak.idea.plugins;

import com.google.common.base.Function;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import makasprzak.idea.plugins.mappers.PsiParameterMapper;
import makasprzak.idea.plugins.model.Property;

import java.io.File;
import java.util.List;

import static com.google.common.collect.Lists.transform;
import static com.intellij.openapi.application.PathManager.getJarPathForClass;
import static java.util.Arrays.asList;
import static makasprzak.idea.plugins.mappers.PsiFieldMapper.toProperty;

public class StepBuilderGeneratorTest extends LightCodeInsightFixtureTestCase{
    @Override
    protected String getTestDataPath() {
        return new File(getJarPathForClass(StepBuilderGeneratorTest.class)).getPath();
    }

    public void test_shouldSupportSetterInjection() throws Exception {
        shouldGenerateBuilderFromFields("setters");
    }

    public void test_shouldSupportConstructorInjection() throws Exception {
        shouldGenerateBuilderFromFields("constructor");
    }

    public void test_shouldUseConstructorSignatureWhenChoosingConstructorInjection() throws Exception {
        shouldGenerateBuilderFromConstructor("constructorWithVararg");
    }

    private void shouldGenerateBuilderFromFields(String prefix) {
        shouldGenerateBuilder(prefix, new Function<PsiClass, List<Property>>() {
            @Override
            public List<Property> apply(PsiClass psiClass) {
                return getPropertiesFromFields(psiClass);
            }
        });    }

    private void shouldGenerateBuilderFromConstructor(String prefix) {
        shouldGenerateBuilder(prefix, new Function<PsiClass, List<Property>>() {
            @Override
            public List<Property> apply(PsiClass psiClass) {
                return getPropertiesFromConstructor(psiClass);
            }
        });
    }

    private List<Property> getPropertiesFromConstructor(PsiClass psiClass) {
        List<PsiParameter> parameterList = asList(psiClass.getConstructors()[0].getParameterList().getParameters());
        return transform(parameterList, PsiParameterMapper.toProperty());
    }

    private List<Property> getPropertiesFromFields(PsiClass pojoWithSettersPsiClass) {
        List<PsiField> psiFields = asList(pojoWithSettersPsiClass.getAllFields());
        return transform(psiFields, toProperty());
    }

    private void shouldGenerateBuilder(String prefix, Function<PsiClass, List<Property>> toProperties) {
        myFixture.configureByFile(prefix + "_before.java");
        PsiElement elementAtCaret = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiClass pojoWithSettersPsiClass = PsiTreeUtil.getParentOfType(elementAtCaret, PsiClass.class);
        new StepBuilderGeneratorImpl().generateBuilderPattern(
                toProperties.apply(pojoWithSettersPsiClass),pojoWithSettersPsiClass, elementAtCaret);
        myFixture.checkResultByFile(prefix + "_after.java");
    }
}