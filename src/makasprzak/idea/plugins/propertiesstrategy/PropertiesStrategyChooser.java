package makasprzak.idea.plugins.propertiesstrategy;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

public class PropertiesStrategyChooser {
    public PropertiesStrategy chooseFor(PsiClass pojo) {
        if (hasConstructorsWithArgs(pojo))
            return PropertiesConcreteStrategy.FROM_CONSTRUCTOR_ARGS.get();
        else
            return PropertiesConcreteStrategy.FROM_SETTERS.get();
    }

    private boolean hasConstructorsWithArgs(PsiClass pojo) {
        PsiMethod[] constructors = pojo.getConstructors();
        return constructors != null && constructors.length != 0 && containsConstructorWithArgs(constructors);
    }

    private boolean containsConstructorWithArgs(PsiMethod[] constructors) {
        for (PsiMethod constructor : constructors) {
            if (constructor.getParameterList().getParametersCount() > 0)
                return true;
        }
        return false;
    }
}
