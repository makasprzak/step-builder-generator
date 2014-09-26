package makasprzak.idea.plugins;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;

import javax.swing.*;

/**
 * Created by Maciej Kasprzak on 2014-09-21.
 */
public class GeneratorDialog extends DialogWrapper{
    private CollectionListModel<PsiField> fields;
    private final LabeledComponent<JPanel> component;

    protected GeneratorDialog(PsiClass psiClass) {
        super(psiClass.getProject());
        setTitle("Configure Step Builder");
        this.fields = new CollectionListModel<PsiField>(psiClass.getAllFields());
        JList<PsiField> fieldList = new JList(fields);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        decorator.disableAddAction();
        JPanel panel = decorator.createPanel();
        component = LabeledComponent.create(panel, "Fields to include in Step Builder:");

        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        return this.component;
    }

    public CollectionListModel<PsiField> getFields() {
        return fields;
    }
}
