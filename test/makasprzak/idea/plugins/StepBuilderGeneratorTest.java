package makasprzak.idea.plugins;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.intellij.openapi.application.PathManager.getJarPathForClass;

public class StepBuilderGeneratorTest extends LightCodeInsightFixtureTestCase{
    @Override
    protected String getTestDataPath() {
        return new File(getJarPathForClass(StepBuilderGeneratorTest.class)).getPath();
    }

    public void test_shouldSupportSetterInjection() throws Exception {
        shouldGenerateBuilder("setters");
    }

    public void test_shouldSupportConstructorInjection() throws Exception {
        shouldGenerateBuilder("constructor");
    }

    public void test_shouldUseConstructorSignatureWhenChoosingConstructorInjection() throws Exception {
        shouldGenerateBuilder("constructorWithVararg");
    }

    private void shouldGenerateBuilder(String prefix) {
        myFixture.configureByFile(prefix + "_before.java");
        PsiElement elementAtCaret = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiClass pojoWithSettersPsiClass = PsiTreeUtil.getParentOfType(elementAtCaret, PsiClass.class);
        List<PsiField> psiFields = Arrays.asList(pojoWithSettersPsiClass.getAllFields());
        new StepBuilderGeneratorAction().generateBuilderPattern(psiFields,pojoWithSettersPsiClass, elementAtCaret);
        myFixture.checkResultByFile(prefix + "_after.java");
    }
}