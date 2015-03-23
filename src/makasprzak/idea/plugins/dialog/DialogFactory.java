package makasprzak.idea.plugins.dialog;

import com.intellij.psi.PsiClass;

/**
 * @author mkasprzak
 */
public interface DialogFactory {
   GeneratorDialog create(PsiClass psiClass);
}
