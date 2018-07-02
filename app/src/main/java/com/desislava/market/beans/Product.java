package com.desislava.market.beans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Desislava on 08-Mar-18.
 */

public class Product implements Serializable {

    private String name;
    private String price;
    private String info;
    private String origin;
    private String imageURL;
    private Bitmap image;
    public byte[] imageByteArray;

    public Product(String name, String price, String info, String origin, String imageURL) {
        this.name = name;
        this.price = price;
        this.info = info;
        this.origin = origin;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", info='" + info + '\'' +
                ", origin='" + origin + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }


    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(name);
        out.writeObject(price);
        out.writeObject(info);
        out.writeObject(origin);
        out.writeObject(imageURL);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.image.compress(Bitmap.CompressFormat.JPEG, 100, stream); //TODO if image is null (NPE occurs) might handle it with a check !!!
        this.imageByteArray = stream.toByteArray();
        out.writeInt(imageByteArray.length);
        out.write(imageByteArray);

    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.name = (String) in.readObject();
        this.price = (String) in.readObject();
        this.info = (String) in.readObject();
        this.origin = (String) in.readObject();
        this.imageURL = (String) in.readObject();

        int imageLength = in.readInt(); //TODO if image is null length will be '0' -> no image stored
        byte[] byteArray = new byte[imageLength];
        int pos = 0;
        do {
            int read = in.read(byteArray, pos, imageLength - pos);

            if (read != -1) {
                pos += read;
            } else {
                break;
            }

        } while (pos < imageLength);

        this.image = BitmapFactory.decodeByteArray(byteArray, 0, imageLength);

    }

}


