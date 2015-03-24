package makasprzak.idea.plugins.propertiesstrategy;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

public interface PropertiesStrategy {
    void start(PsiClass psiClass);
    boolean isOk();
    List<Property> getProperties();
}
