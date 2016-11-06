package cn.flyexp.entity;

/**
 * Created by txy on 2016/8/23 0023.
 */
public class MineBean  {

    private String name;

    private int img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public MineBean(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public MineBean(){

    }
}
