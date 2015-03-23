package makasprzak.idea.plugins.dialog;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import makasprzak.idea.plugins.mappers.PsiFieldMapper;
import makasprzak.idea.plugins.model.Property;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;
import static makasprzak.idea.plugins.mappers.PsiFieldMapper.toProperty;

/**
 * Created by Maciej Kasprzak on 2014-09-21.
 */
public class GeneratorDialog extends DialogWrapper{
    private JList<Property> properties;
    private final LabeledComponent<JPanel> component;

    protected GeneratorDialog(PsiClass psiClass, List<Property> allProperties) {
        super(psiClass.getProject());
        setTitle("Configure Step Builder");
        this.properties = new JBList(new CollectionListModel<>(allProperties));
        this.properties.setCellRenderer(new DefaultPsiElementCellRenderer());
        this.properties.setSelectedIndices(range(allProperties.size()));
        properties.setCellRenderer(new DefaultPsiElementCellRenderer());
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(properties);
        decorator.disableAddAction();
        decorator.disableRemoveAction();
        JPanel panel = decorator.createPanel();
        component = LabeledComponent.create(panel, "Properties to include in Step Builder:");

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

    public List<Property> getProperties() {
        return properties.getSelectedValuesList();
    }
}
