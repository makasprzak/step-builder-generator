package makasprzak.idea.plugins;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by Maciej on 2015-03-24.
 */
public class PsiClassAdapter {
    private final PsiClass psiClass;

    public static PsiClassAdapter forA(PsiClass psiClass) {
        return new PsiClassAdapter(psiClass);
    }

    private PsiClassAdapter(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public List<PsiParameter> getBiggestConstructorsArgs() {
        return getArgumentsOf(getBiggestConstructor());
    }

    private PsiMethod getBiggestConstructor() {
        PsiMethod[] constructors = psiClass.getConstructors();
        PsiMethod biggestConstructor = null;
        for (PsiMethod constructor : constructors) {
            if (biggestConstructor == null || parametersCount(constructor) > parametersCount(biggestConstructor)) {
                biggestConstructor = constructor;
            }
        }
        return biggestConstructor;
    }

    private int parametersCount(PsiMethod constructor) {
        return constructor.getParameterList().getParametersCount();
    }

    private List<PsiParameter> getArgumentsOf(PsiMethod biggestConstructor) {
        return asList(biggestConstructor.getParameterList().getParameters());
    }

}
