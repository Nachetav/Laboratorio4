public class InventoryItem {
    private int id;
    private String name;
    private String category;
    private String location;
    private int stockTotal;
    private int stockAvailable;
    private int stockOnLoan;

    public InventoryItem (int id, String name, String category, String location, int stockTotal, int stockAvailable, int stockOnLoan) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.location = location;
        this.stockTotal = stockTotal;
        this.stockAvailable = stockAvailable;
        this.stockOnLoan = stockOnLoan;
    }
    public int getId() {return id;};
    public String getName() {return name;};
    public String getCategory() {return category;};
    public String getLocation() {return location;};
    public int getStockTotal() {return stockTotal;};
    public int getStockAvailable() {return stockAvailable;};
    public int getStockOnLoan() {return stockOnLoan;};

    //NO STOCK NEGATIVO, NO PRESTAR MAS DE LO DISPONIBLE, NO DEVOLVER MAS DE LO PRESTADO (osea, OnLoan < Total)
    public void addStock (int itemId, int quantity) { //aumentar total y available
        if(id == itemId && quantity > 0) { //pequeño if para darle un uso al ID, ya que no es necesario "identificar"
            stockTotal += quantity;
            stockAvailable += quantity;
        }
    }

    public boolean lend(int itemId, int quantity) { //disminuir available y aumentar onloan, true si fue prestado, false si no
        if(id == itemId && quantity > 0 && quantity <= stockAvailable) {
            stockAvailable -= quantity;
            stockOnLoan += quantity;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean receive(int itemId, int quantity) { //aumentar available y disminuir onloan
        if(id == itemId && quantity > 0 && quantity <= stockOnLoan) {
            stockAvailable += quantity;
            stockOnLoan -= quantity;
            return true;
        }
        else {
            return false;
        }
    }

}
