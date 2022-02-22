/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {
    Table _input;
    String _colName1,_colName2;

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _input = input;
        _colName1 = colName1;
        _colName2 = colName2;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        return false;
    }

    // FIXME: Add instance variables?
}
