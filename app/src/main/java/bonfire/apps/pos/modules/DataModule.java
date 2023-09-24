package bonfire.apps.pos.modules;

import java.util.List;

public class DataModule {
    String itemName, itemImage, itemPrice, productDetail, instruction, remarks;
    int itemImag, itemQty, itemID;
    double itemPric, itemAmount;
    int dbid;
    String sno;
    String kotid;
    String kotdetailid;
    String kot_date;

    public String getKot_date() {
        return kot_date;
    }

    public void setKot_date(String kot_date) {
        this.kot_date = kot_date;
    }

    String isComboItem, ComboCat, CCatId, CCategoryName, Combo_Categoryname, Combo_product_id, Combo_product_name;

    String first_order, last_order, table_cost;

    public String getKotid() {
        return kotid;
    }

    public void setKotid(String kotid) {
        this.kotid = kotid;
    }

    public String getKotdetailid() {
        return kotdetailid;
    }

    public void setKotdetailid(String kotdetailid) {
        this.kotdetailid = kotdetailid;
    }

    public String getWaitername() {
        return waitername;
    }

    public void setWaitername(String waitername) {
        this.waitername = waitername;
    }

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    String waitername;
    int notify;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public int getDbid() {
        return dbid;
    }

    public void setDbid(int dbid) {
        this.dbid = dbid;
    }


    public String getFirst_order() {
        return first_order;
    }

    public void setFirst_order(String first_order) {
        this.first_order = first_order;
    }

    public String getLast_order() {
        return last_order;
    }

    public void setLast_order(String last_order) {
        this.last_order = last_order;
    }

    public String getTable_cost() {
        return table_cost;
    }

    public void setTable_cost(String table_cost) {
        this.table_cost = table_cost;
    }

    List<DataModule> combos, combo;

    public String getCombo_product_name() {
        return Combo_product_name;
    }

    public void setCombo_product_name(String combo_product_name) {
        Combo_product_name = combo_product_name;
    }

    public List<DataModule> getCombo() {
        return combo;
    }

    public void setCombo(List<DataModule> combo) {
        this.combo = combo;
    }

    public List<DataModule> getCombos() {
        return combos;
    }

    public void setCombos(List<DataModule> combos) {
        this.combos = combos;
    }


    public String getIsComboItem() {
        return isComboItem;
    }

    public void setIsComboItem(String isComboItem) {
        this.isComboItem = isComboItem;
    }

    public String getComboCat() {
        return ComboCat;
    }

    public void setComboCat(String comboCat) {
        ComboCat = comboCat;
    }

    public String getCCatId() {
        return CCatId;
    }

    public void setCCatId(String CCatId) {
        this.CCatId = CCatId;
    }

    public String getCCategoryName() {
        return CCategoryName;
    }

    public void setCCategoryName(String CCategoryName) {
        this.CCategoryName = CCategoryName;
    }

    public String getCombo_Categoryname() {
        return Combo_Categoryname;
    }

    public void setCombo_Categoryname(String combo_Categoryname) {
        Combo_Categoryname = combo_Categoryname;
    }

    public String getCombo_product_id() {
        return Combo_product_id;
    }

    public void setCombo_product_id(String combo_product_id) {
        Combo_product_id = combo_product_id;
    }

    String tableid, tablename, tableno, is_available;

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getTableno() {
        return tableno;
    }

    public void setTableno(String tableno) {
        this.tableno = tableno;
    }

    public String getIs_available() {
        return is_available;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }

    public int getItemQty() {
        return itemQty;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public double getItemPric() {
        return itemPric;
    }

    public void setItemPric(double itemPric) {
        this.itemPric = itemPric;
    }

    public double getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(double itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemImag() {
        return itemImag;
    }

    public void setItemImag(int itemImag) {
        this.itemImag = itemImag;
    }

    public DataModule() {
    }


    public DataModule(int itemImag) {
        this.itemImag = itemImag;
    }

    public DataModule(String itemName, double itemPrice, int itemImag, int itemID) {
        this.itemName = itemName;
        this.itemPric = itemPrice;
        this.itemImag = itemImag;
        this.itemID = itemID;
    }
}
