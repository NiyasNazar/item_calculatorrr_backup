package ebitza.itemcalculator.Models;

public class Model_category_item {
    private String item_id;
  private   String Itemname;
     private String Itemprice;
    private byte[] _img;
    private String  itemquantity;
    public Model_category_item(){

    }

    public String getItemname() {
        return Itemname;
    }

    public void setItemname(String itemname) {
        Itemname = itemname;
    }

    public String getItemprice() {
        return Itemprice;
    }

    public void setItemprice(String itemprice) {
        Itemprice = itemprice;
    }

    public byte[] get_img() {
        return _img;
    }

    public void set_img(byte[] _img) {
        this._img = _img;
    }

    public String getItemquantity() {
        return itemquantity;
    }

    public void setItemquantity(String itemquantity) {
        this.itemquantity = itemquantity;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
