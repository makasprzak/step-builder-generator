package makasprzak.idea.plugins;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.ide.util.MemberChooserBuilder;
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
import com.intellij.psi.util.PsiUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.intellij.psi.JavaPsiFacade.getElementFactory;
import static java.util.Arrays.asList;

/**
 * Created by Maciej Kasprzak on 2014-09-21.
 */
public class StepBuilderGeneratorAction extends AnAction implements StepBuilderGenerator{

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiElement currentElement = getCurrentElement(e);
        PsiClass psiClass = currentElement == null ? null : PsiTreeUtil.getParentOfType(currentElement, PsiClass.class);
        GeneratorDialog generatorDialog = new GeneratorDialog(psiClass);
        generatorDialog.show();
        if (generatorDialog.isOK()) {
            generateBuilderPattern(generatorDialog.getFields(), psiClass, currentElement);
        }

    }

    @Override
    public void generateBuilderPattern(List<PsiField> fields, PsiClass psiClass, PsiElement currentElement) {
        new WriteCommandAction.Simple(psiClass.getProject()) {
            @Override
            protected void run() throws Throwable {
                BuilderClassComposer composer = composer(getProject());
                ImmutableList.<PsiClass>builder()
                        .add(composer.builderClass(psiClass, fields))
                        .addAll(composer.stepInterfaces(psiClass, fields))
                        .build()
                        .forEach(inner -> {
                            reformat(inner);
                            psiClass.add(inner);
                        });
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

    private BuilderClassComposer composer(Project project) {
        return new BuilderClassComposer(new ElementGenerator(), getElementFactory(project));
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
