package coffeemaker.exceptions;

import java.io.Serial;


/**
 * A runtime exception for the {@link coffeemaker.domain.Inventory} component.
 *
 * @author Bryan Basham
 */
public class InventoryException extends IllegalArgumentException {

	@Serial
	private static final long serialVersionUID = 1L;

	public InventoryException(String msg) {
		super(msg);
	}

}
