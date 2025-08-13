package coffeemaker.exceptions;

import java.io.Serial;


/**
 * A runtime exception for the {@link coffeemaker.domain.Recipe} component.
 *
 * @author Bryan Basham
 */
public class RecipeException extends IllegalArgumentException {

	@Serial
	private static final long serialVersionUID = 1L;

	public RecipeException(String msg) {
		super(msg);
	}

}
