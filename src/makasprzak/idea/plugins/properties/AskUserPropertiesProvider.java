package makasprzak.idea.plugins.properties;

import com.intellij.psi.PsiClass;
import makasprzak.idea.plugins.dialog.DialogFactory;
import makasprzak.idea.plugins.dialog.GeneratorDialog;

public class AskUserPropertiesProvider implements PropertiesProvider{

    private DialogFactory dialogFactory;

    public AskUserPropertiesProvider(DialogFactory dialogFactory) {
        this.dialogFactory = dialogFactory;
    }

    @Override
    public void getProperties(PsiClass psiClass, PropertiesConsumer consumer) {
        GeneratorDialog generatorDialog = dialogFactory.create(psiClass);
        generatorDialog.show();
        if (generatorDialog.isOK())
            consumer.consume(generatorDialog.getProperties());
    }
}
