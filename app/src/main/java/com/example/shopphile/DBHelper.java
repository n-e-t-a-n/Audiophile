package com.example.shopphile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shopping_cart.db";
    private static final int DATABASE_VERSION = 10;
    private static final String TABLE_CART = "cart";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDER_ITEMS = "order_items";

    // CART COLUMNS
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_BRAND_NAME = "brand_name";
    private static final String COLUMN_PRODUCT_IMAGE = "product_image";

    // ORDERS COLUMNS
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_ORDER_DATE = "order_date";

    // ORDER ITEMS COLUMNS
    private static final String COLUMN_ORDER_ITEM_ID = "order_item_id";
    private static final String COLUMN_ORDER_ITEM_ORDER_ID = "order_id";
    private static final String COLUMN_ORDER_ITEM_PRODUCT_NAME = "order_product_name";
    private static final String COLUMN_ORDER_ITEM_PRODUCT_PRICE = "order_product_price";
    private static final String COLUMN_ORDER_ITEM_QUANTITY = "order_quantity";
    private static final String COLUMN_ORDER_ITEM_BRAND_NAME = "order_brand_name";
    private static final String COLUMN_ORDER_ITEM_PRODUCT_IMAGE = "order_product_image";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_PRODUCT_PRICE + " REAL, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_BRAND_NAME + " TEXT, " +
                COLUMN_PRODUCT_IMAGE + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_DATE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
                COLUMN_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_ITEM_ORDER_ID + " INTEGER, " +
                COLUMN_ORDER_ITEM_PRODUCT_NAME + " TEXT, " +
                COLUMN_ORDER_ITEM_PRODUCT_PRICE + " REAL, " +
                COLUMN_ORDER_ITEM_QUANTITY + " INTEGER, " +
                COLUMN_ORDER_ITEM_BRAND_NAME + " TEXT, " +
                COLUMN_ORDER_ITEM_PRODUCT_IMAGE + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_ORDER_ITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ORDER_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        onCreate(db);
    }

    public void addItemToCart(String productName, double price, int quantity, String brandName, int productImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CART, null, COLUMN_PRODUCT_NAME + " = ?", new String[]{productName}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                quantity += currentQuantity;
                ContentValues values = new ContentValues();
                values.put(COLUMN_QUANTITY, quantity);
                db.update(TABLE_CART, values, COLUMN_PRODUCT_NAME + " = ?", new String[]{productName});
            } else {
                ContentValues values = new ContentValues();
                values.put(COLUMN_PRODUCT_NAME, productName);
                values.put(COLUMN_PRODUCT_PRICE, price);
                values.put(COLUMN_QUANTITY, quantity);
                values.put(COLUMN_BRAND_NAME, brandName);
                values.put(COLUMN_PRODUCT_IMAGE, productImage);
                db.insert(TABLE_CART, null, values);
            }
            cursor.close();
        }
        db.close();
    }

    public void updateItemQuantity(String productName, int quantity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, quantity);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CART, values, COLUMN_PRODUCT_NAME + " = ?", new String[]{productName});
        db.close();
    }

    public double getTotalAmount() {
        double total = 0.0;

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_PRODUCT_PRICE + " * " + COLUMN_QUANTITY + ") FROM " + TABLE_CART, null)) {
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
        }
        return total;
    }

    public void deleteCartItem(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_PRODUCT_NAME + " = ?", new String[]{productName});
        db.close();
    }

    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(TABLE_CART, null, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String productName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                    String brandName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND_NAME));
                    double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                    int productImage = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE));
                    cartItems.add(new CartItem(id, productName, brandName, productPrice, quantity, productImage));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error reading cart items", e);
        }
        return cartItems;
    }

    public double calculatePrice(String productName, int quantity) {
        double price = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{COLUMN_PRODUCT_PRICE}, COLUMN_PRODUCT_NAME + " = ?", new String[]{productName}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
            cursor.close();
        }
        db.close();
        return price * quantity;
    }

    public void addOrder(String orderDate, List<CartItem> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        orderValues.put(COLUMN_ORDER_DATE, orderDate);
        long orderId = db.insert(TABLE_ORDERS, null, orderValues);

        for (CartItem item : cartItems) {
            ContentValues itemValues = new ContentValues();
            itemValues.put(COLUMN_ORDER_ITEM_ORDER_ID, orderId);
            itemValues.put(COLUMN_ORDER_ITEM_PRODUCT_NAME, item.getProductName());
            itemValues.put(COLUMN_ORDER_ITEM_PRODUCT_PRICE, item.getProductPrice());
            itemValues.put(COLUMN_ORDER_ITEM_QUANTITY, item.getQuantity());
            itemValues.put(COLUMN_ORDER_ITEM_BRAND_NAME, item.getBrandName());
            itemValues.put(COLUMN_ORDER_ITEM_PRODUCT_IMAGE, item.getProductImage());
            db.insert(TABLE_ORDER_ITEMS, null, itemValues);
        }
        db.close();
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(TABLE_ORDERS, null, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
                    String orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE));
                    List<OrderItem> orderItems = getOrderItems(orderId);
                    orders.add(new Order(orderId, orderDate, orderItems));
                } while (cursor.moveToNext());
            }
        }
        return orders;
    }

    public List<OrderItem> getOrderItems(long orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_ORDER_ITEMS, null, COLUMN_ORDER_ITEM_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)}, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    String productImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRODUCT_IMAGE));
                    String brandName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_BRAND_NAME));
                    String productName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRODUCT_NAME));
                    double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRODUCT_PRICE));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_QUANTITY));
                    orderItems.add(new OrderItem(productImage, brandName, productName, productPrice, quantity, (int) orderId));
                } while (cursor.moveToNext());
            }
        }
        return orderItems;
    }
}
