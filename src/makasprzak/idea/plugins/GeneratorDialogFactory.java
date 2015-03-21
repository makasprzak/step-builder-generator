package makasprzak.idea.plugins;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

import java.util.Arrays;

import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;
import static makasprzak.idea.plugins.mappers.PsiFieldMapper.toProperty;

public class GeneratorDialogFactory {
    public GeneratorDialog createFromFields(PsiClass psiClass) {
        return new GeneratorDialog(psiClass, transform(asList(psiClass.getAllFields()), toProperty()));
    }
    public GeneratorDialog createFromConstructor(PsiClass psiClass) {
        PsiMethod[] constructors = psiClass.getConstructors();
        PsiMethod biggestConstructor = null;
        for (PsiMethod constructor : constructors) {
            if (biggestConstructor == null || parametersCount(constructor) > parametersCount(biggestConstructor)) {
                biggestConstructor = constructor;
            }
        }
        PsiParameter[] parameters = biggestConstructor.getParameterList().getParameters();
        //TODO parameters mapper
        return new GeneratorDialog(psiClass, transform(asList(psiClass.getAllFields()), toProperty()));
    }
    
    private int parametersCount(PsiMethod constructor) {
        return constructor.getParameterList().getParametersCount();
    }
}
