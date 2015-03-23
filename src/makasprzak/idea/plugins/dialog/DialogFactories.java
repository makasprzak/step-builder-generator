package makasprzak.idea.plugins.dialog;

/**
 * @author mkasprzak
 */
public enum DialogFactories {
   FROM_FIELDS(new FromFieldsDialogFactory()),
   FROM_CONSTRUCTOR(new FromConstructorDialogFactory());
   private final DialogFactory dialogFactory;

   private DialogFactories(DialogFactory dialogFactory) {

      this.dialogFactory = dialogFactory;
   }

   public DialogFactory get(){
      return this.dialogFactory;
   }
}
