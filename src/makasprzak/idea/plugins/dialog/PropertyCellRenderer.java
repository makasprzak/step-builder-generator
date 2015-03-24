package makasprzak.idea.plugins.dialog;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import makasprzak.idea.plugins.model.Property;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Maciej on 2015-03-25.
 */
public class PropertyCellRenderer implements ListCellRenderer<Property> {
    private final DefaultPsiElementCellRenderer renderer = new DefaultPsiElementCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList<? extends Property> list, Property value, int index, boolean isSelected, boolean cellHasFocus) {
        return renderer.getListCellRendererComponent(list,value.getType() + " " + value.getName(),index,isSelected,cellHasFocus);
    }
}
