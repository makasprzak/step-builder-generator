package makasprzak.idea.plugins.generationstrategy;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

public interface GenerationStrategy {
    void start(PsiClass psiClass);
    boolean isOk();
    List<Property> getProperties();
}
