package org.rifidi.prototyper.items.handlers;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.rifidi.prototyper.items.model.ItemModel;
import org.rifidi.prototyper.items.view.ItemModelProviderSingleton;


/**
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 *
 */
public class ExportToCSVHandler extends AbstractHandler implements IHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//create a file window
		Shell shell = new Shell(PlatformUI.getWorkbench().getDisplay());
	    FileDialog dialog = new FileDialog(shell, SWT.SAVE);
	    dialog.setFileName("products.csv");
	    String fileName=dialog.open();
	    //if filename is null, the user pressed cancel
	    if(fileName==null){
	    	return null;
	    }
	    StringBuilder sb = new StringBuilder();
	    sb.append("\"tagid\",\"name\"\n");
	    List<ItemModel> items = ItemModelProviderSingleton.getModelProvider().getItems();
	    for(ItemModel item : items){
	    	byte[] tagid = item.getTag().getTag().readId();
	    	char[] encodedTag = Hex.encodeHex(tagid);
	    	sb.append((new String(encodedTag)).toUpperCase());
	    	sb.append(",\""+item.getName()+"\"\n");
	    }
	    
	    try {
			FileWriter fw = new FileWriter(new File(fileName));
			fw.write(sb.toString());
			fw.write('\n');
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return null;
	}
	
	
}
