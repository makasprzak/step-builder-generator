package makasprzak.idea.plugins;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;

import javax.swing.*;

/**
 * Created by Maciej Kasprzak on 2014-09-21.
 */
public class GeneratorDialog extends DialogWrapper{
    private JList<PsiField> fieldList;
    private final LabeledComponent<JPanel> component;

    protected GeneratorDialog(PsiClass psiClass) {
        super(psiClass.getProject());
        setTitle("Configure Step Builder");
        PsiField[] allFields = psiClass.getAllFields();
        this.fieldList = new JBList(new CollectionListModel<>(allFields));
        this.fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        this.fieldList.setSelectedIndices(range(allFields.length));
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        decorator.disableAddAction();
        decorator.disableRemoveAction();
        JPanel panel = decorator.createPanel();
        component = LabeledComponent.create(panel, "Fields to include in Step Builder:");

        init();
    }

    private int[] range(int size) {
        int[] range = new int[size];
        for (int i = 0; i < range.length; i++) {
            range[i] = i;
        }
        return range;
    }

    @Override
    protected JComponent createCenterPanel() {
        return this.component;
    }

    public java.util.List<PsiField> getFields() {
        return fieldList.getSelectedValuesList();
    }
}
