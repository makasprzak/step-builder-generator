package makasprzak.idea.plugins.properties;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

public class PropertiesProviderFactory {
    public PropertiesProvider createFor(PsiClass pojo) {
        if (hasConstructorsWithArgs(pojo))
            return PropertiesProviderFactories.FROM_CONSTRUCTOR_ARGS.get();
        else
            return PropertiesProviderFactories.FROM_SETTERS.get();
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
