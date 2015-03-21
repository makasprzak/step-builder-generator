package makasprzak.idea.plugins.generationstrategy;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.GeneratorDialog;
import makasprzak.idea.plugins.GeneratorDialogFactory;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

public class GenerateFromFieldsCS implements GenerationStrategy {
    private final GeneratorDialogFactory generatorDialogFactory;
    private GeneratorDialog generatorDialog;
    
    public GenerateFromFieldsCS(GeneratorDialogFactory generatorDialogFactory) {
        this.generatorDialogFactory = generatorDialogFactory;
    }
    
    @Override
    public void start(PsiClass psiClass) {
        this.generatorDialog = generatorDialogFactory.createFromFields(psiClass);
        this.generatorDialog.show();
    }

    @Override
    public boolean isOk() {
        return generatorDialog.isOK();
    }

    @Override
    public List<Property> getProperties() {
        return generatorDialog.getProperties();
    }
}
