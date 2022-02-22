/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {
    Table _input;
    String _colName, _ref;

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _input = input;
        _colName = colName;
        _ref = ref;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        return false;
    }

    // FIXME: Add instance variables?
}
