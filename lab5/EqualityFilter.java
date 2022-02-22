/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {
    Table _input;
    String _colName, _match;

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _input = input;
        _colName = colName;
        _match = match;

    }

    @Override
    protected boolean keep() {
        col= _colNametoIndex;
        if (_input.equals(_match)){
            return true;
        }
        return false;
    }

}
