package makasprzak.idea.plugins.mappers;

import makasprzak.idea.plugins.model.Property;

/**
 * @author mkasprzak
 */
public interface PsiMapper<T> {
   Property map(T psiEntity);
}
