package ebitza.itemcalculator;

public class Category_model {
    private int _id;
    private String _subject;
    private String _description;
    public Category_model(){

    }
    public Category_model(int id,String subject,String description){
        this._id=id;
        this._subject=subject;
        this._description=description;

    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_subject() {
        return _subject;
    }

    public void set_subject(String _subject) {
        this._subject = _subject;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
