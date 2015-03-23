package makasprzak.idea.plugins.generationstrategy;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.dialog.DialogFactory;
import makasprzak.idea.plugins.dialog.GeneratorDialog;
import makasprzak.idea.plugins.model.Property;

import java.util.List;

public class GenerateFromFieldsCS implements GenerationStrategy {
    private final DialogFactory dialogFactory;
    private GeneratorDialog generatorDialog;

    public GenerateFromFieldsCS(DialogFactory dialogFactory) {
        this.dialogFactory = dialogFactory;
    }

    @Override
    public void start(PsiClass psiClass) {
        this.generatorDialog = dialogFactory.create(psiClass);
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
