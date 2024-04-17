package marco.utils;

public class MyData {
  String name;
  String address;
  int level;

  public MyData(String name, String address, int level) {
    this.name = name;
    this.address = address;
    this.level = level;
  }

  public MyData() {
    this.name = "";
    this.address = "";
    this.level = 0;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public int getLevel() {
    return level;
  }
  public void setLevel(int level) {
    this.level = level;
  }

  public String toString() {
    return new StringBuffer().append(this.name).append(", ").append(this.address).append(", ").append(""+this.level).toString();
  }
}
