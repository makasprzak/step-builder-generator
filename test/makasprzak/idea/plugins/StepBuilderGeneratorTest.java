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

    public void test_shouldGenerateInnerBuilderForPojoWithSetterInjection() throws Exception {
        myFixture.configureByFile("PojoWithSetters_before.java");
        PsiElement elementAtCaret = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiClass pojoWithSettersPsiClass = PsiTreeUtil.getParentOfType(elementAtCaret, PsiClass.class);
        List<PsiField> psiFields = Arrays.asList(pojoWithSettersPsiClass.getAllFields());
        new StepBuilderGeneratorAction().generateBuilderPattern(psiFields,pojoWithSettersPsiClass, elementAtCaret);
        myFixture.checkResultByFile("PojoWithSetters_after.java");
    }
}