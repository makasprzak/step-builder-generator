package makasprzak.idea.plugins.properties;

import com.intellij.psi.PsiClass;

public interface PropertiesProvider {
    void getProperties(PsiClass psiClass, PropertiesConsumer consumer);
}
