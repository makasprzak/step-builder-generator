package makasprzak.idea.plugins;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.CollectionListModel;

import static com.intellij.psi.JavaPsiFacade.getElementFactory;

/**
 * Created by Maciej Kasprzak on 2014-09-21.
 */
public class StepBuilderGeneratorAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        PsiClass psiClass = getPsiClassFromContext(e);
        GeneratorDialog generatorDialog = new GeneratorDialog(psiClass);
        generatorDialog.show();
        if (generatorDialog.isOK()) {
            generateBuilderPattern(generatorDialog.getFields(), psiClass);
        }
    }

    private void generateBuilderPattern(CollectionListModel<PsiField> fields, PsiClass psiClass) {
        new WriteCommandAction.Simple(psiClass.getProject()) {
            @Override
            protected void run() throws Throwable {
                BuilderClassComposer composer = composer(getProject());
                psiClass.add(composer.builderClass(psiClass, fields.getItems()));
                composer.stepInterfaces(psiClass, fields.getItems()).forEach(stepInterface -> psiClass.add(stepInterface));
            }
        }.execute();
    }

    private BuilderClassComposer composer(Project project) {
        return new BuilderClassComposer(new ElementGenerator(), getElementFactory(project));
    }

    @Override
    public void update(AnActionEvent e) {
        PsiClass psiClass = getPsiClassFromContext(e);
        e.getPresentation().setEnabled(psiClass != null);
    }

    private PsiClass getPsiClassFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement currentElement = psiFile.findElementAt(offset);
        return PsiTreeUtil.getParentOfType(currentElement, PsiClass.class);
    }
}
