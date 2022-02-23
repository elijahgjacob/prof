/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {


    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _colName = colName;
        _match = match;
        _input = input;

    }
    @Override
    protected boolean keep() {
        if (_next.getValue(_input.colNameToIndex(_colName)).equals(_match)) {
            return true;
        } else {
            return false;
        }
    }
    public Table _input;
    private String _colName, _match;


}
