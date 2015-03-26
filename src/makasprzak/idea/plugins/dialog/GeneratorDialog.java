package makasprzak.idea.plugins.dialog;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import makasprzak.idea.plugins.model.Property;
import makasprzak.idea.plugins.model.PsiPropertyContainer;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.transform;

public class GeneratorDialog extends DialogWrapper{
    private JList properties;
    private final LabeledComponent<JPanel> component;

    protected GeneratorDialog(PsiClass psiClass, List<PsiPropertyContainer> allProperties) {
        super(psiClass.getProject());
        setTitle("Configure Step Builder");
        this.properties = new JBList(new CollectionListModel<PsiPropertyContainer>(allProperties));
        this.properties.setCellRenderer(new PropertyCellRenderer());
        this.properties.setSelectedIndices(range(allProperties.size()));
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
        return transform(getPsiPropertyContainers(), toProperty());
    }

    private List<PsiPropertyContainer> getPsiPropertyContainers() {
        Object[] selectedObjects = properties.getSelectedValues();
        List<Object> selectedObjectList = Arrays.asList(selectedObjects);
        return Lists.transform(selectedObjectList, new Function<Object, PsiPropertyContainer>() {
            @Override
            public PsiPropertyContainer apply(Object o) {
                return (PsiPropertyContainer) o;
            }
        });
    }

    private Function<PsiPropertyContainer, Property> toProperty() {
        return new Function<PsiPropertyContainer, Property>() {
            @Override
            public Property apply(PsiPropertyContainer psiPropertyContainer) {
                return psiPropertyContainer.getProperty();
            }
        };
    }
}
