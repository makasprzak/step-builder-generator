package makasprzak.idea.plugins;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import makasprzak.idea.plugins.generationstrategy.GenerationConcreteStrategy;
import makasprzak.idea.plugins.generationstrategy.GenerationStrategy;
import makasprzak.idea.plugins.generationstrategy.GenerationStrategyClient;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

/**
 * @author makasprzak
 */
public class StepBuilderGeneratorAction extends AnAction {

    private final StepBuilderGenerator stepBuilderGenerator = new StepBuilderGeneratorImpl();

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        GenerationStrategy generationStrategy = GenerationConcreteStrategy.FROM_FIELDS.get();
        generate(getCurrentElement(actionEvent), getPsiClass(getCurrentElement(actionEvent)), generationStrategy);
    }

    private PsiClass getPsiClass(PsiElement currentElement) {
        return currentElement == null ? null : PsiTreeUtil.getParentOfType(currentElement, PsiClass.class);
    }

    private void generate(final PsiElement currentElement, final PsiClass psiClass, GenerationStrategy generationStrategy) {
        new GenerationStrategyClient(new GenerationStrategyClient.PropertiesConsumer() {
            @Override
            public void consume(List<Property> properties) {
                stepBuilderGenerator.generateBuilderPattern(properties, psiClass, currentElement);
            }
        }).executeStrategy(psiClass, generationStrategy);
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(getCurrentClass(e) != null);
    }

    private PsiClass getCurrentClass(AnActionEvent e) {
        PsiElement currentElement = getCurrentElement(e);
        return currentElement == null ? null : PsiTreeUtil.getParentOfType(currentElement, PsiClass.class);
    }

    private PsiElement getCurrentElement(AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        return psiFile.findElementAt(offset);
    }
}
