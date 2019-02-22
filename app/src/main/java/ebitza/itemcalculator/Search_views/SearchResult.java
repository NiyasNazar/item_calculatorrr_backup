package ebitza.itemcalculator.Search_views;

public class SearchResult  {
        private String title;
        private String description;
        private String iconUrl;
        private String item_id;
    private String item_price;
    private String item_Quantity;
    private byte[] _img;




    public SearchResult() {
            this.title = title;
            this.description = description;
            this.iconUrl = iconUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_Quantity() {
        return item_Quantity;
    }

    public void setItem_Quantity(String item_Quantity) {
        this.item_Quantity = item_Quantity;
    }

    public byte[] get_img() {
        return _img;
    }

    public void set_img(byte[] _img) {
        this._img = _img;
    }
}