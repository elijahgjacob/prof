/**
 * TableFilter which does not filter out any rows.
 *
 * @author Matthew Owen
 */
public class IdentityFilter extends TableFilter {
    Table _input;

    public IdentityFilter(Table input) {
        super(input);
        _input = input;

    }

    @Override
    protected boolean keep() {
        return true;
    }
}
