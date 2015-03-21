package makasprzak.idea.plugins;

import com.google.common.collect.ImmutableList;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import makasprzak.idea.plugins.element.ElementGenerator;
import makasprzak.idea.plugins.generationstrategy.GenerationConcreteStrategy;
import makasprzak.idea.plugins.generationstrategy.GenerationStrategy;
import makasprzak.idea.plugins.model.Pojo;
import makasprzak.idea.plugins.model.Property;
import makasprzak.idea.plugins.model.StepBuilderPattern;

import java.util.List;

import static com.intellij.psi.JavaPsiFacade.getElementFactory;
import static makasprzak.idea.plugins.model.PsiPojo.Builder.psiPojo;

/**
 * Created by Maciej Kasprzak on 2014-09-21.
 */
public class StepBuilderGeneratorAction extends AnAction implements StepBuilderGenerator{

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiElement currentElement = getCurrentElement(e);
        PsiClass psiClass = currentElement == null ? null : PsiTreeUtil.getParentOfType(currentElement, PsiClass.class);
        //TODO generation strategy client
        GenerationStrategy generationStrategy = GenerationConcreteStrategy.FROM_FIELDS.get();
        generationStrategy.start(psiClass);
        if (generationStrategy.isOk()) {
            generateBuilderPattern(generationStrategy.getProperties(), psiClass, currentElement);
        }

    }

    @Override
    public void generateBuilderPattern(final List<Property> properties, final PsiClass psiClass, PsiElement currentElement) {
        new WriteCommandAction.Simple(psiClass.getProject()) {
            @Override
            protected void run() throws Throwable {
                StepBuilderPattern stepBuilderPattern = composer(getProject()).build(Pojo.Builder.pojo()
                        .withName(psiClass.getName())
                        .withProperties(properties)
                        .withConstructorInjection(false)
                        .build());
                for (PsiClass inner : ImmutableList.<PsiClass>builder()
                        .add(stepBuilderPattern.getBuilderClass())
                        .addAll(stepBuilderPattern.getStepInterfaces())
                        .build()) {
                    reformat(inner);
                    psiClass.add(inner);
                }
            }


            private void reformat(PsiClass psiClass) {
                CodeStyleManager.getInstance(getProject()).reformat(psiClass);
            }
        }.execute();
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(getCurrentClass(e) != null);
    }

    private PsiClass getCurrentClass(AnActionEvent e) {
        PsiElement currentElement = getCurrentElement(e);
        return currentElement == null ? null : PsiTreeUtil.getParentOfType(currentElement, PsiClass.class);
    }

    private BuilderPatternComposerImpl composer(Project project) {
        return new BuilderPatternComposerImpl(new PsiElementGenerator(), getElementFactory(project), new ElementGenerator());
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
