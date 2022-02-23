/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {


    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _input = input;
        _colName = colName;
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
        int x = _input.colNameToIndex(_colName);
        if (_next.getValue(x).contains(_subStr)) {
            return true;
        } else {
            return false;
        }
    }
    Table _input;
    String _colName, _subStr;
}
